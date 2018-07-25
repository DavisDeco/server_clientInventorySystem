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
import daviscahtech_assetmanagement.Pojo.StockTrace;
import daviscahtech_assetmanagement.Utils.HeaderFooterPageEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
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
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class
 *
 * @author davis
 */
public class CateringsectionController implements Initializable {    Image errorFlag = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/errorFlag.png").toExternalForm());
    ImageView errorImage = new ImageView(errorFlag);
    
    Image errorNotify = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/errornotification.png").toExternalForm());
    ImageView errorNotifyImageView = new ImageView(errorNotify);
    
    Image thumbsImage = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/thumbs.png").toExternalForm());
    ImageView imageSuccess = new ImageView(thumbsImage);    
    
    Image goodImage = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/good.png").toExternalForm());
    ImageView successNotiyiImageView = new ImageView(goodImage);
    
    Image stockedproduct = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/store.png").toExternalForm());
    ImageView stockedproductImageView = new ImageView(stockedproduct); 
    
    Image warning = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/Warning.png").toExternalForm());
    ImageView warningImagevies = new ImageView(warning);
    
    Image pdfImage = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/pdf.png").toExternalForm());
    ImageView pdfImageView = new ImageView(pdfImage);  
    
    Image info = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/info.png").toExternalForm());
    ImageView infoImage = new ImageView(info); 
    
    
    //###############################For DB link##########################  warningImagevies
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
    
       
    //instance of javafx image
    private Image image;
    private Image image2;
    
    //instance of FIlechooser
    private FileChooser fileChooser;
    private FileChooser pdfFileChooser;
    
    //instance of File
    private File file;
    private File pdfFile;
    /*
    instance of Desktop and Window
    private final Desktop desktop = Desktop.getDesktop();
    */
    private Window Stage;
    
    //NOTIFICATION BUILDER VARIABLE
    Notifications notificationBuilder;
    Node graphic;
    
    //id holders
    private int stockTraceProductIDHolder;
    private int stockGeneralIDHolder;
    private int stockForeignKeyIDHolder;
    
    
    private String stockTraceProductDepartment;
    

    @FXML
    private ImageView productImageView;
    @FXML
    private TextField productCode;
    @FXML
    private TextField productName;
    @FXML
    private TextArea productDesc;
    @FXML
    private TextField productQuantity;
    @FXML
    private ComboBox productTypeCombobox;
    ObservableList<String> productTypeData = FXCollections.<String>observableArrayList(
            "Single units","Pairs","Packets","Bunches","Boxes","Cartons","Dozens","Crates",
            "Sets","Bales","Tonnes","Kilograms","Grams","Litres","MiliLitres");
    @FXML
    private TextField productPurchaseCost;
    @FXML
    private ComboBox productSupplierCombobox;
    private final ObservableList supplierData = FXCollections.observableArrayList();
    @FXML
    private DatePicker productStockedDate;
    @FXML
    private ComboBox productMinAlartCombobox;
    private final ObservableList minimumLevelData = FXCollections.observableArrayList();
    @FXML
    private TableView<StockTrace> stockTrace_Table;
    @FXML
    private TableColumn<StockTrace, String> stockTrace_code;
    @FXML
    private TableColumn<StockTrace, String> stockTrace_name;
    @FXML
    private TableColumn<StockTrace, String> stockTrace_quantity;
    @FXML
    private TableColumn<StockTrace, String> stockTrace_type;
    @FXML
    private TableColumn<StockTrace, String> stockTrace_cost;
    @FXML
    private TableColumn<StockTrace, String> stockTrace_supplier;
    @FXML
    private TableColumn<StockTrace, String> stockTrace_stockedDate;
    private final ObservableList<StockTrace> stockTraceData = FXCollections.observableArrayList();
    @FXML
    private Text displayProductType;
    @FXML
    private Text displayProductSupplier;
    @FXML
    private Text displayMinimumQuantityLevel;    
    @FXML
    private TextField searchOnTableField;
    @FXML
    private DatePicker unitsStockedSpecificDate;
    @FXML
    private DatePicker startDateStock;
    @FXML
    private DatePicker endDateStock;
    @FXML
    private DatePicker printStockOnSpecificDate;
    @FXML
    private DatePicker printStartDate;
    @FXML
    private DatePicker printEndDate;
    @FXML
    private DatePicker deleteStartDate;
    @FXML
    private DatePicker deleteEndDate;
    
   

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        con = DatabaseConnection.connectDb();
        
        // call method to load variables with school info
        loadSchoolInfo();
        
        //populate table
        updateStockTraceTable();
        
        //populate comboboxes
        productTypeCombobox.setItems(productTypeData);
        updateSupplierNamesCombo();
        updateMinLevelCombo();
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
               
