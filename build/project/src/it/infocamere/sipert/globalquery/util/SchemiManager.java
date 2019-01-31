package it.infocamere.sipert.globalquery.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import it.infocamere.sipert.globalquery.db.dto.SchemaDTO;
import it.infocamere.sipert.globalquery.exception.ErroreColonneFileXlsSchemiKo;
import it.infocamere.sipert.globalquery.exception.ErroreFileSchemiNonTrovato;
import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class SchemiManager {
	
	private boolean colonneFileXlsSchemiOk = false;
	
	private static final Logger LOGGER = Logger.getLogger(SchemiManager.class.getName());

	public List<SchemaDTO> getListSchemi(File fileSchemiXLS) throws ErroreFileSchemiNonTrovato, ErroreColonneFileXlsSchemiKo {
		
		List<SchemaDTO> schemi  = new ArrayList<>() ;
		
		if (!fileSchemiXLS.exists()) {
			throw new ErroreFileSchemiNonTrovato();
		}
		
		Workbook workbook; 
		
		try {
			
			workbook = Workbook.getWorkbook(fileSchemiXLS);
			Sheet sheet = workbook.getSheet(0);
			
			for (int iRiga = 0; iRiga < sheet.getRows(); iRiga++) {
				
				if (iRiga == 0) colonneFileXlsSchemiOk = checkTestataColonne(sheet.getRow(iRiga));
				
				if (!colonneFileXlsSchemiOk) {
					throw new ErroreColonneFileXlsSchemiKo();
				}
				
				if (colonneFileXlsSchemiOk && iRiga > 0) {
					
					SchemaDTO schemaDTO = new SchemaDTO();
					
					for (int iColonna = 0; iColonna < sheet.getColumns(); iColonna++) {

						Cell cell = sheet.getCell(iColonna, iRiga);
						CellType cellType = cell.getType();
						
						
						if (cellType == CellType.LABEL) {
							if (iColonna == Constants.NUM_COLL_SCHEMA) {
								schemaDTO.setSchemaUserName(cell.getContents());
							}
							if (iColonna == Constants.NUM_COLL_PASSWORD) {
								schemaDTO.setPassword(cell.getContents());
							}
							if (iColonna == Constants.NUM_COLL_DBNAME) {
								schemaDTO.setDbName(cell.getContents());
							}
							if (iColonna == Constants.NUM_COLL_PORT) {
								schemaDTO.setPort(cell.getContents());
							}
							if (iColonna == Constants.NUM_COLL_SERVER) {
								schemaDTO.setHostServerURL(cell.getContents());
							}
						}
//						if (cellType == CellType.LABEL) {
//							System.out.println("CELLA (riga " + iRiga + " colonna " + iColonna + ") tipo LABEL - valore = " + cell.getContents());
//						}
//						if (cellType == CellType.NUMBER) {
//							System.out.println("CELLA (riga " + iRiga + " colonna " + iColonna + ") tipo NUMBER - valore = " + cell.getContents());
//						}
//						if (cellType == CellType.DATE) {
//							System.out.println("CELLA (riga " + iRiga + " colonna " + iColonna + ") tipo DATE - valore = " + cell.getContents());
//						}
					}					
					schemi.add(schemaDTO) ;
				}
			}
			
		} catch (BiffException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore nella lettura del file xls delle connessioni", e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore nella lettura del file xls delle connessioni", e);
		}

		return schemi;
	}

	private boolean checkTestataColonne(Cell[] row) {

		int qtaOK = 0;

		for (int i = 0; i < row.length; i++) {
			if (row[i].getType() == CellType.LABEL) {
				if (i == Constants.NUM_COLL_SCHEMA && row[i].getContents().equalsIgnoreCase(Constants.NOME_COLL_SCHEMA)) {
					qtaOK++;
				}
				if (i == Constants.NUM_COLL_PASSWORD && row[i].getContents().equalsIgnoreCase(Constants.NOME_COLL_PASSWORD)) {
					qtaOK++;
				}
				if (i == Constants.NUM_COLL_SCHEMA_AD && row[i].getContents().equalsIgnoreCase(Constants.NOME_COLL_SCHEMA_AD)) {
					qtaOK++;
				}
				if (i == Constants.NUM_COLL_PASSWORD_AD && row[i].getContents().equalsIgnoreCase(Constants.NOME_COLL_PASSWORD_AD)) {
					qtaOK++;
				}
				if (i == Constants.NUM_COLL_DBNAME && row[i].getContents().equalsIgnoreCase(Constants.NOME_COLL_DBNAME)) {
					qtaOK++;
				}
				if (i == Constants.NUM_COLL_PORT && row[i].getContents().equalsIgnoreCase(Constants.NOME_COLL_PORT)) {
					qtaOK++;
				}
				if (i == Constants.NUM_COLL_SERVER && row[i].getContents().equalsIgnoreCase(Constants.NOME_COLL_SERVER)) {
					qtaOK++;
				}
			}
		}
		if (qtaOK == row.length) {
			return true;
		}
		return false;
	}

}
