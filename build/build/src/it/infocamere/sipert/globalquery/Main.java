package it.infocamere.sipert.globalquery;
	
import java.io.IOException;

import it.infocamere.sipert.globalquery.model.Model;
import it.infocamere.sipert.globalquery.model.QueryModel;
import it.infocamere.sipert.globalquery.view.QueryEditDialogController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	
    private Stage stagePrincipale;
    
    /**
     * i dati nel formato di observable list di queries.
     */
    private ObservableList<QueryModel> queryData = FXCollections.observableArrayList();

	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			//BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("MainPage.fxml"));
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MainPage.fxml"));
			BorderPane root = (BorderPane) loader.load();
			MainPageController controller = loader.getController();

			// set the model
			Model model = new Model() ;
			controller.setModel(model);
			
			// ********************
			
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
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
            // Load the fxml file and create a new stage for the popup dialog.
        	
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/QueryEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Crea lo Stage di dialogo
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Query");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stagePrincipale);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Imposta la query nel controller
            QueryEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setQuery(query);
            
            // Set the dialog icon.
            // >>> TROVATE UN'ICONA dialogStage.getIcons().add(new Image("file:resources/images/edit.png"));

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
	
    /**
     * Ritorna i dati nel formato di observable list of QueryModel. 
     * @return
     */
    public ObservableList<QueryModel> getQueryData() {
        return queryData;
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
}
