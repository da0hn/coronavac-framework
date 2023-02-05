package com.da0hn.coronavac.core;

import com.da0hn.coronavac.commons.exceptions.ApplicationClassNotFoundException;
import com.da0hn.coronavac.commons.exceptions.IllegalClassException;
import com.da0hn.coronavac.core.annotations.Component;
import com.da0hn.coronavac.core.annotations.CoronavacApplication;
import com.da0hn.coronavac.core.annotations.Get;
import com.da0hn.coronavac.core.annotations.Repository;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

final class Coronavac {


  static final Map<Class<?>, InstanceWrapper> instances = new HashMap<>();


  public static void initializeContext(final Class<?> applicationClass) {
    if (!applicationClass.isAnnotationPresent(CoronavacApplication.class)) {
      throw new ApplicationClassNotFoundException("Application class not found");
    }
    try {
      final var applicationClassMetadata = new ApplicationClassMetadata(applicationClass);

      final var classesAsFile = new ClassesDiscovery().find(applicationClassMetadata.baseFolder());

      final var classesWithFullname = classesAsFile.stream()
        .map(File::getPath)
        .map(applicationClassMetadata::fullClassName)
        .toList();

      registryInContainer(classesWithFullname);
    }
    catch (final Exception e) {
      throw new RuntimeException(e);
    }

  }


  private static void registryInContainer(final Iterable<String> classes) {
    try {
      for (final var fullClassName : classes) {

        final Class<?> loadedClass = Class.forName(fullClassName);

        if (loadedClass.isAnnotationPresent(Component.class) || loadedClass.isAnnotationPresent(Repository.class)) {
          final Constructor<?>[] constructors = loadedClass.getDeclaredConstructors();
          final var newInstance = getNewInstance(loadedClass, constructors);
          instances.put(loadedClass, newInstance);
        }
      }
    }
    catch (
      final ClassNotFoundException
            | InvocationTargetException
            | InstantiationException
            | IllegalAccessException e
    ) {
      throw new IllegalClassException(e);
    }
  }

  private static InstanceWrapper getNewInstance(
    final Class<?> loadedClass,
    final Constructor<?>[] constructors
  ) throws InvocationTargetException,
    InstantiationException, IllegalAccessException {
    final var maybeAnnotedConstructor = Arrays.stream(constructors)
      .filter(constructor -> constructor.isAnnotationPresent(Get.class))
      .reduce((a, b) -> {
        throw new IllegalStateException("Has more than one constructor annotated");
      });

    if (maybeAnnotedConstructor.isPresent()) {
      final Constructor<?> constructor = maybeAnnotedConstructor.get();

      final var parameterCount = constructor.getParameterCount();

      if (parameterCount == 0) return InstanceWrapper.defaultConstructor(loadedClass, constructor);

      return null; // TODO: instantiate loaded class using constructor annotated with `Get.class`
    } else {

      final var maybeInstance = maybeHandleNotAnnotatedDefaultConstructor(
        loadedClass,
        constructors
      );

      if (maybeInstance.isPresent()) return maybeInstance.get();

      return tryHandleDeclaredFields(loadedClass);
    }
  }

  private static Optional<InstanceWrapper> maybeHandleNotAnnotatedDefaultConstructor(
    final Class<?> loadedClass,
    final Constructor<?>[] constructors
  ) throws InvocationTargetException, InstantiationException, IllegalAccessException {
    final var maybeDefaultConstructor = Arrays.stream(constructors)
      .filter(constructor -> constructor.getParameterCount() == 0)
      .findFirst();

    return maybeDefaultConstructor.isPresent() ?
      Optional.of(InstanceWrapper.defaultConstructor(loadedClass, maybeDefaultConstructor.get())) :
      Optional.empty();
  }

  private static InstanceWrapper tryHandleDeclaredFields(final Class<?> loadedClass)
    throws InvocationTargetException, InstantiationException, IllegalAccessException {
    try {
      final var declaredFields = loadedClass.getDeclaredFields();

      final var finalDeclaredFields = Arrays.stream(declaredFields)
        .filter(field -> Modifier.isFinal(field.getModifiers()))
        .toList();

      final var parameters = finalDeclaredFields.stream()
        .map(Field::getType)
        .toArray(size -> new Class<?>[size]);

      final Constructor<?> constructorWithFinalFields = loadedClass.getDeclaredConstructor(parameters);

      return InstanceWrapper.requiredFieldsConstructor(
        constructorWithFinalFields,
        parameters
      );
    }
    catch (final NoSuchMethodException e) {
      throw new IllegalStateException("Non-annotated constructor compatible was found", e);
    }
  }

}
