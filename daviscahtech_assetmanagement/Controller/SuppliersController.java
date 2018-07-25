/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daviscahtech_assetmanagement.Controller;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import daviscahtech_assetmanagement.Dao.DatabaseConnection;
import daviscahtech_assetmanagement.Pojo.Suppliers;
import daviscahtech_assetmanagement.Utils.HeaderFooterPageEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class
 *
 * @author davis
 */
public class SuppliersController implements Initializable {
    
    Image errorFlag = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/errorFlag.png").toExternalForm());
    ImageView errorImage = new ImageView(errorFlag);
    
    Image thumbsImage = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/thumbs.png").toExternalForm());
    ImageView imageSuccess = new ImageView(thumbsImage);
    
    Image warehousewoman = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/warehousewoman.png").toExternalForm());
    ImageView supplierImageView = new ImageView(warehousewoman);

    
    Image errorNotify = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/errornotification.png").toExternalForm());
    ImageView errorNotifyImageView = new ImageView(errorNotify);
   
    
    Image goodImage = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/good.png").toExternalForm());
    ImageView successNotiyiImageView = new ImageView(goodImage);
    
    Image studentImage = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/student.png").toExternalForm());
    ImageView pupilImageView = new ImageView(studentImage); 
    
    Image pdfImage = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/pdf.png").toExternalForm());
    ImageView pdfImageView = new ImageView(pdfImage);    
    


    //###############################For DB link##########################
    Connection con = null;
    PreparedStatement pstmt;
    ResultSet rs;
    Statement st;
    //variables to hold school info
    String schoolName = null;
    String schoolContact = null;
    String schoolAddress = null;
    String schoolRegion = null;
    String schoolEmail = null;
    String schoolWebsite = null; 
    
    
    private File file;
    private File excelFile;    
    private File pdfFile;
    private FileChooser fileChooser;
    private FileChooser pdfFileChooser;
    private FileChooser excelFileChooser;
    //instance of javafx image
    private Image image;
    /*
    instance of Desktop and Window
    private final Desktop desktop = Desktop.getDesktop();
    */
    private Window Stage;      
    
    //NOTIFICATION BUILDER VARIABLE
    Notifications notificationBuilder; 
     
    
    private int supplierIDHolder;
    
