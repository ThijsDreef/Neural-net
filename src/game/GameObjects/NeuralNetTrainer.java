package game.GameObjects;

import engine.Engine;
import engine.Fx.Pixel;
import engine.Fx.ShadowType;
import engine.Renderer;
import game.NeuralNet.Net;
import game.managers.GameObject;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Arrays;

public class NeuralNetTrainer extends GameObject
{
  private Net net;
  private NeuralThread nThread;
  private Thread aThread;
  boolean active = false;
  private float [][] input;
  private float [][] target;
  private int inputSelector = 0;
  private String lastRun;
  float epoch;
  Thread lThread;
  private NeuralNetUser user;
  public NeuralNetTrainer(Net net, float[][] input, float[][] target, float epochError, int x, int y, NeuralNetUser user)
  {
    lThread = new Thread();
    this.user = user;
    this.input = input;
    this.target = target;
    this.epoch = epochError;
    this.x = x;
    this.y = y;
    this.net = net;
    nThread = new NeuralThread(net, input, target, epochError);
    aThread = new Thread(nThread);
    aThread.start();
  }
  @Override
  public void update(Engine en, float dt)
  {
    if (en.getInput().isKeyReleased(KeyEvent.VK_UP))
      inputSelector++;
    if (en.getInput().isKeyReleased(KeyEvent.VK_DOWN))
      inputSelector--;
    inputSelector = (inputSelector < 0) ? input.length - 1 : (inputSelector >= input.length) ? 0 : inputSelector;
    if (en.getInput().getMouseButtonReleased(1))
    {
      active = false;

      if (en.getInput().getMouseX() > x && en.getInput().getMouseX() < x + 300 && en.getInput().getMouseY() > y && en.getInput().getMouseY() < y + 40)
      {
        active = true;
        if (user != null && !lThread.isAlive())
        {
          //interupt thread and wait till its done then proceed
          aThread.interrupt();
          while (aThread.isAlive()) ;
          //feedforward the input the user wants to test
          net.feedForward(input[inputSelector]);
          //save the results in a string formated by the user
          lastRun = user.useResult(net.getResults());
          //restart the thread
          aThread = new Thread(nThread);
          aThread.start();
        }
      }
    }
    if (en.getInput().isKeyReleased(KeyEvent.VK_S) && active)
      net.save();
    if (en.getInput().isKeyReleased(KeyEvent.VK_L) && active)
    {
      File folder = new File(".");
      String [] items = folder.list();
      String choice =
              (String) JOptionPane.showInputDialog(
              en.getWindow().getFrame(),
              "select the NN to load",
              "nn loader",
              JOptionPane.PLAIN_MESSAGE,
              null,
              items, "select a nn please");
      aThread.interrupt();
      try
      {
        aThread.join();
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
      lThread = new Thread(new LoadNetWorkThread(choice, net));
      lThread.start();
    }
    if (!aThread.isAlive() && !lThread.isAlive())
    {
      nThread = new NeuralThread(net, input, target, epoch);
      aThread = new Thread(nThread);
      aThread.start();
    }

  }

  @Override
  public void render(Engine en, Renderer r)
  {
    if (lThread.isAlive())
    {
      r.drawLargeString("loading: %" + net.loadPercentage, 0xff000000, (int)x, (int)y, ShadowType.FADE);
      r.drawRect((int)x + 100,(int)y, 102, 14, 0xff000000, ShadowType.FADE);
      r.drawRect((int)x + 101,(int)y + 1, net.loadPercentage, 12, 0xff00ff00, ShadowType.FADE);
      return;
    }
    //todo need to refactor badly
    r.drawLargeString("topology is : " + Arrays.toString(net.topology), 0xff000000, en.getCamera().getTransx() + (int)x, en.getCamera().getTransy() + (int)y, ShadowType.FADE);
    r.drawLargeString("time running = " + (net.runtime / 60) + ":" + (net.runtime % 60) + String.format("  error is: %f", net.visualEpoch), 0xff000000, (int)x + en.getCamera().getTransx(), (int)y + 20 + en.getCamera().getTransy(), ShadowType.FADE);
    if (active)
    {
      r.drawLargeString("input selected: " + inputSelector + "\n" + lastRun, 0xff000000, en.getCamera().getTransx() + (int)x,en.getCamera().getTransy() + en.getHeight() - 48, ShadowType.FADE);
      int offy = 0;
      for (int layernum = 0; layernum < net.layers.length; layernum++)
      {
        int size = Math.round((1280 - 350) / net.layers[layernum].neurons.length);
        size = (size > 32) ? 32 : size;
        float deltaLayer = 0;
        if (size > 2)
        {
          for (int i = 0; i < net.layers[layernum].neurons.length; i++)
          {
            float deltaNeuron = 0;
            r.drawRect(350 + i * size, 100 + offy, size, size, ((i % 2) == 1) ? 0xff000000 : 0xffffffff, ShadowType.FADE);
            if (layernum == net.layers.length - 1)
              continue;
            int nextlayerSize = Math.round(1280 - 350) / net.layers[layernum + 1].neurons.length;
            nextlayerSize = (nextlayerSize > 32) ? 32 : nextlayerSize;
            final float[] weigths = net.layers[layernum].neurons[i].getOutputWeights();
            final float[] deltaWeights = net.layers[layernum].neurons[i].getDeltaWeights();

            for (int j = 0; j < weigths.length; j++)
            {
              r.drawLine(350 + i * size + size / 2, 100 + offy + size / 2, 350 + j * nextlayerSize + nextlayerSize / 2, 100 + offy + size + 128 + nextlayerSize / 2, Pixel.getColor(1, weigths[j], weigths[j], weigths[j]));
              deltaNeuron += deltaWeights[j] / weigths.length;
            }
            deltaLayer += deltaNeuron / net.layers[layernum].neurons.length;
          }
        }
        else
          r.drawLargeString("layer to big to draw", 0xff000000, 350, 100 + offy, ShadowType.FADE);
        r.drawLargeString(String.format("delta error is : %f", deltaLayer), 0xff000000, (en.getCamera().getTransx() + (int)x), en.getCamera().getTransy() + (int)y + 32 + 12 * layernum, ShadowType.FADE);
        offy += size + 128;
      }
    }
  }

  @Override
  public void componentEvent(String name, GameObject object, String axis)
  {

  }

  @Override
  public void dispose()
  {

  }
  public void restart(int maxLayerDepth, int maxNeurons, int inputs, int outputs)
  {
    int depth = 2 + (int)(Math.random() * maxLayerDepth);
//    depth = (depth < 2) ? 2 : depth;
    int topology[] = new int[depth];
    topology[0] = inputs;
    topology[depth - 1] = outputs;
    for (int i = 1; i < depth - 1; i++)
      topology[i] = 1 + (int)(Math.random() * maxNeurons);
    aThread.interrupt();
    net = new Net(topology);
    nThread = new NeuralThread(net, input, target, epoch);
    aThread = new Thread(nThread);
    aThread.start();
  }
}
