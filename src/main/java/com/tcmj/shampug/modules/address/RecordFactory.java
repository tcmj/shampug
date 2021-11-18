package com.tcmj.shampug.modules.address;

import java.util.function.Supplier;

/**
 * Factory class to create instances of test classes.
 * A nice feature ist to specify default values for fields. Of course those defaults can
 * be overriden by the specific instances.
 * @param <T> the test class
 */
public class RecordFactory<T extends Comparable<T>> {
    private Supplier<T> supplier;

    /**
     * Constructor used to specify default values. Here we create some famous family members:
     * <pre>
     *     LineFactory<Address> lineFactory = new LineFactory<>(() -> new Address().lastName("Stark"));
     *     ShamPug shamPug = new ShamPug();
     *     shamPug.put(
     *       lineFactory.create()
     *          .firstName("Robb")
     *     );
     *     shamPug.put(
     *       lineFactory.create()
     *          .firstName("Eddard")
     *     );
     * </pre>
     * @param supplier
     */
    public RecordFactory(Supplier<T> supplier) {
        this.supplier = supplier;
    }


    public T create() {
        return this.supplier.get();
    }
}
