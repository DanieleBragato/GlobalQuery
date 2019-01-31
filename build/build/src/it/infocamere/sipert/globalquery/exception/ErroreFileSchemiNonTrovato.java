package it.infocamere.sipert.globalquery.exception;

public class ErroreFileSchemiNonTrovato extends Exception {

	public ErroreFileSchemiNonTrovato() {
		super("Attenzione, non trovato file excel relativo agli schemi dei data base oracle di Sipert");
	}
}
