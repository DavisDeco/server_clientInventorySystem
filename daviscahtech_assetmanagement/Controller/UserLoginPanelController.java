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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
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
public class UserLoginPanelController implements Initializable {
    
    Image errorFlag = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/errorFlag.png").toExternalForm());
    ImageView errorImage = new ImageView(errorFlag);
    
    Image thumbsImage = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/thumbs.png").toExternalForm());
    ImageView imageSuccess = new ImageView(thumbsImage);
    
    Image partnerImage = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/partner.png").toExternalForm());
    ImageView partnerImageView = new ImageView(partnerImage);
    
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
    @FXML
    private ListView<String> ourservicesOperation;
    
    LoginUtils loginUtils;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        con = DatabaseConnection.connectDb();        
        populateServiceListView();
        
        loginUtils = new LoginUtils();
    }    
    //method to populate ourServiceListView
    private void populateServiceListView(){ 
        
         ObservableList ourservicesData = FXCollections.observableArrayList();
                 
         ourservicesData.add("WEBSITE SERVICES\n");
         ourservicesData.add("\tWebsite Designing\n");
         ourservicesData.add("\tWebsite Development\n");
         ourservicesData.add("\tWebsite Re-designing\n");
         ourservicesData.add("\tWebsite Updating\n\n");
         ourservicesData.add("SOFTWARE SERVICES\n");
         ourservicesData.add("\tRecord management systems\n");
         ourservicesData.add("\tRobust database systems\n");
         ourservicesData.add("\tDesktop client systems\n");
         ourservicesData.add("\tNetworking softwares\n\n");
         ourservicesData.add("MOBILE APP SERVICES\n");
         ourservicesData.add("\tAndroid development\n");
         
         // show this details on the listview
        ourservicesOperation.getItems().setAll(ourservicesData);
        
    }
    
    @FXML
    private void loginAuthenthicationOperation(ActionEvent event) throws SQLException, IOException {
        
        
        String uName = DigestUtils.sha1Hex(loginUserName.getText());
        String pWord = DigestUtils.sha1Hex(loginPassword.getText());
        
        //Retrieve master details from DB where user level is equal to 0       
        String sql = "SELECT * FROM userinfo WHERE userName=? AND userPassword= ?";
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, loginUserName.getText());
        pstmt.setString(2, pWord);

        rs=pstmt.executeQuery();         
        
        
                
        if (uName.equals(loginUtils.getN()) && pWord.equals(loginUtils.getZ())) {
            closeStage();
            //cretate stage with specified owner and modality                
            Parent root = FXMLLoader.load(getClass().getResource("/daviscahtech_assetmanagement/FXML/mainPage.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Asset Check");
            stage.getIcons().add(new Image(UserLoginPanelController.class.getResourceAsStream("/daviscahtech_assetmanagement/Resources/assetcheckpng.png")));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        
        } else if (rs.next()) {
            closeStage();
            //cretate stage with specified owner and modality                
            Parent root = FXMLLoader.load(getClass().getResource("/daviscahtech_assetmanagement/FXML/mainPage.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Asset Check");
            stage.getIcons().add(new Image(UserLoginPanelController.class.getResourceAsStream("/daviscahtech_assetmanagement/Resources/assetcheckpng.png")));
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

    
    @FXML
    private void termsAndConditionOperation(ActionEvent event) {
         String ms = "Daviscah Tech Ltd operates with a mission to provide condusive teaching and learning environment.\n\n"
                 + "School Asset Check software is strictly the property of this company and Selling or distribution of this"
                 + " software without consent and permission from Daviscah Tech Ltd shall lead to legal prosecution and punishment.\n\n"
                 + "On installing and launching the software, contact us to provide you with 'username' and 'password' for login.\n\n";           
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Asset Check Terms and Conditions.");
            alert.setGraphic(partnerImageView);
            alert.setHeaderText(null);
            alert.setContentText(ms);
            alert.showAndWait();
    }
}// end of class
