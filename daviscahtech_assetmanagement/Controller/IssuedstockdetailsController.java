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
import daviscahtech_assetmanagement.Pojo.GeneralStockStore;
import daviscahtech_assetmanagement.Pojo.IssuedStock;
import daviscahtech_assetmanagement.Pojo.StockTrace;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
public class IssuedstockdetailsController implements Initializable {

    Image errorFlag = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/errorFlag.png").toExternalForm());
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
    
        
    
    @FXML
    private DatePicker unitsIssuedSpecificDate;
    @FXML
    private DatePicker startDateIssued;
    @FXML
    private DatePicker endDateIssued;
    @FXML
    private DatePicker printIssuedOnSpecificDate;
    @FXML
    private DatePicker printStartDate;
    @FXML
    private DatePicker printEndDate;
    @FXML
    private TextField searchOnTableField;
    @FXML
    private TableView<IssuedStock> issuedStockTable;
    @FXML
    private TableColumn<IssuedStock, String> IssuedStockColumn_code;
    @FXML
    private TableColumn<IssuedStock, String> IssuedStockColumn_name;
    @FXML
    private TableColumn<IssuedStock, String> IssuedStockColumn_quantity;
    @FXML
    private TableColumn<IssuedStock, String> IssuedStockColumn_receipient;
    @FXML
    private TableColumn<IssuedStock, String> IssuedStockColumn_department;
    @FXML
    private TableColumn<IssuedStock, String> IssuedStockColumn_dateIssued;
    @FXML
    private TableColumn<IssuedStock, String> IssuedStockColumn_dateReg;
    private final ObservableList<IssuedStock> issuedStockData = FXCollections.observableArrayList();



    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        con = DatabaseConnection.connectDb();
        
        // call method to load variables with school info
        loadSchoolInfo(); 
        updateSIssuedStockTable();
        
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
    
