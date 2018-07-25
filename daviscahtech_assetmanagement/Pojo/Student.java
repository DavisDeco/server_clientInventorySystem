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
public class Student {
    
    private final SimpleStringProperty id;
    private final SimpleStringProperty name;
    private final SimpleStringProperty studentClass;
    private final SimpleStringProperty dateReg;

    public Student(String id, String name, String studentClass, String dateReg) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.studentClass = new SimpleStringProperty(studentClass);
        this.dateReg = new SimpleStringProperty(dateReg);
    }

    /**
     * @return the id
     */
    public String getId() {
        return id.get();
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        idProperty().set(id);
    }
    
    public final StringProperty idProperty(){
        return id;
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
     * @return the studentClass
     */
    public String getStudentClass() {
        return studentClass.get();
    }

    /**
     * @param studentClass the studentClass to set
     */
    public void setStudentClass(String studentClass) {
       studentClassProperty().set(studentClass);
    }
    
    public final StringProperty studentClassProperty(){
        return studentClass;
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
