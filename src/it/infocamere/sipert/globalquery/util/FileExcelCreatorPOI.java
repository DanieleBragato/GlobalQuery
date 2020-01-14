package it.infocamere.sipert.globalquery.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import it.infocamere.sipert.globalquery.db.QueryDB;
import it.infocamere.sipert.globalquery.db.dto.GenericResultsDTO;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class FileExcelCreatorPOI {
	
	private static Logger LOGGER = Logger.getLogger(FileExcelCreator.class.getName());
	
	private static CellStyle dateCellStyle;
	
	public static boolean writeFileExcelOfResults(String fileName, List<GenericResultsDTO> results, QueryDB queryDB) {
		
		LOGGER.debug("writeFileExcelOfResults");
        
		//Blank workbook
        XSSFWorkbook xssfworkbook = new XSSFWorkbook();
        FileOutputStream fileOutStream = null;
        
        dateCellStyle = xssfworkbook.createCellStyle();
    	CreationHelper dateStyleHelper = xssfworkbook.getCreationHelper();
    	dateCellStyle.setDataFormat(dateStyleHelper.createDataFormat().getFormat("m/d/yy"));        
        
        try {

        	fileOutStream = new FileOutputStream(fileName);
        	
            // CREO I 2 FOGLI: Dati Estratti e Query
        	Sheet xssfsheetDatiEstratti = xssfworkbook.createSheet("Dati Estratti");        				
        	Sheet xssfsheetQuery = xssfworkbook.createSheet("Query");
        	// CREO LA PRIMA RIGA DEL FOGLIO DELLA QUERY: servirà per valorizzare la query usata in fase di estrazione dati da data-base
        	Row xssfQueryRowTitoli = xssfsheetQuery.createRow(Constants.RIGA_ZERO);
        	// CREO UNA CELLA CHE VALORIZZO CON LA QUERY USATA 
        	Cell xssfCellQuery = xssfQueryRowTitoli.createCell(Constants.COLONNA_ZERO);
        	xssfCellQuery.setCellValue(queryDB.getQuery());
        	// CREO LA RIGA DEI TITOLI DEI DATI ESTRATTI
        	Row xssfDatiEstrattiRowTitoli = xssfsheetDatiEstratti.createRow(Constants.RIGA_ZERO);
            
            int iRiga = 1;	
            for (GenericResultsDTO resultsDTO : results) {
            	// risultati di un singolo schema dentro resultsDTO 
            	//System.out.println("Schema = " + resultsDTO.getSchema());
            	
            	for (LinkedHashMap<String, Object> map : resultsDTO.getListLinkedHashMap()) {
            		
                	// CREO RIGA RISULTATI
                	Row xssfDatiEstrattiRow = xssfsheetDatiEstratti.createRow(iRiga);
            		
            		Set entrySet = map.entrySet();
            		Iterator it = entrySet.iterator();
        			//System.out.println("riga = " + iRiga);
            		if (iRiga == 1) {
        				//  inserimento del nome della prima colonna (Schema)    
            			addCellValueXssf(xssfDatiEstrattiRowTitoli, Constants.COLONNA_ZERO, Constants.SCHEMA);
            			addCellValueXssf(xssfDatiEstrattiRow, Constants.COLONNA_ZERO, resultsDTO.getSchema());
            		} else {
        				//  inserimento del valore della colonna (Schema)
            			addCellValueXssf(xssfDatiEstrattiRow, Constants.COLONNA_ZERO, resultsDTO.getSchema());
            		}
        			int iColonna = 1;
            		while(it.hasNext()){
            			//System.out.println("colonna = " + iColonna);
            			Map.Entry me = (Map.Entry)it.next();
            			//System.out.println("me.getKey() = " + me.getKey());
            			//System.out.println("me.getValue() = " + me.getValue());
            			if (iRiga == 1) {
            				//  inserimento dei nomi delle colonne
            				if (me.getKey() instanceof String) {
            		            addCellValueXssf(xssfDatiEstrattiRowTitoli, iColonna, me.getKey());
                				addCellValueXssf(xssfDatiEstrattiRow, iColonna, me.getValue());
            				} else {
            					// TODO  gestire l'eventuale assenza del tipo nome colonna diverso da String 
            				}
            			} else {
            				//  inserimento dei valori delle colonne
            				addCellValueXssf(xssfDatiEstrattiRow, iColonna, me.getValue());
            			}
            			iColonna++;
            		}
            		iRiga++;
            	}
            }
            
            
            xssfworkbook.write(fileOutStream);

        } catch (IOException e1) {
            throw new RuntimeException(e1.toString(), e1);
        } finally {

        	if (xssfworkbook != null) {
                	try {
						xssfworkbook.close();
						if (fileOutStream != null) {
							fileOutStream.flush();
							fileOutStream.close();
						}
						return true;
					} catch (IOException e) {
						throw new RuntimeException(e.toString(), e);
					}
        	}
        }
		
		return false;
	}

	private static void addCellValue(WritableSheet excelSheet, int colonna, int riga, Object obj) {
		
		try {
			if (obj instanceof java.util.Date) {
				jxl.write.DateTime dateTime = new jxl.write.DateTime(colonna, riga, (java.util.Date) obj);
				excelSheet.addCell(dateTime);
			}
			if (obj instanceof java.lang.Boolean) {
				jxl.write.Boolean b = new jxl.write.Boolean(colonna, riga, (java.lang.Boolean) obj);
				excelSheet.addCell(b);
			}
			if (obj instanceof String) {
				Label label = new Label(colonna, riga, (String) obj);
				excelSheet.addCell(label);
			}
			if (obj instanceof java.lang.Double) {
				Number number = new Number(colonna, riga, (java.lang.Double) obj);
				excelSheet.addCell(number);
			}
			if (obj instanceof java.math.BigDecimal) {
				BigDecimal bd = (BigDecimal) obj;
				Number number = new Number(colonna, riga, bd.doubleValue());
				excelSheet.addCell(number);
			}
		} catch (RowsExceededException e) {
			LOGGER.debug("addCellValue RowsExceededException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			LOGGER.debug("addCellValue WriteException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private static void addCellValueXssf(Row row, int colonna, Object o) {
		
		Cell cell = row.createCell(colonna);
		
		if (o == null) {
		    
		    cell.setCellValue(new XSSFRichTextString());
		    
		} else if (o instanceof Number) {
		    
		    cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
		    cell.setCellValue(new Double(o.toString()));
		    
		} else if (o instanceof Date) {
		    
		    cell.setCellValue(new Date());
		    cell.setCellStyle(dateCellStyle);
		    cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
		    cell.setCellValue((Date)o);
		    
		} else if (o instanceof Boolean) {
		    
		    cell.setCellType(XSSFCell.CELL_TYPE_BOOLEAN);
		    cell.setCellValue(new Boolean((Boolean) o));
		    
		} else {
		    
		    cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		    cell.setCellValue(new XSSFRichTextString(o.toString()));
		}
		
	}

}	
