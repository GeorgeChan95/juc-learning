//package com.atguigu.juc.aqs;
//
//import java.util.concurrent.locks.AbstractQueuedSynchronizer;
//
///**
// * @ClassName MyTest
// * @Description TODO
// * @Author George
// * @Date 2024/10/15 20:39
// */
//public class MyTest {
//final void lock() {
//    // 使用CAS将同步状态从0修改为1，如果成功表示成功抢占了锁
//    if (compareAndSetState(0, 1))
//        // 抢占锁成功，设置当前线程为独占线程
//        setExclusiveOwnerThread(Thread.currentThread());
//    else
//        // 以独占模式争抢锁，成功则返回，失败则线程进入CLH队列
//        acquire(1);
//}
//
//
//
//final boolean nonfairTryAcquire(int acquires) {
//    // 获取当前线程
//    final Thread current = Thread.currentThread();
//    // 获取同步状态
//    int c = getState();
//    if (c == 0) { // 如果同步状态为0，表示资源空闲，没有被其它线程占用
//        // CAS修改同步状态，成功的话则设置当前线程为独占线程，并返回true
//        if (compareAndSetState(0, acquires)) {
//            setExclusiveOwnerThread(current);
//            return true;
//        }
//    }
//    // 如果同步状态不是0，但是当前线程就是资源的独占线程，这就是表示锁重入，此时更新同步状态，并返回true
//    else if (current == getExclusiveOwnerThread()) {
//        int nextc = c + acquires;
//        if (nextc < 0) // overflow
//            throw new Error("Maximum lock count exceeded");
//        setState(nextc);
//        return true;
//    }
//    // 同步状态既不是空闲（0），当前线程也不是资源的独占线程，表示当前线程此次抢占锁失败，返回false。
//    return false;
//}
//
//private Node addWaiter(Node mode) {
//    // 通过当前的线程和锁模式新建一个节点
//    Node node = new Node(Thread.currentThread(), mode);
//    // Pred指针指向尾节点Tail
//    Node pred = tail;
//    if (pred != null) {
//        // 将New中Node的Prev指针指向Pred
//        node.prev = pred;
//        // CAS设置Tail节点为新建的节点
//        if (compareAndSetTail(pred, node)) {
//            // pred节点的next指针指向新的node节点
//            pred.next = node;
//            // 返回新建的node节点
//            return node;
//        }
//    }
//    // node节点加入CLH队列
//    enq(node);
//    // 返回node节点
//    return node;
//}
//
//
//    private Node enq(final Node node) {
//        // 自旋操作
//        for (;;) {
//            Node t = tail;
//            // 如果队列没有被初始化过，则先初始化一个虚拟节点（new Node()）
//            if (t == null) { // Must initialize
//                // 初始化虚节点，并将它设置成头节点
//                if (compareAndSetHead(new Node()))
//                    tail = head;
//            } else {
//                // 队列不为空时，当前线程所在节点node的prev指针指向尾节点
//                node.prev = t;
//                // CAS设置当前节点为尾节点
//                if (compareAndSetTail(t, node)) {
//                    // 之前的尾节点的next指针指向当前节点node
//                    t.next = node;
//                    // 返回之前的尾节点
//                    return t;
//                }
//            }
//        }
//    }
//
//    public final boolean hasQueuedPredecessors() {
//        // 尾节点
//        Node t = tail; // Read fields in reverse initialization order
//        // 头节点
//        Node h = head;
//        // 指向头节点的下一个节点
//        Node s;
//        return h != t &&
//                ((s = h.next) == null || s.thread != Thread.currentThread());
//    }
//
//
//    public final boolean release(int arg) {
//        // 尝试释放锁，成功返回true，失败则返回false
//        if (tryRelease(arg)) {
//            // 队列头节点
//            Node h = head;
//            if (h != null && h.waitStatus != 0)
//                // 更新头节点的waitStatus，并唤醒头节点的后继节点
//                unparkSuccessor(h);
//            return true;
//        }
//        return false;
//    }
//}
