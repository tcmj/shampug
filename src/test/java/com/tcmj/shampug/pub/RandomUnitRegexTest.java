package com.tcmj.shampug.pub;


import com.tcmj.shampug.ShamPug;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A great feature is to use the internal reverse regex creation.
 * Here you can get the idea by investigating some examples.
 * <p>Consider that {@link ShamPug#regex(String)} is the same as {@link RandomUnit#regex(String)} so you can use this feature in the ShamPug class
 * or in the lighter RandomUnit class.</p>
 */
public class RandomUnitRegexTest {

    /** ShamPug object using an internal RandomUnit object with seed of 1000L. */
    private static final ShamPug SHAMPUG = ShamPug.newGlobal();

    /** RandomUnit object using seed of 1000L. */
    private static final RandomUnit randoms = new Randoms(1000L);

    /** Amount of test examples to be created. */
    private static final int AMOUNT_OF_EXAMPLES = 5;


    private void toExamples(final String regex) {
        Pattern pattern = Pattern.compile(regex);
        for (int i = 1; i <= AMOUNT_OF_EXAMPLES; i++) {
            final String result1 = SHAMPUG.regex(regex);  // Shortcut on the ShamPug object...
            final String result2 = randoms.regex(regex);  // ...can also be used via RandomUnit
            System.out.println(result1);
            assertTrue(pattern.matcher(result1).matches());
            assertTrue(pattern.matcher(result2).matches());
            assertEquals(result1, result2, "Both results should be equal!");
        }
    }

    @Test
    void createSomething() {
        toExamples("^a+c$");
    }

    @Test
    void createFileExtensions() {
        toExamples("\\.jpg|\\.png|\\.js|\\.gif|\\.txt|\\.java|\\.doc|\\.ipm|\\.xml|\\.tiff");
    }

    /**
     * \w matches any word character (equivalent to [a-zA-Z0-9_])
     * {4} matches the previous token exactly 4 times
     */
    @Test
    void create4digitWords() {
        toExamples("[\\w]{4}");
    }

    @Test
    void createTimestamps() {
        toExamples("([01]?[0-9]|2[0-3]):[0-5][0-9]");
    }

    @Test
    void createIbans() {
        toExamples("^DE([0-9]{2})(\\d{4})(\\d{4})(\\d{4})(\\d{4})([\\d]{0,2})$");
    }

    @Test
    void createPhone() {
        toExamples("01[7856][0-9]{2}-[0-9]{6}");
    }

    @Test
    void createUUIDs() {
        toExamples("[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}");
    }

    @Test
    void createEuroCurrencies() {
        toExamples("\\€([1-9]{1,3}(,\\d{3}){0,3}|([1-9]{1,3}))(\\.\\d{2})?");
    }

    @Test
    void createIPs() {
        toExamples("(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])");
    }

    @Test
    void createDigits() {
        toExamples("[\\d]{4}");
    }

    @Test
    void createIPv4() {
        toExamples("([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})");
    }

    @Test
    void createUrls() {
        // Creates http or https web urls of type .com .de .org or .net with random chars of length 3 to 8
        toExamples("(http|https)\\:\\/\\/www\\.[aeiou]{4}[a-z]{1,2}\\.(com|de|org|net)");
    }

    @Test
    void createText50() {
        toExamples(".{50}");
    }

    @Test
    void createEmailAddies() {
        // emails of the same domain
        toExamples("user[0-9]{4}\\@mail\\.com");
    }

    @Test
    void checkForPrimes() { //just a nice regex example - but it doesn't work with ShamPug
        for (int i = 0; i < 120; i++) {
            if (!new String(new char[i]).matches(".?|(..+?)\\1+")) {
                System.out.println(i + " is Prime ");
            }
        }
    }

}
