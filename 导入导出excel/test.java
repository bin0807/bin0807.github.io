
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class test {
    public static void main(String[] args) throws Exception {
        showExcel();
    }

    // 读取，全部sheet表及数据
    public static void showExcel() throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File("E:/QMDownload/a.xls")));
        XSSFSheet sheet = null;
        Map map=new HashMap();
        String b="[；'，；：。，、]";
        // 获取每个Sheet表
            sheet = workbook.getSheetAt(0);
            for (int j = 1; j < 61510; j++) {// getLastRowNum，获取最后一行的行标
                XSSFRow row = sheet.getRow(j);
                if (row != null) {
                  // getLastCellNum，是获取最后一个不为空的列是第几个
                        String[] a=row.getCell(11).toString().split(b);
                        for(int i=0;i<a.length;i++){
                                if(map.containsKey(a[i])){
                                    Integer g= (Integer) map.get(a[i]);
                                    map.put(a[i],g+1);
                                }else{
                                    Integer g=1;
                                    map.put(a[i],g);
                                }
                        }
                    }
                System.out.println(""); // 读完一行后换行
            }
        insertExcel22(map,0,0);
    }

    // 写入，往指定sheet表的单元格
    public static void insertExcel22(Map map,int idx,int idx2) throws Exception {

            XSSFWorkbook workbook2 = new XSSFWorkbook(new FileInputStream(new File("E:/数据库比对.xls"))); // 读取的文件
            XSSFSheet sheet2 = null;
            sheet2 = workbook2.getSheetAt(idx2);
            int index=0;
            for(Object key:map.keySet()){
                index++;
                XSSFRow row = sheet2.getRow(index); // 获取指定的行对象，无数据则为空，需要创建
                if (row == null) {
                    row = sheet2.createRow(index); // 该行无数据，创建行对象
                }
                XSSFCell cell = row.createCell(idx);
                String val=map.get(key).toString();
                cell.setCellValue(key.toString());
            }
        FileOutputStream fo = new FileOutputStream("E:/数据库比对.xls"); // 输出到文件
        workbook2.write(fo);
        fo.close();
    }
    // 写入，往指定sheet表的单元格
    public static void insertExcel21(List<String> list,int idx,int sheet) throws Exception {
        XSSFWorkbook workbook2 = new XSSFWorkbook(new FileInputStream(new File("E:/库表字段比对.xls"))); // 读取的文件
        XSSFSheet sheet2 = null;
        sheet2 = workbook2.getSheetAt(sheet);
        int index=0;
        for(int i=0;i<list.size();i++){
            index++;
            XSSFRow row = sheet2.getRow(index); // 获取指定的行对象，无数据则为空，需要创建
            if (row == null) {
                row = sheet2.createRow(index); // 该行无数据，创建行对象
            }
            XSSFCell cell = row.createCell(idx);
            String val= list.get(i);
            cell.setCellValue(val);

        }
        FileOutputStream fo = new FileOutputStream("E:/库表字段比对.xls"); // 输出到文件
        workbook2.write(fo);
        fo.close();

    }

    public static void insertExcel2(Map<String,Map<String,String>> map) throws Exception {
        XSSFWorkbook workbook2 = new XSSFWorkbook(new FileInputStream(new File("E:/库表字段比对.xls"))); // 读取的文件
        XSSFSheet sheet2 = null;
        sheet2 = workbook2.getSheetAt(0);
        int index = 0;
        for (String str : map.keySet()) {
            for (Object str2:map.get(str).keySet()) {
                index++;
                XSSFRow row = sheet2.getRow(index); // 获取指定的行对象，无数据则为空，需要创建
                if (row == null) {
                    row = sheet2.createRow(index); // 该行无数据，创建行对象
                }
                XSSFCell cell = row.createCell(0);
                XSSFCell cell2 = row.createCell(1);
                XSSFCell cell3 = row.createCell(2  );
                String val=map.get(str).get(str2).toString();
                cell.setCellValue(str);
                cell2.setCellValue(str2.toString());
                cell3.setCellValue(val);
            }
        }
        FileOutputStream fo = new FileOutputStream("E:/库表字段比对.xls"); // 输出到文件
        workbook2.write(fo);
        fo.close();
    }

    public static void insertExcel1(Map<String,Map<String,Map<String,String>>> map) throws Exception {
        XSSFWorkbook workbook2 = new XSSFWorkbook(new FileInputStream(new File("E:/a.xls"))); // 读取的文件
        XSSFSheet sheet2 = null;
        sheet2 = workbook2.getSheetAt(0);
        int index = 0;
        for (String str : map.keySet()) {
            for (Object str2:map.get(str).keySet()) {
                for(String type:map.get(str).get(str2).keySet()) {
                    index++;
                    XSSFRow row = sheet2.getRow(index); // 获取指定的行对象，无数据则为空，需要创建
                    if (row == null) {
                        row = sheet2.createRow(index); // 该行无数据，创建行对象
                    }
                    XSSFCell cell = row.createCell(0);
                    XSSFCell cell2 = row.createCell(1);
                    XSSFCell cell3 = row.createCell(2);
                    XSSFCell cell4 = row.createCell(3);
                    String type1=map.get(str).get(str2).get(type);
                    cell.setCellValue(str);
                    cell2.setCellValue(str2.toString());
                    cell3.setCellValue(type);
                    cell4.setCellValue(type1);
                }
            }
        }
        FileOutputStream fo = new FileOutputStream("E:/a.xls"); // 输出到文件
        workbook2.write(fo);
        fo.close();
    }
}

