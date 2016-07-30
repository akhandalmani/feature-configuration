package com.getusroi.ui.dynastore.config;

import org.codehaus.jettison.json.JSONArray;

import com.getusroi.config.RequestContext;
import com.getusroi.dynastore.config.DynaStoreConfigurationException;


public interface IDynaStoreConfigService {
	
	public JSONArray getDynaStoreConfig(int nodeId);
	public boolean changeStatusDynaStoreConfig(RequestContext dyContext,String configName,String version,boolean isEnable)  throws DynaStoreConfigurationException;
	public boolean deleteDynaStoreConfiguration(RequestContext dyContext,String configName,String version)  throws DynaStoreConfigurationException;

}
