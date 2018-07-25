/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daviscahtech_assetmanagement.Controller;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import daviscahtech_assetmanagement.Dao.DatabaseConnection;
import daviscahtech_assetmanagement.Pojo.Student;
import daviscahtech_assetmanagement.Utils.HeaderFooterPageEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class
 *
 * @author davis
 */
public class AdminStudentPanelController implements Initializable {
    Image errorFlag = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/errorFlag.png").toExternalForm());
    ImageView errorImage = new ImageView(errorFlag);
    
    Image errorNotify = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/errornotification.png").toExternalForm());
    ImageView errorNotifyImageView = new ImageView(errorNotify);
    
    Image thumbsImage = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/thumbs.png").toExternalForm());
    ImageView imageSuccess = new ImageView(thumbsImage);    
    
    Image goodImage = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/good.png").toExternalForm());
    ImageView successNotiyiImageView = new ImageView(goodImage);
    
    Image studentImage = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/student.png").toExternalForm());
    ImageView pupilImageView = new ImageView(studentImage); 
    
    Image pdfImage = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/pdf.png").toExternalForm());
    ImageView pdfImageView = new ImageView(pdfImage);    
    
        
    //###############################For DB link##########################  warningImagevies
    Connection con = null;
    PreparedStatement pstmt;
    ResultSet rs;
    Statement st;
    
    //variables to hold school info
    String schoolName = null;
    String schoolContact = null;
    String schoolAddress = null;
    String schoolRegion = null;
    String schoolEmail = null;
    String schoolWebsite = null;    
    
    //NOTIFICATION BUILDER VARIABLE
    Notifications notificationBuilder; 
    
    //ID variable holder
    int studentIDHolder;
    
    private File file;
    private File excelFile;    
    private File pdfFile;
    private FileChooser fileChooser;
    private FileChooser pdfFileChooser;
    private FileChooser excelFileChooser;
    //instance of javafx image
    private Image image;
    /*
    instance of Desktop and Window
    private final Desktop desktop = Desktop.getDesktop();
    */
    private Window Stage;    
    
    

    @FXML
    private ImageView studentImageView;
    @FXML
    private TextField student_id;
    @FXML
    private TextField student_name;
    @FXML
    private TextField student_searchField;
    @FXML
    private TableView<Student> studentTable;
    @FXML
    private TableColumn<Student, String> studentColumn_id;
    @FXML
    private TableColumn<Student, String> studentColumn_name;
    @FXML
    private TableColumn<Student, String> studentColumn_class;
    @FXML
    private TableColumn<Student, String> studentColumn_dateReg;
    private final ObservableList<Student> studentData = FXCollections.observableArrayList();
    @FXML
    private Text displayStudentClass;
    private String retrievedImagePath;
    @FXML
    private ComboBox<String> promoteSingleStudentCombobox;
    @FXML
    private ComboBox<String> depromoteStudentCombobox;
    @FXML
    private ComboBox<String> finalStudentComboClass;
    @FXML
    private ComboBox<String> promote_comboCurrentClass;
    @FXML
    private ComboBox<String> promote_comboTargetClass;
    private final ObservableList<String> classData = FXCollections.observableArrayList();
    private final ObservableList<String> isFinalclassData = FXCollections.observableArrayList();


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        con = DatabaseConnection.connectDb();
        updateStudentTable();
        updateStudentClassNamesCombo();
        updateFinalStudentClassNamesCombo();
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
    
    private void setDefaultImage(ImageView imageView){    
        Image defaultImage = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/student.png").toExternalForm(),imageView.getFitWidth(),imageView.getFitHeight(),false,true);
        imageView.setImage(defaultImage); 
    }     
    
