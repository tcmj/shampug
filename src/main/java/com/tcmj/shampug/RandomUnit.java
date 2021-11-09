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

    int nextInt(int min, int max);

    String nextHex();

    long nextLong(long min, long max);

    long nextLong(long min);

    String regex(String pattern);
}
