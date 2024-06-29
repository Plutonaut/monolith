package com.game;

import com.game.engine.MainEngine;

public class Main {
  public static void main(String[] args) {
    System.out.println("Hello world!");

    MainEngine engine = new MainEngine();
    engine.init(true);
  }
}