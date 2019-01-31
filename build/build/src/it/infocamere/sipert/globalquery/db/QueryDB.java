package it.infocamere.sipert.globalquery.db;

public class QueryDB {
	
	private String descrizione;
	private String query;
	private String autore;
	
	public QueryDB() {
		super();
	}
	
	public QueryDB(String descrizione, String query, String autore) {
		super();
		this.descrizione = descrizione;
		this.query = query;
		this.autore = autore;
	}
	
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getAutore() {
		return autore;
	}
	public void setAutore(String autore) {
		this.autore = autore;
	}

	@Override
	public String toString() {
		return "QueryDB [descrizione=" + descrizione + ", query=" + query + ", autore=" + autore + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((autore == null) ? 0 : autore.hashCode());
		result = prime * result + ((descrizione == null) ? 0 : descrizione.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QueryDB other = (QueryDB) obj;
		if (autore == null) {
			if (other.autore != null)
				return false;
		} else if (!autore.equals(other.autore))
			return false;
		if (descrizione == null) {
			if (other.descrizione != null)
				return false;
		} else if (!descrizione.equals(other.descrizione))
			return false;
		return true;
	}

	
	
}
