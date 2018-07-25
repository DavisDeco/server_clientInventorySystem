/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daviscahtech_assetmanagement.Controller;

import daviscahtech_assetmanagement.Dao.DatabaseConnection;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Duration;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class
 *
 * @author davis
 */
public class DeskChairController implements Initializable {
    
    Image errorFlag = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/errorFlag.png").toExternalForm());
    ImageView errorImage = new ImageView(errorFlag);  
    
    Image thumbsImage = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/thumbs.png").toExternalForm());
    ImageView imageSuccess = new ImageView(thumbsImage); 

    Image schoolImage = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/School.png").toExternalForm());
    ImageView schoolSuccess = new ImageView(schoolImage);
    
    Image errorNotify = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/errornotification.png").toExternalForm());
    ImageView errorNotifyImageView = new ImageView(errorNotify);  
    
    Image goodImage = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/good.png").toExternalForm());
    ImageView successNotiyiImageView = new ImageView(goodImage);    
    
        //###############################For DB link##########################
    Connection con = null;
    PreparedStatement pstmt;
    ResultSet rs;
    Statement st;
    
//    private File file;
    private File excelFile;
    /*    private File pdfFile;
    private FileChooser fileChooser;
    private FileChooser pdfFileChooser;*/
    private FileChooser excelFileChooser;
    
    /*
    instance of Desktop and Window
    private final Desktop desktop = Desktop.getDesktop();
    */
    private Window Stage;     
    
    //NOTIFICATION BUILDER VARIABLE
    Notifications notificationBuilder;     

    @FXML
    private TextField deskNumber;
    @FXML
    private TextField chairNumber;
    @FXML
    private ListView<String> deskList;
    ObservableList<String> deskData = FXCollections.observableArrayList();
    @FXML
    private ListView<String> chairList;
    ObservableList<String> chairData = FXCollections.observableArrayList();
    private int chairIDHolder;
    private int deskIDHolder;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        con = DatabaseConnection.connectDb();
        
