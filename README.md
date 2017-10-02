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

static class Context {
  public String message;

  public Context(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}

...

StateMachine<State, Event, Context> stateMachine =
  new StateMachineBuilder<State, Event, Context>(State.INIT)
    .addTransition(State.INIT, Event.RUN, State.RUNNING)
    .addTransition(State.RUNNING, Event.END, State.COMPLETED)
    .build();

stateMachine.getState(); // State.INIT
stateMachine.apply(Event.RUN);
stateMachine.getState(); // State.RUNNING
```

### On State Enter/Exit Listeners

```java
StateMachine<State, Event, Context> stateMachine =
  new StateMachineBuilder<State, Event, Context>(State.INIT)
    .addTransition(State.INIT, Event.RUN, State.RUNNING)
    .onExit(State.INIT, (context) -> System.out.println("Exiting Init!"))
    .onEnter(State.RUNNING, (context) -> System.out.println("Entering Running!"))
    .build();

stateMachine.apply(Event.RUN);
```

In addition to applying simple events, a context may be passed in to allow further decoupling of application concerns:
```java
StateMachine<State, Event, Context> stateMachine =
  new StateMachineBuilder<State, Event, Context>(State.INIT)
    .addTransition(State.INIT, Event.RUN, State.RUNNING)
    .onExit(State.INIT, (context) -> System.out.println("Exiting Init: " + String.valueOf(context)))
    .onEnter(State.RUNNING, (context) -> System.out.println("Entering Running: " + String.valueOf(context)))
    .build();

stateMachine.apply(Event.RUN, new Context("Started at: " + new Date()));
```

### Non-strict transition mode

By default, applying an event which does not cause a state transition throws an **UnexpectedEventTypeException**. This behavior can be disabled by setting **strictTransitions** to false when building the state machine.

```java
StateMachine<State, Event, Context> stateMachine =
  new StateMachineBuilder<State, Event, Context>(State.INIT)
    .strictTransitions(false)
    .build();

// No longer throws an exception
stateMachine.apply(Event.RUN);
``` 