       //Method to open a fileChooser dialog box
    public void fileChooserOpener(){
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files","*.*"),
                new FileChooser.ExtensionFilter("Text Files","*txt"),
                new FileChooser.ExtensionFilter("Image Files","*.png","*.jpg","*.gif"),
                new FileChooser.ExtensionFilter("Audio Files","*wav","*.mp3","*.mp4","*.acc")  
//               new FileChooser.ExtensionFilter("All Files","*.*")        
//               new FileChooser.ExtensionFilter("Excel Files","*.xslx") 
//                new FileChooser.ExtensionFilter("Text Files","*.txt"),
//                new FileChooser.ExtensionFilter("Word Files","*.docx"),
//                new FileChooser.ExtensionFilter("Image Files","*.png","*.jpg","*.gif"),
//                new FileChooser.ExtensionFilter("Audio Files","*.wav","*.mp3","*.mp4","*.acc") 
                
        );   
    }    
    
    private void updateStudentTable(){
        
        studentColumn_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        studentColumn_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        studentColumn_class.setCellValueFactory(new PropertyValueFactory<>("studentClass"));
        studentColumn_dateReg.setCellValueFactory(new PropertyValueFactory<>("dateReg"));

        try { 
            String sql = "SELECT * FROM student ";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next()){
                
                studentData.add( new Student(
                        rs.getString("id_no"),
                        rs.getString("name"),
                        rs.getString("class"),
                        rs.getString("date_reg")
                ));         
            }
            //load items to the table
            studentTable.setItems(studentData);
            studentTable.setPlaceholder(new Label("No student matches the details provided", errorImage));
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
    
    private void refreshStudentTable(){    
        studentData.clear();
        updateStudentTable();
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

    
    private void clearStudentFields(){
    
        student_id.clear();
        student_name.clear();
        setDefaultImage(studentImageView);
        displayStudentClass.setText(null);
    }

    @FXML
    private void student_searchFieldMouseClicked(MouseEvent event) {
        changeAnyTableOnSearch(studentData, student_searchField, studentTable);
    }

    @FXML
    private void studentTableMouseClickerd(MouseEvent event) {
        clearStudentFields();
                try {
                    Student sp = studentTable.getSelectionModel().getSelectedItem();
                    
                    String sql = "SELECT * FROM student WHERE id_no = ? ";
                    pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, sp.getId());
                    rs=pstmt.executeQuery();
                    
                    if (rs.next()) {
                        
                        studentIDHolder = rs.getInt("id");
                        
                        String add1 = rs.getString("id_no");
                        student_id.setText(add1); 
                        String add2 = rs.getString("name");
                        student_name.setText(add2);
                        String add3 = rs.getString("class");
                        displayStudentClass.setText(add3);
                        
                        String imagePath = rs.getString("image");
                        retrievedImagePath = imagePath;
                         
                        try {
                            image = new Image(new FileInputStream(imagePath),studentImageView.getFitWidth(),studentImageView.getFitHeight(),false,true);
                            studentImageView.setImage(image);                  
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(StudentsController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    }else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Invalid");
                        alert.setHeaderText(null);
                        alert.setContentText("Record not Found!");
                        alert.showAndWait(); 
                    }                    
                } catch (SQLException ev) {
                }        
        
    }

    @FXML
    private void studentTableKeyReleased(KeyEvent event) {
        clearStudentFields();
            studentTable.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.DOWN) {
                
                try {
                    Student sp = studentTable.getSelectionModel().getSelectedItem();
                    
                    String sql = "SELECT * FROM student WHERE id_no = ? ";
                    pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, sp.getId());
                    
                    rs=pstmt.executeQuery();
                    
                    if (rs.next()) {
                        
                        studentIDHolder = rs.getInt("id");
                        
                        String add1 = rs.getString("id_no");
                        student_id.setText(add1); 
                        String add2 = rs.getString("name");
                        student_name.setText(add2);
                        String add3 = rs.getString("class");
                        displayStudentClass.setText(add3);
                        
                        String imagePath = rs.getString("image");
                        retrievedImagePath = imagePath;
                         
                        try {
                            image = new Image(new FileInputStream(imagePath),studentImageView.getFitWidth(),studentImageView.getFitHeight(),false,true);
                            studentImageView.setImage(image);                  
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(StudentsController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    }else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Invalid");
                        alert.setHeaderText(null);
                        alert.setContentText("Record not Found!");
                        alert.showAndWait(); 
                    }                    
                } catch (SQLException ev) {
                }
            }
        });         
        
    }

    @FXML
    private void student_refreshTableOperation(ActionEvent event) {
        refreshStudentTable();
    }
    
       //Method to monitor tenant's profile update on table upon searching
    private void changeAnyTableOnSearch(ObservableList data,TextField serchField,TableView tableView){
        
        //wrap the observablelist in a FilteredList()
        FilteredList<Student> filteredData =  new FilteredList<>(data,p -> true);
        
        //set the filter predicate whenever the filter changes
        serchField.textProperty().addListener((observable,oldValue,newValue)->{
                
            filteredData.setPredicate(Student -> {
            
                //if filter text is empty, display all product
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                
                //compare product id and product name of every product with the filter text
                String lowerCaseFilter =newValue.toLowerCase();
               
                if (Student.getId().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches product Id
                } else if (Student.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches product Id
                }  else if (Student.getStudentClass().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches product Id
                }  
                return false; // Do not match            
            });
        });
        
        //wrap the filteredList in a sortedList
        SortedList<Student> sortedData = new SortedList<>(filteredData);
        
        //bind the sortedData to the Tableview
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        
        //add sorted and filtered data to the table
        tableView.setItems(sortedData); 
    } 





   private void updateStudentClassNamesCombo(){  
        try { 
            String sql = "SELECT className FROM class";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next()){                
                classData.add( rs.getString("className")); 
            }
            // populate class combo box 
            promoteSingleStudentCombobox.setItems(classData);
            depromoteStudentCombobox.setItems(classData); 
            promote_comboCurrentClass.setItems(classData); 
            promote_comboTargetClass.setItems(classData); 
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
   
   private void refreshStudentClassCombo(){   
       classData.clear();
       updateStudentClassNamesCombo();
   } 

   private void updateFinalStudentClassNamesCombo(){  
        try { 
            String sql = "SELECT className FROM class WHERE isFinal = ? ";
            pstmt = con.prepareStatement(sql);
            pstmt.setBoolean(1, true);
            rs = pstmt.executeQuery();
            while(rs.next()){                
                isFinalclassData.add( rs.getString("className")); 
            }
            // populate class combo box ; 
            finalStudentComboClass.setItems(isFinalclassData);
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
   
   private void refreshFinalStudentClassCombo(){   
       isFinalclassData.clear();
       updateFinalStudentClassNamesCombo();
   }   

    @FXML
    private void refresh_promoteSingleStudentCombobox(ActionEvent event) {
        refreshStudentClassCombo();
    }

    @FXML
    private void promoteSingleStudentOperation(ActionEvent event) {
        
         if (promoteSingleStudentCombobox.getSelectionModel().isEmpty() || student_id.getText().isEmpty() || student_name.getText().isEmpty()) { 
                 
                 String msg = null;
            
                if(student_id.getText().isEmpty()) {
                   msg = "Student to be promoted not found, retrieve the student first.";
                } else if (depromoteStudentCombobox.getSelectionModel().isEmpty()) {
                     msg = "Target class is not choosen, you must choose it";                 
                } else if(student_name.getText().isEmpty()) {
                   msg = "Student name not found, retrieve the student first.";
                }
             
            Alert errorIssue = new Alert(Alert.AlertType.ERROR);
            errorIssue.setTitle("Failed");
            errorIssue.setGraphic(errorImage);
            errorIssue.setHeaderText(null);
            errorIssue.setContentText("The student could not be promoted.\n"
                        + msg);
            errorIssue.showAndWait(); 
            
        }else { 
            
            String destClass =  promoteSingleStudentCombobox.getSelectionModel().getSelectedItem();
            
            try {
                    String sql = "UPDATE student SET class = ? WHERE"
                            + " id_no = '"+student_id.getText()+"' AND "
                            + "name = '"+student_name.getText()+"' AND class = '"+displayStudentClass.getText()+"'      ";
                    
                    pstmt= con.prepareStatement(sql);

                    pstmt.setString(1, destClass);

                    int i = pstmt.executeUpdate(); // load data into the database             
                    if (i>0) {
                        //show alert before promoting
                    String msg = ""+student_name.getText()+""
                            + " has successfully been promoted to "+promoteSingleStudentCombobox.getSelectionModel().getSelectedItem()+ ".\n\n";
                        successNotification(Pos.BOTTOM_RIGHT, msg);
                    } else {
                        errorNotification(Pos.BOTTOM_RIGHT, "OOPS! Could not promote Student due  to internal error.");
                    } 
                    
                    pstmt.close();
                    clearStudentFields();
                    refreshStudentTable();
                    
                } catch (Exception e) {
                    errorNotification(Pos.BOTTOM_RIGHT, "OOPS! Could not promote Student due  to internal error."); 
                }finally {
                   try {
                        rs.close();
                        pstmt.close();
                    } catch (Exception e) {}               
               }
        }  
    }

    @FXML
    private void refresh_depromoteStudentCombobox(ActionEvent event) {
        refreshStudentClassCombo();
    }

    @FXML
    private void depromoteSingleStudentOperation(ActionEvent event) {
        
        
         if (depromoteStudentCombobox.getSelectionModel().isEmpty() || student_id.getText().isEmpty() || student_name.getText().isEmpty()) { 
                 
                 String msg = null;
            
                if(student_id.getText().isEmpty()) {
                   msg = "Student to be de-promoted not found, retrieve the student first.";
                } else if (depromoteStudentCombobox.getSelectionModel().isEmpty()) {
                     msg = "Target class is not choosen, you must choose it";                 
                } else if(student_name.getText().isEmpty()) {
                   msg = "Student name not found, retrieve the student first.";
                }
             
            Alert errorIssue = new Alert(Alert.AlertType.ERROR);
            errorIssue.setTitle("Failed");
            errorIssue.setGraphic(errorImage);
            errorIssue.setHeaderText(null);
            errorIssue.setContentText("The student could not be de-promoted.\n"
                        + msg);
            errorIssue.showAndWait(); 
            
        }else { 
            
            String destClass =  depromoteStudentCombobox.getSelectionModel().getSelectedItem();
            
            try {
                    String sql = "UPDATE student SET class = ? WHERE"
                            + " id_no = '"+student_id.getText()+"' AND "
                            + "name = '"+student_name.getText()+"' AND class = '"+displayStudentClass.getText()+"'      ";
                    
                    pstmt= con.prepareStatement(sql);

                    pstmt.setString(1, destClass);

                    int i = pstmt.executeUpdate(); // load data into the database             
                    if (i>0) {
                        //show alert before promoting
                    String msg = ""+student_name.getText()+""
                            + " has successfully been de-promoted to "+depromoteStudentCombobox.getSelectionModel().getSelectedItem()+ ".\n\n";
                        successNotification(Pos.BOTTOM_RIGHT, msg);
                    } else {
                        errorNotification(Pos.BOTTOM_RIGHT, "OOPS! Could not de-promote Student due  to internal error.");
                    } 
                    
                    pstmt.close();
                    clearStudentFields();
                    refreshStudentTable();
                    
                } catch (Exception e) {
                    errorNotification(Pos.BOTTOM_RIGHT, "OOPS! Could not de-promote Student due  to internal error."); 
                }finally {
                   try {
                        rs.close();
                        pstmt.close();
                    } catch (Exception e) {}               
               }
        }        
        
    }

    @FXML
    private void refresh_finalStudentComboClass(ActionEvent event) {
        refreshFinalStudentClassCombo();
    }

    @FXML
    private void deleteSpecificStudentInClass(ActionEvent event) {
        
        if (finalStudentComboClass.getSelectionModel().isEmpty()) {
            
            Alert errorIssue = new Alert(Alert.AlertType.ERROR);
            errorIssue.setTitle("Delete Failed");
            errorIssue.setGraphic(errorImage);
            errorIssue.setHeaderText(null);
            errorIssue.setContentText("The students has failed DELETE operation.\n"
                        + "Ensure the class to be removed students is selected.");
            errorIssue.showAndWait(); 
            
        }else {                        
            String thisClass = finalStudentComboClass.getSelectionModel().getSelectedItem();
            
            try {
                String sql = "SELECT * FROM student WHERE class = ?";
                pstmt = con.prepareStatement(sql);
                pstmt.setString(1, thisClass);
                
                rs = pstmt.executeQuery();                
                if (rs.next()) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Delete Students");
                        alert.setGraphic(pupilImageView);
                        alert.setHeaderText(null);
                        alert.setContentText("Are you sure to delete students form "+thisClass+" ?");         
                        Optional <ButtonType> obt = alert.showAndWait();

                        if (obt.get()== ButtonType.OK) {
                            try {
                           String query = "DELETE FROM student WHERE class = ? ";
                           pstmt = con.prepareStatement(query);
                           pstmt.setString(1, thisClass);
                           pstmt.executeUpdate();            
                           pstmt.close();            
                           } catch (SQLException ex) {
                               Logger.getLogger(MainPageController.class.getName()).log(Level.SEVERE, null, ex);
                           }finally {
                                try {
                                    rs.close();
                                    pstmt.close();
                                } catch (Exception e) {
                                }        
                            }   
                       }
                        refreshStudentTable();
                } else {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("OOPS!");
                        alert.setGraphic(pupilImageView);
                        alert.setHeaderText(null);
                        alert.setContentText("There are no students in "+thisClass+"\n"
                                + "The class is empty. You can add or promote students into this class."); 
                        alert.showAndWait();
                }

            } catch (Exception e) {
            }
        }        
        
    }

    @FXML
    private void loadInstructionOperation(ActionEvent event) {
         //show instruction alert before promoting class
            String ms = "Be careful will promoting a class to the next level. All students in a particular class will be "
                    + "automatically be promoted.\n\n"
                    + "To stop promoting a particular student to the next level, you have to do that"
                    + " manually after promoting all the students including them."
                    + " This can be done in the above previous tab 'Configure individual Students & Final classes.\n\n"
                    + "To depromote a class, the function is the same but reverse the TARGET CLASS.\n\n"
                    + "VERY IMPORTANT: Promoting a class starts from upper final classes then proceeding downwards, that is,"
                    + "First delete students from final classes to make them empty, then promote the lower preceeding class to final class"
                    + "then the rest follow in a top-bottom approach. ";           
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Promote instruction");
            alert.setGraphic(pupilImageView);
            alert.setHeaderText(null);
            alert.setContentText(ms);
            alert.showAndWait();        
        
    }

    @FXML
    private void promote_refreshComboCurrentClass(ActionEvent event) {
        refreshStudentClassCombo();
    }

    @FXML
    private void promote_refreshComboTargetClass(ActionEvent event) {
        refreshStudentClassCombo();
    }

    @FXML
    private void promoteClassOperation(ActionEvent event) throws SQLException {
        
        
        if (promote_comboCurrentClass.getSelectionModel().isEmpty() || promote_comboTargetClass.getSelectionModel().isEmpty()) { 
                 
                 String msg = null;
            
                if (promote_comboCurrentClass.getSelectionModel().isEmpty()) {
                     msg = "Current class is not selected, you must select it";                 
                } else if(promote_comboTargetClass.getSelectionModel().isEmpty()) {
                   msg = "Target class is not select, you must select it";
                } 
             
            Alert errorIssue = new Alert(Alert.AlertType.ERROR);
            errorIssue.setTitle("Promotion Failed");
            errorIssue.setGraphic(errorImage);
            errorIssue.setHeaderText(null);
            errorIssue.setContentText("The students could not be promoted.\n"
                        + msg);
            errorIssue.showAndWait(); 
            
        }else { 

            String currentClass  = promote_comboCurrentClass.getSelectionModel().getSelectedItem();
            String targetClass  = promote_comboTargetClass.getSelectionModel().getSelectedItem();
            
                String s = "SELECT * FROM class WHERE className = ?  ";
                pstmt = con.prepareStatement(s);
                pstmt.setString(1, currentClass);
                rs=pstmt.executeQuery();
                
                if (rs.next()) {                    
                  boolean i =  rs.getBoolean("isFinal");                  
                    if (i) {
                        Alert errorIssue = new Alert(Alert.AlertType.ERROR);
                        errorIssue.setTitle("Promotion Failed");
                        errorIssue.setGraphic(errorImage);
                        errorIssue.setHeaderText(null);
                        errorIssue.setContentText(""+currentClass+" is the final class in the school system.\n"
                                + " You can not promote students any further.\n\n"
                                + "You can delete all students from this class and allow lower students be promoted into it");
                        errorIssue.showAndWait();
                    } else {
                        String ss = "SELECT * FROM class WHERE className = ?  ";
                        pstmt = con.prepareStatement(ss);
                        pstmt.setString(1, targetClass);
                        ResultSet rs2 = pstmt.executeQuery();
                        
                        if (rs2.next()) {
                            
                            boolean ii =  rs2.getBoolean("isFinal");
                            
                            if (ii) {
                                triggerPromotionWhenTargetClassIsFinalButStudentsHaveBeenRemoved();
                            } else {
                                 triggerPromotionWhenBothClassesNotFinal();
                            }
                            
                            refreshStudentTable();
                        }
                    }
                }
        }        
        
        
    }

    //method to trigger promotion if both current class and target class are not final in school
    private void triggerPromotionWhenBothClassesNotFinal(){
         try {
                    String sql = "UPDATE student SET class = ? WHERE class = '"+promote_comboCurrentClass.getSelectionModel().getSelectedItem()+"'";
                    
                    pstmt= con.prepareStatement(sql);

                    pstmt.setString(1, promote_comboTargetClass.getSelectionModel().getSelectedItem());

                    int i = pstmt.executeUpdate(); // load data into the database             
                    if (i>0) {
                        //show alert before promoting
                    String msg = "All Student from class = "+promote_comboCurrentClass.getSelectionModel().getSelectedItem()+""
                            + " have successfully been promoted to "+promote_comboTargetClass.getSelectionModel().getSelectedItem()+ ".\n\n";
                   successNotification(Pos.BOTTOM_RIGHT, msg);
                   refreshStudentTable();
                    } else {
                        errorNotification(Pos.BOTTOM_RIGHT, "OOPS! Could not promote Student due  to internal error.");
                    } 
                    pstmt.close();
                    
                } catch (Exception e) {
                        errorNotification(Pos.BOTTOM_RIGHT, "OOPS! Could not promote Student due  to internal error."); 
                }finally {
                   try {
                        rs.close();
                        pstmt.close();
                    } catch (Exception e) {}               
               }
    }
    
    
    
    //Method to trigger promotion when target class is final but students hopefully have been deleted from it.
    private void triggerPromotionWhenTargetClassIsFinalButStudentsHaveBeenRemoved(){
        
             //show confirm alert before promoting
            String ms = ""+promote_comboTargetClass.getSelectionModel().getSelectedItem()+""
            + "is a final class and might have students already!\n\n"
                    + "Are you sure you have deleted students and ready promote new students from "+promote_comboCurrentClass.getSelectionModel().getSelectedItem()+" to"
                    + " "+promote_comboTargetClass.getSelectionModel().getSelectedItem() +" ?";           
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm promotion - ALERT");
            alert.setGraphic(pupilImageView);
            alert.setHeaderText(null);
            alert.setContentText(ms);
           
            Optional<ButtonType> response = alert.showAndWait();            
            if (response.get()==ButtonType.OK) {
               try {
                    String sql = "UPDATE student SET class = ? WHERE class = '"+promote_comboCurrentClass.getSelectionModel().getSelectedItem()+"'";
                    
                    pstmt= con.prepareStatement(sql);
                    pstmt.setString(1, promote_comboTargetClass.getSelectionModel().getSelectedItem());

                    int i = pstmt.executeUpdate(); // load data into the database             
                    if (i>0) {
                        //show alert before promoting
                    String msg = "All Student from class = "+promote_comboCurrentClass.getSelectionModel().getSelectedItem()+""
                            + " have successfully been promoted to "+promote_comboTargetClass.getSelectionModel().getSelectedItem()+ ".\n\n";
                        successNotification(Pos.BOTTOM_RIGHT, msg);
                    
                    } else {
                       
                        errorNotification(Pos.BOTTOM_RIGHT, "OOPS! Could not promote Student due  to internal error.");
                    } 
                    pstmt.close();
                    
                } catch (Exception e) {
                    errorNotification(Pos.BOTTOM_RIGHT, "OOPS! Could not promote Student due  to internal error."); 
                }finally {
                   try {
                        rs.close();
                        pstmt.close();
                    } catch (Exception e) {}               
               }
            }
    }     
    
}// end of class