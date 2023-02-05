package com.da0hn.coronavac.core;

import java.lang.reflect.Field;

public class ApplicationContext {


  public ApplicationContext(final Class<?> clazz) {
    Coronavac.initializeContext(clazz);
  }

  @SuppressWarnings({"raw", "unchecked"})
  public <T> T find(final Class<T> targetClass) {
    try {
      final var instanceWrapper = Coronavac.instances.get(targetClass);

      if (instanceWrapper.loaded()) return (T) instanceWrapper.instance();

      final var instance = (T) instanceWrapper.instance();
      final var fields = targetClass.getDeclaredFields();

      for (final Field field : fields) {
        field.setAccessible(true);
        final Class<?> classField = field.getType();
        final var fieldInstance = instanceWrapper.getField(classField);

        // Does not change value of a field not found
        if(fieldInstance.isEmpty()) continue;

        field.set(instance, fieldInstance);
      }
      return instance;
    }
    catch (final IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

}
