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
public class Suppliers {
    
    private final  SimpleStringProperty name;
    private final  SimpleStringProperty address;
    private final  SimpleStringProperty email;
    private final  SimpleStringProperty contacts;
    private final  SimpleStringProperty date_approved;
    private final  SimpleStringProperty date_reg;

    public Suppliers(String name, String address, String email, String contacts, String date_approved, String date_reg) {
        this.name = new  SimpleStringProperty(name);
        this.address = new  SimpleStringProperty(address);
        this.email = new  SimpleStringProperty(email);
        this.contacts = new  SimpleStringProperty(contacts);
        this.date_approved = new  SimpleStringProperty(date_approved);
        this.date_reg = new  SimpleStringProperty(date_reg);
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
        namepProperty().set(name);
    }
    
    public final StringProperty namepProperty(){
        return name;
    }    

    /**
     * @return the address
     */
    public String getAddress() {
        return address.get();
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        addressProperty().set(address);
    }
    
    public final StringProperty addressProperty(){
        return address;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email.get();
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        emailProperty().set(email);
    }
    
    public final StringProperty emailProperty(){
        return email;
    }

    /**
     * @return the contacts
     */
    public String getContacts() {
        return contacts.get();
    }

    /**
     * @param contacts the contacts to set
     */
    public void setContacts(String contacts) {
        contactsProperty().set(contacts);
    }
    
    public final StringProperty contactsProperty(){
        return contacts;
    }

    /**
     * @return the date_approved
     */
    public String getDate_approved() {
        return date_approved.get();
    }

    /**
     * @param date_approved the date_approved to set
     */
    public void setDate_approved(String date_approved) {
        date_approvedProperty().set(date_approved);
    }
    
    public final StringProperty date_approvedProperty(){
        return date_approved;
    }

    /**
     * @return the date_reg
     */
    public String getDate_reg() {
        return date_reg.get();
    }

    /**
     * @param date_reg the date_reg to set
     */
    public void setDate_reg(String date_reg) {
        date_regProperty().set(date_reg);
    }
    
    public final StringProperty date_regProperty(){
        return date_reg;
    }
    
}// end of class
