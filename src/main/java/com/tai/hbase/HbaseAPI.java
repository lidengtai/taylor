package com.tai.hbase;

import org.apache.catalina.LifecycleState;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: Hbase的API
 * @author: Taylor
 * @date :  2020-10-16 14:50
 **/
public class HbaseAPI {

    private static Configuration configuration = null;
    private static Connection connection  = null;

    static {
        try {
            configuration = HBaseConfiguration.create();
            configuration.set("hbase.zookeeper.quorum","hadoop01,hadoop02,hadoop03");
            connection = ConnectionFactory.createConnection(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 表是否存在
     * @param table
     * @return
     * @throws IOException
     */
    public static boolean isExistsTable(String table) throws IOException {

        /**
         * getAdmin ddl操作对象
         *  Admin admin  = connection.getAdmin();
         * getTable dml操作对象
         * Table table = connection.getTable(TableName.valueOf(table))
         */
        Admin admin  = connection.getAdmin();
        boolean b = admin.tableExists(TableName.valueOf(table));
        admin.close();

        return b;
    }

}
