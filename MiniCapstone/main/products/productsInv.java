package main.products;

import java.sql.Date;

public class productsInv { //encapsulate data

    final static String DATE_FORMAT = "YYYY-MM-DD";

    private String product_name, product_category ; //private variables
    private String product_quantity;
    private Date product_exp, date_today, date_added, date_quantity_modified, date_removed;
    private long days_left;

    //mutators

    public void setDateQuantityModified(Date date_quantity_modified) { 

        this.date_quantity_modified = date_quantity_modified;

    }

    public void setDateRemoved(Date date_removed) { 

        this.date_removed = date_removed;
    }

    public void setProductName(String product_name) { 

        this.product_name = product_name;

    }

    public void setProductCategory(String product_category) { 

        this.product_category = product_category;
    }

    public void setProductQuantity(String product_quantity) {

        this.product_quantity = product_quantity;

    }

    public void setDateToday(Date date_today) { 

        this.date_today = date_today;

    }

    public void setProductExpiration(Date product_exp) { 

        this.product_exp = product_exp;

    }

    public void setDaysLeft(long days_left) {

        this.days_left = days_left;
    }

    public void setDateAdded(Date date_added) { 

        this.date_added = date_added;
    }

    //accessors

    public Date getDateQuantityModified() { 

        return date_quantity_modified;
    }

    public Date getDateRemoved() {

        return date_removed;

    }

    public String getProductName() {

        return product_name;
    }

    public String getProductQuantity() {

        return product_quantity;
    }

    public Date getDateToday() { 

        return date_today;
    }

    public Date getProductExpirationDate() {

        return product_exp;

    }

    public String getProductCategory() {

        return product_category;
    }

    public long getDaysLeft() {

        return days_left;
    }

    public Date getDateAdded() { 

        return date_added;

    }

    
}
