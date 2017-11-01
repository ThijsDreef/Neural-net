package game.GameObjects;

import game.NeuralNet.Net;

/**
 * Created by Thijs Dreef on 18/10/2017.
 */
public class LoadNetWorkThread implements Runnable
{
  String file;
  Net net;
  public LoadNetWorkThread(String file, Net net)
  {
    this.file = file;
    this.net = net;
  }
  @Override
  public void run()
  {
    net.load(file);
  }
}
