package com.tcmj.shampug.pub;

import java.util.Set;

public interface Record<T extends Comparable<T>> {

    <V extends Comparable> V get(String name);

    <V extends Comparable<? super V>> void set(String field, V value);

    String key();

    Set<String> getTokens();

    RandomUnit getRandomUnit();


    // <V extends Comparable> void set(String dogsname, V cicolina);
}