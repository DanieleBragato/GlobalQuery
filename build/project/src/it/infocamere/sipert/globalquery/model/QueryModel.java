package it.infocamere.sipert.globalquery.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import it.infocamere.sipert.globalquery.util.LocalDateAdapter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class QueryModel {

    private final StringProperty nome;
    private final StringProperty descrizione;
    private final StringProperty query;
    private final StringProperty autore;
    private final ObjectProperty<LocalDate> dataUltimoAggionamento;
    
    public QueryModel() {
        this(null, null, null, null, null);
    }
    
    /**
     * 
     * @param nome
     * @param descrizione
     * @param query
     * @param autore
     * @param dataUltimoAggiornamento 
     */
    
    public QueryModel(String nome, String descrizione, String query, String autore, LocalDate dataUltimoAggiornamento) {
    	
        this.nome = new SimpleStringProperty(nome);
        this.descrizione = new SimpleStringProperty(descrizione);
        this.autore = new SimpleStringProperty(autore);
        this.query = new SimpleStringProperty(query);
        this.dataUltimoAggionamento = new SimpleObjectProperty<LocalDate>(dataUltimoAggiornamento);
    }
    
	public String getNome() {
		return nome.get();
	}
	public String getDescrizione() {
		return descrizione.get();
	}
	public String getQuery() {
		return query.get();
	}
	public String getAutore() {
		return autore.get();
	}
	
    public void setNome(String nome) {
        this.nome.set(nome);
    }
    public void setDescrizione(String descrizione) {
        this.descrizione.set(descrizione);
    }
    public void setQuery(String query) {
        this.query.set(query);
    }
    public void setAutore(String autore) {
        this.autore.set(autore);
    }
    
    public StringProperty descrizioneProperty() {
        return descrizione;
    }

    public StringProperty autoreProperty() {
        return autore;
    }
    
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    public LocalDate getDataUltimoAggionamento() {
        return dataUltimoAggionamento.get();
    }

    public void setDataUltimoAggionamento(LocalDate dataUltimoAggionamento) {
        this.dataUltimoAggionamento.set(dataUltimoAggionamento);
    }

    public ObjectProperty<LocalDate> dataUltimoAggionamentoProperty() {
        return dataUltimoAggionamento;
    }
}
