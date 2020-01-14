package it.infocamere.sipert.globalquery.view;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FilenameUtils;

import it.infocamere.sipert.globalquery.MainApp;
import it.infocamere.sipert.globalquery.db.QueryDB;
import it.infocamere.sipert.globalquery.db.dto.GenericResultsDTO;
import it.infocamere.sipert.globalquery.db.dto.SchemaDTO;
import it.infocamere.sipert.globalquery.model.Model;
import it.infocamere.sipert.globalquery.model.QueryModel;
import it.infocamere.sipert.globalquery.util.Constants;
import it.infocamere.sipert.globalquery.util.FileExcelCreatorPOI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class QueryEditDialogController {
	
	private Model model ;

    @FXML
    private TextField nomeField;
    @FXML
    private TextField descrizioneField;
    @FXML
    private TextArea queryArea;
    @FXML
    private TextField autoreField;
    @FXML
    private Label labelFileRisultati;
    @FXML
    private Label labelInfoEsecuzione;
    @FXML
    private Button btnRunQuery;
    @FXML
    private Button bntOkSalva;
    @FXML
    private Button bntStop;
    @FXML
    private Button bntExit;
    @FXML
    private Button bntChange;
    @FXML
    private ProgressBar bar;
    @FXML
    private VBox vboxProgressBar;
    
    private Stage dialogStage;
    private QueryModel query;
    private boolean okClicked = false;
    boolean estrazioneDatiTerminataCorrettamente = false;
    boolean erroreSuScritturaFileRisultati = false;
    boolean nessunDatoEstratto = false;
    private String pathFileResults = null;
    private List<SchemaDTO> listSchemi = null;
    private List<GenericResultsDTO> listResults = new ArrayList<GenericResultsDTO>();
    private int qtaRigheEstratte = 0;
    
    private Task copyWorker;
    
    private MainApp mainApp;
	
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        labelFileRisultati.setText(this.mainApp.getPathResultsFile());
    }
	
    @FXML
    private void initialize() {

    	bar.setVisible(true);
    	bntStop.setDisable(true);
    	if (this.mainApp != null) {
    		labelFileRisultati.setText(this.mainApp.getPathResultsFile());
    	}
    }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    public void setQuery(QueryModel query) {    	
        this.query = query;
        nomeField.setText(query.getNome());
        descrizioneField.setText(query.getDescrizione());
        autoreField.setText(query.getAutore());
        queryArea.setText(query.getQuery());
    }
    
    public boolean isOkClicked() {
        return okClicked;
    }
    
    @FXML
    void doRunQuery(ActionEvent event) {
    	
    	if (!validationFilesIsOK()) return;
    	
    	QueryDB queryDB = new QueryDB();
    	
    	if (isInputQueryValorizzato()) {
    		queryDB.setQuery(queryArea.getText());
    		queryDB.setDescrizione(descrizioneField.getText());
    		queryDB.setAutore(autoreField.getText());
    		try {
    	    	if (listSchemi.size() > 0) {
    	    		estrazioneDatiDB(queryDB);
    	    	} else {
    	    		showAlert(AlertType.ERROR, "Error", "", "Schemi data base non trovati!", null);
    	    	}
    	    	
    		} catch (RuntimeException e2) {
    			showAlert(AlertType.ERROR, "Error", "", e2.toString(), null);
    		}
    	} 
    }
    
    private boolean validationFilesIsOK() {
    	
    	if (!fileResultsIsOk()) return false;
    	
    	listSchemi = mainApp.getListSchemi();
    	
    	if (listSchemi == null || listSchemi.size() <= 0) {
    		if (!fileSchemiIsOK()) return false;
    	}
		return true;
	}

	private void estrazioneDatiDB(QueryDB queryDB) {
		
		estrazioneDatiTerminataCorrettamente = false;
		erroreSuScritturaFileRisultati = false;
		nessunDatoEstratto = false;
		
		disabledView(true);
		
		btnRunQuery.setDisable(true);
        bar.setProgress(0);
        bntStop.setDisable(false);
 
        copyWorker = createWorker(queryDB);
        
        labelInfoEsecuzione.textProperty().unbind();
        labelInfoEsecuzione.textProperty().bind(copyWorker.messageProperty());

        bar.progressProperty().unbind();
        bar.progressProperty().bind(copyWorker.progressProperty());
       
        copyWorker.messageProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                System.out.println("newValue " + newValue);
            }
        });

	    Thread backgroundThread = new Thread(copyWorker , "queryDataBase-thread");
	    //System.out.println("sono dopo la new del Thread");
	    backgroundThread.setDaemon(true);
	    //System.out.println("sono prima della start del Thread");
	    backgroundThread.start();
	    //System.out.println("sono dopo la start del Thread");

	    copyWorker.setOnFailed(e -> {
            Throwable exception = ((Task) e.getSource()).getException();
            if (exception != null) {
            	//System.out.println("eccezione " + exception.getStackTrace());
                copyWorker.cancel(true);
                bar.progressProperty().unbind();
                labelInfoEsecuzione.textProperty().unbind();
                labelInfoEsecuzione.setText("Elaborazione fallita!");
                bar.setProgress(0);
            	btnRunQuery.setDisable(false);
                bntStop.setDisable(true);
                bntOkSalva.setDisable(false);
                bntExit.setDisable(false);
                disabledView(false);
            	showAlert(AlertType.ERROR, "Error", "", "Errore " + exception.toString(), null);
            }
        });
		
	}

    private void disabledView(boolean b) {
    	
		nomeField.setDisable(b);
		descrizioneField.setDisable(b);
		autoreField.setDisable(b);
		queryArea.setDisable(b);
		
        bntOkSalva.setDisable(b);
        bntExit.setDisable(b);
        bntChange.setDisable(b);
		
	}

	public Task createWorker(QueryDB queryDB) {
        return new Task() {
            @Override
            protected Object call() throws Exception {

				//System.out.println("sono dentro il metodo call del Task");
				GenericResultsDTO risultatiDTO;
				//System.out.println(listSchemi.size() + " Schemi da trattare");
				for (int i = 0; i < listSchemi.size(); i++) {
					//System.out.println("Inizio trattamento dello Schema " + listSchemi.get(i).getSchemaUserName());
					if (this.isCancelled()) {
						//System.out.println("Canceling...");
						break;
					} 
					risultatiDTO = model.runQuery (listSchemi.get(i), queryDB); 
					//System.out.println("Schema nr. " + (i+1) + " " + listSchemi.get(i).getSchemaUserName() + " - qta righe = " + risultatiDTO.getListLinkedHashMap().size());
					
					updateMessage("Schema nr. " + (i+1) + " di " + listSchemi.size() + " - " + listSchemi.get(i).getSchemaUserName() + " - qta righe estratte = " + risultatiDTO.getListLinkedHashMap().size());
					
					qtaRigheEstratte = qtaRigheEstratte + risultatiDTO.getListLinkedHashMap().size();
					listResults.add(risultatiDTO);
					updateProgress(i, listSchemi.size());
				}
				if (listResults.size() > 0) {
					if (FileExcelCreatorPOI.writeFileExcelOfResults(pathFileResults, listResults, queryDB)) {
						//System.out.println("sono dentro il metodo call del Task - estrazione Dati Terminata Correttamente");
						estrazioneDatiTerminataCorrettamente = true;
					} else {
						//System.out.println("sono dentro il metodo call del Task - errore Su Scrittura File Risultati");
						erroreSuScritturaFileRisultati = true;
					}
				} else {
					//System.out.println("sono dentro il metodo call del Task - nessun Dato Estratto");
					nessunDatoEstratto = true;
				}
                return true;
            }
            
            @Override protected void succeeded() {
            	System.out.println("sono dentro il metodo succeeded del Task");
                super.succeeded();
                updateMessage("Done!");
                showAlertEstrazioneOK();
                bar.progressProperty().unbind();
                labelInfoEsecuzione.textProperty().unbind();
                labelInfoEsecuzione.setText("Elaborazione terminata correttamente");
                bar.setProgress(0);
            	btnRunQuery.setDisable(false);
                bntStop.setDisable(true);
                bntOkSalva.setDisable(false);
                bntExit.setDisable(false);
        		disabledView(false);
            }


			@Override protected void cancelled() {
            	//System.out.println("sono dentro il metodo cancelled del Task");
                super.cancelled();
                updateMessage("Cancelled!");
                disabledView(false);
            }
            
        };
    }

    private void showAlertEstrazioneOK() {
    	
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(listSchemi.size() + " schemi trattati \n" + qtaRigheEstratte + " righe estratte");
		alert.setContentText("Dati salvati sul file\n" + pathFileResults);

		ButtonType buttonOpenFile = new ButtonType("Apri File Risultati");
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(buttonOpenFile, buttonTypeCancel);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonOpenFile){  
			mainApp.openFileWithOfficeExcel(pathFileResults);
		} 
		
	}
	
    @FXML
    private void handleStop(ActionEvent event) {
    	btnRunQuery.setDisable(false);
        bntStop.setDisable(true);
        bntOkSalva.setDisable(false);
        bntExit.setDisable(false);
        if (copyWorker != null) copyWorker.cancel(true);
        bar.progressProperty().unbind();
        //System.out.println("click on Stop button");
    }
    
    @FXML
    private void handleExit(ActionEvent event) {
    	if (copyWorker != null) copyWorker.cancel(true);
        bar.progressProperty().unbind();
        bar.setProgress(0);
        //System.out.println("click on Exit button");
        dialogStage.close();
    }
    
    @FXML
    private void doChangeFileRisultati(ActionEvent event) {
    	
		FileChooser fileChooser = new FileChooser();
		
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XLSX files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);		
		
		fileChooser.setTitle("File Risultati");
		pathFileResults = mainApp.getPathResultsFile();
		fileChooser.setInitialFileName(pathFileResults);
		File fileResults = fileChooser.showSaveDialog(mainApp.getStagePrincipale());
		
        String fileExtension = FilenameUtils.getExtension(fileResults.getAbsolutePath());

		if (fileResults != null) {
        	if (!(fileExtension != null && "xlsx".equals(fileExtension.toLowerCase()))) {
    			showAlert(AlertType.ERROR, "Global Query" + mainApp.getVersione(), "Cambio file risultati non effettuato - tipo file non corretto",
    					"Prima di procedere con l'esecuzione delle query è necessario indicare il file - tipo xlsx - di destinazione dei risultati estratti.",
    					null);
        	} else {
    	        mainApp.setFilePathRisultati(fileResults);
    	        mainApp.setPathResultsFile(fileResults.getAbsolutePath());
    	        pathFileResults = fileResults.getAbsolutePath();
    	        labelFileRisultati.setText(pathFileResults);
        	}
		} else {
			showAlert(AlertType.ERROR, "Global Query" + mainApp.getVersione(), "Errore - nome file non corretto",
					"Prima di procedere con l'esecuzione delle query è necessario indicare il file di destinazione dei risultati estratti ",
					null);
		}
    	
    }
    
	public void showAlert(AlertType type, String title, String headerText, String text, Stage stage) {
		
		Alert alert = new Alert(type);

		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(text);

		if (stage != null) alert.initOwner(stage);
		
		alert.showAndWait();
	}
	    
	private boolean fileSchemiIsOK() {
    	
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Indicare il File xls degli Schemi dei Data Base");
		alert.setHeaderText("Indicare il File xls degli Schemi dei Data Base Oracle");
		alert.setContentText("Scegli la tua opzione");

		ButtonType buttonSelectFile = new ButtonType("Select File Schemi..");
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(buttonSelectFile, buttonTypeCancel);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonSelectFile){
			if (handlePathFileSchemiDB()) {
				listSchemi = mainApp.getListSchemi();
			}
		} 
		return false;
	}

	private boolean fileResultsIsOk() {
		
		boolean fileIsOk = false;
    	
    	pathFileResults = mainApp.getPathResultsFile();
    	
    	if (pathFileResults == null || "".equalsIgnoreCase(pathFileResults)) {
    		Alert alert = new Alert(AlertType.CONFIRMATION);
    		alert.setTitle("Indicare il File xlsx dei risultati");
    		alert.setHeaderText("Indicare il File xlsx sul quale salvare i risultati della query");
    		alert.setContentText("Scegli la tua opzione");

    		ButtonType buttonSelectFile = new ButtonType("Select File Risultati..");
    		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

    		alert.getButtonTypes().setAll(buttonSelectFile, buttonTypeCancel);

    		Optional<ButtonType> result = alert.showAndWait();
    		if (result.get() == buttonSelectFile){
    			if (handlePathFileForSaveResultsFile()) {
    				pathFileResults = mainApp.getPathResultsFile();
    				labelFileRisultati.setText(pathFileResults);
    				fileIsOk = true;
    			} 
    		} 
    	} else {
            String fileExtension = FilenameUtils.getExtension(pathFileResults);
        	if (!(fileExtension != null && "xlsx".equals(fileExtension.toLowerCase()))) {
    			showAlert(AlertType.ERROR, "Global Query" + mainApp.getVersione(), "Errore - file non corretto",
    					"Prima di procedere con l'esecuzione delle query è necessario indicare il file - tipo xlsx - di destinazione dei risultati estratti ",
    					null);
            	return false;
        	}            
    		fileIsOk = true;
    	}
    	
		return fileIsOk;
	}

	private boolean handlePathFileForSaveResultsFile() {
    	
        FileChooser fileChooser = new FileChooser();

        // impostazione del filtro dell'estensione xlsx
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XLSX files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);

        File fileResults = fileChooser.showOpenDialog(mainApp.getStagePrincipale());
        
        String fileExtension = FilenameUtils.getExtension(fileResults.getAbsolutePath());

        if (fileResults != null) {
        	if (!(fileExtension != null && "xlsx".equals(fileExtension.toLowerCase()))) {
    			showAlert(AlertType.ERROR, "Global Query" + mainApp.getVersione(), "Errore - file non corretto",
    					"Prima di procedere con l'esecuzione delle query è necessario indicare il file - tipo xlsx - di destinazione dei risultati estratti ",
    					null);
            	return false;
        	}
        	mainApp.setFilePathRisultati(fileResults);
        	mainApp.setPathResultsFile(fileResults.getAbsolutePath());
			showAlert(AlertType.WARNING, "Global Query" + mainApp.getVersione(), "Attenzione",
					"l'esecuzione delle query determina la sovrascrittura del file \" + fileResults.getAbsolutePath() + \"\\nnel quale verranno salvati i dati estratti",
					null);
        	return true;
        } else {
			showAlert(AlertType.ERROR, "Global Query" + mainApp.getVersione(), "Errore - nome file non corretto",
					"Prima di procedere con l'esecuzione delle query è necessario indicare il file - tipo xlsx - di destinazione dei risultati estratti ",
					null);
        	return false;
        }
    }
    
    private boolean handlePathFileSchemiDB() {
    	
        FileChooser fileChooser = new FileChooser();

        // impostazione del filtro dell'estensione
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XLS files (*.xls)", "*.xls");
        fileChooser.getExtensionFilters().add(extFilter);

        File fileSchemi = fileChooser.showOpenDialog(mainApp.getStagePrincipale());

        if (fileSchemi != null) {
        	mainApp.setSchemiDataBaseFilePath(fileSchemi);
        	boolean reload = false;
        	mainApp.loadSchemiDataBaseFromFile(fileSchemi, reload);
        	return true;
        } else {
        	return false;
        }    	
    }
    
    @FXML
    private void handleOkSalva() {
    	
    	if (copyWorker != null) copyWorker.cancel(true);
    	
        if (isInputValid()) {
        	query.setNome(nomeField.getText());
        	query.setDescrizione(descrizioneField.getText());
        	query.setQuery(queryArea.getText());
        	query.setAutore(autoreField.getText());
        	LocalDate oggi = LocalDate.now();
        	query.setDataUltimoAggionamento(oggi);
            okClicked = true;
            dialogStage.close();
        }
    }
    

    private boolean isInputQueryValorizzato() {
    	
        String errorMessage = "";
        
        if (queryArea.getText() == null || queryArea.getText().length() == 0) {
            errorMessage += "query non valorizzata!\n"; 
        } else {
        	String presenceOfUpdateWordsString = presenceOfUpdateWords(queryArea.getText().toUpperCase());
        	if (presenceOfUpdateWordsString != null && presenceOfUpdateWordsString.length() > 0) {
        		errorMessage += "SQL NON VALIDO: impostare solo istruzioni di lettura dati.\n";
        		errorMessage += "Togliere le istruzioni di " + presenceOfUpdateWordsString + "\n";
        	}
        	if (!queryArea.getText().toUpperCase().contains(Constants.SELECT)) {
        		errorMessage += "SQL NON VALIDO: istruzioni di SELECT assenti\n";
        	}
        }

		if (errorMessage.length() == 0) {
			return true;
		} else {
			showAlert(AlertType.ERROR, "campi non validi", "Per cortesia, correggi i campi non validi", errorMessage, dialogStage);
			return false;
		}
    }
    
    private String presenceOfUpdateWords(String sqlText) {
        String updateWords = "";
		if (sqlText.contains(Constants.UPDATE)) updateWords = updateWords.concat(Constants.UPDATE + " ");
		if (sqlText.contains(Constants.DELETE)) updateWords = updateWords.concat(Constants.DELETE + " ");
		if (sqlText.contains(Constants.INSERT)) updateWords = updateWords.concat(Constants.INSERT + " ");
		if ( sqlText.contains(Constants.CREATE)) updateWords = updateWords.concat(Constants.CREATE + " ");
		if ( sqlText.contains(Constants.DROP)) updateWords = updateWords.concat(Constants.DROP + " ");
		if (sqlText.contains(Constants.ALTER)) updateWords = updateWords.concat(Constants.ALTER + " ");
		if (sqlText.contains(Constants.GRANT)) updateWords = updateWords.concat(Constants.GRANT + " ");
		if (sqlText.contains(Constants.MODIFY)) updateWords = updateWords.concat(Constants.MODIFY + " ");
		if (sqlText.contains(Constants.RENAME)) updateWords = updateWords.concat(Constants.RENAME + " ");
		if (sqlText.contains(Constants.REVOKE)) updateWords = updateWords.concat(Constants.REVOKE + " ");
		if (sqlText.contains(Constants.ADD)) updateWords = updateWords.concat(Constants.ADD + " ");
		if (sqlText.contains(Constants.CONNECT)) updateWords = updateWords.concat(Constants.CONNECT + " ");
		if (sqlText.contains(Constants.LOCK)) updateWords = updateWords.concat(Constants.LOCK + " ");

		return updateWords;
    }
    
    private boolean isInputValid() {
        String errorMessage = "";

        if (nomeField.getText() == null || nomeField.getText().length() == 0) {
            errorMessage += "Nome query non valorizzato!\n"; 
        }
        if (descrizioneField.getText() == null || descrizioneField.getText().length() == 0) {
            errorMessage += "Descrizione non valorizzata!\n"; 
        }
        if (autoreField.getText() == null || autoreField.getText().length() == 0) {
            errorMessage += "Autore non valorizzato!\n"; 
        }

        if (queryArea.getText() == null || queryArea.getText().length() == 0) {
            errorMessage += "query non valorizzata!\n"; 
        }

		if (errorMessage.length() == 0) {
			return true;
		} else {
			showAlert(AlertType.ERROR, "campi non validi", "Per cortesia, correggi i campi non validi", errorMessage, dialogStage);
			return false;
		}
    }
    
	public void setModel(Model model) {
		this.model = model;
	}
	    
}
