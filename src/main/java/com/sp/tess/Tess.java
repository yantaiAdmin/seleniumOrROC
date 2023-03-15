package com.sp.tess;

import com.spire.xls.core.IFont;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shipeng
 * @date 2022年11月24日 13:35:49
 */
public class Tess {

    public static void main(String[] args) {
        File file = new File("F:\\Environment\\tesseract\\tessdata");
        net.sourceforge.tess4j.Tesseract tesseract = new net.sourceforge.tess4j.Tesseract();
        //chi_tra\chi_tra_vert
        tesseract.setLanguage("chi_sim");
        tesseract.setDatapath(file.getAbsolutePath());
        tesseract.setTessVariable("user_defined_dpi", "70");
        Map<Long, File> mapFile = new HashMap<>();
        Map<Long, String> sum = new HashMap<>();
        Long a = 0L;
        try {
            for (File listFile : new File("D:\\桌面\\测试\\测试照片").listFiles()) {
                a++;
                mapFile.put(a, listFile);
                String result = tesseract.doOCR(listFile);
                String replace = result.replace(" ", "").replace(",", "");
                sum.put(a, replace);
                System.out.println(replace);
                System.out.println("==================");
            }
        } catch (TesseractException e) {
            e.printStackTrace();
        }
    }

}
