package com.tcmj.shampug;

import com.tcmj.shampug.intern.Registry;
import com.tcmj.shampug.intern.Registry.Strategy;
import com.tcmj.shampug.intern.ShamPugException;
import com.tcmj.shampug.modules.custom.AbstractRecord;

import java.util.Objects;
import java.util.Random;
import java.util.Set;

/**
 * Main class and entry point to create your random ShamPug data.
 * You can use ShamPug in a very simplistic way!
 * The following code shows how to use ShamPug with default values (eg.: seed=1000)
 * <pre>
 *    ShamPug shamPug = ShamPug.newGlobal();
 * </pre>
 * Another example shows how to define a registry strategy and using your own seed
 * There are following registry strategies: GLOBAL, PER_TREAD, NEW_INSTANCE which is used to define internal threading behaviour
 * <pre>
 *     ShamPug shamPug =
 *             ShamPug.setup()
 *                    .withRegistryStrategy(Strategy.GLOBAL)
 *                    .usingSeed(1234567869L)
 *                    .create();
 * </pre>
 */
public final class ShamPug {

    private final RandomUnit randoms;
    private Registry registry;

    /**
     * Special constructor if you need to pass your own instance of a random generator.
     * For example if you need to use your own {@link java.security.SecureRandom} for any reason.
     * @param randoms any instance of a {@link RandomUnit}
     */
    private ShamPug(RandomUnit randoms) {
        this.randoms = Objects.requireNonNull(randoms, "Please pass a non null instance of RandomUnit!");
    }

    public static Builder setup() {
        return new Builder();
    }

    public static ShamPug newGlobal() {
        return ShamPug.setup()
            .withRegistryStrategy(Strategy.GLOBAL)
            .usingSeed(1000L)
            .create();
    }

    public Registry getRegistry() {
        return this.registry;
    }

    public ShamPug put(Record rec1) {
        this.registry.put(rec1.key(), rec1);
        return this;
    }

    /**
     * Access to all standard randomness like numbers.
     */
    public RandomUnit getRandomUnit() {
        return this.randoms;
    }

    /**
     * Same as {@link #getRandomUnit()#regex(String)}
     */
    public String regex(String pattern) {
        return this.randoms.regex(pattern);
    }

    public <T extends Comparable<T>> Record<T> get(String address) {
        Set<Record<? extends Comparable>> found = registry.lookup(address);
        if (found != null) {
            return (Record<T>) found.stream().skip(getRandomUnit().nextInt(found.size())).findFirst().get();
        }
        throw new ShamPugException("No '" + address + "' records available!");
    }

    public <T extends AbstractRecord<T>> T get(Class<T> clazz) {
        Set<Record<? extends Comparable>> found = registry.lookup(clazz);
        if (found != null) {
            return (T) found.stream().skip(getRandomUnit().nextInt(found.size())).findFirst().get();
        }
        throw new ShamPugException("No '" + clazz + "' records available!");
    }

    public static class Builder {
        private Registry.Strategy registryStrategy;
        private Random customRandom;
        private Long customSeed;

        public Builder withRegistryStrategy(Registry.Strategy strategy) {
            this.registryStrategy = strategy;
            return this;
        }

        public Builder usingRandom(Random random) {
            this.customRandom = random;
            return this;
        }

        public Builder usingSeed(Long seed) {
            this.customSeed = seed;
            return this;
        }

        public ShamPug create() {
            Objects.requireNonNull(this.registryStrategy, "You have to call #withRegistryStrategy specifying either 'GLOBAL, PER_TREAD or NEW_INSTANCE'!");
            if (this.customSeed != null && this.customRandom != null) {
                throw new IllegalStateException("Setting a seed and a own java.util.Random object is not allowed! Use just only one!");
            }

            RandomUnit randomUnit;
            if (this.customSeed != null) {
                randomUnit = new Randoms(this.customSeed);
            } else if (this.customRandom != null) {
                randomUnit = new Randoms(this.customRandom);
            } else {
                randomUnit = new Randoms();
            }
            ShamPug shamPug = new ShamPug(randomUnit);

            if (Builder.this.registryStrategy == null) {
                shamPug.registry = Registry.get(Strategy.NEW_INSTANCE);
            } else {
                shamPug.registry = Registry.get(this.registryStrategy);
            }

            return shamPug;
        }
    }

}