package org.sevada.stateful;

import java.util.HashMap;
import java.util.Map;

/**
 * A builder for simple event based state machines
 *
 * @param <State> The state of the entity
 * @param <EventType> The event type to be handled
 */
final public class StateMachineBuilder<State extends Enum<State>, EventType extends Enum<EventType>> {
  private final Map<State, Node<State, EventType>> nodes;
  private final Node<State, EventType> root;

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
  public StateMachine<State, EventType> build() {
    return new StateMachine<>(root);
  }

  /**
   * Add a transition to the state machine from "startState"
   * to "endState" in response to events of type "eventType"
   *
   * @param startState the starting state of the transition
   * @param eventType the event type that triggered the transition
   * @param endState the end state of the transition
   */
  public StateMachineBuilder<State, EventType> addTransition(State startState, EventType eventType, State endState) {
    Node<State, EventType> startNode = nodes.get(startState);

    if (startNode == null) {
      startNode = new Node<>(startState);
      nodes.put(startState, startNode);
    }

    Node<State, EventType> endNode = nodes.get(endState);

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
  public StateMachineBuilder<State, EventType> onEnter(State state, Runnable onEnter) {
    Node<State, EventType> node = nodes.get(state);

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
  public StateMachineBuilder<State, EventType> onExit(State state, Runnable onExit) {
    Node<State, EventType> node = nodes.get(state);

    if (node == null) {
      node = new Node<>(state);
      nodes.put(state, node);
    }

    node.addOnExitListener(onExit);

    return this;
  }
}
