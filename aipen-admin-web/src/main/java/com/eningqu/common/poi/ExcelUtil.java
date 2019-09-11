package com.eningqu.common.poi;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * @description excel工具类
 * @author H.P.YANG
 * @date 2017/6/15 14:30
 */

public class ExcelUtil {

    private final static Logger logger = LogManager.getLogger(ExcelUtil.class);

    private final static String XLS = ".xls";
    private final static String XLSX = ".xlsx";

    /**
     * @param headList
     *            Excel文件Head标题集合
     * @param fieldList
     *            Excel文件Field标题集合
     * @param dataList
     *            Excel文件数据内容部分
     * @throws Exception
     */
    public static Workbook createExcel(List<String> headList,
                                       List<String> fieldList, List<Map<String, Object>> dataList) throws Exception {
        // 创建新的Excel 工作簿
        Workbook workbook = new HSSFWorkbook();

        // 在Excel工作簿中建一工作表，其名为缺省值
        // 如要新建一名为"效益指标"的工作表，其语句为：
        // HSSFSheet sheet = workbook.createSheet("效益指标");
        Sheet sheet = workbook.createSheet();

        // 定义单元格样式
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        Font font = workbook.createFont();
        //设置粗体
        font.setBold(true);

        cellStyle.setFont(font);
        // 设置自动换行
        cellStyle.setWrapText(true);

        // 在索引0的位置创建行（最顶端的行）
        Row row = sheet.createRow(0);
        row.setHeightInPoints((short)30);

        // ===============================================================
        for (int i = 0, length = headList.size(); i < length; i++) {
            // 在索引0的位置创建单元格（左上端）
            Cell cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            // 在单元格中输入一些内容
            cell.setCellValue(headList.get(i));
            // 设置列宽, 单位字符(in units of 1/256th of a character width)
            sheet.setColumnWidth(i + 1, 20 * 256);
            //设置宽度自适应
            //sheet.autoSizeColumn(0);
            //取消宽度自适应
            //sheet.autoSizeColumn(0, false);

        }
        // ===============================================================
        // 定义单元格样式
        CellStyle cellStyleContent = workbook.createCellStyle();
        cellStyleContent.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyleContent.setAlignment(HorizontalAlignment.CENTER);
        cellStyleContent.setWrapText(true);

        for (int n = 0, size = dataList.size(); n < size; n++) {
            // 在索引1的位置创建行（最顶端的行）
            Row row_value = sheet.createRow(n + 1);
            row_value.setHeightInPoints((short)45);
            Map<String, Object> dataMap = dataList.get(n);
            // ===============================================================
            for (int i = 0, length = fieldList.size(); i < length; i++) {
                // 在索引0的位置创建单元格（左上端）
                Cell cell = row_value.createCell(i);
                cell.setCellStyle(cellStyleContent);
                // 在单元格中输入一些内容
                cell.setCellValue(objToString(dataMap.get(fieldList.get(i))));
            }
            // ===============================================================
        }
        System.gc();
        return workbook;
    }


    private synchronized static String objToString(Object obj) {
        if (obj == null) {
            return "";
        } else {
            if (obj instanceof String) {
                return (String) obj;
            } else {
                return obj.toString();
            }
        }
    }

    /**
     * bean转map
     * @param bean
     * @return
     * @throws Exception
     */
    public static final Map<String, Object> toMap(Object bean) throws Exception {
        Map<String, Object> returnMap = Maps.newHashMap();
        BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo
                .getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    returnMap.put(propertyName, result);
                } else {
                    returnMap.put(propertyName, "");
                }
            }
        }
        return returnMap;
    }

    public static List<List<Object>> readExcel(InputStream inputstream, String fileName) throws IOException{
        List<List<Object>> lists = Lists.newArrayList();

        // 创建新的Excel 工作簿
        Workbook workbook = null;
        switch (fileName.substring(fileName.lastIndexOf("."))){
            case XLS:
                workbook = new HSSFWorkbook(inputstream);
                break;
            case XLSX:
                workbook = new XSSFWorkbook(inputstream);
                break;
            default:
                break;
        }

        Sheet sheet = workbook.getSheetAt(0);
        int col = sheet.getRow(0).getPhysicalNumberOfCells();
        for (int i = 0, l = sheet.getPhysicalNumberOfRows(); i < l; i++) {
            Row row = sheet.getRow( i + 1 );
            if ( row != null ) {
                List<Object> data = Lists.newArrayList();
                boolean flag = true;
                for ( int j = 0; j < col; j++ ) {
                    Cell cell = row.getCell(j);
                    /*String value = getCellValue(cell);
                    if(StringUtils.isEmpty(value)){
                        flag = false;
                        logger.error("导入【" + fileName + "】文件时，第 " + (i + 2)
                                + " 行的第 " + (j + 1) + " 列数据格式错误,此条数据导入失败");
                        logService.addImportLog("导入【" + fileName + "】文件时，第 " + (i + 2)
                                + " 行的第 " + (j + 1) + " 列数据格式错误,此条数据导入失败", ImportLogEnum.SALESMAN_TYPE.getCode());
                        break;
                    }else{
                        data.add(value);
                    }*/
                    data.add(getCellValue(cell));

                }
                if(flag){
                    lists.add(data);
                }
            }
        }

        return lists;
    }

    /**
     * 单元格cell类型判断
     * @param cell
     * @return
     */
    private static String getCellValue(Cell cell){
        String cellvalue = "";
        if(cell == null){
            return cellvalue;
        }
        switch (cell.getCellTypeEnum()) {

            //数值类型（包括日期等）
            case NUMERIC:
                if(DateUtil.isCellDateFormatted(cell)) {
                    cellvalue =  cn.hutool.core.date.DateUtil.format(cell.getDateCellValue(), "yyyy-MM-dd");
                }else{
                    cellvalue = new DecimalFormat("#").format(cell.getNumericCellValue());
                }
                break;

            //字符串类型
            case STRING:
                cellvalue = StringUtils.isEmpty(cell.getStringCellValue()) ? "" : cell.getStringCellValue();
                break;

            //表达式类型
            case FORMULA:

                break;

            //空
            case BLANK:

                break;

            //布尔值类型
            case BOOLEAN:
                cellvalue = cell.getBooleanCellValue() ? "true" : "false";
                break;

            //异常
            case ERROR:

                break;
        }

        return cellvalue;
    }


    /**
     * map to bean
     * @param clazz
     * @param map
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws IntrospectionException
     */
    public static <T> T mapToBean(Class<T> clazz, Map<String, ? extends Object> map) throws IllegalAccessException, InstantiationException, InvocationTargetException, IntrospectionException {

        //new 出一个对象
        T obj = clazz.newInstance();
        // 获取person类的BeanInfo对象
        BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
        //获取属性描述器
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            //获取属性名
            String key = propertyDescriptor.getName();
            Object value = map.get(key);
            if(key.equals("class"))
                continue;
            Method writeMethod = propertyDescriptor.getWriteMethod();
            //通过反射来调用Person的定义的setName()方法
            writeMethod.invoke(obj,value);
        }

        return obj;
    }
}
