package com.game.engine.render.pipeline.packets;

import com.game.engine.render.pipeline.packets.interfaces.IRenderPacket;
import com.game.utils.engine.PipelineUtils;
import com.game.utils.enums.ERenderer;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

@Accessors(fluent = true)
@Slf4j
public class RenderPacket implements IRenderPacket {
  @Getter
  protected final ERenderer destination;
  protected final ArrayList<String> items;
  protected final ArrayBlockingQueue<String> queue;

  public RenderPacket(ERenderer destination) {
    this.destination = destination;

    items = new ArrayList<>();
    queue = new ArrayBlockingQueue<>(
      PipelineUtils.MAX_QUEUE_SIZE,
      true
    );
  }

  void logWarning(String warningMessage, String entityName) {
    log.warn("Render Packet {} Entity {} Message {}", destination.name(), entityName, warningMessage);
  }

  public void bind(String entityName) {
    if (items.contains(entityName)) {
      logWarning("already contains entity! Bind failed", entityName);
      return;
    }

    if (items.size() > PipelineUtils.MAX_QUEUE_SIZE) {
      logWarning("max size has been reached! Bind failed", entityName);
      return;
    }

    items.add(entityName);
  }

  public void unbind(String entityName) {
    if (items.isEmpty()) {
      logWarning("packet is empty! Unbind failed", entityName);
      return;
    }

    if (!items.contains(entityName)) {
      logWarning("entity not found! Unbind failed", entityName);
      return;
    }

    items.remove(entityName);
  }

  public ArrayBlockingQueue<String> renderQueue() {
    if (!queue.isEmpty()) {
      log.warn("Emptying render queue with size {}!", queue.size());
      queue.clear();
    }

    queue.addAll(items);
    return queue;
  }
}
