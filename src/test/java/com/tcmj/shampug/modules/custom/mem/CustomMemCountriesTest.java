package com.tcmj.shampug.modules.custom.mem;

import com.tcmj.shampug.Record;
import com.tcmj.shampug.ShamPug;
import com.tcmj.shampug.intern.Registry.Strategy;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * [2] Simple Usage Tutorial for the custom in-memory data creation
 * This is the same as we saw in the {@link CustomMemPugsTest} but using countries instead.
 * This time our code can look a little more professional
 */
class CustomMemCountriesTest {

    public static final String CATEGORY_NAME = "countries";
    public static final long SEED = 223220222L;
    private static final Logger LOG = LoggerFactory.getLogger(CustomMemCountriesTest.class);
    private static final ShamPug SHAMPUG = ShamPug.setup()
        .withRegistryStrategy(Strategy.GLOBAL) //with GLOBAL we use a static instance which you also get by ShamPug#newGlobal
        .usingSeed(SEED) // this is needed to get always the same random sequence, and we can assert our test results
        .create();

    /**
     * We build some data records for our custom countries.
     */
    @BeforeAll
    static void setUp() {
        //...create some data
        SHAMPUG.put(new CustomMem(CATEGORY_NAME, SHAMPUG.getRandomUnit()).add("name", "Afghanistan").add("population", 38_928_346).add("area", 652_860).add("density", 60));
        SHAMPUG.put(new CustomMem(CATEGORY_NAME, SHAMPUG.getRandomUnit()).add("name", "Austria").add("population", 9_006_398).add("area", 82_409).add("density", 109));
        SHAMPUG.put(new CustomMem(CATEGORY_NAME, SHAMPUG.getRandomUnit()).add("name", "Cuba").add("population", 11_326_616).add("area", 106_440).add("density", 106));
        SHAMPUG.put(new CustomMem(CATEGORY_NAME, SHAMPUG.getRandomUnit()).add("name", "Egypt").add("population", 102_334_404).add("area", 995_450).add("density", 103));
        SHAMPUG.put(new CustomMem(CATEGORY_NAME, SHAMPUG.getRandomUnit()).add("name", "Russia").add("population", 145_934_462).add("area", 16_376_870).add("density", 9));
        SHAMPUG.put(new CustomMem(CATEGORY_NAME, SHAMPUG.getRandomUnit()).add("name", "United States of America").add("population", 331_002_651).add("area", 9_147_420).add("density", 36));
    }


    @Test
    void simpleUsage() {
        Record<CustomMem> data = SHAMPUG.get(CATEGORY_NAME);
        assertNotNull(data, "DataRecord expected!");

        String name = data.get("name");
        Integer population = data.get("population");
        Integer density = data.get("density");

        assertNotNull(name, "Name expected!");
        assertNotNull(population, "Population expected");
        assertNotNull(density, "Density expected");

        //consistency check
        assertEquals(name, data.get("name"), "We should get the same field value as many times we call the get method");
        String nameA = data.get("name");
        String nameB = data.get("name");
        String nameC = data.get("name");
        assertTrue(nameA == nameB && nameB == nameC, "We will get exactly the same object as many times we call #get");

        assertEquals(population, data.get("population"));
        assertEquals(density, data.get("density"));
        for (int i = 1; i <= 11; i++) {
            LOG.info("We have just retrieved a random data record of our (In-Memory) {} category: {}", CATEGORY_NAME, SHAMPUG.get(CATEGORY_NAME));
        }
        int amount = 0;
        for (int i = 1; i <= 11111; i++) {
            Record<CustomMem> dat = SHAMPUG.get(CATEGORY_NAME);
            String myName = dat.get("name");
            if ("Austria".equals(myName)) {
                amount++;
                assertEquals(109, (Integer) dat.get("density"));
                assertEquals(9_006_398, (Integer) dat.get("population"));
            }
        }
        LOG.info("{} times went to Austria with seed: {}", amount, SEED);
    }
}
