package com.game.utils.application;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
public class LambdaValue {
  private float value;
  private float previousValue;
  private final float initialValue;

  public LambdaValue() { this(0); }
  public LambdaValue(float value) { this.value = this.previousValue = this.initialValue = value; }

  public float set(float value) {
    this.previousValue = this.value;
    this.value = value;

    return this.value;
  }

  public void reset() {
    this.value = initialValue;
  }

  public boolean hasChanged() {
    return this.value != initialValue;
  }
}
