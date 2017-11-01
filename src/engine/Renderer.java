package engine;

import engine.Fx.*;

import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Arrays;

public class Renderer {
  private Engine en;
  public int backgroundColor = 0xff626262;
  private int width, height;
  private int[] pixels;
  private int[] lightMap;
  private Font font;
  private Font largeFont;
  private ShadowType[] shadowMap;
  ArrayList<LightRequest> lr = new ArrayList<>();
  public int ambient = 0xff777777;

  public Renderer(Engine en)
  {
    this.en = en;
    width = en.getWidth();
    height = en.getHeight();
    pixels = ((DataBufferInt) en.getWindow().getImage().getRaster().getDataBuffer()).getData();
    lightMap = new int[pixels.length];
    font = new Font("resources/standard.png");
    largeFont = new Font("resources/large.png");
    shadowMap = new ShadowType[pixels.length];
  }
  public void setPixelswithoutTranslate(int x, int y, int color, ShadowType lightblock) {
    if (x < 0 || x >= width || y < 0 || y >= height || color == 0xffff00ff)
      return;
    pixels[x + y * width] = color;
    shadowMap[x + y * width] = lightblock;
  }
  public void setPixels(int x, int y, int color, ShadowType lightblock) {
    x -= en.getCamera().getTransx();
    y -= en.getCamera().getTransy();

    if (x < 0 || x >= width || y < 0 || y >= height || color == 0xffff00ff)
      return;
    pixels[x + y * width] = color;
    shadowMap[x + y * width] = lightblock;
  }

  public ShadowType getLightBlock(int x, int y) {
    x -= en.getCamera().getTransx();
    y -= en.getCamera().getTransy();

    if ((x < 0 || x >= width || y < 0 || y >= height)) {
      return ShadowType.TOTAL;
    }
    return shadowMap[x + y * width];
  }

  public void setLightMap(int x, int y, int color) {
    x -= en.getCamera().getTransx();
    y -= en.getCamera().getTransy();
    if (x < 0 || x >= width|| y < 0|| y >= height)
      return;
    lightMap[x + y * width] = Pixel.getMaxLight(color, lightMap[x + y * width]);
  }

