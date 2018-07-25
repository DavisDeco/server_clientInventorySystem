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
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author davis
 */
public class ReturnedStockController implements Initializable {
    Image errorFlag = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/errorFlag.png").toExternalForm());
    ImageView errorImage = new ImageView(errorFlag);
    
    //###############################For DB link##########################  warningImagevies
    Connection con = null;
    PreparedStatement pstmt;
    ResultSet rs;
    Statement st;
    
    
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
    private void searchOnTableFieldMouseClicked(MouseEvent event) {
        changeStockReturnedonOnSearch();
    }

    @FXML
    private void refresfTable(ActionEvent event) {
        refreshStockReturnedTable();
    }

    
}// end of class
