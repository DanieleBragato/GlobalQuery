package it.infocamere.sipert.globalquery.model;

import java.io.File;
import java.util.List;

import it.infocamere.sipert.globalquery.db.QueryDB;
import it.infocamere.sipert.globalquery.db.dao.GenericDAO;
import it.infocamere.sipert.globalquery.db.dto.GenericResultsDTO;
import it.infocamere.sipert.globalquery.db.dto.SchemaDTO;
import it.infocamere.sipert.globalquery.exception.ErroreColonneFileXlsSchemiKo;
import it.infocamere.sipert.globalquery.exception.ErroreFileSchemiNonTrovato;
import it.infocamere.sipert.globalquery.util.SchemiManager;

public class Model {

	private List<SchemaDTO> schemi;
	
	public List<SchemaDTO> getSchemi(File fileSchemiXLS) throws ErroreFileSchemiNonTrovato, ErroreColonneFileXlsSchemiKo {
		
		if (this.schemi == null) {
			SchemiManager schemiManager = new SchemiManager();
			this.schemi = schemiManager.getListSchemi(fileSchemiXLS) ;

			//System.out.println("Trovati " +  this.schemi.size() + " schemi");
		}

		return this.schemi ;

	}
	
	public boolean testConnessioneDB(SchemaDTO schemaDB) {
		
		GenericDAO genericDAO = new GenericDAO();
		
		return genericDAO.testConnessioneOK(schemaDB);
		
	}
	
	public GenericResultsDTO runQuery(SchemaDTO schema, QueryDB queryDB) {
		
		GenericResultsDTO risultati = null;
		
		GenericDAO genericDAO = new GenericDAO();
		risultati = genericDAO.executeQuery(schema, queryDB);
		
		return risultati;
	}
}
