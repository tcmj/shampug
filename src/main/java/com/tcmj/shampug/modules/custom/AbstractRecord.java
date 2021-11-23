package com.tcmj.shampug.modules.custom;

import com.tcmj.shampug.pub.RandomUnit;
import com.tcmj.shampug.pub.Record;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Base class used for every implementation eg. memory, csv, json and so on
 * @param <T>
 */
public abstract class AbstractRecord<T extends Comparable<T>> implements Record<T>, Comparable<T> {
    protected final Map<String, Comparable<?>> fields = new HashMap<>();
    final String pugs;
    final transient RandomUnit randomUnit;

    public AbstractRecord(String pugs, RandomUnit randomUnit) {
        this.pugs = pugs;
        this.randomUnit = randomUnit;
    }

    @Override
    public String key() {
        return this.pugs;
    }

    @Override
    public <V extends Comparable> V get(String name) {
        return (V) fields.get(name);
    }

    @Override
    public <V extends Comparable<? super V>> void set(String field, V value) {
        fields.put(field, value);
    }


    @Override
    public int compareTo(T o) {
        final String a = this.fields.values().stream().map(String::valueOf).collect(Collectors.joining());
        final String b = (String) ((AbstractRecord) o).fields.values().stream().map(String::valueOf).collect(Collectors.joining());
        return a.compareTo(b);
    }

    @Override
    public String toString() {
        return this.pugs + "@" + Integer.toHexString(hashCode()) + fields;
    }

    @Override
    public RandomUnit getRandomUnit() {
        return this.randomUnit;
    }

}