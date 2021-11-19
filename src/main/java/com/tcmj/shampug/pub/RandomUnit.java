package com.tcmj.shampug.pub;

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

    /**
     * Creates a regular expression.
     * amount of chars used for infinite expressions (e.g. '+') is 100
     * creates case-sensitive results (e.g. 'a+b' creates 'aaab')
     * @param pattern regex pattern to be used
     * @return the random string based on the regex
     */
    String regex(String pattern);

    /**
     * Creates a regular expression.
     * creates case-sensitive results (e.g. 'a+b' creates 'aaab')
     * @param pattern regex pattern to be used
     * @param infinity amount of chars used for infinite expression ('+')
     * @return the random string based on the regex
     */
    String regex(String pattern, int infinity);

    /**
     * Creates a regular expression.
     * @param pattern regex pattern to be used
     * @param infinity amount of chars used for infinite expression ('+')
     * @param caseSensitivity true creates case sensitive results (e.g. 'aAaA'). false (default) creates 'aaaa'.
     * @return the random string based on the regex
     */
    String regex(String pattern, int infinity, boolean caseSensitivity);
}
