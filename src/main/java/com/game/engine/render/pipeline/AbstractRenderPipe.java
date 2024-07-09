package com.game.engine.render.pipeline;

import java.util.concurrent.ArrayBlockingQueue;

public abstract class AbstractRenderPipe implements IRenderPipe {
  static final int MAX_QUEUE_SIZE = 16;
  protected final ArrayBlockingQueue<IRenderPacket> queue;

  public AbstractRenderPipe() {
    queue = new ArrayBlockingQueue<>(MAX_QUEUE_SIZE);
  }

  public abstract IRenderPacket onPoll(IRenderPacket packet);

  @Override
  public AbstractRenderPipe receive(IRenderPacket packet) {
    if (!queue.offer(packet)) throw onPipeFailure(
      "Receive",
      "Failed to receive packet with destination: " + packet.destination()
    );
    return this;
  }

  @Override
  public IRenderPipe pipe(IRenderPipe receiver) {
    StringBuilder builder = new StringBuilder();
    if (receiver == null) builder.append("Receiver is null!");
    else {
      if (queue.isEmpty()) builder.append(System.lineSeparator()).append("Queue is empty!");
      else {
        IRenderPacket packet = onPoll(queue.poll());

        if (packet == null) builder
          .append("Packet is null! Failed to send packet to: ")
          .append(receiver.getClass().getName());
        else return receiver.receive(packet);
      }
    }
    throw onPipeFailure("Pipe", builder.toString());
  }

  protected void typeCheck(IRenderPacket packet, Class<?> type) {
    if (!packet.getClass().isInstance(type))
      throw onPipeFailure("Type Check", "Failed! Could not convert packet to " + type.getName());
  }

  protected RuntimeException onPipeFailure(String method, String message) {
    String errorMsg = "ERROR: " + method + " in process " + this
      .getClass()
      .getName() + System.lineSeparator() + "In queue: " + queue.size() + message;

    return new RuntimeException(errorMsg);
  }
}
