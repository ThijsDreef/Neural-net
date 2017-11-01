package engine.physics;

import engine.Engine;
import engine.Renderer;
import game.managers.Component;
import game.managers.GameObject;

import java.io.Serializable;

public class Collider extends Component implements Serializable
{
  private GameObject object;
  private float x, y, hW, hH;
  public Collider(String setTag, GameObject object)
  {
    setTag(setTag);
  }
  public void collision(GameObject object, String tag, String axis)
  {
    this.object.componentEvent(tag, object, axis);
  }
  @Override
  public void update(Engine en, GameObject object, float dt)
  {
    if (this. object == null)
      this.object = object;

    hW = object.getW() / 2;
    hH = object.getH() / 2;
    x = object.getX() + hW;
    y = object.getY() + hH;
    en.getPhysics().addCollider(this);
  }

  @Override
  public void render(Engine en, Renderer r) {

  }

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

  public float gethW() {
    return hW;
  }

  public void sethW(float hW) {
    this.hW = hW;
  }

  public float gethH() {
    return hH;
  }

  public void sethH(float hH) {
    this.hH = hH;
  }

  public GameObject getObject() {
    return object;
  }

  public void setObject(GameObject object) {
    this.object = object;
  }
}

