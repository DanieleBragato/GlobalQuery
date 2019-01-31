/**
 * Sample Skeleton for 'MainPage.fxml' Controller Class
 */

package it.infocamere.sipert.globalquery;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.infocamere.sipert.globalquery.db.QueryDB;
import it.infocamere.sipert.globalquery.db.dto.GenericResultsDTO;
import it.infocamere.sipert.globalquery.db.dto.SchemaDTO;
import it.infocamere.sipert.globalquery.exception.ErroreColonneFileXlsSchemiKo;
import it.infocamere.sipert.globalquery.exception.ErroreFileSchemiNonTrovato;
import it.infocamere.sipert.globalquery.model.Model;
import it.infocamere.sipert.globalquery.util.FileExcelCreator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class MainPageController {
	
	private Model model ;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtAreaQuery"
    private TextArea txtAreaQuery; // Value injected by FXMLLoader

    @FXML // fx:id="btnLoadConnectios"
    private Button btnLoadConnectios; // Value injected by FXMLLoader
    
    @FXML // fx:id="bntTestConnections"
    private Button bntTestConnections; // Value injected by FXMLLoader
    
    @FXML // fx:id="btnRunQuery"
    private Button btnRunQuery; // Value injected by FXMLLoader

    @FXML // fx:id="btnResetQuery"
    private Button btnResetQuery; // Value injected by FXMLLoader

    @FXML // fx:id="hboxEsiti"
    private HBox hboxEsiti; // Value injected by FXMLLoader

    @FXML // fx:id="txtFieldEsito"
    private TextField txtFieldEsito; // Value injected by FXMLLoader

    @FXML
    void doLoadConnections(ActionEvent event) {
    	
    	List<SchemaDTO> listSchemi;
    	

    	
//		try {
//			listSchemi = model.getSchemi();
//	    	if (listSchemi.size() > 0) {
//	    		txtFieldEsito.setText("Trovati " + listSchemi.size() + " schemi");	
//	    	} else {
//	    		txtFieldEsito.setText("Nessun Schema Trovato");
//	    	}
//	    	hboxEsiti.setVisible(true);	
//		} catch (ErroreFileSchemiNonTrovato e) {
//			
//			Alert alert = new Alert(AlertType.ERROR);
//			alert.setTitle("Error");
//			alert.setContentText("Ooops, file excel degli schemi non trovato!");
//			alert.showAndWait();
//		}
//		catch (ErroreColonneFileXlsSchemiKo e) {
//
//			Alert alert = new Alert(AlertType.ERROR);
//			alert.setTitle("Error");
//			alert.setContentText("Ooops, file excel degli schemi con colonne errate!");
//			alert.showAndWait();
//		}
    }
    
    @FXML
    void doResetQuery(ActionEvent event) {
    	txtAreaQuery.setText("");
    	hboxEsiti.setVisible(false);
    }

    @FXML
    void doRunQuery(ActionEvent event) {
    	
    	QueryDB queryDB = new QueryDB();
    	
//    	if (txtAreaQuery.getText().length() > 0) {
//    		queryDB.setQuery(txtAreaQuery.getText());
//    		queryDB.setDescrizione("Descrizione di prova");
//    		queryDB.setAutore("TOPOLINO");
//    		try {
//    			List<SchemaDTO> listSchemi = model.getSchemi();
//    			List<GenericResultsDTO> listResults = new ArrayList<GenericResultsDTO>();
//    	    	if (listSchemi.size() > 0) {
//    	    		GenericResultsDTO risultatiDTO;
//    	    		for (int i = 0; i < listSchemi.size(); i++) {
//    	    			risultatiDTO = model.runQuery (listSchemi.get(i), queryDB); 
//    	    			System.out.println("Schema " + listSchemi.get(i).getSchemaUserName() + " - qta righe = " + risultatiDTO.getList().size());
//    	    			listResults.add(risultatiDTO);
//    	    		}
//    	    		if (listResults.size() > 0) {
//    	    			// write file excel
//    	    			String fileName = "D:\\SIPERT\\BASE DATI\\SVIL_CONNESSIONI_DB\\risultatiQuery.xls";
//    	    			if (FileExcelCreator.writeFileExcelOfResults(fileName, listResults)) {
//    	        			Alert alert = new Alert(AlertType.INFORMATION);
//    	        			alert.setTitle("Information Dialog");
//    	        			alert.setHeaderText("Look, an Information Dialog");
//    	        			alert.setContentText("creato file excel dei risultati >> " + fileName);
//    	        			alert.showAndWait();
//    	    			} else {
//    	        			Alert alert = new Alert(AlertType.ERROR);
//    	        			alert.setTitle("Error");
//    	        			alert.setContentText("Ooops, errore in fase di scrittura del file excel dei risultati!");
//    	        			alert.showAndWait();
//    	    			}
//    	    		}
//    
//    	    	} else {
//    	    		txtFieldEsito.setText("Nessun Schema Trovato");
//    	    	}
//    	    	hboxEsiti.setVisible(true);
//    		} catch (ErroreFileSchemiNonTrovato e) {
//
//    			Alert alert = new Alert(AlertType.ERROR);
//    			alert.setTitle("Error Dialog");
//    			alert.setHeaderText("Look, an Error Dialog");
//    			alert.setContentText("Ooops, file excel degli schemi non trovato!");
//    			alert.showAndWait();
//    		}catch (ErroreColonneFileXlsSchemiKo e) {
//
//    			Alert alert = new Alert(AlertType.ERROR);
//    			alert.setTitle("Error Dialog");
//    			alert.setHeaderText("Look, an Error Dialog");
//    			alert.setContentText("Ooops, file excel degli schemi con colonne errate!");
//    			alert.showAndWait();
//    		}
//    	} else {
//			Alert alert = new Alert(AlertType.ERROR);
//			alert.setTitle("Error Dialog");
//			alert.setHeaderText("Look, an Error Dialog");
//			alert.setContentText("Ooops, la QUERY è VUOTA!");
//			alert.showAndWait();
//    	}
    }

    @FXML
    void doTestConnections(ActionEvent event) {

    	boolean connessioniTutteOK = true;
    	int countConnessioniKO = 0;
    	
//		try {
//			List<SchemaDTO> listSchemi = model.getSchemi();
//	    	if (listSchemi.size() > 0) {
//	    		for (int i = 0; i < listSchemi.size(); i++) {
//	        		if (!model.testConnessioneDB(listSchemi.get(i))) {
//	        			//txtAreaQuery.appendText("Test Connessione su " + listSchemi.get(i).getSchemaUserName() + " OK\n");
//	        		//} else {
//	        			//txtAreaQuery.appendText("Test Connessione su " + listSchemi.get(i).getSchemaUserName() + " KO\n");
//	        			connessioniTutteOK = false;
//	        			countConnessioniKO++;
//	        		}	    			
//	    		}
//	    		if (connessioniTutteOK) {
//	    			txtFieldEsito.setText("Connessioni tutte OK");
//	    		} else {
//	    			txtFieldEsito.setText(countConnessioniKO + " Connessioni KO");
//	    		}
//	    	} else {
//	    		txtFieldEsito.setText("Nessun Schema Trovato");
//	    	}
//	    	hboxEsiti.setVisible(true);
//		} catch (ErroreFileSchemiNonTrovato e) {
//
//			Alert alert = new Alert(AlertType.ERROR);
//			alert.setTitle("Error Dialog");
//			alert.setHeaderText("Look, an Error Dialog");
//			alert.setContentText("Ooops, file excel degli schemi non trovato!");
//			alert.showAndWait();
//		}catch (ErroreColonneFileXlsSchemiKo e) {
//
//			Alert alert = new Alert(AlertType.ERROR);
//			alert.setTitle("Error Dialog");
//			alert.setHeaderText("Look, an Error Dialog");
//			alert.setContentText("Ooops, file excel degli schemi con colonne errate!");
//			alert.showAndWait();
//		}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtAreaQuery != null : "fx:id=\"txtAreaQuery\" was not injected: check your FXML file 'MainPage.fxml'.";
        assert btnLoadConnectios != null : "fx:id=\"btnLoadConnectios\" was not injected: check your FXML file 'MainPage.fxml'.";
        assert btnRunQuery != null : "fx:id=\"btnRunQuery\" was not injected: check your FXML file 'MainPage.fxml'.";
        assert bntTestConnections != null : "fx:id=\"bntTestConnections\" was not injected: check your FXML file 'MainPage.fxml'.";
        assert btnResetQuery != null : "fx:id=\"btnResetQuery\" was not injected: check your FXML file 'MainPage.fxml'.";
        assert hboxEsiti != null : "fx:id=\"hboxEsiti\" was not injected: check your FXML file 'MainPage.fxml'.";
        assert txtFieldEsito != null : "fx:id=\"txtFieldEsito\" was not injected: check your FXML file 'MainPage.fxml'.";

    }
    
	public void setModel(Model model) {
		
		this.model = model;
		
		//boxPrimo.getItems().addAll(this.model.getAutori()) ;
		//boxSecondo.getItems().clear() ;
	}
}
