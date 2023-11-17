package com.ctb.classes;

import java.util.Calendar;
import java.util.Objects;
import java.util.Random;

public class ProductApplication { // TODO: obsolete file
    private static final Calendar calendar = Calendar.getInstance();
    private static final Random rand = new Random();
    private String productID;
    private String productType;

    /*----------------------Setter Methods----------------------*/
    public void setProductID(String productID) {
        this.productID = productID;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    /*----------------------Getter Methods----------------------*/
    public String getProductID() {
        return productID;
    }

    public String getProductType() {
        return productType;
    }

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
