package properties;

public enum PropertyType {
	LUCENE("resources/lucene"),
	SETTINGS("resources/settings"),
	RELATEDCITATION("resources/relatedCitation");
	
	private String propertyFileName;
	
	PropertyType(String propertyFileName){
		this.propertyFileName = propertyFileName;
	}
	
	public String getPropertyFilePath(){
		return propertyFileName;
	}
	
	@Override
	public String toString(){
		return propertyFileName;
	}
}
