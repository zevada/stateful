package com.github.zevada.stateful;

import java.util.HashMap;
import java.util.Map;

/**
 * A builder for simple event based state machines
 *
 * @param <State> The state of the entity
 * @param <EventType> The event type to be handled
 */
final public class StateMachineBuilder<State extends Enum<State>, EventType extends Enum<EventType>, Context> {
  private final Map<State, Node<State, EventType, Context>> nodes;
  private final Node<State, EventType, Context> root;
  private boolean strictTransitions = true;


  /**
   * @param initialState the initial state of the state machine
   */
  public StateMachineBuilder(State initialState) {
    nodes = new HashMap<>();
    root = new Node<>(initialState);
    nodes.put(initialState, root);
  }

  /**
   * Use this method to construct the state machine after
   * completing the declaration of state machine
   * topology and listeners.
   *
   * @return the final state machine
   */
  public StateMachine<State, EventType, Context> build() {
    return new StateMachine<>(root, strictTransitions);
  }

  /**
   * Add a transition to the state machine from "startState"
   * to "endState" in response to events of type "eventType"
   *
   * @param startState the starting state of the transition
   * @param eventType the event type that triggered the transition
   * @param endState the end state of the transition
   */
  public StateMachineBuilder<State, EventType, Context> addTransition(State startState, EventType eventType, State endState) {
    Node<State, EventType, Context> startNode = nodes.get(startState);

    if (startNode == null) {
      startNode = new Node<>(startState);
      nodes.put(startState, startNode);
    }

    Node<State, EventType, Context> endNode = nodes.get(endState);

    if (endNode == null) {
      endNode = new Node<>(endState);
      nodes.put(endState, endNode);
    }

    startNode.addNeighbor(eventType, endNode);

    return this;
  }

  /**
   * Add a runnable to the state machine which will only be
   * executed when the state machine enters the specified state.
   *
   * @param state The state for which we are listening to onEnter events
   * @param onEnter The runnable to call when the state is entered
   */
  public StateMachineBuilder<State, EventType, Context> onEnter(State state, StatefulFunction<Context> onEnter) {
    Node<State, EventType, Context> node = nodes.get(state);

    if (node == null) {
      node = new Node<>(state);
      nodes.put(state, node);
    }

    node.addOnEnterListener(onEnter);

    return this;
  }

  /**
   * Add a runnable to the state machine which will only be
   * executed when the state machine exits the specified state.
   *
   * @param state The state for which we are listening to onExit events
   * @param onExit The runnable to call when the state is exited
   */
  public StateMachineBuilder<State, EventType, Context> onExit(State state, StatefulFunction<Context> onExit) {
    Node<State, EventType, Context> node = nodes.get(state);

    if (node == null) {
      node = new Node<>(state);
      nodes.put(state, node);
    }

    node.addOnExitListener(onExit);

    return this;
  }

  /**
   * Configures whether or not applying a state with no available transition throws an exception.
   * @param strictTransitions If true, calling apply with an invalid transition will throw.
   */
  public StateMachineBuilder<State, EventType, Context> strictTransitions(boolean strictTransitions) {
    this.strictTransitions = strictTransitions;

    return this;
  }
}
