package com.hqk.jvm;

/**
 * @author hqk
 *
 * 初始化机制
 * 1. new 关键字
 * 2。java.lang.reflect 包的方法进行反射调用
 * 3。当初始化一个类的时候,如果发现其父类还没有进行初始化,则需要先触发其父类的初始化(接口初始化例外,不要求所有父接口 全部都初始化,只有在真正调用到父接口的时候才会初始化）
 * 4. 当启动虚拟机时,用户需要指定一个需要执行的主类,虚拟机先初始化这个主类
 * 5. 当使用java7的动态语言支持时,如果一个MethodHandle实例在解析时,该方法对应的类没有进行初始化,则需要先触发其初始 化.
 *
 * VM参数 -XX:+TraceClassLoading查看加载了哪些信息
 */
public class NotInitialization {
    public static void main(String[] args) {
        //被动引用
        //通过子类引用父类的静态字段,不会导致子类初始化
        //System.out.println(SubClass.value);
        //通过数组定义来引用类,不会触发此类的初始化,有加载但是没有初始化
        SuperClass[] sc = new SuperClass[10];
        //常量在编译阶段会存入调用类的常量池中,本质上并没有直接引用到定义常量的类,因此不会触发定义常量的类的初始化.
        //System.out.println(SubClass.data);
    }
}

class SuperClass{
    static {
        System.out.println("SuperClass init");
    }

    public static int value = 123;
    public static final int value2 = 456;
    // 因为用了final才会在加载的时候就放到常量池，如果不用final，那么第一次加载还是在堆里面
    public static final String data = "java";
}

class SubClass extends SuperClass{
    static {
        System.out.println("SbClass init");
    }
}
