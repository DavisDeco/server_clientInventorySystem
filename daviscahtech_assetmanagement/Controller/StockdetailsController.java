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
import java.time.LocalDate;
import java.time.LocalTime;
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
public class StockdetailsController implements Initializable {
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
    private DatePicker stockTrace_SpecificDate;
    @FXML
    private DatePicker stockTrace_startDateStock;
    @FXML
    private DatePicker stockTrace_endDateStock;
    @FXML
    private DatePicker stockTrace_printStockOnSpecificDate;
    @FXML
    private DatePicker stockTrace_printStartDate;
    @FXML
    private DatePicker stockTrace_printEndDate;
    @FXML
    private TextField stockTrace_searchOnTableField;
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
    private TableColumn<StockTrace, String> stockTrace_department;
    @FXML
    private TableColumn<StockTrace, String> stockTrace_stockedDate;
    private final ObservableList<StockTrace> stockTraceData = FXCollections.observableArrayList();
    @FXML
    private ImageView generalStockImageView;
    @FXML
    private Text generalStock_alertLevel;
    @FXML
    private Text generalStock_quantityType;
    @FXML
    private TextField generalStock_searchOnTableField;
    @FXML
    private TableView<GeneralStockStore> generalStocktable;
    @FXML
    private TableColumn<GeneralStockStore, String> generalStockColumn_code;
    @FXML
    private TableColumn<GeneralStockStore, String> generalStockColumn_name;
    @FXML
    private TableColumn<GeneralStockStore, String> generalStockColumn_quantity;
    @FXML
    private TableColumn<GeneralStockStore, String> generalStockColumn_cost;
    @FXML
    private TableColumn<GeneralStockStore, String> generalStockColumn_department;
    @FXML
    private TableColumn<GeneralStockStore, String> generalStockColumn_stockedDate;
    private final ObservableList<GeneralStockStore> generalStoreData = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
                // TODO
        con = DatabaseConnection.connectDb();
        
        // call method to load variables with school info
        loadSchoolInfo();
        
