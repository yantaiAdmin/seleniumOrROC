package com.sp.tess;

import java.io.File;

/**
 * @author shipeng
 * @date 2022年11月24日 15:21:04
 */
public class FileTest {
    public static void main(String[] args) {
        File file = new File("D:\\桌面\\测试\\测试照片");
        Long a = 0L;
        File[] files = file.listFiles();
        for (File file1 : files) {
            a++;
            file1.renameTo(new File("D:\\桌面\\测试\\测试照片\\"+a+ ".jpg"));
            System.out.println(file1.getName());
        }
    }
}
