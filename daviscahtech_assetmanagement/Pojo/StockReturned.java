/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daviscahtech_assetmanagement.Pojo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author davis
 */
public class StockReturned {
    
    private final SimpleStringProperty code;
    private final SimpleStringProperty name;
    private final SimpleStringProperty quantity;
    private final SimpleStringProperty type;
    private final SimpleStringProperty department;
    private final SimpleStringProperty returnedBy;
    private final SimpleStringProperty dateReturned;

    public StockReturned(String code, String name, String quantity, String type, String department, String returnedBy, String dateReturned) {
        this.code = new SimpleStringProperty(code);
        this.name = new SimpleStringProperty(name);
        this.quantity = new SimpleStringProperty(quantity);
        this.type = new SimpleStringProperty(type);
        this.department = new SimpleStringProperty(department);
        this.returnedBy = new SimpleStringProperty(returnedBy);
        this.dateReturned = new SimpleStringProperty(dateReturned);
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code.get();
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
       codeProperty().set(code);
    }
    
    public final StringProperty codeProperty(){
        return code;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name.get();
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        nameProperty().set(name);
    }
    
    public final StringProperty nameProperty(){
        return name;
    }

    /**
     * @return the quantity
     */
    public String getQuantity() {
        return quantity.get();
    }    
    

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(String quantity) {
        quantityProperty().set(quantity);
    }
    
    public final StringProperty quantityProperty(){
        return quantity;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type.get();
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        typeProperty().set(type);
    }
    
    public final StringProperty typeProperty(){
        return type;
    }

    /**
     * @return the department
     */
    public String getDepartment() {
        return department.get();
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(String department) {
        departmentProperty().set(department);
    }
    
    public final StringProperty departmentProperty(){
        return department;
    }

    /**
     * @return the returnedBy
     */
    public String getReturnedBy() {
        return returnedBy.get();
    }

    /**
     * @param returnedBy the returnedBy to set
     */
    public void setReturnedBy(String returnedBy) {
        returnedByProperty().set(returnedBy);
    }
    
    public final StringProperty returnedByProperty(){
        return returnedBy;
    }

    /**
     * @return the dateReturned
     */
    public String getDateReturned() {
        return dateReturned.get();
    }

    /**
     * @param dateReturned the dateReturned to set
     */
    public void setDateReturned(String dateReturned) {
        dateReturnedProperty().set(dateReturned);
    }
    
    public final StringProperty dateReturnedProperty(){
        return dateReturned;
    }
    
    
    
}// end of class
