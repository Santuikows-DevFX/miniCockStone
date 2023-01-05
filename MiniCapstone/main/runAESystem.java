package main;

import main.MsgPrompts.*;
import main.database.config;
import main.products.productsInv;
import main.storeMethods.absMethods;
import java.util.Scanner;
import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;

public class runAESystem extends absMethods { //goal to make kinda reallistic by implementing loading using thread.sleep

    //global scanner
    static Scanner sc = new Scanner(System.in);

    //global polymorphism classes for prompts
    GeneralerrorPrompt generalPrompt = new GeneralerrorPrompt();
    insertPrompt insertPrompt = new insertPrompt();
    updatePrompt updatePrompt = new updatePrompt();
    idNotFoundError idNotFoundPrompt = new idNotFoundError();
    categoryNotFoundError categNotFoundPrompt = new categoryNotFoundError();
    cannotAddSameProductError cannotAddPrompt = new cannotAddSameProductError();
    invalidQuantityValueError invalidQntValuePrompt = new invalidQuantityValueError();
    removeItemCancelledPrompt removeCancelPrompt = new removeItemCancelledPrompt();
    tableIsEmpty tableEmptyPrompt = new tableIsEmpty();
    productRemovedPrompt removePrompt = new productRemovedPrompt();
    invalidDateInput dateInvalid = new invalidDateInput();
    exitPrompt exitPromptMsg = new exitPrompt();
    operationCancelledPrompt cancelAction = new operationCancelledPrompt();
    productsDeletedPrompt tableCleared = new productsDeletedPrompt();
    logsPrintedPrompt logsPrinted = new logsPrintedPrompt();
    
    //global class
    config aeDataBase;
    productsInv productsInv;
    Connection conn;

    //global variables
    boolean isValid = false;

    // int productID;
    String productID, removeID ,productName, productCategory, productExp;

