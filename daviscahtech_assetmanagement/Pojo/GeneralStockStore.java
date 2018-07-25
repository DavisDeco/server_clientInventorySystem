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
public class GeneralStockStore {
    
    private final SimpleStringProperty code;
    private final SimpleStringProperty name;
    private final SimpleStringProperty quantity;
    private final SimpleStringProperty department;
    private final SimpleStringProperty cost;
    private final SimpleStringProperty alartStatus;
    private final SimpleStringProperty date_reg;

    public GeneralStockStore(String code, String name, String quantity, String department, String cost, String alartStatus) {
        this.code = new SimpleStringProperty(code);
        this.name =  new SimpleStringProperty(name);
        this.quantity =  new SimpleStringProperty(quantity);
        this.department =  new SimpleStringProperty(department);
        this.cost =  new SimpleStringProperty(cost);
        this.alartStatus =  new SimpleStringProperty(alartStatus);
        this.date_reg = null;
    }
    
    public GeneralStockStore(String code, String name, String quantity, String department, String cost,String alart,String date_reg) {
        this.code = new SimpleStringProperty(code);
        this.name =  new SimpleStringProperty(name);
        this.quantity =  new SimpleStringProperty(quantity);
        this.department =  new SimpleStringProperty(department);
        this.cost =  new SimpleStringProperty(cost);
        this.alartStatus = null;
        this.date_reg =  new SimpleStringProperty(date_reg);
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
     * @return the cost
     */
    public String getCost() {
        return cost.get();
    }

    /**
     * @param cost the cost to set
     */
    public void setCost(String cost) {
        costProperty().set(cost);
    }
    
    public final StringProperty costProperty(){    
        return cost;
    }


    /**
     * @return the alartStatus
     */
    public String getAlartStatus() {
        return alartStatus.get();
    }

    /**
     * @param alartStatus the alartStatus to set
     */
    public void setAlartStatus(String alartStatus) {
        alartStatusProperty().set(alartStatus);
    }
    
    public final StringProperty alartStatusProperty(){    
        return alartStatus;
    }
    

    /**
     * @return the date_reg
     */
    public String getDate_reg() {
        return date_reg.get();
    }

    /**
     * @param alartStatus the alartStatus to set
     */
    public void setDate_reg(String date_reg) {
        date_regProperty().set(date_reg);
    }
    
    public final StringProperty date_regProperty(){    
        return date_reg;
    }    
    
    
}// end of class
