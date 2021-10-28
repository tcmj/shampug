package com.tcmj.shampug.intern;


import com.tcmj.shampug.Record;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static com.tcmj.shampug.intern.Registry.Strategy.GLOBAL;

/**
 * Modules must be registered here.
 * We have some of our own implementations like Address and also custom ones.
 */
public class Registry {

    private static final Registry STATIC_INSTANCE = new Registry(GLOBAL);
    private static ThreadLocal<Registry> THREADLOCAL_INSTANCE;

    private final Map<String, Set<Record<? extends Comparable>>> modules = new HashMap<>();

    private Strategy strategy;

    public Registry(Strategy strategy) {
        this.strategy = strategy;
    }

    public static Registry get(Strategy strategy) {
        switch(strategy) {
            case GLOBAL:
                return STATIC_INSTANCE;
            case PER_TREAD:
                THREADLOCAL_INSTANCE = new ThreadLocal();
                THREADLOCAL_INSTANCE.set(new Registry(strategy));
                return THREADLOCAL_INSTANCE.get();
            case NEW_INSTANCE:
                return new Registry(strategy);
            default:
                throw new IllegalStateException("Unknown Registry.Strategy: " + strategy);
        }
    }

    public void put(String address, Record record) {
        Set<Record<? extends Comparable>> records = modules.get(address);
        if (records == null) {
            records = new LinkedHashSet<>();
            modules.put(address, records);
        }
        records.add(record);

    }

    public Set<Record<? extends Comparable>> lookup(String address) {
        Set<Record<? extends Comparable>> records = modules.get(address);
        return records;
    }

    public <T extends Comparable<T>> Set<Record<? extends Comparable>> lookup(Class<T> clazz) {
        Set<Record<? extends Comparable>> records = modules.get(clazz.getName());
        return records;
    }

    public enum Strategy {GLOBAL, PER_TREAD, NEW_INSTANCE}
}