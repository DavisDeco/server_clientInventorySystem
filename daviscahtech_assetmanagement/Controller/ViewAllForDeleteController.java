/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daviscahtech_assetmanagement.Controller;

import daviscahtech_assetmanagement.Dao.DatabaseConnection;
import daviscahtech_assetmanagement.Pojo.StockTrace;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author davis
 */
public class ViewAllForDeleteController implements Initializable {
        
    Image errorFlag = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/errorFlag.png").toExternalForm());
    ImageView errorImage = new ImageView(errorFlag);
    
    //###############################For DB link##########################  warningImagevies
    Connection con = null;
    PreparedStatement pstmt;
    ResultSet rs;
    Statement st;
    

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        con = DatabaseConnection.connectDb();
        updateStockTraceTable();
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
            String sql = "SELECT * FROM stocktrace WHERE allowdelete = ? ";
            pstmt = con.prepareStatement(sql);
            pstmt.setBoolean(1, true);
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
            stockTrace_Table.setPlaceholder(new Label("No stocked product marked for delete", errorImage));
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

    
} // end of class
