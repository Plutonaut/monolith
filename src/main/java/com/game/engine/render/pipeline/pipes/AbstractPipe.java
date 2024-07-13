package com.game.engine.render.pipeline.pipes;

import com.game.engine.render.IRenderable;
import com.game.graphics.shaders.Program;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ArrayBlockingQueue;

@Slf4j
public abstract class AbstractPipe {
  private final ArrayBlockingQueue<IRenderable> queue;
  private Program program;

  public AbstractPipe() {
    queue = new ArrayBlockingQueue<>(16);
    program = null;
  }

  protected abstract String name();

  protected abstract String offer(IRenderable item);

  public AbstractPipe use(Program program) {
    if (program == null) {
      onProgramIsNull();
      return null;
    }

    this.program = program;
    return this;
  }

  public AbstractPipe flush(AbstractPipe pipe) {
    pipe.use(program);
    while (queue.peek() != null) pipe.pipe(queue.poll());
    program = null;
    return pipe;
  }

  public void pipe(IRenderable item) {
    if (program == null) onProgramIsNull();
    else {
      String message = offer(item);
      if (!StringUtils.isEmpty(message)) {
        onError(message);
      }
    }
  }

  void onProgramIsNull() {
    onError("Program is null!");
  }

  void onError(String message) {
    log.error("ERROR {} Pipeline Stage: {}", name(), message);
  }
}
