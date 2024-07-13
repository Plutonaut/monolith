package com.game.engine.render.pipeline.packets;

import com.game.engine.render.models.Model;
import com.game.engine.scene.entities.Entity;
import com.game.utils.engine.PipelineUtils;
import com.game.utils.enums.ERenderer;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

@Accessors(fluent = true)
@Data
@Slf4j
public class RenderPacket implements IRenderPacket {
  protected final ERenderer destination;
  protected final ArrayList<Entity> items;
  protected final ArrayBlockingQueue<Model> queue;

  public RenderPacket(ERenderer destination) {
    this.destination = destination;
    items = new ArrayList<>();
    queue = new ArrayBlockingQueue<>(PipelineUtils.MAX_QUEUE_SIZE);
  }

  public void queue(Model model) {
    queue.offer(model);
  }

  public RenderPacket add(Entity entity) {
    if (items.size() <= PipelineUtils.MAX_QUEUE_SIZE) items.add(entity);
    else log.error("Render packet {} is currently full!", destination);
    return this;
  }

  public ArrayBlockingQueue<Entity> renderQueue() {
    ArrayBlockingQueue<Entity> queue = new ArrayBlockingQueue<>(PipelineUtils.MAX_QUEUE_SIZE, true);
    queue.addAll(items);
    return queue;
  }
}
