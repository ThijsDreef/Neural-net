package game.NeuralNet;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ImageLoader
{
  public float[][][] loadTrainingData(File trainingData)
  {
    ArrayList<File> imagesToLoad = new ArrayList<>();
    ArrayList<float[]> targets = new ArrayList<>();
    try
    {
      FileInputStream input = new FileInputStream(trainingData);
      Scanner fileInput = new Scanner(input);
      while (fileInput.hasNextLine())
      {
        String line = fileInput.nextLine();
        if (line.startsWith("["))
        {

          line = line.replace ("[", "");
          line = line.replace ("]", "");
          String vals [] = line.split (", ");
          float target[] = new float[vals.length];
          for (int i = 0; i < vals.length; i++)
            target[i] = Float.parseFloat(vals[i]);
          targets.add(target);
        }
        else
        {
          File file = new File(trainingData.getParent() + "/" + line);
          if (file.exists())
          {
            System.out.println(file.getAbsolutePath());
            imagesToLoad.add(file);
          }
        }
      }
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    float data[][][] = new float[2][][];
    data[0] = new float[imagesToLoad.size()][];
    for (int i = 0; i < imagesToLoad.size(); i++)
      data[0][i] = loadImage(imagesToLoad.get(i));
    data[1] = new float[targets.size()][];
    for (int i = 0; i < targets.size(); i++)
      data[1][i] = targets.get(i);
    return data;
  }
  public float[] loadImage(File path)
  {
    BufferedImage image = null;
    try
    {
      image = ImageIO.read(path);
    } catch (IOException e)
    {
      e.printStackTrace();
    }
    if (image == null)
      return null;
    int width = image.getWidth();
    int height = image.getHeight();
    int pixels[] = image.getRGB(0, 0, width, height, null, 0, width);
    float grayScale[] = new float[pixels.length * 3];
    for (int i = 0; i < pixels.length; i++)
    {
      grayScale[i * 3] = getRed(pixels[i]);
      grayScale[i * 3 + 1] = getGreen(pixels[i]);
      grayScale[i * 3 + 2] = getBlue(pixels[i]);
    }
    return grayScale;
  }
  public float[][] loadImages(File[] files)
  {
    float data[][] = new float[files.length][];
    for (int i = 0; i < files.length; i++)
      data[i] = loadImage(files[i]);
    return data;
  }
  private float getGreen(int color)
  {
    return (0xff & (color >> 8)) / 255f;
  }
  private float getRed(int color)
  {
    return (0xff & (color >> 16)) / 255f;
  }
  private float getBlue(int color)
  {
    return (0xff & (color)) / 255f;
  }
}
