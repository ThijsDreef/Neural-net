package engine.Fx;

import java.io.Serializable;

public class Light implements Serializable
{
  public int color, radius, diameter;
  public int[] lightArray;

  public Light(int color, int radius, double intensity) {
    this.color = color;
    this.radius = radius;
    this.diameter = radius * 2;

    this.color = color;
    this.radius = radius;
    this.diameter = radius * 2;

    lightArray = new int[diameter * diameter];

    for (int x = 0; x < diameter; x++)
    {
      for (int y = 0;y < diameter; y++)
      {
        float distance = (float)Math.sqrt((x - radius) *  (x - radius) + (y - radius) * (y - radius));

        if (distance < radius)
        {
          lightArray[x + y * diameter] = Pixel.getColorPower(color, 1 - distance / radius);
        }
        else
        {
          lightArray[x + y * diameter] = 0;
        }
      }
    }
//    lightArray = new int[(diameter * diameter * 2)];
//    int x, dx, dy ;
//    int y;
//    int offx, offy;
//    double distance;
//      x = 0;
//      y = radius;
//      distance = 5.0 / 4.0 - radius;
//      while (y >= x) {
//        if (distance < 0)
//          distance += 2.0 * x + 3.0;
//        else {
//          distance += 2.0 * (x - y) + 5.0;
//          y--;
//        }
//        x++;
//        offx = 0;
//        offy = 0;
//        lightArray[x + offx + radius - 1 + (radius + y + offy - 1) * diameter] = color; // done
//        lightArray[(-x + offx + radius) + (radius + y + offy - 1) * diameter] = color; // done
//        lightArray[(x + offx +radius - 1) + (radius+ -y + offy) * diameter] = color; // done
//        lightArray[(-x + offx + radius) + (radius + -y + offy) * diameter] = color; // done
//        lightArray[y + offx + radius - 1 + (radius + x - 1 + offy) * diameter] = color; // done
//        lightArray[(y + offx + radius - 1) + (radius + -x + offy) * diameter] = color; // done
//        lightArray[(-y + offx + radius) + (radius + x + offy) * diameter] = color; // done
//        lightArray[((-y + offx + radius) + (radius + -x + 1 + offy) * diameter)] = color; // done
//
//      }
//    boolean fill = false;
//    int diameter2=diameter*diameter;
//    for (int filly = 1; filly < diameter - 1; filly++ )
//    {
//      for (int fillx = 1; fillx < diameter; fillx++)
//      {
//        dx = fillx - radius;
//        dy = filly - radius;
//        float dxdy = (dx * dx + dy * dy);
//        double power = intensity - dxdy / (diameter2); // is nu tussen 0 en 1.
//        if (lightArray[fillx + filly * diameter] == color && lightArray[(fillx + 1) + filly * diameter] == 0)
//        {
//          fill = !fill;
//        }
//        if (lightArray[fillx + filly * diameter] != color && fill) {
//          if (fillx <= radius)
//            lightArray[fillx + (filly * diameter)] = Pixel.getColorPower(color, (float)power);
//          if (fillx > radius) {
//            lightArray[fillx + filly * diameter] = Pixel.getColorPower(color, (float)power);
//          }
//        }
//      }
//    }
//    for (int looperd = 0; looperd < lightArray.length; looperd++) {
//      if (lightArray[looperd] == color)
//        lightArray[looperd] = 0;
//    }
  }
  public int getLightValue(int x, int y)
  {
    if (x < 0 || x >= diameter || y < 0 || y >=diameter)
      return 0xff000000;
    return lightArray[x + y * diameter];
  }
}
