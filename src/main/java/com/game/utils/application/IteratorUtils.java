package com.game.utils.application;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class IteratorUtils {
  public static <T> Consumer<T> indexed(BiConsumer<T, Integer> consumer) {
    final AtomicInteger counter = new AtomicInteger(0);
    return item -> consumer.accept(item, counter.getAndIncrement());
  }

  public static int getIndexOf(Collection<Integer> collection, int element) {
    int index = -1;
    Integer[] arr = collection.toArray(new Integer[0]);

    for (int i = 0; i < collection.size(); i++) {
      if (arr[i] == element) {
        index = i;
        break;
      }
    }
    return index;
  }
}
