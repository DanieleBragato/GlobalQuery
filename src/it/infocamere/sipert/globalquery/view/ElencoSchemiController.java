/**
 * Sample Skeleton for 'ElencoSchemi.fxml' Controller Class
 */

package it.infocamere.sipert.globalquery.view;

import java.net.URL;
import java.util.ResourceBundle;

import it.infocamere.sipert.globalquery.MainApp;
import it.infocamere.sipert.globalquery.model.Model;
import it.infocamere.sipert.globalquery.model.Schema;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ElencoSchemiController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="schemiTable"
    private TableView<Schema> schemiTable; // Value injected by FXMLLoader

    @FXML // fx:id="codSchemaColumn"
    private TableColumn<Schema, String> codSchemaColumn; // Value injected by FXMLLoader

    @FXML // fx:id="descrSchemaColumn"
    private TableColumn<Schema, String> descrSchemaColumn; // Value injected by FXMLLoader

    @FXML // fx:id="filterField"
    private TextField filterField; // Value injected by FXMLLoader
    
	private ObservableList<Schema> masterData = FXCollections.observableArrayList();

	private Model model ;
	private Stage dialogStage;
    private MainApp mainApp;
	
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

	/**
	 *  
	 */
	public ElencoSchemiController() {
		
		masterData.add(new Schema("XYZFC4", "Camera di commercio di Milano"));
		masterData.add(new Schema("W3S7B1", "Camera di commercio di Torino"));
		masterData.add(new Schema("TR4DSW", "Camera di commercio di Firenze"));
		
		masterData.add(new Schema("dasdasXYZFC4", "Camera di commercio di Roma"));
		masterData.add(new Schema("W3S7Bfsdf1", "Camera di commercio di Padova"));
		masterData.add(new Schema("TR4DScdW", "Camera di commercio di Modena"));
		
		masterData.add(new Schema("XYZfsdfsdfFC4", "Camera di commercio di Bologna"));

	}
	
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
		// 0. Initialize the columns.
    	codSchemaColumn.setCellValueFactory(cellData -> cellData.getValue().codiceSchemaProperty());
    	descrSchemaColumn.setCellValueFactory(cellData -> cellData.getValue().descrizioneSchemaProperty());
		
		// 1. Wrap the ObservableList in a FilteredList (initially display all data).
		FilteredList<Schema> filteredData = new FilteredList<>(masterData, p -> true);
		
		// 2. Set the filter Predicate whenever the filter changes.
		filterField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(schema -> {
				// If filter text is empty, display all schema.
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				
				// Compare first name and last name of every schema with filter text.
				String lowerCaseFilter = newValue.toLowerCase();
				
				if (schema.getCodiceSchema().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true; // Filter matches first name.
				} else if (schema.getDescrizioneSchema().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true; // Filter matches last name.
				}
				return false; // Does not match.
			});
		});
		
		// 3. Wrap the FilteredList in a SortedList. 
		SortedList<Schema> sortedData = new SortedList<>(filteredData);
		
		// 4. Bind the SortedList comparator to the TableView comparator.
		// 	  Otherwise, sorting the TableView would have no effect.
		sortedData.comparatorProperty().bind(schemiTable.comparatorProperty());
		
		// 5. Add sorted (and filtered) data to the table.
		schemiTable.setItems(sortedData);

    }
    
	public void setModel(Model model) {
		this.model = model;
	}
	    
}
