package com.ctb.classes;

import java.util.Objects;
import java.util.Random;

public class ProductApplication {

    private static final Random rand = new Random();

    /*----------------------Class Methods----------------------*/
    protected static long generateProductID(final String productType) {
        long time = System.currentTimeMillis();
        int randomNumber = rand.nextInt(9999);
        String idString;
        if (Objects.equals(productType, "Savings Account")) {
            idString = "1" + String.format("%013d", time) + String.format("%04d", randomNumber);
        } else if (Objects.equals(productType, "Credit Account")) {
            idString = "2" + String.format("%013d", time) + String.format("%04d", randomNumber);
        } else {
            idString = "3" + String.format("%013d", time) + String.format("%04d", randomNumber);
        }
        return Long.parseLong(idString);
    }
}
