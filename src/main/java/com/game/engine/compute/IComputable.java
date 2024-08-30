package com.game.engine.compute;

public interface IComputable {
  String name();
  Instruction access(String instruction);
}
