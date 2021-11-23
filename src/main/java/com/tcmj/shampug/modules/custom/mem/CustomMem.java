package com.tcmj.shampug.modules.custom.mem;

import com.tcmj.shampug.modules.custom.AbstractRecord;
import com.tcmj.shampug.pub.RandomUnit;
import com.tcmj.shampug.pub.Randoms;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * This is a template used for really custom record sets which stays temporarily in memory instead of csv or json (etc.).
 * It will be used if you can/want define all of your data structure right from scratch at current coding point.
 * Another strategy will be set via json or csv or something else.
 */
public class CustomMem extends AbstractRecord<CustomMem> {

    public CustomMem(String pugs, RandomUnit randomUnit) {
        super(pugs, randomUnit);
    }

    public CustomMem(String pugs) {
        super(pugs, new Randoms());
    }

    public <V extends Comparable<? super V>> CustomMem add(String field, V value) {
        super.set(field, value);
        return this;
    }

    @Override
    public int compareTo(CustomMem o) {
        final String a = this.fields.values().stream().map(String::valueOf).collect(Collectors.joining());
        final String b = o.fields.values().stream().map(String::valueOf).collect(Collectors.joining());
        return a.compareTo(b);
    }

    @Override
    public Set<String> getTokens() {
        return this.fields.keySet();
    }


}