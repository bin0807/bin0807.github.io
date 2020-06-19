import cfca.com.itextpdf.text.log.Logger;
import cfca.com.itextpdf.text.log.LoggerFactory;
import org.apache.commons.lang.StringUtils;

import java.sql.*;
import java.util.*;

public class DatabaseUtil {
    private final static Logger logger = LoggerFactory.getLogger(DatabaseUtil.class);

    private static final String DRIVER = "oracle.jdbc.OracleDriver";
    private static final String URL1 = "jdbc:oracle:thin:@192.168.3.202:1522/gxycdzh";
    private static final String URL2 = "jdbc:oracle:thin:@192.168.3.195:1521/icpsp";
    private static final String USERNAME1 = "ahwy4";
    private static final String PASSWORD1 = "oracle";
    private static final String USERNAME2 = "hainancs2";
    private static final String PASSWORD2 = "oracle";
    private static final String TABLE_INFO = "SELECT DISTINCT TABLE_NAME FROM USER_TABLES";
    private static final String TABLE_NAME = "SELECT TABLE_NAME,COLUMN_NAME,DATA_TYPE,DATA_LENGTH,NULLABLE FROM USER_TAB_COLUMNS";
    private static final String SQL = "SELECT * FROM ";
    private static final String SQL2 = "select A.table_name, A.COLUMN_NAME,A.DATA_TYPE  from user_tab_columns A where TABLE_NAME=";
    private static final String SQL3 = "select A.table_name, A.COLUMN_NAME,A.DATA_TYPE,A.DATA_LENGTH  from user_tab_columns A where TABLE_NAME=";
    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            logger.error("can not load jdbc driver", e);
        }
    }
    /**
     * 获取数据库连接
     *
     * @return
     */
    public static Connection getConnection(String url, String userName, String password) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
            logger.error("get connection failure", e);
        }
        return conn;
    }

    /**
     * 关闭数据库连接
     *
     * @param conn
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error("close connection failure", e);
            }
        }
    }
    public static void main(String [] args) throws Exception {
        Connection connection1=getConnection(URL1, USERNAME1, PASSWORD1);
        Connection connection2=getConnection(URL2, USERNAME2, PASSWORD2);
        try {
            Map<String, String> ty = getTableNames(connection1);
            Map<String, String> hainan = getTableNames(connection2);
            List<String> change1 = new ArrayList<String>();
            List<String> change2 = new ArrayList<String>();
            //表明相同的集合
            Map<String, String> normal = new HashMap<String, String>();
            //表明相同的不相同字段名集合
            Map<String, Map<String, String>> column = new HashMap<String, Map<String, String>>();

            //表明相同的相同字段名集合
            Map<String, Map<String, String>> column2 = new HashMap<String, Map<String, String>>();
            for (String tableName : ty.keySet()) {
                if (StringUtils.isEmpty(hainan.get(tableName))) {
                    change1.add(tableName);
                } else {
                    normal.put(tableName, tableName);
                }
            }
            for (String tableName : hainan.keySet()) {
                if (StringUtils.isEmpty(ty.get(tableName))) {
                    change2.add(tableName);
                } else if (StringUtils.isEmpty(normal.get(tableName))) {
                    normal.put(tableName, tableName);
                }
            }
            for (String tablename : normal.keySet()) {
                Map<String, String> columns1 = getColumnNames(tablename, connection1);
                Map<String, String> columns2 = getColumnNames(tablename, connection2);
                Map<String, String> map = new HashMap<String, String>();
                Map<String, String> map2 = new HashMap<String, String>();
                for (String str : columns1.keySet()) {
                    if (StringUtils.isEmpty(columns2.get(str))) {
                        map.put(str, "通用");
                    } else {
                        map2.put(str, columns1.get(str));
                    }
                }
                for (String str : columns2.keySet()) {
                    if (StringUtils.isEmpty(columns1.get(str))) {
                        map.put(str, "海南");
                    } else {
                        if (StringUtils.isEmpty(map2.get(str))) {
                            map2.put(str, columns2.get(str));
                        }
                    }
                }
                if (map.size() > 0) {
                    column.put(tablename, map);
                }
                if (map2.size() > 0) {
                    column2.put(tablename, map2);
                }
            }
            Map<String, Map<String, Map<String, String>>> map = new HashMap<String, Map<String, Map<String, String>>>();
            for (String tableName : column2.keySet()) {
                Map<String, Map<String, String>> columnname1 = new HashMap<String, Map<String, String>>();
                System.out.println(tableName);
                for (String columnname : column2.get(tableName).keySet()) {
                    Map<String, String> columntype = new HashMap<String, String>();
                    String columnType = getColumnType(tableName, columnname, connection1);
                    String columnType2 = getColumnType(tableName, columnname, connection2);
                    if (!StringUtils.equals(columnType, columnType2)) {
                        columntype.put(columnType, columnType2);
                        columnname1.put(columnname, columntype);
                    }
                }

                if (columnname1.size() > 0) {
                    map.put(tableName, columnname1);
                }
            }
            test.insertExcel1(map);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeConnection(connection1);
            closeConnection(connection2);
        }
        System.out.println("输出完成！！！！！！！！！！！！！！！！！！！！！！！");

    }
    /**
     * 获取数据库下的所有表名
     */
    public static Map<String, String> getTableInfos(Connection a) {
        Map<String, String> tableNames = new HashMap<String, String>();
        Connection conn =a;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(TABLE_NAME);
            rs = pst.executeQuery();
            while (rs.next()) {
                tableNames.put(rs.getString(1) , rs.getString(1) + "=" + rs.getString(2) + "=" + rs.getString(3) + "=" + rs.getString(4) + "=" + rs.getString(5) + "=" + rs.getString(1));
            }
        } catch (SQLException e) {
            logger.error("getTableNames failure", e);
        } finally {
            try {
                rs.close();
                closeConnection(conn);
            } catch (SQLException e) {
                logger.error("close ResultSet failure", e);
            }
        }
        return tableNames;
    }
    public static Map<String, String> getTableNames(Connection a) {
        Map<String, String> tableNames = new HashMap<String, String>();
        Connection conn =a;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(TABLE_INFO);
            rs = pst.executeQuery();
            while (rs.next()) {
                tableNames.put(rs.getString(1), rs.getString(1));
            }
        } catch (SQLException e) {
            logger.error("getTableNames failure", e);
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error("close ResultSet failure", e);
            }
        }
        return tableNames;
    }

    /**
     * 获取表中所有字段名称
     *
     * @param tableName 表名
     * @return
     */
    public static Map<String,String> getColumnNames(String tableName, Connection a) {
        Map<String,String> columnNames = new HashMap<String,String>();
        // 与数据库的连接
        Connection conn = a;
        PreparedStatement pStemt = null;
        String tableSql = SQL2 +"\'"+tableName+"\'";
        try {
            pStemt = conn.prepareStatement(tableSql);
            // 结果集元数据
            ResultSet rsmd = pStemt.executeQuery();
            while (rsmd.next()) {
                columnNames.put(rsmd.getString(2),rsmd.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("getColumnNames failure", e);
        } finally {
            if (pStemt != null) {
                try {
                    pStemt.close();
                } catch (SQLException e) {
                    logger.error("getColumnNames close pstem and connection failure", e);
                }
            }
        }
        return columnNames;
    }
    /**
     * 获取表中所有字段leixi
     *
     * @param tableName 表名
     * @return
     */
    public static String getColumnType(String tableName,String columnType,Connection a) {
        String columnNames = null;
        // 与数据库的连接
        Connection conn = a;
        PreparedStatement pStemt = null;
        String tableSql = SQL3 +"\'"+tableName+"\'"+"And column_name=" +"\'"+columnType+"\'";
        try {
            pStemt = conn.prepareStatement(tableSql);
            // 结果集元数据
            ResultSet rsmd = pStemt.executeQuery();
            while (rsmd.next()) {
                System.out.println(columnType+"("+rsmd.getString(3)+")");
                columnNames=rsmd.getString(4);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("getColumnNames failure", e);
        } finally {
            if (pStemt != null) {
                try {
                    pStemt.close();
                } catch (SQLException e) {
                    logger.error("getColumnNames close pstem and connection failure", e);
                }
            }
        }
        return columnNames;
    }
    /**
     * 获取表中所有字段类型
     *
     * @param tableName
     * @return
     */
    public static List<String> getColumnTypes(String tableName, String url, String userName, String password) {
        List<String> columnTypes = new ArrayList<String>();
        // 与数据库的连接
        Connection conn = getConnection(url, userName, password);
        PreparedStatement pStemt = null;
        String tableSql = SQL + tableName;
        try {
            pStemt = conn.prepareStatement(tableSql);
            // 结果集元数据
            ResultSetMetaData rsmd = pStemt.getMetaData();
            // 表列数
            int size = rsmd.getColumnCount();
            for (int i = 0; i < size; i++) {
                columnTypes.add(rsmd.getColumnTypeName(i + 1));
            }
            Arrays.sort(columnTypes.toArray());
        } catch (SQLException e) {
            logger.error("getColumnTypes failure", e);
        } finally {
            if (pStemt != null) {
                try {
                    pStemt.close();
                    closeConnection(conn);
                } catch (SQLException e) {
                    logger.error("getColumnTypes close pstem and connection failure", e);
                }
            }
        }
        return columnTypes;
    }
    public static List<List<String>> selectTableInfo(Map<String, String> uat, Map<String, String> sit) {
        Set<Map.Entry<String, String>> uats = uat.entrySet();
        Iterator<Map.Entry<String, String>> it = uats.iterator();
        Map.Entry<String, String> infos = null;
        String uatInfo = "", sitInfo = "";
        List<List<String>> list = new ArrayList<List<String>>();
        String[] uatArray = null, sitArray = null;
        while (it.hasNext()) {
            infos = it.next();
            uatInfo = infos.getValue();
            sitInfo = sit.get(infos.getKey());
            if (!uatInfo.equals(sitInfo)) {
                List<String> isDiffent = new ArrayList<String>();
                uatArray = uatInfo.split("=");
                for (int i = 0; i < uatArray.length; i++) {
                    isDiffent.add(uatArray[i]);
                }
                if (sitInfo != null) {
                    sitArray = sitInfo.split("=");
                    for (int i = 0; i < sitArray.length; i++) {
                        isDiffent.add(sitArray[i]);
                    }
                } else {
                    for (int i = 0; i < uatArray.length; i++) {
                        isDiffent.add("");
                    }
                }
                list.add(isDiffent);
            }
        }
        return list;
    }
    public static List<List<String>>selectTableInfo(Map<String, String> uat, Map<String, String> sit, String prefix) {
        Set<Map.Entry<String, String>> uats = uat.entrySet();
        Iterator<Map.Entry<String, String>> it = uats.iterator();
        Map.Entry<String, String> infos = null;
        String uatInfo = "", sitInfo = "";
        List<List<String>> list = new ArrayList<List<String>>();
        String[] uatArray = null, sitArray = null;
        int len=0;
        while (it.hasNext()) {
            infos = it.next();
            uatInfo = infos.getValue();
            sitInfo = sit.get(infos.getKey());
            if (!uatInfo.equals(sitInfo)) {
                uatArray = uatInfo.split("=");
                if (prefix != null && prefix.contains(uatArray[uatArray.length - 1])) {
                    continue;
                }
                List<String> isDiffent = new ArrayList<String>();
                if(prefix!=null) {
                    len = uatArray.length - 1;
                }else{
                    len=uatArray.length;
                    for (int i = 0; i < len; i++) {
                        isDiffent.add(uatArray[i]);
                    }
                }
                if (sitInfo != null) {
                    sitArray = sitInfo.split("=");
                    for (int i = 0; i < len; i++) {
                        isDiffent.add(sitArray[i]);
                    }
                } else {
                    for (int i = 0; i < len; i++) {
                        isDiffent.add("");
                    }
                }
                list.add(isDiffent);
            }
        }
        return list;
    }

}
