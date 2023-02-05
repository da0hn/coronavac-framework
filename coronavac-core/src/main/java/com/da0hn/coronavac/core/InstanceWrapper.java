package com.da0hn.coronavac.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public record InstanceWrapper(
  Object instance,
  Map<Class<?>, Object> dependencies,
  boolean defaultConstructor,
  boolean loaded
) {

  public static InstanceWrapper defaultConstructor(
    final Class<?> loadedClass,
    final Constructor<?> constructor
  ) throws InvocationTargetException, InstantiationException, IllegalAccessException {
    final Object instance = constructor.newInstance();

    final var classes = CoronavacUtils.loadTypeDependenciesFromClass(loadedClass);

    final var dependencies = Arrays.stream(classes)
      .collect(Collectors.toMap(
        Function.identity(),
        InstanceWrapper::lazyLoadInstance
      ));

    return new InstanceWrapper(
      instance,
      dependencies,
      true,
      false
    );
  }

  public static InstanceWrapper requiredFieldsConstructor(
    final Constructor<?> constructorWithFinalFields,
    final Class<?>[] parameters
  ) throws InvocationTargetException, InstantiationException, IllegalAccessException {

    final var dependencies = Arrays.stream(parameters).collect(Collectors.toMap(
      Function.identity(),
      InstanceWrapper::lazyLoadInstance
    ));

    final Object instance = constructorWithFinalFields.newInstance(dependencies.values());

    return new InstanceWrapper(
      instance,
      dependencies,
      false,
      true
    );
  }

  private static Object lazyLoadInstance(final Class<?> parameter) {
    return Coronavac.instances.getOrDefault(parameter, null).instance;
  }


  public Optional<Object> getField(final Class<?> clazz) {
    return Optional.ofNullable(this.dependencies.getOrDefault(clazz, null));
  }

}
