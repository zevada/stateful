[![Build Status](https://travis-ci.org/zevada/stateful.svg?branch=master)](https://travis-ci.org/zevada/stateful)

# Introduction

Stateful is a simple and lightweight event driven state machine library. With 
Stateful you can implement complex state dependant logic with a clean and simple design. 

# Installation

The project is built using Maven and the artifacts are available from Maven Central.

```xml
  <dependency>
    <groupId>com.github.zevada</groupId>
    <artifactId>stateful</artifactId>
    <version>1.0.0</version>
  </dependency>
```

# Usage

### Simple Transitions

```java
enum State {
  INIT, RUNNING, COMPLETED
}

enum Event {
  RUN, END
}

...

StateMachine<State, Event> stateMachine =
  new StateMachineBuilder<State, Event>(State.INIT)
    .addTransition(State.INIT, Event.RUN, State.RUNNING)
    .addTransition(State.RUNNING, Event.END, State.COMPLETED)
    .build();

stateMachine.getState(); // State.INIT
stateMachine.apply(Event.RUN);
stateMachine.getState(); // State.RUNNING
```

### On State Enter/Exit Listeners

```java
StateMachine<State, Event> stateMachine =
  new StateMachineBuilder<State, Event>(State.INIT)
    .addTransition(State.INIT, Event.RUN, State.RUNNING)
    .onExit(State.INIT, () -> System.out.println("Exiting Init!"))
    .onEnter(State.RUNNING, () -> System.out.println("Entering Running!"))
    .build();

stateMachine.apply(Event.RUN);
```

### Non-strict transition mode

By default, applying an event which does not cause a state transition throws an **UnexpectedEventTypeException**. This behavior can be disabled by setting **strictTransitions** to false when building the state machine.

```java
StateMachine<State, EventType> stateMachine =
  new StateMachineBuilder<State, EventType>(State.INIT)
    .strictTransitions(false)
    .build();

// No longer throws an exception
stateMachine.apply(EventType.RUN);
``` 
