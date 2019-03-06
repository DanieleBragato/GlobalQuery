package it.infocamere.sipert.globalquery.util;


import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import it.infocamere.sipert.globalquery.db.QueryDB;
import it.infocamere.sipert.globalquery.db.dto.GenericResultsDTO;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class FileExcelCreator {

	private static Logger LOGGER = Logger.getLogger(FileExcelCreator.class.getName());
	
	public static boolean writeFileExcelOfResults(String fileName, List<GenericResultsDTO> results, QueryDB queryDB) {
		
		LOGGER.debug("writeFileExcelOfResults");
		
        WritableWorkbook wbook = null;
        
        try {

            wbook = Workbook.createWorkbook(new File(fileName));
            WritableSheet excelSheetDatiEstratti = wbook.createSheet("Dati Estratti", 0);
            
            WritableSheet excelSheetQuery = wbook.createSheet("Query", 1);
            Label labelQuery = new Label(Constants.COLONNA_ZERO, Constants.RIGA_ZERO, queryDB.getQuery());
            excelSheetQuery.addCell(labelQuery);
            

            int iRiga = 0;	
            for (GenericResultsDTO resultsDTO : results) {
            	// risultati di un singolo schema dentro resultsDTO 
            	//System.out.println("Schema = " + resultsDTO.getSchema());	

            	for (LinkedHashMap<String, Object> map : resultsDTO.getListLinkedHashMap()) {
                	if (iRiga == 1) iRiga++;
            		Set entrySet = map.entrySet();
            		Iterator it = entrySet.iterator();
        			//System.out.println("riga = " + iRiga);
            		if (iRiga == 0) {
        				//  inserimento del nome della prima colonna (Schema)    
            			Label label = new Label(Constants.COLONNA_ZERO, Constants.RIGA_ZERO, Constants.SCHEMA);
            			excelSheetDatiEstratti.addCell(label);
            			addCellValue(excelSheetDatiEstratti, Constants.COLONNA_ZERO, Constants.RIGA_ZERO + 1, resultsDTO.getSchema());
            		} else {
        				//  inserimento del valore della colonna (Schema)
            			addCellValue(excelSheetDatiEstratti, Constants.COLONNA_ZERO, iRiga, resultsDTO.getSchema());
            		}
        			int iColonna = 1;
            		while(it.hasNext()){
            			//System.out.println("colonna = " + iColonna);
            			Map.Entry me = (Map.Entry)it.next();
            			//System.out.println("me.getKey() = " + me.getKey());
            			//System.out.println("me.getValue() = " + me.getValue());
            			if (iRiga == 0) {
            				//  inserimento dei nomi delle colonne
            				if (me.getKey() instanceof String) {
            		            Label label = new Label(iColonna, iRiga, (String) me.getKey());
            		            excelSheetDatiEstratti.addCell(label);
            		            addCellValue(excelSheetDatiEstratti, iColonna, iRiga + 1, me.getValue());
            				} else {
            					// TODO  gestire l'eventuale assenza del tipo nome colonna diverso da String 
            				}
            			} else {
            				//  inserimento dei valori delle colonne
            				addCellValue(excelSheetDatiEstratti, iColonna, iRiga, me.getValue());
            			}
            			iColonna++;
            		}
            		iRiga++;
            	}
            }
            
            wbook.write();

        } catch (IOException e1) {
            throw new RuntimeException(e1.toString(), e1);
        } catch (WriteException e2) {
            throw new RuntimeException(e2.toString(), e2);
        } finally {

            if (wbook != null) {
                try {
                    wbook.close();
                    return true;
                } catch (IOException e3) {
                	throw new RuntimeException(e3.toString(), e3);
                } catch (WriteException e4) {
                	throw new RuntimeException(e4.toString(), e4);
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
	

	
}
