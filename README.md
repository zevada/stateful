# Introduction

Stateful is a simple and lightweight event driven state machine library. With 
Stateful you can implement complex state dependant logic with a clean and simple design. 

[![Build Status](https://travis-ci.org/zevada/Stateful.svg?branch=master)](https://travis-ci.org/zevada/Stateful)

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
