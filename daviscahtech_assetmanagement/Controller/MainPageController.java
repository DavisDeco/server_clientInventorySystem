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
import daviscahtech_assetmanagement.Pojo.GeneralStockStore;
import daviscahtech_assetmanagement.Pojo.StudentAllocation;
import daviscahtech_assetmanagement.Preferences.ServerPost;
import daviscahtech_assetmanagement.Server.Server;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
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
public class MainPageController implements Initializable {
    
    Image errorFlag = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/errorFlag.png").toExternalForm());
    ImageView errorImage = new ImageView(errorFlag);
    
    Image errorNotify = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/errornotification.png").toExternalForm());
    ImageView errorNotifyImageView = new ImageView(errorNotify);
    
    Image thumbsImage = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/thumbs.png").toExternalForm());
    ImageView imageSuccess = new ImageView(thumbsImage);    
    
    Image goodImage = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/good.png").toExternalForm());
    ImageView successNotiyiImageView = new ImageView(goodImage);
    
    Image stockedproduct = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/store.png").toExternalForm());
    ImageView stockedproductImageView = new ImageView(stockedproduct); 
    
    Image warning = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/Warning.png").toExternalForm());
    ImageView warningImagevies = new ImageView(warning);
    
    Image pdfImage = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/pdf.png").toExternalForm());
    ImageView pdfImageView = new ImageView(pdfImage);  
    
    Image info = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/info.png").toExternalForm());
    ImageView infoImage = new ImageView(info); 
    
    Image studentImage = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/student.png").toExternalForm());
    ImageView pupilImageView = new ImageView(studentImage); 
        
    
    
    //###############################For DB link##########################
    Connection con = null;
    PreparedStatement pstmt;
    ResultSet rs;
    Statement st;
    
    //instance of javafx image
    private Image image;
    
    //variable holders
    private int stockTablePrimaryID;
    private int stockTableForeignID;
     private int stockTableQuantityAvailable;

    //NOTIFICATION BUILDER VARIABLE
    Notifications notificationBuilder;    
    
    //instance of FIlechooser
    private FileChooser fileChooser;
    //instance of File
    private File file;
    private Window Stage;
    private File pdfFile;
    private FileChooser pdfFileChooser;  
    private File excelFile;
    private FileChooser excelFileChooser;
    
    //variables to hold school info
    String schoolName = null;
    String schoolContact = null;
    String schoolAddress = null;
    String schoolRegion = null;
    String schoolEmail = null;
    String schoolWebsite = null;      
    
    private final ObservableList<String> productTypeData = FXCollections.<String>observableArrayList(
            "Single units","Pairs","Packets","Bunches","Boxes","Cartons","Dozens","Crates",
            "Sets","Bales","Tonnes","Kilograms","Grams","Litres","MiliLitres");
    
    @FXML
    private TableView<GeneralStockStore> teachingTable;
    @FXML
    private TableColumn<GeneralStockStore, String> teachingColumn_code;
    @FXML
    private TableColumn<GeneralStockStore, String> teachingColumn_name;
    @FXML
    private TableColumn<GeneralStockStore, String> teachingColumn_quantity;
    @FXML
    private TableColumn<GeneralStockStore, String> teachingColumn_lastStocked;
    @FXML
    private TableColumn<GeneralStockStore, String> teachingColumn_totalCost;
    @FXML
    private TableColumn<GeneralStockStore, String> teachingColumn_alartStatus;
    private final ObservableList<GeneralStockStore> teacherStoreData = FXCollections.observableArrayList();
    
    @FXML
    private ImageView teachingSectorImageView;
    @FXML
    private TextField teachingSector_code;
    @FXML
    private TextField teachingSector_name;
    @FXML
    private TextArea teachingSector_desc;
    @FXML
    private TextField teachingSector_QuantityIssued;
    @FXML
    private TextField teachingSector_receipientName;
    @FXML
    private DatePicker teachingSector_dateIssued;
    @FXML
    private TextField teachingSector_searchField;
    private Image image2;
    @FXML
    private TextField teacherSection_returnUnits;
    @FXML
    private ComboBox teacherSection_productType;    
    @FXML
    private TextField teacherSection_returnedBy;
    @FXML
    private DatePicker teacherSection_dateReturned;
    @FXML
    private ImageView boardingSectrImageview;
    @FXML
    private TextField boardingSector_code;
    @FXML
    private TextField boardingSector_name;
    @FXML
    private TextArea boardingSector_desc;
    @FXML
    private TextField boardingSector_quantity;
    @FXML
    private TextField boardingSector_receipientName;
    @FXML
    private DatePicker boardingSector_dateIssued;
    @FXML
    private TextField boardingSector_returnedUnits;
    @FXML
    private TextField boardingSector_returnedBy;
    @FXML
    private DatePicker boardingSector_dateReturned;
    @FXML
    private TextField boardingSector_searchField;
    @FXML
    private TableView<GeneralStockStore> boardingSectorTable;
    @FXML
    private TableColumn<GeneralStockStore, String> bordingColumn_code;
    @FXML
    private TableColumn<GeneralStockStore, String> bordingColumn_name;
    @FXML
    private TableColumn<GeneralStockStore, String> bordingColumn_quantity;
    @FXML
    private TableColumn<GeneralStockStore, String> bordingColumn_department;
    @FXML
    private TableColumn<GeneralStockStore, String> bordingColumn_cost;
    @FXML
    private TableColumn<GeneralStockStore, String> bordingColumn_alartStatus;
    private final ObservableList<GeneralStockStore> boardingStoreData = FXCollections.observableArrayList();
    @FXML
    private ComboBox boardingSector_productType;
    @FXML
    private ImageView gamesSectorImageView;
    @FXML
    private TextField gamesSector_code;
    @FXML
    private TextField gamesSector_name;
    @FXML
    private TextArea gamesSector_desc;
    @FXML
    private TextField gamesSector_quantity;
    @FXML
    private TextField gamesSector_receipient;
    @FXML
    private DatePicker gamesSector_dateIssued;
    @FXML
    private TextField gamesSector_returnedUnits;
    @FXML
    private ComboBox gamesSector_productType;
    @FXML
    private TextField gamesSector_returnedBy;
    @FXML
    private DatePicker gamesSector_dateReturned;
    @FXML
    private TextField gamesSector_searchField;
    @FXML
    private TableView<GeneralStockStore> gamesTable;
    @FXML
    private TableColumn<GeneralStockStore, String> gamesColumn_code;
    @FXML
    private TableColumn<GeneralStockStore, String> gamesColumn_name;
    @FXML
    private TableColumn<GeneralStockStore, String> gamesColumn_quantity;
    @FXML
    private TableColumn<GeneralStockStore, String> gamesColumn_department;
    @FXML
    private TableColumn<GeneralStockStore, String> gamesColumn_cost;
    @FXML
    private TableColumn<GeneralStockStore, String> gamesColumn_alart;
    private final ObservableList<GeneralStockStore> gamesStoreData = FXCollections.observableArrayList();
    @FXML
    private ImageView cateringSectorImageView;
    @FXML
    private TextField cateringSector_code;
    @FXML
    private TextField cateringSector_name;
    @FXML
    private TextArea cateringSector_desc;
    @FXML
    private TextField cateringSector_quantity;
    @FXML
    private TextField cateringSector_receipient;
    @FXML
    private DatePicker cateringSector_dateIssued;
    @FXML
    private TextField cateringSector_returnedUnits;
    @FXML
    private ComboBox cateringSector_productType;
    @FXML
    private TextField cateringSector_returnedBy;
    @FXML
    private DatePicker cateringSector_dateReturned;
    @FXML
    private TextField cateringSector_searchField;
    @FXML
    private TableView<GeneralStockStore> CateringTable;
    @FXML
    private TableColumn<GeneralStockStore, String> cateringColumn_code;
    @FXML
    private TableColumn<GeneralStockStore, String> cateringColumn_name;
    @FXML
    private TableColumn<GeneralStockStore, String> cateringColumn_quantity;
    @FXML
    private TableColumn<GeneralStockStore, String> cateringColumn_department;
    @FXML
    private TableColumn<GeneralStockStore, String> cateringColumn_cost;
    @FXML
    private TableColumn<GeneralStockStore, String> cateringColumn_alart;
    private final ObservableList<GeneralStockStore> cateringStoreData = FXCollections.observableArrayList();
    @FXML
    private ImageView surbodinateSectorImageView;
    @FXML
    private TextField surbodinateSector_code;
    @FXML
    private TextField surbodinateSector_name;
    @FXML
    private TextArea surbodinateSector_desc;
    @FXML
    private TextField surbodinateSector_quantity;
    @FXML
    private TextField surbodinateSector_recipient;
    @FXML
    private DatePicker surbodinateSector_dateIssued;
    @FXML
    private TextField surbodinateSector_returnedUnits;
    @FXML
    private ComboBox surbodinateSector_productType;
    @FXML
    private TextField surbodinateSector_returnedBy;
    @FXML
    private DatePicker surbodinateSector_dateReturned;
    @FXML
    private TextField surbodinateSector_searchField;
    @FXML
    private TableView<GeneralStockStore> surbodinateTable;
    @FXML
    private TableColumn<GeneralStockStore, String> surbodinateColum_code;
    @FXML
    private TableColumn<GeneralStockStore, String> surbodinateColum_name;
    @FXML
    private TableColumn<GeneralStockStore, String> surbodinateColum_quantity;
    @FXML
    private TableColumn<GeneralStockStore, String> surbodinateColum_department;
    @FXML
    private TableColumn<GeneralStockStore, String> surbodinateColum_cost;
    @FXML
    private TableColumn<GeneralStockStore, String> surbodinateColum_alart;
    private final ObservableList<GeneralStockStore> surbodinateStoreData = FXCollections.observableArrayList();
    @FXML
    private ImageView securitySectorImageview;
    @FXML
    private TextField securitySector_code;
    @FXML
    private TextField securitySector_name;
    @FXML
    private TextArea securitySector_desc;
    @FXML
    private TextField securitySector_quantity;
    @FXML
    private TextField securitySector_receipient;
    @FXML
    private DatePicker securitySector_dateIssued;
    @FXML
    private TextField securitySector_returnedUnits;
    @FXML
    private ComboBox securitySector_productType;
    @FXML
    private TextField securitySector__returnedBy;
    @FXML
    private DatePicker securitySector_returnedDate;
    @FXML
    private TextField securitySector_searchField;
    @FXML
    private TableView<GeneralStockStore> SecurityTable;
    @FXML
    private TableColumn<GeneralStockStore, String> SecurityColumn_code;
    @FXML
    private TableColumn<GeneralStockStore, String> SecurityColumn_name;
    @FXML
    private TableColumn<GeneralStockStore, String> SecurityColumn_quantity;
    @FXML
    private TableColumn<GeneralStockStore, String> SecurityColumn_department;
    @FXML
    private TableColumn<GeneralStockStore, String> SecurityColumn_cost;
    @FXML
    private TableColumn<GeneralStockStore, String> SecurityColumn_alart;
    private final ObservableList<GeneralStockStore> securityStoreData = FXCollections.observableArrayList();
    @FXML
    private ImageView productionImageView;
    @FXML
    private TextField productionSector_code;
    @FXML
    private TextField productionSector_name;
    @FXML
    private TextArea productionSector_desc;
    @FXML
    private TextField productionSector_quantity;
    @FXML
    private TextField productionSector_recipient;
    @FXML
    private DatePicker productionSector_dateissued;
    @FXML
    private TextField productionSector_returnedUnits;
    @FXML
    private ComboBox productionSector_productType;
    @FXML
    private TextField productionSector_returnedBy;
    @FXML
    private DatePicker productionSector_dateReturned;
    @FXML
    private TextField productionSector_searchField;
    @FXML
    private TableView<GeneralStockStore> productionTable;
    @FXML
    private TableColumn<GeneralStockStore, String> productionColumn_code;
    @FXML
    private TableColumn<GeneralStockStore, String> productionColumn_name;
    @FXML
    private TableColumn<GeneralStockStore, String> productionColumn_quantity;
    @FXML
    private TableColumn<GeneralStockStore, String> productionColumn_department;
    @FXML
    private TableColumn<GeneralStockStore, String> productionColumn_cost;
    @FXML
    private TableColumn<GeneralStockStore, String> productionColumn_alart;
    private final ObservableList<GeneralStockStore> productionStoreData = FXCollections.observableArrayList();
    @FXML
    private ImageView enetertainmentImageview;
    @FXML
    private TextField entertainmentSector_code;
    @FXML
    private TextField entertainmentSector_name;
    @FXML
    private TextArea entertainmentSector_desc;
    @FXML
    private TextField entertainmentSector_quantity;
    @FXML
    private DatePicker entertainmentSector_dateIssued;
    @FXML
    private TextField entertainmentSector_returnedUnits;
    @FXML
    private ComboBox entertainmentSector_productType;
    @FXML
    private TextField entertainmentSector_returnedBy;
    @FXML
    private DatePicker entertainmentSector_dateReturned;
    @FXML
    private TextField entertainmentSector_searchField;
    @FXML
    private TableView<GeneralStockStore> entertainmentTable;
    @FXML
    private TableColumn<GeneralStockStore, String> entertainmentColumn_code;
    @FXML
    private TableColumn<GeneralStockStore, String> entertainmentColumn_name;
    @FXML
    private TableColumn<GeneralStockStore, String> entertainmentColumn_quantity;
    @FXML
    private TableColumn<GeneralStockStore, String> entertainmentColumn_department;
    @FXML
    private TableColumn<GeneralStockStore, String> entertainmentColumn_cost;
    @FXML
    private TableColumn<GeneralStockStore, String> entertainmentColumn_alart;
    private final ObservableList<GeneralStockStore> entertainmentStoreData = FXCollections.observableArrayList();
    @FXML
    private TextField entertainmentSector_receipient;
    @FXML
    private ImageView transportSectorImageView;
    @FXML
    private TextField transportSector_code;
    @FXML
    private TextField transportSector_name;
    @FXML
    private TextArea transportSector_desc;
    @FXML
    private TextField transportSector_quantity;
    @FXML
    private TextField transportSector_recipient;
    @FXML
    private DatePicker transportSector_dateIssued;
    @FXML
    private TextField transportSector_returnedUnits;
    @FXML
    private ComboBox transportSector_productType;
    @FXML
    private TextField transportSector_returnedBy;
    @FXML
    private DatePicker transportSector_dateReturned;
    @FXML
    private TextField transportSector_searchField;
    @FXML
    private TableView<GeneralStockStore> transportTable;
    @FXML
    private TableColumn<GeneralStockStore, String> transportColumn_code;
    @FXML
    private TableColumn<GeneralStockStore, String> transportColumn_name;
    @FXML
    private TableColumn<GeneralStockStore, String> transportColumn_quantity;
    @FXML
    private TableColumn<GeneralStockStore, String> transportColumn_department;
    @FXML
    private TableColumn<GeneralStockStore, String> transportColumn_cost;
    @FXML
    private TableColumn<GeneralStockStore, String> transportColumn_alart;
    private final ObservableList<GeneralStockStore> transportStoreData = FXCollections.observableArrayList();
    @FXML
    private ImageView studentSectorImageView;
    @FXML
    private TextField studentSector_code;
    @FXML
    private TextField studentSector_name;
    @FXML
    private TextArea studentSector_desc;
    @FXML
    private TextField studentSector_quantity;
    @FXML
    private TextField studentSector_IDName;
    @FXML
    private DatePicker studentSector_dateIssued;
    @FXML
    private TextField studentSector_returnedUnits;
    @FXML
    private ComboBox studentSector_productType;
    @FXML
    private TextField studentSector_returnedBy;
    @FXML
    private DatePicker studentSector_dateReturned;
    @FXML
    private TextField studentSector_searchField;
    @FXML
    private TableView<GeneralStockStore> studentSectorTable;
    @FXML
    private TableColumn<GeneralStockStore, String> studentSectorColumn_code;
    @FXML
    private TableColumn<GeneralStockStore, String> studentSectorColumn_name;
    @FXML
    private TableColumn<GeneralStockStore, String> studentSectorColumn_quantity;
    @FXML
    private TableColumn<GeneralStockStore, String> studentSectorColumn_department;
    @FXML
    private TableColumn<GeneralStockStore, String> studentSectorColumn_cost;
    @FXML
    private TableColumn<GeneralStockStore, String> studentSectorColumn_alart;
    private final ObservableList<GeneralStockStore> studentSectorStoreData = FXCollections.observableArrayList();
    @FXML
    private ImageView studentImageView;
    @FXML
    private TextField student_ID;
    @FXML
    private ComboBox<String> student_registeredID;
    private final ObservableList<String> studentIdData = FXCollections.observableArrayList();
    @FXML
    private TextField student_name;
    @FXML
    private ComboBox<String> student_registeredNames;
    private final ObservableList<String> studentNamesData = FXCollections.observableArrayList();
    @FXML
    private TextField student_deskNumber;
    @FXML
    private ComboBox<String> student_registeredDeskNumber;
    private final ObservableList<String> deskNoData = FXCollections.observableArrayList();
    @FXML
    private TextField student_chairNumber;
    @FXML
    private ComboBox<String> student_rgisteredChairNumber;
    private final ObservableList<String> chairNoData = FXCollections.observableArrayList();
    @FXML
    private TextField student_searchField;
    @FXML
    private TableView<StudentAllocation> studentTable;
    @FXML
    private TableColumn<StudentAllocation, String> studentColumn_ID;
    @FXML
    private TableColumn<StudentAllocation, String> studentColumn_name;
    @FXML
    private TableColumn<StudentAllocation, String> studentColumn_class;
    @FXML
    private TableColumn<StudentAllocation, String> studentColumn_desk;
    @FXML
    private TableColumn<StudentAllocation, String> studentColumn_chair;
    @FXML
    private TableColumn<StudentAllocation, String> studentColumn_dateIssued;
    private final ObservableList<StudentAllocation> studentAllocationData = FXCollections.observableArrayList();
    @FXML
    private HBox hbox_studentID;
    @FXML
    private HBox hbox_studentName;
    @FXML
    private HBox hbox_desk;
    @FXML
    private HBox hbox_chair;
    @FXML
    private CheckBox fromRegisteredID;
    @FXML
    private CheckBox fromRegisteredNames;
    @FXML
    private CheckBox fromRegisteredDesk;
    @FXML
    private CheckBox fromRegisteredChairs;
    @FXML
    private ComboBox<String> printClassName;
    private final ObservableList<String> classNameData = FXCollections.observableArrayList();
    private int studentIDHolder;
    @FXML
    private ImageView roomsAllocationImageView;
    @FXML
    private TextField roomAllocation_code;
    @FXML
    private TextField roomAllocation_name;
    @FXML
    private TextArea roomAllocation_desc;
    @FXML
    private TextField roomAllocation_quantity;
    @FXML
    private ComboBox<String> roomAllocation_receipient;
    @FXML
    private ComboBox<String> roomAllocation_returnedFrom;
    @FXML
    private DatePicker roomAllocation_dateIssued;
    @FXML
    private TextField roomAllocation_returnedUnits;
    @FXML
    private ComboBox roomAllocation_productType;
    
