/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daviscahtech_assetmanagement.Server;

import daviscahtech_assetmanagement.Dao.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;

/**
 *
 * @author davis
 */
public class ClientDBRequests {
    
        /// db variable
    Connection con = null;
    PreparedStatement pstmt;
    ResultSet rs;
    Statement st;

    public ClientDBRequests() {
        con = DatabaseConnection.connectDb();
    }
    
   // method to get all stocktrace details
    public ObservableList updateStockTraceData(){ 
        ObservableList stockTraceData = FXCollections.observableArrayList();
        
        try { 
            String sql = "SELECT * FROM stocktrace ";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next()){
                
                stockTraceData.add(
                    rs.getString("product_code")+" | "+ rs.getString("product_name")+" | "+ rs.getString("quantity_in") +" "+rs.getString("type")+""
                            + " | "+ rs.getString("purchase_cost")+" | "+rs.getString("supplier")+" | "+rs.getString("department")+" | "+ rs.getString("date_bought")
                        
                );         
            }
            
            pstmt.close();
            rs.close();
            
            return stockTraceData;
            
        } catch (SQLException e) {
            return  null;
        } finally {
            try {
                rs.close();
                pstmt.close();
            } catch (Exception e) {
            }        
        }         
        
    }   
    
     // method to get all stocktrace details
    public ObservableList updateStockTraceDataSpecificDate(String date){ 
        ObservableList stockTraceData = FXCollections.observableArrayList();
        
        try { 
            String sql = "SELECT * FROM stocktrace WHERE date_bought = ? ";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, date);
            rs = pstmt.executeQuery();
            while(rs.next()){
                
                stockTraceData.add(
                    rs.getString("product_code")+" | "+ rs.getString("product_name")+" | "+ rs.getString("quantity_in") +" "+rs.getString("type")+""
                            + " | "+ rs.getString("purchase_cost")+" | "+rs.getString("supplier")+" | "+rs.getString("department")+" | "+ rs.getString("date_bought")
                        
                );         
            }
            
            pstmt.close();
            rs.close();
            
            return stockTraceData;
            
        } catch (SQLException e) {
            return  null;
        } finally {
            try {
                rs.close();
                pstmt.close();
            } catch (Exception e) {
            }        
        }         
        
    } 
    
     // method to get all stocktrace details
    public ObservableList updateStockTraceDataByDepart(String depart){ 
        ObservableList stockTraceData = FXCollections.observableArrayList();
        
        try { 
            String sql = "SELECT * FROM stocktrace WHERE department = ? ";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, depart);
            rs = pstmt.executeQuery();
            while(rs.next()){
                
                stockTraceData.add(
                    rs.getString("product_code")+" | "+ rs.getString("product_name")+" | "+ rs.getString("quantity_in") +" "+rs.getString("type")+""
                            + " | "+ rs.getString("purchase_cost")+" | "+rs.getString("supplier")+" | "+rs.getString("department")+" | "+ rs.getString("date_bought")
                        
                );         
            }
            
            pstmt.close();
            rs.close();
            
            return stockTraceData;
            
        } catch (SQLException e) {
            return  null;
        } finally {
            try {
                rs.close();
                pstmt.close();
            } catch (Exception e) {
            }        
        }         
        
    }     
    
     public ObservableList updateDepartments(){ 
        ObservableList departs = FXCollections.observableArrayList();
        
        try { 
            String sql = "SELECT names FROM departments";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next()){
                
                departs.add(
                    rs.getString("names")
                );         
            }
            
            pstmt.close();
            rs.close();
            
            return departs;
            
        } catch (SQLException e) {
            return  null;
        } finally {
            try {
                rs.close();
                pstmt.close();
            } catch (Exception e) {
            }        
        }         
        
    }    
   
   // method to get all stocktrace details
    public ObservableList updateGeneralStockData(){ 
        ObservableList generalStockData = FXCollections.observableArrayList();
        
        try { 
            String sql = "SELECT * FROM stock ";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next()){
                
                generalStockData.add(
                    rs.getString("product_code")+" | "+ rs.getString("product_name")+" | "+ rs.getString("quantity_avail") +" "+rs.getString("type")+""
                            + " | "+ rs.getString("total_cost")+" | "+rs.getString("department")+" | "+rs.getString("date_reg")
                        
                );         
            }
            
            pstmt.close();
            rs.close();
            
            return generalStockData;
            
        } catch (SQLException e) {
            return  null;
        } finally {
            try {
                rs.close();
                pstmt.close();
            } catch (Exception e) {
            }        
        }         
        
    }     
    
   // method to get all stocktrace details
    public ObservableList updateIssuedUnitsData(){ 
        ObservableList generalStockData = FXCollections.observableArrayList();
        
        try { 
            String sql = "SELECT * FROM stockout ";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next()){
                
                generalStockData.add(
                    rs.getString("product_code")+" | "+ rs.getString("product_name")+" | "+ rs.getString("units_out") +" | "+rs.getString("given_to")+""
                            + " | "+ rs.getString("department")+" | "+rs.getString("date_given")+" | "+rs.getString("date_reg")
                        
                );         
            }
            
            pstmt.close();
            rs.close();
            
            return generalStockData;
            
        } catch (SQLException e) {
            return  null;
        } finally {
            try {
                rs.close();
                pstmt.close();
            } catch (Exception e) {
            }        
        }         
        
    }  
    
  // method to get all stocktrace details
    public ObservableList updateSuppliersData(){ 
        ObservableList suppliers = FXCollections.observableArrayList();
        
        try { 
            String sql = "SELECT * FROM supplier ";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next()){
                
                suppliers.add(
                    rs.getString("name")+" | "+ rs.getString("address")+" | "+ rs.getString("email") +" | "+rs.getString("contact")+""
                            + " | "+ rs.getString("date_approved")+" | "+rs.getString("date_reg")
                        
                );         
            }
            
            pstmt.close();
            rs.close();
            
            return suppliers;
            
        } catch (SQLException e) {
            return  null;
        } finally {
            try {
                rs.close();
                pstmt.close();
            } catch (Exception e) {
            }        
        }         
        
    }      

   // method to get all stocktrace details
    public ObservableList updateIssuedUnitsSpecificDate(String date){ 
        ObservableList generalStockData = FXCollections.observableArrayList();
        
        try { 
            String sql = "SELECT * FROM stockout WHERE date_given = ? ";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, date);
            rs = pstmt.executeQuery();
            while(rs.next()){
                
                generalStockData.add(
                    rs.getString("product_code")+" | "+ rs.getString("product_name")+" | "+ rs.getString("units_out") +" | "+rs.getString("given_to")+""
                            + " | "+ rs.getString("department")+" | "+rs.getString("date_given")+" | "+rs.getString("date_reg")
                        
                );         
            }
            
            pstmt.close();
            rs.close();
            
            return generalStockData;
            
        } catch (SQLException e) {
            return  null;
        } finally {
            try {
                rs.close();
                pstmt.close();
            } catch (Exception e) {
            }        
        }         
        
    }   
    
    
   // method to get all stocktrace details
    public ObservableList updateIssuedUnitsByDepart(String depat){ 
        ObservableList generalStockData = FXCollections.observableArrayList();
        
        try { 
            String sql = "SELECT * FROM stockout WHERE department = ? ";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, depat);
            rs = pstmt.executeQuery();
            while(rs.next()){
                
                generalStockData.add(
                    rs.getString("product_code")+" | "+ rs.getString("product_name")+" | "+ rs.getString("units_out") +" | "+rs.getString("given_to")+""
                            + " | "+ rs.getString("department")+" | "+rs.getString("date_given")+" | "+rs.getString("date_reg")
                        
                );         
            }
            
            pstmt.close();
            rs.close();
            
            return generalStockData;
            
        } catch (SQLException e) {
            return  null;
        } finally {
            try {
                rs.close();
                pstmt.close();
            } catch (Exception e) {
            }        
        }         
        
    }     
    
       // method to get all stocktrace details
    public ObservableList updateGeneralStockByDb(String dp){ 
        ObservableList generalStockData = FXCollections.observableArrayList();
        
        try { 
            String sql = "SELECT * FROM stock WHERE department = ? ";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, dp);
            rs = pstmt.executeQuery();
            while(rs.next()){
                
                generalStockData.add(
                    rs.getString("product_code")+" | "+ rs.getString("product_name")+" | "+ rs.getString("quantity_avail") +" "+rs.getString("type")+""
                            + " | "+ rs.getString("total_cost")+" | "+rs.getString("department")+" | "+rs.getString("date_reg")
                        
                );         
            }
            
            pstmt.close();
            rs.close();
            
            return generalStockData;
            
        } catch (SQLException e) {
            return  null;
        } finally {
            try {
                rs.close();
                pstmt.close();
            } catch (Exception e) {
            }        
        }         
        
    }   
    
    
   // method to send data and populate the tenant combobox 
  /* public ObservableList updateTenantNameComboBox(){ 
       ObservableList tenantNames = FXCollections.observableArrayList(); 
        try { 
            String sql = "SELECT tenant_name FROM tenants";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next()){ 
                tenantNames.add(rs.getString("tenant_name"));                
            }
            pstmt.close();
            rs.close();
            
            return tenantNames;
            
        } catch (SQLException e) {
        } finally {
            try {
                rs.close();
                pstmt.close();
            } catch (Exception e) {
            }        
        }
        
        return null;
    } 


    // method to retrieve tenant's profile
    public ObservableList retrieveTenantProfile(String tenantName){
        //List<String> profiledetails = new ArrayList<>();
        ObservableList profiledetails = FXCollections.observableArrayList(); 
        
        try {
            
            String houseNumber = null;
            
            String sql = "SELECT * FROM tenants WHERE tenant_name = ? ";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, tenantName.trim());
            rs=pstmt.executeQuery();

            while(rs.next()) {

                houseNumber = rs.getString("house_number");

                profiledetails.add(rs.getString("national_ID"));
                profiledetails.add(rs.getString("tenant_name"));
                profiledetails.add(rs.getString("phone"));
                profiledetails.add(rs.getString("house_number"));
                profiledetails.add(rs.getString("house_type"));
                profiledetails.add(rs.getString("occupants"));
                
                
                //enable checkbox for children
                boolean add7 = rs.getBoolean("hasChild");
                if (add7) {
                int add8 = rs.getInt("children");
                profiledetails.add(String.valueOf(add8));
                } else {
                profiledetails.add("0");
                }
                
                if (houseNumber != null) {
                
                String sql2 = "SELECT * FROM units WHERE house_number = ? ";
                pstmt = con.prepareStatement(sql2);
                pstmt.setString(1, houseNumber);
                ResultSet rs2 = pstmt.executeQuery();
                
                if (rs2.next()) {
                profiledetails.add(rs2.getString("water_meter"));
                profiledetails.add(rs2.getString("electric_meter"));
                }
                
                }
            }
            
            pstmt.close();
            rs.close();
            
            return profiledetails;
        
                 
            } catch (SQLException ev) {
            } finally {
            try {
                rs.close();
                pstmt.close();
            } catch (Exception e) {
            }        
        }
        
        return null;
    }
    
    
    
    //method to show user picture >> its not working analyse it later in details
      private void showUserPhoto(){
    
    try {
    String sql = "SELECT image FROM user_info WHERE username = ? ";
    pstmt = con.prepareStatement(sql);
    pstmt.setString(1, username.getText());
    rs=pstmt.executeQuery();
    
    if (rs.next()) {
    InputStream is = rs.getBinaryStream("image");
    OutputStream os = new FileOutputStream(new File("user.jpg") );
    byte [] content = new byte[1024];
    int size = 0;
    
    while ( (size=is.read(content)) != -1 ) {
    os.write(content, 0, size);
    }
    
    os.close();
    is.close();
    
    image2 = new Image("file:user.jpg",loggedInUser.getFitWidth(),loggedInUser.getFitHeight(),false,true);
    loggedInUser.setImage(image2);
    
    }
    } catch (Exception e) {
    }
    
    }  
    
    */ 
    
 
   
    
}// end of class
