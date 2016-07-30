package com.getusroi.ui.eventconfig.impl;

import org.json.JSONArray;
import org.json.JSONException;

import play.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.getusroi.config.ConfigurationContext;
import com.getusroi.config.persistence.IConfigPersistenceService;
import com.getusroi.config.persistence.impl.ConfigPersistenceServiceMySqlImpl;
import com.getusroi.eventframework.config.EventFrameworkConfigurationException;
import com.getusroi.eventframework.config.EventFrameworkConstants;
import com.getusroi.eventframework.config.IEventFrameworkConfigService;
import com.getusroi.eventframework.config.impl.EventFrameworkConfigService;
import com.getusroi.ui.eventconfig.IEventConfigService;

public class EventConfigServiceImpl implements IEventConfigService {

	IConfigPersistenceService iConfigPersistenceService = new ConfigPersistenceServiceMySqlImpl();
	ObjectMapper objectMapper = new ObjectMapper();
	IEventFrameworkConfigService iEventFrameworkConfigService=new EventFrameworkConfigService();

	@Override
	public JSONArray getEventConfigarations(int nodeId, String configType) {
		JSONArray jsonArrayOfEventsConifg = new JSONArray();
		try {
			jsonArrayOfEventsConifg=new JSONArray(jsonArrayOfEventsConifg);
		} catch ( JSONException e) {
			Logger.error("error getting eventconfigrations by nodeId=" + nodeId
					+ " configType=" + configType);
		}
		return jsonArrayOfEventsConifg;
	}

	@Override
	public boolean changeStatusOfEventFrameworkConfiguration(
			ConfigurationContext configurationContext, String eventId,boolean isEnable,
			String eventType) throws EventFrameworkConfigurationException {
		Logger.debug("inside changeStatusOfEventFrameworkConfiguration method with configurationContext="+configurationContext +" eventId ="+eventId  +" isEnable ="+isEnable +" eventType="+eventType);
		boolean isUpdated=false;
		if(eventType.equalsIgnoreCase(EventFrameworkConstants.EF_DISPATCHCHANEL_CONFIG_TYPE)){
			isUpdated=iEventFrameworkConfigService.changeStatusOfDispactherChanelConfiguration(configurationContext, eventId, isEnable);
		}else if(eventType.equalsIgnoreCase(EventFrameworkConstants.EF_SYSEVENT_CONFIG_TYPE)){
			
			isUpdated=iEventFrameworkConfigService.changeStatusOfSystemEventConfiguration(configurationContext, eventId, isEnable);
		}else if(eventType.equalsIgnoreCase(EventFrameworkConstants.EF_EVENT_CONFIG_TYPE)){
			isUpdated=iEventFrameworkConfigService.changeStatusOfEventConfiguration(configurationContext, eventId, isEnable);
		}else if(eventType.equalsIgnoreCase(EventFrameworkConstants.EF_EVENTSUBSCRIPTION_CONFIG_TYPE)){
			isUpdated=iEventFrameworkConfigService.changeStatusOfEventSubscriptionConfiguration(configurationContext, eventId, isEnable);

		}
		
		return isUpdated;
	}

	@Override
	public boolean deleteEventFrameworkConfiguration(
			ConfigurationContext configurationContext, String eventId,
			String eventType) throws EventFrameworkConfigurationException {
		boolean isDeleted=false;
		
		if(eventType.equalsIgnoreCase(EventFrameworkConstants.EF_DISPATCHCHANEL_CONFIG_TYPE)){
			isDeleted=iEventFrameworkConfigService.deleteDipatcherChanelConfiguration(configurationContext, eventId);
		}else if(eventType.equalsIgnoreCase(EventFrameworkConstants.EF_SYSEVENT_CONFIG_TYPE)){
			
			isDeleted=iEventFrameworkConfigService.deleteSystemEventConfiguration(configurationContext, eventId);
		}else if(eventType.equalsIgnoreCase(EventFrameworkConstants.EF_EVENT_CONFIG_TYPE)){
			isDeleted=iEventFrameworkConfigService.deleteEventConfiguration(configurationContext, eventId);
		}
		return isDeleted;
	}
	
	

}
