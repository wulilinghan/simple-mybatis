package top.b0x0.mybatis;

import cn.hutool.core.io.resource.ResourceUtil;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tlh Created By 2022-07-25 20:30
 **/
public class ResourceTest {

    @Test
    public void test_1() {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("mybatis/mapper");
        String path = resource.getPath();
        System.out.println("path = " + path);
        File file = new File(path);
        List<File> files = new ArrayList<>();
        getAllFile(files, file);
    }

    public void getAllFile(List<File> fileList, File file) {
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File childFile : listFiles) {
                    getAllFile(fileList, childFile);
                }
            } else {
                fileList.add(file);
            }
        }
    }

    @Test
    public void test() {
        List<URL> resources = ResourceUtil.getResources("mybatis/mapper");
        for (URL resource : resources) {
            System.out.println("resource = " + resource);
        }
    }
}
