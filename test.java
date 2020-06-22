
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
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File("E:/aaa.xls")));
        XSSFSheet sheet = null;
        String e="[；'，；：。，、]";
        Map<String,String> map1=new HashMap<String, String>();
        String aaa="合伙企业";
        // 获取每个Sheet表
            sheet = workbook.getSheetAt(0);
            for (int j = 1; j < 61510; j++) {// getLastRowNum，获取最后一行的行标
                XSSFRow row = sheet.getRow(j);
                if (row != null) {
                  // getLastCellNum，是获取最后一个不为空的列是第几个
                        String a=row.getCell(0).toString();
                        String b=row.getCell(1).toString();
                        String c=row.getCell(2).toString();
                        String[] d=row.getCell(3).toString().split(e);
                        if(a.equals(aaa)) {

                            //不存在行业类型
                            if (!map1.containsKey(b)) {
                                map1.put(b, c);
                            }
                        }
                    }
                }
            List<entitys> lists=new ArrayList<entitys>();
        for (int j = 1; j < 61510; j++) {// getLastRowNum，获取最后一行的行标
            XSSFRow row = sheet.getRow(j);
            if (row != null) {
                // getLastCellNum，是获取最后一个不为空的列是第几个
                String a=row.getCell(0).toString();
                String b=row.getCell(1).toString();
                String c=row.getCell(2).toString();
                String[] d=row.getCell(3).toString().split(e);
                lists.add(new entitys(a,b,c,d));
                /*for(String str:map1.keySet()){
                    if(str.equals(a)&&map1.get(str).equals(c)){
                        for (int i = 0; i < d.length; i++) {
                            list.add(d[i]);
                        }
                    }
                }
                for(int i=0;i<list.size();i++){
                    Map<String,Integer> ma=map2.get(c);
                    if(ma.get(c)==null) {
                        for (String st : ma.keySet()) {

                        }
                    }else{

                    }
                }
                if (map2.containsKey(c)) {
                    Map<String,Integer> map=map2.get(c);
                    for(Object key:map.keySet()) {
                        if(key.equals(d[i])) {
                            x.put(key.toString(), map.get(key.toString()) + 1);
                        }else{
                            x.put(key.toString(), 1);
                        }
                    }
                } else {
                    x.put(d[i], 1);
                }
                map2.put(c, x);*/
            }
        }
        Map<String,Map<String,Integer>> map2=new HashMap<String, Map<String, Integer>>();
        for(int i=0;i<lists.size();i++) {
            entitys ent = lists.get(i);
            if (ent.getA().equals(aaa)) {
                if (map2.containsKey(ent.getC())) {
                    String[] strings = ent.getD();
                    Map<String, Integer> in = map2.get(ent.getC());
                    for (int j = 0; j < strings.length; j++) {

                        if (in.containsKey(strings[j])) {
                            in.put(strings[j], in.get(strings[j]) + 1);
                        } else {
                            in.put(strings[j], 1);
                        }

                    }
                } else {
                    String[] strings = ent.getD();
                    Map<String, Integer> in = new HashMap<String, Integer>();
                    for (int j = 0; j < strings.length; j++) {
                        if (in.containsKey(strings[j])) {
                            in.put(strings[j], in.get(strings[j]) + 1);
                        } else {
                            in.put(strings[j], 1);
                        }

                    }
                    map2.put(ent.getC(), in);

                }
            }
        }
            Map<String,Map<String, Integer>> busiArea=new HashMap<String, Map<String, Integer>>();
            for(Object key:map1.keySet()){
                for(Object key2:map2.keySet()){
                    System.out.println(map1.get(key.toString()));
                    System.out.println(key2.toString());
                    if(map1.get(key.toString()).toString().equals(key2.toString())){
                        busiArea.put(key.toString(),map2.get(key2));
                    }
                }
            }
        insertExcel12(busiArea);
                System.out.println(""); // 读完一行后换行

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
        XSSFWorkbook workbook2 = new XSSFWorkbook(new FileInputStream(new File("E:/aaa.xls"))); // 读取的文件
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
    public static void insertExcel12(Map<String,Map<String,Integer>> map) throws Exception {
        XSSFWorkbook workbook2 = new XSSFWorkbook(new FileInputStream(new File("E:/行业类型对应高频经营范围.xls"))); // 读取的文件
        XSSFSheet sheet2 = null;
        sheet2 = workbook2.getSheetAt(6);
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
        FileOutputStream fo = new FileOutputStream("E:/行业类型对应高频经营范围.xls"); // 输出到文件
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

