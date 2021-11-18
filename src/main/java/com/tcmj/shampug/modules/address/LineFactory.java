package com.tcmj.shampug.modules.address;

import com.tcmj.shampug.pub.RandomUnit;
import com.tcmj.shampug.pub.Record;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Factory class to create instances of data classes.
 * A nice feature ist to specify default values for fields. Of course those defaults can
 * be overriden by the specific instances.
 * <hr/>
 * <p>Rules to define Lines</p>
 * <ul>
 * <li>everything to be interpreted will be split with whitespaces '\s' (regex)</li>
 * <li>Token-Fields must begin with '[' and end with ']' </li>
 * <li>Additional you can use random numbers starting with a '#'</li>
 * <li>All other text will be interpreted as static text </li>
 * </ul>
 */
public class LineFactory {
    /**
     * LineFactory.of("[title] [firstname] [middlename] [lastname]".
     */
    public static LineFactory DEFAULT_NAME = LineFactory.of("[title] [firstname] [middlename] [lastname]");

    private final String lineTokenString;

    public LineFactory(String line) {
        this.lineTokenString = Objects.requireNonNull(line, "Cannot go with a null token line!");
    }

    public static LineFactory of(String lineTokens) {
        return new LineFactory(lineTokens);
    }

    private String harmonize(String value) {
        StringBuilder buffer = new StringBuilder();
        int a = (int) 'a';
        int z = (int) 'z';
        int A = (int) 'A';
        int Z = (int) 'Z';
        for (char single : value.toCharArray()) {
            int c = (int) single;
            // a=97  z=122  A=65  Z=90
            if ((c >= a && c <= z) || (c >= A && c <= Z) || Character.isDigit(single) || '#' == single) {
                buffer.append(single);
            }
        }
        // System.out.println(" --> " + buffer.toString());
        return buffer.toString();
    }

    public String getLine(Record<?> record, RandomUnit randomUnit) {
        int position = 0;
        final Map<Integer, Pair> tokenMap = new HashMap<>();
        for (String part : lineTokenString.split("\\s")) {
            String harmonizedPart = harmonize(part);
            final Comparable comparable = record.get(harmonizedPart);
            Character type;
            if (comparable != null) {
                type = 'F';  // field set
            } else if (record.getTokens().contains(harmonizedPart)) {
                type = 'U';  // unset token field
            } else if (harmonizedPart.startsWith("#")) {
                type = 'C';  // Comment/hashtag sign

                // count amount of digits and build that random number
                int amount = harmonizedPart.length();
                final int lower = (int) (Math.pow(10, (amount - 1)));
                final int upper = (int) (Math.pow(10, amount) - 1);
                long result = (long) randomUnit.nextLong(lower, upper);
                harmonizedPart = String.valueOf(result);
            } else {
                type = 'K';  // constant case
            }
            tokenMap.put(position++, Pair.of(type, harmonizedPart));
        }

        return getData(record, position, tokenMap);
    }

    private String getData(Record<?> record, Integer max, Map<Integer, Pair> tokenMap) {
        System.out.println("max = " + max + " tokenMap = " + tokenMap);
        StringJoiner builder = new StringJoiner(" ");
        for (int i = 0; i < max; i++) {
            final Pair typeAndValue = tokenMap.get(i);
            switch(typeAndValue.getKey()) {
                case 'F':
                    builder.add(record.get(typeAndValue.getValue().toString()));
                    break;
                case 'U':
                    final Comparable comparable = record.get(typeAndValue.getValue().toString());
                    builder.add(typeAndValue.getValue().toString());

                    break;
                case 'C':
                    // count amount of digits and build that random number
                    // int amount = String.valueOf(typeAndValue.getValue()).length();
                    // final double v = Math.pow(10, amount) - 1;

                    // System.out.println("typeAndValue = " + typeAndValue);
                    // builder.add("asdf");
                    builder.add(typeAndValue.getValue().toString());
                    break;
                case 'K':
                    builder.add(typeAndValue.getValue().toString());
                    break;
                default:
                    throw new IllegalStateException("Unknown type: " + typeAndValue.getKey());
            }
        }
        return builder.toString();
    }

    public String getLineTokenString() {
        return lineTokenString;
    }

    static class Pair implements Entry<Character, Comparable>, Comparable<Pair> {
        public final Character type;
        public final Comparable value;

        public Pair(Character left, Comparable right) {
            this.type = left;
            this.value = right;
        }

        public static Pair of(Character left, Comparable right) {
            return new Pair(left, right);
        }

        @Override
        public Character getKey() {
            return this.type;
        }

        @Override
        public Comparable getValue() {
            return this.value;
        }

        @Override
        public Comparable setValue(Comparable value) {
            return null;
        }

        @Override
        public int compareTo(Pair other) {
            return (this.getKey() + this.getValue().toString()).compareTo(other.getKey() + other.getValue().toString());
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (!(obj instanceof Entry)) {
                return false;
            } else {
                Entry<?, ?> other = (Entry) obj;
                return Objects.equals(this.getKey(), other.getKey()) && Objects.equals(this.getValue(), other.getValue());
            }
        }

        @Override
        public int hashCode() {
            return (this.getKey() == null ? 0 : this.getKey().hashCode()) ^ (this.getValue() == null ? 0 : this.getValue().hashCode());
        }

        @Override
        public String toString() {
            return "(" + this.getKey() + ',' + this.getValue() + ')';
        }
    }
}
