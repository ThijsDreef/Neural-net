package game;

import engine.AbstractGame;
import engine.Engine;
import engine.Renderer;
import game.States.Play;

public class Game extends AbstractGame
{
  public Game()
  {
    push(new Play());
  }

  @Override
  public void update(Engine En, float dt)
  {
    peek().update(En, dt);
  }

  @Override
  public void render(Engine En, Renderer r)
  {
    peek().render(En, r);
  }

  public  static void main(String[] args)
  {
    Engine en = new Engine((new Game()));
    en.setWidth(1280);
    en.setHeight(720);
    en.start();
  }
}
