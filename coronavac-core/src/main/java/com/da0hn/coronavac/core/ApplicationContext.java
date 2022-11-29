package com.da0hn.coronavac.core;

public class ApplicationContext {


  public ApplicationContext(final Class<?> clazz) {
    Coronavac.initializeContext(clazz);
  }

  public <T> T find(final Class<T> targetClass) {
    @SuppressWarnings({"raw", "unchecked"})
    final var targetObject = (T) Coronavac.instances.get(targetClass);
    return targetObject;
  }

}
