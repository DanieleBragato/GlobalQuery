package it.infocamere.sipert.globalquery.db.dto;

public class SchemaDTO {

	private String schemaUserName;
    private String password;
    
	private String schemaAdUserName;
    private String passwordAd;

    private String dbName;
    
	private String port;
    
	private String hostServerURL;

	
	public SchemaDTO(String schemaUserName, String password, String schemaAdUserName, String passwordAd, String dbName,
			String port, String hostServerURL) {
		super();
		this.schemaUserName = schemaUserName;
		this.password = password;
		this.schemaAdUserName = schemaAdUserName;
		this.passwordAd = passwordAd;
		this.dbName = dbName;
		this.port = port;
		this.hostServerURL = hostServerURL;
	}

	public SchemaDTO() {
		super();
	}

	public String getSchemaUserName() {
		return schemaUserName;
	}

	public void setSchemaUserName(String schemaUserName) {
		this.schemaUserName = schemaUserName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSchemaAdUserName() {
		return schemaAdUserName;
	}

	public void setSchemaAdUserName(String schemaAdUserName) {
		this.schemaAdUserName = schemaAdUserName;
	}

	public String getPasswordAd() {
		return passwordAd;
	}

	public void setPasswordAd(String passwordAd) {
		this.passwordAd = passwordAd;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getHostServerURL() {
		return hostServerURL;
	}

	public void setHostServerURL(String hostServerURL) {
		this.hostServerURL = hostServerURL;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dbName == null) ? 0 : dbName.hashCode());
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
		SchemaDTO other = (SchemaDTO) obj;
		if (dbName == null) {
			if (other.dbName != null)
				return false;
		} else if (!dbName.equals(other.dbName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SchemaDTO [schemaUserName=" + schemaUserName + ", password=" + password + ", schemaAdUserName="
				+ schemaAdUserName + ", passwordAd=" + passwordAd + ", dbName=" + dbName + ", port=" + port
				+ ", hostServerURL=" + hostServerURL + "]";
	}	
	
	
	
}