    @FXML
    private DatePicker roomAllocation_dateReturned;
    @FXML
    private TextField roomAllocation_searchField;
    @FXML
    private TableView<GeneralStockStore> roomAllocationTable;
    @FXML
    private TableColumn<GeneralStockStore, String> roomColumn_code;
    @FXML
    private TableColumn<GeneralStockStore, String> roomColumn_name;
    @FXML
    private TableColumn<GeneralStockStore, String> roomColumn_quantity;
    @FXML
    private TableColumn<GeneralStockStore, String> roomColumn_department;
    @FXML
    private TableColumn<GeneralStockStore, String> roomColumn_cost;
    @FXML
    private TableColumn<GeneralStockStore, String> roomColumn_alart;
    private final ObservableList<GeneralStockStore> roomAllocationData = FXCollections.observableArrayList();


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO        
        con = DatabaseConnection.connectDb();
        
        //call server communication method
        //tcpsettings();
        ServerPost serverPost = ServerPost.getPreferences();
        Server server = new Server(serverPost.getPort());
        
        
        loadSchoolInfo();
        
       //Create a folder in the C drive to put all TENANT PHOTOS
       File directory = new File("C:/A_Check/Store_photos");
       if (!directory.exists()) {
           if (directory.mkdirs()) {
               System.out.println("Directory created");
           } else {
               System.out.println("Directory not created");
           }
        }
               
        //update combobox
        teacherSection_productType.setItems(productTypeData);
        boardingSector_productType.setItems(productTypeData);
        gamesSector_productType.setItems(productTypeData);
        cateringSector_productType.setItems(productTypeData);
        surbodinateSector_productType.setItems(productTypeData);
        securitySector_productType.setItems(productTypeData);
        productionSector_productType.setItems(productTypeData);
        entertainmentSector_productType.setItems(productTypeData);
        transportSector_productType.setItems(productTypeData);
        roomAllocation_productType.setItems(productTypeData);
        studentSector_productType.setItems(productTypeData);
        updateStudentIDandNamesCombo();
        updateDeskNumberCombo();
        updateChairNumberCombo();
        updateStudentClassNamesCombo();
        
               
        // update tables
        updateTeachingSectorTable();
        updateBoardingSectorTable();
        updateGamesSectorTable();
        updateCateringSectorTable();
        updateSurbodinateSectorTable();
        updateSecuritySectorTable();
        updateProductionSectorTable();
        updateEntertainmentSectorTable();
        updateTransportSectorTable();
        updateStudentSectorTable();
        updateStudentAllocationTable();
              
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
     
            //Method to open a fileChooser dialog box
    public void pdfFileChooserOpener(){
                pdfFileChooser = new FileChooser();
                pdfFileChooser.getExtensionFilters().addAll(                
                new FileChooser.ExtensionFilter("PDF Files","*.pdf")

                
        );   
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
        Image defaultImage = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/store.png").toExternalForm(),imageView.getFitWidth(),imageView.getFitHeight(),false,true);
        imageView.setImage(defaultImage); 
    }    
    
    private void setStudentDefaultImage(ImageView imageView){    
        Image defaultImage = new Image(getClass().getResource("/daviscahtech_assetmanagement/Resources/student.png").toExternalForm(),imageView.getFitWidth(),imageView.getFitHeight(),false,true);
        imageView.setImage(defaultImage); 
    }
    
     //Method to take and open any window
     private void loadWindow(String loc, String title) throws IOException{
        //cretate stage with specified owner and modality                
        Parent root = FXMLLoader.load(getClass().getResource(loc));
        Stage stage = new Stage(StageStyle.UTILITY);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.getIcons().add(new Image(MainPageController.class.getResourceAsStream("/daviscahtech_assetmanagement/Resources/assetcheckpng.png")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();   
    }

    @FXML
    private void openSupplierWindow(ActionEvent event) throws IOException {
        loadWindow("/daviscahtech_assetmanagement/FXML/suppliers.fxml", "Supplier management");
    }
    
    
        //Method to open a fileChooser dialog box
    public void fileChooserOpener(){
                fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files","*.*"),
                new FileChooser.ExtensionFilter("PDF Files","*.pdf"),
                new FileChooser.ExtensionFilter("Excel Files","*.xslx")             

                
//                new FileChooser.ExtensionFilter("Text Files","*.txt"),
//                new FileChooser.ExtensionFilter("Word Files","*.docx"),
//                new FileChooser.ExtensionFilter("Image Files","*.png","*.jpg","*.gif"),
//                new FileChooser.ExtensionFilter("Audio Files","*.wav","*.mp3","*.mp4","*.acc") 
                
        );   
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

    
     //method to validate if input is an EMPTY for blue fields
    private boolean isBlueFieldsEmpty(TextField input){
        input.setStyle("-fx-border-color: blue; -fx-border-radius: 15;-fx-background-radius: 20;");
        
        if (input.getText().isEmpty()) {
            input.setStyle("-fx-border-color: red;");
            input.setText(null);
            input.setPromptText("Field is Empty, must be filled");
            return true;
        } else {            
            return false;
        }
        
    }
    
    // method to validate email addresss
    private boolean validateEmail(TextField input){
    
        Pattern p = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z])+");
        Matcher m = p.matcher(input.getText());
        if (m.find() && m.group().equals(input.getText())) {
            return  true;
        } else {
            
            Alert errorIssue = new Alert(Alert.AlertType.ERROR);
            errorIssue.setTitle("Email Validation Error");
            errorIssue.setGraphic(errorImage);
            errorIssue.setHeaderText(null);
            errorIssue.setContentText("The format of parent's email is incorrect. Enter the correct email format address\n");
            errorIssue.showAndWait(); 
            
            return  false;
        }
    }    

    @FXML
    private void openTeacherSectorWindow(ActionEvent event) throws IOException {
        loadWindow("/daviscahtech_assetmanagement/FXML/teachersection.fxml", "Teaching material section");        
    }

    @FXML
    private void openStudentSectorWindow(ActionEvent event) throws IOException {
        loadWindow("/daviscahtech_assetmanagement/FXML/students.fxml", "Students material section");
    }

    @FXML
    private void openBoardingSectorWindow(ActionEvent event) throws IOException {
        loadWindow("/daviscahtech_assetmanagement/FXML/boardingsection.fxml", "Boarding material section"); 
    }

    @FXML
    private void openGamesSportsWindow(ActionEvent event) throws IOException {
        loadWindow("/daviscahtech_assetmanagement/FXML/gamessection.fxml", "Games material section"); 
    }

    @FXML
    private void openCateringSectorWindow(ActionEvent event) throws IOException {
        loadWindow("/daviscahtech_assetmanagement/FXML/cateringsection.fxml", "Catering material section"); 
    }

    @FXML
    private void openSurbodinateSectorWindow(ActionEvent event) throws IOException {
        loadWindow("/daviscahtech_assetmanagement/FXML/surbordinatesection.fxml", "Surbordinate material section"); 
    }

    @FXML
    private void openSecuritySectorWindow(ActionEvent event) throws IOException {
        loadWindow("/daviscahtech_assetmanagement/FXML/securitysection.fxml", "Security material section"); 
    }

    @FXML
    private void openProductionSectorWindow(ActionEvent event) throws IOException {
        loadWindow("/daviscahtech_assetmanagement/FXML/productionsection.fxml", "Production material section"); 
    }

    @FXML
    private void openEntertainmentSectorWindow(ActionEvent event) throws IOException {
        loadWindow("/daviscahtech_assetmanagement/FXML/entertainmentsection.fxml", "Entertainment material section"); 
    }

    @FXML
    private void openTransportSectorWindow(ActionEvent event) throws IOException {
        loadWindow("/daviscahtech_assetmanagement/FXML/transportsection.fxml", "Transport material section"); 
    }

    @FXML
    private void openSchoolInfoWindow(ActionEvent event) throws IOException {
        loadWindow("/daviscahtech_assetmanagement/FXML/schoolInfo.fxml", "Configure School Settings"); 
    }
    
