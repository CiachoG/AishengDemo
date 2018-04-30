package modular_dbaccess;

import android.util.Log;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQLDataAccess {
    //static String url="jdbc:oracle:thin:@localhost:1521/orcl0230";
    private static String URL="jdbc:oracle:thin:@";
    private static String IP="192.168.43.70",port="1521";
    private static String User="aisheng_user",Password="15211160230";
    private static String DBName="orcl0230";

    private static String ConnectionStrng;
    static{
        ConnectionStrng=URL+IP+":"+port+"/"+DBName;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            Log.e("JDBC驱动加载异常:",e.toString());
        }
    }

    /*
    Method f=Math.class.getMethod("sqrt",double.class);//方法名，后面为方法参数类型
    double d=9;
    double res=(Double) f.invoke(null,d);
    System.out.println(res);
    */

    //参数：要执行的sql语句集，及唯一的查询语句数组索引
    public static void query(String[] array_sql,int queryIndex,Method method,Object obj)throws Exception{
        if(array_sql==null||array_sql.length==0)    return;
        Connection conn=DriverManager.getConnection(ConnectionStrng,User,Password);

        ResultSet rs=null;
        Statement state=conn.createStatement();
        for(int i=0;i<array_sql.length;++i){
            if(i==queryIndex){  //有查询的结果集返回
                rs=state.executeQuery(array_sql[i]);

                if(method!=null)
                    method.invoke(obj,rs);  //第一个参数为调用此方法的对象，后面是调用参数
            }else{
                state.execute(array_sql[i]);
            }
        }
        if(rs!=null)
            rs.close();

        state.close();
        conn.close();
    }

    public static int update(String sql)throws Exception{
        Connection conn=DriverManager.getConnection(ConnectionStrng,User,Password);
        conn.setAutoCommit(true);
        Statement state=conn.createStatement();
        int result=state.executeUpdate(sql);
        conn.commit();

        state.close();
        conn.close();
        return result;
    }
}
