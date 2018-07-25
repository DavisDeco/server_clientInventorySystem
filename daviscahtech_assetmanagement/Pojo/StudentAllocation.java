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
public class StudentAllocation {
    
    private final SimpleStringProperty studentID;
    private final SimpleStringProperty studentName;
    private final SimpleStringProperty studentClass;
    private final SimpleStringProperty deskNo;
    private final SimpleStringProperty chairNo;
    private final SimpleStringProperty dateReg;

    public StudentAllocation(String studentID, String studentName, String studentClass, String deskNo, String chairNo, String dateReg) {
        this.studentID = new SimpleStringProperty(studentID);
        this.studentName = new SimpleStringProperty(studentName);
        this.studentClass = new SimpleStringProperty(studentClass);
        this.deskNo = new SimpleStringProperty(deskNo);
        this.chairNo = new SimpleStringProperty(chairNo);
        this.dateReg = new SimpleStringProperty(dateReg);
    }

    /**
     * @return the studentID
     */
    public String getStudentID() {
        return studentID.get();
    }

    /**
     * @param studentID the studentID to set
     */
    public void setStudentID(String studentID) {
        studentIDProperty().set(studentID);
    }
    
    public final StringProperty studentIDProperty(){
        return studentID;
    }

    /**
     * @return the studentName
     */
    public String getStudentName() {
        return studentName.get();
    }

    /**
     * @param studentName the studentName to set
     */
    public void setStudentName(String studentName) {
       studentNameProperty().set(studentName);
    }
    
    public final StringProperty studentNameProperty(){
        return studentName;
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
     * @return the deskNo
     */
    public String getDeskNo() {
        return deskNo.get();
    }

    /**
     * @param deskNo the deskNo to set
     */
    public void setDeskNo(String deskNo) {
        deskNoProperty().set(deskNo);
    }
    
    public final StringProperty deskNoProperty(){
        return deskNo;
    }

    /**
     * @return the chairNo
     */
    public String getChairNo() {
        return chairNo.get();
    }

    /**
     * @param chairNo the chairNo to set
     */
    public void setChairNo(String chairNo) {
        chairNoProperty().set(chairNo);
    }
    
    public final StringProperty chairNoProperty(){
        return chairNo;
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