    private void fillTable(String department,ObservableList data,TableView tableView){

        try { 
            String sql = "SELECT * FROM stock WHERE department = ? ";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, department);
            rs = pstmt.executeQuery();
            while(rs.next()){
                                
                int minLevel = rs.getInt("min_quantity");
                int quantity_avail = rs.getInt("quantity_avail");
                    //concatinate
                String type = rs.getString("type");
                String quantityAndType = quantity_avail+" "+ type;
                
                String alartStatus = null;
                
                if (minLevel >= quantity_avail) {
                    alartStatus = "Running out of stock, re-stock this products";
                } else {
                    int remaining = quantity_avail - minLevel;
                    alartStatus = "Remaining "+remaining+" units to alert level";
                }
                
                data.add( new GeneralStockStore(
                        rs.getString("product_code"),
                        rs.getString("product_name"),
                        quantityAndType,
                        rs.getString("department"),
                        rs.getString("total_cost"),
                        alartStatus
                ));         
            }
            //load items to the table
            tableView.setItems(data);
            tableView.setPlaceholder(new Label("No matches for the details provided", errorImage));
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
    
    private void keyReleasedOnAnyTable(TableView tableView,TextField code,
            TextField name, TextArea desc, ImageView imageView){
        
            tableView.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.DOWN) {
                
                try {
                    GeneralStockStore ts = (GeneralStockStore) tableView.getSelectionModel().getSelectedItem();
                    
                    String sql = "SELECT * FROM stock "
                            + "WHERE product_code = ? AND product_name = ? AND quantity_avail = ? AND department = ? ";
                    pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, ts.getCode());
                    pstmt.setString(2, ts.getName());
                    pstmt.setString(3, ts.getQuantity());
                    pstmt.setString(4, ts.getDepartment());
                    
                    rs=pstmt.executeQuery();
                    
                    if (rs.next()) {
                        
                        stockTablePrimaryID = rs.getInt("id");
                        stockTableForeignID = rs.getInt("product_pk_fk");
                        stockTableQuantityAvailable = rs.getInt("quantity_avail");
                        
                        String add1 = rs.getString("product_code");
                        code.setText(add1); 
                        String add2 = rs.getString("product_name");
                        name.setText(add2);
                        String add3 = rs.getString("product_desc");
                        desc.setText(add3);
                        
                        String imagePath = rs.getString("imagepath");
                         
                        try {
                            image2 = new Image(new FileInputStream(imagePath),imageView.getFitWidth(),imageView.getFitHeight(),false,true);
                            imageView.setImage(image2);                  
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(MainPageController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    }else {
                        errorNotification(Pos.BOTTOM_RIGHT, "Record not found");
                    }                    
                } catch (SQLException ev) {
                }
            }
        });          
    
    }  
    
    private void mouseClickedOnAnyTable(TableView tableView,TextField code,
            TextField name, TextArea desc, ImageView imageView){
                        
                try {
                    GeneralStockStore ts = (GeneralStockStore) tableView.getSelectionModel().getSelectedItem();
                    
                    String sql = "SELECT * FROM stock "
                            + "WHERE product_code = ? AND product_name = ? AND quantity_avail = ? AND department = ? ";
                    pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, ts.getCode());
                    pstmt.setString(2, ts.getName());
                    pstmt.setString(3, ts.getQuantity());
                    pstmt.setString(4, ts.getDepartment());
                    
                    rs=pstmt.executeQuery();
                    
                    if (rs.next()) {
                        
                        stockTablePrimaryID = rs.getInt("id");
                        stockTableForeignID = rs.getInt("product_pk_fk");
                        stockTableQuantityAvailable = rs.getInt("quantity_avail");
                        
                        String add1 = rs.getString("product_code");
                        code.setText(add1); 
                        String add2 = rs.getString("product_name");
                        name.setText(add2);
                        String add3 = rs.getString("product_desc");
                        desc.setText(add3);
                        
                        String imagePath = rs.getString("imagepath");
                         
                        try {
                            image2 = new Image(new FileInputStream(imagePath),imageView.getFitWidth(),imageView.getFitHeight(),false,true);
                            imageView.setImage(image2);                  
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(MainPageController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    }else {
                        errorNotification(Pos.BOTTOM_RIGHT, "Record not found");
                    }                    
                } catch (SQLException ev) {
                }                    
    
    } 

    private void issueAnyStock(TextField qnty,TextField code,TextField name, TextField receipient,
            TextArea desc,DatePicker date,String department){
    
    
        if (qnty.getText().isEmpty()
                ||code.getText().isEmpty()
                || date.getEditor().getText().isEmpty()
                ||desc.getText().isEmpty()
                ||name.getText().isEmpty()
                ||receipient.getText().isEmpty()) {

                String msg = null;

                if (code.getText().isEmpty()) {
                    msg = "Products' code is empty, you must retrieve the products to be issued";
                } else if (qnty.getText().isEmpty()) {
                    msg = "Quantity of stocked units to be issued is empty, you must enter it";
                } else if (date.getEditor().getText().isEmpty()) {
                    msg = "Date when units were issued is empty, you must choose it";
                } else if (desc.getText().isEmpty()) {
                     msg = "Products' description is empty, you must retrieve the products to be issued";
                } else if (name.getText().isEmpty()) {
                    msg = "Products' name is empty, you must retrieve the products to be issued";
                } else if (receipient.getText().isEmpty()) {
                    msg = "Name of teaching staff to receive the items is empty, you must enter it";
                } 

                Alert errorIssue = new Alert(Alert.AlertType.ERROR);
                errorIssue.setTitle("Error");
                errorIssue.setGraphic(errorImage);
                errorIssue.setHeaderText(null);
                errorIssue.setContentText("The could not be issued.\n"
                + msg);
                errorIssue.showAndWait();
        
        }else {
            
            if (isInt(qnty)) {
                
                int unitsToIsue = Integer.parseInt(qnty.getText().trim());
                
                if (unitsToIsue > stockTableQuantityAvailable) {
                    errorNotification(Pos.BOTTOM_RIGHT, "You can not issue more units than available in the store.\n "
                            + "Available units of product "+ name.getText()+ " is "+ stockTableQuantityAvailable);
                } else {

                    //show confirm alert before registering data of issued
                    String ms = " Are you sure you want to issue product(s) = "+name.getText() +
                            " to "+ receipient.getText();
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirm issue");
                    alert.setGraphic(stockedproductImageView);
                    alert.setHeaderText(null);
                    alert.setContentText(ms);

                    Optional<ButtonType> response = alert.showAndWait();
                    if (response.get()==ButtonType.OK) {
                        try {
                            String sql = "INSERT INTO stockout ("
                            + "product_code,product_name,product_desc,units_out,department,given_to,date_given)"
                            + " VALUES(?,?,?,?,?,?,?)";
                            pstmt= con.prepareStatement(sql);
                             
                            pstmt.setString(1, code.getText());                                      
                            pstmt.setString(2, name.getText());
                            pstmt.setString(3, desc.getText());
                            pstmt.setInt(4, Integer.parseInt(qnty.getText().trim()));                        
                            pstmt.setString(5, department);
                            pstmt.setString(6, receipient.getText());
                            pstmt.setString(7, date.getEditor().getText());

                            int i = pstmt.executeUpdate(); // load data into the database
                            if (i>0) {
                                    
                                    //reduce the total units and update
                                    int remaining = stockTableQuantityAvailable - unitsToIsue;
                                
                                    // save the product details to the general stock table
                                    try {
                                        String sqlString = "UPDATE stock SET quantity_avail = ?"
                                                + " WHERE id = '"+stockTablePrimaryID+"' AND product_pk_fk = '"+stockTableForeignID+"' ";
                                        pstmt= con.prepareStatement(sqlString);
                                        pstmt.setInt(1, remaining);
                                        int ii = pstmt.executeUpdate(); // load data into the database 

                                        if (ii>0) {
                                            String sc = "Stock issue and other changes have successfully been effected in the system.";
                                            successNotification(Pos.BOTTOM_RIGHT, sc);

                                            clearFields();
                                            refreshTeachingTable();
                                            refreshBordingTable();
                                            refreshGamesTable();
                                            refreshCateringTable();
                                            refreshSurbodinateTable();
                                            refreshSecurityTable();
                                            refreshproductionTable();
                                            refreshEntertainmentTable();
                                            refreshTransportTable();
                                            refreshStudentSectorTable();
                                        }

                                        } catch (Exception e) {
                                    }
                            } else {
                                String er = "OOPS! Could not issue product due  to internal error";
                                errorNotification(Pos.BOTTOM_RIGHT, er);
                            }
                            pstmt.close();

                        } catch (SQLException | NumberFormatException e) {
                            String er = "OOPS! Could not issue product due  to internal error";
                            errorNotification(Pos.BOTTOM_RIGHT, er);
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
        }        
    } 
    
    private void returnAnyStock(TextField qnty,TextField code,TextField name, TextField returnedBy,
            TextArea desc,DatePicker date,ComboBox type,String department){
        
        if (qnty.getText().isEmpty()
                ||code.getText().isEmpty()
                || date.getEditor().getText().isEmpty()
                ||desc.getText().isEmpty()
                ||returnedBy.getText().isEmpty()
                ||type.getSelectionModel().isEmpty()
                ||name.getText().isEmpty()) {

                String msg = null;

                if (code.getText().isEmpty()) {
                    msg = "Products' code is empty, you must retrieve the products to be issued";
                } else if (qnty.getText().isEmpty()) {
                    msg = "Quantity of units to be returned is empty, you must enter it";
                } else if (date.getEditor().getText().isEmpty()) {
                    msg = "Date when units were returned is empty, you must choose it";
                } else if (desc.getText().isEmpty()) {
                     msg = "Products' description is empty, you must retrieve the products to be issued";
                } else if (name.getText().isEmpty()) {
                    msg = "Products' name is empty, you must retrieve the products to be issued";
                } else if (returnedBy.getText().isEmpty()) {
                    msg = "Name of person who returned the items is empty, you must enter it";
                } else if (type.getSelectionModel().isEmpty()) {
                    msg = "The quantity nature/size of the units returned is empty, you must select from drop-down menu";
                } 

                Alert errorIssue = new Alert(Alert.AlertType.ERROR);
                errorIssue.setTitle("Error");
                errorIssue.setGraphic(errorImage);
                errorIssue.setHeaderText(null);
                errorIssue.setContentText("The product could not be return.\n"
                + msg);
                errorIssue.showAndWait();
        
        }else {
            
            if (isInt(qnty)) {
                
                    //show confirm alert before registering data of issued
                    String ms = " Are you sure you want to receive product(s) = "+name.getText() +
                            " from "+ returnedBy.getText() +".\n "
                            + "Receiving returned stock will increase the total stocked level of this product.";
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirm");
                    alert.setGraphic(stockedproductImageView);
                    alert.setHeaderText(null);
                    alert.setContentText(ms);

                    Optional<ButtonType> response = alert.showAndWait();
                    if (response.get()==ButtonType.OK) {
                        try {
                            String sql = "INSERT INTO stockreturn ("
                            + "product_code,product_name,product_desc,department,units_in,type,returnedby,date_returned)"
                            + " VALUES(?,?,?,?,?,?,?,?)";
                            pstmt= con.prepareStatement(sql);
                             
                            pstmt.setString(1, code.getText());                                      
                            pstmt.setString(2, name.getText());
                            pstmt.setString(3, desc.getText());
                            pstmt.setString(4, department);
                            pstmt.setInt(5, Integer.parseInt(qnty.getText().trim()));                     
                            pstmt.setString(6, type.getSelectionModel().getSelectedItem().toString());
                            pstmt.setString(7, returnedBy.getText());
                            pstmt.setString(8, date.getEditor().getText());

                            int i = pstmt.executeUpdate(); // load data into the database
                            if (i>0) {
                                    int unitsToReturn = Integer.parseInt(qnty.getText().trim());
                                    //reduce the total units and update
                                    int remaining = stockTableQuantityAvailable + unitsToReturn;
                                
                                    // save the product details to the general stock table
                                    try {
                                        String sqlString = "UPDATE stock SET quantity_avail = ?"
                                                + " WHERE id = '"+stockTablePrimaryID+"' AND product_pk_fk = '"+stockTableForeignID+"' ";
                                        pstmt= con.prepareStatement(sqlString);
                                        pstmt.setInt(1, remaining);
                                        int ii = pstmt.executeUpdate(); // load data into the database 

                                        if (ii>0) {
                                            String sc = "Stock return and other changes have successfully been effected in the system.";
                                            successNotification(Pos.BOTTOM_RIGHT, sc);

                                            clearFields();
                                            refreshTeachingTable();
                                            refreshBordingTable();
                                            refreshGamesTable();
                                            refreshCateringTable();
                                            refreshSurbodinateTable();
                                            refreshSecurityTable();
                                            refreshproductionTable();
                                            refreshEntertainmentTable();
                                            refreshTransportTable();
                                            refreshStudentSectorTable();
                                        }

                                        } catch (Exception e) {
                                    }


                            } else {
                                String er = "OOPS! Could not return product due  to internal error";
                                errorNotification(Pos.BOTTOM_RIGHT, er);
                            }
                            pstmt.close();

                        } catch (SQLException | NumberFormatException e) {
                            String er = "OOPS! Could not return product due  to internal error";
                            errorNotification(Pos.BOTTOM_RIGHT, er);
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
    
    }
    
    
       //Method to monitor tenant's profile update on table upon searching
    private void changeAnyTableOnSearch(ObservableList data,TextField serchField,TableView tableView){
        
        //wrap the observablelist in a FilteredList()
        FilteredList<GeneralStockStore> filteredData =  new FilteredList<>(data,p -> true);
        
        //set the filter predicate whenever the filter changes
        serchField.textProperty().addListener((observable,oldValue,newValue)->{
                
            filteredData.setPredicate(GeneralStockStore -> {
            
                //if filter text is empty, display all product
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                
                //compare product id and product name of every product with the filter text
                String lowerCaseFilter =newValue.toLowerCase();
               
                if (GeneralStockStore.getCode().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches product Id
                } else if (GeneralStockStore.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches product Id
                }  else if (GeneralStockStore.getQuantity().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches product Id
                }  
                return false; // Do not match            
            });
        });
        
        //wrap the filteredList in a sortedList
        SortedList<GeneralStockStore> sortedData = new SortedList<>(filteredData);
        
        //bind the sortedData to the Tableview
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        
        //add sorted and filtered data to the table
        tableView.setItems(sortedData); 
    }  
    
       //Method to monitor tenant's profile update on table upon searching
    private void changeStudentAllocationTableOnSearch(ObservableList data,TextField serchField,TableView tableView){
        
        //wrap the observablelist in a FilteredList()
        FilteredList<StudentAllocation> filteredData =  new FilteredList<>(data,p -> true);
        
        //set the filter predicate whenever the filter changes
        serchField.textProperty().addListener((observable,oldValue,newValue)->{
                
            filteredData.setPredicate(StudentAllocation -> {
            
                //if filter text is empty, display all product
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                
                //compare product id and product name of every product with the filter text
                String lowerCaseFilter =newValue.toLowerCase();
               
                if (StudentAllocation.getStudentID().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches product Id
                } else if (StudentAllocation.getStudentName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches product Id
                }  else if (StudentAllocation.getStudentClass().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches product Id
                }  else if (StudentAllocation.getChairNo().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches product Id
                }   else if (StudentAllocation.getDeskNo().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches product Id
                }   
                return false; // Do not match            
            });
        });
        
        //wrap the filteredList in a sortedList
        SortedList<StudentAllocation> sortedData = new SortedList<>(filteredData);
        
        //bind the sortedData to the Tableview
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        
        //add sorted and filtered data to the table
        tableView.setItems(sortedData); 
    }     
        
    private void populateAnyTable(TableColumn code,TableColumn name,TableColumn qnty,
            TableColumn depart,TableColumn cost,TableColumn alart,ObservableList data,
            TableView tableView,String department){
    
        code.setCellValueFactory(new PropertyValueFactory<>("code"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        qnty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        depart.setCellValueFactory(new PropertyValueFactory<>("department"));
        cost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        
        //Customize data rendering in this column
                    //teachingColumn_alartStatus.setCellValueFactory(new PropertyValueFactory<>("alartStatus"));
        alart.setCellFactory(col ->{
        
            TableCell<GeneralStockStore,String> cell = new TableCell<GeneralStockStore,String>(){
            
                @Override
                public void updateItem(String item,boolean empty){
                    super.updateItem(item, empty);
                    
                    if (!empty) {
                        if (this.getTableRow() != null) {
                            //get the cell
                            int rowIndex = this.getTableRow().getIndex();
                            
                            GeneralStockStore ts = this.getTableView().getItems().get(rowIndex);
                            String alart = ts.getAlartStatus();                            
                            this.setText(alart);
                            
                            if (alart.startsWith("Running")) {
                                this.setTextFill(Paint.valueOf("Red"));
                            } else {
                                this.setTextFill(Paint.valueOf("Green"));
                            } 
                        }
                    }               
                }
                
            };
            return cell;
        });
        
        fillTable(department, data, tableView);  
    }
    
    private void clearFields(){
        setDefaultImage(teachingSectorImageView);
        setDefaultImage(boardingSectrImageview);
        setDefaultImage(gamesSectorImageView);
        setDefaultImage(cateringSectorImageView);
        setDefaultImage(successNotiyiImageView);
        setDefaultImage(securitySectorImageview);
        setDefaultImage(productionImageView);
        setDefaultImage(enetertainmentImageview);
        setDefaultImage(transportSectorImageView);
        setDefaultImage(roomsAllocationImageView);
        setDefaultImage(studentSectorImageView);
    
        teachingSector_QuantityIssued.clear();
        teachingSector_code.clear();
        teachingSector_dateIssued.getEditor().clear();
        teachingSector_desc.clear();
        teachingSector_name.clear();
        teachingSector_receipientName.clear();
        teacherSection_dateReturned.getEditor().clear();
        teacherSection_returnUnits.clear();
        teacherSection_returnedBy.clear();
        
        boardingSector_code.clear();
        boardingSector_desc.clear();
        boardingSector_dateIssued.getEditor().clear();
        boardingSector_name.clear();
        boardingSector_quantity.clear();
        boardingSector_receipientName.clear();
        boardingSector_dateReturned.getEditor().clear();
        boardingSector_returnedUnits.clear();
        boardingSector_returnedBy.clear();  
        
        gamesSector_code.clear();
        gamesSector_desc.clear();
        gamesSector_dateIssued.getEditor().clear();
        gamesSector_name.clear();
        gamesSector_quantity.clear();
        gamesSector_receipient.clear();
        gamesSector_dateReturned.getEditor().clear();
        gamesSector_returnedUnits.clear();
        gamesSector_returnedBy.clear();
        
        cateringSector_code.clear();
        cateringSector_desc.clear();
        cateringSector_dateIssued.getEditor().clear();
        cateringSector_name.clear();
        cateringSector_quantity.clear();
        cateringSector_receipient.clear();
        cateringSector_dateReturned.getEditor().clear();
        cateringSector_returnedUnits.clear();
        cateringSector_returnedBy.clear();
        
        surbodinateSector_code.clear();
        surbodinateSector_desc.clear();
        surbodinateSector_dateIssued.getEditor().clear();
        surbodinateSector_name.clear();
        surbodinateSector_quantity.clear();
        surbodinateSector_recipient.clear();
        surbodinateSector_dateReturned.getEditor().clear();
        surbodinateSector_returnedUnits.clear();
        surbodinateSector_returnedBy.clear(); 
        
        securitySector_code.clear();
        securitySector_desc.clear();
        securitySector_dateIssued.getEditor().clear();
        securitySector_name.clear();
        securitySector_quantity.clear();
        securitySector_receipient.clear();
        securitySector_returnedDate.getEditor().clear();
        securitySector_returnedUnits.clear();
        securitySector__returnedBy.clear();    
        
        productionSector_code.clear();
        productionSector_desc.clear();
        productionSector_dateissued.getEditor().clear();
        productionSector_name.clear();
        productionSector_quantity.clear();
        productionSector_recipient.clear();
        productionSector_dateReturned.getEditor().clear();
        productionSector_returnedUnits.clear();
        productionSector_returnedBy.clear();  
        
        entertainmentSector_code.clear();
        entertainmentSector_desc.clear();
        entertainmentSector_dateIssued.getEditor().clear();
        entertainmentSector_name.clear();
        entertainmentSector_quantity.clear();
        entertainmentSector_receipient.clear();
        entertainmentSector_dateReturned.getEditor().clear();
        entertainmentSector_returnedUnits.clear();
        entertainmentSector_returnedBy.clear();   
        
        transportSector_code.clear();
        transportSector_desc.clear();
        transportSector_dateIssued.getEditor().clear();
        transportSector_name.clear();
        transportSector_quantity.clear();
        transportSector_recipient.clear();
        transportSector_dateReturned.getEditor().clear();
        transportSector_returnedUnits.clear();
        transportSector_returnedBy.clear(); 
        
        roomAllocation_code.clear();
        roomAllocation_desc.clear();
        roomAllocation_dateIssued.getEditor().clear();
        roomAllocation_name.clear();
        roomAllocation_quantity.clear();
        refreshStudentClassCombo();
        roomAllocation_dateReturned.getEditor().clear();
        roomAllocation_returnedUnits.clear();

        studentSector_code.clear();
        studentSector_desc.clear();
        studentSector_dateIssued.getEditor().clear();
        studentSector_name.clear();
        studentSector_quantity.clear();
        studentSector_IDName.clear();
        studentSector_dateReturned.getEditor().clear();
        studentSector_returnedUnits.clear();
        studentSector_returnedBy.clear();         
        
        
    }
    
    
//################################## Teaching sector operations #######################
    
    
    private void updateTeachingSectorTable(){
        populateAnyTable(teachingColumn_code, teachingColumn_name, teachingColumn_quantity,
                teachingColumn_lastStocked, teachingColumn_totalCost, teachingColumn_alartStatus,
                teacherStoreData, teachingTable, "Teaching");

    }  
    
    @FXML
    private void teachingTableKeyReleased(KeyEvent event) {
        keyReleasedOnAnyTable(teachingTable, teachingSector_code, teachingSector_name, teachingSector_desc, teachingSectorImageView);
    }
    


    @FXML
    private void teachingTableMouseClicked(MouseEvent event) {        
        mouseClickedOnAnyTable(teachingTable, teachingSector_code, teachingSector_name, teachingSector_desc, teachingSectorImageView);    
    }

    @FXML
    private void teachingSector_IssueOperation(ActionEvent event) {        
        issueAnyStock(teachingSector_QuantityIssued, teachingSector_code, teachingSector_name,
                teachingSector_receipientName, teachingSector_desc, teachingSector_dateIssued, "Teaching"); 
    }

    @FXML
    private void teachingSector_clearFieldOperation(ActionEvent event) {
        clearFields();
    }
    

    @FXML
    private void teachingSector_searchFieldMouseClicked(MouseEvent event) {
        changeAnyTableOnSearch(teacherStoreData, teachingSector_searchField, teachingTable);
    }

    @FXML
    private void teachingSector_refreshTableOperation(ActionEvent event) {
        refreshTeachingTable();
    }
    
    private void refreshTeachingTable(){    
        teacherStoreData.clear();
        updateTeachingSectorTable();
    }

    @FXML
    private void teacherSectorReturnOperation(ActionEvent event) {
        returnAnyStock(teacherSection_returnUnits, teachingSector_code, teachingSector_name,
                teacherSection_returnedBy, teachingSector_desc, teacherSection_dateReturned,
                teacherSection_productType, "Teaching"); 
    }

    @FXML
    private void clearTeacherSectorFieldAgain(ActionEvent event) {
        clearFields();
    }
//################################### bording sector ############################ 
 
    private void updateBoardingSectorTable(){
        populateAnyTable(bordingColumn_code, bordingColumn_name, bordingColumn_quantity,
                bordingColumn_department, bordingColumn_cost, bordingColumn_alartStatus, boardingStoreData,
                boardingSectorTable, "Boarding");
       
    }
    
    private void refreshBordingTable(){    
        boardingStoreData.clear();
        updateBoardingSectorTable();
    }

    @FXML
    private void boardingSector_issueOperation(ActionEvent event) {        
        issueAnyStock(boardingSector_quantity, boardingSector_code, boardingSector_name,
                boardingSector_receipientName, boardingSector_desc, boardingSector_dateIssued, "Boarding");
    }
    


    @FXML
    private void boardingSector_clearFieldsOperation(ActionEvent event) {
        clearFields();
    }

    @FXML
    private void boardingSector_returnOperation(ActionEvent event) {       
        returnAnyStock(boardingSector_returnedUnits, boardingSector_code, boardingSector_name, 
                boardingSector_returnedBy, boardingSector_desc, boardingSector_dateReturned, boardingSector_productType , "Boarding");
        
    }

    @FXML
    private void boardingSector_clearMoreFieldsOperation(ActionEvent event) {
        clearFields();
    }

    @FXML
    private void boardingSector_refreshBordingTableOperation(ActionEvent event) {
        refreshBordingTable();
    }
    
    @FXML
    private void boardingSector_searchFieldMouseClicked(MouseEvent event) {
        changeAnyTableOnSearch(boardingStoreData, boardingSector_searchField, boardingSectorTable);
    }
           

    @FXML
    private void boardingSectorTableMouseClicked(MouseEvent event) {
        mouseClickedOnAnyTable(boardingSectorTable, boardingSector_code, boardingSector_name, boardingSector_desc, boardingSectrImageview);
       
    }

    @FXML
    private void boardingSectorTableKeyReleased(KeyEvent event) {
       keyReleasedOnAnyTable(boardingSectorTable, boardingSector_code, boardingSector_name, boardingSector_desc, boardingSectrImageview);
    }
    
//####################################### Games and Sports Sector #####################    

    private void updateGamesSectorTable(){
        populateAnyTable(gamesColumn_code, gamesColumn_name, gamesColumn_quantity,
                gamesColumn_department, gamesColumn_cost, gamesColumn_alart, gamesStoreData,
                gamesTable, "Games and Sports");
       
    }
    
    private void refreshGamesTable(){    
        gamesStoreData.clear();
        updateGamesSectorTable();
    }    
    
    @FXML
    private void gamesSector_issueOperation(ActionEvent event) {
        issueAnyStock(gamesSector_quantity, gamesSector_code, gamesSector_name,
                gamesSector_receipient, gamesSector_desc, gamesSector_dateIssued, "Games and Sports");        
    }

    @FXML
    private void gamesSector_clearIssueFieldOperation(ActionEvent event) {
        clearFields();
    }

    @FXML
    private void gamesSector_returnOperation(ActionEvent event) {
        returnAnyStock(gamesSector_returnedUnits, gamesSector_code, gamesSector_name, gamesSector_returnedBy,
                gamesSector_desc, gamesSector_dateReturned, gamesSector_productType, "Games and Sports");
    }

    @FXML
    private void gamesSector_clearReturnFieldOperation(ActionEvent event) {
        clearFields();
    }

    @FXML
    private void gamesSector_searchFieldMouseClick(MouseEvent event) {
        changeAnyTableOnSearch(gamesStoreData, gamesSector_searchField, gamesTable);
        
    }

    @FXML
    private void gamesTableMouseCliked(MouseEvent event) {
        mouseClickedOnAnyTable(gamesTable, gamesSector_code, gamesSector_name, gamesSector_desc, gamesSectorImageView);
    }

    @FXML
    private void gamesTableKeyReleased(KeyEvent event) {
        keyReleasedOnAnyTable(gamesTable, gamesSector_code, gamesSector_name, gamesSector_desc, gamesSectorImageView);
    }

    @FXML
    private void gamesSector_refreshGamesTable(ActionEvent event) {
        refreshGamesTable();
    }
   
//########################## catering sector #############################################  
    
    private void updateCateringSectorTable(){
        populateAnyTable(cateringColumn_code, cateringColumn_name, cateringColumn_quantity,
                cateringColumn_department, cateringColumn_cost, cateringColumn_alart, cateringStoreData,
                CateringTable, "Catering");
       
    }
    
    private void refreshCateringTable(){    
        cateringStoreData.clear();
        updateCateringSectorTable();
    }     

    @FXML
    private void cateringSector_issueOperation(ActionEvent event) {
        issueAnyStock(cateringSector_quantity, cateringSector_code, cateringSector_name, 
                cateringSector_receipient, cateringSector_desc, cateringSector_dateIssued, "Catering");
    }

    @FXML
    private void cateringSector_clearIssueField(ActionEvent event) {
        clearFields();
    }

    @FXML
    private void cateringSector_returnOperation(ActionEvent event) {
        returnAnyStock(cateringSector_returnedUnits, cateringSector_code, cateringSector_name, cateringSector_returnedBy,
                cateringSector_desc, cateringSector_dateReturned, cateringSector_productType, "Catering");
    }

    @FXML
    private void cateringSector_clearReturnOperation(ActionEvent event) {
        clearFields();
    }

    @FXML
    private void cateringSector_refreshCateringTable(ActionEvent event) {
        refreshCateringTable();
    }

    @FXML
    private void CateringTableMouseClicked(MouseEvent event) {
        mouseClickedOnAnyTable(CateringTable, cateringSector_code, cateringSector_name, cateringSector_desc, cateringSectorImageView);
    }

    @FXML
    private void CateringTableKeyReleased(KeyEvent event) {
         keyReleasedOnAnyTable(CateringTable, cateringSector_code, cateringSector_name, cateringSector_desc, cateringSectorImageView);
    }

    @FXML
    private void cateringSector_searchFieldMouseClicked(MouseEvent event) {
        changeAnyTableOnSearch(cateringStoreData, cateringSector_searchField, CateringTable);
    }
    
//####################################### Surbodinate sector ##############################    

    private void updateSurbodinateSectorTable(){
        populateAnyTable(surbodinateColum_code, surbodinateColum_name, surbodinateColum_quantity,
                surbodinateColum_department, surbodinateColum_cost, surbodinateColum_alart, surbodinateStoreData,
                surbodinateTable, "Subordinate");
       
    }
    
    private void refreshSurbodinateTable(){    
        surbodinateStoreData.clear();
        updateSurbodinateSectorTable();
    }     
    
    
    @FXML
    private void surbodinateSector_issueOperation(ActionEvent event) {
        issueAnyStock(surbodinateSector_quantity, surbodinateSector_code, surbodinateSector_name, surbodinateSector_recipient,
                surbodinateSector_desc, surbodinateSector_dateIssued, "Subordinate");
    }

    @FXML
    private void surbodinateSector_clearIssueField(ActionEvent event) {
        clearFields();
    }

    @FXML
    private void surbodinateSector_returnOperation(ActionEvent event) {
        returnAnyStock(surbodinateSector_returnedUnits, surbodinateSector_code, surbodinateSector_name, surbodinateSector_returnedBy,
                surbodinateSector_desc, surbodinateSector_dateReturned, surbodinateSector_productType, "Subordinate");
    }

    @FXML
    private void surbodinateSector_clearReturnFieldsOperation(ActionEvent event) {
        clearFields();
    }

    @FXML
    private void surbodinateSector_searchFieldMouseClicked(MouseEvent event) {
        changeAnyTableOnSearch(surbodinateStoreData, surbodinateSector_searchField, surbodinateTable);
    }

    @FXML
    private void surbodinateSector_refreshTableOperation(ActionEvent event) {
        refreshSurbodinateTable();
    }

    @FXML
    private void surbodinateTableMouseClicked(MouseEvent event) {
        mouseClickedOnAnyTable(surbodinateTable, surbodinateSector_code, surbodinateSector_name, surbodinateSector_desc, surbodinateSectorImageView);
    }

    @FXML
    private void surbodinateTableKeyReleased(KeyEvent event) {
        keyReleasedOnAnyTable(surbodinateTable, surbodinateSector_code, surbodinateSector_name, surbodinateSector_desc, surbodinateSectorImageView);
    }
//################################# security sector ############################
    
    private void updateSecuritySectorTable(){
        populateAnyTable(SecurityColumn_code, SecurityColumn_name, SecurityColumn_quantity,
                SecurityColumn_department, SecurityColumn_cost, SecurityColumn_alart, securityStoreData,
                SecurityTable, "Security");
       
    }
    
    private void refreshSecurityTable(){    
        securityStoreData.clear();
        updateSecuritySectorTable();
    }    

    @FXML
    private void securitySector_issueOperation(ActionEvent event) {
        issueAnyStock(securitySector_quantity, securitySector_code, securitySector_name, securitySector_receipient,
                securitySector_desc, securitySector_dateIssued, "Security");
    }

    @FXML
    private void securitySector_clearIssueFieldOperation(ActionEvent event) {
        clearFields();
    }

    @FXML
    private void securitySector_returnOperation(ActionEvent event) {
        returnAnyStock(securitySector_returnedUnits, securitySector_code, securitySector_name, securitySector__returnedBy,
                securitySector_desc, securitySector_returnedDate, securitySector_productType, "Security");
    }

    @FXML
    private void securitySector_clearReturnFieldOperation(ActionEvent event) {
        clearFields();
    }

    @FXML
    private void securitySector_searchFieldMouseClicked(MouseEvent event) {
        changeAnyTableOnSearch(securityStoreData, securitySector_searchField, SecurityTable);
    }

    @FXML
    private void securitySector_refreshTableOperation(ActionEvent event) {
        refreshSecurityTable();
    }

    @FXML
    private void SecurityTableMouseClicked(MouseEvent event) {
        mouseClickedOnAnyTable(SecurityTable, securitySector_code, securitySector_name, securitySector_desc, securitySectorImageview);
    }

    @FXML
    private void SecurityTableKeyReleased(KeyEvent event) {
        keyReleasedOnAnyTable(SecurityTable, securitySector_code, securitySector_name, securitySector_desc, securitySectorImageview);
    }
//################################# production sector ############################

    private void updateProductionSectorTable(){
        populateAnyTable(productionColumn_code, productionColumn_name, productionColumn_quantity,
                productionColumn_department, productionColumn_cost, productionColumn_alart, productionStoreData,
                productionTable, "Production");
       
    }
    
    private void refreshproductionTable(){    
        productionStoreData.clear();
        updateProductionSectorTable();
    }    
    
    @FXML
    private void productionSector_issueOperation(ActionEvent event) {
        issueAnyStock(productionSector_quantity, productionSector_code, productionSector_name, productionSector_recipient,
                productionSector_desc, productionSector_dateissued, "Production");
    }

    @FXML
    private void productionSector_clearIssueFields(ActionEvent event) {
        clearFields();
    }

    @FXML
    private void productionSector_returnOperation(ActionEvent event) {
        returnAnyStock(productionSector_returnedUnits, productionSector_code, productionSector_name, productionSector_returnedBy,
                productionSector_desc, productionSector_dateReturned, productionSector_productType, "Production");
    }

    @FXML
    private void productionSector_clearReturnFields(ActionEvent event) {
        clearFields();
    }

    @FXML
    private void productionSector_searchFieldMouseClicked(MouseEvent event) {
        changeAnyTableOnSearch(productionStoreData, productionSector_searchField, productionTable);
    }

    @FXML
    private void productionSector_refreshTableOperation(ActionEvent event) {
        refreshproductionTable();
    }

    @FXML
    private void productionTableMouseClicked(MouseEvent event) {
        mouseClickedOnAnyTable(productionTable, productionSector_code, productionSector_name, productionSector_desc, productionImageView);
    }

    @FXML
    private void productionTableKeyReleased(KeyEvent event) {
         keyReleasedOnAnyTable(productionTable, productionSector_code, productionSector_name, productionSector_desc, productionImageView);
    }
    
//########################### Entertainment sector #####################################

    private void updateEntertainmentSectorTable(){
        populateAnyTable(entertainmentColumn_code, entertainmentColumn_name, entertainmentColumn_quantity,
                entertainmentColumn_department, entertainmentColumn_cost, entertainmentColumn_alart, entertainmentStoreData,
                entertainmentTable, "Entertainment");
       
    }
    
    private void refreshEntertainmentTable(){    
        entertainmentStoreData.clear();
        updateEntertainmentSectorTable();
    }
    
    @FXML
    private void entertainmentSector_issueOperation(ActionEvent event) {
        issueAnyStock(entertainmentSector_quantity, entertainmentSector_code, entertainmentSector_name,
                entertainmentSector_receipient, entertainmentSector_desc, entertainmentSector_dateIssued, "Entertainment");
    }

    @FXML
    private void entertainmentSector_clearIssueFields(ActionEvent event) {
        clearFields();
    }

    @FXML
    private void entertainmentSector_returnOperation(ActionEvent event) {
        returnAnyStock(entertainmentSector_returnedUnits, entertainmentSector_code, entertainmentSector_name, 
                entertainmentSector_returnedBy, entertainmentSector_desc, entertainmentSector_dateReturned, entertainmentSector_productType, "Entertainment");
    }

    @FXML
    private void entertainmentSector_clearReturnField(ActionEvent event) {
        clearFields();
    }

    @FXML
    private void entertainmentSector_searchFieldMouseClicked(MouseEvent event) {
        changeAnyTableOnSearch(entertainmentStoreData, entertainmentSector_searchField, entertainmentTable);
    }

    @FXML
    private void entertainmentSector_refreshTableOperation(ActionEvent event) {
        refreshEntertainmentTable();
    }

    @FXML
    private void entertainmentTableMouseClicked(MouseEvent event) {
        mouseClickedOnAnyTable(entertainmentTable, entertainmentSector_code, entertainmentSector_name, entertainmentSector_desc, enetertainmentImageview);
    }

    @FXML
    private void entertainmentTableKeyReleased(KeyEvent event) {
        keyReleasedOnAnyTable(entertainmentTable, entertainmentSector_code, entertainmentSector_name, entertainmentSector_desc, enetertainmentImageview);
    }
    
//############################################### Transport sector ################    

    private void updateTransportSectorTable(){
        populateAnyTable(transportColumn_code, transportColumn_name, transportColumn_quantity,
                transportColumn_department, transportColumn_cost, transportColumn_alart, transportStoreData,
                transportTable, "Transport");
       
    }
    
    private void refreshTransportTable(){    
        transportStoreData.clear();
        updateTransportSectorTable();
    }
    
    @FXML
    private void transportSector_issueOperation(ActionEvent event) {
        issueAnyStock(transportSector_quantity, transportSector_code, transportSector_name,
                transportSector_recipient, transportSector_desc, transportSector_dateIssued, "Transport");
    }

    @FXML
    private void transportSector_clearIssueFields(ActionEvent event) {
        clearFields();
    }

    @FXML
    private void transportSector_returnOperation(ActionEvent event) {
        returnAnyStock(transportSector_returnedUnits, transportSector_code, transportSector_name,
                transportSector_returnedBy, transportSector_desc, transportSector_dateReturned, transportSector_productType, "Transport");
    }

    @FXML
    private void transportSector_clearReturnFields(ActionEvent event) {
        clearFields();
    }

    @FXML
    private void transportSector_searchFieldOperation(MouseEvent event) {
        changeAnyTableOnSearch(transportStoreData, transportSector_searchField, transportTable);
    }

    @FXML
    private void transportSector_refreshTableOperation(ActionEvent event) {
        refreshTransportTable();
    }

    @FXML
    private void transportTableMouseClicked(MouseEvent event) {
        mouseClickedOnAnyTable(transportTable, transportSector_code, transportSector_name, transportSector_desc, transportSectorImageView);
    }

    @FXML
    private void transportTableKeyReleased(KeyEvent event) {
        keyReleasedOnAnyTable(transportTable, transportSector_code, transportSector_name, transportSector_desc, transportSectorImageView);
    }
    
//############################################ student issued learning material ##################################  
    
    private void updateStudentSectorTable(){
        populateAnyTable(studentSectorColumn_code, studentSectorColumn_name, studentSectorColumn_quantity,
                studentSectorColumn_department, studentSectorColumn_cost, studentSectorColumn_alart, studentSectorStoreData,
                studentSectorTable, "Students");
       
    }
    
    private void refreshStudentSectorTable(){    
        studentSectorStoreData.clear();
        updateStudentSectorTable();
    }    

    
    @FXML
    private void studentSector_isssueOperation(ActionEvent event) {
        issueAnyStock(studentSector_quantity, studentSector_code, studentSector_name, studentSector_IDName,
                studentSector_desc, studentSector_dateIssued, "Students");
    }

    @FXML
    private void studentSector_ClearIssueFields(ActionEvent event) {
        clearFields();
    }

    @FXML
    private void studentSector_returnedOperation(ActionEvent event) {
        returnAnyStock(studentSector_returnedUnits, studentSector_code, studentSector_name,
                studentSector_returnedBy, studentSector_desc, studentSector_dateReturned, 
                studentSector_productType, "Students");
    }

    @FXML
    private void studentSector_clearReturnedOperation(ActionEvent event) {
        clearFields();
    }

    @FXML
    private void studentSector_searchFieldMouseClicked(MouseEvent event) {
        changeAnyTableOnSearch(studentSectorStoreData, studentSector_searchField, studentSectorTable);
    }

    @FXML
    private void studentSector_refreshStudentSectorTable(ActionEvent event) {
        refreshStudentSectorTable();
    }

    @FXML
    private void studentSectorTableMouseClicked(MouseEvent event) {
        mouseClickedOnAnyTable(studentSectorTable, studentSector_code, studentSector_name, studentSector_desc, studentSectorImageView);
    }

    @FXML
    private void studentSectorTableKeyRTeleased(KeyEvent event) {
        keyReleasedOnAnyTable(studentSectorTable, studentSector_code, studentSector_name, studentSector_desc, studentSectorImageView);
    }
    
// #################################################################### desk & chair allocation #############    
            //Method to open a fileChooser dialog box
    public void excelFileChooserOpener(){
                excelFileChooser = new FileChooser();
                excelFileChooser.getExtensionFilters().addAll(                
                new FileChooser.ExtensionFilter("Excel Files","*.*")

                
        );   
    } 
    
    @FXML
    private void importAllocationFromExcel(ActionEvent event) {
      
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Import students allocation data");
            alert.setGraphic(stockedproductImageView);
            alert.setHeaderText(null);
            alert.setContentText("Only data in Excel format and with an extension of .xlsx can be imported in the correct format.\n\n"
                    + "Are you sure you want to import students' allocation data from outside the system? ");
           
            Optional<ButtonType> response = alert.showAndWait();            
            if (response.get()==ButtonType.OK) {
                
                excelFileChooserOpener();
                   excelFileChooser.setTitle("Select Students' allocation data");
                   //single File selection
                   excelFile = excelFileChooser.showOpenDialog(Stage);
                    if (excelFile != null) {
                        String filePath = excelFile.getAbsolutePath();
                        
                        try {
                            String sql = "INSERT INTO deskchairissued"
                                    + " (student_id,student_name,student_class,desk_no,chair_no)"
                                    + " VALUES(?,?,?,?,?)";
                            pstmt= con.prepareStatement(sql);
                            
                            FileInputStream fileIn = new FileInputStream(new File(filePath));
                            
                            XSSFWorkbook wb = new XSSFWorkbook(fileIn);
                            XSSFSheet sheet = wb.getSheetAt(0);
                            Row row;
                             
                            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                                
                                
                                row = sheet.getRow(i);
                                //for student ID
                                    if (isRowValueDouble(row)) {
                                        int id = (int) row.getCell(0).getNumericCellValue();
                                        pstmt.setString(1, String.valueOf(id));
                                        
                                    } else {
                                        pstmt.setString(1, row.getCell(0).getStringCellValue());
                                    } 
                                    
                                //for student name
                                pstmt.setString(2, row.getCell(1).getStringCellValue());
                                pstmt.setString(3, row.getCell(2).getStringCellValue());
                                pstmt.setString(4, row.getCell(3).getStringCellValue());
                                pstmt.setString(5, row.getCell(4).getStringCellValue());                                
                         
                                pstmt.execute();
                                 
                                   
                            }                                    
                                    successNotification(Pos.BOTTOM_RIGHT, "Successfully imported students' allocation data from this file.\n\n" + filePath);
                                    
                                    wb.close();
                                    fileIn.close();
                                    pstmt.close();
                                    
                               refreshStudentAllocationTable();
                            
                        } catch (SQLException | IOException e) {
                            
                            Alert at = new Alert(Alert.AlertType.ERROR);
                            at.setTitle("Error in registration");
                            at.setGraphic(errorImage);
                            at.setHeaderText(null);
                            at.setContentText("Internal server Error occurred and some students' allocation data was NOT REGISTERED. The following errors might have occured \n\n"
                                    + "1. Two or more students share same students ID noted below."
                                    + " Therefore only one student will be registered and the other student(s) after the duplicate will NOT be REGISTERED.\n\n"+e);
                            at.showAndWait(); 
                            
                            refreshStudentAllocationTable();
 
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
    private void student_chooseRegisteredIDOperation(ActionEvent event) {
        if (fromRegisteredID.isSelected()) {
            student_ID.clear();
            setStudentDefaultImage(studentImageView);
            student_ID.setVisible(false);
            hbox_studentID.setVisible(true);
        } else {
            student_ID.clear();
            setStudentDefaultImage(studentImageView);
            student_ID.setVisible(true);
            hbox_studentID.setVisible(false);
            refreshStudentIDandNamesCombo();
        }
    }

    @FXML
    private void student_chooseRegisteredNameOperation(ActionEvent event) {
        if (fromRegisteredNames.isSelected()) {
            student_name.clear();
            student_name.setVisible(false);
            hbox_studentName.setVisible(true);
        } else {
            student_name.clear();
            student_name.setVisible(true);
            hbox_studentName.setVisible(false);
            refreshStudentIDandNamesCombo(); 
        }       
        
    }

    @FXML
    private void student_chooseRegisteredDeskOperation(ActionEvent event) {
        if (fromRegisteredDesk.isSelected()) {
            student_deskNumber.clear();
            student_deskNumber.setVisible(false);
            hbox_desk.setVisible(true);
        } else {
            student_deskNumber.clear();
            student_deskNumber.setVisible(true);
            hbox_desk.setVisible(false);
            refreshDeskNumberCombo();
        }         
    }

    @FXML
    private void student_chooseChairNumberOperation(ActionEvent event) {
        if (fromRegisteredChairs.isSelected()) {
            student_chairNumber.clear();
            student_chairNumber.setVisible(false);
            hbox_chair.setVisible(true);
        } else {
            student_chairNumber.clear();
            student_chairNumber.setVisible(true);
            hbox_chair.setVisible(false);
            refreshChairNumberCombo();
        }         
        
    }

    @FXML
    private void student_searchFieldMouseClicked(MouseEvent event) {
        changeStudentAllocationTableOnSearch(studentAllocationData, student_searchField, studentTable);
    }

    @FXML
    private void refresfTableOperation(ActionEvent event) {
        refreshStudentAllocationTable();
    }

    @FXML
    private void student_allocateResourceOperation(ActionEvent event) {
        String studentID = null;
        String studentName = null;
        String deskNo = null;
        String chairNo = null;
        
        // #1
        if (fromRegisteredID.isSelected()) {
            if (student_registeredID.getSelectionModel().isEmpty()) {
                System.out.println("Choose registered id");
            } else {
                studentID = student_registeredID.getSelectionModel().getSelectedItem();
            }
            
        } else {
            studentID = student_ID.getText();
        }
        
        // #2
        if (fromRegisteredNames.isSelected()) {
            if (student_registeredNames.getSelectionModel().isEmpty()) {
                System.out.println("Choose registered names");
            } else {
                studentName = student_registeredNames.getSelectionModel().getSelectedItem();
            }
            
        } else {
            studentName = student_name.getText();
        }
        
        // #3
        if (fromRegisteredDesk.isSelected()) {
            if (student_registeredDeskNumber.getSelectionModel().isEmpty()) {
                System.out.println("Choose registered desk");
            } else {
                deskNo = student_registeredDeskNumber.getSelectionModel().getSelectedItem();
            }
            
        } else {
            deskNo = student_deskNumber.getText();
        }
        
        // #4
        if (fromRegisteredChairs.isSelected()) {
            if (student_rgisteredChairNumber.getSelectionModel().isEmpty()) {
                System.out.println("Choose registered chair");
            } else {
                chairNo = student_rgisteredChairNumber.getSelectionModel().getSelectedItem();
            }
            
        } else {
            chairNo = student_chairNumber.getText();        
        }
        
        if (studentID == null || studentName == null||deskNo == null ||chairNo == null ) {

                String msg = null;

                if (studentID == null) {
                    msg = "Student's ID is empty, you must enter it or choose from registered";
                } else if (studentName == null) {
                    msg = "Student's name is empty, you must enter it or choose from registered";
                } else if (deskNo == null ) {
                    msg = "Student's desk number is empty, you must enter it or choose from registered";
                }  else if (chairNo == null ) {
                    msg = "Student's chair number is empty, you must enter it or choose from registered";
                } 

                Alert errorIssue = new Alert(Alert.AlertType.ERROR);
                errorIssue.setTitle("Registration Failed");
                errorIssue.setGraphic(errorImage);
                errorIssue.setHeaderText(null);
                errorIssue.setContentText("The allocation has failed registration.\n"
                + msg);
                errorIssue.showAndWait();
        
        }else {
                           
            
                //show confirm alert before registering data of issued
                String ms = " Are you sure you want to allocate chair and desk to "+student_name.getText();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm registration");
                alert.setGraphic(pupilImageView);
                alert.setHeaderText(null);
                alert.setContentText(ms);

                Optional<ButtonType> response = alert.showAndWait();
                if (response.get()==ButtonType.OK) {
                    try {
                        String studentClass = retrieveStudentClass(studentID,studentName);
                        System.out.println(studentClass);
                        
                        String sql = "INSERT INTO deskchairissued "
                                + "(student_id,student_name,student_class,desk_no,chair_no)"
                        + " VALUES(?,?,?,?,?)";
                        pstmt= con.prepareStatement(sql);
                        
                        pstmt.setString(1, studentID);
                        pstmt.setString(2, studentName);                        
                        pstmt.setString(3, studentClass);
                        pstmt.setString(4, deskNo);
                        pstmt.setString(5, chairNo);
                                                  
                        int i = pstmt.executeUpdate(); // load data into the database
                        if (i>0) {
                          String ok = student_name.getText() +" has been successfully allocated a desk and chair.";
                            successNotification(Pos.BOTTOM_RIGHT, ok);
                            
                            clearStudentFields();
                            refreshStudentAllocationTable();

                        } else {
                            errorNotification(Pos.BOTTOM_RIGHT, "OOPS! Could not allocate resources due  to internal error.");
                       
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
    
    private String retrieveStudentClass(String id, String name){
        String result = null;
        
                try {                    
                    String sqlString = "SELECT * FROM student WHERE id_no = ? AND name = ? ";
                    pstmt = con.prepareStatement(sqlString);
                    pstmt.setString(1, id);
                    pstmt.setString(2, name);
                    ResultSet rss=pstmt.executeQuery();
                    
                    if (rss.next()) {
                        result = rss.getString("class");
                    }                    
                } catch (SQLException ev) {
                    
                } 
                
        if (result != null) {
            return result;
        } else {
            return null;
        }
    
    }
    
    private List<String> retrieveStudentNameImage(String id){
        List<String> resultsList = new ArrayList<>();
        
                try {                    
                    String sqlString = "SELECT * FROM student WHERE id_no = ?";
                    pstmt = con.prepareStatement(sqlString);
                    pstmt.setString(1, id);
                    ResultSet rss=pstmt.executeQuery();
                    
                    if (rss.next()) {
                        resultsList.add(rss.getString("name"));
                        resultsList.add(rss.getString("image"));
                    }                   
                } catch (SQLException ev) {
                    
                } 
                
        if (resultsList != null) {
            return resultsList;
        } else {
            return null;
        }
    
    }

    @FXML
    private void student_updateResourceOperation(ActionEvent event) {
        
        if (studentIDHolder <= 0) {
            errorNotification(Pos.BOTTOM_RIGHT, "You must retrieve allocation record to be updated");
        } else {


                String studentID = null;
                String studentName = null;
                String deskNo = null;
                String chairNo = null;

                // #1
                if (fromRegisteredID.isSelected()) {
                    if (student_registeredID.getSelectionModel().isEmpty()) {
                        System.out.println("Choose registered id");
                    } else {
                        studentID = student_registeredID.getSelectionModel().getSelectedItem();
                    }

                } else {
                    studentID = student_ID.getText();
                }

                // #2
                if (fromRegisteredNames.isSelected()) {
                    if (student_registeredNames.getSelectionModel().isEmpty()) {
                        System.out.println("Choose registered names");
                    } else {
                        studentName = student_registeredNames.getSelectionModel().getSelectedItem();
                    }

                } else {
                    studentName = student_name.getText();
                }

                // #3
                if (fromRegisteredDesk.isSelected()) {
                    if (student_registeredDeskNumber.getSelectionModel().isEmpty()) {
                        System.out.println("Choose registered desk");
                    } else {
                        deskNo = student_registeredDeskNumber.getSelectionModel().getSelectedItem();
                    }

                } else {
                    deskNo = student_deskNumber.getText();
                }

                // #4
                if (fromRegisteredChairs.isSelected()) {
                    if (student_rgisteredChairNumber.getSelectionModel().isEmpty()) {
                        System.out.println("Choose registered chair");
                    } else {
                        chairNo = student_rgisteredChairNumber.getSelectionModel().getSelectedItem();
                    }

                } else {
                    chairNo = student_chairNumber.getText();        
                }

                if (studentID == null || studentName == null||deskNo == null ||chairNo == null ) {

                        String msg = null;

                        if (studentID == null) {
                            msg = "Student's ID is empty, you must enter it or choose from registered";
                        } else if (studentName == null) {
                            msg = "Student's name is empty, you must enter it or choose from registered";
                        } else if (deskNo == null ) {
                            msg = "Student's desk number is empty, you must enter it or choose from registered";
                        }  else if (chairNo == null ) {
                            msg = "Student's chair number is empty, you must enter it or choose from registered";
                        } 

                        Alert errorIssue = new Alert(Alert.AlertType.ERROR);
                        errorIssue.setTitle("uPDATE Failed");
                        errorIssue.setGraphic(errorImage);
                        errorIssue.setHeaderText(null);
                        errorIssue.setContentText("The allocation has failed update.\n"
                        + msg);
                        errorIssue.showAndWait();

                }else {


                        //show confirm alert before registering data of issued
                        String ms = " Are you sure you want to update allocation to "+student_name.getText();
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirm update");
                        alert.setGraphic(pupilImageView);
                        alert.setHeaderText(null);
                        alert.setContentText(ms);

                        Optional<ButtonType> response = alert.showAndWait();
                        if (response.get()==ButtonType.OK) {
                            try {
                                String studentClass = retrieveStudentClass(studentID,studentName);

                                String sql = "UPDATE deskchairissued SET "
                                        + "student_id = ?,student_name = ?,student_class = ?,desk_no = ?,chair_no = ?"
                                + " WHERE id = '"+studentIDHolder+"' ";
                                pstmt= con.prepareStatement(sql);

                                pstmt.setString(1, studentID);
                                pstmt.setString(2, studentName);                        
                                pstmt.setString(3, studentClass);
                                pstmt.setString(4, deskNo);
                                pstmt.setString(5, chairNo);

                                int i = pstmt.executeUpdate(); // load data into the database
                                if (i>0) {
                                  String ok = student_name.getText() +"'s allocation has been successfully updated.";
                                    successNotification(Pos.BOTTOM_RIGHT, ok);

                                    clearStudentFields();
                                    refreshStudentAllocationTable();

                                } else {
                                    errorNotification(Pos.BOTTOM_RIGHT, "OOPS! Could not update due  to internal error.");

                                }
                                pstmt.close();

                            } catch (Exception e) {
                                errorNotification(Pos.BOTTOM_RIGHT, "OOPS! Could not update due  to internal error.");
                            }finally {
                            try {
                            rs.close();
                            pstmt.close();
                            } catch (Exception e) {}
                            }
                        }   
                }         
        } 
    }

    @FXML
    private void student_deleteResourceOperation(ActionEvent event) {
        
          if (studentIDHolder <=0) {
            
            Alert errorIssue = new Alert(Alert.AlertType.ERROR);
            errorIssue.setTitle("Delete Failed");
            errorIssue.setGraphic(errorImage);
            errorIssue.setHeaderText(null);
            errorIssue.setContentText("Ensure the registered allocation to be deleted has been retrieved.");
            errorIssue.showAndWait(); 
            
        }else {
              Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
              alert.setTitle("Delete");
              alert.setGraphic(pupilImageView);
              alert.setHeaderText(null);
              alert.setContentText("Are you sure you want to delete this student's allocation?");
              
              Optional <ButtonType> obt = alert.showAndWait();
              
              if (obt.get()== ButtonType.OK) {
              try {
                String query = "DELETE FROM deskchairissued WHERE id = ? ";
                pstmt = con.prepareStatement(query);
                pstmt.setInt(1, studentIDHolder);
               int i = pstmt.executeUpdate();
              
                if (i>0) {
                    successNotification(Pos.BOTTOM_RIGHT, "The student's allocation has been deleted.");
                }
              
              pstmt.close(); 
              
              clearStudentFields();
              refreshStudentAllocationTable();
              
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
    private void student_clearFieldsOperation(ActionEvent event) {
        clearStudentFields();
    }
    
    @FXML
    private void student_refreshStudentIDCombobox(ActionEvent event) {
        refreshStudentIDandNamesCombo();
    }

    @FXML
    private void student_refreshStudentNameCombobox(ActionEvent event) {
        refreshStudentIDandNamesCombo();
    }

    @FXML
    private void student_refreshDeskNumberCombobox(ActionEvent event) {
        refreshDeskNumberCombo();
    }

    @FXML
    private void student_refreshChairNumberCombobox(ActionEvent event) {
        refreshChairNumberCombo();
    } 
    
    @FXML
    private void printAllocationToSpecificClassOperation(ActionEvent event) {

        if (printClassName.getSelectionModel().isEmpty()) {             
            
            String msg = "Class is empty, you must choose a class from the drop-down menu to generate report";           
             
            Alert errorIssue = new Alert(Alert.AlertType.ERROR);
            errorIssue.setTitle("FAILED");
            errorIssue.setGraphic(errorImage);
            errorIssue.setHeaderText(null);
            errorIssue.setContentText("Report generation for printing failed.\n"
                        + msg);
            errorIssue.showAndWait(); 
            
        }else {
            String className = printClassName.getSelectionModel().getSelectedItem();
            
            // generate pdf
                 pdfFileChooserOpener();
                   pdfFileChooser.setTitle("Save printed report");
                   //single File selection
                   pdfFile = pdfFileChooser.showSaveDialog(Stage);                   
                    if (pdfFile != null) {
                        String path = pdfFile.getAbsolutePath();
                        
                        createClassListForDeskChairPDF(path,className);
                        refreshStudentClassCombo();
                    }        
        }         
        
    }
    
    //method to create pdf of a specific date when stock was stored
    public void createClassListForDeskChairPDF(String filename,String className){
        
       // create a pdf
       Document document = new Document(PageSize.A4,5,5,20,40);
       
        try {
            
             String sq = "SELECT * FROM deskchairissued WHERE student_class = ? ";
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
                    + "Allocation for "+ className +" as at "+ LocalDate.now()+" \n\r",bfBold12);
            intro.setAlignment(Element.ALIGN_CENTER);            
            document.add(intro);
            
            
            
            // Add a table
            float[] columnWidths = {2f,3f,1.2f,1.2f,3f};
            PdfPTable studentTable = new PdfPTable(columnWidths);
            studentTable.setWidthPercentage(90f);
            
            insertCell(studentTable, "Student ID", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(studentTable, "Student Name", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(studentTable, "Desk No.", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(studentTable, "Chair No.", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(studentTable, "Date issued", Element.ALIGN_LEFT, 1, bfBold12);
            
            while (rs.next()) {
                String code = rs.getString("student_id");
                insertCell(studentTable, code, Element.ALIGN_LEFT, 1, bfBold);
                
                String name = rs.getString("student_name");
                insertCell(studentTable, name, Element.ALIGN_LEFT, 1, bfBold); 
                 
                String desk = rs.getString("desk_no"); 
                insertCell(studentTable, desk, Element.ALIGN_LEFT, 1, bfBold); 
                
                String chair = rs.getString("chair_no");
                insertCell(studentTable, chair, Element.ALIGN_LEFT, 1, bfBold); 
                
                String date = rs.getString("date_reg");
                insertCell(studentTable, date, Element.ALIGN_LEFT, 1, bfBold);
                         
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

    @FXML
    private void studentTableMouseClicked(MouseEvent event) {
        
               try {
                    StudentAllocation sa = studentTable.getSelectionModel().getSelectedItem();
                    
                    String sql = "SELECT * FROM deskchairissued WHERE student_id = ? AND student_name = ? AND student_class = ?";
                    pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, sa.getStudentID());
                    pstmt.setString(2, sa.getStudentName());
                    pstmt.setString(3, sa.getStudentClass());
                    
                    rs=pstmt.executeQuery();
                    
                    if (rs.next()) {
                        
                        studentIDHolder = rs.getInt("id");
                        
                        String add1 = rs.getString("student_id");
                        student_ID.setText(add1); 
                        String add2 = rs.getString("student_name");
                        student_name.setText(add2);
                        String add3 = rs.getString("desk_no");
                        student_deskNumber.setText(add3);
                        String add4 = rs.getString("chair_no");
                        student_chairNumber.setText(add4);
                        
                            List<String> results = retrieveStudentNameImage(add1);

                            if (results.isEmpty()) {
                                System.out.println("Empty results");
                            } else {
                                String imagePath = null;

                                String retrievedPath = results.get(1);
                                if (retrievedPath == null) {
                                    setStudentDefaultImage(studentImageView);
                                } else {
                                    imagePath = retrievedPath;
                                }

                                try {
                                    image = new Image(new FileInputStream(imagePath),studentImageView.getFitWidth(),studentImageView.getFitHeight(),false,true);
                                    studentImageView.setImage(image);                  
                                } catch (FileNotFoundException ex) {
                                    Logger.getLogger(StudentsController.class.getName()).log(Level.SEVERE, null, ex);
                                }
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
    private void studentTableKeyRealesed(KeyEvent event) {
        
            studentTable.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.DOWN) {
                
                try {
                    StudentAllocation sa = studentTable.getSelectionModel().getSelectedItem();
                    
                    String sql = "SELECT * FROM deskchairissued WHERE student_id = ? AND student_name = ? AND student_class = ?";
                    pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, sa.getStudentID());
                    pstmt.setString(2, sa.getStudentName());
                    pstmt.setString(3, sa.getStudentClass());
                    
                    rs=pstmt.executeQuery();
                    
                    if (rs.next()) {
                        
                        studentIDHolder = rs.getInt("id");
                        
                        String add1 = rs.getString("student_id");
                        student_ID.setText(add1); 
                        String add2 = rs.getString("student_name");
                        student_name.setText(add2);
                        String add3 = rs.getString("desk_no");
                        student_deskNumber.setText(add3);
                        String add4 = rs.getString("chair_no");
                        student_chairNumber.setText(add4);
                        
                            List<String> results = retrieveStudentNameImage(add1);

                            if (results.isEmpty()) {
                                System.out.println("Empty results");
                            } else {
                                String imagePath = null;

                                String retrievedPath = results.get(1);
                                if (retrievedPath == null) {
                                    setStudentDefaultImage(studentImageView);
                                } else {
                                    imagePath = retrievedPath;
                                }

                                try {
                                    image = new Image(new FileInputStream(imagePath),studentImageView.getFitWidth(),studentImageView.getFitHeight(),false,true);
                                    studentImageView.setImage(image);                  
                                } catch (FileNotFoundException ex) {
                                    Logger.getLogger(StudentsController.class.getName()).log(Level.SEVERE, null, ex);
                                }
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
    private void student_getNameImageOperation(ActionEvent event) {
        
        //remove any image present in student imageview
        setStudentDefaultImage(studentImageView);
        
        String id = student_registeredID.getSelectionModel().getSelectedItem();
        
        List<String> results = retrieveStudentNameImage(id);
        
        if (results.isEmpty()) {
            System.out.println("Empty results");
        } else {
            String name = results.get(0);
            student_name.setText(name);
            
            String imagePath = null;
            
            String retrievedPath = results.get(1);
            if (retrievedPath.equalsIgnoreCase("Null")) {
                imagePath = "C:\\A_Check\\Store_photos\\student.png";
            } else {
                imagePath = retrievedPath;
            }
            
            try {
                image = new Image(new FileInputStream(imagePath),studentImageView.getFitWidth(),studentImageView.getFitHeight(),false,true);
                studentImageView.setImage(image);                  
            } catch (FileNotFoundException ex) {
                Logger.getLogger(StudentsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }    
    
    private void clearStudentFields(){

        student_ID.clear();
        student_name.clear();
        student_chairNumber.clear();
        student_deskNumber.clear();
        refreshStudentIDandNamesCombo();
        refreshChairNumberCombo();
        refreshDeskNumberCombo();
        fromRegisteredChairs.setSelected(false);
        fromRegisteredDesk.setSelected(false);
        fromRegisteredID.setSelected(false);
        fromRegisteredNames.setSelected(false);
        hbox_studentID.setVisible(false);
        hbox_studentName.setVisible(false);
        hbox_desk.setVisible(false);
        hbox_chair.setVisible(false);
        student_ID.setVisible(true);
        student_name.setVisible(true);
        student_chairNumber.setVisible(true);
        student_deskNumber.setVisible(true);
        setStudentDefaultImage(studentImageView);
        
        
    }    
    
   private void updateStudentIDandNamesCombo(){  
        try { 
            String sql = "SELECT * FROM student ";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next()){                
                studentIdData.add( rs.getString("id_no")); 
                studentNamesData.add( rs.getString("name")); 
            }
            // populate class combo box 
            student_registeredID.setItems(studentIdData);
            student_registeredNames.setItems(studentNamesData); 
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
   
   private void refreshStudentIDandNamesCombo(){   
       studentIdData.clear();
       studentNamesData.clear();
       updateStudentIDandNamesCombo();
   }
   
   private void updateDeskNumberCombo(){  
        try { 
            String sql = "SELECT desk_no FROM desk";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next()){                
                deskNoData.add( rs.getString("desk_no")); 
            }
            // populate class combo box 
            student_registeredDeskNumber.setItems(deskNoData);
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
   
   private void refreshDeskNumberCombo(){   
       deskNoData.clear();
       updateDeskNumberCombo();
   } 
   
   private void updateChairNumberCombo(){  
        try { 
            String sql = "SELECT chair_no FROM chair";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next()){                
                chairNoData.add( rs.getString("chair_no")); 
            }
            // populate class combo box 
            student_rgisteredChairNumber.setItems(chairNoData);
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
   
   private void refreshChairNumberCombo(){   
       chairNoData.clear();
       updateChairNumberCombo();
   }
   
   private void updateStudentClassNamesCombo(){  
        try { 
            String sql = "SELECT className FROM class";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next()){                
                classNameData.add( rs.getString("className")); 
                
            }
            // populate class combo box 
            printClassName.setItems(classNameData);
            roomAllocation_receipient.setItems(classNameData);
            roomAllocation_returnedFrom.setItems(classNameData);
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
       classNameData.clear();
       updateStudentClassNamesCombo();
   } 
   
    private void updateStudentAllocationTable(){
        
        studentColumn_ID.setCellValueFactory(new PropertyValueFactory<>("studentID"));
        studentColumn_name.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        studentColumn_class.setCellValueFactory(new PropertyValueFactory<>("studentClass"));
        studentColumn_desk.setCellValueFactory(new PropertyValueFactory<>("deskNo"));
        studentColumn_chair.setCellValueFactory(new PropertyValueFactory<>("chairNo"));
        studentColumn_dateIssued.setCellValueFactory(new PropertyValueFactory<>("dateReg"));

        try { 
            String sql = "SELECT * FROM deskchairissued ";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next()){
                
                studentAllocationData.add( new StudentAllocation(
                        rs.getString("student_id"),
                        rs.getString("student_name"),
                        rs.getString("student_class"),
                        rs.getString("desk_no"),
                        rs.getString("chair_no"),
                        rs.getString("date_reg")
                ));         
            }
            //load items to the table
            studentTable.setItems(studentAllocationData);
            studentTable.setPlaceholder(new Label("No allocation matches the details provided", errorImage));
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
    
    private void refreshStudentAllocationTable(){    
        studentAllocationData.clear();
        updateStudentAllocationTable();
    }

    @FXML
    private void openStockPurchasedWindow(ActionEvent event) throws IOException {
        loadWindow("/daviscahtech_assetmanagement/FXML/stockdetails.fxml", "Stocked/Purcahsed units");
    }

    @FXML
    private void openissuedUnitsWindow(ActionEvent event) throws IOException {
        loadWindow("/daviscahtech_assetmanagement/FXML/issuedstockdetails.fxml", "Issued stock details");
    }

    @FXML
    private void openSchoolClassesWindow(ActionEvent event) throws IOException {
        loadWindow("/daviscahtech_assetmanagement/FXML/schoolClasses.fxml", "Configure school classes and rooms for resources");        
    }
    
    @FXML
    private void openRoomMaterialWindow(ActionEvent event) throws IOException {
        loadWindow("/daviscahtech_assetmanagement/FXML/roomMaterial.fxml", "Configure resources to be allocated to rooms/classes");        
    } 
    
    @FXML
    private void openReturnedUnitsWindow(ActionEvent event) throws IOException {
        loadWindow("/daviscahtech_assetmanagement/FXML/returnedStock.fxml", "View all returned units/material to store");        
    }
    
    @FXML
    private void openAdminPanelWindow(ActionEvent event) throws IOException {
       loadWindow("/daviscahtech_assetmanagement/FXML/adminLogin.fxml", "Adminstrator Login");        

    }    
//############################## room allocation detail section #####################

    private void updateRoomAllocationTable(){
        populateAnyTable(roomColumn_code, roomColumn_name, roomColumn_quantity,
                roomColumn_department, roomColumn_cost, roomColumn_alart, roomAllocationData,
                roomAllocationTable, "Room resources");
       
    }
    
    private void refreshRoomAllocationTable(){    
        roomAllocationData.clear();
        updateRoomAllocationTable();
    } 
    
    private void issueRoomResourceStock(TextField qnty,TextField code,TextField name, ComboBox receipient,
            TextArea desc,DatePicker date,String department){
    
    
        if (qnty.getText().isEmpty()
                ||code.getText().isEmpty()
                || date.getEditor().getText().isEmpty()
                ||desc.getText().isEmpty()
                ||name.getText().isEmpty()
                ||receipient.getSelectionModel().isEmpty()) {

                String msg = null;

                if (code.getText().isEmpty()) {
                    msg = "Products' code is empty, you must retrieve the products to be issued";
                } else if (qnty.getText().isEmpty()) {
                    msg = "Quantity of stocked units to be issued is empty, you must enter it";
                } else if (date.getEditor().getText().isEmpty()) {
                    msg = "Date when units were issued is empty, you must choose it";
                } else if (desc.getText().isEmpty()) {
                     msg = "Products' description is empty, you must retrieve the products to be issued";
                } else if (name.getText().isEmpty()) {
                    msg = "Products' name is empty, you must retrieve the products to be issued";
                } else if (receipient.getSelectionModel().isEmpty()) {
                    msg = "Room or class to be allocated items is empty, you must enter it";
                } 

                Alert errorIssue = new Alert(Alert.AlertType.ERROR);
                errorIssue.setTitle("Error");
                errorIssue.setGraphic(errorImage);
                errorIssue.setHeaderText(null);
                errorIssue.setContentText("The could not be issued.\n"
                + msg);
                errorIssue.showAndWait();
        
        }else {
            
            if (isInt(qnty)) {
                
                int unitsToIsue = Integer.parseInt(qnty.getText().trim());
                
                if (unitsToIsue > stockTableQuantityAvailable) {
                    errorNotification(Pos.BOTTOM_RIGHT, "You can not issue more units than available in the store.\n "
                            + "Available units of product "+ name.getText()+ " is "+ stockTableQuantityAvailable);
                } else {

                    //show confirm alert before registering data of issued
                    String ms = " Are you sure you want to allocate resources = "+name.getText() +
                            " to "+ receipient.getSelectionModel().getSelectedItem();
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirm allocation");
                    alert.setGraphic(stockedproductImageView);
                    alert.setHeaderText(null);
                    alert.setContentText(ms);

                    Optional<ButtonType> response = alert.showAndWait();
                    if (response.get()==ButtonType.OK) {
                        try {
                            String sql = "INSERT INTO stockout ("
                            + "product_code,product_name,product_desc,units_out,department,given_to,date_given)"
                            + " VALUES(?,?,?,?,?,?,?)";
                            pstmt= con.prepareStatement(sql);
                             
                            pstmt.setString(1, code.getText());                                      
                            pstmt.setString(2, name.getText());
                            pstmt.setString(3, desc.getText());
                            pstmt.setInt(4, Integer.parseInt(qnty.getText().trim()));                        
                            pstmt.setString(5, department);
                            pstmt.setString(6, receipient.getSelectionModel().getSelectedItem().toString());
                            pstmt.setString(7, date.getEditor().getText());

                            int i = pstmt.executeUpdate(); // load data into the database
                            if (i>0) {
                                    
                                    //reduce the total units and update
                                    int remaining = stockTableQuantityAvailable - unitsToIsue;
                                
                                    // save the product details to the general stock table
                                    try {
                                        String sqlString = "UPDATE stock SET quantity_avail = ?"
                                                + " WHERE id = '"+stockTablePrimaryID+"' AND product_pk_fk = '"+stockTableForeignID+"' ";
                                        pstmt= con.prepareStatement(sqlString);
                                        pstmt.setInt(1, remaining);
                                        int ii = pstmt.executeUpdate(); // load data into the database 

                                        if (ii>0) {
                                            String sc = "Stock issue and other changes have successfully been effected in the system.";
                                            successNotification(Pos.BOTTOM_RIGHT, sc);

                                            clearFields();
                                            refreshTeachingTable();
                                            refreshBordingTable();
                                            refreshGamesTable();
                                            refreshCateringTable();
                                            refreshSurbodinateTable();
                                            refreshSecurityTable();
                                            refreshproductionTable();
                                            refreshEntertainmentTable();
                                            refreshTransportTable();
                                            refreshStudentSectorTable();
                                            refreshRoomAllocationTable();
                                        }

                                        } catch (Exception e) {
                                    }
                            } else {
                                String er = "OOPS! Could not issue product due  to internal error";
                                errorNotification(Pos.BOTTOM_RIGHT, er);
                            }
                            pstmt.close();

                        } catch (SQLException | NumberFormatException e) {
                            String er = "OOPS! Could not issue product due  to internal error";
                            errorNotification(Pos.BOTTOM_RIGHT, er);
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
        }        
    } 

    private void returnRoomResourceStock(TextField qnty,TextField code,TextField name, ComboBox returnedBy,
            TextArea desc,DatePicker date,ComboBox type,String department){
        
        if (qnty.getText().isEmpty()
                ||code.getText().isEmpty()
                || date.getEditor().getText().isEmpty()
                ||desc.getText().isEmpty()
                ||returnedBy.getSelectionModel().isEmpty()
                ||type.getSelectionModel().isEmpty()
                ||name.getText().isEmpty()) {

                String msg = null;

                if (code.getText().isEmpty()) {
                    msg = "Products' code is empty, you must retrieve the products to be issued";
                } else if (qnty.getText().isEmpty()) {
                    msg = "Quantity of units to be returned is empty, you must enter it";
                } else if (date.getEditor().getText().isEmpty()) {
                    msg = "Date when units were returned is empty, you must choose it";
                } else if (desc.getText().isEmpty()) {
                     msg = "Products' description is empty, you must retrieve the products to be issued";
                } else if (name.getText().isEmpty()) {
                    msg = "Products' name is empty, you must retrieve the products to be issued";
                } else if (returnedBy.getSelectionModel().isEmpty()) {
                    msg = "Name of room or clas that material was returned from is empty, you must enter it";
                } else if (type.getSelectionModel().isEmpty()) {
                    msg = "The quantity nature/size of the units returned is empty, you must select from drop-down menu";
                } 

                Alert errorIssue = new Alert(Alert.AlertType.ERROR);
                errorIssue.setTitle("Error");
                errorIssue.setGraphic(errorImage);
                errorIssue.setHeaderText(null);
                errorIssue.setContentText("The material could not be return.\n"
                + msg);
                errorIssue.showAndWait();
        
        }else {
            
            if (isInt(qnty)) {
                
                    //show confirm alert before registering data of issued
                    String ms = " Are you sure you want to receive returned materials, "+name.getText() +
                            " from "+ returnedBy.getSelectionModel().getSelectedItem().toString()+".\n "
                            + "Receiving returned stock/material will increase the total stocked level of this product.";
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirm");
                    alert.setGraphic(stockedproductImageView);
                    alert.setHeaderText(null);
                    alert.setContentText(ms);

                    Optional<ButtonType> response = alert.showAndWait();
                    if (response.get()==ButtonType.OK) {
                        try {
                            String sql = "INSERT INTO stockreturn ("
                            + "product_code,product_name,product_desc,department,units_in,type,returnedby,date_returned)"
                            + " VALUES(?,?,?,?,?,?,?,?)";
                            pstmt= con.prepareStatement(sql);
                             
                            pstmt.setString(1, code.getText());                                      
                            pstmt.setString(2, name.getText());
                            pstmt.setString(3, desc.getText());
                            pstmt.setString(4, department);
                            pstmt.setInt(5, Integer.parseInt(qnty.getText().trim()));                     
                            pstmt.setString(6, type.getSelectionModel().getSelectedItem().toString());
                            pstmt.setString(7, returnedBy.getSelectionModel().getSelectedItem().toString());
                            pstmt.setString(8, date.getEditor().getText());

                            int i = pstmt.executeUpdate(); // load data into the database
                            if (i>0) {
                                    int unitsToReturn = Integer.parseInt(qnty.getText().trim());
                                    //reduce the total units and update
                                    int remaining = stockTableQuantityAvailable + unitsToReturn;
                                
                                    // save the product details to the general stock table
                                    try {
                                        String sqlString = "UPDATE stock SET quantity_avail = ?"
                                                + " WHERE id = '"+stockTablePrimaryID+"' AND product_pk_fk = '"+stockTableForeignID+"' ";
                                        pstmt= con.prepareStatement(sqlString);
                                        pstmt.setInt(1, remaining);
                                        int ii = pstmt.executeUpdate(); // load data into the database 

                                        if (ii>0) {
                                            String sc = "Stock/material return and other changes have successfully been effected in the system.";
                                            successNotification(Pos.BOTTOM_RIGHT, sc);

                                            clearFields();
                                            refreshTeachingTable();
                                            refreshBordingTable();
                                            refreshGamesTable();
                                            refreshCateringTable();
                                            refreshSurbodinateTable();
                                            refreshSecurityTable();
                                            refreshproductionTable();
                                            refreshEntertainmentTable();
                                            refreshTransportTable();
                                            refreshStudentSectorTable();
                                            refreshRoomAllocationTable();
                                        }

                                        } catch (Exception e) {
                                    }


                            } else {
                                String er = "OOPS! Could not return product due  to internal error";
                                errorNotification(Pos.BOTTOM_RIGHT, er);
                            }
                            pstmt.close();

                        } catch (SQLException | NumberFormatException e) {
                            String er = "OOPS! Could not return product due  to internal error";
                            errorNotification(Pos.BOTTOM_RIGHT, er);
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
    
    }    
    
    
    @FXML
    private void roomAllocation_issueOperation(ActionEvent event) {
            issueRoomResourceStock(roomAllocation_quantity, roomAllocation_code, roomAllocation_name,
                roomAllocation_receipient, roomAllocation_desc, roomAllocation_dateIssued, "Room resources");        
    }

    @FXML
    private void roomAllocation_clearIssueFields(ActionEvent event) {
        clearFields();
    }

    @FXML
    private void roomAllocation_returnOperation(ActionEvent event) {
                returnRoomResourceStock(roomAllocation_returnedUnits, roomAllocation_code, roomAllocation_name,
                roomAllocation_returnedFrom, roomAllocation_desc, roomAllocation_dateReturned,
                roomAllocation_productType, "Room resources"); 
    }

    @FXML
    private void roomAllocation_clearReturnFields(ActionEvent event) {
        clearFields();
    }

    @FXML
    private void roomAllocation_searchFieldOperation(MouseEvent event) {
        changeAnyTableOnSearch(roomAllocationData, roomAllocation_searchField, roomAllocationTable);
    }

    @FXML
    private void roomColumn__refreshTableOperation(ActionEvent event) {
        refreshRoomAllocationTable();
    }

    @FXML
    private void roomAllocationTableMouseClicked(MouseEvent event) {
        mouseClickedOnAnyTable(roomAllocationTable, roomAllocation_code, roomAllocation_name, roomAllocation_desc, roomsAllocationImageView);
    }

    @FXML
    private void roomAllocationTableKeyReleased(KeyEvent event) {
        keyReleasedOnAnyTable(roomAllocationTable, roomAllocation_code, roomAllocation_name, roomAllocation_desc, roomsAllocationImageView);
    }

    @FXML
    private void roomAllocationRefreshRooms(ActionEvent event) {
        refreshStudentClassCombo();
    }

    @FXML
    private void openPersonalAccountWindow(ActionEvent event) throws IOException {
        loadWindow("/daviscahtech_assetmanagement/FXML/personalAccount.fxml", "Configure Logging setting");
    }

    @FXML
    private void closeApplicationOperation(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void openDaviscahTechWindow(ActionEvent event) throws IOException {
        loadWindow("/daviscahtech_assetmanagement/FXML/daviscahtech.fxml", "About this software developers");
    }








    

    
    
    
    
}// end of class
