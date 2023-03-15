package com.sp.tess.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shipeng
 * @date 2022年11月24日 14:52:23
 */
@Getter
@Setter
@EqualsAndHashCode
public class Data {
    @ExcelProperty(index = 4)
    String prefixName;
    @ExcelProperty(index = 8)
    String finance;
    @ExcelProperty(index = 9)
    String suffixName;
    String local;

    @Override
    public String toString() {
        return "Data{" +
                "prefixName='" + prefixName + '\'' +
                ", finance='" + finance + '\'' +
                ", suffixName='" + suffixName + '\'' +
                ", local='" + local + '\'' +
                '}';
    }
}
