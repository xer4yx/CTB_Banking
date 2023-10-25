package com.ctb.classes;

import java.util.Calendar;
import java.util.Objects;
import java.util.Random;

public class ProductApplication {
    private static final Calendar calendar = Calendar.getInstance();
    private static final Random rand = new Random();
    private String productID;
    private String productType;

    /*----------------------Setter Methods----------------------*/
    public void setProductID(String productID) {this.productID = productID;}
    public void setProductType(String productType) {this.productType = productType;}

    /*----------------------Getter Methods----------------------*/
    public String getProductID() {return productID;}
    public String getProductType() {return productType;}

    /*----------------------Class Methods----------------------*/
    protected static String generateProductID(final String productType) {
        long time = calendar.getTimeInMillis();
        int randomNumber = rand.nextInt();
        String timeString = Long.toString(time);
        String randomNumberString = Integer.toString(randomNumber);
        if (Objects.equals(productType, "Savings Account"))
        {
            return "SAV" + timeString + randomNumberString;
        }
        if (Objects.equals(productType, "Credit Account"))
        {
            return "CRD" + timeString + randomNumberString;
        }
        else
        {
            return "PRD" + timeString + randomNumberString;
        }
    }
}
