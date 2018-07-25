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
public class StockTrace {
    
    private final SimpleStringProperty productCode;
    private final SimpleStringProperty productName;
    private final SimpleStringProperty productQuantity;
    private final SimpleStringProperty productType;
    private final SimpleStringProperty productCost;
    private final SimpleStringProperty productSupplier;
    private final SimpleStringProperty department;
    private final SimpleStringProperty productDateStocked;

    public StockTrace(String productCode, String productName, String productQuantity, String productType, String productCost, String productSupplier, String productDateStocked) {
        this.productCode = new SimpleStringProperty(productCode);
        this.productName = new SimpleStringProperty(productName);
        this.productQuantity = new SimpleStringProperty(productQuantity);
        this.productType = new SimpleStringProperty(productType);
        this.productCost = new SimpleStringProperty(productCost);
        this.productSupplier = new SimpleStringProperty(productSupplier);
        this.department = null;
        this.productDateStocked = new SimpleStringProperty(productDateStocked);
    }
    
    public StockTrace(String productCode, String productName, String productQuantity, String productType, String productCost, String productSupplier,String department, String productDateStocked) {
        this.productCode = new SimpleStringProperty(productCode);
        this.productName = new SimpleStringProperty(productName);
        this.productQuantity = new SimpleStringProperty(productQuantity);
        this.productType = new SimpleStringProperty(productType);
        this.productCost = new SimpleStringProperty(productCost);
        this.productSupplier = new SimpleStringProperty(productSupplier);
        this.department = new SimpleStringProperty(department);
        this.productDateStocked = new SimpleStringProperty(productDateStocked);
    }    

    /**
     * @return the productCode
     */
    public String getProductCode() {
        return productCode.get();
    }

    /**
     * @param productCode the productCode to set
     */
    public void setProductCode(String productCode) {
        productCodeProperty().set(productCode);
    }
    
    public final StringProperty productCodeProperty(){
        return productCode;
    }

    /**
     * @return the productName
     */
    public String getProductName() {
        return productName.get();
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        productNameProperty().set(productName);
    }
    
    public final StringProperty productNameProperty(){
        return productName;
    }

    /**
     * @return the productQuantity
     */
    public String getProductQuantity() {
        return productQuantity.get();
    }

    /**
     * @param productQuantity the productQuantity to set
     */
    public void setProductQuantity(String productQuantity) {
        productQuantityProperty().set(productQuantity);
    }
    
    public final StringProperty productQuantityProperty(){
        return productQuantity;
    }

    /**
     * @return the productType
     */
    public String getProductType() {
        return productType.get();
    }

    /**
     * @param productType the productType to set
     */
    public void setProductType(String productType) {
        productTypeProperty().set(productType);
    }
    
    public final StringProperty productTypeProperty(){
        return productType;
    }

    /**
     * @return the productCost
     */
    public String getProductCost() {
        return productCost.get();
    }

    /**
     * @param productCost the productCost to set
     */
    public void setProductCost(String productCost) {
        productCostProperty().set(productCost);
    }
    
    public final StringProperty productCostProperty(){
        return productCost;
    }

    /**
     * @return the productSupplier
     */
    public String getProductSupplier() {
        return productSupplier.get();
    }

    /**
     * @param productSupplier the productSupplier to set
     */
    public void setProductSupplier(String productSupplier) {
        productSupplierProperty().set(productSupplier);
    }
    
    public final StringProperty productSupplierProperty(){
        return productSupplier;
    }
    
    /**
     * @return the department
     */
    public String getDepartment() {
        return department.get();
    }

    /**
     * @param department the productSupplier to set
     */
    public void setDepartment(String department) {
        departmentProperty().set(department);
    }
    
    public final StringProperty departmentProperty(){
        return department;
    }    


    /**
     * @return the department
     */
    public String getProductDateStocked() {
        return productDateStocked.get();
    }

    /**
     * @param productDateStocked the productDateStocked to set
     */
    public void setProductDateStocked(String productDateStocked) {
        productDateStockedProperty().set(productDateStocked);
    }
    
    public final StringProperty productDateStockedProperty(){
        return productDateStocked;
    }

    
    
    
    
    
    
    
    
}// END OF CLASS
