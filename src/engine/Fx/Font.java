package engine.Fx;

import engine.Image;

public class Font
{
  public Image standard;
  public int[] offsets = new int[90];
  public int[] widths = new int[90];

  public Font(String path)
  {
    standard = new Image(path);
    int unicode = 0;

    for (int x = 0; x < standard.width; x++)
    {
      int color = standard.pixels[x];

      if (color == 0xff0000ff)
      {
        unicode++;
        offsets[unicode] = x;
      }
      if (color == 0xffffff00)
      {
        widths[unicode] = x - offsets[unicode];
      }
    }
  }
}
