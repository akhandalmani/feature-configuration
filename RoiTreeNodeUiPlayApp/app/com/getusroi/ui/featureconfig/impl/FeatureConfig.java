package com.getusroi.ui.featureconfig.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONArray;

import play.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.getusroi.config.ConfigurationContext;
import com.getusroi.config.persistence.ConfigPersistenceException;
import com.getusroi.config.persistence.IConfigPersistenceService;
import com.getusroi.config.persistence.impl.ConfigPersistenceServiceMySqlImpl;
import com.getusroi.feature.config.FeatureConfigRequestContext;
import com.getusroi.feature.config.FeatureConfigurationException;
import com.getusroi.feature.config.FeatureConfigurationUnit;
import com.getusroi.feature.config.IFeatureConfigurationService;
import com.getusroi.feature.config.impl.FeatureConfigurationService;
import com.getusroi.ui.featureconfig.IFeatureConfig;
import com.getusroi.ui.treenode.INodeService;
import com.getusroi.ui.treenode.impl.NodeServiceImpl;

public class FeatureConfig implements IFeatureConfig {

	static INodeService nodeService = new NodeServiceImpl();

	IFeatureConfigurationService ifeatureConfigurationService = new FeatureConfigurationService();
	ObjectMapper objectMapper = new ObjectMapper();
	static IConfigPersistenceService iConfigPersistenceService = new ConfigPersistenceServiceMySqlImpl();

	

	/*@Override
	public void addFeatureConfiguration(String tenantId, String siteId,
			String groupName, boolean isFeatureEnabled, String featureName,
			String featureVersion, boolean isStdImplEnabled, String vendName,
			String vendDescription, boolean isVendEnabled)
			throws FeatureConfigurationException {
		Logger.debug("inside add feature configuration method");
		Feature feature2 = new Feature();
		feature2.setEnabled(isFeatureEnabled);
		feature2.setFeatureName(featureName);
		feature2.setVersion(featureVersion);

		StandardImplementation standardImplementation = new StandardImplementation();
		standardImplementation.setEnabled(isStdImplEnabled);
		StandardService standardService = new StandardService();
		standardService.setName("create");
		standardService.setValue("Draft-CreateRoute");
		standardImplementation.getStandardService().add(standardService);// c
																			// this
		feature2.setStandardImplementation(standardImplementation);

		VendorImplementation vendorImplementation = new VendorImplementation();
		vendorImplementation.setName(vendName);
		vendorImplementation.setDescription(vendDescription);
		vendorImplementation.setEnabled(isVendEnabled);

		VendorService vs = new VendorService();
		vs.setName("create");

		vs.setValue("royalmail-draft-MoveRoute");

		vendorImplementation.getVendorService().add(vs);

		// Logger.debug("*-*-*-*--*-*-*-*-*"+vendName);
		feature2.getVendorImplementation().add(vendorImplementation); // c this

		// TODO Auto-generated method stub
		ifeatureConfigurationService.addFeatureConfiguration(tenantId, siteId,
				groupName, feature2);
	}
*/
	/**
	 * get FeatConfiguration based on Requestcontext and configname
	 */
	public FeatureConfigurationUnit getFeatureConfiguration(
			FeatureConfigRequestContext requestContext, String configName)
			throws FeatureConfigurationException {

		return	ifeatureConfigurationService.getFeatureConfiguration(requestContext,
				configName);
	}
	
	/**
	 * change status of featureconfig to enable or disable 
	 */
	public void changeStatusOfFeatureConfig(
			ConfigurationContext featureRequestContext,
			String featureName, boolean isConfigEnabled)
			throws FeatureConfigurationException {
		
		  Logger.debug("Inside FeatureConfigC" + "lass: "+isConfigEnabled);
		  Logger.debug("Inside FeatureConfigClass: "+featureName);
		 
		try {
			ifeatureConfigurationService.changeStatusOfFeatureConfig(
					featureRequestContext, featureName, isConfigEnabled);
		} catch (FeatureConfigurationException e) {
			Logger.error("Error in changing status: " + e);
		}
	
	}

/**
 * to delete FeatureConfiguration based on given NodeID and NodeDataID
 */
	public boolean deleteFeatureConfiguration(String nodeID, String NodeDataId,
			String featureName) throws FeatureConfigurationException,
			ConfigPersistenceException{
		ArrayList<String> listOfParentNodeName =nodeService.getParentNodeNames(nodeID);

		String featureGroup = listOfParentNodeName.get(0);
		String siteName = listOfParentNodeName.get(1);
		String tenantName = listOfParentNodeName.get(2);

		ConfigurationContext featureRequestContext = new ConfigurationContext(
				tenantName, siteName, featureGroup, featureName);

		// Logger.debug("inside deleteFeatureConfigData  inside with "+featureRequestContext);

		boolean deleteFeatureConfiguration = ifeatureConfigurationService
				.deleteFeatureConfiguration(featureRequestContext, featureName);

		if (deleteFeatureConfiguration == false) {
			int nodeId = Integer.parseInt(NodeDataId);
			Logger.info("Trying to delete the configuration in the node with featureconfig id: "
					+ nodeID);
			deleteFeatureConfiguration=	iConfigPersistenceService.deleteConfigNodeData(nodeId);
		}
		return deleteFeatureConfiguration;
	}

	@Override
	public String getFeatureConfigartionByNodeIdByType(int nodeIdint,
			String string) throws FeatureConfigurationException {

		Logger.debug("inside FeatureConfigClass: ");
		JSONArray jarray = new JSONArray();
		String featureConfigurationjson = "";
		try {
			List<com.getusroi.config.persistence.ConfigNodeData> configNodeData = new ConfigPersistenceServiceMySqlImpl()
					.getConfigNodeDataByNodeIdAndByType(nodeIdint, string);
		 Logger.info("Size of the vend list: "+configNodeData.size());

			featureConfigurationjson = objectMapper
					.writeValueAsString(configNodeData);
			Logger.debug("Feature Configuration :  "+featureConfigurationjson);
			jarray.put(featureConfigurationjson);

		} catch (com.getusroi.config.persistence.ConfigPersistenceException e) {

			Logger.error("ConfigPersistenceException: " + e);

		} catch (JsonProcessingException e) {
			Logger.error("JsonProcessingException: " + e);
		}

		return featureConfigurationjson;
	}

	/**
	 * change status of feature to enable /disable
	 */
	public void changeStatusOfFeatureService(
			ConfigurationContext featureRequestContext,
			String configName, Map<String, Boolean> statusMap
		)
			throws FeatureConfigurationException {

		Logger.debug("Inside changeStatusOfFeatureService method");
		;
		Logger.debug("configName  value " + configName.toString());
		ifeatureConfigurationService.changeStatusOfFeatureService(featureRequestContext, configName, statusMap);
		

	}
	
	
	


}
