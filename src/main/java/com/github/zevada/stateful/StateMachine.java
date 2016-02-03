package com.github.zevada.stateful;

/**
 * A simple event based state machine.
 *
 * @param <State> The state of the entity
 * @param <EventType> The event type to be handled
 */
public final class StateMachine<State extends Enum<State>, EventType extends Enum<EventType>> {
  private Node<State, EventType> root;

  StateMachine(Node<State, EventType> root) {
    this.root = root;
  }

  /**
   * Apply an event to the state machine.
   *
   * @param eventType The event type to be handled
   */
  public void apply(EventType eventType) {
    Node<State, EventType> nextNode = root.getNeighbor(eventType);

    if (nextNode == null) {
      throw new UnexpectedEventTypeException(root.getState(), eventType);
    }

    root.onExit();
    root = nextNode;
    root.onEnter();
  }

  /**
   * @return The current state of the state machine
   */
  public State getState() {
    return root.getState();
  }
}
