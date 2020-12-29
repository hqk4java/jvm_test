package com.hqk.jvm;

import org.junit.Test;

/**
 * @author hqk
 *
 * 长期存活的对象将进入老年代
 * JVM参数
 * -verbose:gc 输出GC信息
 * -XX:SurvivorRatio=8 Eden和一个Survior比例 8:1
 * -Xmn10M 新生代内存空间
 *
 * -XX:MaxTenuringThreshold 设置新生代存活次数
 * -XX:TargetSurvivorRatio 一个计算期望s区存活大小(Desired survivor size)的参数。默认值为50，即50%。
 * -XX:+UseSerialGC 使用SerialGC回收
 *
 * -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=1
 *
 * 最后的结果看出 新生代from区域的空间利用率为0，说明GC一次直接进入老年代了
 */
public class MaxTenuringThresholdTest {
    private static final int _1MB = 1024 * 1024;

    @Test
    public void test(){
        byte[] allocation1,allocation2,allocation3;

        allocation1 = new byte[_1MB / 100];
        System.out.println("1");
        allocation2 = new byte[_1MB  * 4];
        //申请Eden不足，先youngGC,再Full GC
        System.out.println("2");
        allocation3 = new byte[_1MB * 5];
        allocation3 = null ;
        //申请Eden不足，直接GC
        allocation3 = new byte[4 * _1MB];
        System.out.println("3");
    }
}