    public static void main(String[] args) {

        //instance of this class
        runAESystem runSystem = new runAESystem();

        System.out.println();
        System.out.print("\t\t\t\t\t\t\t\t\t     Establishing network");
		for(int i = 0; i < 5; i++) {
			System.out.print(".");
				try {
					Thread.sleep(350);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

        System.out.println("\n");

		System.out.print("\t\t\t\t\t\t\t\t\t     Fetching data");
		for(int i = 0; i < 5; i++) {
			System.out.print(".");
				try {
					Thread.sleep(350);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
                if(i == 4){
                    System.out.println("DONE");
                }
			}

        runSystem.updateTodaysDate(); //method for automatically updating the date into current date and automatically reduces it into days left
        runSystem.removeExpiredProducts(); //function for detecting products with 0 days left and removing it from the table, inserting it into expired prd table.
        runSystem.selectTask(); // method that will start the system
        
    }

    @Override
    public void selectTask() { //method that handles the selection choice

        try {
            aeDataBase = new config();
            conn = aeDataBase.getConnection();

            PreparedStatement updateNulls =  conn.prepareStatement("UPDATE productadd_logs SET date_quantity_modifed = '', date_removed = '' WHERE date_quantity_modifed = 'null' AND date_removed = 'null'");
            updateNulls.executeUpdate();

        } catch (Exception e) {
        }

        System.out.println();

        isValid = false;

        System.out.println("=====================================================================================================================================================================================\n");
        System.out.println("\t\t\t\t\t\t\t\t\t\t  == HELLO ADMIN! ==");
        System.out.println("\t\t\t\t\t\t\t\t     == AE SYSTEM: AUTOMATED INVENTORY SYSTEM ==");
        System.out.println("\t\t\t\t\t\t\t       == Developed By: Bana-ag, Canlas, Santuico, Tablante ==\n");

        do{ //validation loop if the user inputs invalid option.

        System.out.println("\t\t\t\t\t\t\t\t             == What do you want to do? ==\n");
        System.out.println("\t\t\t\t\t\t\t\t             > Manage Products[1]\n");
        System.out.println("\t\t\t\t\t\t\t\t             > Update Product Quantity[2]\n");
        System.out.println("\t\t\t\t\t\t\t\t             > View Overall Products[3]\n");
        System.out.println("\t\t\t\t\t\t\t\t             > View Product Expiry Only[4]\n");
        System.out.println("\t\t\t\t\t\t\t\t             > Date Added Logs[5]\n");
        System.out.println("\t\t\t\t\t\t\t\t             > Exit System[6]\n\n");

        System.out.print("\t\t\t\t\t\t\t\t        Enter based on corresponding number: ");
        String selectionChoice = sc.nextLine(); //handles the storage for the admin choice

        if(selectionChoice.equals("1")) { //conditional statement for the admin choice

            isValid = true;
            
            do{

                System.out.println();
                System.out.println("\t\t\t\t\t\t\t\t             == What do you want to do? ==\n");
                System.out.println("\t\t\t\t\t\t\t\t                 > Add Product[1]\n");
                System.out.println("\t\t\t\t\t\t\t\t                 > Remove Product[2]\n");
                System.out.println("\t\t\t\t\t\t\t\t                 > Back[3]\n");
                System.out.print("\t\t\t\t\t\t\t\t        Enter based on corresponding number: ");
                String manageChoice = sc.nextLine();

                if(manageChoice.equals("1")) {

                    isValid = true;
                    addItems();

                }else if(manageChoice.equals("2")) { 

                    isValid = true;
                    
                    try {

                        aeDataBase = new config();
                        conn = aeDataBase.getConnection();
                        
                        PreparedStatement checkTableQuery = conn.prepareStatement("SELECT * FROM products");
                        ResultSet checkResult = checkTableQuery.executeQuery();

                        if(!checkResult.next()) { 

                            System.out.println();
                            System.out.println(tableEmptyPrompt.messagePrompt());
                            isValid = false;

                        }else { 

                            removeItem();
                        }


                    } catch (Exception e) {
                        
                    }
                }else if(manageChoice.equals("3")){

                    isValid = true;
                    selectTask();

                    
                }else { 

                    isValid = false;
                    System.out.println();
                    System.out.println(generalPrompt.messagePrompt());
                }

            }while(isValid == false);

        }else if(selectionChoice.equals("2")) { 

            isValid = true;

                try {

                    aeDataBase = new config();
                    conn = aeDataBase.getConnection();
                    
                    PreparedStatement checkTableQuery = conn.prepareStatement("SELECT * FROM products");
                    ResultSet checkResult = checkTableQuery.executeQuery();

                    if(!checkResult.next()) { 

                        System.out.println();
                        System.out.println(tableEmptyPrompt.messagePrompt());
                        isValid = false;

                    }else { 

                        editQnt();
                    }


                } catch (Exception e) {
                    
            }
    
        }else if(selectionChoice.equals("3")) { 

            isValid = true;
            viewOverAllItems(); //view the table with this function

        }else if(selectionChoice.equals("4")) { 

            isValid = true;

            try {

                aeDataBase = new config();
                conn = aeDataBase.getConnection();
                
                PreparedStatement checkTableQuery = conn.prepareStatement("SELECT * FROM products");
                ResultSet checkResult = checkTableQuery.executeQuery();

                if(!checkResult.next()) { 

                    System.out.println();
                    System.out.println(tableEmptyPrompt.messagePrompt());
                    isValid = false;

                }else { 

                    viewItemsExpiry();
                }


            } catch (Exception e) {
                
            }
            
        }else if(selectionChoice.equals("5")) {

            isValid = true;
            viewProductsDate();

        }else if(selectionChoice.equals("6")){

            isValid = true;
            exitSystem();


        }else { 

            isValid = false;
            System.out.println();
            System.out.println(generalPrompt.messagePrompt()); //for validation purposes

        }

        }while(isValid == false);
    }

    @Override
    public void addItems() { //adding items function

        System.out.println();
                System.out.println("=====================================================================================================================================================================================\n");
        System.out.println("\t\t\t\t\t\t\t\t                  == ADD PRODUCTS ==");

        try {

            aeDataBase = new config();
            productsInv = new productsInv(); //instance of the class of the class that is being encapsulated.

        
        do{ //loop for user validation if the user inputs invalid data.
            try {
                conn = aeDataBase.getConnection(); //establishing a connection to our data base

                System.out.println();
                System.out.println("\t\t\t\t\t\t\t\t\t            PRODUCT INFO: \n");
                System.out.println("\t\t\t\t\t\t\t\t\t           TYPE \"0\" TO CANCEL\n");

                System.out.println("\t\t\t\t\t\t\t\t      Enter Product Category (Ex. Frozen, Canned, PDR CONDI)");
                System.out.print("\t\t\t\t\t\t\t\t      > ");
                productCategory = sc.nextLine().toUpperCase();

                if(productCategory.equals("0")) { 

                    System.out.println();
                    System.out.println(cancelAction.messagePrompt());
                    selectTask();

                }

                productsInv.setProductCategory(productCategory);

                PreparedStatement checkIfNonExpiry = conn.prepareStatement("SELECT * FROM non_exp_category WHERE no_expCategs = ('" + productCategory + "')");
                ResultSet checkResult = checkIfNonExpiry.executeQuery();

                if(checkResult.isBeforeFirst()) { 

                    nonExpiryItems();

                }else { 

                    isValid = true;
                System.out.println();

                System.out.println("\t\t\t\t\t\t\t\t      Enter Product Name (Ex. Tender Juicy Hotdog)");
                System.out.print("\t\t\t\t\t\t\t\t      > ");
                productName = sc.nextLine().toUpperCase();

                if(productName.equals("0")) { 

                    System.out.println();
                    System.out.println(cancelAction.messagePrompt());
                    selectTask();
                }
        
                productsInv.setProductName(productName); //calling setter method to set product name
        
                System.out.println();

                String productQnt = "";

            do{

                try{
                            
                System.out.println("\t\t\t\t\t\t\t\t      Enter Product Quantity (Ex. 20)");
                System.out.print("\t\t\t\t\t\t\t\t      > ");
                productQnt = sc.nextLine();

                if(productQnt.equals("0")) { 

                    System.out.println();
                    System.out.println(cancelAction.messagePrompt());
                    selectTask();

                }

                for(int i = 0; i < productQnt.length(); i++) { 

                    if((int) productQnt.charAt(i) >= 33 && (int) productQnt.charAt(i) <= 47 || (int) productQnt.charAt(i) >= 58 && (int) productQnt.charAt(i) <= 126) { 

                        isValid = false;
                        System.out.println();
                        throw new userDefinedException();
                        
                    }else { 

                        isValid = true;
                        
                    }

                }

            }catch(userDefinedException e) { 

                System.out.println(e.getMessage());
            }

        }while(isValid == false);
        
                productsInv.setProductQuantity(productQnt); //calling setter method to set product quantity
        
                System.out.println();
                    
                try{

                    System.out.println("\t\t\t\t\t\t\t\t      Enter Product Expiration Date in this format (YYYY-MM-DD)");
                    System.out.print("\t\t\t\t\t\t\t\t      > ");
                    productExp = sc.nextLine();
                    System.out.println(); 

                    if(productExp.equals("0")) { 

                        System.out.println();
                        System.out.println(cancelAction.messagePrompt());
                        selectTask();
                    }

                    Date prodExp = Date.valueOf(productExp); //converting the string value into a util.Date

                    productsInv.setProductExpiration(prodExp); //calling setter method to set product expiration

                    }catch(Exception e){
                        System.out.println();
                        System.out.println(dateInvalid.messagePrompt());

                    }
        
                long millis = System.currentTimeMillis(); //getting the time and storing it into a long variable
                Date dateToday = new Date(millis); //using java.sql.date we can get the date today, this will serve as the date where a product has been inserted.
                productsInv.setDateToday(dateToday); //calling the setter method to set the date today. 

                long days = compareDates(productsInv.getDateToday(), productsInv.getProductExpirationDate()) + 1; //method for getting the days by comparing the two date
                    
                productsInv.setDaysLeft(days);

                long dateAddedMillis = System.currentTimeMillis();
                Date dateAdded = new Date(dateAddedMillis);

                productsInv.setDateAdded(dateAdded);
        
                postMethod(); //calling the post function

                }

            } catch (Exception e) {
                
                System.out.println();
                isValid = false;
                System.out.println(generalPrompt.messagePrompt()); //validation purposes
                // e.printStackTrace();
            }

        }while(isValid == false);
            
        }catch (Exception e) {
        }
        
    }

    @Override
    public void editQnt() { //function for editing quantity

        aeDataBase = new config();
        conn = aeDataBase.getConnection(); //getting the connection from the data base

        try {
            PreparedStatement selectQuery = conn.prepareStatement("SELECT * FROM products"); //selecting from the table

		    ResultSet fetchProducts = selectQuery.executeQuery(); //fetching the products
            System.out.println();
            System.out.println("=====================================================================================================================================================================================\n");
            System.out.println("\t\t\t\t\t\t\t\t\t              PRODUCTS TABLE");
            System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("\t\t\t\t\t   %8s%18s%18s%18s%17s%15s%15s", " ID" ,"      PRODUCT NAME" , "   QUANTITY", " DATE TODAY", "EXP DATE", "CATEGORY", "DAYS LEFT");
            System.out.println();
            System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");

            while(fetchProducts.next()) { //while it has a row to be fetch, display the row from the data base

                System.out.printf("\t\t\t\t\t   %8s%15s%18s%21s%18s%13s%13s" ,fetchProducts.getInt("product_id") ,fetchProducts.getString("product_name"), fetchProducts.getInt("product_quantity"), fetchProducts.getDate("date_today"), fetchProducts.getString("product_exp"), fetchProducts.getString("category"), fetchProducts.getInt("days_left"));
                System.out.println();
              
                System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        try {
        
        do{ //loop for user validation if the user inputs invalid data.
            try {

                System.out.println();
                System.out.println("\t\t\t\t\t\t\t\t                    EDIT QUANTITY: \n");

                do{

                    try {
                        
                        System.out.println("\t\t\t\t\t\t\t\t\t           TYPE \"0\" TO CANCEL\n");
                        System.out.print("\t\t\t\t\t\t\t\t           Enter the ID of the product: ");
                        productID = sc.nextLine();

                        if(productID.equals("0")) { 

                            System.out.println();
                            System.out.println(cancelAction.messagePrompt());
                            selectTask(); 
        
                        }

                        for(int i = 0; i < productID.length(); i++) {

                            if((int) productID.charAt(i) >= 33 && (int) productID.charAt(i) <= 47 || (int) productID.charAt(i) >= 58 && (int) productID.charAt(i) <= 126) { 

                                isValid = false;
                                System.out.println();
                                throw new userDefinedException();
                                
                            }else { 
        
                                isValid = true;
                                
                            }

                        }

                    } catch (userDefinedException e) {

                        System.out.println(e.getMessage());
                    }

                }while(isValid == false);

                System.out.println();
        
                PreparedStatement selectProduct = conn.prepareStatement("SELECT * FROM products WHERE product_id = '"+ productID +"' "); //selecting from the data base where the ID is equal to the admins id input, in order to display specific ID.
                ResultSet fetchID = selectProduct.executeQuery();

                    if(fetchID.isBeforeFirst()) {

                        System.out.println("\t\t\t\t\t\t\t\t\t         PRODUCTS TABLE");
                        System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
                        System.out.printf("\t\t\t\t\t   %8s%18s%18s%18s%17s%15s%15s", " ID" ,"      PRODUCT NAME" , "   QUANTITY", " DATE TODAY", "EXP DATE", "CATEGORY", "DAYS LEFT");
                        System.out.println();
                        System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
            
            
                        while(fetchID.next()) { //while it has a row to be fetch, display the row from the data base
            
                            System.out.printf("\t\t\t\t\t   %8s%15s%18s%21s%18s%13s%13s" ,fetchID.getInt("product_id") ,fetchID.getString("product_name"), fetchID.getInt("product_quantity"), fetchID.getDate("date_today"), fetchID.getString("product_exp"), fetchID.getString("category"), fetchID.getInt("days_left"));
                            System.out.println();
                          
                            System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
                        }
            
                    do{ //prompting the admin to choose between these options

                        System.out.println();
                        System.out.println("\t\t\t\t\t\t\t\t              == What do you want to do? ==\n");
                        System.out.println("\t\t\t\t\t\t\t\t                   > Add Quantity[1]\n");
                        System.out.println("\t\t\t\t\t\t\t\t                   > Deduct Quantity[2]\n");
                        System.out.println("\t\t\t\t\t\t\t\t                   > Back[3]\n");
                
                        System.out.print("\t\t\t\t\t\t\t\t        Enter based on corresponding number: ");
                        String choice = sc.nextLine();
            
                        if(choice.equals("1")) { 
                
                            isValid = true;
                            addQuantity();
                           
                        }else if(choice.equals("2")) { 
                
                            isValid = true;
                            subtractQuantity();
                            
                        }else if(choice.equals("3")) {
            
                            isValid = true;
                            selectTask();
                            System.out.println();

                        }else { 
                
                            isValid = false;
                            System.out.println();
                            System.out.println(generalPrompt.messagePrompt());
                
                        }
                
                        }while(isValid == false);

                    }else {

                        isValid = false;
                        System.out.println(idNotFoundPrompt.messagePrompt());
                        System.out.println();
                    }                

            } catch (Exception e) {
                
                System.out.println();
                isValid = false;
                System.out.println(generalPrompt.messagePrompt());
            }

        }while(isValid == false);
            
        }catch (Exception e) {
        }
    }

    @Override
    public void viewOverAllItems() { //function for viewing the entire table

        System.out.println();
        
        aeDataBase = new config();
        conn = aeDataBase.getConnection();

		try {
            PreparedStatement selectQuery = conn.prepareStatement("SELECT * FROM products"); //selecting from the entire table

		    ResultSet fetchProducts = selectQuery.executeQuery(); //fetching the products
            System.out.println();
            System.out.println("=====================================================================================================================================================================================\n");
            System.out.println("\t\t\t\t\t\t\t\t\t         VIEW PRODUCTS TABLE");
            System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("\t\t\t\t\t   %8s%18s%18s%18s%17s%15s%15s", " ID" ,"      PRODUCT NAME" , "   QUANTITY", " DATE TODAY", "EXP DATE", "CATEGORY", "DAYS LEFT");
            System.out.println();
            System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");


            while(fetchProducts.next()) { //while it has a row to be fetch, display the row from the data base

                System.out.printf("\t\t\t\t\t   %8s%15s%18s%21s%18s%13s%13s" ,fetchProducts.getInt("product_id") ,fetchProducts.getString("product_name"), fetchProducts.getInt("product_quantity"), fetchProducts.getDate("date_today"), fetchProducts.getString("product_exp"), fetchProducts.getString("category"), fetchProducts.getInt("days_left"));
                System.out.println();
              
                System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }

        do{

            System.out.println();
            System.out.println("\t\t\t\t\t\t\t\t              == What do you want to do? ==\n");
            System.out.println("\t\t\t\t\t\t\t\t                  > Select Category[1]\n");
            System.out.println("\t\t\t\t\t\t\t\t                  > Back[2]\n");
    
            System.out.print("\t\t\t\t\t\t\t\t           Enter based on corresponding number: ");
            String choice = sc.nextLine();

            if(choice.equals("1")) { 
    
                isValid = true;
                selectCategory();
               
    
            }else if(choice.equals("2")) {

                isValid = true;
                selectTask();
                System.out.println();

            }else { 
    
                isValid = false;
                System.out.println();
                System.out.println(generalPrompt.messagePrompt());
    
            }
    
            }while(isValid == false);

    }

    @Override
    public void viewItemsExpiry() { // function for viewing the items expiry 

        aeDataBase = new config();
        conn = aeDataBase.getConnection();

        try {
            
            PreparedStatement selectProducts = conn.prepareStatement("SELECT * FROM products ORDER BY days_left ASC");
            ResultSet fetchProducts = selectProducts.executeQuery();

            System.out.println();
            System.out.println();
            System.out.println("=====================================================================================================================================================================================\n");
            System.out.println("\t\t\t\t\t\t\t\t\t         VIEW PRODUCTS TABLE (EXPIRY)");
            System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("\t\t\t\t\t   %8s%18s%18s%18s%17s%15s%15s", " ID" ,"      PRODUCT NAME" , "   QUANTITY", " DATE TODAY", "EXP DATE", "CATEGORY", "DAYS LEFT");
            System.out.println();
            System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");

            while(fetchProducts.next()) { //while it has a row to be fetch, display the row from the data base

                System.out.printf("\t\t\t\t\t   %8s%15s%18s%21s%18s%13s%13s" ,fetchProducts.getInt("product_id") ,fetchProducts.getString("product_name"), fetchProducts.getInt("product_quantity"), fetchProducts.getDate("date_today"), fetchProducts.getString("product_exp"), fetchProducts.getString("category"), fetchProducts.getInt("days_left"));
                System.out.println();
              
                System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
            }
            do{

                System.out.println();
                System.out.println("\t\t\t\t\t\t\t\t                == What do you want to do? ==\n");
                System.out.println("\t\t\t\t\t\t\t\t         > Show near expiring products (< 90 days)[1]\n");
                System.out.println("\t\t\t\t\t\t\t\t         > Show expired products[2]\n");
                System.out.println("\t\t\t\t\t\t\t\t         > Back[3]\n");
    
                System.out.print("\t\t\t\t\t\t\t\t        Enter based on corresponding number: ");
                String adminChoice = sc.nextLine();
        
                if(adminChoice.equals("1")) { 
        
                    isValid = true;
                    show90DaysBelow();
        
                }else if(adminChoice.equals("2")) {

                    isValid = true;
                    viewExpiredItems();

                }else if(adminChoice.equals("3")) { 

                    isValid = true;
                    selectTask();
        
                }else { 
        
                    isValid = false;
                    System.out.println();
                    System.out.println(generalPrompt.messagePrompt());
        
                }
        
                }while(isValid == false);

        } catch (SQLException e) {

            e.printStackTrace();
        }
        
    }

    @Override
    public void exitSystem() { //function for exiting the system and ending the program
        
        System.out.println();
        System.out.print("\t\t\t\t\t\t\t\t\t     Turning off please wait");
		for(int i = 0; i < 6; i++) {
			System.out.print(".");
				try {
					Thread.sleep(350);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
        System.out.println("\n");
        System.out.println(exitPromptMsg.messagePrompt());

        System.exit(0);
        
    }

    @Override
    public void postMethod() { //equivalent of $_POST in PHP, to post the data into the table.

        try {
            aeDataBase = new config();
            conn = aeDataBase.getConnection(); //connecting to db
            
            PreparedStatement checkNames = conn.prepareStatement("SELECT * FROM products WHERE product_name = ('"+ productsInv.getProductName() + "')");
            ResultSet check = checkNames.executeQuery();

            if(check.isBeforeFirst()) {

                isValid = false;
                System.out.println();
                System.out.println(cannotAddPrompt.messagePrompt());
                selectTask();

            }else { 

			PreparedStatement insertQuery = conn.prepareStatement( //making a query for inserting a product into the table

            "INSERT INTO products (product_name, product_quantity, product_exp, date_today, category, days_left) VALUES ('" + productsInv.getProductName()  + "', '" + productsInv.getProductQuantity() + "', '"+ productsInv.getProductExpirationDate() + "', '"+ productsInv.getDateToday() +"', '"+ productsInv.getProductCategory() + "', '" + productsInv.getDaysLeft() + "')"); //inserting products into db

            insertQuery.executeUpdate(); //update the date base

            System.out.println(insertPrompt.messagePrompt()); //updating the user that his/herp product is added into the db

            String standardDate = "";

            PreparedStatement updateSelectedNullRow = conn.prepareStatement("UPDATE products SET product_exp = ('" + standardDate +"') WHERE product_exp = 'null'");

            updateSelectedNullRow.executeUpdate();
            logsPostMethod();

            }

		} catch (Exception e) {
		}
        
        do{

		System.out.println("\t\t\t\t\t\t\t\t                  == What do you want to do? ==\n");
        System.out.println("\t\t\t\t\t\t\t\t                       > Insert More[1]\n");
        System.out.println("\t\t\t\t\t\t\t\t                       > Back[2]\n");

        System.out.print("\t\t\t\t\t\t\t\t        Enter based on corresponding number: ");
        String choice = sc.nextLine();

        if(choice.equals("1")) { 

            isValid = true;
            addItems();

        }else if(choice.equals("2")) { 

            isValid = true;
            selectTask();

        }else { 

            isValid = false;
            System.out.println();
            System.out.println(generalPrompt.messagePrompt());

        }

        }while(isValid == false);
        
    }

    @Override
    public void logsPostMethod() { // posting the same added product into the logs for date added table this will be used for logs purposes

        try {
    
            aeDataBase = new config();
            conn = aeDataBase.getConnection();

            PreparedStatement selectPrdQuery = conn.prepareStatement("SELECT * FROM products");
            ResultSet fetchPrdID = selectPrdQuery.executeQuery();

            PreparedStatement insertLogsQuery = null;

            while(fetchPrdID.next()){
                                                        
                insertLogsQuery = conn.prepareStatement("INSERT INTO productadd_logs (product_id, product_name, date_added, date_quantity_modifed, date_removed) VALUES ('"+ fetchPrdID.getInt("product_id") + "', '" + productsInv.getProductName() + "', '" + productsInv.getDateAdded() + "', '"+ productsInv.getDateQuantityModified() + "', '"+ productsInv.getDateRemoved() + "')");

            }

            insertLogsQuery.executeUpdate();

        } catch (Exception e) { 

            System.out.println(e);
        }
        
    }

    @Override
    public void selectCategory() {

        System.out.println();
        System.out.println("\t\t\t\t\t\t\t\t\t           TYPE \"0\" TO CANCEL\n");
        
        System.out.print("\t\t\t\t\t\t\t\t        Enter the category: ");
        String getCategory = sc.nextLine().toUpperCase();

        if(getCategory.equals("0")) { 

            System.out.println();
            System.out.println(cancelAction.messagePrompt());
            viewOverAllItems(); 
        }

        try {

            PreparedStatement selectCategoryQuery = conn.prepareStatement("SELECT * FROM products WHERE category = ('"+ getCategory +"')"); //selecting from the table

            ResultSet fetchCategory = selectCategoryQuery.executeQuery(); //fetching the products
            System.out.println();

            if(fetchCategory.isBeforeFirst()) { 
                System.out.println("=====================================================================================================================================================================================\n");
                System.out.println("\t\t\t\t\t\t\t\t\t         PRODUCTS TABLE");
                System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
                System.out.printf("\t\t\t\t\t   %8s%18s%18s%18s%17s%15s%15s", " ID" ,"      PRODUCT NAME" , "   QUANTITY", " DATE TODAY", "EXP DATE", "CATEGORY", "DAYS LEFT");
                System.out.println();
                System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
    
                while(fetchCategory.next()) { //while it has a row to be fetch, display the row from the data base
    
                    System.out.printf("\t\t\t\t\t   %8s%15s%18s%21s%18s%13s%13s" ,fetchCategory.getInt("product_id") ,fetchCategory.getString("product_name"), fetchCategory.getInt("product_quantity"), fetchCategory.getDate("date_today"), fetchCategory.getString("product_exp"), fetchCategory.getString("category"), fetchCategory.getInt("days_left"));
                    System.out.println();
                  
                    System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
                }

                do{

                    System.out.println();
                    System.out.println("\t\t\t\t\t\t\t\t                     What to do now?\n");
                    System.out.println("\t\t\t\t\t\t\t\t                > Search Another Category[1]\n");
                    System.out.println("\t\t\t\t\t\t\t\t                > View Table[2]\n");
                    System.out.println("\t\t\t\t\t\t\t\t                > Exit[3]\n");
            
                    System.out.print("\t\t\t\t\t\t\t\t           Enter based on corresponding number: ");
                    String adminChoice = sc.nextLine();
            
                    if(adminChoice.equals("1")) { 
            
                        isValid = true;
                        selectCategory();
            
                    }else if(adminChoice.equals("2")) { 
    
                        isValid = true;
                        viewOverAllItems();
            
                    }else if(adminChoice.equals("3")){
    
                        isValid = true;
                        selectTask();
                        System.out.println();
    
                    }else { 
            
                        isValid = false;
                        System.out.println();
                        System.out.println(generalPrompt.messagePrompt());
            
                    }
            
                    }while(isValid == false);
    
            }else {

                isValid = false;
                System.out.println(categNotFoundPrompt.messagePrompt());

            }

            
        } catch (SQLException e) {

            e.printStackTrace();
        }
        
    }

    @Override
    public void addQuantity() {

        aeDataBase = new config();
        conn = aeDataBase.getConnection();

        do{

        System.out.println();
        System.out.println("\t\t\t\t\t\t\t\t\t           TYPE \"0\" TO CANCEL\n");

        System.out.print("\t\t\t\t\t\t\t\t                Quantity to add: ");
        int updateAddQnt = sc.nextInt();

        sc.nextLine();

        if(updateAddQnt == 0) { 

            System.out.println();
            System.out.println(cancelAction.messagePrompt());
            editQnt(); 
        }

        try {
            
            Statement state = conn.createStatement();
            String selectQuery = "SELECT product_quantity FROM products WHERE product_id = '"+ productID +"'";

            ResultSet queryRes = state.executeQuery(selectQuery);
            queryRes.next();

            int prevQuantity = queryRes.getInt("product_quantity");
            int updatedQuantity = prevQuantity + updateAddQnt;
            
            String updateQuery = "UPDATE products SET product_quantity = '"+ updatedQuantity +"' WHERE product_id = '"+ productID +"'";
            state.executeUpdate(updateQuery);

            System.out.println();
            System.out.print(updatePrompt.messagePrompt());

            long dateModifiedMillis = System.currentTimeMillis();
            Date dateModified = new Date(dateModifiedMillis);
            productsInv.setDateQuantityModified(dateModified);

            updateDateModified();

            do{

                System.out.println();
                System.out.println("\t\t\t\t\t\t\t\t      Do you still want to manage product quantity?\n");
                System.out.println("\t\t\t\t\t\t\t\t                > Yes[1]\n");
                System.out.println("\t\t\t\t\t\t\t\t                > No[2]\n");
                System.out.print("\t\t\t\t\t\t\t\t       Enter based on corresponding number:  ");
                String choice = sc.nextLine();

                if(choice.equals("1")) {

                    isValid = true;
                    editQnt();

                }else if(choice.equals("2")) {

                    isValid = true;
                    selectTask();
                    System.out.println();
                }else { 

                    isValid = false;
                    System.out.println();
                    System.out.println(generalPrompt.messagePrompt());

                }

            }while(isValid == false);


        } catch (SQLException e) {
            
            e.printStackTrace();
        } 

    }while(isValid == false);
        
    }

    @Override
    public void subtractQuantity() {
        
        aeDataBase = new config();
        conn = aeDataBase.getConnection();

        do{

        System.out.println();
        System.out.println("\t\t\t\t\t\t\t\t\t           TYPE \"0\" TO CANCEL\n");

        System.out.print("\t\t\t\t\t\t\t\t                Quantity to deduct: ");
        int updateSubQnt = sc.nextInt();

        sc.nextLine();

        if(updateSubQnt == 0) { 

            System.out.println();
            System.out.println(cancelAction.messagePrompt());
            editQnt(); 
        }

        try {
            
            Statement state = conn.createStatement();
            String selectQuery = "SELECT product_quantity FROM products WHERE product_id = '"+ productID +"'";
            ResultSet queryRes = state.executeQuery(selectQuery);
            queryRes.next();

            if(updateSubQnt < 0) {

                isValid = false;
                System.out.println();
                System.out.println(generalPrompt.messagePrompt());

            }else if(queryRes.getInt("product_quantity") < updateSubQnt) { 

                isValid  = false;
                System.out.println();
                System.out.println(invalidQntValuePrompt.messagePrompt());


            }else {

            int prevQuantity = queryRes.getInt("product_quantity");
            int updatedQuantity = prevQuantity - updateSubQnt;
            
            String updateQuery = "UPDATE products SET product_quantity = '"+ updatedQuantity +"' WHERE product_id = '"+ productID +"'";
            state.executeUpdate(updateQuery);

            System.out.println();
            System.out.print(updatePrompt.messagePrompt());

            long dateModifiedMillis = System.currentTimeMillis();
            Date dateModified = new Date(dateModifiedMillis);
            productsInv.setDateQuantityModified(dateModified);

            updateDateModified();

            do{

                System.out.println();
                System.out.println("\t\t\t\t\t\t\t\t      Do you still want to manage product quantity?\n");
                System.out.println("\t\t\t\t\t\t\t\t                > Yes[1]\n");
                System.out.println("\t\t\t\t\t\t\t\t                > No[2]\n");
                System.out.print("\t\t\t\t\t\t\t\t       Enter based on corresponding number:  ");
                String choice = sc.nextLine();

                if(choice.equals("1")) {

                    isValid = true;
                    editQnt();

                }else if(choice.equals("2")) {

                    isValid = true;
                    selectTask();
                    System.out.println();
                }else { 

                    isValid = false;
                    System.out.println();
                    System.out.println(generalPrompt.messagePrompt());

                }

            }while(isValid == false);

            }

        } catch (SQLException e) {
            
            e.printStackTrace();
        } 

        }while(isValid == false);
        
    }

    @Override
    public long compareDates(java.sql.Date dateToday, java.sql.Date expiryDate) { // function for getting the days left, by comparing the date today and set expiry date of the product

        long daysDifference = (dateToday.getTime() - expiryDate.getTime()) / 86400000; //86400000 milliseconds is equivalent to 1 day
		return Math.abs(daysDifference); //math.abs will return positive numbers so that no negative will return
    }

    @Override 
    public void show90DaysBelow() { //function for showing all the 'expiry products' with below 90 days left

        aeDataBase = new config();
        conn = aeDataBase.getConnection();

        try {

           final int specificExpiryDate = 90; //3 months
           final int maximumReadedExpiry = 1; //this is used to filter out the 0 days

            PreparedStatement selectProducts = conn.prepareStatement("SELECT * FROM products WHERE days_left <= ('" + specificExpiryDate +"') AND days_left >= ('" + maximumReadedExpiry +"') ORDER BY days_left ASC");
            ResultSet fetchProducts = selectProducts.executeQuery();

            System.out.println();
            System.out.println("=====================================================================================================================================================================================\n");
            System.out.println("\t\t\t\t\t\t\t\t\t      NEAR EXPIRY PRODUCTS TABLE");
            System.out.println("\t\t\t\t      -----------------------------------------------------------------------------------------------------------");
            System.out.printf("\t\t\t\t\t           %20s%15s%15s%15s", " ID" ,"      PRODUCT NAME" , "EXP DATE", "DAYS LEFT");
            System.out.println();
            System.out.println("\t\t\t\t      -----------------------------------------------------------------------------------------------------------");

            while(fetchProducts.next()) { //while it has a row to be fetch

                System.out.printf("\t\t\t\t\t           %20s%16s%17s%11s" ,fetchProducts.getInt("product_id") ,fetchProducts.getString("product_name"), fetchProducts.getString("product_exp"), fetchProducts.getInt("days_left"));
                System.out.println();
            
                System.out.println("\t\t\t\t      -----------------------------------------------------------------------------------------------------------");
            }

            do{

                System.out.println();
                System.out.println("\t\t\t\t\t\t\t\t                  What to do now?\n");
                System.out.println("\t\t\t\t\t\t\t\t               > Back to prev. table[1]\n");
                System.out.println("\t\t\t\t\t\t\t\t               > Back to home[2]\n");
        
                System.out.print("\t\t\t\t\t\t\t\t        Enter based on corresponding number: ");
                String adminChoice = sc.nextLine();
        
                if(adminChoice.equals("1")) { 
        
                    isValid = true;
                    viewItemsExpiry();
        
                }else if(adminChoice.equals("2")) { 

                    isValid = true;
                    selectTask();
        
                }else { 
        
                    isValid = false;
                    System.out.println();
                    System.out.println(generalPrompt.messagePrompt());
        
                }
        
                }while(isValid == false);

        } catch (Exception e) {
            
        }
    }

    @Override
    public void removeItem() {

        aeDataBase = new config();
        conn = aeDataBase.getConnection();

        try {

            System.out.println();
                System.out.println("=====================================================================================================================================================================================\n");
        System.out.println("\t\t\t\t\t\t\t\t               == REMOVE PRODUCTS ==");

            PreparedStatement selectQuery = conn.prepareStatement("SELECT * FROM products"); //selecting from the entire table

		    ResultSet fetchProducts = selectQuery.executeQuery(); //fetching the products
            System.out.println();

            System.out.println("\t\t\t\t\t\t\t\t\t           PRODUCTS TABLE");
            System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("\t\t\t\t\t   %8s%18s%18s%18s%17s%15s%15s", " ID" ,"      PRODUCT NAME" , "   QUANTITY", " DATE TODAY", "EXP DATE", "CATEGORY", "DAYS LEFT");
            System.out.println();
            System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");

            while(fetchProducts.next()) { //while it has a row to be fetch, display the row from the data base

                System.out.printf("\t\t\t\t\t   %8s%15s%18s%21s%18s%13s%13s" ,fetchProducts.getInt("product_id") ,fetchProducts.getString("product_name"), fetchProducts.getInt("product_quantity"), fetchProducts.getDate("date_today"), fetchProducts.getString("product_exp"), fetchProducts.getString("category"), fetchProducts.getInt("days_left"));
                System.out.println();
              
                System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
            }

            System.out.println();
            System.out.println("\t\t\t\t\t\t\t\t\t           TYPE \"0\" TO CANCEL\n");


            do {
                
                System.out.print("\t\t\t\t\t\t\t\t   Enter the ID of the product you wish to remove: ");
                removeID = sc.nextLine(); 

                if(removeID.equals("0")) { 

                    System.out.println();
                    System.out.println(cancelAction.messagePrompt());
                    selectTask(); 
                }
    
                System.out.println(); 

                PreparedStatement selectRemoveQuery = conn.prepareStatement("SELECT * FROM products WHERE product_id = ('"+ removeID +"')");
                ResultSet removeProductResult = selectRemoveQuery.executeQuery();

                PreparedStatement checkTable = conn.prepareStatement("SELECT * FROM products");
                ResultSet checkResult = checkTable.executeQuery();

                if(!checkResult.next()){

                    System.out.println(tableEmptyPrompt.messagePrompt());
                    selectTask();

                }else { 

                    if(removeProductResult.isBeforeFirst()) { 
                        System.out.println("=====================================================================================================================================================================================\n");
                        System.out.println("\t\t\t\t\t\t\t\t\t         REMOVE PRODUCTS TABLE");
                        System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
                        System.out.printf("\t\t\t\t\t\t\t\t\t      %8s%18s", "ID" ,"     PRODUCT NAME");
                        System.out.println();
                        System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
            
                        while(removeProductResult.next()) { //while it has a row to be fetch, display the row from the data base
            
                            System.out.printf("\t\t\t\t\t\t\t\t\t      %8s%15s" ,removeProductResult.getInt("product_id") ,removeProductResult.getString("product_name"));
                            System.out.println();
                        
                            System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------\n");
        
                            System.out.println("\t\t\t\t\t\t\t\t       Are you sure you want to remove " + removeProductResult.getString("product_name") + "?");
        
                        }              
                            System.out.println();
                            System.out.println("\t\t\t\t\t\t\t\t             > Yes[1]\n");
                            System.out.println("\t\t\t\t\t\t\t\t             > No[2]\n"); 
                    
                            do{
                                System.out.print("\t\t\t\t\t\t\t\t        Enter based on corresponding number: ");
                                String adminChoice = sc.nextLine();
            
                                if(adminChoice.equals("1")) { 
            
                                    isValid = true;
                                    PreparedStatement removeQuery = conn.prepareStatement("DELETE FROM products WHERE product_id = ('" + removeID +"')");
                                    removeQuery.executeUpdate();

                                    System.out.println();
                                    System.out.println(removePrompt.messagePrompt());

                                    long removedDateMillis = System.currentTimeMillis();
                                    Date dateRemoved = new Date(removedDateMillis);

                                    productsInv.setDateRemoved(dateRemoved);

                                    updateDateRemoved();
            
                                }else if(adminChoice.equals("2")) { 
            
                                    isValid = true;
                                    System.out.println();
                                    System.out.println(removeCancelPrompt.messagePrompt());
                                    removeItem();
            
                                }else { 
            
                                    isValid = false;
                                    System.out.println(generalPrompt.messagePrompt());
            
                                }
                            }while(isValid == false);
        
                    }else {
        
                        isValid = false;
                        System.out.println(idNotFoundPrompt.messagePrompt());
        
                    }

                }

            }while(isValid == false);

            do{

                System.out.println();
                System.out.println("\t\t\t\t\t\t\t\t           == What do you want to do? ==\n");
                System.out.println("\t\t\t\t\t\t\t\t             > View updated table[1]\n");
                System.out.println("\t\t\t\t\t\t\t\t             > Remove another product[2]\n");
                System.out.println("\t\t\t\t\t\t\t\t             > Exit[3]\n");

                System.out.print("\t\t\t\t\t\t\t\t        Enter based on corresponding number: ");
                String adminChoice = sc.nextLine();
        
                if(adminChoice.equals("1")) { 
        
                    isValid = true;
                    
                    try {

                        PreparedStatement selectTableQuery = conn.prepareStatement("SELECT * FROM products"); //selecting from the entire table

                        ResultSet fetchTableProducts = selectTableQuery.executeQuery(); //fetching the products
                        System.out.println();

                        System.out.println("\t\t\t\t\t\t\t\t\t         UPDATED PRODUCTS TABLE");
                        System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
                        System.out.printf("\t\t\t\t\t   %8s%18s%18s%18s%17s%15s%15s", " ID" ,"      PRODUCT NAME" , "   QUANTITY", " DATE TODAY", "EXP DATE", "CATEGORY", "DAYS LEFT");
                        System.out.println();
                        System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");

                        while(fetchTableProducts.next()) { //while it has a row to be fetch, display the row from the data base

                            System.out.printf("\t\t\t\t\t   %8s%15s%18s%21s%18s%13s%13s" ,fetchTableProducts.getInt("product_id") ,fetchTableProducts.getString("product_name"), fetchTableProducts.getInt("product_quantity"), fetchTableProducts.getDate("date_today"), fetchTableProducts.getString("product_exp"), fetchTableProducts.getString("category"), fetchTableProducts.getInt("days_left"));
                            System.out.println();
                        
                            System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
                        }

                        do{
                            System.out.println();
                            System.out.println("\t\t\t\t\t\t\t\t           == What do you want to do? ==\n");
                            System.out.println("\t\t\t\t\t\t\t\t             > Remove another product[1]\n");
                            System.out.println("\t\t\t\t\t\t\t\t             > Back[2]\n");

                            System.out.print("\t\t\t\t\t\t\t\t        Enter based on corresponding number: ");
                            String admnChoice = sc.nextLine();

                            if(admnChoice.equals("1")){

                                isValid = true;
                    
                                try {
                                    
                                    PreparedStatement checkTable = conn.prepareStatement("SELECT * FROM products");
                                    ResultSet checkResult = checkTable.executeQuery();
            
                                    if(!checkResult.next()) { 
            
                                        System.out.println(tableEmptyPrompt.messagePrompt());
                                        selectTask();
            
                                    }else { 
            
                                        removeItem(); 
                                    }
            
                                } catch (Exception e) {
                                    
                                }
                            }else if(admnChoice.equals("2")){

                                isValid = true;
                                selectTask();

                            }
                        }while(isValid == false);
                        
                    } catch (Exception e) {
                    }
        
                }else if(adminChoice.equals("2")){

                    isValid = true;
                    
                    try {
                        
                        PreparedStatement checkTable = conn.prepareStatement("SELECT * FROM products");
                        ResultSet checkResult = checkTable.executeQuery();

                        if(!checkResult.next()) { 

                            System.out.println(tableEmptyPrompt.messagePrompt());
                            selectTask();

                        }else { 

                            removeItem();
                        }

                    } catch (Exception e) {
                        
                    }

                }else if(adminChoice.equals("3")) { 

                    isValid = true;
                    selectTask();
        
                }else { 
        
                    isValid = false;
                    System.out.println();
                    System.out.println(generalPrompt.messagePrompt());
        
                }
    
            }while(isValid == false);

        } catch (Exception e) {
        }

    }

    @Override
    public void nonExpiryItems() { //function that will be called if the system detects that the admin will input non-expiry items

        aeDataBase = new config();
        conn = aeDataBase.getConnection(); 

        try {

            System.out.println();
            System.out.println("\t\t\t\t\t\t\t\t     YOU ARE ABOUT TO INPUT A NON-EXPIRY PRODUCT.\n");
            System.out.println("\t\t\t\t\t\t\t\t      Enter Product Name (Ex. Tender Juicy Hotdog)");
                System.out.print("\t\t\t\t\t\t\t\t      > ");
                productName = sc.nextLine().toUpperCase();

                productsInv.setProductName(productName); //calling setter method to set product name

                System.out.println();
                    
                System.out.println("\t\t\t\t\t\t\t\t      Enter Product Quantity (Ex. 20)");
                System.out.print("\t\t\t\t\t\t\t\t      > ");
                String productQnt = sc.nextLine();
                System.out.println();

                productsInv.setProductQuantity(productQnt);

                long millis = System.currentTimeMillis(); //getting the time and storing it into a long variable
                Date dateToday = new Date(millis); //using java.sql.date we can get the date today, this will serve as the date where a product has been inserted.

                productsInv.setDateToday(dateToday); //calling the setter method to set the date today.

                long dateAddedMillis = System.currentTimeMillis();
                Date dateAdded = new Date(dateAddedMillis);

                productsInv.setDateAdded(dateAdded); //this date added is for date added logs

                postMethod();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    long updatedDays;

    @Override
    public void updateTodaysDate() { //this function is for updating the date today automatically, it will also deduct the days left

       aeDataBase = new config();
       conn = aeDataBase.getConnection();

       productsInv = new productsInv(); 

       try {

        long millis = System.currentTimeMillis();
        Date updatedDate = new Date(millis); //getting the date today

        PreparedStatement getPrevDate = conn.prepareStatement("SELECT * FROM products");
        ResultSet getPrev = getPrevDate.executeQuery();

        getPrev.next();

        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd"); // same pattern with the db

        if(formatDate.format(updatedDate).equals(formatDate.format(getPrev.getDate("date_today")))) { //check if the date today is = to the date_today column in the date base

            //do nothing

        }else { //if not update the date_today column with updated date today and deduct the days_Left by 1

            PreparedStatement updateTheDate = conn.prepareStatement("UPDATE products SET date_today = ('" + updatedDate +"')");
            updateTheDate.executeUpdate();

            PreparedStatement updateDaysLeft = conn.prepareStatement("UPDATE products SET days_left = days_left - 1 WHERE product_exp != ''");
            updateDaysLeft.executeUpdate();
        }
       } catch (Exception e) {
       }

    }

    @Override
    public void removeExpiredProducts() { //this function is for automatically removing the 'expiry product' with 0 days left and inserting it into expired products table. 
        
        try {
            
            aeDataBase = new config();
            conn = aeDataBase.getConnection();

            String product_status = "EXPIRED"; //default status

            PreparedStatement selectExpiredProducts = conn.prepareStatement("SELECT * FROM products WHERE days_left = '0' AND product_exp != ''"); //filter out the non expiry items and selecting product where days left is = 0
            ResultSet selectExpired = selectExpiredProducts.executeQuery();
 
            while(selectExpired.next()) { 

                PreparedStatement insertToExpTableQuery = conn.prepareStatement( //insert into expired products table query
                    "INSERT INTO expired_products (product_id, product_name, product_exp, status) VALUES ('" + selectExpired.getInt("product_id")  + "', '" + selectExpired.getString("product_name")  + "', '" + selectExpired.getDate("product_exp") + "', '"+ product_status + "')"); //getting the fetched products and inserting into a new table.
        
                insertToExpTableQuery.executeUpdate();

                PreparedStatement removeFromTableQuery = conn.prepareStatement("DELETE FROM products WHERE days_left = '0' AND product_exp != ''"); //after inserting it, delete it from the products table.
                removeFromTableQuery.executeUpdate();

            }

        } catch (Exception e) {
        }
        
    }

    @Override
    public void viewExpiredItems() { //function for viewing the expired products table.
        
        try {
            
            aeDataBase = new config();
            conn = aeDataBase.getConnection();

            PreparedStatement selectExpiredQuery = conn.prepareStatement("SELECT * FROM expired_products");
            ResultSet fetchExpiredProducts = selectExpiredQuery.executeQuery();

            System.out.println();
            System.out.println("=====================================================================================================================================================================================\n");
            System.out.println("\t\t\t\t\t\t\t\t\t       EXPIRED PRODUCTS TABLE");
            System.out.println("\t\t\t\t      -----------------------------------------------------------------------------------------------------------");
            System.out.printf("\t\t\t\t\t        %20s%15s%15s%15s", " ID" ,"      PRODUCT NAME" , "EXP DATE", "STATUS");
            System.out.println();
            System.out.println("\t\t\t\t      -----------------------------------------------------------------------------------------------------------");

            while(fetchExpiredProducts.next()) { 

                System.out.printf("\t\t\t\t\t        %20s%14s%20s%14s" ,fetchExpiredProducts.getInt("product_id") ,fetchExpiredProducts.getString("product_name"), fetchExpiredProducts.getDate("product_exp"), fetchExpiredProducts.getString("status"));
                System.out.println();
            
                System.out.println("\t\t\t\t      -----------------------------------------------------------------------------------------------------------");

            }

        } catch (Exception e) {
        }

        do{
            System.out.println();
            System.out.println("\t\t\t\t\t\t\t\t               == What do you want to do? ==\n");
            System.out.println("\t\t\t\t\t\t\t\t              > View Expiry table[1]\n");
            System.out.println("\t\t\t\t\t\t\t\t              > Clear Table & Insert into CSV[2]\n");
            System.out.println("\t\t\t\t\t\t\t\t              > Back[3]\n");

            System.out.print("\t\t\t\t\t\t\t\t        Enter based on corresponding number: ");
            String adminChoice = sc.nextLine();

            if(adminChoice.equals("1")){

                isValid = true;
                viewItemsExpiry();

            }else if(adminChoice.equals("2")) {

                isValid = true;
                
                try {
                    
                    PreparedStatement checkTableIfEmptyQuery = conn.prepareStatement("SELECT * FROM expired_products");
                    ResultSet fetchProducts = checkTableIfEmptyQuery.executeQuery();

                    if(!fetchProducts.next()) { 

                        System.out.println();
                        System.out.println("     "+ tableEmptyPrompt.messagePrompt());
                        isValid = false;

                    }else  {

                        clearExpiredProducts();
                    }

                } catch (Exception e) {
                    
                }

            }else if(adminChoice.equals("3")){

                isValid = true;
                selectTask();

            }else{
                isValid = false;
                System.out.println(generalPrompt.messagePrompt());
            }

        }while(isValid == false);

    }

    @Override
    public void clearExpiredProducts() { //function for clearing the table and getting the all of the date into a CSV file for inventory purposes
        
        try {
            
            aeDataBase = new config();
            conn = aeDataBase.getConnection();

            File fileToBeDeleted = new File("C:\\Users\\joshu\\OneDrive\\Desktop\\MiniCapstone\\main\\dataLogFile\\expiredProductsLogs.csv");
            PreparedStatement getPreviousData = conn.prepareStatement("LOAD DATA INFILE 'C:/Users/joshu/OneDrive/Desktop/MiniCapstone/main/dataLogFile/expiredProductsLogs.csv' INTO TABLE expired_products FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n';");
            getPreviousData.executeUpdate(); //get the previous data from the previous file, inserting it into the table before overwriting it.

            if(fileToBeDeleted.delete()) { //delete the first file and overwrite it with a new one.

                PreparedStatement insertIntoLogFile = conn.prepareStatement("SELECT * INTO OUTFILE 'C:/Users/joshu/OneDrive/Desktop/MiniCapstone/main/dataLogFile/expiredProductsLogs.csv' FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n' FROM expired_products");
                insertIntoLogFile.executeQuery();

            }else { //else if file does not exist create a file

                PreparedStatement insertIntoLogFile = conn.prepareStatement("SELECT * INTO OUTFILE 'C:/Users/joshu/OneDrive/Desktop/MiniCapstone/main/dataLogFile/expiredProductsLogs.csv' FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n' FROM expired_products");
                insertIntoLogFile.executeQuery();

            }
            
            PreparedStatement deleteAllQuery = conn.prepareStatement("DELETE FROM expired_products WHERE status = 'EXPIRED'"); //clear the table after getting all data
            deleteAllQuery.executeUpdate();

            System.out.println();
            System.out.println(tableCleared.messagePrompt());
    
            do{

                System.out.println();
                System.out.println("\t\t\t\t\t\t\t\t               == What do you want to do? ==\n");
                System.out.println("\t\t\t\t\t\t\t\t                  > Back[1]\n");
                
                System.out.print("\t\t\t\t\t\t\t\t        Enter based on corresponding number: ");
                String adminChoice = sc.nextLine();

                if(adminChoice.equals("1")) { 

                    isValid = true;
                    viewExpiredItems();

                }else { 

                    isValid = false;
                    System.out.println();
                    System.out.println(generalPrompt.messagePrompt());
                }

            }while(isValid == false);

        } catch (Exception e) {

            System.out.println(e);
        }   
    }

    @Override
    public void viewProductsDate() { //function for viewing the date added logs for inventory purposes.

        try {
            
            aeDataBase = new config();
            conn = aeDataBase.getConnection();

            PreparedStatement dataLogsQuery = conn.prepareStatement("SELECT * FROM productadd_logs");
            ResultSet resultLogs = dataLogsQuery.executeQuery();

            System.out.println();
            System.out.println("\t\t\t\t\t\t\t\t\t         DATE ADDED LOGS TABLE");
            System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("\t\t\t\t\t\t   %8s%18s%18s%15s%15s", " ID" ,"      PRODUCT NAME" , "      DATE ADDED", "     DATE MODIFIED (QNT)", "          DATE REMOVED");
            System.out.println();
            System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");

            while(resultLogs.next()) {

                System.out.printf("\t\t\t\t\t\t        %3s%14s%22s%20s%10s%15s" ,resultLogs.getInt("product_id") ,resultLogs.getString("product_name"), resultLogs.getDate("date_added"), resultLogs.getString("date_quantity_modifed"),"          ", resultLogs.getString("date_removed"));
                System.out.println();
        
                System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
            }

            do{

                System.out.println();
                System.out.println("\t\t\t\t\t\t\t\t               == What do you want to do? ==\n");
                System.out.println("\t\t\t\t\t\t\t\t                  > Sort table[1]\n");
                System.out.println("\t\t\t\t\t\t\t\t                  > Get the CSV file[2]\n");
                System.out.println("\t\t\t\t\t\t\t\t                  > Back[3]\n");
                
                System.out.print("\t\t\t\t\t\t\t\t        Enter based on corresponding number: ");
                String adminChoice = sc.nextLine();

                if(adminChoice.equals("1"))  {

                    isValid = true;
                    aeDataBase = new config();
                    conn = aeDataBase.getConnection();

                    PreparedStatement checkDateAddedLogs = conn.prepareStatement("SELECT * FROM productadd_logs");
                    ResultSet checkResult = checkDateAddedLogs.executeQuery();

                    if(!checkResult.next()) {

                        isValid = false;
                        System.out.println();
                        System.out.println(tableEmptyPrompt.messagePrompt());

                    }else  {
                        do {

                            System.out.println();
                            System.out.println("\t\t\t\t\t\t\t\t               == Sort the table by? ==\n");
                            System.out.println("\t\t\t\t\t\t\t\t                  > Ascending Order[1]\n");
                            System.out.println("\t\t\t\t\t\t\t\t                  > Descending Order[2]\n");
                            System.out.print("\t\t\t\t\t\t\t\t        Enter based on corresponding number: ");
                            String sortChoice = sc.nextLine();
    
                            if(sortChoice.equals("1")) { //sort the date added in ascending order
    
                                isValid = true;
    
                                PreparedStatement ascendingQuery = conn.prepareStatement("SELECT * FROM productadd_logs ORDER BY date_added ASC");
                                ResultSet ascendingResult = ascendingQuery.executeQuery();
    
                                System.out.println();
                                System.out.println("\t\t\t\t\t\t\t\t\t         DATE ADDED LOGS TABLE (ASC)");
                                System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
                                System.out.printf("\t\t\t\t\t\t\t\t   %8s%18s%18s", " ID" ,"      PRODUCT NAME" , " DATE ADDED");
                                System.out.println();
                                System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
    
                                while(ascendingResult.next()) { 
    
                                    System.out.printf("\t\t\t\t\t\t\t\t        %3s%14s%22s" ,ascendingResult.getInt("product_id") ,ascendingResult.getString("product_name"), ascendingResult.getDate("date_added"));
                                    System.out.println();
                            
                                    System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
                            
                                }
    
                            }else if(sortChoice.equals("2")) { //sort table in descending order
    
                                isValid = true;
    
                                PreparedStatement descendingQuery = conn.prepareStatement("SELECT * FROM productadd_logs ORDER BY date_added DESC");
                                ResultSet descendingResult = descendingQuery.executeQuery();
    
                                System.out.println();
                                System.out.println("\t\t\t\t\t\t\t\t\t         DATE ADDED LOGS TABLE (DESC)");
                                System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
                                System.out.printf("\t\t\t\t\t\t\t\t   %8s%18s%18s", " ID" ,"      PRODUCT NAME" , " DATE ADDED");
                                System.out.println();
                                System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
    
                                while(descendingResult.next()) { 
    
                                    System.out.printf("\t\t\t\t\t\t\t\t        %3s%14s%22s" ,descendingResult.getInt("product_id") ,descendingResult.getString("product_name"), descendingResult.getDate("date_added"));
                                    System.out.println();
                            
                                    System.out.println("\t\t\t\t ---------------------------------------------------------------------------------------------------------------------------");
                            
                                }
    
                            }else { 
    
                                isValid = false;
                                System.out.println();
                                System.out.println(generalPrompt.messagePrompt());
    
                            }
    
                        } while (isValid == false);
    
                            do{
    
                                System.out.println();
                                System.out.println("\t\t\t\t\t\t\t\t               == What do you want to do? ==\n");
                                System.out.println("\t\t\t\t\t\t\t\t                  > Back[1]\n");
                                System.out.print("\t\t\t\t\t\t\t\t        Enter based on corresponding number: ");
                                String backChoice = sc.nextLine();
    
                                if(backChoice.equals("1")) { 
                                    
                                    isValid = true;
                                    viewProductsDate();
                                }else { 
    
                                    isValid = false;
                                    System.out.println();
                                    System.out.println(generalPrompt.messagePrompt());
                                }
    
                            }while(isValid == false);
    
                    }
                    
                }else if(adminChoice.equals("2")) { //getting the CSV file of the date added for inventory purposes.

                    isValid = true;

                   try { 

                        aeDataBase = new config();
                        conn = aeDataBase.getConnection();

                        PreparedStatement checkDateAddedLogs = conn.prepareStatement("SELECT * FROM productadd_logs");
                        ResultSet checkResult = checkDateAddedLogs.executeQuery();

                        if(!checkResult.next()) { 

                            isValid = false;
                            System.out.println();
                            System.out.println(tableEmptyPrompt.messagePrompt());

                        }else  {

                            File fileToBeDeleted = new File("C:\\Users\\joshu\\OneDrive\\Desktop\\MiniCapstone\\main\\dataLogFile\\dateAddedLogs.csv");
                            fileToBeDeleted.delete();

                            PreparedStatement insertIntoLogFile = conn.prepareStatement("SELECT * INTO OUTFILE 'C:/Users/joshu/OneDrive/Desktop/MiniCapstone/main/dataLogFile/dateAddedLogs.csv' FIELDS TERMINATED BY '/,      ' LINES TERMINATED BY '\n' FROM productadd_logs");
                            insertIntoLogFile.executeQuery();

                            System.out.println();
                            System.out.println(logsPrinted.messagePrompt());
                        
                            do {

                                System.out.println("\t\t\t\t\t\t\t\t               == What do you want to do? ==\n");
                                System.out.println("\t\t\t\t\t\t\t\t                  > Back[1]\n");
                                System.out.print("\t\t\t\t\t\t\t\t        Enter based on corresponding number: ");
                                String backChoice = sc.nextLine();

                                if(backChoice.equals("1")) { 
                                    
                                    isValid = true;
                                    viewProductsDate();

                                }else { 

                                    isValid = false;
                                    System.out.println();
                                    System.out.println(generalPrompt.messagePrompt());
                                }
                                
                            } while (isValid == false);
                            }

                   } catch (Exception e) {

                    System.out.println(e);
                   }

                }else if(adminChoice.equals("3")) { 

                    isValid = true;
                    selectTask();

                }else { 

                    isValid = false;
                    System.out.println();
                    System.out.println(generalPrompt.messagePrompt());
                }

            }while(isValid == false);
        } catch (Exception e) { 
        }

    }

    @Override
    public void updateDateModified() { //function for updating the date modified whenever the admin updates it
        
        try {
            
            aeDataBase = new config();
            conn = aeDataBase.getConnection();

            PreparedStatement updateDateQuantityModified = conn.prepareStatement("UPDATE productadd_logs SET date_quantity_modifed = '"+ productsInv.getDateQuantityModified() + "' WHERE product_id = '"+ productID +"'");
            updateDateQuantityModified.executeUpdate();

        } catch (Exception e) {
        }
        
    }

    @Override
    public void updateDateRemoved() { //function for updating the remove product
        
        try {

            aeDataBase = new config();
            conn = aeDataBase.getConnection();

            PreparedStatement updateDateRemoved = conn.prepareStatement("UPDATE productadd_logs SET date_removed = '"+ productsInv.getDateRemoved() + "' WHERE product_id = '"+ removeID +"'");
            updateDateRemoved.executeUpdate();
            
        } catch (Exception e) {
        }
        
    }
} 