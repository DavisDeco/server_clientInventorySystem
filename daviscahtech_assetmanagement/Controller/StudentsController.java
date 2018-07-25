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
public class StudentsController implements Initializable {
    
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
    private ComboBox<String> student_class;
    private final ObservableList<String> classData = FXCollections.observableArrayList();
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
    private ComboBox<String> classNameForPrinting;
    @FXML
    private ComboBox<String> classNameForOtherAllocation;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        con = DatabaseConnection.connectDb();
        
        loadSchoolInfo();
        updateStudentClassNamesCombo();
        updateStudentTable();
    } 
    
    private  void addMetaData(Document document) {
        document.addTitle( "Daviscah Tech product" );
        document.addSubject( "www.daviscahtech.com" );
        document.addKeywords( "School, software, Books, Students, Teachers" );
        document.addAuthor( "Daviscah Tech Ltd" );
        document.addCreator( "Daviscah Tech Ltd" );
    } 
    
    //Method to creat an empty line in the document
     private static void addEmptyLine(Paragraph paragraph, int number) {
        for ( int i = 0 ; i < number; i++) {
              paragraph.add( new Paragraph( " " ));
        }
    }
     
    // method to format table cell with data
     private void insertCell(PdfPTable table,String text,int align,int colspan,Font font){
     
         PdfPCell cell = new PdfPCell(new Phrase(text.trim(),font));
         cell.setHorizontalAlignment(align);
         cell.setColspan(colspan);
         if (text.trim().equalsIgnoreCase("")) {
             cell.setMinimumHeight(10f);
         }
         table.addCell(cell);
     }    
    
    //method to automatically set school information from the database
    private void loadSchoolInfo() {
    
        try {
                 // Get fine ammount to be incurred by retrieving data from the database.                               
                String sql3 = "SELECT * FROM schoolInfo ";
                pstmt = con.prepareStatement(sql3);

                ResultSet rs3=pstmt.executeQuery();
                if (rs3.next()) {
                     schoolName = rs3.getString("name");
                     schoolContact = rs3.getString("contact");
                     schoolAddress = rs3.getString("address");
                     schoolRegion = rs3.getString("region");
                     schoolEmail = rs3.getString("email");
                     schoolWebsite = rs3.getString("website");
                } 
                
                pstmt.close();
            
        } catch (SQLException e) {
        }
    } 
    
    
            //Method to open a fileChooser dialog box
    public void pdfFileChooserOpener(){
                pdfFileChooser = new FileChooser();
                pdfFileChooser.getExtensionFilters().addAll(                
                new FileChooser.ExtensionFilter("PDF Files","*.pdf")

                
        );   
    } 
    
            //Method to open a fileChooser dialog box
    public void excelFileChooserOpener(){
                excelFileChooser = new FileChooser();
                excelFileChooser.getExtensionFilters().addAll(                
                new FileChooser.ExtensionFilter("Excel Files","*.*")

                
        );   
    }     

    //method to create pdf of a specific date when stock was stored
    public void createClassListForDeskChairPDF(String filename,String className){
        
       // create a pdf
       Document document = new Document(PageSize.A4,5,5,20,40);
       
        try {
            
             String sq = "SELECT * FROM student WHERE class = ? ";
             pstmt = con.prepareStatement(sq);
             pstmt.setString(1, className );
             rs=pstmt.executeQuery();
            
            Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK);
            Font bfBold = new Font(Font.FontFamily.TIMES_ROMAN, 11);
            
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
            HeaderFooterPageEvent event = new HeaderFooterPageEvent();
            writer.setPageEvent(event);
            
            document.open();
            addMetaData(document);
            
            //school info
            Paragraph intro = new Paragraph(""+schoolName+"\n"
                    + ""+schoolAddress+" "+ schoolRegion+"\n"
                    + "Website: "+ schoolWebsite+"\n"
                    + "Class list for "+ className +" as at "+ LocalDate.now()+" \n\r",bfBold12);
            intro.setAlignment(Element.ALIGN_CENTER);            
            document.add(intro);
            
            
            
            // Add a table
            float[] columnWidths = {1.5f,2.6f,1.2f,1.2f,1.5f,3f};
            PdfPTable studentTable = new PdfPTable(columnWidths);
            studentTable.setWidthPercentage(90f);
            
            insertCell(studentTable, "Student ID", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(studentTable, "Student Name", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(studentTable, "Desk No.", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(studentTable, "Chair No.", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(studentTable, "Student Sign", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(studentTable, "Staff Sign/Comment", Element.ALIGN_LEFT, 1, bfBold12);
            
            while (rs.next()) {
                String code = rs.getString("id_no");
                insertCell(studentTable, code, Element.ALIGN_LEFT, 1, bfBold);
                
                String name = rs.getString("name");
                insertCell(studentTable, name, Element.ALIGN_LEFT, 1, bfBold); 
                               
                insertCell(studentTable, " ", Element.ALIGN_LEFT, 1, bfBold); 
                insertCell(studentTable, " ", Element.ALIGN_LEFT, 1, bfBold);                
                insertCell(studentTable, " ", Element.ALIGN_LEFT, 1, bfBold);
                insertCell(studentTable, " ", Element.ALIGN_LEFT, 1, bfBold);
                         
            }
            
            document.add(studentTable);
            
            document.close();
            pstmt.close();
            
            pdfPrintedNotification(Pos.BOTTOM_RIGHT, "Successfully printed report and can be found at: \n" + filename);
            
            
        } catch (DocumentException | FileNotFoundException | SQLException ex) {
            Logger.getLogger(TeachersectionController.class.getName()).log(Level.SEVERE, null, ex);
            errorNotification(Pos.BOTTOM_RIGHT,"Report Not Generated");
        } 
        

    }  

    public void createClassListForOtherAllocationPDF(String filename,String className){
        
       // create a pdf
       Document document = new Document(PageSize.A4,5,5,20,40);
       
        try {
            
             String sq = "SELECT * FROM student WHERE class = ? ";
             pstmt = con.prepareStatement(sq);
             pstmt.setString(1, className );
             rs=pstmt.executeQuery();
            
            Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK);
            Font bfBold = new Font(Font.FontFamily.TIMES_ROMAN, 11);
            
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
            HeaderFooterPageEvent event = new HeaderFooterPageEvent();
            writer.setPageEvent(event);
            
            document.open();
            addMetaData(document);
            
            //school info
            Paragraph intro = new Paragraph(""+schoolName+"\n"
                    + ""+schoolAddress+" "+ schoolRegion+"\n"
                    + "Website: "+ schoolWebsite+"\n"
                    + "Class list for "+ className +" as at "+ LocalDate.now()+" \n\r",bfBold12);
            intro.setAlignment(Element.ALIGN_CENTER);            
            document.add(intro);
            
            
            
            // Add a table
            float[] columnWidths = {1.5f,2.6f,4f,1.5f,1.5f};
            PdfPTable studentTable = new PdfPTable(columnWidths);
            studentTable.setWidthPercentage(90f);
            
            insertCell(studentTable, "Student ID", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(studentTable, "Student Name", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(studentTable, "Allocated resource", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(studentTable, "Student Sign", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(studentTable, "Staff Sign", Element.ALIGN_LEFT, 1, bfBold12);
            
            while (rs.next()) {
                String code = rs.getString("id_no");
                insertCell(studentTable, code, Element.ALIGN_LEFT, 1, bfBold);
                
                String name = rs.getString("name");
                insertCell(studentTable, name, Element.ALIGN_LEFT, 1, bfBold); 
                               
                insertCell(studentTable, " ", Element.ALIGN_LEFT, 1, bfBold); 
                insertCell(studentTable, " ", Element.ALIGN_LEFT, 1, bfBold);                
                insertCell(studentTable, " ", Element.ALIGN_LEFT, 1, bfBold);
                         
            }
            
            document.add(studentTable);
            
            document.close();
            pstmt.close();
            
            pdfPrintedNotification(Pos.BOTTOM_RIGHT, "Successfully printed report and can be found at: \n" + filename);
            
            
        } catch (DocumentException | FileNotFoundException | SQLException ex) {
            Logger.getLogger(TeachersectionController.class.getName()).log(Level.SEVERE, null, ex);
            errorNotification(Pos.BOTTOM_RIGHT,"Report Not Generated");
        } 
        

    }      
    
    public void pdfPrintedNotification(Pos pos,String title){
       notificationBuilder = Notifications.create()
                .title("PDF Report Printed")
                .text(title)
                .graphic(pdfImageView)
                .hideAfter(Duration.seconds(10))
                .position(pos)
                .onAction((ActionEvent event) -> {
                    System.out.println("Notification created");
                });
        notificationBuilder.show();
                
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
    
    
   private void updateStudentClassNamesCombo(){  
        try { 
            String sql = "SELECT className FROM class";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next()){                
                classData.add( rs.getString("className")); 
            }
            // populate class combo box 
            student_class.setItems(classData);
            classNameForPrinting.setItems(classData); 
            classNameForOtherAllocation.setItems(classData);
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
    private void openStudentLearningMaterialWindow(ActionEvent event) throws IOException {
        loadWindow("/daviscahtech_assetmanagement/FXML/learningMaterial.fxml", "Student learning material sector");        
    }  

    @FXML
    private void openDesksAndChairsWindow(ActionEvent event) throws IOException {
        loadWindow("/daviscahtech_assetmanagement/FXML/deskChair.fxml", "Students' chairs and desks");
    }

    @FXML
    private void choosestudentImageView(ActionEvent event) {
        
        fileChooserOpener();
           fileChooser.setTitle("Select product image");
           //single File selection
           file = fileChooser.showOpenDialog(Stage);
            if (file != null) {
               String studentImagePath = file.getAbsolutePath();
               try {
                   image = new Image(new FileInputStream(studentImagePath),studentImageView.getFitWidth(),studentImageView.getFitHeight(),false,true);
                   studentImageView.setImage(image);                  
               } catch (FileNotFoundException ex) {
                   Logger.getLogger(StudentsController.class.getName()).log(Level.SEVERE, null, ex);
               }            
            }         
        
    }

    @FXML
    private void student_refreshClassOperation(ActionEvent event) {
        refreshStudentClassCombo();
    }

    @FXML
    private void registerStudentOperation(ActionEvent event) {
        
        if (student_id.getText().isEmpty() 
                || student_name.getText().isEmpty()
                ||student_class.getSelectionModel().isEmpty()) {

                String msg = null;

                if (student_id.getText().isEmpty()) {
                    msg = "Student's ID is empty, you must enter it";
                } else if (student_name.getText().isEmpty()) {
                    msg = "Student's name is empty, you must enter it";
                } else if (student_class.getSelectionModel().isEmpty()) {
                    msg = "Student's class is empty, choose from the drop down menu";
                } 

                Alert errorIssue = new Alert(Alert.AlertType.ERROR);
                errorIssue.setTitle("Registration Failed");
                errorIssue.setGraphic(errorImage);
                errorIssue.setHeaderText(null);
                errorIssue.setContentText("The student has failed registration.\n"
                + msg);
                errorIssue.showAndWait();
        
        }else {
                           
            
                //show confirm alert before registering data of issued
                String ms = " Are you sure you want to register "+student_name.getText();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm registration");
                alert.setGraphic(pupilImageView);
                alert.setHeaderText(null);
                alert.setContentText(ms);

                Optional<ButtonType> response = alert.showAndWait();
                if (response.get()==ButtonType.OK) {
                    try {
                        String sql = "INSERT INTO student (id_no,name,class,image)"
                        + " VALUES(?,?,?,?)";
                        pstmt= con.prepareStatement(sql);
                        
                        pstmt.setString(1, student_id.getText());
                        pstmt.setString(2, student_name.getText());                        
                        pstmt.setString(3, student_class.getSelectionModel().getSelectedItem().toString());
                                                            //save a file path to database
                        if (file == null) {
                            String imagePath = "C:\\A_Check\\Store_photos\\student.png";
                            pstmt.setString(4, imagePath);
                        } else {
                           String filePath = file.getAbsolutePath();
                           pstmt.setString(4, filePath);
                        }
                        
                        int i = pstmt.executeUpdate(); // load data into the database
                        if (i>0) {
                          String ok = student_name.getText() +" has been successfully registered";
                            successNotification(Pos.BOTTOM_RIGHT, ok);
                            
                            clearStudentFields();
                            refreshStudentTable();

                        } else {
                            errorNotification(Pos.BOTTOM_RIGHT, "OOPS! Could not register student due  to internal error.");
                       
                        }
                        pstmt.close();

                    } catch (Exception e) {
                        errorNotification(Pos.BOTTOM_RIGHT, "OOPS! Could not register student due  to internal error.");
                    }finally {
                    try {
                    rs.close();
                    pstmt.close();
                    } catch (Exception e) {}
                    }
                }   
        }        
        
        
    }

    @FXML
    private void updateStudentOperation(ActionEvent event) {
        
        if (student_id.getText().isEmpty() 
                || student_name.getText().isEmpty()) {

                String msg = null;

                if (student_id.getText().isEmpty()) {
                    msg = "Student's ID is empty, you must enter it";
                } else if (student_name.getText().isEmpty()) {
                    msg = "Student's name is empty, you must enter it";
                }  

                Alert errorIssue = new Alert(Alert.AlertType.ERROR);
                errorIssue.setTitle("Update Failed");
                errorIssue.setGraphic(errorImage);
                errorIssue.setHeaderText(null);
                errorIssue.setContentText("The student has failed update.\n"
                + msg);
                errorIssue.showAndWait();
        
        }else {
                           
            
                //show confirm alert before registering data of issued
                String ms = " Are you sure you want to update details? ";
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm update");
                alert.setGraphic(pupilImageView);
                alert.setHeaderText(null);
                alert.setContentText(ms);

                Optional<ButtonType> response = alert.showAndWait();
                if (response.get()==ButtonType.OK) {
                    try {
                        String sql = "UPDATE student SET id_no = ?,name = ?,class = ?,image = ?"
                        + " WHERE id = '"+studentIDHolder+"' ";
                        pstmt= con.prepareStatement(sql);
                        
                        pstmt.setString(1, student_id.getText());
                        pstmt.setString(2, student_name.getText()); 
                        
                        if (student_class.getSelectionModel().isEmpty()) {
                            pstmt.setString(3, displayStudentClass.getText());
                        } else {
                            pstmt.setString(3, student_class.getSelectionModel().getSelectedItem().toString());
                        }
                        
                        //save a file path to database
                        if (file == null) {
                            //String imagePath = "C:\\A_Check\\Store_photos\\student.png";
                            pstmt.setString(4, retrievedImagePath);
                        } else {
                           String filePath = file.getAbsolutePath();
                           pstmt.setString(4, filePath);
                        }
                        
                        int i = pstmt.executeUpdate(); // load data into the database
                        if (i>0) {
                          String ok = student_name.getText() +" has been successfully updated";
                            successNotification(Pos.BOTTOM_RIGHT, ok);
                            
                            clearStudentFields();
                            refreshStudentTable();

                        } else {
                            errorNotification(Pos.BOTTOM_RIGHT, "OOPS! Could not update student due  to internal error.");
                       
                        }
                        pstmt.close();

                    } catch (Exception e) {
                        errorNotification(Pos.BOTTOM_RIGHT, "OOPS! Could not update student  due  to internal error.");
                        e.printStackTrace();
                    }finally {
                    try {
                    rs.close();
                    pstmt.close();
                    } catch (Exception e) {}
                    }
                }   
        }          
        
    }

    @FXML
    private void deleteStudentOperation(ActionEvent event) {

          if (studentIDHolder <=0) {
            
            Alert errorIssue = new Alert(Alert.AlertType.ERROR);
            errorIssue.setTitle("Delete Failed");
            errorIssue.setGraphic(errorImage);
            errorIssue.setHeaderText(null);
            errorIssue.setContentText("Ensure the registered student to be deleted has been retrieved.");
            errorIssue.showAndWait(); 
            
        }else {
              Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
              alert.setTitle("Delete");
              alert.setGraphic(pupilImageView);
              alert.setHeaderText(null);
              alert.setContentText("Are you sure you want to delete this student's details?");
              
              Optional <ButtonType> obt = alert.showAndWait();
              
              if (obt.get()== ButtonType.OK) {
              try {
              String query = "DELETE FROM student WHERE id = ? ";
              pstmt = con.prepareStatement(query);
              pstmt.setInt(1, studentIDHolder);
              int i = pstmt.executeUpdate();
              
              if (i>0) {
                  successNotification(Pos.BOTTOM_RIGHT, "The student's details has been deleted.");
              }
              
              pstmt.close(); 
              
              clearStudentFields();
              refreshStudentTable();
              
              } catch (SQLException ex) {
              Logger.getLogger(TeachersectionController.class.getName()).log(Level.SEVERE, null, ex);
              }finally {
              try {
              rs.close();
              pstmt.close();
              } catch (Exception e) {
              }
              }
              
              }          
        }        
        
    }

    @FXML
    private void clearStudentFiledOperation(ActionEvent event) {
        clearStudentFields();
    }
    
    private void clearStudentFields(){
    
        student_id.clear();
        student_name.clear();
        refreshStudentClassCombo();
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

    @FXML
    private void refreshClassNameForPrinting(ActionEvent event) {
        refreshStudentClassCombo();
    }

    @FXML
    private void printClassListOperation(ActionEvent event) {
        
        if (classNameForPrinting.getSelectionModel().isEmpty()) {             
            
            String msg = "Class is empty, you must choose a class from the drop-down menu to generate class lists";           
             
            Alert errorIssue = new Alert(Alert.AlertType.ERROR);
            errorIssue.setTitle("FAILED");
            errorIssue.setGraphic(errorImage);
            errorIssue.setHeaderText(null);
            errorIssue.setContentText("Report generation for printing failed.\n"
                        + msg);
            errorIssue.showAndWait(); 
            
        }else {
            String className = classNameForPrinting.getSelectionModel().getSelectedItem().toString();
            
            // generate pdf
                 pdfFileChooserOpener();
                   pdfFileChooser.setTitle("Save printed report");
                   //single File selection
                   pdfFile = pdfFileChooser.showSaveDialog(Stage);                   
                    if (pdfFile != null) {
                        String path = pdfFile.getAbsolutePath();
                        
                        createClassListForDeskChairPDF(path,className);
                    }        
        }        
        
    }

    @FXML
    private void refreshClassNameForOtherAllocation(ActionEvent event) {
        refreshStudentClassCombo();
    }

    @FXML
    private void printClassListForOtherAllocationOperation(ActionEvent event) {
  
        if (classNameForOtherAllocation.getSelectionModel().isEmpty()) {             
            
            String msg = "Class is empty, you must choose a class from the drop-down menu to generate class lists";           
             
            Alert errorIssue = new Alert(Alert.AlertType.ERROR);
            errorIssue.setTitle("FAILED");
            errorIssue.setGraphic(errorImage);
            errorIssue.setHeaderText(null);
            errorIssue.setContentText("Report generation for printing failed.\n"
                        + msg);
            errorIssue.showAndWait(); 
            
        }else {
            String className = classNameForOtherAllocation.getSelectionModel().getSelectedItem().toString();
            
            // generate pdf
                 pdfFileChooserOpener();
                   pdfFileChooser.setTitle("Save printed report");
                   //single File selection
                   pdfFile = pdfFileChooser.showSaveDialog(Stage);                   
                    if (pdfFile != null) {
                        String path = pdfFile.getAbsolutePath();
                        
                        createClassListForOtherAllocationPDF(path,className);
                    }        
        }         
        
    }

    @FXML
    private void importStudentsFromExcelOperation(ActionEvent event) {
        
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Import students data");
            alert.setGraphic(pupilImageView);
            alert.setHeaderText(null);
            alert.setContentText("Only data in Excel format and with an extension of .xlsx can be imported in the correct format.\n\n"
                    + "Are you sure you want to import students data from outside the system? ");
           
            Optional<ButtonType> response = alert.showAndWait();            
            if (response.get()==ButtonType.OK) {
                
                excelFileChooserOpener();
                   excelFileChooser.setTitle("Select Students' data File Name");
                   //single File selection
                   excelFile = excelFileChooser.showOpenDialog(Stage);
                    if (excelFile != null) {
                        String filePath = excelFile.getAbsolutePath();
                        
                        try {
                            String sql = "INSERT INTO student"
                                    + " (id_no,name,class)"
                                    + " VALUES(?,?,?)";
                            pstmt= con.prepareStatement(sql);
                            
                            FileInputStream fileIn = new FileInputStream(new File(filePath));
                            
                            XSSFWorkbook wb = new XSSFWorkbook(fileIn);
                            XSSFSheet sheet = wb.getSheetAt(0);
                            Row row;
                             
                            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                                
                                row = sheet.getRow(i);
                                
                                    if (isRowValueDouble(row)) {
                                        int content = (int) row.getCell(0).getNumericCellValue();
                                        pstmt.setString(1, String.valueOf(content));
                                    } else {
                                        pstmt.setString(1, row.getCell(0).getStringCellValue());
                                    }                                
                                
                                pstmt.setString(2, row.getCell(1).getStringCellValue());
                                pstmt.setString(3, row.getCell(2).getStringCellValue());
                                pstmt.execute();
                                 
                                   
                            }                                    
                                    successNotification(Pos.BOTTOM_RIGHT, "Successfully imported students data from this file.\n\n" + filePath);
                                    
                                    wb.close();
                                    fileIn.close();
                                    pstmt.close();
                                    
                               refreshStudentTable();
                            
                        } catch (SQLException | IOException e) {
                            
                            Alert at = new Alert(Alert.AlertType.ERROR);
                            at.setTitle("Error in registration");
                            at.setGraphic(errorImage);
                            at.setHeaderText(null);
                            at.setContentText("Internal server Error occurred and some students data was NOT REGISTERED. The following errors might have occured \n\n"
                                    + "1. Two or more students share same students ID noted below."
                                    + " Therefore only one student will be registered and the other student(s) after the duplicate will NOT be REGISTERED.\n\n"+e);
                            at.showAndWait(); 
                            
                            refreshStudentTable();
 
                           e.printStackTrace();
                        }

                    }
            }         
        
    }
    
    //method to validate if input is an int
    private boolean isRowValueDouble(Row row){
        try {
            double number = row.getCell(0).getNumericCellValue();
            System.out.println("Input is" + number );
            return true;
        } catch (Exception e) {
            return false;
        }
    }    

    @FXML
    private void closethisWindowOperation(ActionEvent event) {
        closeStage();
    }
    
    private void closeStage(){
        ( (Stage)student_name.getScene().getWindow() ).close();
    }
    
     
    
}// end of class
