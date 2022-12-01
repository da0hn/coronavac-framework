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

import static com.da0hn.coronavac.core.Constants.COMPILED_JAVA_CLASS_EXTENSION;
import static com.da0hn.coronavac.core.Constants.JAVA_CLASS_EXTENSION;

final class Coronavac {


  static final Map<Class<?>, Object> instances = new HashMap<>();
  private static final String BASE_FOLDER = "file:target/classes/";


  public static void initializeContext(final Class<?> applicationClass) {
    if (!applicationClass.isAnnotationPresent(CoronavacApplication.class)) {
      throw new ApplicationClassNotFoundException("Application class not found");
    }
    try {
      final var applicationClassMetadata = new ApplicationClassMetadata(applicationClass);

      final var classesAsFile = new ClassesDiscovery().find(applicationClassMetadata.baseFolder());

      // test file -> package extraction
      final var foo = classesAsFile.stream()
        .map(File::getPath)
        .map(applicationClassMetadata::extractPackageFrom)
        .toList();

      registryInContainer(applicationClassMetadata.basePackage(), classesAsFile);
    }
    catch (final Exception e) {
      throw new RuntimeException(e);
    }

  }


  private static void registryInContainer(
    final String basePackage,
    final Iterable<? extends File> classesAsFile
  ) {
    try {
      for (final var file : classesAsFile) {

        final String className = String.format("%s.%s", basePackage, mountJavaClass(file.getName()));

        final Class<?> loadedClass = Class.forName(className);

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

  private static String mountJavaClass(final String name) {
    return name.replace(COMPILED_JAVA_CLASS_EXTENSION, JAVA_CLASS_EXTENSION);
  }

}
