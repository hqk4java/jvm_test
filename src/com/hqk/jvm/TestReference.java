package com.hqk.jvm;

import java.lang.ref.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author hqk
 * 测试引用
 * 20201225
 */
public class TestReference {

    public static void main(String[] args) {
//        strongReferenceMethod();
//        softReferenceMethod();
//        weakReferenceMethod();
        referenceQueueMethod();
    }

    /**
     * 强引用 就算OOM，都不会被回收
     */
    private static void strongReferenceMethod() {
        TestObj obj = new TestObj();
        System.out.println(obj);
        obj = null;
        System.gc();
    }

    /**
     * 软引用
     * GC 回收后发现内存还是不够直接干掉软引用对象  二次回收
     *
     *  -Xmx20M 设置JVM最大内存为20M
     */
    private static void softReferenceMethod() {
        SoftReference<byte[]> softReference = new SoftReference<byte[]>(new byte[1024*1024*10]);
        System.out.println("回收之前\t"+softReference.get());
        System.gc();
        // 只要内存还没满，GC也不会回收
        System.out.println("回收之后\t"+softReference.get());
        // 创建一个对象，触发GC 回收
        byte[] bytes = new byte[1024 * 1024 * 10];
        // 此时 调用对象为NUlL
        System.out.println(softReference.get());
    }

    /**
     * 弱引用
     *弱引用的特点是不管内存是否足够，只要发生GC，都会被回收：
     */
    private static void weakReferenceMethod(){
        WeakReference<byte[]> weakReference = new WeakReference<byte[]>(new byte[1]);
        System.out.println(weakReference.get());
        System.gc();
        System.out.println(weakReference.get());
    }

    /**
     * 虚引用  == > 幻影引用
     * NIO(直接内存，不在JVM堆内存的就是直接内存，也就是除JVM外的操作系统内存)常用 netty框架就是用这个
     * 回收是通过开启一个线程将垃圾对象放到专门的Queue 队列回收
     * 这块掌握不是很好，马士兵JVM有讲到
     */
    private static void referenceQueueMethod(){
        ReferenceQueue queue = new ReferenceQueue();
        PhantomReference<byte[]> reference = new PhantomReference<byte[]>(new byte[1], queue);
        // 是无法找到这个对象的引用地址
        System.out.println(reference.get());


        List<byte[]> bytes = new ArrayList<>();
       PhantomReference<TestObj> reference2 = new PhantomReference<TestObj>(new TestObj(),queue);
        new Thread(() -> {
            for (int i = 0; i < 100;i++ ) {
                bytes.add(new byte[1024 * 1024]);
            }
        }).start();

        //开启一个线程
        new Thread(() -> {
            while (true) {
                // 轮询队列查询对象是否可用 没有可以引用，那么就返回 ，有则为null  (源码的大意)
                Reference poll = queue.poll();
                if (poll != null) {
                    System.out.println("虚引用被回收了：" + poll);
                }
            }
        }).start();
        // 阻塞方法结束 因为上面开了多线程
        Scanner scanner = new Scanner(System.in);
        scanner.hasNext();
    }
}

class TestObj{
    @Override
    protected void finalize() throws Throwable {
        System.out.println("obj 被回收了");
    }
}

