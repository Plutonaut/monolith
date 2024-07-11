package com.game.engine.render.pipeline.packets;

import com.game.engine.render.models.Model;
import com.game.engine.scene.entities.Entity;
import com.game.utils.enums.ERenderer;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.BiConsumer;

public class PacketManager {
  private final HashMap<ERenderer, RenderPacket> packets;

  public PacketManager() {
    packets = new HashMap<>();
  }

  public void bind(ERenderer shader, Model model) {
    packet(shader).queue(model);
  }

  public RenderPacket packet(ERenderer shader) {
    return packets.computeIfAbsent(shader, RenderPacket::new);
  }

  public void addPacket(RenderPacket packet) {
    packets.put(packet.destination(), packet);
  }

  public ArrayBlockingQueue<Entity> renderQueue(ERenderer renderer) {
    return packets.get(renderer).renderQueue();
  }

  public void stream(BiConsumer<ERenderer, RenderPacket> consumer) {
    packets.forEach(consumer);
  }
}
