package com.es.phoneshop.model.product;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
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

    public <T> void safeWrite(Consumer<T> function, T object) {
        lock.writeLock().lock();
        try {
            function.accept(object);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void safeWrite(Volunteer function) {
        lock.writeLock().lock();
        try {
            function.perform();
        } finally {
            lock.writeLock().unlock();
        }
    }
}
