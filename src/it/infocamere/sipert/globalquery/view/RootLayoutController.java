package it.infocamere.sipert.globalquery.view;

import java.io.File;
import java.util.Optional;

import org.apache.commons.io.FilenameUtils;

import it.infocamere.sipert.globalquery.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.stage.FileChooser;

public class RootLayoutController {

    private MainApp mainApp;
	
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
 
    @FXML
    private void handleExit() {
    	if (mainApp.getQueryFilePath() == null) {
    		if (mainApp.getQueryData().size() > 0) {
            	// alert per indicare dove salvare la lista delle query
        		Alert alert = new Alert(AlertType.CONFIRMATION);
        		alert.setTitle("Indicare il File xml delle query");
        		alert.setHeaderText("Indicare il File xml sul quale salvare l'elenco delle query");
        		alert.setContentText("Scegli la tua opzione");

        		ButtonType buttonSelectFile = new ButtonType("Select File Query..");
        		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

        		alert.getButtonTypes().setAll(buttonSelectFile, buttonTypeCancel);

        		Optional<ButtonType> result = alert.showAndWait();
        		if (result.get() == buttonSelectFile){
        			handleSave();
        		}
            } 
    	} else {
			handleSave();
		}
		System.exit(0);
    }
    
    /**
     * salva sul file correntemente aperto, se non c'� nessun file aperto viene esposto il dialogo Save As
     */
    @FXML
    private void handleSave() {
        File queryFile = mainApp.getQueryFilePath();
        if (queryFile != null) {
            mainApp.saveQueryDataToFile(queryFile);
        } else {
            handleSaveAs();
        }
    }

    /**
     * Apre un FileChooser per permettere all'utente di selezionare un file da salvare
     */
    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();

        // impostazione del filtro per l'estensione
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // espone il dialogo per il salvataggio del file
        File file = fileChooser.showSaveDialog(mainApp.getStagePrincipale());

        if (file != null) {
            // verifica la corretta estensione del file
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            mainApp.saveQueryDataToFile(file);
        }
    }
    
    /**
     * Apre un FileChooser per permettere all'utente la scelta del file delle query da aprire
     */
    @FXML
    private void handleOpenQuery() {
        FileChooser fileChooser = new FileChooser();

        // impostazione del filtro dell'estensione
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(mainApp.getStagePrincipale());

        if (file != null) {
            mainApp.loadQueryDataFromFile(file);
        }
    }
    
    /**
     * Apre un FileChooser per permettere all'utente la scelta del file degli schemi dei data base oracle
     */
    @FXML
    private void handleOpenSchemi() {
        FileChooser fileChooser = new FileChooser();

        // impostazione del filtro dell'estensione
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XLS files (*.xls)", "*.xls");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(mainApp.getStagePrincipale());

        if (file != null) {
        	boolean reload = false;
        	mainApp.loadSchemiDataBaseFromFile(file, reload);
        	mainApp.setCurrentSchemiOracleFileName(file.getAbsolutePath());
        }
    }
    
    /**
     * Apre un FileChooser per permettere all'utente la scelta del corrente file degli schemi dei data base oracle
     */
    @FXML
    private void handleSeeCurrentSchemi() {
    	
		if (mainApp.getCurrentSchemiOracleFileName() != null && mainApp.getCurrentSchemiOracleFileName() != ""
				&& mainApp.getCurrentSchemiOracleFileName().length() > 0) {
			
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Information");
			alert.setHeaderText("File Corrente Schemi Oracle");
			alert.setContentText("File Corrente degli Schemi Oracle\n" + mainApp.getCurrentSchemiOracleFileName());

			ButtonType buttonOpenFile = new ButtonType("Apri File Corrente Schemi Oracle");
			ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

			alert.getButtonTypes().setAll(buttonOpenFile, buttonTypeCancel);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == buttonOpenFile){  
				mainApp.openFileWithOfficeExcel(mainApp.getCurrentSchemiOracleFileName());
			} 
			
    	}
    	
    }    
    
    @FXML
    private void handleReloadCurrentSchemi() {
    	
		if (mainApp.getCurrentSchemiOracleFileName() != null && mainApp.getCurrentSchemiOracleFileName() != ""
				&& mainApp.getCurrentSchemiOracleFileName().length() > 0) {
			boolean reload = true;
			mainApp.loadSchemiDataBaseFromFile(new File(mainApp.getCurrentSchemiOracleFileName()), reload);
			if (mainApp.getListSchemi() != null && mainApp.getListSchemi().size() > 0) {
				Alert alertInfo = new Alert(AlertType.INFORMATION);
				alertInfo.setTitle("Information");
				alertInfo.setHeaderText("File Corrente Schemi Oracle ricaricato");
				alertInfo.setContentText(
						"Ricaricato File Corrente degli Schemi Oracle\n" + mainApp.getCurrentSchemiOracleFileName());
				alertInfo.showAndWait();
			}
		}
    	
    }
    
    @FXML
    private void handlePathFileForSaveResultsFile() {
    	
        FileChooser fileChooser = new FileChooser();

        // impostazione del filtro dell'estensione
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
        		"XLSX files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);

        File fileResults = fileChooser.showOpenDialog(mainApp.getStagePrincipale());
        
        String fileExtension = FilenameUtils.getExtension(fileResults.getAbsolutePath());

        if (fileResults != null) {
        	if (!(fileExtension != null && "xlsx".equals(fileExtension.toLowerCase()))) {
        		Alert alert = new Alert(AlertType.ERROR);
        		alert.setTitle("Global Query" + mainApp.getVersione());
        		alert.setHeaderText("Errore - nome file non corretto");
            	alert.setContentText("Prima di procedere con l'esecuzione delle query � necessario indicare il file - tipo xlsx - di destinazione dei risultati estratti ");
            	alert.showAndWait();        		
        	} else {
            	mainApp.setPathResultsFile(fileResults.getAbsolutePath());
            	mainApp.setFilePathRisultati(fileResults);
            	Alert alert = new Alert(AlertType.WARNING);
            	alert.setTitle("Global Query" + mainApp.getVersione());
            	alert.setHeaderText("Attenzione");
            	alert.setContentText("l'esecuzione delle query determina la sovrascrittura del file " + fileResults.getAbsolutePath() + " nel quale verranno salvati i dati estratti");
            	alert.showAndWait();        		
        	}
        } else {
        	Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Global Query" + mainApp.getVersione());
        	alert.setHeaderText("Errore - nome file non corretto");
        	alert.setContentText("Prima di procedere con l'esecuzione delle query � necessario indicare il file di destinazione dei risultati estratti ");
        	alert.showAndWait();
        }
    }
    
    @FXML
    private void handleSchemiProd() {
        mainApp.showListSchemiDialog(true);;	
    }
    
    /**
     * apre il dialogo dell'about
     */
    @FXML
    private void handleAbout() {
    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("Global Query" + mainApp.getVersione());
    	alert.setHeaderText("info");
    	alert.setContentText("Permette la gestione (inserimento/modifica/cancellazione/elenco/salvataggio su file xml) delle query sql da eseguire su data base oracle; l'esecuzione viene ripetuta enne volte in funzione degli schemi oracle indicati nell'apposito file di input in formato xls. Il risultato dell'esecuzione viene automaticamente salvato su un file di tipo xlsx.");

    	alert.showAndWait();
    }
}
