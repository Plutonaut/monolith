package com.game.utils.application;

public interface IPersistable {
  public String id();
  public void write(StringBuilder builder);
}
