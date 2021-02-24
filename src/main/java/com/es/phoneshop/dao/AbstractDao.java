package com.es.phoneshop.dao;

import com.es.phoneshop.model.threadsafe.ExtendedReadWriteLock;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class AbstractDao<T extends IdentifiableItem> {

    protected List<T> items;
    protected long lastId;
    protected ExtendedReadWriteLock lock = new ExtendedReadWriteLock();

    protected AbstractDao() {
        items = new ArrayList<>();
        lastId = 0L;
    }

    public T get(Long id) {
        return lock.safeRead(() -> items.stream()
                .filter(item -> item.getId().equals(id))
                .findAny()
                .orElseThrow(NoSuchElementException::new)
        );
    }

    public void save(T parameter) {
        lock.safeWrite((T item) -> {
                    if (item.getId() != null) {
                        T sameIdOrder = get(item.getId());
                        items.remove(sameIdOrder);
                    } else {
                        item.setId(lastId++);
                    }
                    items.add(item);
                },
                parameter
        );
    }

    public void clear() {
        lock.safeWrite(() -> {
            items.clear();
            lastId = 0;
        });
    }
}

