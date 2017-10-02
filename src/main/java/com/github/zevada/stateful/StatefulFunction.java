package com.github.zevada.stateful;

/**
 * A callback to execute during state transition.
 *
 * @param <Context> A context object which may be provided when applying an event.
 */
@FunctionalInterface
public interface StatefulFunction<Context> {
  void transition(Context c);
}

