package com.sp.tess;


import com.spire.xls.Workbook;
import com.spire.xls.Worksheet;

import java.awt.*;

/**
 * @author shipeng
 * @date 2022年11月24日 16:16:01
 */
public class TestStyle {
    public static void main(String[] args) {
        Workbook wb = new Workbook();
        wb.loadFromFile("D:\\桌面\\11月18日 世创汇致 第一批(1).xlsx");
        Worksheet ws = wb.getWorksheets().get(0);
        ws.getCellRange("E2").getStyle().setColor(new Color(255, 0, 0));
        wb.saveToFile("D:\\桌面\\11月18日 世创汇致 第一批(1).xlsx");
        wb.dispose();

    }

}

