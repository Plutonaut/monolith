package com.game.engine.render.pipeline.packets;

import com.game.engine.render.IRenderable;
import com.game.engine.render.models.Model;
import com.game.engine.scene.entities.Entity;
import com.game.engine.scene.entities.TextEntity;
import com.game.utils.engine.PipelineUtils;
import com.game.utils.enums.ERenderer;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Stream;

@Accessors(fluent = true)
@Data
@Slf4j
public class RenderPacket implements IRenderPacket {
  protected final ERenderer destination;
  // TODO: Replace with list of entity ids. Move entity objects to entity manager.
  // TODO: Move HUD entities to HUD. Pull from HUDManager exclusively.
  protected final ArrayList<IRenderable> items;
  protected final ArrayBlockingQueue<Model> queue;

  public RenderPacket(ERenderer destination) {
    this.destination = destination;
    items = new ArrayList<>();
    queue = new ArrayBlockingQueue<>(PipelineUtils.MAX_QUEUE_SIZE);
  }

  public void queue(Model model) {
    queue.offer(model);
  }

  public Entity getEntity(String key) {
    return getAllEntities(key).findFirst().orElse(null);
  }

  public TextEntity getGameText(String key) {
    return getAllGameText(key).findFirst().orElse(null);
  }

  public Stream<Entity> getAllSceneEntities() {
    return getAllEntities("");
  }

  public Stream<Entity> getAllEntities(String key) {
    return getAllAs(key, Entity.class).map(i -> (Entity)i);
  }

  public Stream<TextEntity> getAllGameText(String key) {
    return getAllAs(key, TextEntity.class).map(i -> (TextEntity)i);
  }

  Stream<IRenderable> getAllAs(String key, Class<?> type) {
    return items.stream().filter(i -> i.name().startsWith(key) && type.isInstance(i));
  }

  public void flush(ModelBinder binder) {
    while (queue().peek() != null) {
      Model model = queue().poll();
//      List<Mesh> meshes = binder.bind(destination, model);
//      String entityName = GlobalCache.instance().resolveEntityName(model.name());
//      Entity entity = model.create(entityName);
//      entity.addMeshes(meshes);
//      add(entity);
      PacketResult result = binder.bind(destination, model);
      IRenderable item = result.create(model);
      add(item);
    }
  }

  public RenderPacket add(IRenderable entity) {
    if (items.size() <= PipelineUtils.MAX_QUEUE_SIZE) items.add(entity);
    else log.error("Render packet {} is currently full!", destination);
    return this;
  }

  public ArrayBlockingQueue<IRenderable> renderQueue() {
    ArrayBlockingQueue<IRenderable> queue = new ArrayBlockingQueue<>(
      PipelineUtils.MAX_QUEUE_SIZE,
      true
    );
    queue.addAll(items);
    return queue;
  }
}
