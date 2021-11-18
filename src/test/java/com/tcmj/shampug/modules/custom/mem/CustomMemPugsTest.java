package com.tcmj.shampug.modules.custom.mem;

import com.tcmj.shampug.ShamPug;
import com.tcmj.shampug.intern.Registry.Strategy;
import com.tcmj.shampug.pub.Record;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <h2>Simple Usage / Tutorial for the custom in-memory data creation.</h2>
 * <p>
 * ShamPug consists of several - lets call it - submodules to be used for different use cases.
 * The first module/use case is the memory module. You can use it to quickly build up some test data and use it in a random way.
 * </p><br/><p>
 * To begin we will create our own custom data records (in memory) for a unit test. Later you will find some other modules where you can load
 * data from json or even access existing random data for user registration
 * </p><p><br/>
 * Here we create some pug dogs with some custom defined attributes!
 * In this case we build everything from scratch in java source code.
 * </p>
 */
class CustomMemPugsTest {
    private static final Logger LOG = LoggerFactory.getLogger(CustomMemPugsTest.class);

    /**
     * Static Initialization: Let's build some data records.
     */
    @BeforeAll
    static void setUp() {
        /* First line will be initialization of our ShamPug object. This is the simplest way to do it. */
        ShamPug shamPug = ShamPug.newGlobal();

        // we define a simple new name to be used to store our data. Let's call it 'pugs'
        final String categoryName = "pugs";

        /* Now we need to build some data records using the in-memory-module class {@link CustomMem}.
         *  Please notice the 2nd parameter where we pass the {@link RandomUnit} to be able to always get the same random data for each run (using the same seed)
         *  We could use all attributes we want! In our case we chose 'name', 'weight', 'leader' and 'color' */
        Record<CustomMem> rec1 = new CustomMem(categoryName, shamPug.getRandomUnit());
        rec1.set("name", "Baby");  // string
        rec1.set("weight", 6.5);   // double            << we can use different data types as we wish
        rec1.set("leader", false); // boolean
        rec1.set("color", 'S');    // character (B=Beige, S=Black)
        shamPug.put(rec1);

        Record<CustomMem> rec2 = new CustomMem(categoryName, shamPug.getRandomUnit());
        rec2.set("name", "Emmy");
        rec2.set("weight", 5.0);
        rec2.set("leader", true);
        rec2.set("color", 'S');
        shamPug.put(rec2);

        // here is a much smarter way to do the same (the CustomMem#add method does exactly the same but has a return value for chaining)
        shamPug.put(
            new CustomMem(categoryName, shamPug.getRandomUnit())
                .add("name", "Biene")
                .add("weight", 7.6)
                .add("leader", false)
                .add("color", 'B')
        );

        // ...some more data...
        shamPug.put(new CustomMem(categoryName, shamPug.getRandomUnit()).add("name", "Surie").add("weight", 5.1).add("leader", false).add("color", 'B'));
        shamPug.put(new CustomMem(categoryName, shamPug.getRandomUnit()).add("name", "Betzy").add("weight", 7.4).add("leader", false).add("color", 'W'));
        shamPug.put(new CustomMem(categoryName, shamPug.getRandomUnit()).add("name", "Bobby").add("weight", 8.2).add("leader", true).add("color", 'B'));
        shamPug.put(new CustomMem(categoryName, shamPug.getRandomUnit()).add("name", "Sina").add("weight", 3.2).add("leader", true).add("color", 'W'));
        shamPug.put(new CustomMem(categoryName, shamPug.getRandomUnit()).add("name", "Lisa").add("weight", 4.2).add("leader", true).add("color", 'B'));
        shamPug.put(new CustomMem(categoryName, shamPug.getRandomUnit()).add("name", "Maxi").add("weight", 9.2).add("leader", true).add("color", 'B'));

    }

    @Test
    void simpleUsageOfRetrievingOurData() {
        /*
            Notice: We used to store our data globally, so we are not needed to use the same ShamPug instance.
            We just create a new one and can access the data stored. This could be changed using another Strategy (more on that later)
         */

        // First we create a new instance of our 'ShamPug' object (using default settings) anywhere in our codebase
        ShamPug shamPug = ShamPug.newGlobal(); // Global mode!!

        // then we get a record of our previously specified pugs category. This is where the randomness happens :-)
        Record<CustomMem> data = shamPug.get("pugs");
        LOG.info("We have just retrieved a random data record of our (In-Memory) pugs category: {}", data);

        assertNotNull(data, "We expect non null!");
        assertInstanceOf(Record.class, data, "We expect a instance of a Record!");

        // and now we can access our attributes:
        String name = data.get("name");
        Double weight = data.get("weight");
        Boolean leader = data.get("leader");

        assertNotNull(name, "Name must be available!");
        assertNotNull(weight, "Weight must be available");
        assertNotNull(leader, "Leader must be available");

        // consistency check: we can get those attributes as often as we want (no need to store them again for persistence)
        assertEquals(name, data.get("name"), "We should get the same field value as many times we call the get method");
        String nameA = data.get("name");
        String nameB = data.get("name");
        String nameC = data.get("name");
        assertTrue(nameA == nameB && nameB == nameC, "We will get exactly the same object as many times we call #get");

        // and because of our custom initial random instantiation value (default seed = 1000L) we get everytime the same result, so we can obviously check the content in this unit test
        // this is possible because of passing this so called RandomUnit to all of our CustomMem instances ('new CustomMem(categoryName, shamPug.getRandomUnit())')
        assertEquals(4.2, data.get("weight"));
        assertEquals(Boolean.TRUE, data.get("leader"));
    }


