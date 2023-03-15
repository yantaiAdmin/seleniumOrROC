package com.sp.tess;

import lombok.SneakyThrows;
import net.sourceforge.tess4j.Tesseract;
import org.apache.poi.ss.formula.functions.T;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shipeng
 * @date 2023年03月14日
 */
public class Wnagyiyun {
    @SneakyThrows
    public static void main(String[] args) {
        BufferedReader reader = null;
        List<String> wordList = new ArrayList<>();
        //设置为 headless 模式 (必须)
        ChromeOptions chromeOptions = new ChromeOptions();
        //设置浏览器窗口打开大小 (非必须)
        //chromeOptions.addArguments("--headless");
        //System.setProperty("webdriver.chrome.driver", "chromedriver.exe"); 未将driver放到firefox安装路径进行这项配置
        WebDriver webDriver = new FirefoxDriver();
        //有道翻译地址
        webDriver.get("https://www.youdao.com/");
        try {
            reader = new BufferedReader(new FileReader("D:\\桌面\\auto.txt"));
            String s = "";
            while ((s = reader.readLine()) != null) {
                String[] split = s.split("（");
                if (split[0].length() > 0) {
                    wordList.add(split[0]);
                }
            }
            /**
             * 自动化逻辑开始  获取到text element不需要执行click 直接sendKey()输入就行
             * 清除text要重新搜索element
             */
            for (String element : wordList) {
                WebElement searchText = webDriver.findElement(By.id("search_input"));
                searchText.sendKeys(element);
                Thread.sleep(100);
                WebElement searchBtn = webDriver.findElement(By.className("translate_btn"));
                searchBtn.click();
                Thread.sleep(100);
                WebElement fire = webDriver.findElement(By.className("pronounce"));
                fire.click();
                Thread.sleep(2000);
                webDriver.findElement(By.id("search_input")).clear();
                Thread.sleep(2000);
            }


        } catch (FileNotFoundException e  ) {
            e.printStackTrace();
        }finally {
            webDriver.quit();
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