        refreshChairListView();
        refreshDeskListView();
    } 
    
            //Method to open a fileChooser dialog box
    public void excelFileChooserOpener(){
                excelFileChooser = new FileChooser();
                excelFileChooser.getExtensionFilters().addAll(                
                new FileChooser.ExtensionFilter("Excel Files","*.*")

                
        );   
    }     
    
    
    private void errorNotification(Pos pos,String title){
        notificationBuilder = Notifications.create()
                .title("Error occurred")
                .text(title)
                .graphic(errorNotifyImageView)
                .hideAfter(Duration.seconds(10))
                .position(pos)
                .onAction((ActionEvent event) -> {
                    System.out.println("Notification created but error occurred");
                });
        notificationBuilder.show();
                
    } 
    
    public void successNotification(Pos pos,String title){
       notificationBuilder = Notifications.create()
                .title("Success")
                .text(title)
                .graphic(successNotiyiImageView)
                .hideAfter(Duration.seconds(10))
                .position(pos)
                .onAction((ActionEvent event) -> {
                    System.out.println("Notification created");
                });
        notificationBuilder.show();
                
    }     

    @FXML
    private void registerDeskOperation(ActionEvent event) {
        
         if (deskNumber.getText().isEmpty() ) {             
                 String msg = null; 
                 
                if (deskNumber.getText().isEmpty()) {
                     msg = "Desk number is empty, you must enter it";                 
                } 
             
            Alert errorIssue = new Alert(Alert.AlertType.ERROR);
            errorIssue.setTitle("Registration Failed");
            errorIssue.setGraphic(errorImage);
            errorIssue.setHeaderText(null);
            errorIssue.setContentText("Failed registration.\n"
                        + msg);
            errorIssue.showAndWait(); 
            
        }else {              
             //show confirm alert before registering data of issued
            String ms = " Are you sure you want to register desk detail? ";           
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm registration");
            alert.setGraphic(schoolSuccess);
            alert.setHeaderText(null);
            alert.setContentText(ms);
           
            Optional<ButtonType> response = alert.showAndWait();            
            if (response.get()==ButtonType.OK) {
                
               try {
                    String sql = "INSERT INTO desk (desk_no) VALUES(?)";
                    pstmt= con.prepareStatement(sql);

                    pstmt.setString(1, deskNumber.getText());

                    int i = pstmt.executeUpdate(); // load data into the database             
                    if (i>0) {
                        //show alert before registering data of employee
                    String msg = " Desk number = "+deskNumber.getText()+ " has been registered and ready to be allocated to a student.";
                    successNotification(Pos.BOTTOM_RIGHT, msg);
                        
                    clearFields();
                    
                    } else {
                        errorNotification(Pos.BOTTOM_RIGHT, "OOPS! Could not register class due  to internal error.");
                    } 
                    
                    refreshDeskListView();
                    pstmt.close();
                    
                    
                } catch (Exception e) {
                   errorNotification(Pos.BOTTOM_RIGHT, "OOPS! Could not register class due  to internal error.");                        
                }finally {
                   try {
                        rs.close();
                        pstmt.close();
                    } catch (Exception e) {}               
               }
               
            }   
        }        
        
    }
    
    private void refreshDeskList() {
        try {
            String sql = "SELECT * FROM desk ";
            pstmt = con.prepareStatement(sql);
            rs=pstmt.executeQuery();
            while (rs.next()) {
                      deskData.add(rs.getString("desk_no"));            
               }
                                
                pstmt.close(); 
        } catch (SQLException ex) {
            Logger.getLogger(DeskChairController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // show this details on the listview
        deskList.getItems().setAll(deskData);
    }
    
    private void refreshDeskListView(){
        deskData.clear();
        refreshDeskList();
    }
    
    private void refreshChairList() {
        try {
            String sql = "SELECT * FROM chair ";
            pstmt = con.prepareStatement(sql);
            rs=pstmt.executeQuery();
            while (rs.next()) {
                      chairData.add(rs.getString("chair_no"));            
               }
                                
                pstmt.close(); 
        } catch (SQLException ex) {
            Logger.getLogger(DeskChairController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // show this details on the listview
        chairList.getItems().setAll(chairData);
    }

    private void refreshChairListView(){
        chairData.clear();
        refreshChairList();
    }    
    
    private void clearFields(){
        deskNumber.clear();
        chairNumber.clear();
        
    }

    @FXML
    private void deleteDeskOperation(ActionEvent event) {
        
         if (deskNumber.getText().isEmpty()) {
            
            Alert errorIssue = new Alert(Alert.AlertType.ERROR);
            errorIssue.setTitle("Delete Failed");
            errorIssue.setGraphic(errorImage);
            errorIssue.setHeaderText(null);
            errorIssue.setContentText("The desk has failed DELETE operation.\n"
                        + "Ensure to retrieve desk information before deleting.");
            errorIssue.showAndWait(); 
            
        }else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete");
            alert.setGraphic(schoolSuccess);
            alert.setHeaderText(null);
            alert.setContentText("Are you sure to delete this desk?");         
            Optional <ButtonType> obt = alert.showAndWait();

            if (obt.get()== ButtonType.OK) {
                try {
               String query = "DELETE FROM desk WHERE id = ? ";
               pstmt = con.prepareStatement(query);
               pstmt.setInt(1, deskIDHolder);
               pstmt.executeUpdate();            
               pstmt.close(); 
               
               clearFields();
               refreshDeskListView();
               
               } catch (SQLException ex) {
                   Logger.getLogger(DeskChairController.class.getName()).log(Level.SEVERE, null, ex);
               }finally {
                    try {
                        rs.close();
                        pstmt.close();
                    } catch (Exception e) {
                    }        
                }
                      
           }            
        }    
        
    }

    @FXML
    private void registerChairOperation(ActionEvent event) {
        
         if (chairNumber.getText().isEmpty() ) {             
                 String msg = null; 
                 
                if (chairNumber.getText().isEmpty()) {
                     msg = "Chair number is empty, you must enter it";                 
                } 
             
            Alert errorIssue = new Alert(Alert.AlertType.ERROR);
            errorIssue.setTitle("Registration Failed");
            errorIssue.setGraphic(errorImage);
            errorIssue.setHeaderText(null);
            errorIssue.setContentText("Failed registration.\n"
                        + msg);
            errorIssue.showAndWait(); 
            
        }else {              
             //show confirm alert before registering data of issued
            String ms = " Are you sure you want to register chair detail? ";           
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm registration");
            alert.setGraphic(schoolSuccess);
            alert.setHeaderText(null);
            alert.setContentText(ms);
           
            Optional<ButtonType> response = alert.showAndWait();            
            if (response.get()==ButtonType.OK) {
                
               try {
                    String sql = "INSERT INTO chair (chair_no) VALUES(?)";
                    pstmt= con.prepareStatement(sql);

                    pstmt.setString(1, chairNumber.getText());

                    int i = pstmt.executeUpdate(); // load data into the database             
                    if (i>0) {
                        //show alert before registering data of employee
                    String msg = " Chair number "+chairNumber.getText()+ " has been registered and ready to be allocated to a student.";
                    successNotification(Pos.BOTTOM_RIGHT, msg);
                        
                    clearFields();
                    
                    } else {
                        errorNotification(Pos.BOTTOM_RIGHT, "OOPS! Could not register class due  to internal error.");
                    } 
                    
                    refreshChairListView();
                    pstmt.close();
                    
                    
                } catch (Exception e) {
                   errorNotification(Pos.BOTTOM_RIGHT, "OOPS! Could not register class due  to internal error.");                        
                }finally {
                   try {
                        rs.close();
                        pstmt.close();
                    } catch (Exception e) {}               
               }
               
            }   
        }        
        
    }

    @FXML
    private void deleeteChairOperation(ActionEvent event) {
        
         if (chairNumber.getText().isEmpty()) {
            
            Alert errorIssue = new Alert(Alert.AlertType.ERROR);
            errorIssue.setTitle("Delete Failed");
            errorIssue.setGraphic(errorImage);
            errorIssue.setHeaderText(null);
            errorIssue.setContentText("The chair has failed DELETE operation.\n"
                        + "Ensure to retrieve chair information before deleting.");
            errorIssue.showAndWait(); 
            
        }else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete");
            alert.setGraphic(schoolSuccess);
            alert.setHeaderText(null);
            alert.setContentText("Are you sure to delete this chair?");         
            Optional <ButtonType> obt = alert.showAndWait();

            if (obt.get()== ButtonType.OK) {
                try {
               String query = "DELETE FROM chair WHERE id = ? ";
               pstmt = con.prepareStatement(query);
               pstmt.setInt(1, chairIDHolder);
               pstmt.executeUpdate();            
               pstmt.close(); 
               
               clearFields();
               refreshChairListView();
               
               } catch (SQLException ex) {
                   Logger.getLogger(DeskChairController.class.getName()).log(Level.SEVERE, null, ex);
               }finally {
                    try {
                        rs.close();
                        pstmt.close();
                    } catch (Exception e) {
                    }        
                }
                      
           }            
        }        
        
    }

    @FXML
    private void importDeskListOperation(ActionEvent event) {
        
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Import Desk data");
            alert.setGraphic(schoolSuccess);
            alert.setHeaderText(null);
            alert.setContentText("Only data in Excel format and with an extension of .xlsx can be imported in the correct format.\n\n"
                    + "Are you sure you want to import Desk data from outside the system? ");
           
            Optional<ButtonType> response = alert.showAndWait();            
            if (response.get()==ButtonType.OK) {
                
                excelFileChooserOpener();
                   excelFileChooser.setTitle("Select desks' data File Name");
                   //single File selection
                   excelFile = excelFileChooser.showOpenDialog(Stage);
                    if (excelFile != null) {
                        String filePath = excelFile.getAbsolutePath();
                        
                        try {
                            String sql = "INSERT INTO desk (desk_no) VALUES(?)";
                            pstmt= con.prepareStatement(sql);
                            
                            try (FileInputStream fileIn = new FileInputStream(new File(filePath)); 
                                    XSSFWorkbook wb = new XSSFWorkbook(fileIn)) {
                                XSSFSheet sheet = wb.getSheetAt(0);
                                Row row = null;
                                //String desskStringNo = row.getCell(0).getStringCellValue();
                                
                                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                                    
                                    row = sheet.getRow(i);
                                    
                                    if (isRowValueDouble(row)) {
                                        int content = (int) row.getCell(0).getNumericCellValue();
                                        pstmt.setString(1, String.valueOf(content));
                                    } else {
                                        pstmt.setString(1, row.getCell(0).getStringCellValue());
                                    }
                                    pstmt.execute();
                                    
                                    
                                }
                                successNotification(Pos.BOTTOM_RIGHT, "Successfully imported desk data from this file.\n\n" + filePath);
                                
                            }
                                    pstmt.close();
                                    
                               refreshDeskListView();
                            
                        } catch (SQLException | IOException e) {
                            
                            Alert at = new Alert(Alert.AlertType.ERROR);
                            at.setTitle("Error in registration");
                            at.setGraphic(errorImage);
                            at.setHeaderText(null);
                            at.setContentText("Internal server Error occurred and some desk data was NOT REGISTERED. The following errors might have occured \n\n"
                                    + "1. Two or more desk share same desk number noted below."
                                    + " Therefore only one desk will be registered and the other desk(s) after the duplicate will NOT be REGISTERED.\n\n"+e);
                            at.showAndWait(); 
                            
                            refreshDeskListView();
 
                           e.printStackTrace();
                        }

                    }
            }          
        
        
    }
    
    //method to validate if input is an int
    private boolean isRowValueDouble(Row row){
        try {
            double number = row.getCell(0).getNumericCellValue();
            System.out.println("Input is" + number );
            return true;
        } catch (Exception e) {
            return false;
        }
    }    
    

    @FXML
    private void importChairkListOperation(ActionEvent event) {
        
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Import Chairs data");
            alert.setGraphic(schoolSuccess);
            alert.setHeaderText(null);
            alert.setContentText("Only data in Excel format and with an extension of .xlsx can be imported in the correct format.\n\n"
                    + "Are you sure you want to import chairs data from outside the system? ");
           
            Optional<ButtonType> response = alert.showAndWait();            
            if (response.get()==ButtonType.OK) {
                
                excelFileChooserOpener();
                   excelFileChooser.setTitle("Select chairs' data File Name");
                   //single File selection
                   excelFile = excelFileChooser.showOpenDialog(Stage);
                    if (excelFile != null) {
                        String filePath = excelFile.getAbsolutePath();
                        
                        try {
                            String sql = "INSERT INTO chair (chair_no) VALUES(?)";
                            pstmt= con.prepareStatement(sql);
                            
                            try (FileInputStream fileIn = new FileInputStream(new File(filePath)); 
                                XSSFWorkbook wb = new XSSFWorkbook(fileIn)) {
                                XSSFSheet sheet = wb.getSheetAt(0);
                                Row row = null;
                                //String desskStringNo = row.getCell(0).getStringCellValue();
                                
                                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                                    
                                    row = sheet.getRow(i);
                                    
                                    if (isRowValueDouble(row)) {
                                        int content = (int) row.getCell(0).getNumericCellValue();
                                        pstmt.setString(1, String.valueOf(content));
                                    } else {
                                        pstmt.setString(1, row.getCell(0).getStringCellValue());
                                    }
                                    
                                    pstmt.execute();
                                    
                                    
                                }
                                successNotification(Pos.BOTTOM_RIGHT, "Successfully imported chairs data from this file.\n\n" + filePath);
                                
                            }
                                pstmt.close();
                                    
                               refreshChairListView();
                            
                        } catch (SQLException | IOException e) {
                            
                            Alert at = new Alert(Alert.AlertType.ERROR);
                            at.setTitle("Error in registration");
                            at.setGraphic(errorImage);
                            at.setHeaderText(null);
                            at.setContentText("Internal server Error occurred and some Chair data was NOT REGISTERED. The following errors might have occured \n\n"
                                    + "1. Two or more chair share same desk number noted below."
                                    + " Therefore only one chair will be registered and the other chair(s) after the duplicate will NOT be REGISTERED.\n\n"+e);
                            at.showAndWait(); 
                            
                            refreshDeskListView();
 
                           e.printStackTrace();
                        }

                    }
            }          
                
        
    }

    @FXML
    private void deskListMouseClicked(MouseEvent event) {
        
            try {
                String name = deskList.getSelectionModel().getSelectedItem();
                String sql = "SELECT * FROM desk WHERE desk_no = ?  ";
                pstmt = con.prepareStatement(sql);
                pstmt.setString(1, name);
                rs=pstmt.executeQuery();
                
                if (rs.next()) {
                    deskIDHolder = rs.getInt("id");                   
                    String number = rs.getString("desk_no");
                    deskNumber.setText(number);
                      
                }else {                 
                    errorNotification(Pos.BOTTOM_RIGHT, "Record not Found or desk does not Existing!");
                }
            } catch (SQLException e) {
            } finally {
                    try {
                        rs.close();
                        pstmt.close();
                    } catch (Exception e) {
                    }        
               } 
        
    }

    @FXML
    private void chairListMouseClicked(MouseEvent event) {
        
            try {
                String name = chairList.getSelectionModel().getSelectedItem();
                String sql = "SELECT * FROM chair WHERE chair_no = ?  ";
                pstmt = con.prepareStatement(sql);
                pstmt.setString(1, name);
                rs=pstmt.executeQuery();
                
                if (rs.next()) {
                    chairIDHolder = rs.getInt("id");
                    String number = rs.getString("chair_no");
                    chairNumber.setText(number);
                      
                }else {                 
                    errorNotification(Pos.BOTTOM_RIGHT, "Record not Found or chair does not Existing!");
                }
            } catch (SQLException e) {
            } finally {
                    try {
                        rs.close();
                        pstmt.close();
                    } catch (Exception e) {
                    }        
               }         
        
    }
    
}//end of class
