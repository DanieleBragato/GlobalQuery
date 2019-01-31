package it.infocamere.sipert.globalquery;
	
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import it.infocamere.sipert.globalquery.db.dto.SchemaDTO;
import it.infocamere.sipert.globalquery.exception.ErroreColonneFileXlsSchemiKo;
import it.infocamere.sipert.globalquery.exception.ErroreFileSchemiNonTrovato;
import it.infocamere.sipert.globalquery.model.Model;
import it.infocamere.sipert.globalquery.model.QueryListWrapper;
import it.infocamere.sipert.globalquery.model.QueryModel;
import it.infocamere.sipert.globalquery.view.QueryEditDialogController;
import it.infocamere.sipert.globalquery.view.QueryOverviewController;
import it.infocamere.sipert.globalquery.view.RootLayoutController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class MainApp extends Application {
	
    private Stage stagePrincipale;
    private BorderPane rootLayout;
    
    /**
     * i dati nel formato di observable list di queries.
     */
    private ObservableList<QueryModel> queryData = FXCollections.observableArrayList();
    
	/**
     *  gli schemi dei data base oracle da trattare
     */
    private List<SchemaDTO> listSchemi = new ArrayList<SchemaDTO>();
    
    private String pathResultsFile = "";

	public List<SchemaDTO> getListSchemi() {
		return listSchemi;
	}

	public MainApp() {
        // Aggiungo alcuni dati di esempio
    	//queryData.add(new QueryModel("prova1", "test1", "SELECT * FROM PIPPO", "TOPOLINO", LocalDate.of(1999, 2, 21)));
    	//queryData.add(new QueryModel("prova2", "test2", "SELECT * FROM PLUTO", "MINNI", LocalDate.of(2009, 2, 8)));
    }
	
 
	@Override
	public void start(Stage stagePrincipale) {
		
        this.stagePrincipale = stagePrincipale;
        this.stagePrincipale.setTitle("Global Query");
        
        // Set the application icon.
        this.stagePrincipale.getIcons().add(new Image("file:resources/images/globalquery_32.png"));

        initRootLayout();

        showQueryOverview();
		
//		try {
//			
//			//BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("MainPage.fxml"));
//			
//			FXMLLoader loader = new FXMLLoader(getClass().getResource("MainPage.fxml"));
//			BorderPane root = (BorderPane) loader.load();
//			MainPageController controller = loader.getController();
//
//			// set the model
//			Model model = new Model() ;
//			controller.setModel(model);
//			
//			// ********************
//			
//			Scene scene = new Scene(root);
//			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
//			primaryStage.setScene(scene);
//			primaryStage.show();
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
	}
	
	public void initRootLayout() {

		try {
			// carico della root layout dall' fxml file
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// esposizione della Scene contenente il root layout.
			Scene scene = new Scene(rootLayout);
			stagePrincipale.setScene(scene);

			// do al controllore l'accesso alla main app.
			RootLayoutController controller = loader.getController();
			controller.setMainApp(this);

			stagePrincipale.show();

		} catch (IOException e) {
			e.printStackTrace();
		}

		// tentativo di caricamento dell'ultimo file delle query aperto 
		File file = getQueryFilePath();
		if (file != null) {
			loadQueryDataFromFile(file);
		}
		// tentativo di caricamento dell'ultimo file degli schemi dei data base oracle aperto 
		File fileSchemiDB = getSchemiFilePath();
		if (fileSchemiDB != null) {
			loadSchemiDataBaseFromFile(fileSchemiDB);
		}
		// tentativo di ricerca dell'ultimo file aperto dei risultati 
		File filePathRisultati = getFilePathRisultati();
		if (filePathRisultati != null) {
			setPathResultsFile(filePathRisultati.getAbsolutePath());
		}
	}

	public File getQueryFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		String filePath = prefs.get("filePath", null);
		if (filePath != null) {
			return new File(filePath);
		} else {
			return null;
		}
	}


	public File getSchemiFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		String filePathSchemiDB = prefs.get("filePathSchemiDB", null);
		if (filePathSchemiDB != null) {
			return new File(filePathSchemiDB);
		} else {
			return null;
		}
	}

	public File getFilePathRisultati() {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		String filePathResults = prefs.get("filePathResults", null);
		if (filePathResults != null) {
			return new File(filePathResults);
		} else {
			return null;
		}
	}
	
	
    public void loadQueryDataFromFile(File file) {
    	
        try {
            JAXBContext context = JAXBContext
                    .newInstance(QueryListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // lettura XML dal file e unmarshalling.
            QueryListWrapper wrapper = (QueryListWrapper) um.unmarshal(file);

            queryData.clear();
            queryData.addAll(wrapper.getQueries());

            // Save del file path sul registro.
            setQueryFilePath(file);

        } catch (Exception e) { // catches ANY exception
        	
        	Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Error");
        	alert.setHeaderText("Elenco Query vuoto");
        	alert.setContentText("Impossibile caricare le query dal file:\n" + file.getPath());
        	
        	alert.showAndWait();
        	queryData.clear();
        	
        }
    }
    
	public void loadSchemiDataBaseFromFile(File fileSchemiXLS) {

		Model model = new Model();

		try {
			listSchemi = model.getSchemi(fileSchemiXLS);
			setSchemiDataBaseFilePath(fileSchemiXLS);

		} catch (ErroreFileSchemiNonTrovato e) {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setContentText("File excel degli schemi non trovato!");
			alert.showAndWait();
		} catch (ErroreColonneFileXlsSchemiKo e1) {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setContentText("File excel degli schemi con colonne errate!");
			alert.showAndWait();
		} catch (RuntimeException e2) {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setContentText(e2.toString());
			alert.showAndWait();
		}
	}
	
	/**
     * Apre un dialog per editare i dettagli di una specifica query. Se l'utente clicca OK
     * i cambiamenti vengono salvati e ritorna true
     * 
     * @param query
     * @return true , se l'utente clicca su OK, altrimenti false
     */
    public boolean showQueryEditDialog(QueryModel query) {
        try {
            // carico dell' fxml file e creazione del nuovo stage per il popup dialog.
        	
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/QueryEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Crea lo Stage di dialogo
            Stage dialogStage = new Stage();
            
            dialogStage.getIcons().add(new Image("file:resources/images/globalquery_32.png"));
            
            dialogStage.setTitle("Edit Query");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stagePrincipale);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Imposta la query nel controller
            QueryEditDialogController controller = loader.getController();
            controller.setMainApp(this);
            controller.setDialogStage(dialogStage);
            controller.setQuery(query);
            
			// set the model
			Model model = new Model() ;
			controller.setModel(model);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
            
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public void setQueryFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());

            // modifica del titolo dell'app
            stagePrincipale.setTitle("Global Query - " + file.getName());
        } else {
            prefs.remove("filePath");

            // modifica del titolo dell'app
        	stagePrincipale.setTitle("Global Query");
        }
    }
    
    public void setSchemiDataBaseFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            prefs.put("filePathSchemiDB", file.getPath());
        } else {
            prefs.remove("filePathSchemiDB");
        }
    }
    
    public void setFilePathRisultati(File file) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            prefs.put("filePathResults", file.getPath());
        } else {
            prefs.remove("filePathResults");
        }
    }
    
    public void saveQueryDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(QueryListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Wrapping dei dati di query
            QueryListWrapper wrapper = new QueryListWrapper();
            wrapper.setQueries(queryData);

            // Marshalling e salvataggio XML su file
            m.marshal(wrapper, file);

            // Salvataggio del file path sul registro
            setQueryFilePath(file);
            
        } catch (Exception e) { // catches ANY exception
        	Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Error");
        	alert.setHeaderText("Impossibile salvare i dati");
        	alert.setContentText("Impossibile salvare i dati sul file:\n" + file.getPath());
        	
        	alert.showAndWait();
        }
    }
	
    public void showQueryOverview() {
        try {
            // Load query overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/QueryOverview.fxml"));
            AnchorPane queryOverview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(queryOverview);

            // Give the controller access to the main app.
            QueryOverviewController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Ritorna i dati nel formato di observable list of QueryModel. 
     * @return
     */
    public ObservableList<QueryModel> getQueryData() {
        return queryData;
    }
    
    public void setQueryData(ObservableList<QueryModel> queryData) {
		this.queryData = queryData;
	}
    
    public boolean addQueryToQueryData (QueryModel query) {
    	if (query != null) {
    		this.queryData.add(query);
    		return true;
    	}
    	return false;
    }
	
    /**
     * Ritorna lo stage principale
     * @return
     */
    public Stage getStagePrincipale() {
        return stagePrincipale;
    }
	
	public static void main(String[] args) {
		launch(args);
	}

	public String getPathResultsFile() {
		return pathResultsFile;
	}

	public void setPathResultsFile(String pathResultsFile) {
		this.pathResultsFile = pathResultsFile;
	}
	
	public void openFileRisultatiWithOfficeExcel(String pathFile) {
		File excelFile = new File(pathFile);
		try {
			getHostServices().showDocument(excelFile.toURI().toURL().toExternalForm());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
