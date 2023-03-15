package com.sp.tess;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.sp.tess.entity.Data;
import com.spire.xls.Workbook;
import com.spire.xls.Worksheet;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author shipeng
 * @date 2022年11月30日 13:53:28
 */
public class Version2 {
    public static void main(String[] args) throws InterruptedException, IOException {
        String log = "    111111111       1       1         1     1             1          1   1    1      \n" +
                "    11     11        11    111        11    11            11         111111   11  1  \n" +
                "    111111111         11  11  1      111    11            11         11 11 111111111 \n" +
                "    11     11      1111111111111     11     11           11          11 11   11      \n" +
                "    111111111           11   1      11      11           11      1   11 11  111      \n" +
                "                    11111111111     111 11  11  11  111111111111111  11 1   1 11     \n" +
                " 111111111111111        11     1   1 11 11  11  11      11   11      111   1  11  1  \n" +
                "   11 11     1    111111111111111    11 11  11  11      11   11      11 1  111111111 \n" +
                "   111111111111         11           11 11  11  11     11   11       11 11    11     \n" +
                "   11 111   11          11    1      11 11  11  11     111  11       11 11  1 111    \n" +
                "   11111 1  1      1111111111111     11 11  11  11       1111        11 11 11111 1   \n" +
                "   11 11 1 11          11 1          11 11  11  11        1111       11111 11 11 11  \n" +
                "   11 111 11          11   11        11 11  11  11       11 111      11 1 11  11 111 \n" +
                " 1111111  1 11       11     111      11 1111111111      11    111    11   1   11  11 \n" +
                "  1   11 1   111    11       1111    11 11      11    11       111   11  1  1111  1  \n" +
                "      1 1     1   11           1     11             11          1    11       1   ";
        System.out.println(log);
        //照片路径
        //‪C:\Users\Administrator\Desktop\11月
        String photoPath = "D:\\桌面\\测试\\测试照片";
        File xlsxFile = null;
        //爬虫获取有道云照片提取文字

        //System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        //设置为 headless 模式 (必须)
        ChromeOptions chromeOptions = new ChromeOptions();
        //设置浏览器窗口打开大小 (非必须)
        chromeOptions.addArguments("--headless");
        //创建自动化
        WebDriver webDriver = new ChromeDriver(chromeOptions);

        //打开url
        webDriver.get("https://ai.youdao.com/product-fanyi-picture.s");
        WebElement kw = webDriver.findElement(By.id("local-upload4"));
        kw.click();
        WebElement file = webDriver.findElement(By.id("upload-file-input4"));

        ////获取照片路径
        Map<Long, File> mapFile = new HashMap<>();
        Map<Long, String> sum = new HashMap<>();
        Long a = 0L;
        File fileImage = new File(photoPath);
        for (File filePath : fileImage.listFiles()) {
            String[] split = filePath.getName().split("\\.");
            if (split[split.length - 1].equals("xlsx") ) {
                xlsxFile = filePath;
            }
            a++;
            mapFile.put(a, filePath);
            String content = "";
            //获取文件窗口焦点传入路径
            file.sendKeys(filePath.getAbsolutePath());
            //等待2s网站反应时间
            Thread.sleep(2000);
            for (WebElement context : webDriver.findElements(By.className("context"))) {
                content += context.getAttribute("innerText");
            }
            String replace = content.replace(" ", "").replace(",", "");
            sum.put(a, replace);
        }
        webDriver.quit();
        //加载workbook
        Workbook wb = new Workbook();
        assert xlsxFile != null;
        wb.loadFromFile(xlsxFile.getAbsolutePath());
        Worksheet ws = wb.getWorksheets().get(0);

        //xlsx位置
        String xlsxPath = xlsxFile.getAbsolutePath();
        ArrayList<Data> data = new ArrayList<>();
        AtomicReference<Long> l = new AtomicReference<>(2L);
        EasyExcel.read(xlsxPath, Data.class, new PageReadListener<Data>(dataList -> {
            for (Data demoData : dataList) {
                demoData.setLocal("E" + l);
                l.updateAndGet(v -> v + 1);
                if (null == demoData.getPrefixName()) {
                    demoData.setPrefixName(data.get(data.size() - 1).getPrefixName());
                }
                data.add(demoData);
            }
        })).sheet().doRead();

        //配对匹配ps待完善 未做前后处理比对

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
                    resultFile.renameTo(new File(photoPath + "\\" + datum.getPrefixName() + "-" + datum.getSuffixName() + ".jpg"));
                    ws.getCellRange(datum.getLocal()).getStyle().setColor(new Color(255, 0, 0));
                    System.out.println("完成识别==>>" + resultFile);
                    break;
                }
            }
            //File resultFile = mapFile.get(key);
            //System.out.println("未完成的==>>" + resultFile.getAbsolutePath());
        }
        wb.saveToFile(xlsxPath);
        wb.dispose();

    }
}
