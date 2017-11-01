package engine.Fx;

/**
 * Created by Thijs Dreef on 10/03/2017.
 */
public class LightRequest
{
  public Light light;
  public int x, y;
  public LightRequest(int x, int y, Light light)
  {
    this.light = light;
    this.x = x;
    this.y = y;
  }
}
