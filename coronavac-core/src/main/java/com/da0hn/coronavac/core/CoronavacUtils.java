package com.da0hn.coronavac.core;

import java.lang.reflect.Field;
import java.util.Arrays;

public final class CoronavacUtils {

  private CoronavacUtils() {}

  public static Class<?>[] loadTypeDependenciesFromClass(final Class<?> clazz) {
    final var declaredFields = clazz.getDeclaredFields();
    return Arrays.stream(declaredFields)
      .map(Field::getType)
      .toArray(size -> new Class<?>[size]);
  }

}
