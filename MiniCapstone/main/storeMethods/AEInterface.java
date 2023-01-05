package main.storeMethods;

public interface AEInterface {

    public abstract void selectTask();

    public abstract void addItems();

    public abstract void editQnt();

    public abstract void viewOverAllItems(); 

    public abstract void viewItemsExpiry();

    public abstract void removeItem();

    public abstract void viewExpiredItems();

    public abstract void exitSystem();

    public abstract void postMethod();

    public abstract void selectCategory();

    public abstract void addQuantity();

    public abstract void subtractQuantity();

    public abstract long compareDates(java.sql.Date dateToday, java.sql.Date expiryDate);

    public abstract void show90DaysBelow();

    public abstract void nonExpiryItems();

    public abstract void updateTodaysDate();

    public abstract void removeExpiredProducts();

    public abstract void clearExpiredProducts();

    public abstract void logsPostMethod();

    public abstract void viewProductsDate();

    public abstract void updateDateModified();

    public abstract void updateDateRemoved();
}
