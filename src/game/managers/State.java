package game.managers;

import engine.Engine;
import engine.Renderer;

public abstract class State
{
  protected ObjectManager manager = new ObjectManager();
  public abstract void update(Engine en, float dt);
  public abstract void render(Engine en, Renderer r);
  public abstract void dispose();

  public ObjectManager getManager()
  {
    return manager;
  }
  public void setManager(ObjectManager manager)
  {
    this.manager = manager;
  }
}
