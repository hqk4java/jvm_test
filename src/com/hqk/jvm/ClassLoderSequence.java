package com.hqk.jvm;

import org.junit.Test;

/**
 * 类加载机制
 * @author hqk
 *
 *  结果不同的原因是
 *  对于情况一: 连接阶段,为静态变量赋初始值.count1=0, count2=0, singltTon=null.
 *  初始化阶段,从上到下依次执行赋值操作和静态代码块. count1=0, count2=0,创建对象之后,对两个数值进行递增.结果 count1=1,count2=1
 *
 *  对于情况二: 连接阶段,为静态变量赋初始值.singltTon=null.count1=0, count2=0,.
 *  初始化阶段,从上到下依次执行赋值操作和静态代码块. 先创建对象,对两个数值进行递增.结果 count1=1,count2=1. 再是赋值,count1 没有赋值,count2 重新赋值.count1=1, count2=0.
 */
public class ClassLoderSequence {
    public static int count1;
    public static int count2 = 0;

    @Test
    public void testLoad(){
        SingleTon.getSingleTon();
        System.out.println("测试1\t"+SingleTon.count1);
        System.out.println("测试1\t"+SingleTon.count2);

        SingleTon2.getSingleTon();
        System.out.println("测试2\t"+SingleTon2.count1);
        System.out.println("测试2\t"+SingleTon2.count2);
    }
}

/**
 * 单例
 */
class SingleTon{
    public static int count1;
    public static int count2 = 0;

    private static SingleTon singleTon;

    private SingleTon(){
        count1++;
        count2++;
    }

    public static SingleTon getSingleTon(){
        if(singleTon == null){
            singleTon = new SingleTon();
        }
        return singleTon;
    }
}

/**
 * 单例
 */
class SingleTon2{
    private static SingleTon2 singleTon2 = new SingleTon2();
    public static int count1 ;
    public static int count2 = 0;

    private SingleTon2() {
        System.out.println("SingleTon2初始化之前count1\t"+count1);
        System.out.println("SingleTon2初始化之前count1\t"+count2);
//        count1++;
//        count2++;
        count1 = count1+1;
        count2 = count2+1;
        System.out.println("SingleTon2初始化之后count1\t"+count1);
        System.out.println("SingleTon2初始化之后count2\t"+count2);
    }

    public static SingleTon2 getSingleTon(){
        if(singleTon2 == null){
            singleTon2 = new SingleTon2();
        }
        return singleTon2;
    }
}