    @Test
    void conistencyUsingSameSeed() {
        // this time we use the manual setup instead of ShamPug.newGlobal(). Just to prove that it will do the same thing
        ShamPug shamPug = ShamPug.setup()
            .withRegistryStrategy(Strategy.GLOBAL)   // global storage strategy
            .usingSeed(1000L) // seed is needed to get always the same random sequence, and we can assert our test results
            .create();

        for (int i = 0; i < 10; i++) {
            Record<CustomMem> data = shamPug.get("pugs");
            String name = data.get("name");
            Double weight = data.get("weight");
            LOG.info("[{}] Name: {}, Weight: {}, Color: {}, Leader: {}", i, name, weight, data.get("color"), data.get("leader"));


            // ...comparing some values of seed 1000...
            if (i == 0) {
                assertEquals("Lisa", data.get("name"));
                assertEquals(4.2d, data.get("weight"));
            }
            if (i == 1) {
                assertEquals("Emmy", data.get("name"));
                assertEquals(5.0d, data.get("weight"));
            }
            if (i == 2) {
                assertEquals("Surie", data.get("name"));
                assertEquals(5.1d, data.get("weight"));
            }
            if (i == 3) {
                assertEquals("Lisa", data.get("name"));
                assertEquals(4.2d, data.get("weight"));
            }
            if (i == 4) {
                assertEquals("Surie", data.get("name"));
                assertEquals(5.1d, data.get("weight"));
            }

        }
        /* we expect exact this data (because of init with 1000L as mentioned before)
        [0] Name: Sina, Weight: 3.2, Color: W, Leader: true
        [1] Name: Bobby, Weight: 8.2, Color: B, Leader: true
        [2] Name: Maxi, Weight: 9.2, Color: B, Leader: true
        [3] Name: Baby, Weight: 6.5, Color: S, Leader: false
        [4] Name: Betzy, Weight: 7.4, Color: W, Leader: false
        [5] Name: Betzy, Weight: 7.4, Color: W, Leader: false
        [6] Name: Bobby, Weight: 8.2, Color: B, Leader: true
        [7] Name: Betzy, Weight: 7.4, Color: W, Leader: false
        [8] Name: Bobby, Weight: 8.2, Color: B, Leader: true
        [9] Name: Maxi, Weight: 9.2, Color: B, Leader: true
         */
    }


    @Test
    void loopSomeAdditionalPugUsages() {

        Record<CustomMem> firstRecordL1 = null;
        Record<CustomMem> firstRecordL2 = null;

        LOG.info("First we will retrieve five random pugs");
        ShamPug shamPug = ShamPug.newGlobal();
        for (int i = 1; i <= 5; i++) {
            Record<CustomMem> data = shamPug.get("pugs"); // here the random magic happens!
            if (i == 1) {
                firstRecordL1 = data;
                LOG.info("[{}] Record: {}", i, data);
            }
        }

        LOG.info("On another position of our code we will retrieve some random pugs more. If we use the same seed (of 1000L) we get the exact same random results");
        ShamPug shamPug2 = ShamPug.newGlobal();
        for (int i = 1; i <= 5; i++) {
            Record<CustomMem> data = shamPug2.get("pugs"); // here the random magic happens!
            if (i == 1) {
                firstRecordL2 = data;
                LOG.info("[{}] Record: {}", i, data);
            }
        }

        LOG.info("[Loop1] Record1: {}, [Loop2] Record1: {}", firstRecordL1, firstRecordL2);
        assertSame(firstRecordL1, firstRecordL2, "We will get exactly the same object as many times we call #get");


        LOG.info("Of course you can change the seed easily and get some other random pugs. Best practice is using the current time for the seed...");
        ShamPug shamPug3 = ShamPug.setup().withRegistryStrategy(Strategy.GLOBAL).usingSeed(System.currentTimeMillis()).create();
        for (int i = 1; i <= 5; i++) {
            Record<CustomMem> data = shamPug3.get("pugs"); // here the random magic happens!
            LOG.info("[{}-] Record: {}", i, data);
        }
    }

}
