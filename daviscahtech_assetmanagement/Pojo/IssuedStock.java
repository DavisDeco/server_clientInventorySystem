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
public class IssuedStock {
    
    private final SimpleStringProperty code;
    private final SimpleStringProperty name;
    private final SimpleStringProperty quantity;
    private final SimpleStringProperty receipient;
    private final SimpleStringProperty department;
    private final SimpleStringProperty dateIssued;
    private final SimpleStringProperty dateReg;

    public IssuedStock(String code, String name, String quantity, String receipient, String department, String dateIssued, String dateReg) {
        this.code = new SimpleStringProperty(code);
        this.name =  new SimpleStringProperty(name);
        this.quantity =  new SimpleStringProperty(quantity);
        this.receipient =  new SimpleStringProperty(receipient);
        this.department =  new SimpleStringProperty(department);
        this.dateIssued =  new SimpleStringProperty(dateIssued);
        this.dateReg =  new SimpleStringProperty(dateReg);
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
     * @return the receipient
     */
    public String getReceipient() {
        return receipient.get();
    }

    /**
     * @param receipient the receipient to set
     */
    public void setReceipient(String receipient) {
        receipientProperty().set(receipient);
    }
    
    public final StringProperty receipientProperty(){
        return receipient;
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
     * @return the dateIssued
     */
    public String getDateIssued() {
        return dateIssued.get();
    }

    /**
     * @param dateIssued the dateIssued to set
     */
    public void setDateIssued(String dateIssued) {
        dateIssuedProperty().set(dateIssued);
    }
    
    public final StringProperty dateIssuedProperty(){
        return dateIssued;
    }

    /**
     * @return the dateReg
     */
    public String getDateReg() {
        return dateReg.get();
    }

    /**
     * @param dateReg the dateReg to set
     */
    public void setDateReg(String dateReg) {
        dateRegProperty().set(dateReg);
    }
    
    public final StringProperty dateRegProperty(){
        return dateReg;
    }
    
    
    
    
}// end of class
