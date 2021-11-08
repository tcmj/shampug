package com.tcmj.shampug;

import com.github.curiousoddman.rgxgen.RgxGen;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Similar to java.util.Random but with more functionality.
 * E.g. nextHex(), ...
 */
public class Randoms implements RandomUnit {
    private final java.util.Random random;
    private String regexPattern;
    private RgxGen rgxGen;

    /**
     * Constructor to get a {@link Random} object with a random seed.
     * Internally a {@link ThreadLocalRandom} will be used
     */
    public Randoms() {
        this.random = ThreadLocalRandom.current();
    }

    /**
     * Constructor to specify a seed in order to get a specific random behaviour.
     * @param seed any long number
     */
    public Randoms(long seed) {
        this.random = new Random(seed);
    }

    /**
     * Constructor to pass your own instance of a random generator.
     * If you need a {@link java.security.SecureRandom} for any reason.
     * @param random any instance of a {@link Random}
     */
    public Randoms(java.util.Random random) {
        this.random = Objects.requireNonNull(random, "Please pass a non null instance of java.util.Random!");
    }

    @Override
    public double nextDouble() {
        return this.random.nextDouble();
    }

    @Override
    public Boolean nextBoolean() {
        return this.random.nextBoolean();
    }

    @Override
    public long nextLong() {
        return this.random.nextLong();
    }

    @Override
    public int nextInt() {
        return this.random.nextInt();
    }

    @Override
    public final int nextInt(final int bound) {
        return this.random.nextInt() * bound;
    }

    @Override
    public final int nextInt(final int origin, final int bound) {
        if (origin >= bound) {
            throw new IllegalArgumentException("Upper bound must be greater than origin!");
        }
        return origin + (this.random.nextInt() * (bound - origin));
    }

    @Override
    public String nextHex() {
        final String hex = Long.toString(nextInt(256), 16);
        return (hex.length() == 1) ? "0" + hex : hex;
    }

    /**
     * Returns a pseudorandom {@code long} value between the specified
     * origin (inclusive) and the specified bound (exclusive).
     * @param origin the least value returned
     * @param bound the upper bound (exclusive)
     * @return a pseudorandom {@code long} value between the origin (inclusive) and the bound (exclusive)
     * @throws IllegalArgumentException if {@code origin} is greater than or equal to {@code bound}
     */
    @Override
    public final long nextLong(final long origin, final long bound) {
        if (origin >= bound) {
            throw new IllegalArgumentException("Upper bound must be greater than origin!");
        }
        return origin + ((long) (this.random.nextDouble() * (bound - origin)));
    }

    /**
     * Returns a pseudorandom {@code long} value between zero and the specified bound (exclusive).
     * @param bound the upper bound (exclusive)
     * @return a pseudorandom {@code long} value between the origin (inclusive) and the bound (exclusive)
     * @throws IllegalArgumentException if {@code origin} is greater than or equal to {@code bound}
     */
    @Override
    public final long nextLong(final long bound) {
        return ((long) (this.random.nextDouble() * (bound)));
    }

    /**
     * Just use a regex pattern to build your random string.
     * For performance reasons you should create one ShamPug instance for each regex patterns because
     * caching takes place if the regex pattern doesn't change from call to call.
     * @param pattern regular expression pattern
     * @return random representation matching your pattern
     */
    @Override
    public String regex(final String pattern) {
        Objects.requireNonNull(pattern, "Regex pattern may not be null!");
        if (this.regexPattern == null || !this.regexPattern.equals(pattern)) {
            this.regexPattern = pattern;
            this.rgxGen = new RgxGen(pattern);
        }
        return this.rgxGen.generate(this.random);
    }
}
