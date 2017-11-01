package engine.SaveAndLoad;

import game.Game;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Properties implements Serializable
{
  String config = "Properties/config.properties";
  public Properties()
  {
    if (!new File(config).exists())
    {
      // create the directory if it doesnt exist
      new File("Properties/levels").mkdirs();
      //copy the properties file from inside the jar file or from the folder
      try {
        Files.copy(Game.class.getResourceAsStream("Config/Properties/config.properties"), new File("Properties/config.properties").toPath(), StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException e) {
        e.printStackTrace();
      }
      int count = 0;
      while (true)
      {
        // try to copy level files from inside the jar to outside the jar
        FileOutputStream outputStream = null;
        // get a file
        InputStream inputStream = Game.class.getResourceAsStream("Config/Levels/level"+count+".hashmap");
        // if its null so no file exists break the loop
        if (inputStream == null)
        break;
        try
        {
          //try to create a file to output the levels to
          outputStream = new FileOutputStream("Properties/Levels/level"+count+".hashmap");
        } catch (FileNotFoundException e)
        {
          e.printStackTrace();
        }
        if (outputStream != null)
        {
          // read the bytes from the inputstream and copy these bytes
          ReadableByteChannel channel = Channels.newChannel(inputStream);
          try
          {
            outputStream.getChannel().transferFrom(channel, 0 , Long.MAX_VALUE);
          } catch (IOException e)
          {
            e.printStackTrace();
          }

        }
        count++;
        System.out.println(inputStream);

      }
    }
  }
  public void saveVariable(String key, String value)
  {
    java.util.Properties properties = new java.util.Properties();
    try
    {
      FileInputStream input = new FileInputStream(config);
      properties.load(input);
      FileOutputStream output = new FileOutputStream(config);
      properties.setProperty(key, value);
      properties.store(output, null);
    }
    catch (IOException io)
    {
      io.printStackTrace();
    }
  }
  public String loadVariable(String key)
  {
    try
    {
      java.util.Properties properties = new java.util.Properties();
      FileInputStream input = new FileInputStream(config);
      properties.load(input);
      return properties.getProperty(key);
    }
    catch (IOException io)
    {
      io.printStackTrace();
    }
    return null;
  }
}
