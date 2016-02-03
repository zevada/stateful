package com.github.zevada.stateful;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StateMachineTest {
  enum State {
    INIT, RUNNING, PAUSED, COMPLETED
  }

  enum EventType {
    RUN, PAUSE, END
  }

  @Test
  public void testStateMachineTransitions() {
    StateMachine<State, EventType> stateMachine =
      new StateMachineBuilder<State, EventType>(State.INIT)
        .addTransition(State.INIT, EventType.RUN, State.RUNNING)
        .addTransition(State.RUNNING, EventType.PAUSE, State.PAUSED)
        .addTransition(State.RUNNING, EventType.END, State.COMPLETED)
        .addTransition(State.PAUSED, EventType.RUN, State.RUNNING)
        .build();

    assertEquals(stateMachine.getState(), State.INIT);

    stateMachine.apply(EventType.RUN);

    assertEquals(stateMachine.getState(), State.RUNNING);

    stateMachine.apply(EventType.PAUSE);

    assertEquals(stateMachine.getState(), State.PAUSED);

    stateMachine.apply(EventType.RUN);

    assertEquals(stateMachine.getState(), State.RUNNING);

    stateMachine.apply(EventType.END);

    assertEquals(stateMachine.getState(), State.COMPLETED);
  }

  @Test
  public void testOnStateEnterListener() {
    final boolean[] onEntered = {false};

    StateMachine<State, EventType> stateMachine =
      new StateMachineBuilder<State, EventType>(State.INIT)
        .addTransition(State.INIT, EventType.RUN, State.RUNNING)
        .onEnter(State.RUNNING, () -> onEntered[0] = true)
        .build();

    stateMachine.apply(EventType.RUN);

    assertTrue(onEntered[0]);
  }

  @Test
  public void testOnStateExitListener() {
    final boolean[] onExited = {false};

    StateMachine<State, EventType> stateMachine =
      new StateMachineBuilder<State, EventType>(State.INIT)
        .addTransition(State.INIT, EventType.RUN, State.RUNNING)
        .onExit(State.INIT, () -> onExited[0] = true)
        .build();

    stateMachine.apply(EventType.RUN);

    assertTrue(onExited[0]);
  }

  @Test(expected = UnexpectedEventTypeException.class)
  public void testUnexpectedEventTypeExceptionIsThrown() {
    StateMachine<State, EventType> stateMachine =
      new StateMachineBuilder<State, EventType>(State.INIT)
        .build();

    stateMachine.apply(EventType.RUN);
  }
}
