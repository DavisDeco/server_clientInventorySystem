/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daviscahtech_assetmanagement.Controller;

import daviscahtech_assetmanagement.Dao.DatabaseConnection;
import daviscahtech_assetmanagement.Preferences.LocalServerPreference;
import daviscahtech_assetmanagement.Preferences.ServerPost;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class
 *
 * @author davis
 */
public class AdminControlPanelController implements Initializable {
    
    Image errorFlag = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/errorFlag.png").toExternalForm());
    ImageView errorImage = new ImageView(errorFlag);
    
    Image errorNotify = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/errornotification.png").toExternalForm());
    ImageView errorNotifyImageView = new ImageView(errorNotify);
    
    Image thumbsImage = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/thumbs.png").toExternalForm());
    ImageView imageSuccess = new ImageView(thumbsImage);    
    
    Image goodImage = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/good.png").toExternalForm());
    ImageView successNotiyiImageView = new ImageView(goodImage);
    
    Image info = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/info.png").toExternalForm());
    ImageView infoNotiyiImageView = new ImageView(info);
    
           
        //NOTIFICATION BUILDER VARIABLE
    Notifications notificationBuilder;  
    //###############################For DB link##########################  warningImagevies
    Connection con = null;
    PreparedStatement pstmt;
    ResultSet rs;
    Statement st;
    

