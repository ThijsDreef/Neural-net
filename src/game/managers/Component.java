package game.managers;

import engine.Engine;
import engine.Renderer;

import java.io.Serializable;

public abstract class Component implements Serializable
{
  protected String tag = "null";
  public abstract void update(Engine en, GameObject object, float dt);
  public abstract void render(Engine en, Renderer r);

  public String getTag()
  {
    return tag;
  }
  public void setTag(String tag)
  {
    this.tag = tag;
  }
}
