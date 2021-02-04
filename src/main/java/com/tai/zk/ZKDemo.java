package com.tai.zk;

import org.I0Itec.zkclient.ZkClient;

/**
 * @description: zookeeper
 * @author: Taylor
 * @date :  2020-12-08 09:23
 **/
public class ZKDemo {

    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient("",5000);
    }
}
