package com.tcmj.shampug.modules.custom.json;

import com.tcmj.shampug.modules.custom.AbstractRecord;
import com.tcmj.shampug.pub.RandomUnit;
import com.tcmj.shampug.pub.Randoms;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * This is a template used for really custom record sets which stays temporarily in json.
 * It will be used if you can/want define all of your data structure right from scratch at current coding point.
 * Another strategy will be set via json or csv or something else.
 */
public class CustomJson extends AbstractRecord<CustomJson> {


    public CustomJson(String pugs, RandomUnit randomUnit) {
        super(pugs, randomUnit);
    }

    public CustomJson(String pugs) {
        super(pugs, new Randoms());
    }

    @Override
    public int compareTo(CustomJson o) {
        final String a = this.fields.values().stream().map(String::valueOf).collect(Collectors.joining());
        final String b = o.fields.values().stream().map(String::valueOf).collect(Collectors.joining());
        return a.compareTo(b);
    }

    @Override
    public Set<String> getTokens() {
        return null;
    }
}