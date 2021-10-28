package com.tcmj.shampug;

/**
 * This interface kinda imitates the behaviour of standard java Random class.
 * The benefit is that we can implement our own random class like {@link Randoms}
 */
public interface RandomUnit {
    double nextDouble();

    Boolean nextBoolean();

    long nextLong();

    int nextInt();

    int nextInt(int bound);

    int nextInt(int origin, int bound);

    String nextHex();

    long nextLong(long origin, long bound);

    long nextLong(long bound);

    String regex(String pattern);
}
