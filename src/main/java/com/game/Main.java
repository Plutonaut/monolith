package com.game;

import com.game.engine.GameEngine;

public class Main {
  public static void main(String[] args) {
    System.out.println("Hello world!");

    GameEngine engine = new GameEngine();
    engine.init();
  }
}