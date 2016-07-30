package com.getusroi.ui.dynastore.config.impl;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;

import play.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.getusroi.config.RequestContext;
import com.getusroi.config.persistence.ConfigPersistenceException;
import com.getusroi.dynastore.config.DynaStoreConfigurationConstant;
import com.getusroi.dynastore.config.DynaStoreConfigurationException;
import com.getusroi.dynastore.config.IDynaStoreConfigurationService;
import com.getusroi.dynastore.config.impl.DynaStoreConfigurationService;
import com.getusroi.ui.dynastore.config.IDynaStoreConfigService;
import com.getusroi.ui.treenode.INodeService;
import com.getusroi.ui.treenode.impl.NodeServiceImpl;

public class DynaStoreConfigService implements IDynaStoreConfigService {
	static INodeService nodeService = new NodeServiceImpl();
	
	IDynaStoreConfigurationService iDynaStoreConfigurationService=new DynaStoreConfigurationService();

	@Override
	public JSONArray getDynaStoreConfig(int nodeId) {

		JSONArray configNodeDatas = new JSONArray();
		Logger.debug("inside getEventConfig with nodeId " + nodeId);
		try {
			configNodeDatas = nodeService.getConfigData(nodeId, DynaStoreConfigurationConstant.DYNASTORE_CONFIG_TYPE);
			Logger.debug("configNode data size " + configNodeDatas.length());
		} catch (JsonProcessingException | ConfigPersistenceException
				| JSONException e) {
			Logger.error("json processing exception or Config Pressistance Exception "
					+ e);
		}
		return configNodeDatas;
	}


	@Override
	public boolean changeStatusDynaStoreConfig(
			RequestContext dyContext, String configName,
			String version, boolean isEnable) throws DynaStoreConfigurationException {

		return iDynaStoreConfigurationService.changeStatusOfDynaStoreConfiguration(dyContext, configName, version, isEnable);
		
		
	}


	@Override
	public boolean deleteDynaStoreConfiguration(
			RequestContext dyContext, String configName,
			String version) throws DynaStoreConfigurationException {
		return iDynaStoreConfigurationService.deleteDynaStoreConfiguration(dyContext, configName, version);
	}

}
