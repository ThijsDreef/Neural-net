package game.managers;

import engine.Engine;
import engine.Fx.ShadowType;
import engine.Renderer;

import java.io.Serializable;
import java.util.ArrayList;

import static engine.Fx.ShadowType.NONE;

public abstract class GameObject implements Serializable
{
  public ShadowType shadowType = NONE;
  protected float x,y,h,w;
  protected String tag = "null";
  private boolean dead = false;
  protected ArrayList<Component> components = new ArrayList<Component>();
  private int state;
  public abstract void update(Engine en, float dt);
  public abstract void render(Engine en, Renderer r);

  public void updateComponents(Engine en, float dt)
  {
    for (int i = 0; i < components.size(); i++)
    {
      components.get(i).update(en, this, dt);
    }
  }
  public void renderComponents(Engine en, Renderer r)
  {
    for (int i = 0; i < components.size(); i++)
    {
      components.get(i).render(en, r);
    }
  }
  public void addComponent(Component c)
  {
    components.add(c);
  }
  public void removeComponent(String tag)
  {
    for (int i = 0; i < components.size(); i++)
    {
      if (components.get(i).getTag().equalsIgnoreCase(tag))
        components.remove(i);
    }
  }
  public abstract void componentEvent(String name, GameObject object, String axis);
  public abstract void dispose();

  public float getX() {
    return x;
  }

  public void setX(float x) {
    this.x = x;
  }

  public float getY() {
    return y;
  }

  public void setY(float y) {
    this.y = y;
  }

  public float getH() {
    return h;
  }

  public void setH(float h) {
    this.h = h;
  }

  public float getW() {
    return w;
  }

  public void setW(float w) {
    this.w = w;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public boolean isDead() {
    return dead;
  }

  public void setDead(boolean dead) {
    this.dead = dead;
  }

  public int getState() {
    return state;
  }

  public void setState(int state) {
    this.state = state;
  }


  public void kill()
  {
    dead = true;
  }
  public void addY(int addedy)
  {
    y += addedy;
  }
}
