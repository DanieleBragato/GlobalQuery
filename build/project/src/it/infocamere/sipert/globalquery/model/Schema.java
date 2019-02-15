package it.infocamere.sipert.globalquery.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


/**
 * 
 * @author YYI4627
 *
 */

public class Schema {

	private final StringProperty codiceSchema;
	private final StringProperty descrizioneSchema;
	
	public Schema(String codiceSchema, String descrizioneSchema) {
		this.codiceSchema = new SimpleStringProperty(codiceSchema);
		this.descrizioneSchema = new SimpleStringProperty(descrizioneSchema);
	}

	public String getCodiceSchema() {
		return codiceSchema.get();
	}

	public String getDescrizioneSchema() {
		return descrizioneSchema.get();
	}
	
	public void setCodiceSchema(String codiceSchema) {
		this.codiceSchema.set(codiceSchema);
	}
	
	public void setDescrizioneSchema(String descrizioneSchema) {
		this.descrizioneSchema.set(descrizioneSchema);
	}
	
	public StringProperty codiceSchemaProperty() {
		return codiceSchema;
	}
	
	public StringProperty descrizioneSchemaProperty() {
		return descrizioneSchema;
	}

}
