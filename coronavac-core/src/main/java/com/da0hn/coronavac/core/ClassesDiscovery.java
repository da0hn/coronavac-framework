package com.da0hn.coronavac.core;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static com.da0hn.coronavac.core.Constants.COMPILED_JAVA_CLASS_EXTENSION;

public class ClassesDiscovery {

  public List<File> find(final String baseFolder) {
    try {
      final var resource = ClassLoader.getSystemClassLoader().getResource(baseFolder);
      final var file = Paths.get(resource.toURI()).toFile();

      return findAllClassesInFolder(file.getParent());
    }
    catch (final URISyntaxException exception) {
      throw new RuntimeException(exception);
    }
  }


  private static List<File> findAllClassesInFolder(final String parent) {
    try (final var fileWalkerStream = Files.walk(Paths.get(parent))) {
      return fileWalkerStream.map(Path::toString)
        .filter(path -> path.endsWith(COMPILED_JAVA_CLASS_EXTENSION))
        .map(File::new)
        .collect(Collectors.toList());
    }
    catch (final Exception exception) {
      throw new RuntimeException(exception);
    }
  }

}
