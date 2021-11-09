package com.tcmj.shampug;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomsTest {

    private static final long SEED = 123456789L;
    private static Randoms cut;

    @BeforeAll
    static void setUp() {
        RandomsTest.cut = new Randoms(SEED);
    }

    private static void checkResult(int navigate, LoopResult lr) {
        if (navigate == 1) {
            lr.lowerFound = true;
        }
        if (navigate == 2) {
            lr.upperFound = true;
        }
    }

    private static int checkIntLowerAndUpperBound(Number result, Number lower, Number upper, Long loop) {
        boolean lowerFound = false, upperFound = false;
        if (result.equals(lower)) {
            lowerFound = true;
            System.out.println("lowerFound @ " + loop + " cut.nextInt(lower, upper) = " + result);
        }
        if (result.equals(upper)) { //exclusive!
            upperFound = true;
            System.out.println("upperFound @ " + loop + " cut.nextInt(lower, upper) = " + result);
        }
        if (lowerFound) {
            return 1;
        } else if (upperFound) {
            return 2;
        } else {
            return 0;
        }
    }

    @Test
    void nextDouble() {
        for (int i = 0; i < 12345; i++) {//test would take too long to search min and max!
            double result = cut.nextDouble();
            assertTrue(result <= Double.MAX_VALUE, "Upper range should match (incl.)");
            assertTrue(result >= Double.MIN_VALUE, "Lower range should match (incl.)");
        }
    }

    @Test
    void nextBoolean() {
        for (int i = 0; i < 12345; i++) {//test would take too long to search min and max!
            Boolean result = cut.nextBoolean();
            assertTrue(result instanceof Boolean, "Boolean created!");
        }
    }

    @Test
    void nextInt() { //...Integer MIN and MAX (both inclusive)
        for (int i = 0; i < 12345; i++) {//test would take too long to search min and max!
            int result = cut.nextInt();
            assertTrue(result <= Integer.MAX_VALUE, "Upper range should match (incl.)");
            assertTrue(result >= Integer.MIN_VALUE, "Lower range should match (incl.)");
        }
    }

    @Test
    void nextIntUpperBound() { //...from zero to upper bound!
        int upper = 12030, loop = 0;
        LoopResult lRes = new LoopResult();
        do {
            loop++;
            int result = cut.nextInt(upper);
            assertTrue(result < upper, "Upper range should match (incl.)" + result + " / " + upper);
            assertTrue(result >= 0, "Lower range should match (excl.)" + result + " / " + upper);
            int navigate = checkIntLowerAndUpperBound(result, 0, upper - 1, Long.valueOf(loop));
            checkResult(navigate, lRes);
        } while (!lRes.lowerFound || !lRes.upperFound);
    }

    @Test
    void nextIntLowerAndUpperBound() {
        int lower = 222, upper = 1000, loop = 0;
        LoopResult lRes = new LoopResult();
        do {
            loop++;
            int result = cut.nextInt(lower, upper);
            assertTrue(result < upper, "Upper range should match (incl.)" + result + " / " + upper);
            assertTrue(result >= lower, "Lower range should match (excl.)" + result + " / " + upper);
            int navigate = checkIntLowerAndUpperBound(result, lower, upper - 1, Long.valueOf(loop));
            checkResult(navigate, lRes);
        } while (!lRes.lowerFound || !lRes.upperFound);
    }

    @Test
    void nextLong() {
        for (int i = 0; i < 12345; i++) {//test would take too long to search min and max!
            long result = cut.nextLong();
            assertTrue(result <= Long.MAX_VALUE, "Upper range should match (incl.)");
            assertTrue(result >= Long.MIN_VALUE, "Lower range should match (incl.)");
        }
    }

    @Test
    void nextLongUpperBound() {
        long upper = 12030L, loop = 0;
        LoopResult lRes = new LoopResult();
        do {
            loop++;
            long result = cut.nextLong(upper);
            assertTrue(result < upper, "Upper range should match (incl.)" + result + " / " + upper);
            assertTrue(result >= 0, "Lower range should match (excl.)" + result + " / " + upper);
            int navigate = checkIntLowerAndUpperBound(result, 0L, upper - 1, loop);
            checkResult(navigate, lRes);
        } while (!lRes.lowerFound || !lRes.upperFound);
    }

    @Test
    void regex() {
        String regex = "user[0-9]{4}@mail\\.com";
        Pattern pattern = Pattern.compile(regex);
        for (int i = 0; i < 12345; i++) {
            String result = cut.regex(regex);
            Matcher matcher = pattern.matcher(result);
            assertTrue(matcher.matches(), "Regex not valid: " + result);
        }
    }

    @Test
    void nextHex() {
        for (int i = 0; i < 12345; i++) {//test would take too long to search min and max!
            String result = cut.nextHex();
            assertTrue(Long.valueOf(result, 16) instanceof Long, "Hex not valid: " + result);
        }
    }

    static class LoopResult {
        boolean lowerFound = false, upperFound = false;
    }

}