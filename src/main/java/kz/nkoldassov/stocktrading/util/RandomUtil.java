package kz.nkoldassov.stocktrading.util;

import java.util.concurrent.ThreadLocalRandom;

public abstract class RandomUtil {

    private RandomUtil() {}

    public static int generateOccupiedId() {
        return ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
    }

}