package com.hqk.jvm;

/**
 * @author hqk
 * 测试各类垃圾回收器
 *
 * VM参数设置
 * -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:+PrintGCDateStamps {定义垃圾收集器}
 * 可以结合
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
