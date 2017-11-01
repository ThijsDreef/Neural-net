package engine;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Window
{
  private JFrame frame;
  private Canvas canvas;
  private BufferedImage image;
  private Graphics g;
  private BufferStrategy bs;

  public Window(Engine en)
  {
    image = new BufferedImage(en.getWidth(), en.getHeight(), BufferedImage.TYPE_INT_ARGB);
    canvas = new Canvas();
    Dimension s = new Dimension((int)(en.getWidth() * en.getScale()),(int)(en.getHeight() * en.getScale()));
    canvas.setMinimumSize(s);
    canvas.setPreferredSize(s);
    canvas.setMaximumSize(s);

    frame = new JFrame(en.getTitle());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new BorderLayout());
    frame.add(canvas, BorderLayout.CENTER);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setResizable(false);
    frame.setVisible(true);

    canvas.createBufferStrategy(1);
    bs = canvas.getBufferStrategy();
    g = bs.getDrawGraphics();
  }
  public void update()
  {
    g.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
    bs.show();
  }
  public void dispose()
  {
    frame.dispose();
  }
  public Canvas getCanvas() {
    return canvas;
  }
  public JFrame getFrame() {return frame;}
  public BufferedImage getImage() {
    return image;
  }
}
