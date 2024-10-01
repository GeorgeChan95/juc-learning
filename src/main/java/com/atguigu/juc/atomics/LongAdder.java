//package com.atguigu.juc.atomics;
//
//import java.io.Serializable;
//import java.util.Arrays;
//import java.util.concurrent.ThreadLocalRandom;
//import java.util.concurrent.atomic.Striped64;
//import java.util.function.LongBinaryOperator;
//
///**
// * @author George Chan
// * @date 2024/10/1 06:04
// * <p></p>
// */
//public class LongAdder extends Striped64 implements Serializable {
//
///**
// * Adds the given value.
// *
// * @param x the value to add
// */
//public void add(long x) {
//    Cell[] cs; // cells 的引用
//    long b; // 获取的base值
//    long v; // 期望值
//    int m; // cell数组的长度
//    Cell c; // 当前线程命中的cell单元格
//
//    // 首次首线程 (cs = cells) != null 一定是false，此时走casBase方法，以CAS的方式更新base值，且只有当cas失败时，才会走到if中
//    // 条件1: cells不为室，说明出现过竞争，cell[]己创建
//    // 条件2:cas操作base失败，说明其它线程先一步修改了base正在出现竞争
//    if ((cs = cells) != null || !casBase(b = base, b + x)) {
//        boolean uncontended = true; // true表示当前线程cas更新成功, false表示cas更新失败,线程处于竞争中
//        // 条件1: 表示cells为空,且线程处于竞争状态中,因为经过casBase方法返回false
//        // 条件2: cells数组长度小于0,这个应该不会出现
//        // 条件3: 当前线程所在的cell为空，说明当前线程还没有更新过cell，应初始化一个cell
//        // 条件4: 更新当前线程所在的cell失败，说明现在竞争很激烈，多个线程hash到了同一个cell，应扩容
//        // getProbe()方法返回的是线程中的threadLocalRandomProbe字段,它是通过随机数生成的一个值，对于一个确定的线程这个值是固定的(除非刻意修改它)
//        if (cs == null || (m = cs.length - 1) < 0 ||
//                (c = cs[getProbe() & m]) == null ||
//                !(uncontended = c.cas(v = c.value, v + x)))
//            // 调用striped64中的方法处理
//            longAccumulate(x, null, uncontended);
//    }
//}
//
//
///**
// * long型数据原子方式增长
// * @param x 需要增加的值, 一般默认都是1
// * @param fn 默认传递是null
// * @param wasUncontended 竞争标识,如果是false表示有竞争,只有cells初始化之后,并且当前线程CAS竞争修改失败,才会是false
// */
//final void longAccumulate(long x, LongBinaryOperator fn,
//                          boolean wasUncontended) {
//    // 存储线程的probe值
//    int h;
//    // 如果getProbe()方法返回0，说明随机数未初始化
//    if ((h = getProbe()) == 0) {
//        // 使用ThreadLocalRandom为当前线程重新计算一个hash值,强制初始化
//        ThreadLocalRandom.current(); // force initialization
//        // 重新获取probe值,hash值被重置就好比一个全新的线程一样，所以设置了wasuncontended竞争状态为true。
//        h = getProbe();
//        // 重新计算了当前线程的hash后认为此次不算是一次竞争，都未初始化，肯定还不存在竞争激烈wasuncontended竞争状态为true
//        wasUncontended = true;
//    }
//    boolean collide = false; // 用于标识是否发生碰撞
//    done: for (;;) {
//        Cell[] cs; // cell数组
//        Cell c; // 单个cell
//        int n; // cell数组长度
//        long v; // cell中存储的值
//        // CASE1: cells已经被初始化(cell数组不为空,且长度>0)
//        if ((cs = cells) != null && (n = cs.length) > 0) {
//            if ((c = cs[(n - 1) & h]) == null) { // 当前线程的hash值运算后映射得到的Cell单元为null，说明该Cell没有被使用
//                if (cellsBusy == 0) {       // Cell[]数组没有正在扩容, 尝试创建一个新的cell
//                    Cell r = new Cell(x);   // 创建一个Cell单元,值为x
//                    if (cellsBusy == 0 && casCellsBusy()) { // 尝试加锁,成功后cellsBusy == 1
//                        try {               // Recheck under lock
//                            Cell[] rs; // 新的cell数组
//                            int m; // 新cell数组的长度
//                            int j; // 新的cell在cell数组中的索引下标
//                            // rs = cells 避免了对全局变量的直接引用, 提高安全性和效率,同时rs 和 cells指向同一个数组,rs变化,cells也会同步
//                            if ((rs = cells) != null &&
//                                    (m = rs.length) > 0 &&
//                                    rs[j = (m - 1) & h] == null) {
//                                rs[j] = r; // 将新创建的cell r 放入cell数组的计算位置
//                                break done;
//                            }
//                        } finally {
//                            cellsBusy = 0; // cell创建完成后,重新将cellsBusy置为0,非竞争状态
//                        }
//                        continue;           // Slot is now non-empty
//                    }
//                }
//                collide = false;
//            }
//            else if (!wasUncontended)       // 如果前一次CAS更新Cell单元失败了
//                wasUncontended = true;      // 重新置为true，后面会重新计算线程的hash值
//            else if (c.cas(v = c.value,
//                    (fn == null) ? v + x : fn.applyAsLong(v, x))) // 试CAS更新Cell单元值
//                break;
//            else if (n >= NCPU || cells != cs) // 当Cell数组的大小超过CPU核数后，不再进行扩容
//                collide = false;            // At max size or stale
//            else if (!collide)
//                collide = true;
//            else if (cellsBusy == 0 && casCellsBusy()) { // 尝试加锁进行扩容
//                try {
//                    if (cells == cs) // cells和局部变量cs相同,表示没有其他线程扩容过
//                        cells = Arrays.copyOf(cs, n << 1); // 扩容后的大小==当前容量*2
//                } finally {
//                    cellsBusy = 0;
//                }
//                collide = false;
//                continue;                   // Retry with expanded table
//            }
//            h = advanceProbe(h); // 计算线程新的hash值,重新参与下一轮竞争中
//        }
//        // CASE2: cells没有加锁且没有初始化,则尝试对它进行加锁,并初始化
//        else if (cellsBusy == 0 && cells == cs && casCellsBusy()) {
//            try {                           // Initialize table
//                if (cells == cs) { // 进行两次校验, 由于cells是共有的对象,可能出现多线程并发修改导致对象状态发生变化,两次校验确保数据状态一致
//                    Cell[] rs = new Cell[2]; // 新建一个容量为2的cell数组
//                    rs[h & 1] = new Cell(x); // 找到当前线程hash到数组中的位置,并创建对应的cell
//                    cells = rs;
//                    break done;
//                }
//            } finally {
//                cellsBusy = 0;
//            }
//        }
//        //  // CASE3: cells正在进行初始化,则尝试直接在base上进行累加操作
//        else if (casBase(v = base,
//                (fn == null) ? v + x : fn.applyAsLong(v, x)))
//            break done;
//    }
//}
//
///** 计算当前CPU数量,Cell[] 扩容时会用到 */
//static final int NCPU = Runtime.getRuntime().availableProcessors();
//
///**
// * Table of cells. When non-null, size is a power of 2.
// */
//transient volatile Cell[] cells;
//
///**
// * 类似于AtomicLong中全局的value值。在没有竞争情况下数据直接累加到base上，或者cells扩容时，也需要将数据写入到base上
// */
//transient volatile long base;
//
///**
// * 初始化cells或者扩容cells需要获取锁，0:表示无锁状态 1:表示其他线程已经持有了锁
// */
//transient volatile int cellsBusy;
//
///**
// * 通过CAS操作修改 cellsBusy 的值，CAS成功代表获取锁，返回true
// */
//final boolean casCellsBusy() {
//    return CELLSBUSY.compareAndSet(this, 0, 1);
//}
//
///**
// * 获取当前线程的hash值
// */
//static final int getProbe() {
//    return (int) THREAD_PROBE.get(Thread.currentThread());
//}
//
///**
// * 重置当前线程的hash值
// */
//static final int advanceProbe(int probe) {
//    probe ^= probe << 13;   // xorshift
//    probe ^= probe >>> 17;
//    probe ^= probe << 5;
//    THREAD_PROBE.set(Thread.currentThread(), probe);
//    return probe;
//}
//}
