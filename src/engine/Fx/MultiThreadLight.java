package engine.Fx;

import engine.Renderer;

public class MultiThreadLight extends Thread
{
  private Light light;
  private int size, maxSize, x, y;
  private Renderer r;
  public MultiThreadLight(Light light, int size,int maxSize, int x, int y, Renderer r)
  {
    this.light = light;
    this.size = size;
    this.maxSize = maxSize;
    this.x = x;
    this.y = y;
    this.r = r;
  }

  @Override
  public void run()
  {
    super.run();
    for (int i = size; i < maxSize; i++)
    {
      r.drawLightLine(light.radius, light.radius, i, 0, light, x, y);
      r.drawLightLine(light.radius, light.radius, i, light.diameter, light, x, y);
      r.drawLightLine(light.radius, light.radius, 0, i, light, x, y);
      r.drawLightLine(light.radius, light.radius, light.diameter, i, light, x, y);
    }
  }
}
