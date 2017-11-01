package game.GameObjects;

import sun.audio.AudioPlayer;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Thijs Dreef on 17/10/2017.
 */
public class SoundUser extends NeuralNetUser
{
  AudioPlayer player;
  File[] musicFiles;
  public SoundUser(File soundLocation)
  {
    musicFiles = soundLocation.listFiles();
  }
  @Override
  public String useResult(float[] result)
  {
    for (int i = 0; i < result.length; i++)
    {
      if (result[i] > 0.6f)
      {
        try
        {

          AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicFiles[i]);
          Line.Info lineInfo = new Line.Info(Clip.class);
          Line line = AudioSystem.getLine(lineInfo);
          Clip clip = (Clip) line;
          clip.open(audioInputStream);
          FloatControl volum = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
          volum.setValue(-20f);
          clip.start();
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e)
        {
          e.printStackTrace();
        }
      }
    }
    return Arrays.toString(result);
  }
}
