package org.example.support.pebble;

import io.pebbletemplates.pebble.loader.Loader;
import jakarta.servlet.ServletContext;
import org.example.support.core.ClassLoaderCore;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;


public class ServletContextLoader implements Loader<String> {

    private String prefix;
    private String suffix;
    private Charset charset;
    private ServletContext servletContext;

    //新增後缀詞的多樣性檔案
    private String[] suffixes;

    public ServletContextLoader(ServletContext servletContext) {
        super();
        this.prefix = ClassLoaderCore.getClassPath("") + "templates";
        this.suffix = ".peb";
        this.suffixes = this.suffix.split(",");
        this.charset = StandardCharsets.UTF_8;
        this.servletContext = servletContext;
    }

    @Override
    public Reader getReader(String templateName) {

        if(resourceExists(templateName)) {

            for (String suffix : suffixes) {

                Path path;
                String templatePath = templateName + suffix;
                if(!checkHavePrefix(templateName, "templates")){
                    path = ClassLoaderCore.getClassPath("").resolve(prefix + templatePath);
                }else {
                    path = ClassLoaderCore.getClassPath("").resolve(templatePath);
                }

                String pathString = transferDot(path.toString());
                path = Paths.get(pathString);

                if (Files.exists(path)) {
                    try {
                        return new BufferedReader(new InputStreamReader(Files.newInputStream(path), StandardCharsets.UTF_8));
                    } catch (Exception e) {
                    }
                } else {
                    System.out.println("Template file does not exist.");
                }

            }

        }

        return null;

    }

    @Override
    public void setCharset(String  charset) {
        this.charset = Charset.forName(charset);
    }

    @Override
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void setSuffix(String suffix) {
        this.suffix = suffix;

        if(suffix != null && !suffix.isBlank()){
            this.suffixes = suffix.split(",");
        }
    }

    @Override
    public String resolveRelativePath(String relativePath, String anchorPath) {
        return this.prefix + relativePath;
    }

    @Override
    public String createCacheKey(String templateName) {
        return Objects.requireNonNull(templateName);
    }

    @Override
    public boolean resourceExists(String templateName) {

        for (String suffix : suffixes) {

            System.out.println(" ---------------------------------- ServletContextLoader.resourceExists Start ----------------------------------");

            Path path;
            String templatePath = templateName + suffix;
            if(!checkHavePrefix(templateName, "templates")){
                path = ClassLoaderCore.getClassPath("").resolve(prefix + templatePath);
            }else {
                path = ClassLoaderCore.getClassPath("").resolve(templatePath);
            }

            String pathString = transferDot(path.toString());
            path = Paths.get(pathString);

            System.out.println(servletContext.getRealPath("/"));
            System.out.println(servletContext.getRealPath("/abc"));

            String programPath = servletContext.getRealPath(templatePath);
            System.out.println("programPath：" + programPath);
            System.out.println("ContextPath：" + servletContext.getContextPath());

            System.out.println("path: " + path.toString());
            System.out.println("templateName: " + templateName);
            System.out.println("templatePath: " + templatePath);

            Path relativePath = Paths.get(templateName);
            Path currentDirectory = Paths.get(System.getProperty("user.dir"));
            Path absolutePath = currentDirectory.resolve(relativePath);
            System.out.println("relativePath: " + relativePath);
            System.out.println("currentDirectory: " + currentDirectory);
            System.out.println("absolutePath: " + absolutePath);

            System.out.println(" ---------------------------------- ServletContextLoader.resourceExists End ----------------------------------");

            if (Files.exists(path)) {
                try (Reader templateReader = new BufferedReader(new InputStreamReader(Files.newInputStream(path)))) {
                    return true;
                } catch (IOException ignored) {
                }
            } else {
                System.out.println("Template file does not exist.");
            }

        }

        return false;
    }

    public boolean checkHavePrefix(String templateName, String prefix){
        if (templateName.contains(prefix + "/") || templateName.contains(prefix + "\\")) {
            return true;
        }else{
            return false;
        }
    }

    public String transferDot(String templateName){

        if (templateName.contains("../") || templateName.contains("..\\") ||
            templateName.startsWith("../") || templateName.startsWith("..\\") ||
            templateName.endsWith("/..") || templateName.endsWith("\\..")) {

            templateName = templateName.replaceAll("\\.\\./", "").replaceAll("\\.\\.", "");

        }

        return templateName;
    }

}