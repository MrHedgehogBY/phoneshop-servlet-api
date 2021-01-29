package com.es.phoneshop.model.product;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

public class ExtendedReadWriteLock {
    private ReadWriteLock lock;

    public ExtendedReadWriteLock() {
        lock = new ReentrantReadWriteLock();
    }

    public <T> T safeRead(Supplier<T> function) {
        lock.readLock().lock();
        try {
            return function.get();
        } finally {
            lock.readLock().unlock();
        }
    }

    public void safeWrite(Runnable task) {
        lock.writeLock().lock();
        try {
            task.run();
        } finally {
            lock.writeLock().unlock();
        }
    }
}
