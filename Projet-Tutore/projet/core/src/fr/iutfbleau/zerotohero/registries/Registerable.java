package fr.iutfbleau.zerotohero.registries;

public interface Registerable<PropertiesType extends Properties> {
	String getId();
	
	void setProperties(PropertiesType properties);
	
	PropertiesType getProperties();
}
