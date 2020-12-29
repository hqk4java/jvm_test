package com.hqk.jvm;

import org.junit.Test;

import java.util.Scanner;

/**
 * @author hqk

 * * 大对象直接接入老年代
 * 从JVM内存管理看，新生代(Eden,s0,s1)是没有老年代空间大的
 *
 * JVM参数
 * -verbose:gc 输出GC信息
 * -XX:SurvivorRatio=8 Eden和一个Survior比例 8:1
 * -Xmn10M 新生代内存空间
 * -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
 */
public class BigObjectAnalyse {

    private static final int _1KB = 1024;

    @Test
    public void testAllocation()throws Exception{
        byte[] allocation1,allocation2;

        allocation1 = new byte[150 * _1KB] ;
        /*
         *理论上
         *  5M已经不够吃，直接Minor GC/Young GC, 通过GC日志看是否发生Minor GC/Young GC事件
         *
         *实际中
         *  发现 allocation2直接在老年代分配空间了并没有触发
         *  说明大对象直接接入老年代
         *
         *到底多大的对象才能直接进入老年代?
         *  虚拟机可以设置超过多少空间才是大对象
         *  -XX:PretenureSizeThreshold   (只在Serial收集器中有效，Parallel默认)
         *  如果Eden空间不够，对象超过Eden一半则直接进入老年代
         */
        allocation2 = new byte[5 * _1KB * _1KB];
    }
}
