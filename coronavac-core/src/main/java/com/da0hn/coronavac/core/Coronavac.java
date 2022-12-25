package com.da0hn.coronavac.core;

import com.da0hn.coronavac.commons.exceptions.ApplicationClassNotFoundException;
import com.da0hn.coronavac.commons.exceptions.IllegalClassException;
import com.da0hn.coronavac.core.annotations.Component;
import com.da0hn.coronavac.core.annotations.CoronavacApplication;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

final class Coronavac {


  static final Map<Class<?>, Object> instances = new HashMap<>();


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

        if (loadedClass.isAnnotationPresent(Component.class)) {
          final Constructor<?> constructor = loadedClass.getConstructor();
          final Object newInstance = constructor.newInstance();
          instances.put(loadedClass, newInstance);
        }
      }
    }
    catch (
      final ClassNotFoundException
            | NoSuchMethodException
            | InvocationTargetException
            | InstantiationException
            | IllegalAccessException e
    ) {
      throw new IllegalClassException(e);
    }
  }

}
