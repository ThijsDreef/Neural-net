package engine;

import java.awt.event.*;

public class Input implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener{
  private Engine en;

  private boolean[] keys = new boolean[255];
  private boolean[] keysLast = new boolean[255];
  private boolean[] keyUp = new boolean[255];
  private boolean[] reset = new boolean[255];
  private int mouseScroll = 0;
  private boolean[] buttons = new boolean[5];
  private boolean[] buttonsLast = new boolean[5];
  private boolean[] buttonsUp = new boolean[5];
  private int mouseX, mouseY;

  public Input(Engine en)
  {
    this.en = en;
    en.getWindow().getCanvas().addKeyListener(this);
    en.getWindow().getCanvas().addMouseListener(this);
    en.getWindow().getCanvas().addMouseMotionListener(this);
    en.getWindow().getCanvas().addMouseWheelListener(this);
  }
  public void update()
  {
    mouseScroll = 0;
    keysLast = keys.clone();
    keyUp = reset.clone();
    buttonsLast = buttonsLast.clone();
    buttonsUp = buttonsLast.clone();
  }
  @Override
  public void keyTyped(KeyEvent e) {

  }
  public void keyPressed(KeyEvent e) {
    keys[e.getKeyCode()] = true ;
  }

  @Override
  public void keyReleased(KeyEvent e) {
    keys[e.getKeyCode()] = false;
    keyUp[e.getKeyCode()] = true;
  }

  @Override
  public void mouseClicked(MouseEvent e) {

  }

  @Override
  public void mousePressed(MouseEvent e) {
    buttons[e.getButton()] = true;
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    buttons[e.getButton()] = false;
    buttonsUp[e.getButton()] = true;
  }

  @Override
  public void mouseEntered(MouseEvent e) {

  }

  @Override
  public void mouseExited(MouseEvent e) {

  }

  @Override
  public void mouseWheelMoved(MouseWheelEvent e)
  {
    mouseScroll += e.getPreciseWheelRotation();
  }

  @Override
  public void mouseDragged(MouseEvent e) {

  }

  @Override
  public void mouseMoved(MouseEvent e)
  {
    mouseX = (int)(e.getX() / en.getScale());
    mouseY = (int)(e.getY() / en.getScale());
  }
  public boolean getMouseButtonReleased(int button) {return buttonsUp[button];}
  public boolean isKeyPressed(int keyCode)
  {
    return keys[keyCode];
  }
  public boolean isKeyReleased(int KeyCode)
  {
    return keyUp[KeyCode];
  }
  public int getMouseScroll() {return mouseScroll;}
  public int getMouseX()
  {
    return mouseX;
  }
  public int getMouseY()
  {
    return mouseY;
  }
}
