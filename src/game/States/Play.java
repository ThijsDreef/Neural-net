package game.States;

import engine.Engine;
import engine.Renderer;
import game.GameObjects.ImageRecognizer;
import game.GameObjects.NeuralNetController;
import game.NeuralNet.ImageLoader;
import game.managers.ObjectManager;
import game.managers.State;


public class Play extends State
{
  public Play()
  {
    manager = new ObjectManager();
    ImageLoader loader = new ImageLoader();
    float input[][] = new float[][] { {0, 0}, {0, 1}, {1, 1}, {1, 0} };
    float target[][] = new float[][] { {0}, {1}, {0}, {1}};
//    float input[][] = loader.loadImages(new File("./src/engine/resources/trainingData/").listFiles());
//    float target[][] = new float[][] { {0, 0, 0, 1}, {0, 1, 0, 0}, {1, 0, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}};
//    float data[][][] = loader.loadTrainingData(new File("./src/engine/resources/trainingData/appleTraining.txt"));
//    float input[][] = data[0];
//    float target[][] = data[1];
//    float input[][] = { loader.loadImage(new File("./src/engine/resources/trainingData/lineSample0.png")), loader.loadImage(new File("./src/engine/resources/trainingData/lineSample1.png")),
//                        loader.loadImage(new File("./src/engine/resources/trainingData/lineSample2.png")), loader.loadImage(new File("./src/engine/resources/trainingData/lineSample3.png"))};
//    float target[][] = {loader.loadImage(new File("./src/engine/resources/trainingData/resultSample0.png")), loader.loadImage(new File("./src/engine/resources/trainingData/resultSample1.png")),
//            loader.loadImage(new File("./src/engine/resources/trainingData/resultSample2.png")), loader.loadImage(new File("./src/engine/resources/trainingData/resultSample3.png"))};
    manager.addObject(new NeuralNetController(manager, 20, 20, input, target, 3, 5, 0.0005f, new ImageRecognizer()));

  }
  @Override
  public void update(Engine en, float dt)
  {
    manager.updateObjects(en, dt);
    en.getCamera().addTransy(en.getInput().getMouseScroll() * 3);
  }

  @Override
  public void render(Engine en, Renderer r)
  {
    manager.renderObjects(en, r);
  }

  @Override
  public void dispose()
  {

  }
}