  public void FlushMaps() {
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        setPixelswithoutTranslate(x, y, Pixel.getLightBlend(pixels[x + y * width], lightMap[x + y * width], ambient), ShadowType.NONE);
      }
    }
    Arrays.fill(lightMap, ambient);
  }

  public void drawString(String text, int color, int offx, int offy) {
    text = text.toUpperCase();
    int offset = 0;
    for (int i = 0; i < text.length(); i++) {
      int unicode = text.codePointAt(i) - 31;

      for (int x = 0; x < font.widths[unicode]; x++) {
        for (int y = 0; y < font.standard.height; y++) {
          if (font.standard.pixels[(x + font.offsets[unicode]) + y * font.standard.width] == 0xffffffff)
            setPixels(x + offx + offset, y + offy - 1, color, shadowMap[x + y * width]);

        }
      }
      offset += font.widths[unicode];
    }
  }

  public void drawLargeString(String text, int color, int offx, int offy, ShadowType shadowType)
  {
    text = text.toUpperCase();
    int offset = 0;
    for (int i = 0; i < text.length(); i++)
    {
      int unicode = text.codePointAt(i) - 31;
      if (unicode == -21)
      {
        offy += 12;
        offset = 0;
        continue;
      }
      for (int x = 0; x < largeFont.widths[unicode]; x++)
      {
        for (int y = 0; y < largeFont.standard.height; y++)
        {
          if (largeFont.standard.pixels[(x + largeFont.offsets[unicode]) + y * largeFont.standard.width] == 0xffffffff)
            setPixels(x + offx + offset, y + offy - 1, color, shadowType);
        }
      }
      offset += largeFont.widths[unicode];
    }
  }

  public void drawImage(Image image, int offx, int offy)
  {
    if (offx + width -en.getCamera().getTransx() < 0 && offx - en.getCamera().getTransx() > this.width)
      return;
    if (offy - en.getCamera().getTransy() > this.height && offy + height- en.getCamera().getTransy() < 0)
      return;
    for (int x = 0; x < image.width; x++)
    {
      for (int y = 0; y < image.height; y++)

      {
        setPixels(x + offx, y + offy, image.pixels[x + y * image.width], shadowMap[x + y * width]);
      }
    }
  }

  public void drawLine (int x0, int y0, int x1, int y1, int color)
  {
//    //todo fix y / x reversing
//    float slope = ((float)y1 - y0) / ((float)x1 - x0);
//    int length = Math.abs(x0 - x1);
//    float y = y0;
//    for (int x = 0; x < length; x++)
//    {
//      setPixels(x0 + ((x0 > x1) ? -x : x), (int)y, color, ShadowType.HALF);
//      y += (x0 > x1) ? -slope: slope;
//    }
    int d = 0;

    int dy = Math.abs(y1 - y0);
    int dx = Math.abs(x1 - x0);

    int dy2 = (dy << 1); // slope scaling factors to avoid floating
    int dx2 = (dx << 1); // point

    int ix = x0 < x1 ? 1 : -1; // increment direction
    int iy = y0 < y1 ? 1 : -1;

    if (dy <= dx) {
      for (;;) {
        setPixels(x0, y0, color, ShadowType.HALF);
        if (x0 == x1)
          break;
        x0 += ix;
        d += dy2;
        if (d > dx) {
          y0 += iy;
          d -= dx2;
        }
      }
    } else {
      for (;;) {
        setPixels(x0, y0, color, ShadowType.HALF);

        if (y0 == y1)
          break;
        y0 += iy;
        d += dx2;
        if (d > dy) {
          x0 += ix;
          d -= dy2;
        }
      }
    }
  }

  public void drawCircle(int offx, int offy, int radius, int color, ShadowType shadowType) {
    int[] maxfill = new int[(int) (radius + 1)];
    int x = 0;
    int y = radius;
    double d = 5.0 / 4.0 - radius;
    setPixels(x + offx, y + offy, color, shadowType);
    setPixels(-x + offx, -y + offy, color, shadowType);
    setPixels(y + offx, x + offy, color, shadowType);
    setPixels(-y + offx, -x + offy, color, shadowType);
    while (y >= x) {
      if (d < 0)
        d += 2.0 * x + 3.0; // select east
      else {
        d += 2.0 * (x - y) + 5.0;
        y--;
      }
      x++;
      maxfill[y] = x;

      setPixels(x + offx, y + offy, color, shadowType);
      setPixels(-x + offx, -y + offy, color, shadowType);
      setPixels(-x + offx, y + offy, color, shadowType);
      setPixels(x + offx, -y + offy, color, shadowType);
      setPixels(y + offx, x + offy, color, shadowType);
      setPixels(-y + offx, -x + offy, color, shadowType);
      setPixels(y + offx, -x + offy, color, shadowType);
      setPixels(-y + offx, x + offy, color, shadowType);
    }
    y = radius;
    while (maxfill[y] != 0) {
      for (x = -maxfill[y]; x < maxfill[y]; x++) {
        setPixels(x + offx, y + offy, color, shadowType);
        setPixels(x + offx, -y + offy, color, shadowType);
        setPixels(y + offx, x + offy, color, shadowType);
        setPixels(-y + offx, x + offy, color, shadowType);
      }
      y--;
    }
    int vierkantStart = maxfill[y + 1];
    drawRect(offx - vierkantStart, offy - vierkantStart, vierkantStart * 2, vierkantStart * 2, color, shadowType);

  }

  public void clear()
  {
    Arrays.fill(pixels, backgroundColor);
  }

  public void drawRect(int offx, int offy, int width, int height, int color, ShadowType shadow)
  {
    if (offx + width -en.getCamera().getTransx() < 0 && offx - en.getCamera().getTransx() > this.width)
      return;
    if (offy - en.getCamera().getTransy() > this.height && offy + height- en.getCamera().getTransy() < 0)
      return;
    for (int x = 0; x < width; x++)
    {
      for (int y = 0; y < height; y++)
      {
        setPixels(x + offx, y + offy, color, shadow);
      }
    }
  }

  public synchronized void drawLight(Light light, int x, int y)
  {
    if (en.isLights() || en.isDynamicLights())
      lr.add(new LightRequest(x, y, light));
  }

  public void drawLightArray()
  {
    for (int i = 0; i < lr.size(); i++)
    {
      // only draw lights inside of screen
      if (lr.get(i).x + lr.get(i).light.radius / 2  - en.getCamera().getTransx() < 0 || lr.get(i).x - lr.get(i).light.radius / 2 - en.getCamera().getTransx() < en.getWidth())
        drawLightRequests(lr.get(i).light, lr.get(i).x, lr.get(i).y);
    }
    lr.clear();
  }

  private void drawLightRequests(Light light, int x, int y) {
    if (en.isDynamicLights())
    {
//      default way to render dynamic lights
//      for (int i = 0; i < light.diameter + 1; i++)
//      {
//        drawLightLine(light.radius, light.radius, i, 0, light, x, y);
//        drawLightLine(light.radius, light.radius, i, light.diameter, light, x, y);
//        drawLightLine(light.radius, light.radius, 0, i, light, x, y);
//        drawLightLine(light.radius, light.radius, light.diameter, i, light, x, y);
//      }
//      multiThreaded way to render dynamic lights
      ThreadGroup tg = new ThreadGroup("main");
      tg.setMaxPriority(Thread.MAX_PRIORITY);
      MultiThreadLight object1 = new MultiThreadLight(light, 0, light.diameter / 3, x, y, this);
      MultiThreadLight object2 = new MultiThreadLight(light, light.diameter / 3, light.diameter / 3 * 2, x, y, this);
      MultiThreadLight object3 = new MultiThreadLight(light, light.diameter / 3 * 2, light.diameter + 1, x, y, this);
      object1.start();
      object2.start();
      object3.start();
      while (object1.isAlive() || object2.isAlive() || object3.isAlive()) {}
    }
    else
    {
      for (int offX = 0; x < light.diameter; x++)
      {
        for (int offY = 0; y < light.diameter; y++)
        {
          setLightMap(x + offX - light.radius, y + offY - light.radius, light.getLightValue(x, y));
        }
      }
    }
  }

  public void drawLightLine(int x0, int y0, int x1, int y1, Light light, int offX, int offY) {
    int dx = Math.abs(x1 - x0);
    int dy = Math.abs(y1 - y0);

    int sx = x0 < x1 ? 1 : -1;
    int sy = y0 < y1 ? 1 : -1;

    int err = dx - dy;
    int e2;

    float power = 1;
    boolean hit = false;

    while (true) {
      if (light.getLightValue(x0, y0) == 0xff000000) break;
      int screenX = x0 - light.radius + offX;
      int screenY = y0 - light.radius + offY;

      if (x0 == x1 && y0 == y1) {
        break;
      }
      if (getLightBlock(screenX, screenY) == ShadowType.TOTAL) {
        break;
      }
      if (getLightBlock(screenX, screenY) == ShadowType.FADE) {
        power -= 0.05f;
      }
      if (getLightBlock(screenX, screenY) == ShadowType.HALF && hit == false) {
        hit = true;
        power /= 2;
      }
      if (getLightBlock(screenX, screenY) == ShadowType.NONE && hit == true) {
        hit = false;
      }
      if (power <= 0.1f) {
        break;
      }
      e2 = 2 * err;

      if (e2 > -1 * dy) {
        err -= dy;
        x0 += sx;
      }
      if (e2 < dx) {
        err += dx;
        y0 += sy;
      }
      if (power == 1) {
        setLightMap(screenX, screenY, light.getLightValue(x0, y0));
      } else {
        setLightMap(screenX, screenY, Pixel.getColorPower(light.getLightValue(x0, y0), power));
      }
    }
  }
}