        //load tables
        updateStockTraceTable();
        updateGeneralStockTable();
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
        stockTrace_searchOnTableField.textProperty().addListener((observable,oldValue,newValue)->{
                
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
        stockTrace_department.setCellValueFactory(new PropertyValueFactory<>("department"));
        stockTrace_stockedDate.setCellValueFactory(new PropertyValueFactory<>("productDateStocked"));

        try { 
            String sql = "SELECT * FROM stocktrace ";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next()){
                
                stockTraceData.add( new StockTrace(
                        rs.getString("product_code"),
                        rs.getString("product_name"),
                        rs.getString("quantity_in"),
                        rs.getString("type"),
                        rs.getString("purchase_cost"),
                        rs.getString("supplier"),
                        rs.getString("department"),
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
        
        if (stockTrace_SpecificDate.getEditor().getText().isEmpty()) {
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
        stockTrace_department.setCellValueFactory(new PropertyValueFactory<>("department"));
        stockTrace_stockedDate.setCellValueFactory(new PropertyValueFactory<>("productDateStocked"));

        try { 
            String sql = "SELECT * FROM stocktrace WHERE date_bought = ? ";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, date);
            rs = pstmt.executeQuery();
            while(rs.next()){
                                
                stockTraceData.add( new StockTrace(
                        rs.getString("product_code"),
                        rs.getString("product_name"),
                        rs.getString("quantity_in"),
                        rs.getString("type"),
                        rs.getString("purchase_cost"),
                        rs.getString("supplier"),
                        rs.getString("department"),
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
        
        if (stockTrace_startDateStock.getEditor().getText().isEmpty() || stockTrace_endDateStock.getEditor().getText().isEmpty()) {
            
            String msg = null;
            if (stockTrace_startDateStock.getEditor().getText().isEmpty()) {
                msg = "You must choose a start date.";
            } else if (stockTrace_endDateStock.getEditor().getText().isEmpty()) {
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
        stockTrace_department.setCellValueFactory(new PropertyValueFactory<>("department"));
        stockTrace_stockedDate.setCellValueFactory(new PropertyValueFactory<>("productDateStocked"));

        try { 
            String sql = "SELECT * FROM stocktrace WHERE date_bought BETWEEN  '"+startDate+"'  AND  '"+endDate+"' ";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next()){
                
                stockTraceData.add( new StockTrace(
                        rs.getString("product_code"),
                        rs.getString("product_name"),
                        rs.getString("quantity_in"),
                        rs.getString("type"),
                        rs.getString("purchase_cost"),
                        rs.getString("supplier"),
                        rs.getString("department"),
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
        

    @FXML
    private void stockTrace_unitsStockedSpecificDateOperation(ActionEvent event) {
        updateStockTraceTableBySpecificDate(stockTrace_SpecificDate.getEditor().getText().trim());
        clearDatePickers();
    }

    @FXML
    private void stockTrace_unitsStockedBetweenPeriodOperation(ActionEvent event) {
        String startDate = stockTrace_startDateStock.getEditor().getText().trim();
        String endDate = stockTrace_endDateStock.getEditor().getText().trim();
        updateStockTraceTableBetweenDates(startDate, endDate);
        clearDatePickers();
    }
    
    private void clearDatePickers(){
        
        stockTrace_SpecificDate.getEditor().clear();
        stockTrace_startDateStock.getEditor().clear();
        stockTrace_endDateStock.getEditor().clear();
        stockTrace_printStartDate.getEditor().clear();
        stockTrace_printEndDate.getEditor().clear();
        stockTrace_printStockOnSpecificDate.getEditor().clear();
        
    
    }

    @FXML
    private void stockTrace_printStockOnSpecificDateOperation(ActionEvent event) {
        
        if (stockTrace_printStockOnSpecificDate.getEditor().getText().isEmpty()) {  
            
            String msg = "Date is empty, you must choose specific date";
            Alert errorIssue = new Alert(Alert.AlertType.ERROR);
            errorIssue.setTitle("FAILED");
            errorIssue.setGraphic(errorImage);
            errorIssue.setHeaderText(null);
            errorIssue.setContentText("Report generation for printing failed.\n"
                        + msg);
            errorIssue.showAndWait(); 
            
        }else {
            String date = stockTrace_printStockOnSpecificDate.getEditor().getText().trim();
            
            // generate pdf
                 pdfFileChooserOpener();
                   pdfFileChooser.setTitle("Save printed report");
                   //single File selection
                   pdfFile = pdfFileChooser.showSaveDialog(Stage);                   
                    if (pdfFile != null) {
                        String path = pdfFile.getAbsolutePath();
                        createSpefificDateStockPDF(path,date);
                        clearDatePickers();
                    }        
        }        
        
    }
    
    //method to create pdf of a specific date when stock was stored
    public void createSpefificDateStockPDF(String filename,String date){
        
       // create a pdf
       Document document = new Document(PageSize.A4,5,5,20,40);
       
        try {
            
             String sq = "SELECT * FROM stocktrace WHERE date_bought = ? ";
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
            Paragraph meaning = new Paragraph("* Indicates new stock increase to already registered product in the store",bfNormal);
            meaning.setAlignment(Element.ALIGN_CENTER);
            document.add(meaning);
            
            document.close();
            pstmt.close();
            
            pdfPrintedNotification(Pos.BOTTOM_RIGHT, "Successfully printed report and can be found at: \n" + filename);
            
            
        } catch (DocumentException | FileNotFoundException | SQLException ex) {
            Logger.getLogger(TeachersectionController.class.getName()).log(Level.SEVERE, null, ex);
            errorNotification(Pos.BOTTOM_RIGHT,"Report Not Generated");
        } 
        

    } 
    
    //method to create pdf of a specific date when stock was stored
    public void createGeneralStockPDF(String filename){
        
       // create a pdf
       Document document = new Document(PageSize.A4,5,5,20,40);
       
        try {
            
             String sq = "SELECT * FROM stock ";
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
                    + "All General Stock details as at "+LocalDate.now()+"\n\r",bfBold12);
            intro.setAlignment(Element.ALIGN_CENTER);            
            document.add(intro);
            
            
            
            // Add a table
            float[] columnWidths = {1.5f,2f,2f,1.3f,1.7f};
            PdfPTable stockTable = new PdfPTable(columnWidths);
            stockTable.setWidthPercentage(90f);
            
            insertCell(stockTable, "Code", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(stockTable, "Name", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(stockTable, "Quantity", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(stockTable, "Overall Cost", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(stockTable, "Department", Element.ALIGN_LEFT, 1, bfBold12);
            
            while (rs.next()) {
                String code = rs.getString("product_code");
                insertCell(stockTable, code, Element.ALIGN_LEFT, 1, bfBold);
                
                String name = rs.getString("product_name");
                insertCell(stockTable, name, Element.ALIGN_LEFT, 1, bfBold); 
                
                String q = rs.getString("quantity_avail");
                String t = rs.getString("type");
                String quantity = q+"  "+t;
                insertCell(stockTable, quantity, Element.ALIGN_LEFT, 1, bfBold);
                
                String costed = rs.getString("total_cost");
                insertCell(stockTable, costed, Element.ALIGN_LEFT, 1, bfBold);
                                
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
    private void stockTrace_printUnitsStockedBetweenDatesOperation(ActionEvent event) {
        
        if (stockTrace_printStartDate.getEditor().getText().isEmpty() || stockTrace_printEndDate.getEditor().getText().isEmpty() ) {             
                 String msg = null;
            if (stockTrace_printStartDate.getEditor().getText().isEmpty()) {
                    msg = " Start Date is empty, you must choose specific date";
            } else if (stockTrace_printEndDate.getEditor().getText().isEmpty()) {
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
            String start = stockTrace_printStartDate.getEditor().getText().trim();
            String end = stockTrace_printEndDate.getEditor().getText().trim();
            
            // generate pdf
                 pdfFileChooserOpener();
                   pdfFileChooser.setTitle("Save printed report");
                   //single File selection
                   pdfFile = pdfFileChooser.showSaveDialog(Stage);                   
                    if (pdfFile != null) {
                        String path = pdfFile.getAbsolutePath();
                        
                        createStockPDFBetweenDates(path, start, end);
                        clearDatePickers();
                    }        
        }         
        
    }
    
    //method to create pdf of a specific date when stock was stored
    public void createStockPDFBetweenDates(String filename,String startDate,String endDate){
        
       // create a pdf
       Document document = new Document(PageSize.A4,5,5,20,40);
       
        try {
            
             String sql = "SELECT * FROM stocktrace WHERE date_bought BETWEEN  '"+startDate+"'  AND  '"+endDate+"' ";
             pstmt = con.prepareStatement(sql);
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
            Paragraph meaning = new Paragraph("* Indicates new stock increase to already registered product in the store",bfNormal);
            meaning.setAlignment(Element.ALIGN_CENTER);
            document.add(meaning);
            
            document.close();
            pstmt.close();
            
            pdfPrintedNotification(Pos.BOTTOM_RIGHT, "Successfully printed report and can be found at: \n" + filename);
            
            
        } catch (DocumentException | FileNotFoundException | SQLException ex) {
            Logger.getLogger(TeachersectionController.class.getName()).log(Level.SEVERE, null, ex);
            errorNotification(Pos.BOTTOM_RIGHT,"Report Not Generated");
        } 
        

    }      

    @FXML
    private void searchOnTableFieldMouseClicked(MouseEvent event) {
        changeStockTraceonOnSearch();
    }

    @FXML
    private void stockTrace_refresfTable(ActionEvent event) {
        refreshStockTraceTable();
    }

///######################## General stock methods #####################################
       //Method to monitor tenant's profile update on table upon searching
    private void changeGeneralStockTableonOnSearch(){
        
        //wrap the observablelist in a FilteredList()
        FilteredList<GeneralStockStore> filteredData =  new FilteredList<>(generalStoreData,p -> true);
        
        //set the filter predicate whenever the filter changes
        generalStock_searchOnTableField.textProperty().addListener((observable,oldValue,newValue)->{
                
            filteredData.setPredicate(GeneralStockStore -> {
            
                //if filter text is empty, display all product
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                
                //compare product id and product name of every product with the filter text
                String lowerCaseFilter =newValue.toLowerCase();
               
                if (GeneralStockStore.getCode().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches product Id
                } else if (GeneralStockStore.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches product Id
                }  else if (GeneralStockStore.getDepartment().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches product Id
                }  
                return false; // Do not match            
            });
        });
        
        //wrap the filteredList in a sortedList
        SortedList<GeneralStockStore> sortedData = new SortedList<>(filteredData);
        
        //bind the sortedData to the Tableview
        sortedData.comparatorProperty().bind(generalStocktable.comparatorProperty());
        
        //add sorted and filtered data to the table
        generalStocktable.setItems(sortedData); 
    }   
    
    private void populateGeneralStoreTable(TableColumn code,TableColumn name,TableColumn qnty,
            TableColumn depart,TableColumn cost,TableColumn date,ObservableList data,
            TableView tableView){
    
        code.setCellValueFactory(new PropertyValueFactory<>("code"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        qnty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        depart.setCellValueFactory(new PropertyValueFactory<>("department"));
        cost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        date.setCellValueFactory(new PropertyValueFactory<>("date_reg"));
       

        try { 
            String sql = "SELECT * FROM stock ";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next()){
                                
                int quantity_avail = rs.getInt("quantity_avail");
                    //concatinate
                String type = rs.getString("type");
                String quantityAndType = quantity_avail+" "+ type;
               
                data.add( new GeneralStockStore(
                        rs.getString("product_code"),
                        rs.getString("product_name"),
                        quantityAndType,
                        rs.getString("department"),
                        rs.getString("total_cost"),
                        null,
                        rs.getString("date_reg")
                ));         
            }
            //load items to the table
            tableView.setItems(data);
            tableView.setPlaceholder(new Label("No matches for the details provided", errorImage));
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
    
    private void updateGeneralStockTable(){
    
        populateGeneralStoreTable(generalStockColumn_code, generalStockColumn_name, generalStockColumn_quantity,
                generalStockColumn_department, generalStockColumn_cost, generalStockColumn_stockedDate, generalStoreData, generalStocktable);
    }
    
    private void refrehGeneralStockTable(){
        generalStoreData.clear();
        updateGeneralStockTable();
    }
    
    private void keyReleasedOnAnyTable(TableView tableView,Text type,Text minLevel,ImageView imageView){
        
            tableView.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.DOWN) {
                
                try {
                    GeneralStockStore ts = (GeneralStockStore) tableView.getSelectionModel().getSelectedItem();
                    
                    String sql = "SELECT * FROM stock "
                            + "WHERE product_code = ? AND product_name = ? AND quantity_avail = ? AND department = ? ";
                    pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, ts.getCode());
                    pstmt.setString(2, ts.getName());
                    pstmt.setString(3, ts.getQuantity());
                    pstmt.setString(4, ts.getDepartment());
                    
                    rs=pstmt.executeQuery();
                    
                    if (rs.next()) {
                        
                        String add1 = rs.getString("type");
                        type.setText(add1); 
                        String add2 = rs.getString("min_quantity");
                        minLevel.setText(add2);
                        
                        String imagePath = rs.getString("imagepath");
                         
                        try {
                            image2 = new Image(new FileInputStream(imagePath),imageView.getFitWidth(),imageView.getFitHeight(),false,true);
                            imageView.setImage(image2);                  
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(StockdetailsController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    }else {
                        errorNotification(Pos.BOTTOM_RIGHT, "Record not found");
                    }                    
                } catch (SQLException ev) {
                }
            }
        });          
    
    }  
    
    private void mouseClickedOnAnyTable(TableView tableView,Text type,Text minLevel,ImageView imageView){
                        
                try {
                    GeneralStockStore ts = (GeneralStockStore) tableView.getSelectionModel().getSelectedItem();
                    
                    String sql = "SELECT * FROM stock "
                            + "WHERE product_code = ? AND product_name = ? AND quantity_avail = ? AND department = ? ";
                    pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, ts.getCode());
                    pstmt.setString(2, ts.getName());
                    pstmt.setString(3, ts.getQuantity());
                    pstmt.setString(4, ts.getDepartment());
                    
                    rs=pstmt.executeQuery();
                    
                    if (rs.next()) {
                        
                        String add1 = rs.getString("type");
                        type.setText(add1); 
                        String add2 = rs.getString("min_quantity");
                        minLevel.setText(add2);
                        
                        String imagePath = rs.getString("imagepath");
                         
                        try {
                            image2 = new Image(new FileInputStream(imagePath),imageView.getFitWidth(),imageView.getFitHeight(),false,true);
                            imageView.setImage(image2);                  
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(StockdetailsController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    }else {
                        errorNotification(Pos.BOTTOM_RIGHT, "Record not found");
                    }                    
                } catch (SQLException ev) {
                }                    
    
    }     


    @FXML
    private void generalStock_searchOnTableFieldMouseClicked(MouseEvent event) {
        changeGeneralStockTableonOnSearch();
    }

    @FXML
    private void refreshGeneralStockTable(ActionEvent event) {
        refrehGeneralStockTable();
    }

    @FXML
    private void generalStocktableMouseClicked(MouseEvent event) {
        mouseClickedOnAnyTable(generalStocktable,generalStock_quantityType,generalStock_alertLevel, generalStockImageView);
    }

    @FXML
    private void generalStocktableKeyRealesed(KeyEvent event) {
       keyReleasedOnAnyTable(generalStocktable,generalStock_quantityType,generalStock_alertLevel, generalStockImageView);

    }

    @FXML
    private void generalStock_printAllDetailsOperation(ActionEvent event) {
            // generate pdf
                 pdfFileChooserOpener();
                   pdfFileChooser.setTitle("Save printed report");
                   //single File selection
                   pdfFile = pdfFileChooser.showSaveDialog(Stage);                   
                    if (pdfFile != null) {
                        String path = pdfFile.getAbsolutePath();
                        
                        createGeneralStockPDF(path);
                        generalStock_alertLevel.setText(null);
                        generalStock_quantityType.setText(null);
                    } 
    }
    
}// end of class
