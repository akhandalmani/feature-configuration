package com.getusroi.ui.eventconfig;

import org.json.JSONArray;

import com.getusroi.config.ConfigurationContext;
import com.getusroi.eventframework.config.EventFrameworkConfigurationException;

public interface IEventConfigService {
	
	
	
	public JSONArray getEventConfigarations(int nodeId,String configType);
	
	public boolean changeStatusOfEventFrameworkConfiguration(ConfigurationContext configurationContext,String eventId,boolean isEnable,String eventType)throws EventFrameworkConfigurationException ;
	
	public boolean deleteEventFrameworkConfiguration(ConfigurationContext configurationContext,String eventId,String eventType) throws EventFrameworkConfigurationException ;
	

}
