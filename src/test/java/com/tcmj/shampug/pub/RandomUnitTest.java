package com.tcmj.shampug.pub;

import com.tcmj.shampug.ShamPug;
import com.tcmj.shampug.intern.ShamPugException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomUnitTest {


    @Test
    void nextIntWithUpperBound() {
        final Randoms randoms = new Randoms(123L);
        final int max = 1000;
        int current = 0;
        int counter = 0;
        while (current != max) {
            counter++;
            current = randoms.nextInt(max + 1); // +1 because of exclusive
        }
        System.out.println("iterations to reach 1000 = " + counter);
    }

    @Test
    void nextIntWithBounds() {
        final Randoms randoms = new Randoms();
        final Set<Integer> integers = IntStream.range(12345, 67890).boxed().collect(Collectors.toSet());
        int counter = 0;
        while (integers.size() > 0) {
            counter++;
            final int nextInt = randoms.nextInt(12345, 67890 + 1);
            integers.remove(nextInt);
        }
        System.out.println("iterations to produces all numbers between 12345 and 67890 = " + counter);
    }

    @Test
    void nextIntWithIllegalBounds() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Randoms().nextInt(22, 11);
        });
    }

    @Test
    void nextLongWithBounds() {
        final Randoms randoms = new Randoms();
        final Set<Long> longs = LongStream.range(10000000000L, 10000001000L).boxed().collect(Collectors.toSet());
        int counter = 0;
        while (longs.size() > 0) {
            counter++;
            final long nextLong = randoms.nextLong(10000000000L, 10000001001L);
            longs.remove(nextLong);
        }
        System.out.println("iterations to produces all numbers between 10000000000L and 10000001000L = " + counter);
    }

    @Test
    void nextLongWithIllegalBounds() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ShamPug.newGlobal().getRandomUnit().nextLong(20000000000L, 10000000000L);
        });
    }

    @Test
    void nextLongWithUpperBound() {
        final Randoms randoms = new Randoms(5000L);
        // Results of finding 11.111.111.111:
        // with seed = 69  we need   1.842.203.385 iterations to find our max value       (42s)
        // with seed = 169L we need  3.229.376.111 iterations to find our max value    (1m 14s)
        // with seed = 1000 we need  1.952.153.348 iterations to find our max value       (44s)
        // with seed = 2000 we need  7.263.281.325 iterations to find our max value    (2m 36s)
        final long max = 11_111_111L;
        // with seed = 333.333 we need  29.078.177 iterations to find our max value of 11.111.111  (736ms)
        // with seed =   5.000 we need   4.558.488 iterations to find our max value of 11.111.111  (154ms)
        long current = 0L;
        long counter = 0;
        while (current != max) {
            counter++;
            current = randoms.nextLong(max + 1); // +1 because of exclusive
        }
        System.out.println("iterations to reach 11111111111L = " + counter);
    }

    @Test
    void nextHex() {
        final Randoms randoms = new Randoms(69L);
        Pattern pattern = Pattern.compile("[a-f0-9]{2}");
        for (int i = 0; i < 151515; i++) {
            final String hex = randoms.nextHex();
            if (i < 10) {
                System.out.println("nextHex = " + hex);
            }
            Matcher matcher = pattern.matcher(hex);
            assertTrue(matcher.matches());
        }
    }

    @Test
    void regexUseCase1() {
        final Randoms randoms = new Randoms(111L);
        final String regex = "(http|https)\\:\\/\\/www\\.[aeiou]+[a-z]{2,5}\\.(com|de|org|net)";
        Pattern pattern = Pattern.compile(regex);
        for (int i = 0; i < 10; i++) {
            final String result = randoms.regex(regex);
            System.out.println(result);
            Matcher matcher = pattern.matcher(result);
            assertTrue(matcher.matches());
        }
    }

    @Test
    void regexUseCase2() {
        final Randoms randoms = new Randoms();
        String regex = // German IBANs (with and without spaces after each group)
            "DE\\d{2}[ ]\\d{4}[ ]\\d{4}[ ]\\d{4}[ ]\\d{4}[ ]\\d{2}|DE\\d{20}";
        Pattern pattern = Pattern.compile(regex);
        for (int i = 0; i < 10; i++) {
            final String result = randoms.regex(regex);
            System.out.println(result);
            Matcher matcher = pattern.matcher(result);
            assertTrue(matcher.matches());
        }
    }

    @Test
    void objectNotFound() {
        Assertions.assertThrows(ShamPugException.class, () -> {
            ShamPug.newGlobal().get("ThisThingIsNotAvailable");
        });
    }

}