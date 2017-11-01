package engine;

import game.managers.GameObject;

import java.util.ArrayList;

public class MultiThreadRender extends Thread
{
  Engine en;
  Renderer r;
  ArrayList<GameObject> objects;
  public MultiThreadRender(Engine en, Renderer r, ArrayList<GameObject> objects)
  {
    this.en = en;
    this.r = r;
    this.objects = objects;
  }

  @Override
  public void run()
  {
    super.run();
    for (int i = 0; i < objects.size(); i++)
      objects.get(i).render(en, r);
  }
}
