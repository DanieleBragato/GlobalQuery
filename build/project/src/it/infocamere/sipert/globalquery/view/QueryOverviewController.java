package it.infocamere.sipert.globalquery.view;

import java.time.LocalDate;

import it.infocamere.sipert.globalquery.MainApp;
import it.infocamere.sipert.globalquery.model.QueryModel;
import it.infocamere.sipert.globalquery.util.DateUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class QueryOverviewController {
	
    @FXML
    private TableView<QueryModel> queryTable;
    @FXML
    private TableColumn<QueryModel, String> autoreColumn;
    @FXML
    private TableColumn<QueryModel, String> descrizioneColumn;
    
    @FXML
    private TableColumn<QueryModel, LocalDate> dataUltimoAggiornamentoColumn;
    
    @FXML
    private Label nomeLabel;
    @FXML
    private Label descrizioneLabel;
    @FXML
    private Label autoreLabel;
    @FXML
    private Label queryLabel;
    @FXML
    private Label dataUltimoAggiornamentoLabel;

    // Referimento al main 
    private MainApp mainApp;
    
    public QueryOverviewController() {
    }
    
    /**
     * Initialializza la classe controller, chiamato automaticamente dopo il caricamento del file fxml
     */
    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
    	autoreColumn.setCellValueFactory(cellData -> cellData.getValue().autoreProperty());
    	descrizioneColumn.setCellValueFactory(cellData -> cellData.getValue().descrizioneProperty());
    	dataUltimoAggiornamentoColumn.setCellValueFactory(cellData -> cellData.getValue().dataUltimoAggionamentoProperty());
        
        // pulizia del dettaglio della query
        showQueryDetails(null);

        // Listener per la selezione delle modifiche e della visualizzazione del dettaglio della query quando viene cambiata
        queryTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showQueryDetails(newValue));
    }
    
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // aggiunta di una observable list alla table
        queryTable.setItems(mainApp.getQueryData());
    }
    
    
    /**
     * carica tutti i campi da mostare nel dettaglio della query
     * 
     * @param la query
     */
    private void showQueryDetails(QueryModel query) {
    	
        if (query != null) {
            nomeLabel.setText(query.getNome());
            descrizioneLabel.setText(query.getDescrizione());
            //queryLabel.setText(query.getQuery());
            autoreLabel.setText(query.getAutore());
            dataUltimoAggiornamentoLabel.setText(DateUtil.format(query.getDataUltimoAggionamento()));

        } else {
        	nomeLabel.setText("");
        	descrizioneLabel.setText("");
        	//queryLabel.setText("");
        	autoreLabel.setText("");
        	dataUltimoAggiornamentoLabel.setText("");
        }
    }
    
    /**
     * chiamata quando l'utente fa click sul bottone Cancella
     */
    @FXML
    private void handleDeleteQuery() {
        int selectedIndex = queryTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            queryTable.getItems().remove(selectedIndex);
        } else {
            // Nessuna selezione
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getStagePrincipale());
            alert.setTitle("Nessuna Selezione");
            alert.setHeaderText("Nessuna Query Selezionata");
            alert.setContentText("Per cortesia, seleziona una query dalla lista");
            
            alert.showAndWait();
        }
    }
    
    /**
     * chiamata quando l'utente fa click sul bottone Nuova Query, per impostare i dettagli della nuova query
     */
    @FXML
    private void handleNewQuery() {
    	QueryModel tempQuery = new QueryModel();
        boolean okClicked = mainApp.showQueryEditDialog(tempQuery);
        if (okClicked) {
            mainApp.getQueryData().add(tempQuery);
        }
    }
    
    /**
     * chiamata quando l'utente fa click sul bottone Edit query
     */
    @FXML
    private void handleEditQuery() {
        QueryModel selectedQuery = queryTable.getSelectionModel().getSelectedItem();
        if (selectedQuery != null) {
            boolean okClicked = mainApp.showQueryEditDialog(selectedQuery);
            if (okClicked) {
                showQueryDetails(selectedQuery);
            }

        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getStagePrincipale());
            alert.setTitle("No Selection");
            alert.setHeaderText("nessuna query selezionata");
            alert.setContentText("Per cortesia, seleziona una query dalla lista ");
            
            alert.showAndWait();
        }
    }
    
}
