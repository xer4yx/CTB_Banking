package com.ctb.classes;

public class ProductApplication {
    private String productID;
    private String productType;

    /*----------------------Setter Methods----------------------*/
    public void setProductID(String productID) {this.productID = productID;}
    public void setProductType(String productType) {this.productType = productType;}

    /*----------------------Getter Methods----------------------*/
    public String getProductID() {return productID;}
    public String getProductType() {return productType;}

    /*----------------------Class Methods----------------------*/
    private String generateProductID(final String productType) {
        if (producttype == "Savings Account")
        {
            return "SAV" + to_string(time(nullptr)) + to_string(rand());
        }
        if (producttype == "Credit Account")
        {
            return "CRD" + to_string(time(nullptr)) + to_string(rand());
        }
        else
        {
            return "PRD" + to_string(time(nullptr)) + to_string(rand());
        }
    }
}