    @FXML
    private TextField supplier_name;
    @FXML
    private TextArea supplier_desc;
    @FXML
    private TextField supplier_address;
    @FXML
    private TextField supplier_email;
    @FXML
    private TextField supplier_contact;
    @FXML
    private DatePicker supplier_dateApproved;
    @FXML
    private TableView<Suppliers> supplierTable;
    @FXML
    private TableColumn<Suppliers, String> tableColumn_name;
    @FXML
    private TableColumn<Suppliers, String> tableColumn_address;
    @FXML
    private TableColumn<Suppliers, String> tableColumn_email;
    @FXML
    private TableColumn<Suppliers, String> tableColumn_contact;
    @FXML
    private TableColumn<Suppliers, String> tableColumn_dateApproved;
    @FXML
    private TableColumn<Suppliers, String> tableColumn_dateReg;
    private final ObservableList<Suppliers> supplierData = FXCollections.observableArrayList();
    @FXML
    private TextField supplierSearchDetails;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        con = DatabaseConnection.connectDb();
        loadSchoolInfo();
        updateSupplierTable();
    } 
    
        public void pdfPrintedNotification(Pos pos,String title){
       notificationBuilder = Notifications.create()
                .title("PDF Report Printed")
                .text(title)
                .graphic(pdfImageView)
                .hideAfter(Duration.seconds(10))
                .position(pos)
                .onAction((ActionEvent event) -> {
                    System.out.println("Notification created");
                });
        notificationBuilder.show();
                
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
    
    //method to automatically set school information from the database
    private void loadSchoolInfo() {
    
        try {
                 // Get fine ammount to be incurred by retrieving data from the database.                               
                String sql3 = "SELECT * FROM schoolInfo ";
                pstmt = con.prepareStatement(sql3);

                ResultSet rs3=pstmt.executeQuery();
                if (rs3.next()) {
                     schoolName = rs3.getString("name");
                     schoolContact = rs3.getString("contact");
                     schoolAddress = rs3.getString("address");
                     schoolRegion = rs3.getString("region");
                     schoolEmail = rs3.getString("email");
                     schoolWebsite = rs3.getString("website");
                } 
                
                pstmt.close();
            
        } catch (SQLException e) {
        }
    } 

            //Method to open a fileChooser dialog box
    public void pdfFileChooserOpener(){
                pdfFileChooser = new FileChooser();
                pdfFileChooser.getExtensionFilters().addAll(                
                new FileChooser.ExtensionFilter("PDF Files","*.pdf")

                
        );   
    }     
    
       //Method to monitor tenant's profile update on table upon searching
    private void changeSupplierTableonOnSearch(){
        
        //wrap the observablelist in a FilteredList()
        FilteredList<Suppliers> filteredData =  new FilteredList<>(supplierData,p -> true);
        
        //set the filter predicate whenever the filter changes
        supplierSearchDetails.textProperty().addListener((observable,oldValue,newValue)->{
                
            filteredData.setPredicate(Suppliers -> {
            
                //if filter text is empty, display all product
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                
                //compare product id and product name of every product with the filter text
                String lowerCaseFilter =newValue.toLowerCase();
               
                if (Suppliers.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches product Id
                } else if (Suppliers.getAddress().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches product Id
                }  else if (Suppliers.getContacts().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches product Id
                } 
                return false; // Do not match            
            });
        });
        
        //wrap the filteredList in a sortedList
        SortedList<Suppliers> sortedData = new SortedList<>(filteredData);
        
        //bind the sortedData to the Tableview
        sortedData.comparatorProperty().bind(supplierTable.comparatorProperty());
        
        //add sorted and filtered data to the table
        supplierTable.setItems(sortedData); 
    }    

    
    
    //method to validate if input is an int
    private boolean isInt(TextField input){
        input.setStyle("-fx-border-color: green; -fx-border-radius: 15;-fx-background-radius: 20;");
        try {
            int number = Integer.parseInt(input.getText());
            System.out.println("Input is" + number );
            return true;
        } catch (Exception e) {
            input.setStyle("-fx-border-color: red;");
            input.setText(null);
            input.setPromptText("Only digits/numbers must be filled");
            return false;
        }
    }
    
     //method to validate if input is an EMPTY for blue fields
    private boolean isBlueFieldsEmpty(TextField input){
        input.setStyle("-fx-border-color: blue; -fx-border-radius: 15;-fx-background-radius: 20;");
        
        if (input.getText().isEmpty()) {
            input.setStyle("-fx-border-color: red;");
            input.setText(null);
            input.setPromptText("Field is Empty, must be filled");
            return true;
        } else {            
            return false;
        }
        
    }
    
    // method to validate email addresss
    private boolean isValidEmail(TextField input){
    
        Pattern p = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");
        //Pattern p = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z])+");
        Matcher m = p.matcher(input.getText().trim());
        if (m.find() && m.group().equals(input.getText())) {
            return  true;
        } else {
            
            Alert errorIssue = new Alert(Alert.AlertType.ERROR);
            errorIssue.setTitle("Email Validation Error");
            errorIssue.setGraphic(errorImage);
            errorIssue.setHeaderText(null);
            errorIssue.setContentText("The format of email is incorrect. Enter the correct email format address\n");
            errorIssue.showAndWait(); 
            
            return  false;
        }
    } 
    
    private void updateSupplierTable(){
    
        tableColumn_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumn_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        tableColumn_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableColumn_contact.setCellValueFactory(new PropertyValueFactory<>("contacts"));
        tableColumn_dateApproved.setCellValueFactory(new PropertyValueFactory<>("date_approved"));
        tableColumn_dateReg.setCellValueFactory(new PropertyValueFactory<>("date_reg"));

        try { 
            String sql = "SELECT * FROM supplier";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next()){
                
                supplierData.add( new Suppliers(
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("email"),
                        rs.getString("contact"),
                        rs.getString("date_approved"),
                        rs.getString("date_reg")
                ));         
            }
            //load items to the table
            supplierTable.setItems(supplierData);
            supplierTable.setPlaceholder(new Label("No Supplier matches the details provided", errorImage));
            pstmt.close();
            rs.close();
        } catch (SQLException e) {
        } finally {
            try {
                rs.close();
                pstmt.close();
            } catch (Exception e) {
            }        
        }    
        
    }  
    
    private void refreshSupplierTable(){
        supplierData.clear();
        updateSupplierTable();
    }
    

    @FXML
    private void registerSupplierOperation(ActionEvent event) {
        
        if (supplier_contact.getText().isEmpty()
                ||supplier_desc.getText().isEmpty()
                ||supplier_name.getText().isEmpty()
                || supplier_dateApproved.getEditor().getText().isEmpty()) {
             
                 String msg = null;
            
              
                if (supplier_contact.getText().isEmpty() ) {
                   msg = "Supplier contact is empty, you must fill it."; 
                } else if (supplier_desc.getText().isEmpty() ) {
                   msg = "Products supplied by supplier is empty, you must fill it."; 
                } else if (supplier_name.getText().isEmpty() ) {
                  msg = "Supplier name is empty, you must fill it."; 
                } else if (supplier_dateApproved.getEditor().getText().isEmpty() ) {
                   msg = "Date when supplier was approved to start delively is empty, you must fill it."; 
                } 
             
             
            Alert errorIssue = new Alert(Alert.AlertType.ERROR);
            errorIssue.setTitle("Registration Failed");
            errorIssue.setGraphic(errorImage);
            errorIssue.setHeaderText(null);
            errorIssue.setContentText("The supplier details failed registration.\n"
                        + msg);
            errorIssue.showAndWait(); 
            
        }else { 
                            
                //show confirm alert before registering data of issued
               String ms = " Are you sure you want to register supplier, "+supplier_name.getText();           
               Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
               alert.setTitle("Confirm registration");
               alert.setGraphic(supplierImageView);
               alert.setHeaderText(null);
               alert.setContentText(ms);

               Optional<ButtonType> response = alert.showAndWait();            
               if (response.get()==ButtonType.OK) {
                  try {
                      
                        String sql = "INSERT INTO supplier ("
                                       + "name,address,email,contact,supply_desc,date_approved)"
                                       + " VALUES(?,?,?,?,?,?)";
                        pstmt= con.prepareStatement(sql);

                        pstmt.setString(1, supplier_name.getText());
                               
                        if (supplier_address.getText().isEmpty()) {
                          pstmt.setString(2, "No address");
                        } else {
                            String address = supplier_address.getText().trim();
                            
                            if (!address.toUpperCase().startsWith("P.O BOX")) {
                                address = "P.O BOX " + address;
                                pstmt.setString(2, address);
                            } else {
                                pstmt.setString(2, address);                            
                            }
                            
                        }
                        
                        if (supplier_email.getText().isEmpty()) {
                           pstmt.setString(3, "No email");
                        } else {
                            if (isValidEmail(supplier_email)) {
                                pstmt.setString(3, supplier_email.getText());
                            }
                        }
                        
                        pstmt.setString(4, supplier_contact.getText());
                        pstmt.setString(5, supplier_desc.getText().trim()); 
                        pstmt.setString(6, supplier_dateApproved.getEditor().getText().trim()); 

                       int i = pstmt.executeUpdate(); // load data into the database 

                       if (i>0) {
                            //show alert before registering data of employee
                       String msg = " Supplier  "+supplier_name.getText()+ ", has been registered to the system.";
                       Alert al = new Alert(Alert.AlertType.INFORMATION);
                       al.setTitle("confirmation Dialog");
                       al.setGraphic(imageSuccess);
                       al.setHeaderText(null);
                       al.setContentText(msg);
                       al.showAndWait();

                       clearSupplierFields();
                       refreshSupplierTable(); 

                       } else {
                           Alert error = new Alert(Alert.AlertType.ERROR);
                           error.setTitle("Failed");
                           error.setGraphic(errorImage);
                           error.setContentText("OOPS! Could not register supplier due  to internal error.");
                           error.showAndWait();
                       } 
                       pstmt.close();

                   } catch (SQLException ev) {
                       Alert at = new Alert(Alert.AlertType.ERROR);
                       at.setTitle("Error in registration of lost book");
                       at.setGraphic(errorImage);
                       at.setHeaderText(null);
                       at.setContentText("OOPS! Could not register supplier due  to internal error. ");
                       at.showAndWait(); 
                        
                   }finally {
                      try {
                           rs.close();
                           pstmt.close();
                       } catch (Exception e) {}               
                  }
               }      
        }  
                  
    }
    
    private void clearSupplierFields() {
        supplier_address.clear();
        supplier_contact.clear();
        supplier_dateApproved.getEditor().clear();
        supplier_desc.clear();
        supplier_email.clear();
        supplier_name.clear();
        
    }    

    @FXML
    private void updateSupplierOperation(ActionEvent event) {
        
        if (supplier_contact.getText().isEmpty()
                ||supplier_desc.getText().isEmpty()
                ||supplier_name.getText().isEmpty()
                || supplier_dateApproved.getEditor().getText().isEmpty()) {
             
                 String msg = null;
            
              
                if (supplier_contact.getText().isEmpty() ) {
                   msg = "Supplier contact is empty, you must fill it."; 
                } else if (supplier_desc.getText().isEmpty() ) {
                   msg = "Products supplied by supplier is empty, you must fill it."; 
                } else if (supplier_name.getText().isEmpty() ) {
                  msg = "Supplier name is empty, you must fill it."; 
                } else if (supplier_dateApproved.getEditor().getText().isEmpty() ) {
                   msg = "Date when supplier was approved to start delively is empty, you must fill it."; 
                } 
             
             
            Alert errorIssue = new Alert(Alert.AlertType.ERROR);
            errorIssue.setTitle("Update Failed");
            errorIssue.setGraphic(errorImage);
            errorIssue.setHeaderText(null);
            errorIssue.setContentText("The supplier details failed update.\n"
                        + msg);
            errorIssue.showAndWait(); 
            
        }else { 
            
            if (supplierIDHolder <= 0) {
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setTitle("Update error");
               alert.setGraphic(errorImage);
               alert.setHeaderText(null);
               alert.setContentText("You must retrieve the supplier details to be updated");
               alert.showAndWait();

            } else {
            
                            
                //show confirm alert before registering data of issued
               String ms = " Are you sure you want to update details of supplier, "+supplier_name.getText();           
               Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
               alert.setTitle("Confirm registration");
               alert.setGraphic(supplierImageView);
               alert.setHeaderText(null);
               alert.setContentText(ms);

               Optional<ButtonType> response = alert.showAndWait();            
               if (response.get()==ButtonType.OK) {
                  try {
                      
                        String sql = "UPDATE supplier SET name = ?,address = ?,email = ?,contact = ?,supply_desc = ?,date_approved = ?"
                                       + " WHERE id = '"+supplierIDHolder+"' ";
                        pstmt= con.prepareStatement(sql);

                        pstmt.setString(1, supplier_name.getText());
                               
                        if (supplier_address.getText().isEmpty()) {
                          pstmt.setString(2, "No address");
                        } else {
                            String address = supplier_address.getText().trim();
                            
                            if (!address.toUpperCase().startsWith("P.O BOX")) {
                                address = "P.O BOX " + address;
                                pstmt.setString(2, address);
                            } else {
                                pstmt.setString(2, address);                            
                            }
                        }
                        
                        if (supplier_email.getText().isEmpty()) {
                           pstmt.setString(3, "No email");
                        } else {
                            if (isValidEmail(supplier_email)) {
                                pstmt.setString(3, supplier_email.getText());
                            }
                        }
                        
                        pstmt.setString(4, supplier_contact.getText());
                        pstmt.setString(5, supplier_desc.getText().trim()); 
                        pstmt.setString(6, supplier_dateApproved.getEditor().getText().trim()); 

                       int i = pstmt.executeUpdate(); // load data into the database 

                       if (i>0) {
                            //show alert before registering data of employee
                       String msg = " Supplier  "+supplier_name.getText()+ ", has been registered to the system.";
                       Alert al = new Alert(Alert.AlertType.INFORMATION);
                       al.setTitle("confirmation Dialog");
                       al.setGraphic(imageSuccess);
                       al.setHeaderText(null);
                       al.setContentText(msg);
                       al.showAndWait();

                       clearSupplierFields();
                       refreshSupplierTable(); 

                       } else {
                           Alert error = new Alert(Alert.AlertType.ERROR);
                           error.setTitle("Failed");
                           error.setGraphic(errorImage);
                           error.setContentText("OOPS! Could not register supplier due  to internal error.");
                           error.showAndWait();
                       } 
                       pstmt.close();

                   } catch (SQLException ev) {
                       Alert at = new Alert(Alert.AlertType.ERROR);
                       at.setTitle("Error in registration of lost book");
                       at.setGraphic(errorImage);
                       at.setHeaderText(null);
                       at.setContentText("OOPS! Could not register supplier due  to internal error. ");
                       at.showAndWait(); 
                        
                   }finally {
                      try {
                           rs.close();
                           pstmt.close();
                       } catch (Exception e) {}               
                  }
               }
              } 
        }        
        
    }

    @FXML
    private void deletSupplierOperation(ActionEvent event) {
        
          if (supplierIDHolder<=0) {
            
            Alert errorIssue = new Alert(Alert.AlertType.ERROR);
            errorIssue.setTitle("Delete Failed");
            errorIssue.setGraphic(errorImage);
            errorIssue.setHeaderText(null);
            errorIssue.setContentText("Supplier has failed DELETE operation.\n"
                        + "Ensure the supplier's record or details has been retrieved before deleting.");
            errorIssue.showAndWait(); 
            
        }else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete");
            alert.setGraphic(supplierImageView);
            alert.setHeaderText(null);
            alert.setContentText("Are you sure to delete supplier?\n"); 
            
            Optional <ButtonType> obt = alert.showAndWait();

            if (obt.get()== ButtonType.OK) {
               try {
                        String query = "DELETE FROM supplier WHERE id = ? ";
                        pstmt = con.prepareStatement(query);
                        pstmt.setInt(1, supplierIDHolder);
                        pstmt.executeUpdate();

                        pstmt.close();  

                        clearSupplierFields();
                        refreshSupplierTable();
                        
               } catch (SQLException ex) {
                   Logger.getLogger(SuppliersController.class.getName()).log(Level.SEVERE, null, ex);
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
    private void clearSupplierFieldsOperation(ActionEvent event) {
        clearSupplierFields();
    }

    @FXML
    private void supplierTableMouseClicked(MouseEvent event) {
        
                        try {
                    Suppliers sp = supplierTable.getSelectionModel().getSelectedItem();
                    
                    String sql = "SELECT * FROM supplier "
                            + "WHERE name = ? AND contact = ? AND email = ? ";
                    pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, sp.getName());
                    pstmt.setString(2, sp.getContacts());
                    pstmt.setString(3, sp.getEmail());
                    
                    rs=pstmt.executeQuery();
                    
                    if (rs.next()) {
                        
                        supplierIDHolder = rs.getInt("id");
                        
                        String add1 = rs.getString("name");
                        supplier_name.setText(add1); 
                        String add2 = rs.getString("address");
                        supplier_address.setText(add2);
                        String add3 = rs.getString("email");
                        supplier_email.setText(add3);
                        String add4 = rs.getString("contact");
                        supplier_contact.setText(add4);
                        String add5 = rs.getString("supply_desc");
                        supplier_desc.setText(add5);
                        String add6 = rs.getString("date_approved");
                        supplier_dateApproved.getEditor().setText(add6);
                        
                        
                    }else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Invalid Supplier");
                        alert.setHeaderText(null);
                        alert.setContentText("Record not Found!");
                        alert.showAndWait(); 
                    }                    
                } catch (SQLException ev) {
                }
    }

    @FXML
    private void supplierTableKKeyReleased(KeyEvent event) {
        
            supplierTable.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.DOWN) {
                
                try {
                    Suppliers sp = supplierTable.getSelectionModel().getSelectedItem();
                    
                    String sql = "SELECT * FROM supplier "
                            + "WHERE name = ? AND contact = ? AND email = ? ";
                    pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, sp.getName());
                    pstmt.setString(2, sp.getContacts());
                    pstmt.setString(3, sp.getEmail());
                    
                    rs=pstmt.executeQuery();
                    
                    if (rs.next()) {
                        
                        supplierIDHolder = rs.getInt("id");
                        
                        String add1 = rs.getString("name");
                        supplier_name.setText(add1); 
                        String add2 = rs.getString("address");
                        supplier_address.setText(add2);
                        String add3 = rs.getString("email");
                        supplier_email.setText(add3);
                        String add4 = rs.getString("contact");
                        supplier_contact.setText(add4);
                        String add5 = rs.getString("supply_desc");
                        supplier_desc.setText(add5);
                        String add6 = rs.getString("date_approved");
                        supplier_dateApproved.getEditor().setText(add6);
                        
                        
                    }else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Invalid Supplier");
                        alert.setHeaderText(null);
                        alert.setContentText("Record not Found!");
                        alert.showAndWait(); 
                    }                    
                } catch (SQLException ev) {
                }
            }
        });          
        
        
        
    }

    @FXML
    private void refreshTableOperation(ActionEvent event) {
        refreshSupplierTable();
    }

    @FXML
    private void supplierSearchMouseClicked(MouseEvent event) {
        changeSupplierTableonOnSearch();
    }

    @FXML
    private void printSupplierDetailsOperation(ActionEvent event) {        
        // generate pdf
             pdfFileChooserOpener();
               pdfFileChooser.setTitle("Save printed report");
               //single File selection
               pdfFile = pdfFileChooser.showSaveDialog(Stage);                   
                if (pdfFile != null) {
                    String path = pdfFile.getAbsolutePath();

                    createSAupplierPDF(path);
                }  
    }
    
    private  void addMetaData(Document document) {
        document.addTitle( "Daviscah Tech product" );
        document.addSubject( "www.daviscahtech.com" );
        document.addKeywords( "School, software, Books, Students, Teachers" );
        document.addAuthor( "Daviscah Tech Ltd" );
        document.addCreator( "Daviscah Tech Ltd" );
    } 
    
    //Method to creat an empty line in the document
     private static void addEmptyLine(Paragraph paragraph, int number) {
        for ( int i = 0 ; i < number; i++) {
              paragraph.add( new Paragraph( " " ));
        }
    }
     
    // method to format table cell with data
     private void insertCell(PdfPTable table,String text,int align,int colspan,Font font){
     
         PdfPCell cell = new PdfPCell(new Phrase(text.trim(),font));
         cell.setHorizontalAlignment(align);
         cell.setColspan(colspan);
         if (text.trim().equalsIgnoreCase("")) {
             cell.setMinimumHeight(10f);
         }
         table.addCell(cell);
     }    
        
    
    
    public void createSAupplierPDF(String filename){
        
       // create a pdf
       Document document = new Document(PageSize.A4,5,5,20,40);
       
        try {
            
             String sq = "SELECT * FROM  supplier ";
             pstmt = con.prepareStatement(sq);
             rs=pstmt.executeQuery();
            
            Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK);
            Font bfBold = new Font(Font.FontFamily.TIMES_ROMAN, 11);
            
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
            HeaderFooterPageEvent event = new HeaderFooterPageEvent();
            writer.setPageEvent(event);
            
            document.open();
            addMetaData(document);
            
            //school info
            Paragraph intro = new Paragraph(""+schoolName+"\n"
                    + ""+schoolAddress+" "+ schoolRegion+"\n"
                    + "Website: "+ schoolWebsite+"\n"
                    + "School suppliers as at "+ LocalDate.now()+" \n\r",bfBold12);
            intro.setAlignment(Element.ALIGN_CENTER);            
            document.add(intro);
            
            
            
            // Add a table
            float[] columnWidths = {2.7f,2.5f,3f,1.5f,1.5f};
            PdfPTable studentTable = new PdfPTable(columnWidths);
            studentTable.setWidthPercentage(90f);
            
            insertCell(studentTable, "Supplier name", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(studentTable, "Supplier address", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(studentTable, "Supplier email", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(studentTable, "Contact", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(studentTable, "Date approved", Element.ALIGN_LEFT, 1, bfBold12);
            
            while (rs.next()) {
                String code = rs.getString("name");
                insertCell(studentTable, code, Element.ALIGN_LEFT, 1, bfBold);
                
                String name = rs.getString("address");
                insertCell(studentTable, name, Element.ALIGN_LEFT, 1, bfBold); 
                
                String email = rs.getString("email");
                insertCell(studentTable, email, Element.ALIGN_LEFT, 1, bfBold); 
                
                String contact = rs.getString("contact");
                insertCell(studentTable, contact, Element.ALIGN_LEFT, 1, bfBold); 
                
                 String date_approved = rs.getString("date_approved");
                insertCell(studentTable, date_approved, Element.ALIGN_LEFT, 1, bfBold);
                         
            }
            
            document.add(studentTable);
            
            document.close();
            pstmt.close();
            
            pdfPrintedNotification(Pos.BOTTOM_RIGHT, "Successfully printed report and can be found at: \n" + filename);
            
            
        } catch (DocumentException | FileNotFoundException | SQLException ex) {
            Logger.getLogger(TeachersectionController.class.getName()).log(Level.SEVERE, null, ex);
            errorNotification(Pos.BOTTOM_RIGHT,"Report Not Generated");
        } 
        

    }     
    
    
    
    
    


    
}// end of class
