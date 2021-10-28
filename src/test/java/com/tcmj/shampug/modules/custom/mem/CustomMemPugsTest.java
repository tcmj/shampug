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
 * [1] Simple Usage Tutorial for the custom in-memory data creation
 * Create your own custom data records in memory eg. for a unit test.
 * Here we create some pug dogs with our own attributes!
 * This is used if you want to programm the data used instead of using it from a json or a csv file.
 * Here you have to define everything in your code
 */
class CustomMemPugsTest {
    private static final Logger LOG = LoggerFactory.getLogger(CustomMemPugsTest.class);

    /**
     * We build some data records.
     */
    @BeforeAll
    static void setUp() {
        ShamPug shamPug = ShamPug.setup()
            .withRegistryStrategy(Strategy.GLOBAL) //with GLOBAL we use a static instance which you also get by ShamPug#newGlobal
            .usingSeed(1000L) // this is needed to get always the same random sequence so we can assert our test results (every run the same random numbers were generated)
            .create();   //To beautify we should make ShamPug as a static field in our class!

        //we define a simple new name to be used to store our data. In this case we want to create dog/pug datasets
        final String ourCategoryName = "pugs";

        //Now we need to build up some datarecords and store it in our current memory (as the name 'CustomMem' implies)
        //we want to use the same random generator so we pass: shamPug.getRandomUnit() this can be useful to get consistent random values
        Record<CustomMem> rec1 = new CustomMem(ourCategoryName, shamPug.getRandomUnit());
        rec1.set("name", "Baby"); //string
        rec1.set("weight", 6.5);  //double
        rec1.set("leader", false);//boolean
        rec1.set("color", 'S'); //character (B=Beige, S=Black)
        shamPug.put(rec1);

        Record<CustomMem> rec2 = new CustomMem(ourCategoryName, shamPug.getRandomUnit());
        rec2.set("name", "Emmy");
        rec2.set("weight", 5.0);
        rec2.set("leader", true);
        rec2.set("color", 'S');
        shamPug.put(rec2);

        //here is a much smarter way to do the same (the add method does exactly the same but has a return value for chaining)
        shamPug.put(
            new CustomMem(ourCategoryName, shamPug.getRandomUnit())
                .add("name", "Biene")
                .add("weight", 7.6)
                .add("leader", false)
                .add("color", 'B')
        );

        //You can use all attributes you want! In our case we chose name, weight, leader and color

        //...some more data...
        shamPug.put(new CustomMem(ourCategoryName, shamPug.getRandomUnit()).add("name", "Surie").add("weight", 5.1).add("leader", false).add("color", 'B'));
        shamPug.put(new CustomMem(ourCategoryName, shamPug.getRandomUnit()).add("name", "Betzy").add("weight", 7.4).add("leader", false).add("color", 'W'));
        shamPug.put(new CustomMem(ourCategoryName, shamPug.getRandomUnit()).add("name", "Bobby").add("weight", 8.2).add("leader", true).add("color", 'B'));
        shamPug.put(new CustomMem(ourCategoryName, shamPug.getRandomUnit()).add("name", "Sina").add("weight", 3.2).add("leader", true).add("color", 'W'));
        shamPug.put(new CustomMem(ourCategoryName, shamPug.getRandomUnit()).add("name", "Lisa").add("weight", 4.2).add("leader", true).add("color", 'B'));
        shamPug.put(new CustomMem(ourCategoryName, shamPug.getRandomUnit()).add("name", "Maxi").add("weight", 9.2).add("leader", true).add("color", 'B'));

        //now we are finished setting up our data and we can use it by retrieving it via our ShamPug instance see #simpleUsage

    }

