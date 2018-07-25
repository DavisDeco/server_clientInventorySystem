/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daviscahtech_assetmanagement;

import daviscahtech_assetmanagement.Dao.DatabaseConnection;
import java.sql.Connection;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 *Parent root = FXMLLoader.load(getClass().getResource("/daviscahtech_assetmanagement/FXML/mainPage.fxml"));
 * @author davis
 */
public class Daviscahtech_AssetManagement extends Application {
    Image errorFlag = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/errorFlag.png").toExternalForm());
    ImageView errorImage = new ImageView(errorFlag);
    Connection con;
    
    @Override
    public void start(Stage stage) throws Exception {
          // determine if we are conneted to the database, if true run the programme else terminate.
        con = DatabaseConnection.connectDb();
        if (con==null) {  
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("System Connetion Error!");
            alert.setGraphic(errorImage);
            alert.setHeaderText(null);
            alert.setContentText("Application - Database Connection Unsuccessful !!! \n"
                    + " Ensure the Server/database is Active (ON) to proceed.\n\n"
                    + "If Server is on and this error occurs then server login details are not same as the ones saved in Asset Check system.\n"
                    + "Send us an email for more information or call us for help.\n\t"
                    + "info@daviscahtech.com\n\t"
                    + "0706009803/0796514490");
            alert.showAndWait();
             
            Platform.exit();
        } else {
         
//      Parent root = FXMLLoader.load(getClass().getResource("/daviscahtech_assetmanagement/FXML/mainPage.fxml"));      
        Parent root = FXMLLoader.load(getClass().getResource("/daviscahtech_assetmanagement/FXML/userLoginPanel.fxml"));      
        Scene scene = new Scene(root);        
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/daviscahtech_assetmanagement/Resources/assetcheckpng.png")));
        stage.setTitle("School Asset Check");
        stage.show();
        }
               
       
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    
}
