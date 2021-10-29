package com.tcmj.shampug.util;

import com.tcmj.shampug.intern.ShamPugException;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

public final class FileUtils {

    private FileUtils() {
    }

    /**
     * Creates a path handle either from a open (development) folder environment or from inside a jar file.
     * @param fileName full path or name of the file
     * @param refPoint reference point where to start to locate the file
     * @return a Path handle
     */
    public static Path getFilePath(final String fileName, Class<?> refPoint) {
        try {
            URI uri = Objects.requireNonNull(refPoint.getClassLoader().getResource(fileName)).toURI();
            if ("jar".equals(uri.getScheme())) {
                FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap(), null);
                return fileSystem.getPath(fileName);
            } else {
                return Paths.get(uri);
            }
        } catch (Exception ex) {
            throw new ShamPugException("Cannot get a file handle to " + fileName + "!", ex);
        }
    }

    /**
     * Reads all lines using {@link Collectors#joining()} to return them.
     * @param path file to be read
     * @return a simple concatenated string of all lines
     */
    public static String getUTF8FileContent(final Path path) {
        try {
            return Files.lines(path, StandardCharsets.UTF_8).collect(Collectors.joining());
        } catch (Exception ex) {
            throw new ShamPugException("Cannot get a file handle to " + path + "!", ex);
        }
    }
}