package com.sp.tess;

import cn.hutool.core.text.CharSequenceUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.sp.tess.entity.Data;
import com.spire.xls.Workbook;
import com.spire.xls.Worksheet;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author shipeng
 * @date 2022年11月25日 09:32:11
 * https://jingyan.baidu.com/article/870c6fc3359581f13ee4be5c.html
 */
public class main {

    public static void main(String[] args) {
        File file = new File("F:\\Environment\\tesseract\\tessdata");
        Tesseract tesseract = new Tesseract();
        tesseract.setLanguage("chi_sim+chi_sim_vert");
        tesseract.setDatapath(file.getAbsolutePath());
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
            }
        } catch (TesseractException e) {
            e.printStackTrace();
        }

        Workbook wb = new Workbook();
        wb.loadFromFile("D:\\桌面\\测试\\测试数据.xlsx");
        Worksheet ws = wb.getWorksheets().get(0);


        String fileName = "D:\\桌面\\测试\\测试数据.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        // 这里每次会读取100条数据 然后返回过来 直接调用使用数据就行
        ArrayList<Data> data = new ArrayList<>();
        AtomicReference<Long> l = new AtomicReference<>(2L);
        EasyExcel.read(fileName, Data.class, new PageReadListener<Data>(dataList -> {
            for (Data demoData : dataList) {
                demoData.setLocal("E" + l);
                l.updateAndGet(v -> v + 1);
                if (CharSequenceUtil.isEmpty(demoData.getPrefixName())) {
                    demoData.setPrefixName(data.get(data.size() - 1).getPrefixName());
                }
                data.add(demoData);
            }
        })).sheet().doRead();
        for (Data datum : data) {
            for (Map.Entry<Long, String> longStringEntry : sum.entrySet()) {
                String val = longStringEntry.getValue();
                Long key = longStringEntry.getKey();
                String prefixName = datum.getPrefixName();
                String finance = datum.getFinance();
                String suffixName = datum.getSuffixName();
                if (val.contains(prefixName) && val.contains(finance) && val.contains(suffixName)) {
                    File resultFile = mapFile.get(key);
                    resultFile.renameTo(new File("D:\\桌面\\测试\\测试照片\\" + datum.getPrefixName() + "-" + datum.getSuffixName() + ".jpg"));
                    ws.getCellRange(datum.getLocal()).getStyle().setColor(new Color(255, 0, 0));
                    System.out.println("存在==>>" + resultFile);
                    break;
                } else {
                    File resultFile = mapFile.get(key);
                    System.out.println("不存在==>>" + resultFile);
                }
            }

        }
        wb.saveToFile("D:\\桌面\\测试\\测试数据.xlsx");
        wb.dispose();
    }
}