                //////////////////////////////////// 
    }    
    
       //Method to monitor tenant's profile update on table upon searching
    private void changeStockTraceonOnSearch(){
        
        //wrap the observablelist in a FilteredList()
        FilteredList<StockTrace> filteredData =  new FilteredList<>(stockTraceData,p -> true);
        
        //set the filter predicate whenever the filter changes
        searchOnTableField.textProperty().addListener((observable,oldValue,newValue)->{
                
            filteredData.setPredicate(StockTrace -> {
            
                //if filter text is empty, display all product
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                
                //compare product id and product name of every product with the filter text
                String lowerCaseFilter =newValue.toLowerCase();
               
                if (StockTrace.getProductCode().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches product Id
                } else if (StockTrace.getProductName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches product Id
                }  else if (StockTrace.getProductSupplier().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches product Id
                }  else if (StockTrace.getProductCost().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches product Id
                }  
                return false; // Do not match            
            });
        });
        
        //wrap the filteredList in a sortedList
        SortedList<StockTrace> sortedData = new SortedList<>(filteredData);
        
        //bind the sortedData to the Tableview
        sortedData.comparatorProperty().bind(stockTrace_Table.comparatorProperty());
        
        //add sorted and filtered data to the table
        stockTrace_Table.setItems(sortedData); 
    }     
    
    
    private void updateStockTraceTable(){
        
        stockTrace_code.setCellValueFactory(new PropertyValueFactory<>("productCode"));
        stockTrace_name.setCellValueFactory(new PropertyValueFactory<>("productName"));
        stockTrace_quantity.setCellValueFactory(new PropertyValueFactory<>("productQuantity"));
        stockTrace_type.setCellValueFactory(new PropertyValueFactory<>("productType"));
        stockTrace_cost.setCellValueFactory(new PropertyValueFactory<>("productCost"));
        stockTrace_supplier.setCellValueFactory(new PropertyValueFactory<>("productSupplier"));
        stockTrace_stockedDate.setCellValueFactory(new PropertyValueFactory<>("productDateStocked"));

        try { 
            String sql = "SELECT * FROM stocktrace WHERE department = ? ";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, "Catering");
            rs = pstmt.executeQuery();
            while(rs.next()){
                
                stockTraceData.add( new StockTrace(
                        rs.getString("product_code"),
                        rs.getString("product_name"),
                        rs.getString("quantity_in"),
                        rs.getString("type"),
                        rs.getString("purchase_cost"),
                        rs.getString("supplier"),
                        rs.getString("date_bought")
                ));         
            }
            //load items to the table
            stockTrace_Table.setItems(stockTraceData);
            stockTrace_Table.setPlaceholder(new Label("No stocked product matches the details provided", errorImage));
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
    
    private void updateStockTraceTableBySpecificDate(String date){
        
        if (unitsStockedSpecificDate.getEditor().getText().isEmpty()) {
            Alert em = new Alert(Alert.AlertType.ERROR);
            em.setTitle("No selection");
            em.setHeaderText(null);
            em.setGraphic(errorImage);
            em.setContentText("You must choose a date");
            em.showAndWait();
        } else {
        
        
        //clear all data from collection first
        stockTraceData.clear();
        
        stockTrace_code.setCellValueFactory(new PropertyValueFactory<>("productCode"));
        stockTrace_name.setCellValueFactory(new PropertyValueFactory<>("productName"));
        stockTrace_quantity.setCellValueFactory(new PropertyValueFactory<>("productQuantity"));
        stockTrace_type.setCellValueFactory(new PropertyValueFactory<>("productType"));
        stockTrace_cost.setCellValueFactory(new PropertyValueFactory<>("productCost"));
        stockTrace_supplier.setCellValueFactory(new PropertyValueFactory<>("productSupplier"));
        stockTrace_stockedDate.setCellValueFactory(new PropertyValueFactory<>("productDateStocked"));

        try { 
            String sql = "SELECT * FROM stocktrace WHERE date_bought = ? AND department = ? ";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, date);
            pstmt.setString(2, "Catering");
            rs = pstmt.executeQuery();
            while(rs.next()){
                                
                stockTraceData.add( new StockTrace(
                        rs.getString("product_code"),
                        rs.getString("product_name"),
                        rs.getString("quantity_in"),
                        rs.getString("type"),
                        rs.getString("purchase_cost"),
                        rs.getString("supplier"),
                        rs.getString("date_bought")
                ));         
            }
            //load items to the table
            stockTrace_Table.setItems(stockTraceData);
            
            stockTrace_Table.setPlaceholder(new Label("No stocked product matches the details provided", errorImage));
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
    }
    
    private void updateStockTraceTableBetweenDates(String startDate, String endDate){
        
        if (startDateStock.getEditor().getText().isEmpty() || endDateStock.getEditor().getText().isEmpty()) {
            
            String msg = null;
            if (startDateStock.getEditor().getText().isEmpty()) {
                msg = "You must choose a start date.";
            } else if (endDateStock.getEditor().getText().isEmpty()) {
                msg = "You must choose an end date.";
            }
            
            Alert em = new Alert(Alert.AlertType.ERROR);
            em.setTitle("No selection");
            em.setHeaderText(null);
            em.setGraphic(errorImage);
            em.setContentText(msg);
            em.showAndWait();
            
        } else {
        }
        
        //clear all data from collection first
        stockTraceData.clear();
        
        stockTrace_code.setCellValueFactory(new PropertyValueFactory<>("productCode"));
        stockTrace_name.setCellValueFactory(new PropertyValueFactory<>("productName"));
        stockTrace_quantity.setCellValueFactory(new PropertyValueFactory<>("productQuantity"));
        stockTrace_type.setCellValueFactory(new PropertyValueFactory<>("productType"));
        stockTrace_cost.setCellValueFactory(new PropertyValueFactory<>("productCost"));
        stockTrace_supplier.setCellValueFactory(new PropertyValueFactory<>("productSupplier"));
        stockTrace_stockedDate.setCellValueFactory(new PropertyValueFactory<>("productDateStocked"));

        try { 
            String sql = "SELECT * FROM stocktrace WHERE date_bought BETWEEN  '"+startDate+"'  AND  '"+endDate+"'  AND  department = ? ";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, "Catering");
            rs = pstmt.executeQuery();
            while(rs.next()){
                
                stockTraceData.add( new StockTrace(
                        rs.getString("product_code"),
                        rs.getString("product_name"),
                        rs.getString("quantity_in"),
                        rs.getString("type"),
                        rs.getString("purchase_cost"),
                        rs.getString("supplier"),
                        rs.getString("date_bought")
                ));         
            }
            //load items to the table
            stockTrace_Table.setItems(stockTraceData);
            stockTrace_Table.setPlaceholder(new Label("No stocked product matches the details provided", errorImage));
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
    
    private void refreshStockTraceTable(){
    
        stockTraceData.clear();
        updateStockTraceTable();
    }
    
    
    
    private void setDefaultImage(){    
        Image defaultImage = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/store.png").toExternalForm(),productImageView.getFitWidth(),productImageView.getFitHeight(),false,true);
        productImageView.setImage(defaultImage); 
    }
    
    
    
       //Method to open a fileChooser dialog box
    public void fileChooserOpener(){
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files","*.*"),
                new FileChooser.ExtensionFilter("Text Files","*txt"),
                new FileChooser.ExtensionFilter("Image Files","*.png","*.jpg","*.gif"),
                new FileChooser.ExtensionFilter("Audio Files","*wav","*.mp3","*.mp4","*.acc")  
//               new FileChooser.ExtensionFilter("All Files","*.*")        
//               new FileChooser.ExtensionFilter("Excel Files","*.xslx") 
//                new FileChooser.ExtensionFilter("Text Files","*.txt"),
//                new FileChooser.ExtensionFilter("Word Files","*.docx"),
//                new FileChooser.ExtensionFilter("Image Files","*.png","*.jpg","*.gif"),
//                new FileChooser.ExtensionFilter("Audio Files","*.wav","*.mp3","*.mp4","*.acc") 
                
        );   
    }
    
            //Method to open a fileChooser dialog box
        public void pdfFileChooserOpener(){
                pdfFileChooser = new FileChooser();
                pdfFileChooser.getExtensionFilters().addAll(                
                new FileChooser.ExtensionFilter("PDF Files","*.pdf")

                
        );   
    }    
    
   private void updateSupplierNamesCombo(){  
        try { 
            String sql = "SELECT name FROM supplier";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next()){                
                supplierData.add( rs.getString("name")); 
            }
            // populate class combo box 
            productSupplierCombobox.setItems(supplierData);
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
   
   private void refreshSupplierCombo(){   
       supplierData.clear();
       updateSupplierNamesCombo();
   } 
   
   private void updateMinLevelCombo() {

        for (int i = 1; i < 1000; i++) {
            minimumLevelData.add(i);
        }
        productMinAlartCombobox.setItems(minimumLevelData);
    }
   
   private void refreshAlartLevelCombo(){
       minimumLevelData.clear();
       updateMinLevelCombo();
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
            
                Alert errorIssue = new Alert(Alert.AlertType.WARNING);
                errorIssue.setTitle("Warning");
                errorIssue.setGraphic(warningImagevies);
                errorIssue.setHeaderText(null);
                errorIssue.setContentText("Enter only digits/numbers in red highlighted fields.\n");
                errorIssue.showAndWait();
            
            return false;
        }
    }   
    
    
    ///////////// other methods ////////////    
    

    @FXML
    private void chooseProductPhotoOperation(ActionEvent event) {
        
        
        fileChooserOpener();
           fileChooser.setTitle("Select product image");
           //single File selection
           file = fileChooser.showOpenDialog(Stage);
            if (file != null) {
               String studentImagePath = file.getAbsolutePath();
               try {
                   image = new Image(new FileInputStream(studentImagePath),productImageView.getFitWidth(),productImageView.getFitHeight(),false,true);
                   productImageView.setImage(image);                  
               } catch (FileNotFoundException ex) {
                   Logger.getLogger(TeachersectionController.class.getName()).log(Level.SEVERE, null, ex);
               }            
            }         
        
        
    }

    @FXML
    private void registerProductOperation(ActionEvent event) {
        
        if (productName.getText().isEmpty() 
                || productDesc.getText().isEmpty()
                || productQuantity.getText().isEmpty()
                ||productTypeCombobox.getSelectionModel().isEmpty()
                ||productSupplierCombobox.getSelectionModel().isEmpty()
                ||productMinAlartCombobox.getSelectionModel().isEmpty()
                ||productStockedDate.getEditor().getText().isEmpty()) {

                String msg = null;

                if (productName.getText().isEmpty()) {
                    msg = "Product(s) name is empty, you must enter it";
                } else if (productDesc.getText().isEmpty()) {
                    msg = "Products' short description is empty, you must enter it";
                }  else if (productQuantity.getText().isEmpty()) {
                    msg = "Quantity of product being stocked is empty, you must enter it";
                } else if (productTypeCombobox.getSelectionModel().isEmpty()) {
                    msg = "Quantitative type,size or nature of product is empty, you must choose from the drop-down menu";
                } else if (productSupplierCombobox.getSelectionModel().isEmpty()) {
                    msg = "Product suppliet is empty, you must choose from the drop-down menu.\n"
                    + " Ensure the supplier is first registered in order to be displayedin the drop-down menu";
                } else if (productMinAlartCombobox.getSelectionModel().isEmpty()) {
                    msg = "Produc's minimum quantity level for alart notification is empty, you must choose from the drop-down menu";
                } else if (productStockedDate.getEditor().getText().isEmpty()) {
                    msg = "Date product was stocked is empty, you must choose it.";
                }

                Alert errorIssue = new Alert(Alert.AlertType.ERROR);
                errorIssue.setTitle("Registration Failed");
                errorIssue.setGraphic(errorImage);
                errorIssue.setHeaderText(null);
                errorIssue.setContentText("The product has failed registration.\n"
                + msg);
                errorIssue.showAndWait();
        
        }else {
            
            if (isInt(productQuantity) && isInt(productPurchaseCost)) {
                
            
                //show confirm alert before registering data of issued
                String ms = " Are you sure you want to register product (s) = "+productName.getText();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm registration");
                alert.setGraphic(stockedproductImageView);
                alert.setHeaderText(null);
                alert.setContentText(ms);

                Optional<ButtonType> response = alert.showAndWait();
                if (response.get()==ButtonType.OK) {
                    try {
                        String sql = "INSERT INTO stocktrace ("
                        + "product_code,product_name,product_desc,quantity_in,type,date_bought,purchase_cost,supplier,department)"
                        + " VALUES(?,?,?,?,?,?,?,?,?)";
                        pstmt= con.prepareStatement(sql);

                            if (productCode.getText().isEmpty()) {
                                pstmt.setString(1, "Codeless");
                            } else {
                                pstmt.setString(1, productCode.getText());
                            }        
                        pstmt.setString(2, productName.getText());
                        pstmt.setString(3, productDesc.getText());
                        pstmt.setInt(4, Integer.parseInt(productQuantity.getText().trim()));                        
                        pstmt.setString(5, productTypeCombobox.getSelectionModel().getSelectedItem().toString());
                        pstmt.setString(6, productStockedDate.getEditor().getText().trim());
                        
                        if (productPurchaseCost.getText().isEmpty()) {
                            pstmt.setDouble(7, 0.00);
                        } else {
                            pstmt.setDouble(7, Double.parseDouble(productPurchaseCost.getText().trim()));
                        }                     
                        
                        pstmt.setString(8, productSupplierCombobox.getSelectionModel().getSelectedItem().toString());
                        pstmt.setString(9, "Catering");

                        int i = pstmt.executeUpdate(); // load data into the database
                        if (i>0) {
                           // String productCode,String ProductName,String dateIn,String department
                            int newProductId = retrieveIDofRegisteredStock( productName.getText(),
                                                                            productStockedDate.getEditor().getText().trim(),
                                                                            "Catering");
                            
                            System.out.println("Retrieved id is " + newProductId);
                            
                            if (newProductId <= 0) {
                                System.out.println("ID OF THE NEW REGISTERED PRODUCT IS LESS THAN OR EQUAL TO 0");
                            } else {
                            
                                // save the product details to the general stock table
                                try {
                                    String sqlString = "INSERT INTO stock ("
                                                + "product_pk_fk,product_code,product_name,product_desc,"
                                                + "quantity_avail,type,total_cost,min_quantity,department,imagepath)"
                                                + " VALUES(?,?,?,?,?,?,?,?,?,?)";

                                    pstmt= con.prepareStatement(sqlString);

                                    pstmt.setInt(1, newProductId);

                                        if (productCode.getText().isEmpty()) {
                                            pstmt.setString(2, "Codeless");
                                        } else {
                                            pstmt.setString(2, productCode.getText());
                                        } 

                                    pstmt.setString(3, productName.getText());
                                    pstmt.setString(4, productDesc.getText());
                                    pstmt.setInt(5, Integer.parseInt(productQuantity.getText().trim()));
                                    pstmt.setString(6, productTypeCombobox.getSelectionModel().getSelectedItem().toString());
                                    
                                    if (productPurchaseCost.getText().isEmpty()) {
                                        pstmt.setDouble(7, 0.00);
                                    } else {
                                        pstmt.setDouble(7, Double.parseDouble(productPurchaseCost.getText().trim()));
                                    }
                                    
                                    pstmt.setInt(8, Integer.parseInt(productMinAlartCombobox.getSelectionModel().getSelectedItem().toString()));
                                    pstmt.setString(9, "Catering");


                                    //save a file path to database
                                     if (file == null) {
                                        String imagePath = "C:\\A_Check\\Store_photos\\default.png";
                                        pstmt.setString(10, imagePath);
                                    } else {
                                        String filePath = file.getAbsolutePath();
                                        pstmt.setString(10, filePath);
                                    }
                                     
                                    int ii = pstmt.executeUpdate(); // load data into the database 

                                    if (ii>0) {

                                        //show alert before registering data of employee
                                        String msg = " Product "+productName.getText()+ " has been registered to the system.";
                                        Alert al = new Alert(Alert.AlertType.INFORMATION);
                                        al.setTitle("Registration Information Dialog");
                                        al.setGraphic(imageSuccess);
                                        al.setHeaderText(null);
                                        al.setContentText(msg);
                                        al.showAndWait();

                                        clearinputFields();
                                        refreshStockTraceTable();
                                    }

                                    } catch (Exception e) {
                                }
                            
                          }

                        } else {
                        Alert error = new Alert(Alert.AlertType.ERROR);
                        error.setTitle("Failed");
                        error.setGraphic(errorImage);
                        error.setContentText("OOPS! Could not register product due  to internal error.");
                        error.showAndWait();
                        }
                        pstmt.close();

                    } catch (Exception e) {
                        Alert at = new Alert(Alert.AlertType.ERROR);
                        at.setTitle("Error");
                        at.setGraphic(errorImage);
                        at.setHeaderText(null);
                        at.setContentText("OOPS! Could not register product due  to internal error. " +e);
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
    private void increaseStockOfStockedUnitOperation(ActionEvent event) {
        
        if (stockTraceProductIDHolder <= 0) {
              Alert at = new Alert(Alert.AlertType.ERROR);
              at.setTitle("Error");
              at.setGraphic(errorImage);
              at.setHeaderText(null);
              at.setContentText("Search and retrieve the stored product(s) which you wish to increase its stock level");
              at.showAndWait(); 
              
              System.out.println("stockTraceProductIDHolder is " + stockTraceProductIDHolder);
            
        } else {
            
                if (productName.getText().isEmpty() 
                        || productDesc.getText().isEmpty()
                        || productQuantity.getText().isEmpty()
                        ||productStockedDate.getEditor().getText().isEmpty()) {

                        String msg = null;

                        if (productName.getText().isEmpty()) {
                            msg = "Product(s) name is empty, you must enter it";
                        } else if (productDesc.getText().isEmpty()) {
                            msg = "Products' short description is empty, you must enter it";
                        }  else if (productQuantity.getText().isEmpty()) {
                            msg = "New Quantity of product being increased is empty, you must enter it";
                        }  else if (productStockedDate.getEditor().getText().isEmpty()) {
                            msg = "Date of newly increased product(s) is empty, you must choose it.";
                        }

                        Alert errorIssue = new Alert(Alert.AlertType.INFORMATION);
                        errorIssue.setTitle("Information & Errors");
                        errorIssue.setGraphic(infoImage);
                        errorIssue.setHeaderText(null);
                        errorIssue.setContentText("To increase Quantity of this registered product,"
                                + " other details remain the same but ensure to fill the following.\n"
                                + "\t Quantity amount to increase by\n"
                                + "\t Date when Quanity was increased/stocked\n"
                                + "\t Total purchase Cost of all quantity increased (Optional)\n\n"
                        + msg);
                        errorIssue.showAndWait();

                }else {
                    if (isInt(productQuantity)|| isInt(productPurchaseCost)) {
                        
                        //show confirm alert before registering data of issued
                        String ms = " Are you sure you want to increase quantity of product(s) = "+productName.getText();
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirm ");
                        alert.setGraphic(stockedproductImageView);
                        alert.setHeaderText(null);
                        alert.setContentText(ms);

                        Optional<ButtonType> response = alert.showAndWait();
                        if (response.get()==ButtonType.OK) {                        

                            try {
                                
                                 String sqlretrival = "SELECT * FROM stock WHERE product_pk_fk = ? ";
                                 pstmt = con.prepareStatement(sqlretrival);
                                 pstmt.setInt(1, stockTraceProductIDHolder);

                                 ResultSet rss=pstmt.executeQuery();
                                  if (rss.next()) {
                                      
                                      int quantity = rss.getInt("quantity_avail");
                                      double cost = rss.getDouble("total_cost");
                                      registerToStockTraceAndUpdateStock(quantity,cost);
                                  } else {
                                      String err = "This stock record was used to increase stock and can not be reused for same purpose\n"
                                              + "Use its parent stock record that was initially registered";
                                      errorNotification(Pos.BOTTOM_RIGHT, err);
                                  }           
                                  
                            } catch (Exception e) {

                            }
                        }   
                    }
                
                }  
        }
        
    }    
    
    private void registerToStockTraceAndUpdateStock(int qnt,double cst){
        
                String star = " *";
    
                    try {
                        String sql = "INSERT INTO stocktrace ("
                                + "product_code,product_name,product_desc,"
                                + "quantity_in,type,date_bought,purchase_cost,supplier,department)"
                                + " VALUES(?,?,?,?,?,?,?,?,?)";
                        pstmt= con.prepareStatement(sql);

                            if (productCode.getText().isEmpty()) {
                                pstmt.setString(1, "Codeless" );
                            } else {
                                pstmt.setString(1, productCode.getText());
                            }        
                        pstmt.setString(2, productName.getText() + star);
                        pstmt.setString(3, productDesc.getText());
                        pstmt.setInt(4, Integer.parseInt(productQuantity.getText().trim()));   
                        
                        if (productTypeCombobox.getSelectionModel().isEmpty()) {
                            pstmt.setString(5, displayProductType.getText());
                        } else {
                            pstmt.setString(5, productTypeCombobox.getSelectionModel().getSelectedItem().toString());
                        }
                        
                        pstmt.setString(6, productStockedDate.getEditor().getText().trim());
                        
                        if (productPurchaseCost.getText().isEmpty()) {
                            pstmt.setDouble(7, 0.00);
                        } else {
                            pstmt.setDouble(7, Double.parseDouble(productPurchaseCost.getText().trim()));
                        }                     
                        
                        if (productSupplierCombobox.getSelectionModel().isEmpty()) {
                            pstmt.setString(8, displayProductSupplier.getText() + star);
                        } else {
                            pstmt.setString(8, productSupplierCombobox.getSelectionModel().getSelectedItem().toString() + star);
                        }
                        
                        pstmt.setString(9, "Catering");

                        int i = pstmt.executeUpdate(); // load data into the database
                        
                        if (i>0) {
                            
                           // List<Integer> results = availableQuantityAndCostOfProduct();
                            int availableQuantity = qnt;
                            double totalCost = cst;
                            
                            int addedQuantity = Integer.parseInt(productQuantity.getText());
                            int addedCost = 0;
                            
                            if (productPurchaseCost.getText().isEmpty()) {
                                addedCost = 0;
                            } else {
                                addedCost = Integer.parseInt(productPurchaseCost.getText());
                            }
                            
                            int finalNewQuantity = availableQuantity + addedQuantity;
                            int finalNewTotalCost = (int) (totalCost + addedCost) ;
                            
                                // save the product details to the general stock table
                                try {
                                    String sqlString = "UPDATE stock SET product_code = ?,product_name = ?,product_desc = ?,"
                                                + "quantity_avail = ?,type = ?,total_cost = ?,min_quantity = ?,department = ?,imagepath = ? "
                                                + " WHERE product_pk_fk = '"+stockTraceProductIDHolder+"' ";

                                    pstmt= con.prepareStatement(sqlString);

                                        if (productCode.getText().isEmpty()) {
                                            pstmt.setString(1, "Codeless");
                                        } else {
                                            pstmt.setString(1, productCode.getText());
                                        } 

                                    pstmt.setString(2, productName.getText());
                                    pstmt.setString(3, productDesc.getText());
                                    pstmt.setInt(4, finalNewQuantity);
                                    
                                    if (productTypeCombobox.getSelectionModel().isEmpty()) {
                                        pstmt.setString(5, displayProductType.getText());
                                    } else {
                                        pstmt.setString(5, productTypeCombobox.getSelectionModel().getSelectedItem().toString());
                                    }
                                    
                                    pstmt.setDouble(6, finalNewTotalCost);
                                    
                                    if (productMinAlartCombobox.getSelectionModel().isEmpty()) {
                                        pstmt.setInt(7, Integer.parseInt(displayMinimumQuantityLevel.getText()));
                                    } else {
                                        pstmt.setInt(7, Integer.parseInt(productMinAlartCombobox.getSelectionModel().getSelectedItem().toString()));
                                    }
                                    
                                    pstmt.setString(8, "Catering");


                                    //save a file path to database
                                     if (file == null) {
                                        String imagePath = "C:\\A_Check\\Store_photos\\default.png";
                                        pstmt.setString(9, imagePath);
                                    } else {
                                        String filePath = file.getAbsolutePath();
                                        pstmt.setString(9, filePath);
                                    }
                                     
                                    int ii = pstmt.executeUpdate(); // load data into the database 

                                    if (ii>0) {
                                        //show alert before registering data of employee
                                        String msg = " Product "+productName.getText()+ "'s quantity level has been increased to the system.";
                                        successNotification(Pos.BOTTOM_RIGHT, msg);

                                        clearinputFields();
                                        refreshStockTraceTable();
                                    }

                                    } catch (SQLException | NumberFormatException e) {
                                }
                           

                        } else {
                        Alert error = new Alert(Alert.AlertType.ERROR);
                        error.setTitle("Failed");
                        error.setGraphic(errorImage);
                        error.setContentText("OOPS! Could not increase product due  to internal error.");
                        error.showAndWait();
                        }
                        pstmt.close();

                    } catch (SQLException | NumberFormatException e) {
                        Alert at = new Alert(Alert.AlertType.ERROR);
                        at.setTitle("Error");
                        at.setGraphic(errorImage);
                        at.setHeaderText(null);
                        at.setContentText("OOPS! Could not update product due  to internal error. " +e);
                        at.showAndWait();
                    }finally {
                    try {
                        rs.close();
                        pstmt.close();
                    } catch (Exception e) {}
                    }  
    
    }    
    
    // method to retrieve pk of inserted product and add it as foreign key
    private int retrieveIDofRegisteredStock(String name,String dateIn,String department){
       
        try {
             String sqlretrival = "SELECT id FROM stocktrace WHERE product_name = ? AND date_bought = ?  AND department = ? ";
             pstmt = con.prepareStatement(sqlretrival);
             pstmt.setString(1, name);
             pstmt.setString(2, dateIn);
             pstmt.setString(3, department);

             ResultSet rss=pstmt.executeQuery();
              if (rss.next()) {
                  int id = rss.getInt("id");                  
                  return id;
              } else {
                  return -1;
              }           
        } catch (Exception e) {
            return -1;
            
        }
       
    
    }
    
    private void clearinputFields(){
        
        productCode.clear();
        productDesc.clear();
        productName.clear();
        productPurchaseCost.clear();
        productQuantity.clear();
        productStockedDate.getEditor().clear();
        displayMinimumQuantityLevel.setText(null);
        displayProductSupplier.setText(null);
        displayProductType.setText(null);
        unitsStockedSpecificDate.getEditor().clear();
        startDateStock.getEditor().clear();
        endDateStock.getEditor().clear();
        refreshSupplierCombo();
        refreshAlartLevelCombo();
        setDefaultImage();
    }

    @FXML
    private void updteProductOperation(ActionEvent event) {
        
        List<Integer> results = availableQuantityAndCostOfProduct();
        
        if (results.isEmpty()) {
            System.out.println("Empty results from list");
        } else {
            
                if (productName.getText().isEmpty() 
                        || productDesc.getText().isEmpty()
                        || productQuantity.getText().isEmpty()
                        ||productStockedDate.getEditor().getText().isEmpty()) {

                        String msg = null;

                        if (productName.getText().isEmpty()) {
                            msg = "Product(s) name is empty, you must enter it";
                        } else if (productDesc.getText().isEmpty()) {
                            msg = "Products' short description is empty, you must enter it";
                        }  else if (productQuantity.getText().isEmpty()) {
                            msg = "Quantity of product being stocked is empty, you must enter it";
                        } else if (productStockedDate.getEditor().getText().isEmpty()) {
                            msg = "Date product was stocked is empty, you must choose it.";
                        }

                        Alert errorIssue = new Alert(Alert.AlertType.ERROR);
                        errorIssue.setTitle("Update Failed");
                        errorIssue.setGraphic(errorImage);
                        errorIssue.setHeaderText(null);
                        errorIssue.setContentText("The product has failed update.\n"
                        + msg);
                        errorIssue.showAndWait();

                }else {
                    
                        //from ui
                        int updatedQuantity = 0;
                        double updatedPurchaseCost = 0;
                        
                        if (isInt(productQuantity) || isInt(productPurchaseCost) ) {
                            updatedQuantity = Integer.parseInt(productQuantity.getText());
                            updatedPurchaseCost = Double.parseDouble(productPurchaseCost.getText());
                        } 
                        
                        //from stock general table
                        int totalQuantity = results.get(0);
                        int totalCost = results.get(1);
                        // final variables to the stock general table
                        int finalTotalQuantity = 0;
                        int finalTotalCost = 0;
                        
                        //from stocktrace table
                        int currentQuantity = 0;
                        int currentPurchaseCost = 0;  
                        
                            //get the current quantity and purchase cost in stocktrace
                            try {
                                String sql = "SELECT * FROM stocktrace WHERE id = ? ";
                                pstmt = con.prepareStatement(sql);
                                pstmt.setInt(1, stockTraceProductIDHolder);

                                rs = pstmt.executeQuery();

                                if (rs.next()) {
                                   currentQuantity = rs.getInt("quantity_in");
                                   currentPurchaseCost = rs.getInt("purchase_cost");


                                }else {
                                }                    
                            } catch (SQLException ev) {
                            } 
                            
                           //################## Logic for quantity change ###################
                        if (updatedQuantity > currentQuantity) {
                            
                            int increasedStockBy = updatedQuantity - currentQuantity;
                            System.out.println("increased by " + increasedStockBy);
                            
                            finalTotalQuantity = totalQuantity + increasedStockBy;                            
                            System.out.println("Total stock " + finalTotalQuantity);

                        } else if (currentQuantity > updatedQuantity) {
                            
                            int decreasedStockBy = currentQuantity - updatedQuantity;
                            System.out.println("Decreased by " + decreasedStockBy);
                            
                            finalTotalQuantity = totalQuantity - decreasedStockBy;
                            System.out.println("Total stock " + finalTotalQuantity);
                        
                        } else if (updatedQuantity == currentQuantity) {
                            
                            finalTotalQuantity = totalQuantity;
                            System.out.println("Total stock " + finalTotalQuantity);                        
                        }
                        
                            //################## Logic for price change ###################
                        if (updatedPurchaseCost > currentPurchaseCost) {
                            
                            int increasedPriceBy = (int) (updatedPurchaseCost - currentPurchaseCost);
                            System.out.println("Price increased by " + increasedPriceBy);
                            
                            finalTotalCost = totalCost + increasedPriceBy;
                            System.out.println("Total cost" + finalTotalCost);
                        
                        } else if (currentPurchaseCost > updatedPurchaseCost) {
                            
                            int decreasedPriceBy = (int) (currentPurchaseCost - updatedPurchaseCost);
                            System.out.println("Price decreased by " + decreasedPriceBy);
                            
                            finalTotalCost = totalCost - decreasedPriceBy;
                            System.out.println("Total cost" + finalTotalCost);
                        
                        } else if (updatedPurchaseCost == currentPurchaseCost) {
                            
                            finalTotalCost = totalCost;
                            System.out.println("Total cost" + finalTotalCost);                        
                        }
                     //persist changes to table   
                    updateTableLogic(updatedQuantity, updatedPurchaseCost, finalTotalQuantity, finalTotalCost);
                }  
        }
    }
    
    private void updateTableLogic(int newQuantity,double newPurchaseCost,int finalQuantity,double finalCost){
    
                //show confirm alert before registering data of issued
                String ms = "WARNING!!\n"
                        + "You can only update details of this product(s) IF ONLY it has NOT been issued out of the store."
                        + "Updating details of this product(s) after some of the units have been issued will lead to undesired cross- cutting concerns. \n\n"
                        + "If some units were issued out of the store and still wish to update specific details of this product(s), MARK FOR DELETE to the "
                        + "system admin and later re-register the product(s) with the desired updates and other configuration related to it.\n\n "
                        + " Are you sure you want to update product(s) = "+productName.getText();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm update");
                alert.setGraphic(stockedproductImageView);
                alert.setHeaderText(null);
                alert.setContentText(ms);

                Optional<ButtonType> response = alert.showAndWait();
                if (response.get()==ButtonType.OK) {
                    try {
                        String sql = "UPDATE stocktrace SET product_code = ?,product_name = ?,product_desc = ?,"
                                + "quantity_in = ?,type = ?,date_bought = ?,purchase_cost = ?,supplier = ?,department = ? "
                                + "WHERE id = '"+stockTraceProductIDHolder+"' ";
                        pstmt= con.prepareStatement(sql);

                            if (productCode.getText().isEmpty()) {
                                pstmt.setString(1, "Codeless");
                            } else {
                                pstmt.setString(1, productCode.getText());
                            }        
                        pstmt.setString(2, productName.getText());
                        pstmt.setString(3, productDesc.getText());
                        pstmt.setInt(4, newQuantity); 
                        
                        if (productTypeCombobox.getSelectionModel().isEmpty()) {
                            pstmt.setString(5, displayProductType.getText());
                        } else {
                            pstmt.setString(5, productTypeCombobox.getSelectionModel().getSelectedItem().toString());
                        }
                        
                        pstmt.setString(6, productStockedDate.getEditor().getText().trim());
                        
                        if (productPurchaseCost.getText().isEmpty()) {
                            pstmt.setDouble(7, 0.00);
                        } else {
                            pstmt.setDouble(7, newPurchaseCost);
                        }                     
                        
                        if (productSupplierCombobox.getSelectionModel().isEmpty()) {
                            pstmt.setString(8, displayProductSupplier.getText());
                        } else {
                            pstmt.setString(8, productSupplierCombobox.getSelectionModel().getSelectedItem().toString());
                        }
                        
                        pstmt.setString(9, "Catering");

                        int i = pstmt.executeUpdate(); // load data into the database
                        if (i>0) {
                           
                            System.out.println("Retrieved general id is " + stockGeneralIDHolder + 
                                    " and product_pk_fk is " + stockForeignKeyIDHolder);
                            
                            if (stockGeneralIDHolder <= 0 || stockForeignKeyIDHolder <=0 ) {
                                System.out.println("ID OF THE UPDATED PRODUCT IS LESS THAN OR EQUAL TO 0");
                            } else {
                            
                                // save the product details to the general stock table
                                try {
                                    String sqlString = "UPDATE stock SET product_code = ?,product_name = ?,product_desc = ?,"
                                                + "quantity_avail = ?,type = ?,total_cost = ?,min_quantity = ?,department = ?,imagepath = ? "
                                                + " WHERE id = '"+stockGeneralIDHolder+"' AND product_pk_fk = '"+stockForeignKeyIDHolder+"' ";

                                    pstmt= con.prepareStatement(sqlString);

                                        if (productCode.getText().isEmpty()) {
                                            pstmt.setString(1, "Codeless");
                                        } else {
                                            pstmt.setString(1, productCode.getText());
                                        } 

                                    pstmt.setString(2, productName.getText());
                                    pstmt.setString(3, productDesc.getText());
                                    pstmt.setInt(4, finalQuantity);
                                    
                                    if (productTypeCombobox.getSelectionModel().isEmpty()) {
                                        pstmt.setString(5, displayProductType.getText());
                                    } else {
                                        pstmt.setString(5, productTypeCombobox.getSelectionModel().getSelectedItem().toString());
                                    }
                                    
                                    pstmt.setDouble(6, finalCost);
                                    
                                    if (productMinAlartCombobox.getSelectionModel().isEmpty()) {
                                        pstmt.setInt(7, Integer.parseInt(displayMinimumQuantityLevel.getText()));
                                    } else {
                                        pstmt.setInt(7, Integer.parseInt(productMinAlartCombobox.getSelectionModel().getSelectedItem().toString()));
                                    }
                                    
                                    pstmt.setString(8, "Catering");


                                    //save a file path to database
                                     if (file == null) {
                                        String imagePath = "C:\\A_Check\\Store_photos\\default.png";
                                        pstmt.setString(9, imagePath);
                                    } else {
                                        String filePath = file.getAbsolutePath();
                                        pstmt.setString(9, filePath);
                                    }
                                     
                                    int ii = pstmt.executeUpdate(); // load data into the database 

                                    if (ii>0) {

                                        //show alert before registering data of employee
                                        String msg = " Product "+productName.getText()+ " has been updated to the system.";
                                        Alert al = new Alert(Alert.AlertType.INFORMATION);
                                        al.setTitle("Update Information Dialog");
                                        al.setGraphic(imageSuccess);
                                        al.setHeaderText(null);
                                        al.setContentText(msg);
                                        al.showAndWait();

                                        clearinputFields();
                                        refreshStockTraceTable();
                                    }

                                    } catch (Exception e) {
                                }
                            
                          }

                        } else {
                        Alert error = new Alert(Alert.AlertType.ERROR);
                        error.setTitle("Failed");
                        error.setGraphic(errorImage);
                        error.setContentText("OOPS! Could not update product due  to internal error.");
                        error.showAndWait();
                        }
                        pstmt.close();

                    } catch (Exception e) {
                    Alert at = new Alert(Alert.AlertType.ERROR);
                    at.setTitle("Error in registration of tenant");
                    at.setGraphic(errorImage);
                    at.setHeaderText(null);
                    at.setContentText("OOPS! Could not update product due  to internal error. " +e);
                    at.showAndWait();
                    }finally {
                    try {
                    rs.close();
                    pstmt.close();
                    } catch (Exception e) {}
                    }
                }   
    
    }
    
    private List<Integer> availableQuantityAndCostOfProduct(){
        
        if (stockTraceProductIDHolder <= 0) {
            
            Alert errorIssue = new Alert(Alert.AlertType.ERROR);
            errorIssue.setTitle("Update Failed");
            errorIssue.setGraphic(errorImage);
            errorIssue.setHeaderText(null);
            errorIssue.setContentText("Ensure the stocked product to be updated has been retrieved.");
            errorIssue.showAndWait(); 
            
        } else {
            
            List<Integer> quantityAndCost = new ArrayList<>();
                try {
                    String sql6 = "SELECT * FROM stock WHERE product_pk_fk = ? ";
                    pstmt = con.prepareStatement(sql6);
                    pstmt.setInt(1, stockTraceProductIDHolder);
                    
                    ResultSet rsss=pstmt.executeQuery();
                    
                    if (rsss.next()) {
                        
                        stockGeneralIDHolder = rsss.getInt("id");
                        stockForeignKeyIDHolder = rsss.getInt("product_pk_fk");
                        int qnty = rsss.getInt("quantity_avail");
                        int cost = rsss.getInt("total_cost");
                        
                        quantityAndCost.add(qnty);
                        quantityAndCost.add(cost);
                        
                        return quantityAndCost;
                        
                    }else {
                        return null;
                    }                    
                } catch (SQLException ev) {
                    return null;
                }                
         }
        return null;    
    }

    @FXML
    private void deleteProdutOperation(ActionEvent event) { 
        //only admin can delete any registered product into the store
        //enable a login for
        
          if (stockTraceProductIDHolder <=0) {
            
            Alert errorIssue = new Alert(Alert.AlertType.ERROR);
            errorIssue.setTitle("Delete Failed");
            errorIssue.setGraphic(errorImage);
            errorIssue.setHeaderText(null);
            errorIssue.setContentText("Ensure the stocked product to be deleted has been retrieved.");
            errorIssue.showAndWait(); 
            
        }else {
              Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
              alert.setTitle("Delete");
              alert.setGraphic(stockedproductImageView);
              alert.setHeaderText(null);
              alert.setContentText("Only administrators are allowed to delete stocked product.\n"
                      + "To notify the Admin to delete these product, press OKAY button");
              
              Optional <ButtonType> obt = alert.showAndWait();
              
              if (obt.get()== ButtonType.OK) {
              try {
              String query = "UPDATE stocktrace SET allowdelete = true WHERE id = ? ";
              pstmt = con.prepareStatement(query);
              pstmt.setInt(1, stockTraceProductIDHolder);
              int i = pstmt.executeUpdate();
              
              if (i>0) {
                Alert al = new Alert(Alert.AlertType.CONFIRMATION);
                al.setTitle("Delete");
                al.setGraphic(imageSuccess);
                al.setHeaderText(null);
                al.setContentText("Admin has received notification to delete this product");
                al.show();
              }
              
              pstmt.close();              
              clearinputFields();
              
              } catch (SQLException ex) {
              Logger.getLogger(TeachersectionController.class.getName()).log(Level.SEVERE, null, ex);
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
    private void clearFieldsOperation(ActionEvent event) {
        clearinputFields();
    }

    @FXML
    private void stockTrace_TableMouseClicked(MouseEvent event) {
        
                try {
                    StockTrace sp = stockTrace_Table.getSelectionModel().getSelectedItem();
                    
                    String sql = "SELECT * FROM stocktrace "
                            + "WHERE product_code = ? AND product_name = ? AND supplier = ? AND date_bought = ? ";
                    pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, sp.getProductCode());
                    pstmt.setString(2, sp.getProductName());
                    pstmt.setString(3, sp.getProductSupplier());
                    pstmt.setString(4, sp.getProductDateStocked());
                    
                    rs=pstmt.executeQuery();
                    
                    if (rs.next()) {
                        
                        stockTraceProductIDHolder = rs.getInt("id");
                        System.out.println("stockTraceProductIDHolder is " + stockTraceProductIDHolder);
                        
                        String add1 = rs.getString("product_code");
                        productCode.setText(add1); 
                        String add2 = rs.getString("product_name");
                        productName.setText(add2);
                        String add3 = rs.getString("product_desc");
                        productDesc.setText(add3);
                        String add4 = rs.getString("quantity_in");
                        productQuantity.setText(add4);
                        String add5 = rs.getString("type");
                        displayProductType.setText(add5);
                        String add6 = rs.getString("date_bought");
                        productStockedDate.getEditor().setText(add6);
                        String add7 = rs.getString("purchase_cost");
                        productPurchaseCost.setText(add7);
                        String add8 = rs.getString("supplier");
                        displayProductSupplier.setText(add8);
                        
                        stockTraceProductDepartment= rs.getString("department");
                        
                        List<String> results = retrieveInfoFromStock(rs.getInt("id"));
                        
                        String miniLevel = results.get(1);                        
                        displayMinimumQuantityLevel.setText(miniLevel);
                        
                        String imagePath = results.get(0);
                        
                        try {
                            image2 = new Image(new FileInputStream(imagePath),productImageView.getFitWidth(),productImageView.getFitHeight(),false,true);
                            productImageView.setImage(image2);                  
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(TeachersectionController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    }else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Invalid Stock");
                        alert.setHeaderText(null);
                        alert.setContentText("Record not Found!");
                        alert.showAndWait(); 
                    }                    
                } catch (SQLException ev) {
                }        
        
    }

    @FXML
    private void stockTrace_TableKeyRealesed(KeyEvent event) {
        
            stockTrace_Table.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.DOWN) {
                
                try {
                    StockTrace sp = stockTrace_Table.getSelectionModel().getSelectedItem();
                    
                    String sql = "SELECT * FROM stocktrace "
                            + "WHERE product_code = ? AND product_name = ? AND supplier = ? AND date_bought = ? ";
                    pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, sp.getProductCode());
                    pstmt.setString(2, sp.getProductName());
                    pstmt.setString(3, sp.getProductSupplier());
                    pstmt.setString(4, sp.getProductDateStocked());
                    
                    rs=pstmt.executeQuery();
                    
                    if (rs.next()) {
                        
                        stockTraceProductIDHolder = rs.getInt("id");
                        System.out.println("stockTraceProductIDHolder is " + stockTraceProductIDHolder);
                        
                        String add1 = rs.getString("product_code");
                        productCode.setText(add1); 
                        String add2 = rs.getString("product_name");
                        productName.setText(add2);
                        String add3 = rs.getString("product_desc");
                        productDesc.setText(add3);
                        String add4 = rs.getString("quantity_in");
                        productQuantity.setText(add4);
                        String add5 = rs.getString("type");
                        displayProductType.setText(add5);
                        String add6 = rs.getString("date_bought");
                        productStockedDate.getEditor().setText(add6);
                        String add7 = rs.getString("purchase_cost");
                        productPurchaseCost.setText(add7);
                        String add8 = rs.getString("supplier");
                        displayProductSupplier.setText(add8);
                        
                        stockTraceProductDepartment= rs.getString("department");
                        
                        List<String> results = retrieveInfoFromStock(rs.getInt("id"));
                        
                        String miniLevel = results.get(1);                        
                        displayMinimumQuantityLevel.setText(miniLevel);
                        
                        String imagePath = results.get(0);
                         
                        try {
                            image2 = new Image(new FileInputStream(imagePath),productImageView.getFitWidth(),productImageView.getFitHeight(),false,true);
                            productImageView.setImage(image2);                  
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(TeachersectionController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    }else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Invalid Stock");
                        alert.setHeaderText(null);
                        alert.setContentText("Record not Found!");
                        alert.showAndWait(); 
                    }                    
                } catch (SQLException ev) {
                }
            }
        });          
        
        
    }
    
    private List<String> retrieveInfoFromStock(int productID){
        List<String> imageAndQuantity = new ArrayList<>();
        try {
             String sqlretrival = "SELECT * FROM stock WHERE product_pk_fk = ?";
             pstmt = con.prepareStatement(sqlretrival);
             pstmt.setInt(1, productID);

             ResultSet rss=pstmt.executeQuery();
              if (rss.next()) {
                  //String imagePath = rss.getString("imagepath"); 
                  
                  imageAndQuantity.add(rss.getString("imagepath"));
                  imageAndQuantity.add(rss.getString("min_quantity"));
                  return imageAndQuantity;
                  
                 // return imagePath;
              } else {
                  return null;
              }           
        } catch (Exception e) {
            return null;
            
        }
    
    }

    @FXML
    private void searchOnTableFieldMouseClicked(MouseEvent event) {
        changeStockTraceonOnSearch();
    }

    @FXML
    private void refresfTable(ActionEvent event) {
        refreshStockTraceTable();
    }

    @FXML
    private void unitsStockedSpecificDateOperation(ActionEvent event) {
        String onDate = unitsStockedSpecificDate.getEditor().getText().trim();
        updateStockTraceTableBySpecificDate(onDate);
    }

    @FXML
    private void unitsStockedBetweenPeriodOperation(ActionEvent event) {
        String start = startDateStock.getEditor().getText().trim();
        String end = endDateStock.getEditor().getText().trim();
        updateStockTraceTableBetweenDates(start, end);
        
    }

    @FXML
    private void printStockOnSpecificDateOperation(ActionEvent event) {
        
        if (printStockOnSpecificDate.getEditor().getText().isEmpty()) {             
                 String msg = null;
          if (printStockOnSpecificDate.getEditor().getText().isEmpty()) {
                    msg = "Date is empty, you must choose specific date";
            }
             
            Alert errorIssue = new Alert(Alert.AlertType.ERROR);
            errorIssue.setTitle("FAILED");
            errorIssue.setGraphic(errorImage);
            errorIssue.setHeaderText(null);
            errorIssue.setContentText("Report generation for printing failed.\n"
                        + msg);
            errorIssue.showAndWait(); 
            
        }else {
            String date = printStockOnSpecificDate.getEditor().getText().trim();
            
            // generate pdf
                 pdfFileChooserOpener();
                   pdfFileChooser.setTitle("Save printed report");
                   //single File selection
                   pdfFile = pdfFileChooser.showSaveDialog(Stage);                   
                    if (pdfFile != null) {
                        String path = pdfFile.getAbsolutePath();
                        
                        createSpefificDateStockPDF(path,date);
                    }        
        }
    }
    
    //method to create pdf of a specific date when stock was stored
    public void createSpefificDateStockPDF(String filename,String date){
        
       // create a pdf
       Document document = new Document(PageSize.A4,5,5,20,40);
       
        try {
            
             String sq = "SELECT * FROM stocktrace WHERE date_bought = ? AND department = ? ";
             pstmt = con.prepareStatement(sq);
             pstmt.setString(1, date );
             pstmt.setString(2, "Catering" );
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
                    + "Products stocked on: "+ date +"\n\r",bfBold12);
            intro.setAlignment(Element.ALIGN_CENTER);            
            document.add(intro);
            
            
            
            // Add a table
            float[] columnWidths = {1.5f,2f,2.3f,1.3f,2.8f,1.4f};
            PdfPTable stockTable = new PdfPTable(columnWidths);
            stockTable.setWidthPercentage(90f);
            
            insertCell(stockTable, "Code", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(stockTable, "Name", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(stockTable, "Quantity", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(stockTable, "Costed", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(stockTable, "Supplier", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(stockTable, "Department", Element.ALIGN_LEFT, 1, bfBold12);
            
            while (rs.next()) {
                String code = rs.getString("product_code");
                insertCell(stockTable, code, Element.ALIGN_LEFT, 1, bfBold);
                
                String name = rs.getString("product_name");
                insertCell(stockTable, name, Element.ALIGN_LEFT, 1, bfBold); 
                
                String q = rs.getString("quantity_in");
                String t = rs.getString("type");
                String quantity = q+"  "+t;
                insertCell(stockTable, quantity, Element.ALIGN_LEFT, 1, bfBold);
                
                String costed = rs.getString("purchase_cost");
                insertCell(stockTable, costed, Element.ALIGN_LEFT, 1, bfBold);
                
                String supplier = rs.getString("supplier");
                insertCell(stockTable, supplier, Element.ALIGN_LEFT, 1, bfBold);
                
                String deprt = rs.getString("department");
                insertCell(stockTable, deprt, Element.ALIGN_LEFT, 1, bfBold);
                         
            }
            
            document.add(stockTable);
            
            document.close();
            pstmt.close();
            
            pdfPrintedNotification(Pos.BOTTOM_RIGHT, "Successfully printed report and can be found at: \n" + filename);
            
            
        } catch (DocumentException | FileNotFoundException | SQLException ex) {
            Logger.getLogger(TeachersectionController.class.getName()).log(Level.SEVERE, null, ex);
            errorNotification(Pos.BOTTOM_RIGHT,"Report Not Generated");
        } 
        

    }    

    @FXML
    private void printUnitsStockedBetweenDatesOperation(ActionEvent event) {
        
        if (printStartDate.getEditor().getText().isEmpty() || printEndDate.getEditor().getText().isEmpty() ) {             
                 String msg = null;
            if (printStartDate.getEditor().getText().isEmpty()) {
                    msg = " Start Date is empty, you must choose specific date";
            } else if (printEndDate.getEditor().getText().isEmpty()) {
                    msg = " End Date is empty, you must choose specific date";
            }
             
            Alert errorIssue = new Alert(Alert.AlertType.ERROR);
            errorIssue.setTitle("FAILED");
            errorIssue.setGraphic(errorImage);
            errorIssue.setHeaderText(null);
            errorIssue.setContentText("Report generation for printing failed.\n"
                        + msg);
            errorIssue.showAndWait(); 
            
        }else {
            String start = printStartDate.getEditor().getText().trim();
            String end = printEndDate.getEditor().getText().trim();
            
            // generate pdf
                 pdfFileChooserOpener();
                   pdfFileChooser.setTitle("Save printed report");
                   //single File selection
                   pdfFile = pdfFileChooser.showSaveDialog(Stage);                   
                    if (pdfFile != null) {
                        String path = pdfFile.getAbsolutePath();
                        
                        createStockPDFBetweenDates(path, start, end);
                    }        
        }        
       
    }
    
    //method to create pdf of a specific date when stock was stored
    public void createStockPDFBetweenDates(String filename,String startDate,String endDate){
        
       // create a pdf
       Document document = new Document(PageSize.A4,5,5,20,40);
       
        try {
            
             String sql = "SELECT * FROM stocktrace WHERE date_bought BETWEEN  '"+startDate+"'  AND  '"+endDate+"'  AND  department = ? ";
             pstmt = con.prepareStatement(sql);
             pstmt.setString(1, "Catering" );
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
                    + "Products stocked between: "+ startDate +" and "+ endDate +"\n\r",bfBold12);
            intro.setAlignment(Element.ALIGN_CENTER);            
            document.add(intro);
            
            
            
            // Add a table
            float[] columnWidths = {1.5f,2f,2.3f,1.3f,2.8f,1.4f};
            PdfPTable stockTable = new PdfPTable(columnWidths);
            stockTable.setWidthPercentage(90f);
            
            insertCell(stockTable, "Code", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(stockTable, "Name", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(stockTable, "Quantity", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(stockTable, "Costed", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(stockTable, "Supplier", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(stockTable, "Department", Element.ALIGN_LEFT, 1, bfBold12);
            
            while (rs.next()) {
                String code = rs.getString("product_code");
                insertCell(stockTable, code, Element.ALIGN_LEFT, 1, bfBold);
                
                String name = rs.getString("product_name");
                insertCell(stockTable, name, Element.ALIGN_LEFT, 1, bfBold); 
                
                String q = rs.getString("quantity_in");
                String t = rs.getString("type");
                String quantity = q+"  "+t;
                insertCell(stockTable, quantity, Element.ALIGN_LEFT, 1, bfBold);
                
                String costed = rs.getString("purchase_cost");
                insertCell(stockTable, costed, Element.ALIGN_LEFT, 1, bfBold);
                
                String supplier = rs.getString("supplier");
                insertCell(stockTable, supplier, Element.ALIGN_LEFT, 1, bfBold);
                
                String deprt = rs.getString("department");
                insertCell(stockTable, deprt, Element.ALIGN_LEFT, 1, bfBold);
                         
            }
            
            document.add(stockTable);
            
            document.close();
            pstmt.close();
            
            pdfPrintedNotification(Pos.BOTTOM_RIGHT, "Successfully printed report and can be found at: \n" + filename);
            
            
        } catch (DocumentException | FileNotFoundException | SQLException ex) {
            Logger.getLogger(TeachersectionController.class.getName()).log(Level.SEVERE, null, ex);
            errorNotification(Pos.BOTTOM_RIGHT,"Report Not Generated");
        } 
        

    }     

    @FXML
    private void deleteProductBetweenDatesOperatin(ActionEvent event) {
        //only admin can delete any registered product into the store
        //enable a login for
        
          if (deleteStartDate.getEditor().getText().isEmpty() || deleteEndDate.getEditor().getText().isEmpty()) {
            
              String mgs = null;
              if (deleteStartDate.getEditor().getText().isEmpty() ) {
                  mgs = "Detele from start date is empty, you must choose a datae";
              } else if (deleteEndDate.getEditor().getText().isEmpty() ) {
                  mgs = "Detele to end date is empty, you must choose a datae";
              }
              
            Alert errorIssue = new Alert(Alert.AlertType.ERROR);
            errorIssue.setTitle("Delete Failed");
            errorIssue.setGraphic(errorImage);
            errorIssue.setHeaderText(null);
            errorIssue.setContentText("An error occured. \n" + mgs);
            errorIssue.showAndWait(); 
            
        }else {
              Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
              alert.setTitle("Delete Stock");
              alert.setGraphic(stockedproductImageView);
              alert.setHeaderText(null);
              alert.setContentText("Only administrators are allowed to delete stocked product.\n"
                      + "To notify the Admin to delete these products, press OKAY button");
              
              Optional <ButtonType> obt = alert.showAndWait();
              
              if (obt.get()== ButtonType.OK) {
              try {
              String query = "UPDATE stocktrace SET allowdelete = true WHERE date_bought BETWEEN  ?  AND  ?  AND department = ? ";
              pstmt = con.prepareStatement(query);
              pstmt.setString(1, deleteStartDate.getEditor().getText().trim());
              pstmt.setString(2, deleteEndDate.getEditor().getText().trim());
              pstmt.setString(3, "Catering");
              int i = pstmt.executeUpdate();
              
              if (i>0) {
                Alert al = new Alert(Alert.AlertType.CONFIRMATION);
                al.setTitle("Delete");
                al.setGraphic(imageSuccess);
                al.setHeaderText(null);
                al.setContentText("Admin has received notification to delete this product");
                al.show();
              }
              
              pstmt.close();              
              clearinputFields();
              
              } catch (SQLException ex) {
              Logger.getLogger(TeachersectionController.class.getName()).log(Level.SEVERE, null, ex);
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


    

}
