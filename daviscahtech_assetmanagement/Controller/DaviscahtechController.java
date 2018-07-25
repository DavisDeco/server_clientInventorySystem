/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daviscahtech_assetmanagement.Controller;

import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author davis
 */
public class DaviscahtechController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void loadDaviscahtechWebsite(ActionEvent event) {
        
        openWebPage("http://daviscahtech.com");
    }
    
    //meyhod to call when opening any website page
    public static void openWebPage(String url){
    
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
        }    
    }    
    
}
