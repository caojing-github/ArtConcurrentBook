package chapter05;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 10-16
 */
public class Cache {

    private static final Map<String, Object> MAP = new HashMap<String, Object>();
    private static final ReentrantReadWriteLock RWL = new ReentrantReadWriteLock();
    private static final Lock R = RWL.readLock();
    private static final Lock W = RWL.writeLock();

    public static final Object get(String key) {
        R.lock();
        try {
            return MAP.get(key);
        } finally {
            R.unlock();
        }
    }

    public static final Object put(String key, Object value) {
        W.lock();
        try {
            return MAP.put(key, value);
        } finally {
            W.unlock();
        }
    }

    public static final void clear() {
        W.lock();
        try {
            MAP.clear();
        } finally {
            W.unlock();
        }
    }
}
