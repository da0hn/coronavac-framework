package com.da0hn.coronavac.core;

import java.nio.file.FileSystems;

import static com.da0hn.coronavac.core.Constants.PACKAGE_SEPARATOR;

record ApplicationClassMetadata(Class<?> applicationClass) {


  public String baseFolder() {
    return this.basePackage().replace(PACKAGE_SEPARATOR, FileSystems.getDefault().getSeparator());
  }

  public String basePackage() {
    return this.applicationClass.getPackageName();
  }

  public String extractPackageFrom(final String fileSystemPath) {
    final String firstPackage = this.firstPackage();
    final var indexFirstFolderEquivalent = fileSystemPath.indexOf(firstPackage);
    final var fullDirectoriesPath = fileSystemPath.substring(indexFirstFolderEquivalent);
    return toPackage(fullDirectoriesPath);
  }

  private String firstPackage() {
    final var indexFirstFileSeparator = this.basePackage().indexOf(PACKAGE_SEPARATOR);
    return this.basePackage().substring(0, indexFirstFileSeparator);
  }

  private static String toPackage(final String path) {
    return path.replace(FileSystems.getDefault().getSeparator(), ".");
  }

}
