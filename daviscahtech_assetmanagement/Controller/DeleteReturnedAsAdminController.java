/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daviscahtech_assetmanagement.Controller;

import daviscahtech_assetmanagement.Dao.DatabaseConnection;
import daviscahtech_assetmanagement.Pojo.StockReturned;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class
 *
 * @author davis
 */
public class DeleteReturnedAsAdminController implements Initializable {
    
        
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
        //NOTIFICATION BUILDER VARIABLE
    Notifications notificationBuilder;
    @FXML
    private DatePicker unitsIssuedSpecificDate;
    @FXML
    private DatePicker startDateIssued;
    @FXML
    private DatePicker endDateIssued;
    @FXML
    private TextField searchOnTableField;
    @FXML
    private TableView<StockReturned> stockReturnedTable;
    @FXML
    private TableColumn<StockReturned, String> stockReturnedColumn_code;
    @FXML
    private TableColumn<StockReturned, String> stockReturnedColumn_name;
    @FXML
    private TableColumn<StockReturned, String> stockReturnedColumn_quantity;
    @FXML
    private TableColumn<StockReturned, String> stockReturnedColumn_type;
    @FXML
    private TableColumn<StockReturned, String> stockReturnedColumn_department;
    @FXML
    private TableColumn<StockReturned, String> stockReturnedColumn_returnedBy;
    @FXML
    private TableColumn<StockReturned, String> stockReturnedColumn_Date;
    private final ObservableList<StockReturned> stockReturnedData = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        con = DatabaseConnection.connectDb();
        updateStockReturnedTable();
    } 
    
    public void infoNotification(Pos pos,String title){
       notificationBuilder = Notifications.create()
                .title("Information")
                .text(title)
                .graphic(infoImage)
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
        

    private void updateStockReturnedTable(){
        
        stockReturnedColumn_code.setCellValueFactory(new PropertyValueFactory<>("code"));
        stockReturnedColumn_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        stockReturnedColumn_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        stockReturnedColumn_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        stockReturnedColumn_department.setCellValueFactory(new PropertyValueFactory<>("department"));
        stockReturnedColumn_returnedBy.setCellValueFactory(new PropertyValueFactory<>("returnedBy"));
        stockReturnedColumn_Date.setCellValueFactory(new PropertyValueFactory<>("dateReturned"));

        try { 
            String sql = "SELECT * FROM stockreturn ";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next()){
                
                stockReturnedData.add( new StockReturned(
                        rs.getString("product_code"),
                        rs.getString("product_name"),
                        rs.getString("units_in"),
                        rs.getString("type"),
                        rs.getString("department"),
                        rs.getString("returnedby"),
                        rs.getString("date_returned")
                ));         
            }
            //load items to the table
            stockReturnedTable.setItems(stockReturnedData);
            stockReturnedTable.setPlaceholder(new Label("No returned product matches the details provided", errorImage));
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
    
    private void refreshStockReturnedTable(){
        stockReturnedData.clear();
        updateStockReturnedTable();
    }    

    private void changeStockReturnedonOnSearch(){
        
        //wrap the observablelist in a FilteredList()
        FilteredList<StockReturned> filteredData =  new FilteredList<>(stockReturnedData,p -> true);
        
        //set the filter predicate whenever the filter changes
        searchOnTableField.textProperty().addListener((observable,oldValue,newValue)->{
                
            filteredData.setPredicate(StockReturned -> {
            
                //if filter text is empty, display all product
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                
                //compare product id and product name of every product with the filter text
                String lowerCaseFilter =newValue.toLowerCase();
               
                if (StockReturned.getCode().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches product Id
                } else if (StockReturned.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches product Id
                }  else if (StockReturned.getDepartment().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches product Id
                }  else if (StockReturned.getReturnedBy().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches product Id
                }  
                return false; // Do not match            
            });
        });
        
        //wrap the filteredList in a sortedList
        SortedList<StockReturned> sortedData = new SortedList<>(filteredData);
        
        //bind the sortedData to the Tableview
        sortedData.comparatorProperty().bind(stockReturnedTable.comparatorProperty());
        
        //add sorted and filtered data to the table
        stockReturnedTable.setItems(sortedData); 
    }         
    

    @FXML
    private void deleteUnitsReturnedSpecificDateOperation(ActionEvent event) {
        
          if (unitsIssuedSpecificDate.getEditor().getText().isEmpty()) {
            
            Alert errorIssue = new Alert(Alert.AlertType.ERROR);
            errorIssue.setTitle("Delete Failed");
            errorIssue.setGraphic(errorImage);
            errorIssue.setHeaderText(null);
            errorIssue.setContentText("Choose a specific day when items were returned.");
            errorIssue.showAndWait(); 
            
        }else {
              Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
              alert.setTitle("Delete");
              alert.setGraphic(stockedproductImageView);
              alert.setHeaderText(null);
              alert.setContentText("Are you sure you want to delete returned items");
              
              Optional <ButtonType> obt = alert.showAndWait();
              
              if (obt.get()== ButtonType.OK) {
              try {
                String query = "DELETE FROM stockreturn WHERE date_returned = ? ";
                pstmt = con.prepareStatement(query);
                pstmt.setString(1, unitsIssuedSpecificDate.getEditor().getText().trim());
               int i = pstmt.executeUpdate();
              
                if (i>0) {
                    successNotification(Pos.BOTTOM_RIGHT, "Returned items have been deleted.");
                }
              
              pstmt.close(); 
              refreshStockReturnedTable();
              
              } catch (SQLException ex) {
                     Logger.getLogger(DeleteReturnedAsAdminController.class.getName()).log(Level.SEVERE, null, ex);
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
    private void deleteUnitsReturnedBetweenPeriodOperation(ActionEvent event) {
        
        String startDate = startDateIssued.getEditor().getText().trim();
        String endDate = endDateIssued.getEditor().getText().trim();
        
          if (startDateIssued.getEditor().getText().isEmpty()|| endDateIssued.getEditor().getText().isEmpty()) {
            String msg = null;
            
            
              if (startDateIssued.getEditor().getText().isEmpty()) {
                  msg = "Choose start date";
              } else if (endDateIssued.getEditor().getText().isEmpty()) {
                  msg = "Choose end date";
              }
            Alert errorIssue = new Alert(Alert.AlertType.ERROR);
            errorIssue.setTitle("Delete Failed");
            errorIssue.setGraphic(errorImage);
            errorIssue.setHeaderText(null);
            errorIssue.setContentText("Could not delete\n\n."+ msg);
            errorIssue.showAndWait(); 
            
        }else {
              Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
              alert.setTitle("Delete");
              alert.setGraphic(stockedproductImageView);
              alert.setHeaderText(null);
              alert.setContentText("Are you sure you want to delete returned items");
              
              Optional <ButtonType> obt = alert.showAndWait();
              
              if (obt.get()== ButtonType.OK) {
              try {
                String query = "DELETE FROM stockreturn WHERE date_returned BETWEEN  '"+startDate+"'  AND  '"+endDate+"'  ";
                pstmt = con.prepareStatement(query);
               int i = pstmt.executeUpdate();
              
                if (i>0) {
                    successNotification(Pos.BOTTOM_RIGHT, "Returned items have been deleted.");
                }
              
              pstmt.close(); 
              refreshStockReturnedTable();
              
              } catch (SQLException ex) {
                     Logger.getLogger(DeleteIssuedAsAdminController.class.getName()).log(Level.SEVERE, null, ex);
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
    private void searchOnTableFieldMouseClicked(MouseEvent event) {
        changeStockReturnedonOnSearch();
    }

    @FXML
    private void refresfTable(ActionEvent event) {
        refreshStockReturnedTable();
    }
    
}// end of class
