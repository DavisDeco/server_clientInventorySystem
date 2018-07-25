/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daviscahtech_assetmanagement.Preferences;

import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;

/**
 *
 * @author davis
 */
public class ServerPost {
    public static final String PORT_CONFIG_FILE = "./Config/serverport.txt";
    private int port;

    public ServerPost() {
        setPort(8152);
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        if (port >0 ) {
            this.port = port;
        } else {
        }
    }
    
     // method to store preferences into json file
    public static  void  initConfig(){
    
        Writer writer = null;
        try {  
            ServerPost preferences = new ServerPost();
            Gson gson = new Gson();
            writer = new FileWriter(PORT_CONFIG_FILE);
            gson.toJson(preferences,writer);
        } catch (IOException ex) {
            Logger.getLogger(ServerPost.class.getName()).log(Level.SEVERE, null, ex);
        }finally{        
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerPost.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    //method to get preferences from the json file
    public static ServerPost getPreferences(){
        
        Gson gson = new Gson();
        ServerPost preferences = new ServerPost();
        
        try {
            preferences = gson.fromJson(new FileReader(PORT_CONFIG_FILE), ServerPost.class);
        } catch (FileNotFoundException ex) {
            initConfig();
            Logger.getLogger(ServerPost.class.getName()).log(Level.SEVERE, null, ex);
        }    
        return preferences;
    }
    
    //method to update preferences to the system json file
    
    public static void writePreferences(ServerPost preferences){
        
         Writer writer = null;
        try { 
            Gson gson = new Gson();
            writer = new FileWriter(PORT_CONFIG_FILE);
            gson.toJson(preferences,writer);
            
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);  
            successAlert.setTitle("SUCCESS");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Setting changes were successful");
            successAlert.showAndWait();
            
        } catch (IOException ex) {
            Alert bookError = new Alert(Alert.AlertType.ERROR);  
            bookError.setTitle("FAILED");
            bookError.setHeaderText(null);
            bookError.setContentText("Changes update failed!\nDefault values will still be used");
            bookError.showAndWait();
        }finally{        
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerPost.class.getName()).log(Level.SEVERE, null, ex);
            }
        }   
        
    }     
    
    
    
}// end of class
