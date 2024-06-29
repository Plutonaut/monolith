package com.game.utils.application;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class IteratorUtils {
  public static <T> Consumer<T> indexed(BiConsumer<T, Integer> consumer) {
    final AtomicInteger counter = new AtomicInteger(0);
    return item -> consumer.accept(item, counter.getAndIncrement());
  }
 }
