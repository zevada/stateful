package com.github.zevada.stateful;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

final class Node<State extends Enum<State>, EventType extends Enum<EventType>> {
  private final Map<EventType, Node<State, EventType>> neighbors;
  private final List<Runnable> onEnterListeners;
  private final List<Runnable> onExitListeners;
  private final State state;

  Node(State state) {
    this.state = state;
    neighbors = new HashMap<>();
    onEnterListeners = new LinkedList<>();
    onExitListeners = new LinkedList<>();
  }

  public State getState() {
    return state;
  }

  public Node<State, EventType> getNeighbor(EventType eventType) {
    return neighbors.get(eventType);
  }

  public void onEnter() {
    onEnterListeners.forEach(Runnable::run);
  }

  public void onExit() {
    onExitListeners.forEach(Runnable::run);
  }

  public void addNeighbor(EventType eventType, Node<State, EventType> destination) {
    neighbors.put(eventType, destination);
  }

  public void addOnEnterListener(Runnable onEnterListener) {
    onEnterListeners.add(onEnterListener);
  }

  public void addOnExitListener(Runnable onExitListener) {
    onExitListeners.add(onExitListener);
  }
}
