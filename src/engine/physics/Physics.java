package engine.physics;

import java.util.ArrayList;

public class Physics
{
  private ArrayList<Collider> objects = new ArrayList<Collider>();
  private ArrayList<Collider> prePos = new ArrayList<>();
  public void update()
  {
    prePos = objects;
    for (int i = 0; i < objects.size(); i++)
    {
      for (int j = i + 1; j < objects.size(); j++)
      {
        Collider col0 = objects.get(i);
        Collider col1 = objects.get(j);
        if (Math.abs(col0.getX() - col1.getX()) < col0.gethW() + col1.gethW())
        {
          if (Math.abs(col0.getY() - col1.getY()) < col0.gethH() + col1.gethH())
          {
            if (Math.abs(prePos.get(i).getX() - prePos.get(j).getX()) - Math.abs(prePos.get(i).gethW() + prePos.get(j).gethW()) < Math.abs(prePos.get(i).getY() - prePos.get(j).getY()) - Math.abs(prePos.get(i).gethH() + prePos.get(j).gethH()))
            {
              col0.collision(col1.getObject(), col1.getTag(), "y");
              col1.collision(col0.getObject(), col0.getTag(), "y");
            }
            else
            {
              col0.collision(col1.getObject(), col1.getTag(), "x");
              col1.collision(col0.getObject(), col0.getTag(), "x");
            }
          }
        }
      }
    }
    objects.clear();

  }
  public void addCollider(Collider e)
  {
    objects.add(e);
  }
}