    private void updateSIssuedStockTable(){
        
        IssuedStockColumn_code.setCellValueFactory(new PropertyValueFactory<>("code"));
        IssuedStockColumn_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        IssuedStockColumn_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        IssuedStockColumn_receipient.setCellValueFactory(new PropertyValueFactory<>("receipient"));
        IssuedStockColumn_department.setCellValueFactory(new PropertyValueFactory<>("department"));
        IssuedStockColumn_dateIssued.setCellValueFactory(new PropertyValueFactory<>("dateIssued"));
        IssuedStockColumn_dateReg.setCellValueFactory(new PropertyValueFactory<>("dateReg"));
        
        try { 
            String sql = "SELECT * FROM stockout ";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next()){
                
                issuedStockData.add( new IssuedStock(
                        rs.getString("product_code"),
                        rs.getString("product_name"),
                        rs.getString("units_out"),
                        rs.getString("given_to"),
                        rs.getString("department"),
                        rs.getString("date_given"),
                        rs.getString("date_reg")
                ));         
            }
            //load items to the table
            issuedStockTable.setItems(issuedStockData);
            issuedStockTable.setPlaceholder(new Label("No issued product matches the details provided", errorImage));
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

    private void refreshIssuedStockTable(){
        issuedStockData.clear();
        updateSIssuedStockTable();
    }    
    

    @FXML
    private void unitsIssuedSpecificDateOperation(ActionEvent event) {        
        updateIssuedStockTableBySpecificDate(unitsIssuedSpecificDate.getEditor().getText().trim());
                      
    }
    
    private void clearDatePickers(){
        unitsIssuedSpecificDate.getEditor().clear();
        startDateIssued.getEditor().clear();
        endDateIssued.getEditor().clear();
    }
    
    private void updateIssuedStockTableBySpecificDate(String date){
        
        if (unitsIssuedSpecificDate.getEditor().getText().isEmpty()) {
            Alert em = new Alert(Alert.AlertType.ERROR);
            em.setTitle("No selection");
            em.setHeaderText(null);
            em.setGraphic(errorImage);
            em.setContentText("You must choose a date");
            em.showAndWait();
        } else {
        
        
        //clear all data from collection first
        issuedStockData.clear();
        
        IssuedStockColumn_code.setCellValueFactory(new PropertyValueFactory<>("code"));
        IssuedStockColumn_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        IssuedStockColumn_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        IssuedStockColumn_receipient.setCellValueFactory(new PropertyValueFactory<>("receipient"));
        IssuedStockColumn_department.setCellValueFactory(new PropertyValueFactory<>("department"));
        IssuedStockColumn_dateIssued.setCellValueFactory(new PropertyValueFactory<>("dateIssued"));
        IssuedStockColumn_dateReg.setCellValueFactory(new PropertyValueFactory<>("dateReg"));
        
        try { 
            String sql = "SELECT * FROM stockout WHERE date_given = ? ";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, date);
            rs = pstmt.executeQuery();
            
            while(rs.next()){
                
                issuedStockData.add( new IssuedStock(
                        rs.getString("product_code"),
                        rs.getString("product_name"),
                        rs.getString("units_out"),
                        rs.getString("given_to"),
                        rs.getString("department"),
                        rs.getString("date_given"),
                        rs.getString("date_reg")
                ));         
            }
            //load items to the table
            issuedStockTable.setItems(issuedStockData);
            issuedStockTable.setPlaceholder(new Label("No issued product matches the details provided", errorImage));
            pstmt.close();
            rs.close();
            
            clearDatePickers();
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
    
    private void updateIssuedStockTableBetweenDates(String startDate, String endDate){
        
        if (startDateIssued.getEditor().getText().isEmpty() || endDateIssued.getEditor().getText().isEmpty()) {
            
            String msg = null;
            if (startDateIssued.getEditor().getText().isEmpty()) {
                msg = "You must choose a start date.";
            } else if (endDateIssued.getEditor().getText().isEmpty()) {
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
        issuedStockData.clear();
        
        IssuedStockColumn_code.setCellValueFactory(new PropertyValueFactory<>("code"));
        IssuedStockColumn_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        IssuedStockColumn_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        IssuedStockColumn_receipient.setCellValueFactory(new PropertyValueFactory<>("receipient"));
        IssuedStockColumn_department.setCellValueFactory(new PropertyValueFactory<>("department"));
        IssuedStockColumn_dateIssued.setCellValueFactory(new PropertyValueFactory<>("dateIssued"));
        IssuedStockColumn_dateReg.setCellValueFactory(new PropertyValueFactory<>("dateReg"));
        
        try { 
            String sql = "SELECT * FROM stockout WHERE date_given BETWEEN  '"+startDate+"'  AND  '"+endDate+"' ";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next()){
                
                issuedStockData.add( new IssuedStock(
                        rs.getString("product_code"),
                        rs.getString("product_name"),
                        rs.getString("units_out"),
                        rs.getString("given_to"),
                        rs.getString("department"),
                        rs.getString("date_given"),
                        rs.getString("date_reg")
                ));         
            }
            //load items to the table
            issuedStockTable.setItems(issuedStockData);
            issuedStockTable.setPlaceholder(new Label("No issued product matches the details provided", errorImage));
            pstmt.close();
            rs.close();            
            clearDatePickers();
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
    private void unitsIssuedBetweenPeriodOperation(ActionEvent event) {
        String startDate = startDateIssued.getEditor().getText().trim();
        String endDate = endDateIssued.getEditor().getText().trim();           
            updateIssuedStockTableBetweenDates(startDate,endDate);
            
    }

    @FXML
    private void printIssuedOnSpecificDateOperation(ActionEvent event) {
    
        if (printIssuedOnSpecificDate.getEditor().getText().isEmpty()) {  
            
            String msg = "Date is empty, you must choose specific date";
            Alert errorIssue = new Alert(Alert.AlertType.ERROR);
            errorIssue.setTitle("FAILED");
            errorIssue.setGraphic(errorImage);
            errorIssue.setHeaderText(null);
            errorIssue.setContentText("Report generation for printing failed.\n"
                        + msg);
            errorIssue.showAndWait(); 
            
        }else {
            String date = printIssuedOnSpecificDate.getEditor().getText().trim();
            
            // generate pdf
                 pdfFileChooserOpener();
                   pdfFileChooser.setTitle("Save printed report");
                   //single File selection
                   pdfFile = pdfFileChooser.showSaveDialog(Stage);                   
                    if (pdfFile != null) {
                        String path = pdfFile.getAbsolutePath();
                        createSpefificDateIssuedStockPDF(path,date);
                        clearDatePickers();
                    }        
        }        
        
    }
    
    //method to create pdf of a specific date when stock was stored
    public void createSpefificDateIssuedStockPDF(String filename,String date){
        
       // create a pdf
       Document document = new Document(PageSize.A4,5,5,20,40);
       
        try {
            
             String sq = "SELECT * FROM stockout WHERE date_given = ? ";
             pstmt = con.prepareStatement(sq);
             pstmt.setString(1, date );
             rs=pstmt.executeQuery();
            
            Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK);
            Font bfBold = new Font(Font.FontFamily.TIMES_ROMAN, 11);
            Font bfNormal = new Font(Font.FontFamily.TIMES_ROMAN, 8);
            
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
            HeaderFooterPageEvent event = new HeaderFooterPageEvent();
            writer.setPageEvent(event);
            
            document.open();
            addMetaData(document);
            
            //school info
            Paragraph intro = new Paragraph(""+schoolName+"\n"
                    + ""+schoolAddress+" "+ schoolRegion+"\n"
                    + "Website: "+ schoolWebsite+"\n"
                    + "Products issued on: "+ date +"\n\r",bfBold12);
            intro.setAlignment(Element.ALIGN_CENTER);            
            document.add(intro);
            
            
            
            // Add a table
            float[] columnWidths = {1.5f,2.6f,1f,3.3f,2f};
            PdfPTable stockTable = new PdfPTable(columnWidths);
            stockTable.setWidthPercentage(90f);
            
            insertCell(stockTable, "Product Code", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(stockTable, "Product Name", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(stockTable, "Units", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(stockTable, "Receipient", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(stockTable, "Department", Element.ALIGN_LEFT, 1, bfBold12);
            
            while (rs.next()) {
                String code = rs.getString("product_code");
                insertCell(stockTable, code, Element.ALIGN_LEFT, 1, bfBold);
                
                String name = rs.getString("product_name");
                insertCell(stockTable, name, Element.ALIGN_LEFT, 1, bfBold); 
                
                String q = rs.getString("units_out");
                insertCell(stockTable, q, Element.ALIGN_LEFT, 1, bfBold);
                
                String recipient = rs.getString("given_to");
                insertCell(stockTable, recipient, Element.ALIGN_LEFT, 1, bfBold);
                
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
    
    
    //method to create pdf of a specific date when stock was stored
    public void createIssuedStockBetweenDatesPDF(String filename,String startDate,String endDate){
        
       // create a pdf
       Document document = new Document(PageSize.A4,5,5,20,40);
       
        try {
            
             String sq = "SELECT * FROM stockout WHERE date_given BETWEEN  '"+startDate+"'  AND  '"+endDate+"' ";
             pstmt = con.prepareStatement(sq);
             rs=pstmt.executeQuery();
            
            Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK);
            Font bfBold = new Font(Font.FontFamily.TIMES_ROMAN, 11);
            Font bfNormal = new Font(Font.FontFamily.TIMES_ROMAN, 8);
            
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
            HeaderFooterPageEvent event = new HeaderFooterPageEvent();
            writer.setPageEvent(event);
            
            document.open();
            addMetaData(document);
            
            //school info
            Paragraph intro = new Paragraph(""+schoolName+"\n"
                    + ""+schoolAddress+" "+ schoolRegion+"\n"
                    + "Website: "+ schoolWebsite+"\n"
                    + "Products issued between "+ startDate +" and "+ endDate +"\n\r",bfBold12);
            intro.setAlignment(Element.ALIGN_CENTER);            
            document.add(intro);
            
            
            
            // Add a table
            float[] columnWidths = {1.5f,2.6f,1f,3.3f,2f};
            PdfPTable stockTable = new PdfPTable(columnWidths);
            stockTable.setWidthPercentage(90f);
            
            insertCell(stockTable, "Product Code", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(stockTable, "Product Name", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(stockTable, "Units", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(stockTable, "Receipient", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(stockTable, "Department", Element.ALIGN_LEFT, 1, bfBold12);
            
            while (rs.next()) {
                String code = rs.getString("product_code");
                insertCell(stockTable, code, Element.ALIGN_LEFT, 1, bfBold);
                
                String name = rs.getString("product_name");
                insertCell(stockTable, name, Element.ALIGN_LEFT, 1, bfBold); 
                
                String q = rs.getString("units_out");
                insertCell(stockTable, q, Element.ALIGN_LEFT, 1, bfBold);
                
                String recipient = rs.getString("given_to");
                insertCell(stockTable, recipient, Element.ALIGN_LEFT, 1, bfBold);
                
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
    
            //Method to open a fileChooser dialog box
    public void pdfFileChooserOpener(){
                pdfFileChooser = new FileChooser();
                pdfFileChooser.getExtensionFilters().addAll(                
                new FileChooser.ExtensionFilter("PDF Files","*.pdf")

                
        );   
    }    

    @FXML
    private void printUnitsIssuedBetweenDatesOperation(ActionEvent event) {
     
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
                        
                        createIssuedStockBetweenDatesPDF(path, start, end);
                        clearDatePickers();
                    }        
        }         
                
    }

    @FXML
    private void searchOnTableFieldMouseClicked(MouseEvent event) {
        changeIssuedStockTableonOnSearch();
    }
    
    private void changeIssuedStockTableonOnSearch(){
        
        //wrap the observablelist in a FilteredList()
        FilteredList<IssuedStock> filteredData =  new FilteredList<>(issuedStockData,p -> true);
        
        //set the filter predicate whenever the filter changes
        searchOnTableField.textProperty().addListener((observable,oldValue,newValue)->{
                
            filteredData.setPredicate(IssuedStock -> {
            
                //if filter text is empty, display all product
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                
                //compare product id and product name of every product with the filter text
                String lowerCaseFilter =newValue.toLowerCase();
               
                if (IssuedStock.getCode().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches product Id
                } else if (IssuedStock.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches product Id
                }  else if (IssuedStock.getDepartment().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches product Id
                }   else if (IssuedStock.getReceipient().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches product Id
                } 
                return false; // Do not match            
            });
        });
        
        //wrap the filteredList in a sortedList
        SortedList<IssuedStock> sortedData = new SortedList<>(filteredData);
        
        //bind the sortedData to the Tableview
        sortedData.comparatorProperty().bind(issuedStockTable.comparatorProperty());
        
        //add sorted and filtered data to the table
        issuedStockTable.setItems(sortedData); 
    }       

    @FXML
    private void refresfTable(ActionEvent event) {
        refreshIssuedStockTable();
    }


    
}// end of class
