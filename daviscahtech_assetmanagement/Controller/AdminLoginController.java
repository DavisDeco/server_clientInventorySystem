/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daviscahtech_assetmanagement.Controller;

import daviscahtech_assetmanagement.Dao.DatabaseConnection;
import daviscahtech_assetmanagement.Utils.LoginUtils;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * FXML Controller class
 *
 * @author davis
 */
public class AdminLoginController implements Initializable {
    
        //###############################For DB link##########################
    Connection con = null;
    PreparedStatement pstmt;
    ResultSet rs;
    Statement st;

    @FXML
    private TextField loginUserName;
    @FXML
    private PasswordField loginPassword;
    @FXML
    private ImageView warmingImage;
    @FXML
    private Text warningText;
    
    LoginUtils loginUtils;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        con = DatabaseConnection.connectDb();
        
        loginUtils = new LoginUtils();
    }    

    @FXML
    private void loginAuthenthicationOperation(ActionEvent event) throws SQLException, IOException {
        
        
        String uName = DigestUtils.sha1Hex(loginUserName.getText());
        String pWord = DigestUtils.sha1Hex(loginPassword.getText());
        
        //Retrieve master details from DB where user level is equal to 0       
        String sql = "SELECT * FROM userinfo WHERE userName=? AND userPassword= ? AND userLevel = ?";
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, loginUserName.getText());
        pstmt.setString(2, pWord);
        pstmt.setBoolean(3, false);

        rs=pstmt.executeQuery();         
        
        
                
        if (uName.equals(loginUtils.getN()) && pWord.equals(loginUtils.getZ())) {
            closeStage();
            //cretate stage with specified owner and modality                
            Parent root = FXMLLoader.load(getClass().getResource("/daviscahtech_assetmanagement/FXML/adminControlPanel.fxml"));
            Stage stage = new Stage(StageStyle.UTILITY);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Admin control panel");
            stage.getIcons().add(new Image(AdminLoginController.class.getResourceAsStream("/daviscahtech_assetmanagement/Resources/assetcheckpng.png")));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        
        } else if (rs.next()) {
            closeStage();
            //cretate stage with specified owner and modality                
            Parent root = FXMLLoader.load(getClass().getResource("/daviscahtech_assetmanagement/FXML/adminControlPanel.fxml"));
            Stage stage = new Stage(StageStyle.UTILITY);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Admin control panel");
            stage.getIcons().add(new Image(AdminLoginController.class.getResourceAsStream("/daviscahtech_assetmanagement/Resources/assetcheckpng.png")));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();            
            
        } else {
            warmingImage.setVisible(true);
            warningText.setText("Invalid username or password ");
        }        
        
    }
    
        //method to close stage after login in
    private void closeStage(){
        ( (Stage)loginUserName.getScene().getWindow() ).close();
    }
    
}// end of class
