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
  Map<Class<?>, Function<Class<?>, InstanceWrapper>> dependencies,
  boolean defaultConstructor,
  boolean loaded // FIXME: remove usage always inject dependency when `ApplicationContext.find()` is called
) {

  public static InstanceWrapper defaultConstructor(
    final Class<?> loadedClass,
    final Constructor<?> constructor
  ) throws InvocationTargetException, InstantiationException, IllegalAccessException {
    final Object instance = constructor.newInstance();

    final var classes = CoronavacUtils.loadTypeDependenciesFromClass(loadedClass);

    final var dependencies = getDependenciesMap(classes);

    return new InstanceWrapper(
      instance,
      dependencies,
      true,
      false
    );
  }

  private static Map<Class<?>, Function<Class<?>, InstanceWrapper>> getDependenciesMap(final Class<?>[] parameters) {
    return Arrays.stream(parameters).collect(Collectors.toMap(
      Function.identity(),
      parameter -> InstanceWrapper::lazyLoadInstance
    ));
  }

  private static InstanceWrapper lazyLoadInstance(final Class<?> parameter) {
    return Coronavac.instances.getOrDefault(parameter, null);
  }

  public static InstanceWrapper requiredFieldsConstructor(
    final Constructor<?> constructorWithFinalFields,
    final Class<?>[] parameters
  ) throws InvocationTargetException, InstantiationException, IllegalAccessException {

    final var dependencies = getDependenciesMap(parameters);

    final Object instance = constructorWithFinalFields.newInstance(
      dependencies.entrySet()
        .stream()
        .map(entry -> entry.getValue().apply(entry.getKey()))
        .toArray()
    );

    return new InstanceWrapper(
      instance,
      dependencies,
      false,
      true
    );
  }

  public static InstanceWrapper annotatedConstructor(
    final Constructor<?> constructor,
    final Class<?>[] parameters
  ) throws InvocationTargetException, InstantiationException, IllegalAccessException {

    final var dependencies = getDependenciesMap(parameters);

    final Object instance = constructor.newInstance(new Object[constructor.getParameterCount()]);

    return new InstanceWrapper(
      instance,
      dependencies,
      false,
      false
    );
  }


  public Optional<InstanceWrapper> getField(final Class<?> clazz) {
    final var objectFinderFunction = this.dependencies.getOrDefault(clazz, null);
    return Optional.ofNullable(objectFinderFunction)
      .map(fn -> fn.apply(clazz));
  }

}
