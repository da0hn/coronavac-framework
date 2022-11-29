package com.da0hn.coronavac.core;

import com.da0hn.coronavac.commons.exceptions.ConfigurationNotFoundException;
import com.da0hn.coronavac.commons.exceptions.IllegalClassException;
import com.da0hn.coronavac.commons.exceptions.PackageNotFoundException;
import com.da0hn.coronavac.core.annotations.Component;
import com.da0hn.coronavac.core.annotations.Configuration;
import com.da0hn.coronavac.core.annotations.ScanPackage;

import java.io.File;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public final class Coronavac {


  static final Map<Class<?>, Object> instances = new HashMap<>();
  private static final String BASE_FOLDER = "classes/";
  private static final String JAVA_CLASS_EXTENSION = ".java";
  private static final String COMPILED_JAVA_CLASS_EXTENSION = ".class";

  public static void initializeContext(final AnnotatedElement configurationClass) {
    if (!configurationClass.isAnnotationPresent(Configuration.class)) {
      throw new ConfigurationNotFoundException("Configuration class not found");
    }
    final var annotation = configurationClass.getAnnotation(ScanPackage.class);
    final var basePackage = annotation.value();

    final var packageSystemPath = BASE_FOLDER + basePackage.replace(".", "/");
    final var classes = findClasses(new File(packageSystemPath));
    registryInContainer(basePackage, classes);
  }

  private static File[] findClasses(final File file) {
    if (!file.exists()) {
      throw new PackageNotFoundException("Package %s not found", file.getName());
    }
    return file.listFiles(f -> f.getName().endsWith(COMPILED_JAVA_CLASS_EXTENSION));
  }

  private static void registryInContainer(
    final String basePackage,
    final File[] classes
  ) {
    try {
      for (final var aClass : classes) {
        final String className = String.format("%s.%s", basePackage, mountJavaClass(aClass.getName()));
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
