package com.hqk.jvm;

/**
 * @author hqk
 * 测试Java是否用到 引用算法
 * 循环引用的方式，如果没有被回收就是引用算法，立即回收，说明没有用到引用算法
 * -XX:+PrintGCDetails  打印GC回收日志
 */
public class ReferenceCountingGC {
    public Object instance = null;

    private static final int _1MB = 1024 * 1024;

    // 使用该变量表示在内存中占用空间,以便可以方便观察日志.
    private byte[] bigSize = new byte[2 * _1MB];

    public static void main(String[] args) {
        ReferenceCountingGC objA = new ReferenceCountingGC();
        ReferenceCountingGC objB = new ReferenceCountingGC();
        objA.instance = objB;
        objB.instance = objA;
        // 设置两个变量为 null,那么对象理论上来说就是垃圾.
        objA = null;
        objB = null;

        // 假设这里发生 GC,查看日志,观察两个对象是否被回收.
        System.gc();
    }
}
