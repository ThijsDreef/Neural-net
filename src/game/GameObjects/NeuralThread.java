package game.GameObjects;

import game.NeuralNet.Net;

/**
 * Created by Thijs Dreef on 13/10/2017.
 */
public class NeuralThread implements Runnable
{
  float minEpoch;
  Net net;
  float[][] input;
  float[][] target;
  public NeuralThread(Net net, float[][] input, float[][] target, float minEpoch)
  {
    this.net = net;
    this.input = input;
    this.target = target;
    this.minEpoch = minEpoch;
  }
  @Override
  public void run()
  {
    net.train(input, target, minEpoch);
  }
}
