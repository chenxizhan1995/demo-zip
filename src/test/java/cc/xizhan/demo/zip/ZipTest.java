package cc.xizhan.demo.zip;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;

import static org.junit.Assert.*;

public class ZipTest {
    @Test
    public void hello(){
        System.out.println("hello world");
    }
    @Test
    public void zipTest(){
        List<File> files = new ArrayList<>(2);
        files.add(new File("src/test/resources/hello.txt"));
        files.add(new File("src/test/resources/foo/foo.txt"));

        File zip = new File("target/foo.zip");
        zip(files, zip, "压缩测试");
    }
    /**
     *  传入文件清单，返回压缩后的文件。
     *  不保留目录结构，所以，要求没有同名文件
     */
    public File zip(List<File> files, File archive, String comment){
        try (ZipArchiveOutputStream outputStream = new ZipArchiveOutputStream(archive)) {
            // 可以设置压缩等级
            outputStream.setLevel(5);
            // 可以设置压缩算法，当前支持ZipEntry.DEFLATED和ZipEntry.STORED两种
            outputStream.setMethod(ZipEntry.DEFLATED);
            for (File file : files) {
                ZipArchiveEntry entry = new ZipArchiveEntry(file, file.getName());
                // 在zip中创建一个文件条目
                outputStream.putArchiveEntry(entry);
                // 写入文件内容
                if (!file.exists()){
                    throw new FileNotFoundException("文件不存在");
                }
                if (file.isFile()){
                    InputStream in = Files.newInputStream(file.toPath());
                    IOUtils.copy(in, outputStream);
                }
                outputStream.closeArchiveEntry();
            }
            outputStream.setComment(comment);
            outputStream.setEncoding("GBK");
            outputStream.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return archive;
    }
}