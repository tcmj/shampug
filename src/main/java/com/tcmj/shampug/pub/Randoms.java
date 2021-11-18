package com.tcmj.shampug.pub;

import com.github.curiousoddman.rgxgen.RgxGen;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <h2>Implementation of {@link RandomUnit}.</h2>
 * Similar to java.util.Random but with more functionality.
 * E.g. {@link #nextHex()} and {@link #regex(String)}
 */
public class Randoms implements RandomUnit {
    private final java.util.Random random;
    private String regexPattern;
    private RgxGen rgxGen;

    /**
     * Constructor to get a {@link Random} object with a random seed.
     * Internally a {@link ThreadLocalRandom#current()} will be used
     */
    public Randoms() {
        this.random = ThreadLocalRandom.current();
    }

    /**
     * Constructor to specify a seed in order to get a specific random behaviour.
     * @param seed any long number
     */
    public Randoms(final long seed) {
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

    /**
     * Any random integer number.
     * @return integer within {@link Integer#MIN_VALUE} and {@link Integer#MAX_VALUE} both <b>inclusive</b>!
     */
    @Override
    public int nextInt() {
        return this.random.nextInt();
    }

    /**
     * Retrieve next random integer using a upper bound.
     * @param bound upper bound (exclusive)
     * @return next pseudo random integer
     */
    @Override
    public final int nextInt(final int bound) {
        return this.random.nextInt(bound);
    }

    /**
     * Retrieve next random integer within a range.
     * @param min lower bound (inclusive)
     * @param max upper bound (exclusive)
     * @return next pseudo random integer
     */
    @Override
    public final int nextInt(final int min, final int max) {
        if (min >= max) {
            throw new IllegalArgumentException("Upper bound must be greater than origin!");
        }
        if (this.random instanceof ThreadLocalRandom) {
            return ((ThreadLocalRandom) this.random).nextInt(min, max);
        }
        return this.random.nextInt((max - min)) + min;
    }

    @Override
    public String nextHex() {
        final String hex = Long.toString(nextInt(256), 16);
        return (hex.length() == 1) ? "0" + hex : hex;
    }

    /**
     * Returns a pseudorandom {@code long} value between the specified
     * origin (inclusive) and the specified bound (exclusive).
     * @param min the least value returned
     * @param max the upper bound (exclusive)
     * @return a pseudorandom {@code long} value between the origin (inclusive) and the bound (exclusive)
     * @throws IllegalArgumentException if {@code origin} is greater than or equal to {@code bound}
     */
    @Override
    public final long nextLong(final long min, final long max) {
        if (min >= max) {
            throw new IllegalArgumentException("Upper bound must be greater than origin!");
        }
        if (this.random instanceof ThreadLocalRandom) {
            return ((ThreadLocalRandom) this.random).nextLong(min, max);
        }
        return min + (this.random.nextLong() * (max - min));

    }

    /**
     * Returns a pseudorandom {@code long} value between zero and the specified bound (exclusive).
     * @param min the upper bound (exclusive)
     * @return a pseudorandom {@code long} value between the origin (inclusive) and the bound (exclusive)
     * @throws IllegalArgumentException if {@code origin} is greater than or equal to {@code bound}
     */
    @Override
    public final long nextLong(final long min) {
        if (this.random instanceof ThreadLocalRandom) {
            return ((ThreadLocalRandom) this.random).nextLong(min);
        }
        long bits;
        long val;
        do {
            bits = (this.random.nextLong() << 1) >>> 1;
            val = bits % min;
        } while (bits - val + (min - 1) < 0L);
        return val;
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