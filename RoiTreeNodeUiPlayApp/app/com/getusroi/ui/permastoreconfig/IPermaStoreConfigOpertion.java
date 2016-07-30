package com.getusroi.ui.permastoreconfig;

import com.getusroi.config.ConfigurationContext;
import com.getusroi.config.persistence.ConfigPersistenceException;
import com.getusroi.permastore.config.PermaStoreConfigParserException;
import com.getusroi.permastore.config.PermaStoreConfigurationException;
import com.getusroi.permastore.config.jaxb.PermaStoreConfiguration;

public interface IPermaStoreConfigOpertion {
	
	public PermaStoreConfiguration checkPermastoreBuilderType(String eventName,
			String feature, String feartureGroup, String configName,
			String builderType, String isEnabled, String dataType,
			String[] publishPermaStoreEvents,String inlineBuilderType,String inlineJson);
	
	public void addPermastoreData(ConfigurationContext context,
			PermaStoreConfiguration permaStoreConfiguration)
			throws PermaStoreConfigurationException,
			PermaStoreConfigParserException;
	
	void changeStatusOfPermaStoreConfig(ConfigurationContext configurationContext,
			String configName,boolean isEnable)throws PermaStoreConfigurationException;
	
	public void updatePermastoreData(ConfigurationContext configurationContext,
			PermaStoreConfiguration permaStoreConfiguration,
			int confignodedataId) throws PermaStoreConfigurationException,
			PermaStoreConfigParserException;
	
	public boolean deletePermastoreConfig(ConfigurationContext context,
			String configName) throws ConfigPersistenceException;
}