    @FXML
    private ImageView redbulletimageview;
    @FXML
    private Text displayItemsToDelete;
    @FXML
    private TextField serverUsername;
    @FXML
    private PasswordField serverPassword;
    @FXML
    private TextField portNumber;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        con= DatabaseConnection.connectDb();
        scanForMarkedItemsToDelete();
    } 
    
     //Method to take and open any window
     private void loadWindow(String loc, String title) throws IOException{
        //cretate stage with specified owner and modality                
        Parent root = FXMLLoader.load(getClass().getResource(loc));
        Stage stage = new Stage(StageStyle.UTILITY);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.getIcons().add(new Image(MainPageController.class.getResourceAsStream("/daviscahtech_assetmanagement/Resources/store.png")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();   
    }    

    @FXML
    private void openPromoteStudentWindow(ActionEvent event) throws IOException {
        loadWindow("/daviscahtech_assetmanagement/FXML/adminStudentPanel.fxml", "Student detail management");
        
    }

    @FXML
    private void openRegisterSystemUserWindow(ActionEvent event) throws IOException {
        loadWindow("/daviscahtech_assetmanagement/FXML/manageSystemUsers.fxml", "Manage System users");
    }
    
    @FXML
    private void openIssuedItemsToDelete(ActionEvent event) throws IOException {
        loadWindow("/daviscahtech_assetmanagement/FXML/deleteIssuedAsAdmin.fxml", "Delete issued products from store");
    }

    @FXML
    private void openReturnedItemsToDelete(ActionEvent event) throws IOException {
        loadWindow("/daviscahtech_assetmanagement/FXML/deleteReturnedAsAdmin.fxml", "Delete returned products to the store");
    }
    
    //method to validate if input is an int
    private boolean isInt(TextField input){
        input.setStyle("-fx-border-color: blue; -fx-border-radius: 15;-fx-background-radius: 20;");
        try {
            int number = Integer.parseInt(input.getText());
            System.out.println("Input is" + number );
            return true;
        } catch (Exception e) {
            input.setStyle("-fx-border-color: red;");
            input.setText(null);
            input.setPromptText("Only digits/numbers must be filled");
            return false;
        }
    }    
    
    private void scaleAnimation() {
        redbulletimageview.setVisible(true);
        ScaleTransition st = new ScaleTransition(Duration.seconds(0.5), redbulletimageview);
        st.setFromX(2.0);
        st.setToX(0.50);
        st.setFromY(2.0);
        st.setToY(0.50);
        st.setCycleCount(ScaleTransition.INDEFINITE);
        st.setAutoReverse(true);
        st.play();
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
    
    public void infoNotification(Pos pos,String title){
       notificationBuilder = Notifications.create()
                .title("Information")
                .text(title)
                .graphic(infoNotiyiImageView)
                .hideAfter(Duration.seconds(10))
                .position(pos)
                .onAction((ActionEvent event) -> {
                    System.out.println("Notification created");
                });
        notificationBuilder.show();
                
    }

    @FXML
    private void viewAllStockeditemForDelete(ActionEvent event) throws IOException {
        
        try { 
            String sql = "SELECT * FROM stocktrace WHERE allowdelete = ? ";
            pstmt = con.prepareStatement(sql);
            pstmt.setBoolean(1, true);
            rs = pstmt.executeQuery();
            if(rs.next()){
                 loadWindow("/daviscahtech_assetmanagement/FXML/viewAllForDelete.fxml", "Stocked Product to delete");           
            } else {
                infoNotification(Pos.BOTTOM_RIGHT, "No products marked for delete by system user.");
            }
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

    @FXML
    private void deleteAllMarkedStockedItems(ActionEvent event) {
        try { 
            String sql = "SELECT * FROM stocktrace WHERE allowdelete = ? ";
            pstmt = con.prepareStatement(sql);
            pstmt.setBoolean(1, true);
            rs = pstmt.executeQuery();
            if(rs.next()){
                String query = "DELETE FROM stocktrace WHERE allowdelete = ? ";
                pstmt = con.prepareStatement(query);
                pstmt.setBoolean(1, true);
               int i = pstmt.executeUpdate();
              
                if (i>0) {
                    successNotification(Pos.BOTTOM_RIGHT, "The marked products have been deleted.");
                }            
            } else {
                infoNotification(Pos.BOTTOM_RIGHT, "No products marked for delete by system user.");
            }
            
            scanForMarkedItemsToDelete();
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
    
    private void scanForMarkedItemsToDelete(){
    
        try { 
            String sql = "SELECT * FROM stocktrace WHERE allowdelete = ? ";
            pstmt = con.prepareStatement(sql);
            pstmt.setBoolean(1, true);
            rs = pstmt.executeQuery();
            if(rs.next()){
                scaleAnimation();              
            } else {
                redbulletimageview.setVisible(false);
            }
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

    @FXML
    private void openServerInstruction(ActionEvent event) {
       
        String msg = "Asset Check use Mysql Database from a local server installed in you machine."
                + " This must be provided for Asset Check software to work. "
                + "The step simply enhances security to the database and ensure integrity of data."
                + " Default details were used to launch the software.\n\n"
                + "Before changing local server's login details ALWAYS ensure you have changed those "
                + "details here in Asset Check setting FIRST, before you change in the Local server's side."
                + " Otherwise the Asset Check will not start since the previous login details will be in use.\n\n"
                + "You can change local server's login details as many time you want whenever you suspect"
                + " access has been compromised as long as you remember to effect those changes from this"
                + " software first.";
                
        Alert errorIssue = new Alert(Alert.AlertType.ERROR);
        errorIssue.setTitle("Local server instructions");
        errorIssue.setGraphic(infoNotiyiImageView);
        errorIssue.setHeaderText(null);
        errorIssue.setContentText(msg);
        errorIssue.showAndWait();
    }

    @FXML
    private void saveServerChangedDetails(ActionEvent event) {
        
        if (serverPassword.getText().isEmpty() || serverUsername.getText().isEmpty()) {
            String msg = null;
            if (serverPassword.getText().isEmpty()) {
                msg = "Server Password can not be empty, fill password";
            } if (serverUsername.getText().isEmpty()) {
                msg = "Server username can not be empty, fill username";
            }
            
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Setting failed.");
            alert.setGraphic(errorImage);
            alert.setHeaderText(null);
            alert.setContentText("Changing server username and password failed.\n" + msg); 
            alert.showAndWait();
            
        } else {
            LocalServerPreference preference = LocalServerPreference.getPreferences();
        
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Authenthication setting");
            alert.setGraphic(infoNotiyiImageView);
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to change local server login detals from the current settings?");         
             Optional <ButtonType> obt = alert.showAndWait();

            if (obt.get()== ButtonType.OK) {
            preference.setE(serverUsername.getText());
            preference.setD(serverPassword.getText());

            LocalServerPreference.writePreferences(preference);

            serverPassword.clear();
            serverUsername.clear();

            }
        }        
        
        
    }

    @FXML
    private void changeportNumberOperation(ActionEvent event) {
        
        if (portNumber.getText().isEmpty()) {
            String msg = null;
            if (portNumber.getText().isEmpty()) {
                msg = "Enter port number.";
            } 
            
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Setting failed.");
            alert.setGraphic(errorImage);
            alert.setHeaderText(null);
            alert.setContentText("Changing port failed.\n" + msg); 
            alert.showAndWait();
            
        } else {
            if (isInt(portNumber)) {
                
            
            ServerPost preference = ServerPost.getPreferences();
        
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Authenthication setting");
            alert.setGraphic(imageSuccess);
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to change communication port from the current port?");         
             Optional <ButtonType> obt = alert.showAndWait();

            if (obt.get()== ButtonType.OK) {
            preference.setPort(Integer.parseInt(portNumber.getText().trim()));

            ServerPost.writePreferences(preference);
            portNumber.clear();
            
            Alert at = new Alert(Alert.AlertType.CONFIRMATION);
            at.setTitle("Ok");
            at.setGraphic(imageSuccess);
            at.setHeaderText(null);
            at.setContentText("Port has been changed");
            at.showAndWait();

            }
          }  
        }        
                
        
        
    }


    
    
    
}// end of class
