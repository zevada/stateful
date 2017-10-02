package com.github.zevada.stateful;

/**
 * A simple event based state machine.
 *
 * @param <State> The state of the entity
 * @param <EventType> The event type to be handled
 */
public final class StateMachine<State extends Enum<State>, EventType extends Enum<EventType>, Context> {
  private Node<State, EventType, Context> root;
  private boolean strictTransitions;

  StateMachine(Node<State, EventType, Context> root, boolean strictTransitions) {
    this.root = root;
    this.strictTransitions = strictTransitions;
  }

  /**
   * Apply an event to the state machine with no context object.
   */
  public void apply(EventType eventType) {
    apply(eventType, null);
  }

  /**
   * Apply an event to the state machine.
   *
   * @param eventType The event type to be handled
   * @param context A context to pass into the transition function
   */
  public void apply(EventType eventType, Context context) {
    Node<State, EventType, Context> nextNode = root.getNeighbor(eventType);

    if (nextNode == null) {
      if (strictTransitions) {
        throw new UnexpectedEventTypeException(root.getState(), eventType);
      } else {
        return;
      }
    }

    root.onExit(context);
    root = nextNode;
    root.onEnter(context);
  }

  /**
   * @return The current state of the state machine
   */
  public State getState() {
    return root.getState();
  }
}
