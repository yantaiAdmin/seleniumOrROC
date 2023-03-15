package com.sp.tess;

import cn.hutool.core.text.CharSequenceUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.sp.tess.entity.Data;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author shipeng
 * @date 2022年11月24日 14:38:02
 */

public class JavaBase {
    public static void main(String[] args) {

        String fileName ="D:\\桌面\\测试\\测试数据.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        // 这里每次会读取100条数据 然后返回过来 直接调用使用数据就行
        ArrayList<Data> data = new ArrayList<>();
        AtomicReference<Long> l = new AtomicReference<>(2L);
        EasyExcel.read(fileName, Data.class, new PageReadListener<Data>(dataList -> {
            for (Data demoData : dataList) {
                demoData.setLocal("E"+l);
                l.updateAndGet(v -> v + 1);
                if (CharSequenceUtil.isEmpty(demoData.getPrefixName())) {
                    demoData.setPrefixName(data.get(data.size() - 1).getPrefixName());
                }
                data.add(demoData);
                System.out.println(demoData);
            }
        })).sheet().doRead();

    }
}