    @Test
    void simpleUsageOfRetrievingOurData() {
        //First we create a new instance of our 'ShamPug' object (using default settings) anywhere in our codebase
        ShamPug shamPug = ShamPug.newGlobal();

        //then we get a record of our previously specified pugs category. This is where the randomness happens :-)
        Record<CustomMem> data = shamPug.get("pugs");
        LOG.info("We have just retrieved a random datarecord of our (In-Memory) pugs category: {}", data);

        assertNotNull(data, "We expect a non null instance and a random one!");

        //..and so we access our attributes:
        String name = data.get("name");
        Double weight = data.get("weight");
        Boolean leader = data.get("leader");

        assertNotNull(name, "Name must be available!");
        assertNotNull(weight, "Weight must be available");
        assertNotNull(leader, "Leader must be available");

        //consistency check: we can get those attributes as often as we want (no need to store them again for persistence)
        assertEquals(name, data.get("name"), "We should get the same field value as many times we call the get method");
        String nameA = data.get("name");
        String nameB = data.get("name");
        String nameC = data.get("name");
        assertTrue(nameA == nameB && nameB == nameC, "We will get exactly the same object as many times we call #get");

        //and because of our custom initial random instanciation value of 1000L we get everytime the same result so we can obviously check for the content
        //this is only possible because of our hack that we alway use the 1000L as init for our random generator and because of passing this so called RandomUnit to all of our CustomMem instances
        assertEquals(3.2, data.get("weight"));
        assertEquals(Boolean.TRUE, data.get("leader"));
    }


    @Test
    void conistencyUsingSameSeed() {
        //this time we use the manually setup instead of ShamPug.newGlobal(). Just to prove that it is the same
        ShamPug shamPug = ShamPug.setup()
            .withRegistryStrategy(Strategy.GLOBAL)
            .usingSeed(1000L) // this is needed to get always the same random sequence and we can assert our test results
            .create();

        for (int i = 0; i < 10; i++) {
            Record<CustomMem> data = shamPug.get("pugs");
            String name = data.get("name");
            Double weight = data.get("weight");
            LOG.trace("[{}] Name: {}, Weight: {}, Color: {}, Leader: {}", i, name, weight, data.get("color"), data.get("leader"));


            //...comparing some of the values of seed 1000...
            if (i == 0) {
                assertEquals("Sina", data.get("name"));
                assertEquals(3.2d, data.get("weight"));
            }
            if (i == 1) {
                assertEquals("Bobby", data.get("name"));
                assertEquals(8.2d, data.get("weight"));
            }
            if (i == 2) {
                assertEquals("Maxi", data.get("name"));
                assertEquals(9.2d, data.get("weight"));
            }
            if (i == 3) {
                assertEquals("Baby", data.get("name"));
                assertEquals(6.5d, data.get("weight"));
            }
            if (i == 4) {
                assertEquals("Betzy", data.get("name"));
                assertEquals(7.4d, data.get("weight"));
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

        LOG.debug("First we will retrieve five random pugs");
        ShamPug shamPug = ShamPug.newGlobal();
        for (int i = 1; i <= 5; i++) {
            Record<CustomMem> data = shamPug.get("pugs"); //here the random magic happens!
            LOG.trace("[{}] Record: {}", i, data);
        }

        LOG.debug("On another position of our code we will retrieve some random pugs more. If we use the same seed (of 1000L) we get the exact same random results");
        ShamPug shamPug2 = ShamPug.newGlobal();
        for (int i = 1; i <= 5; i++) {
            Record<CustomMem> data = shamPug2.get("pugs"); //here the random magic happens!
            LOG.trace("[{}] Record: {}", i, data);
        }

        LOG.debug("Of course you can change the seed easily and get some other random pugs. Best practice is using the current time for the seed...");
        ShamPug shamPug3 = ShamPug.setup().withRegistryStrategy(Strategy.GLOBAL).usingSeed(System.currentTimeMillis()).create();
        for (int i = 1; i <= 5; i++) {
            Record<CustomMem> data = shamPug3.get("pugs"); //here the random magic happens!
            LOG.trace("[{}-] Record: {}", i, data);
        }
    }

}