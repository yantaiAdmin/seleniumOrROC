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
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author shipeng
 * @date 2022年11月25日 14:40:00
 */
public class vsersin1 {
    public static void main(String[] args) {
        //tesserct配置路径
        String tessearctPath = "F:\\Environment\\tesseract\\tessdata";
        //配置语言不用语言加号拼接
        String tessearctLanguage = "chi_sim+chi_sim_vert";
        //照片路径
        String photoPath = "D:\\桌面\\测试\\测试照片";
        //xlsx位置
        String xlsxPath = "D:\\桌面\\测试\\测试数据.xlsx";
        //加载tessearct
        File file = new File(tessearctPath);
        Tesseract tesseract = new Tesseract();
        tesseract.setLanguage(tessearctLanguage);
        tesseract.setDatapath(file.getAbsolutePath());

        //加载workbook
        Workbook wb = new Workbook();
        wb.loadFromFile(xlsxPath);
        Worksheet ws = wb.getWorksheets().get(0);

        Map<Long, File> mapFile = new HashMap<>();
        Map<Long, String> sum = new HashMap<>();
        Long a = 0L;
        try {
            for (File listFile : new File(photoPath).listFiles()) {
                a++;
                mapFile.put(a, listFile);
                String result = tesseract.doOCR(listFile);
                String replace = result.replace(" ", "").replace(",", "");
                sum.put(a, replace);
            }
        } catch (TesseractException e) {
            e.printStackTrace();
        }

        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        // 这里每次会读取100条数据 然后返回过来 直接调用使用数据就行
        //TODO 加载excel数据 ps：单元格位置写死非动态
        ArrayList<Data> data = new ArrayList<>();
        AtomicReference<Long> l = new AtomicReference<>(2L);
        EasyExcel.read(xlsxPath, Data.class, new PageReadListener<Data>(dataList -> {
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
            Set<Map.Entry<Long, String>> entries = sum.entrySet();
            String val = null;
            Long key = null;
            for (Map.Entry<Long, String> longStringEntry : entries) {
                 val = longStringEntry.getValue();
                 key = longStringEntry.getKey();
                String prefixName = datum.getPrefixName();
                String finance = datum.getFinance();
                String suffixName = datum.getSuffixName();
                if (val.contains(prefixName) && val.contains(finance) && val.contains(suffixName)) {
                    File resultFile = mapFile.get(key);
                    resultFile.renameTo(new File(photoPath+"\\" + datum.getPrefixName() + "-" + datum.getSuffixName() + ".jpg"));
                    ws.getCellRange(datum.getLocal()).getStyle().setColor(new Color(255, 0, 0));
                    System.out.println("完成识别==>>" + resultFile);
                    break;
                }
            }
            File resultFile = mapFile.get(key);
            System.out.println("未完成的==>>" + resultFile);
        }
        wb.saveToFile(xlsxPath);
        wb.dispose();
    }
}
