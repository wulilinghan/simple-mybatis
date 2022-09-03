package top.b0x0.mybatis.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * @author tlh Created By 2022-07-24 18:50
 **/
public class Resources {
    private static final Logger log = LoggerFactory.getLogger(Resources.class);

    public static List<Reader> getResourceAsReaders(String relativePath) {
        List<Reader> readers = new ArrayList<>();
        for (ClassLoader classLoader : getClassLoader()) {
            if (classLoader != null) {
                URL resource = classLoader.getResource(relativePath);
                if (resource == null) {
                    throw new RuntimeException("Resource " + relativePath + " not found");
                }
                String path = resource.getPath();
                File file = new File(path);
                log.info("file: {}", file);
                List<File> files = new ArrayList<>();
                getAllFile(files, file);
                for (File tempFile : files) {
                    if (!isXmlDocument(tempFile)) {
                        continue;
                    }
                    Reader input = null;
                    try {
                        input = new FileReader(tempFile);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    readers.add(input);
                }
                return readers;
            }
        }
        return readers;
    }

    public static void getAllFile(List<File> fileList, File file) {
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File childFile : listFiles) {
                    getAllFile(fileList, childFile);
                }
            }
        } else {
            fileList.add(file);
        }
    }

    private static boolean isXmlDocument(File file) {
        return file.getName().endsWith(".xml");
        /*
            boolean flag = true;
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                builder.parse(file);
            } catch (Exception e) {
                flag = false;
            }
            return flag;
        */
    }

    public static Reader getResourceAsReader(String resource) throws IOException {
        return new InputStreamReader(getResourceAsStream(resource));
    }

    public static InputStream getResourceAsStream(String resource) throws IOException {
        for (ClassLoader classLoader : getClassLoader()) {
            if (classLoader != null) {
                InputStream resourceAsStream = classLoader.getResourceAsStream(resource);
                if (resourceAsStream != null) {
                    return resourceAsStream;
                }
            }
        }
        throw new IOException("Could not find resource " + resource);
    }

    private static ClassLoader[] getClassLoader() {
        ClassLoader[] classLoader = new ClassLoader[]{
                ClassLoader.getSystemClassLoader(),
                Thread.currentThread().getContextClassLoader()
        };
        return classLoader;
    }

    public static Class<?> classForName(String className) throws ClassNotFoundException {
        return Class.forName(className);
    }
}
