package game.managers;

import engine.Engine;
import engine.Renderer;

import java.io.*;
import java.util.ArrayList;

public class ObjectManager implements Serializable
{
  private ArrayList<GameObject> objects = new ArrayList<GameObject>();

  public void updateObjects(Engine en, float dt)
  {
    for (int i = 0; i < objects.size(); i++)
    {
      if (objects.get(i).isDead())
        objects.remove(i);
      else
      {
//        if (objects.get(i).x + objects.get(i).w - en.getCamera().getTransx() <= 0 && objects.get(i).getX() - en.getCamera().getTransx() >= en.getWidth())
          objects.get(i).update(en, dt);
      }
    }
  }
  public void renderObjects(Engine en, Renderer r)
  {
//    default render method
    for (int i = 0; i < objects.size(); i++)
      objects.get(i).render(en , r);
//    multiThreaded render method
//    ThreadGroup tg = new ThreadGroup("multi Render");
//    tg.setMaxPriority(Thread.MAX_PRIORITY);
//    int quarterSize = objects.size()/3;
//    MultiThreadRender object1 = new MultiThreadRender(en, r, new ArrayList<>(objects.subList(0, quarterSize)));
//    MultiThreadRender object2 = new MultiThreadRender(en, r, new ArrayList<>(objects.subList(quarterSize, quarterSize * 2)));
//    MultiThreadRender object3 = new MultiThreadRender(en, r, new ArrayList<>(objects.subList(quarterSize * 2, objects.size())));
//    object1.start();
//    object2.start();
//    object3.start();
//    while (object1.isAlive() || object2.isAlive() || object3.isAlive()){}
  }
  public void addObject (GameObject object)
  {
    objects.add(object);
  }
  public GameObject findObject(String tag)
  {
    for (int i = 0; i < objects.size(); i++)
    {
      if (objects.get(i).getTag().equalsIgnoreCase(tag))
        return objects.get(i);
    }
    return null;
  }
  public void disposeObjects()
  {
    for (int i = 0; i < objects.size(); i++)
      objects.get(i).dispose();
  }
  public void removeObject(GameObject object)
  {
    for (int i = 0; i < objects.size(); i ++)
      if (objects.get(i) == object)
        objects.remove(i);
  }
  public String SaveLevel() throws IOException
  {
    FileOutputStream file;
    File outputLocation;
    int count = 0;
    while (true)
    {
      outputLocation = new File("Properties/Levels/level" + count + ".hashmap");
      if (!outputLocation.exists())
        break;
      count++;
    }
    outputLocation.createNewFile();
    file = new FileOutputStream(outputLocation);
    ObjectOutputStream outputStream = new ObjectOutputStream(file);
    outputStream.writeObject(objects);
    outputStream.close();
    file.close();
    return outputLocation.toString();
  }
  public void SaveThisLevel(String level) throws IOException
  {
    FileOutputStream outputStream =  new FileOutputStream("Properties/"+level);
    ObjectOutputStream save = new ObjectOutputStream(outputStream);
    save.writeObject(objects);
    outputStream.flush();
    save.flush();
    outputStream.close();
    save.close();
  }
  public void LoadLevel(String level) throws IOException, ClassNotFoundException
  {
    FileInputStream fis = new FileInputStream("Properties/"+level);
    ObjectInputStream ois = new ObjectInputStream(fis);
    objects = (ArrayList<GameObject>)ois.readObject();
  }
  public void PlayLevel(String level) throws IOException, ClassNotFoundException {
    FileInputStream fis = new FileInputStream("Properties/"+level);
    ObjectInputStream ois = new ObjectInputStream(fis);
    objects = (ArrayList<GameObject>)ois.readObject();
  }
}