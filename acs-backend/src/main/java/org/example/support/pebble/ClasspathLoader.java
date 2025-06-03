package org.example.support.pebble;

import io.pebbletemplates.pebble.error.LoaderException;
import io.pebbletemplates.pebble.loader.Loader;
import io.pebbletemplates.pebble.utils.PathUtils;

import java.io.*;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClasspathLoader implements Loader<String> {

    private static final Logger logger = LoggerFactory.getLogger(io.pebbletemplates.pebble.loader.ClasspathLoader.class);

    @Setter
    @Getter
    private String prefix;
    @Getter
    private String suffix;
    private String[] suffixes;
    @Setter
    @Getter
    private String charset;
    private final char expectedSeparator;
    private final ClassLoader rcl;

    public ClasspathLoader(ClassLoader classLoader) {
        this.charset = "UTF-8";
        this.expectedSeparator = '/';
        this.rcl = classLoader;
    }

    public ClasspathLoader() {
        this(io.pebbletemplates.pebble.loader.ClasspathLoader.class.getClassLoader());
    }

    public Reader getReader(String templateName) {

        for (String suffix : suffixes) {
            String location = this.getLocation(templateName, suffix);
            logger.debug("Looking for template in {}.", location);
            InputStream is = null;
            File file = new File(location);
            try {
                is = new FileInputStream(file);
            } catch (FileNotFoundException e) {
            }
            if(is == null){
                is = this.rcl.getResourceAsStream(location);
            }
            if (is == null) {
                throw new LoaderException((Throwable) null, "Could not find template \"" + location + "\"");
            } else {
                try {
                    return new BufferedReader(new InputStreamReader(is, this.charset));
                } catch (UnsupportedEncodingException var5) {
                }
            }
        }

        return null;

    }

    private String getLocation(String templateName, String suffix) {
        StringBuilder path = new StringBuilder(128);
        if (this.getPrefix() != null) {
            path.append(this.getPrefix());
            if (!this.getPrefix().endsWith(Character.toString(this.expectedSeparator))) {
                path.append(this.expectedSeparator);
            }
        }

        path.append(templateName);
        if (suffix != null) {
            path.append(suffix);
        }

        return path.toString();
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;

        if(suffix != null && !suffix.isBlank()){
            this.suffixes = suffix.split(",");
        }
    }

    public String resolveRelativePath(String relativePath, String anchorPath) {
        return PathUtils.resolveRelativePath(relativePath, anchorPath, this.expectedSeparator);
    }

    public String createCacheKey(String templateName) {
        return templateName;
    }

    public boolean resourceExists(String templateName) {

        for (String suffix : suffixes) {
            return this.rcl.getResource(this.getLocation(templateName, suffix)) != null;
        }

        return false;
    }
}
