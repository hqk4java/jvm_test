package com.hqk.jvm;

/**
 * @author hqk
 * 测试各类垃圾回收器
 *
 * VM参数设置
 * 堆的最小空间 / 最大空间 / 新生代空间 , 打印GC日志 ,打印GC执行时间
 * -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:+PrintGCDateStamps
 * 可以结合jvisualvm.exe 工具查看JVM的内存消耗情况
 *
 * 如果已经知道当前应用程序的内存最小/最大值,那么两个参数设置成一样的好处
 * 就是为了避免在生产环境由于heap内存扩大或缩小导致应用停顿，降低延迟，同时避免每次垃圾回收完成后JVM重新分配内存。
 *
 * Serial GC
 *  -XX:+UseSerialGC 标记算法 单线程 stop-the-world 进行垃圾回收
 *  年轻代将使用标记-复制(mark-copy)算法，老年代使用标记-清理- 压缩(mark-sweep-compact)算法
 *
 * Parallel GC
 *  -XX:+UseParallelGC
 *  年轻代将使用标记-复制(mark-copy)算法，老年代使用标记- 清理-压缩(mark-sweep-compact)算法，
 *  并且均以多线程，stop-the-world 的方式运行垃圾收集过程，还可通过 -XX:ParallelGCThreads=N 来指定运行垃圾收集过程的线程数，默认为 CPU 核数。
 *
 * CMS
 *  -XX: +UseConcMarkSweepGC
 *  年轻代将采用并行，stop-the-world，标记-复制的收集算法，老年代则采用并发-标记-清理(Concurrent Mark-Sweep)的收集算法，该算法有利于降低应用暂定时间
 *
 * G1
 *  -XX:+UseG1GC
 *  每个区域都可以被称为 Eden 区，Survivor 区，或 Old 区，逻辑上 Eden 区+Survivor 区为 年轻代，Old 区则为老年代，从而避免了每次 GC 时，回收整个堆空间.
 *
 *逃逸分析
 *  -XX:+PrintEscapeAnalysis
 *
 */
public class GCLogDetailTest {
    // 为了触发GC 初始化 1M
    private static int _1MB = 1024 * 1024;

    public static void main(String[] args) {
        byte[] allocation1, allocation2, allocation3, allocation4;
        // Eden 申请 2M,现在 Eden 共 4M,第一次的 2M 变成垃圾
        allocation1 = new byte[2 * _1MB];
        // Eden 申请 2M,现在 Eden 共 6M.
        allocation2 = new byte[2 * _1MB];
        // 新生代最大 10M, Eden 只有 8M,此时申请空间失败,出发一次 Minor GC.
        allocation3 = new byte[2 * _1MB];

        /**
         * 触发 GC 时,会将 Eden 中存活对象复制到 Survivor 空间.
         * Survivor 只有 1M,空间不足,直接将 Eden 复制到老年代.并清空 Eden.
         * 清空 Eden 中,申请这次的 2M 空间.此时 Eden 共 2M.
         * Eden 申请 4M,现在 Eden 共 6M.
         */
        allocation4 = new byte[4 * _1MB];
        // allocation4 的空间变成垃圾
        allocation4 = null;
        // 触发 Full GC,回收 4M 内存,并且 Eden 中存活的 2M 会复制到老年代,清空 Eden
        System.gc();
    }
}
