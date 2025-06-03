package org.example.support.core;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClassLoaderCore {

    public static String getClassPathString() {

        ClassLoader classLoader = ClassLoaderCore.class.getClassLoader();

        // Get the URL for the classpath
        URL url = classLoader.getResource("");

        // Convert URL to a Path
        String pathString = url.getPath();

        // Check if the path starts with a drive letter (Windows)
        if (pathString.startsWith("/")) {
            // Remove the leading slash
            pathString = pathString.substring(1);
        }

        // Decode the URL
        try {
            pathString = URLDecoder.decode(pathString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }

        return pathString;

    }

    public static Path getClassPath(String prefix) {

        ClassLoader classLoader = ClassLoaderCore.class.getClassLoader();

        // Get the URL for the classpath
        URL url = classLoader.getResource(prefix);

        // Convert URL to a Path
        String pathString = url.getPath();

        // Check if the path starts with a drive letter (Windows)
        if (pathString.startsWith("/")) {
            // Remove the leading slash
            pathString = pathString.substring(1);
        }

        // Decode the URL
        try {
            pathString = URLDecoder.decode(pathString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }

        Path path = Paths.get(pathString);

        System.out.println("prefix: " + prefix);
        System.out.println("path: " + path);

        return path;

    }

    public static Path getClassResourcePath(String templateName) {

        ClassLoader classLoader = ClassLoaderCore.class.getClassLoader();

        URL url = classLoader.getResource(templateName);
        if (url != null) {
            try {
                return Paths.get(url.toURI());
            }catch (Exception ignored){
            }
        }

        return null;

    }

    public static InputStream getClassResource(String templateName) {

        ClassLoader classLoader = ClassLoaderCore.class.getClassLoader();

        InputStream inputStream = classLoader.getResourceAsStream(templateName);

        if (inputStream != null) {
            System.out.println("File exists: " + templateName);
            return inputStream;
        } else {
            System.out.println("File not found: " + templateName);
            return null;
        }

    }

}