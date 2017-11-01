package engine;

import game.managers.State;

import java.util.Stack;

public abstract class AbstractGame
{
  private State changeState;
  private Stack<State> states = new Stack<State>();
  public abstract void update(Engine En, float dt);
  public abstract void render(Engine En, Renderer r);
  public  void changeState()
  {
    if (changeState != null)
      states.push(changeState);
  }
  public void changeToState(State s)
  {
    changeState = s;
  }
  public void push(State s)
  {
    states.push(s);
  }
  public State peek()
  {
    return states.peek();
  }
}
