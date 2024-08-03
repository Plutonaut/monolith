package com.game.engine.render.pipeline.packets;

import com.game.utils.enums.ERenderer;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

public class PacketManager {
  private final HashMap<ERenderer, RenderPacket> packets;

  public PacketManager() {
    packets = new HashMap<>();
  }

  public boolean contains(ERenderer shader) {
    return packets.containsKey(shader);
  }

  public RenderPacket packet(ERenderer shader) {
    return packets.computeIfAbsent(shader, RenderPacket::new);
  }

  public void bind(ERenderer shader, String entityName) {
    packet(shader).bind(entityName);
  }

  public void unbind(ERenderer shader, String entityName) {
    packet(shader).unbind(entityName);
  }

  public ArrayBlockingQueue<String> renderQueue(ERenderer renderer) {
    return packet(renderer).renderQueue();
  }
}
