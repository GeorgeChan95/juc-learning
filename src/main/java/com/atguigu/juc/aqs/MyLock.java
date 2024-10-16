package com.atguigu.juc.aqs;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @ClassName MyLock
 * @Description TODO
 * @Author George
 * @Date 2024/10/16 16:52
 */
public class MyLock {

    private static class Sync extends AbstractQueuedSynchronizer {
        @Override
        protected boolean tryAcquire(int arg) {
            return compareAndSetState(0, 1);
        }

        @Override
        protected boolean tryRelease(int arg) {
            return compareAndSetState(1, 0);
        }

        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }
    }

    private Sync sync = new Sync();

    /**
     * 加锁
     */
    public void lock() {
        sync.acquire(1);
    }

    /**
     * 解锁
     */
    public void unlock() {
        sync.release(1);
    }
}
