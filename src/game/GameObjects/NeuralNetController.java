package game.GameObjects;

import engine.Engine;
import engine.Renderer;
import game.NeuralNet.Net;
import game.managers.GameObject;
import game.managers.ObjectManager;

import java.awt.event.KeyEvent;

public class NeuralNetController extends GameObject
{
  int maxDepth;
  int maxNeuronsPerLayer;
  int input;
  int outputa;
  NeuralNetTrainer trainers[] = new NeuralNetTrainer[4];
  public NeuralNetController(ObjectManager manager, int x, int y, float[][] input, float[][] target, int maxDepth, int maxNeuronsPerLayer, float expectedError, NeuralNetUser user)
  {
    this.maxDepth = maxDepth;
    this.maxNeuronsPerLayer = maxNeuronsPerLayer;
    this.input = input[0].length;
    this.outputa = target[0].length;
    int[] topology = new int[maxDepth];
    topology[0] = input[0].length;
    for (int i = 1; i < maxDepth - 1; i++)
      topology[i] = (int)(maxNeuronsPerLayer * Math.random());
    topology[maxDepth - 1] = target[0].length;
    for (int i = 0; i < 4; i++)
    {
      trainers[i] = new NeuralNetTrainer(new Net(topology), input, target, expectedError, x, y + 50 + 100 * i, user);
      manager.addObject(trainers[i]);
    }

  }
  @Override
  public void update(Engine en, float dt)
  {
    if (en.getInput().isKeyReleased(KeyEvent.VK_R))
    {
      for (int i = 0; i < trainers.length;i ++)
        if (trainers[i].active)
          trainers[i].restart(maxDepth, maxNeuronsPerLayer, input, outputa);
    }
  }

  @Override
  public void render(Engine en, Renderer r)
  {

  }

  @Override
  public void componentEvent(String name, GameObject object, String axis)
  {

  }

  @Override
  public void dispose()
  {

  }
}
