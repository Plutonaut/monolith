package com.game.engine.scene;

import com.game.engine.compute.Context;
import com.game.engine.compute.Instruction;
import com.game.utils.enums.ERenderer;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

public class ContextManager {
  private final HashMap<ERenderer, Context> contexts;

  public ContextManager() {
    contexts = new HashMap<>();
  }

  public boolean contains(ERenderer shader) {
    return contexts.containsKey(shader);
  }

  public void bind(Context context) {
    contexts.put(context.shader(), context);
  }

  public Context context(ERenderer shader) {
    return contexts.computeIfAbsent(shader, (s) -> new Context(s.compute(), s));
  }

  public ArrayBlockingQueue<Instruction> computeQueue(ERenderer shader) {
    return context(shader).computeQueue();
  }
}
