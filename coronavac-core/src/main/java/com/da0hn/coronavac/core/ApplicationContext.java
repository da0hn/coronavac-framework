package com.da0hn.coronavac.core;

import java.lang.reflect.Field;

public class ApplicationContext {


  public ApplicationContext(final Class<?> clazz) {
    Coronavac.initializeContext(clazz);
  }

  @SuppressWarnings({"raw"})
  private static <T> void injectDependency(
    final Class<T> targetClass,
    final InstanceWrapper instanceWrapper
  ) throws IllegalAccessException {
    final var fields = targetClass.getDeclaredFields();

    for (final Field field : fields) {
      field.setAccessible(true);

      final Class<?> classField = field.getType();

      final var maybeFieldInstanceWrapper = instanceWrapper.getField(classField);

      // Does not change value of a field not found
      if (maybeFieldInstanceWrapper.isEmpty()) continue;

      field.set(instanceWrapper.instance(), maybeFieldInstanceWrapper.get().instance());
      injectDependency(classField, maybeFieldInstanceWrapper.get());
    }
  }

  public <T> T find(final Class<T> targetClass) {
    try {
      final var instanceWrapper = Coronavac.instances.get(targetClass);

      @SuppressWarnings({"raw", "unchecked"})
      final var instance = (T) instanceWrapper.instance();

      injectDependency(targetClass, instanceWrapper);

      return instance;
    }
    catch (final IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

}
