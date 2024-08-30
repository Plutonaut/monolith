package com.game.engine.compute;

import com.game.utils.engine.PipelineUtils;
import com.game.utils.enums.ERenderer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

@Accessors(fluent = true)
@Data
public class Context implements IComputable {
  protected final HashMap<String, Instruction> instructions;
  protected final ArrayBlockingQueue<Instruction> queue;
  protected final ERenderer shader;
  protected final String name;

  public Context(String name, ERenderer shader) {
    this.name = name;
    this.shader = shader;
    this.queue = new ArrayBlockingQueue<>(PipelineUtils.MAX_QUEUE_SIZE);

    instructions = new HashMap<>();
  }

  public void add(Instruction instruction) {
    instructions.put(instruction.name(), instruction);
  }

  public ArrayBlockingQueue<Instruction> computeQueue() {
    queue.clear();

    queue.addAll(instructions.values());
    return queue;
  }

  public Instruction access(String instruction) {
    return instructions.getOrDefault(
      instruction,
      null
    );
  }
}
