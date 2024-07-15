package com.game.engine.render.pipeline.packets;

import com.game.engine.render.IRenderable;
import com.game.engine.render.models.Model;
import com.game.engine.scene.entities.Entity;
import com.game.engine.scene.entities.TextEntity;
import com.game.utils.enums.ERenderer;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Stream;

public class PacketManager {
  private final HashMap<ERenderer, RenderPacket> packets;

  public PacketManager() {
    packets = new HashMap<>();
  }

  public Entity getEntity(String key) {
    return packetStream()
      .map(p -> p.getEntity(key))
      .filter(Objects::nonNull)
      .findFirst()
      .orElse(null);
  }

  public TextEntity getGameText(String key) {
    return packetStream()
      .map(p -> p.getGameText(key))
      .filter(Objects::nonNull)
      .findFirst()
      .orElse(null);
  }

  Stream<RenderPacket> packetStream() { return packets.values().stream(); }

  public void bind(ERenderer shader, Model model) {
    packet(shader).queue(model);
  }

  public RenderPacket packet(ERenderer shader) {
    return packets.computeIfAbsent(shader, RenderPacket::new);
  }

  public void addPacket(RenderPacket packet) {
    packets.put(packet.destination(), packet);
  }

  public ArrayBlockingQueue<IRenderable> renderQueue(ERenderer renderer) {
    return packets.get(renderer).renderQueue();
  }

  public void bindQueue(ModelBinder binder) {
    packets.values().forEach((packet) -> packet.flush(binder));
  }
}
