package com.game.utils.application;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
public class LambdaCounter {
  private int value;

  public LambdaCounter() {this(0);}

  public LambdaCounter(int value) {this.value = value;}

  public int inc() {return value++;}

  public int set(int value) {
    this.value = value;
    return value;
  }

  public int add(int value) {
    this.value += value;
    return value;
  }
}
