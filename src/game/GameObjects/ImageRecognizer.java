package game.GameObjects;

import java.util.Arrays;

/**
 * Created by Thijs Dreef on 17/10/2017.
 */
public class ImageRecognizer extends NeuralNetUser
{
  @Override
  public String useResult(float[] result)
  {
    String rawResult = Arrays.toString(result);
    for (int i = 0; i < result.length; i++)
      result[i] = Math.round(result[i]);
    return "formated output: " + Arrays.toString(result) +"\n" + "unformatted output: " + rawResult;
  }
}
