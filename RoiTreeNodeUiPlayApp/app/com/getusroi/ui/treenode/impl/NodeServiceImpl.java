package com.getusroi.ui.treenode.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import play.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.getusroi.config.ConfigurationContext;
import com.getusroi.config.RequestContext;
import com.getusroi.config.persistence.ConfigNode;
import com.getusroi.config.persistence.ConfigNodeData;
import com.getusroi.config.persistence.ConfigPersistenceException;
import com.getusroi.config.persistence.IConfigPersistenceService;
import com.getusroi.config.persistence.impl.ConfigPersistenceServiceMySqlImpl;
import com.getusroi.feature.config.FeatureConfigurationException;
import com.getusroi.feature.config.impl.FeatureConfigurationService;
import com.getusroi.permastore.InvalidNodeTreeException;
import com.getusroi.permastore.config.IPermaStoreConfigurationService;
import com.getusroi.permastore.config.PermaStoreConfigParserException;
import com.getusroi.permastore.config.PermaStoreConfigRequestException;
import com.getusroi.permastore.config.PermaStoreConfigurationException;
import com.getusroi.permastore.config.PermaStoreConfigurationUnit;
import com.getusroi.permastore.config.impl.PermaStoreConfigurationService;
import com.getusroi.permastore.config.jaxb.PermaStoreConfigurations;
import com.getusroi.policy.config.IPolicyConfigurationService;
import com.getusroi.policy.config.PolicyConfigXMLParserException;
import com.getusroi.policy.config.PolicyConfigurationException;
import com.getusroi.policy.config.impl.PolicyConfigurationService;
import com.getusroi.ui.featureconfig.impl.FeatureConstants;
import com.getusroi.ui.permastoreconfig.PermaStoreBuilderConstant;
import com.getusroi.ui.treenode.INodeService;

public class NodeServiceImpl implements INodeService {

	static IConfigPersistenceService iConfigPersistenceService = new ConfigPersistenceServiceMySqlImpl();
	FeatureConfigurationService feIFeatureStoreConfigurationService = new FeatureConfigurationService();

	IPolicyConfigurationService iPolicyConfigurationService = new PolicyConfigurationService();
	
	IPermaStoreConfigurationService iPermaStoreConfigurationService = new PermaStoreConfigurationService();
	ObjectMapper objectmapper = new ObjectMapper();

	private void addTenantNodes(String[] tenants, int parentNodeId)
			throws ConfigPersistenceException {

		for (int i = 0; i < tenants.length; i++) {
			// Logger.debug("Tenant Name : " + tenants[i]);

			ConfigNode parentNode = new ConfigNode();
			parentNode.setType("tenant");
			parentNode.setRoot(false);
			parentNode.setNodeName(tenants[i].toLowerCase());
			parentNode.setHasChildren(false);
			parentNode.setParentNodeId(parentNodeId);
			parentNode.setDescription("This is  " + tenants[i] + " tenant");
			parentNode.setLevel(1);
			iConfigPersistenceService.insertConfigNode(parentNode);

		}

	}

	private void addSiteNodes(String[] sites, int parentNodeId)
			throws ConfigPersistenceException {

		for (int i = 0; i < sites.length; i++) {
			Logger.debug("Site Name : " + sites[i]);
			ConfigNode siteNode = new ConfigNode();
			siteNode.setType("site");
			siteNode.setRoot(false);
			siteNode.setNodeName(sites[i].toLowerCase());
			siteNode.setHasChildren(false);
			siteNode.setParentNodeId(parentNodeId);
			siteNode.setDescription("This is  " + sites[i] + " Site");
			siteNode.setLevel(2);
			iConfigPersistenceService.insertConfigNode(siteNode);

		}

	}

	private void addFeatureGroupNodes(String[] featuresGroup, int parentNodeId)
			throws ConfigPersistenceException {

		for (int i = 0; i < featuresGroup.length; i++) {
			Logger.debug("feature group Name : " + featuresGroup[i]);
			ConfigNode featureGroupNode = new ConfigNode();
			featureGroupNode.setType("feature_group");
			featureGroupNode.setRoot(false);
			featureGroupNode.setNodeName(featuresGroup[i].toLowerCase());
			featureGroupNode.setHasChildren(false);
			featureGroupNode.setParentNodeId(parentNodeId);
			featureGroupNode.setDescription("This is  " + featuresGroup[i]
					+ " Feature Group");
			featureGroupNode.setLevel(3);
			iConfigPersistenceService.insertConfigNode(featureGroupNode);

		}

	}

	private void addFeatureNodes(String[] features, int parentNodeId)
			throws ConfigPersistenceException {

		for (int i = 0; i < features.length; i++) {
			Logger.debug("feature Name : " + features[i]);
			ConfigNode featureNode = new ConfigNode();
			featureNode.setType("feature");
			featureNode.setRoot(false);
			featureNode.setNodeName(features[i].toLowerCase());
			featureNode.setHasChildren(false);
			featureNode.setParentNodeId(parentNodeId);
			featureNode.setDescription("This is  " + features[i] + " Feature");
			featureNode.setLevel(4);
			iConfigPersistenceService.insertConfigNode(featureNode);

		}

	}

	public void addPolicyGroupNodes(String[] policyGroup, int parentNodeId)
			throws ConfigPersistenceException {

		for (int i = 0; i < policyGroup.length; i++) {
			Logger.debug("policy Group Name : " + policyGroup[i]);
			ConfigNode policyGroupNode = new ConfigNode();
			policyGroupNode.setType("policy_group");
			policyGroupNode.setRoot(false);
			policyGroupNode.setNodeName(policyGroup[i].toLowerCase());
			policyGroupNode.setHasChildren(false);
			policyGroupNode.setParentNodeId(parentNodeId);
			policyGroupNode.setDescription("This is  " + policyGroup[i]
					+ " policyGroup");
			policyGroupNode.setLevel(5);
			iConfigPersistenceService.insertConfigNode(policyGroupNode);

		}

	}

	public void addPolicyNodes(String[] policy, int parentNodeId)
			throws ConfigPersistenceException {

		for (int i = 0; i < policy.length; i++) {
			Logger.debug("policy Group Name : " + policy[i]);
			ConfigNode policyGroupNode = new ConfigNode();
			policyGroupNode.setType("policy");
			policyGroupNode.setRoot(false);
			policyGroupNode.setNodeName(policy[i].toLowerCase());
			policyGroupNode.setHasChildren(false);
			policyGroupNode.setParentNodeId(parentNodeId);
			policyGroupNode.setDescription("This is  " + policy[i]
					+ " policy  ");
			policyGroupNode.setLevel(6);
			iConfigPersistenceService.insertConfigNode(policyGroupNode);

		}

	}

	/*public void addPermastoreData(String tenantId, String siteId,
			PermaStoreConfiguration permaStoreConfiguration)
			throws PermaStoreConfigurationException,
			PermaStoreConfigParserException {
		ConfigurationContext context=new ConfigurationContext(tenantId,siteId);
		iPermaStoreConfigurationService.addPermaStoreConfiguration(context, permaStoreConfiguration);
	}
*/
	// get node data by Node id to edit data
	public ConfigNode getNodeDataToEditService(int nodeId)
			throws ConfigPersistenceException {

		ConfigNode configNode = iConfigPersistenceService.getNodeById(nodeId);

		return configNode;
	}

	public int updateNodeByName(int nodeId, String nodeName)
			throws SQLException {

		int sucess = 0;
		// sucess = iConfigPersistenceService.updateNodeByName(nodeId,
		// nodeName);

		return sucess;

	}

	// delete node permastore
	public void deleteNodeByNodeId(int nodeId, String type, String nodeName)
			throws ConfigPersistenceException, FeatureConfigurationException {

		deleteConfigrations(nodeId, type, nodeName);
		iConfigPersistenceService.deleteNodeByNodeId(nodeId);

	}

	private void deleteConfigrations(int nodeId, String type, String nodeName)
			throws ConfigPersistenceException, FeatureConfigurationException {
		ArrayList<ConfigNodeData> listofConfigaration = null;
		if (type != null
				& type.equalsIgnoreCase(NodeConstants.FEATURE_NODE_TYPE)
				|| type.equalsIgnoreCase(NodeConstants.FEATURE_GROUP_NODE_TYPE)) {
			listofConfigaration = (ArrayList<ConfigNodeData>) iConfigPersistenceService
					.getConfigNodeDataByNodeId(nodeId);
		}
		if (listofConfigaration != null) {
			for (Iterator<ConfigNodeData> iterator = listofConfigaration.iterator(); iterator
					.hasNext();) {
				ConfigNodeData configNodeData = (ConfigNodeData) iterator
						.next();
				if (configNodeData != null
						&& configNodeData.getConfigType() != null
						&& configNodeData.getConfigType().equalsIgnoreCase(
								PermaStoreBuilderConstant.TYPE)) {
					deleteNodeByNodeIdPermastore(nodeName, nodeId + "",
							configNodeData.getConfigName());
				} else if (configNodeData != null
						&& configNodeData.getConfigType() != null
						&& configNodeData.getConfigType().equalsIgnoreCase(
								FeatureConstants.FEATURE_CONFIG_TYPE)) {
					deleteFeatureConfigData(nodeId + "",
							configNodeData.getNodeDataId() + "",
							configNodeData.getConfigName());
				}

			}
		}

	}

	// check the node name exist or not before adding
	public int getNodeIdByNameAndByType(String nodeName, String type,
			int parentId) throws ConfigPersistenceException {
		int nodeId = 0;
		nodeId = iConfigPersistenceService.getNodeIdByNodeNameAndByType(
				nodeName, type, parentId);

		return nodeId;
	}

	// to get nodename,sitename,featuregroup and parentid for permostore add
	// method
	public ConfigNode getNodeData(int nodeId) throws ConfigPersistenceException {
		ConfigNode configNode = iConfigPersistenceService.getNodeById(nodeId);
		return configNode;
	}

	// TODO add defualt folder stracture
	public void createDefaultTenantNodeStracture(String[] nodeArray,
			int parentNodeId) throws ConfigPersistenceException {

		for (int i = 0; i < nodeArray.length; i++) {
			// Logger.debug("Tenant Name : " + nodeArray[i]);
			ConfigNode parentNode = new ConfigNode();
			parentNode.setType("tenant");
			parentNode.setRoot(false);
			parentNode.setNodeName(nodeArray[i]);
			parentNode.setHasChildren(true);
			parentNode.setParentNodeId(parentNodeId);
			parentNode.setDescription("This is  " + nodeArray[i] + " tenant");
			parentNode.setLevel(1);
			iConfigPersistenceService.insertConfigNode(parentNode);

			ConfigNode siteNode = new ConfigNode();
			siteNode.setType("site");
			siteNode.setRoot(false);
			siteNode.setNodeName("Site1");
			siteNode.setHasChildren(true);
			siteNode.setParentNodeId(parentNodeId);
			siteNode.setDescription("This is  sit1 Site");
			siteNode.setLevel(2);
			iConfigPersistenceService.insertConfigNode(siteNode);

			ConfigNode featureNode = new ConfigNode();
			featureNode.setType("feature");
			featureNode.setRoot(false);
			featureNode.setNodeName("PRINTING1");
			featureNode.setHasChildren(true);
			featureNode.setParentNodeId(parentNodeId);
			featureNode.setDescription("This is  PRINTING1  feature");
			featureNode.setLevel(3);
			iConfigPersistenceService.insertConfigNode(featureNode);

			ConfigNode policyGroupNode = new ConfigNode();
			policyGroupNode.setType("policy_group");
			policyGroupNode.setRoot(false);
			policyGroupNode.setNodeName("PrintingPolicy1");
			policyGroupNode.setHasChildren(true);
			policyGroupNode.setParentNodeId(parentNodeId);
			policyGroupNode
					.setDescription("This is  PrintingPolicy1  policy_group");
			policyGroupNode.setLevel(4);
			iConfigPersistenceService.insertConfigNode(featureNode);

		}

	}

	/**
	 * getCondifgData details based on given Node ID and type OF Config 
	 */
	public JSONArray getConfigData(int nodeId, String typeOfConfig)
			throws ConfigPersistenceException, JsonProcessingException,
			JSONException {

		Logger.debug("inside getConfigData method with nodeId : " + nodeId
				+ " configType " + typeOfConfig);
		ArrayList<ConfigNodeData> configNodeData = (ArrayList<ConfigNodeData>) ((ConfigPersistenceServiceMySqlImpl) iConfigPersistenceService)
				.getConfigNodeDataByNodeIdAndByType(nodeId, typeOfConfig);
	

		JSONArray configDataJsonArray = new JSONArray();
		for (Iterator<ConfigNodeData> iterator = configNodeData.iterator(); iterator.hasNext();) {
			ConfigNodeData configNodeData2 = (ConfigNodeData) iterator.next();
			JSONObject jsonConfigdata = new JSONObject(
					objectmapper.writeValueAsString(configNodeData2));
			configDataJsonArray.put(jsonConfigdata);
		}
		return configDataJsonArray;
	}



	// check the node name exist with same name in the same level excluding the
	// updatingid

	public int getNodeIdByNameAndByTypeNotWithUpdatingID(String nodeName,
			String type, int parentId, int updatingNodeId) throws SQLException {
		int nodeId = 0;

		return nodeId;
	}

	public boolean addNode(String nodeType, String[] nodeArray,
			int parentNodeidInt) {
		boolean sucess = false;
		switch (nodeType) {
		case "Add Tenants":
			try {
				addTenantNodes(nodeArray, parentNodeidInt);
				sucess = true;
			} catch (ConfigPersistenceException e) {
				Logger.error("error in adding tenant node " + e);
			}
			break;

		case "Add Sites":
			try {
				addSiteNodes(nodeArray, parentNodeidInt);
				sucess = true;

			} catch (ConfigPersistenceException e) {
				Logger.error("error in adding site node " + e);
			}
			break;
		case "Add FeatureGroup":
			try {
				addFeatureGroupNodes(nodeArray, parentNodeidInt);
				sucess = true;

			} catch (ConfigPersistenceException e) {

				Logger.error("error in adding featureGroup node " + e);
			}
			break;

		case "Add Feature":
			try {
				addFeatureNodes(nodeArray, parentNodeidInt);
				sucess = true;

			} catch (ConfigPersistenceException e) {
				Logger.error("error in adding feature node " + e);
			}
			break;

		case "Add PolicyGroup":
			try {
				addPolicyGroupNodes(nodeArray, parentNodeidInt);
				sucess = true;

			} catch (ConfigPersistenceException e) {
				Logger.error("error in adding PolicyGroup node " + e);
			}
			break;

		case "Add Policy":
			try {
				addPolicyNodes(nodeArray, parentNodeidInt);
				sucess = true;

			} catch (ConfigPersistenceException e) {

				Logger.error("error in adding Policy node " + e);
			}
			break;

		default:
			break;
		}

		return sucess;
	}

	

	public boolean checkNodeNameExist(String[] nodeArray, String nodeType,
			int parentNodeidInt) {

		boolean nodeNameExist = false;
		// check node already exsist with this name
		for (int i = 0; i < nodeArray.length; i++) {
			int nodeId = 0;

			if (nodeId != 0) {
				nodeNameExist = true;
				break;
			}
		}

		return nodeNameExist;
	}



	public ArrayList<String> getParentNodeNames(String featureNodeId)
			throws ConfigPersistenceException {
		int nodeId = 0;
		ArrayList<String> listOfParentNodeName = new ArrayList<String>();
		ConfigNode configNode = null;
		NodeServiceImpl nodeService = new NodeServiceImpl();
		try {
			nodeId = Integer.parseInt(featureNodeId);

		} catch (NumberFormatException e) {
			Logger.error("Error to parse feartureNodeID not a number " + e);
		}

		configNode = nodeService.getNodeData(nodeId);
		Logger.info("node data  " + configNode);
		if (configNode != null) {
			while (configNode.getParentNodeId() != 0) {
				try {
					configNode = nodeService.getNodeData(configNode
							.getParentNodeId());
					Logger.info("parent Node data " + configNode);
					listOfParentNodeName.add(configNode.getNodeName());

				} catch (NullPointerException e) {
					Logger.error("null pointer exception node name not found "
							+ e);
				}
			}
			return listOfParentNodeName;
		}
		return listOfParentNodeName;

	}
	
	
	public ArrayList<String> getParentNodeNames(int vendorId)
			throws ConfigPersistenceException {
	
		ArrayList<String> listOfParentNodeName = new ArrayList<String>();
		ConfigNode configNode = null;
		NodeServiceImpl nodeService = new NodeServiceImpl();
		

		configNode = nodeService.getNodeData(vendorId);
		Logger.info("node data  " + configNode);
		if (configNode != null) {
			while (configNode.getParentNodeId() != 0) {
				try {
					configNode = nodeService.getNodeData(configNode
							.getParentNodeId());
					Logger.info("parent Node data " + configNode);
					listOfParentNodeName.add(configNode.getNodeName());

				} catch (NullPointerException e) {
					Logger.error("null pointer exception node name not found "
							+ e);
				}
			}
			return listOfParentNodeName;
		}
		return listOfParentNodeName;

	}

	public boolean deleteNodeByNodeIdPermastore(String nodeName, String nodeId,
			String configName) throws ConfigPersistenceException {

		ArrayList<String> listOfParentNodeName = getParentNodeNames(nodeId);

		String featureGroup = listOfParentNodeName.get(0);
		String siteName = listOfParentNodeName.get(1);
		String tenantName = listOfParentNodeName.get(2);

		ConfigurationContext permaStoreRequestContext = new ConfigurationContext(
				tenantName, siteName, featureGroup, nodeName);
		boolean deletePermaStoreConfiguration =false;
		try {
			deletePermaStoreConfiguration= iPermaStoreConfigurationService
					.deletePermaStoreConfiguration(permaStoreRequestContext,
							configName);

		} catch (PermaStoreConfigurationException e) {
			Logger.error("Cache delete not succeded cause: " + e);
		}
		return deletePermaStoreConfiguration;
	}

	private void deleteFeatureConfigData(String nodeID, String NodeDataId,
			String featureName) throws FeatureConfigurationException,
			ConfigPersistenceException {

		ArrayList<String> listOfParentNodeName = getParentNodeNames(nodeID);

		String featureGroup = listOfParentNodeName.get(0);
		String siteName = listOfParentNodeName.get(1);
		String tenantName = listOfParentNodeName.get(2);

		ConfigurationContext featureRequestContext = new ConfigurationContext(
				tenantName, siteName, featureGroup, featureName);

		// Logger.debug("inside deleteFeatureConfigData  inside with "+featureRequestContext);

		boolean deleteFeatureConfiguration = feIFeatureStoreConfigurationService
				.deleteFeatureConfiguration(featureRequestContext, featureName);

		if (deleteFeatureConfiguration == false) {
			int nodeId = Integer.parseInt(NodeDataId);
			Logger.info("Trying to delete the configuration in the node with featureconfig id: "
					+ nodeID);
			iConfigPersistenceService.deleteConfigNodeData(nodeId);
		}

	}

	

	@Override
	public ArrayList<String> getNodeTypeAndNodeDataType(JsonNode jsonNode) {
		ArrayList<String> listOfType = new ArrayList<String>();

		String nodeType = "";
		String nodeTypeData = "";
		if (jsonNode.findPath("nodeTypeTenant").textValue() != null
				&& !jsonNode.findPath("nodeTypeTenant").textValue().isEmpty()) {
			nodeTypeData = jsonNode.findPath("nodeTypeTenant").textValue();
			nodeType = "tenant";

		} else if (jsonNode.findPath("nodeTypeSite").textValue() != null
				&& !jsonNode.findPath("nodeTypeSite").textValue().isEmpty()) {
			nodeTypeData = jsonNode.findPath("nodeTypeSite").textValue();
			nodeType = "site";

		} else if (jsonNode.findPath("nodeTypeFeatureGroup").textValue() != null
				&& !jsonNode.findPath("nodeTypeFeatureGroup").textValue().isEmpty()) {
			nodeTypeData = jsonNode.findPath("nodeTypeFeatureGroup").textValue();
			nodeType = "feature_group";

		}

		else if (jsonNode.findPath("nodeTypeFeature").textValue() != null
				&& !jsonNode.findPath("nodeTypeFeature").textValue().isEmpty()) {
			nodeTypeData = jsonNode.findPath("nodeTypeFeature").textValue();
			nodeType = "feature";

		} else if (jsonNode.findPath("nodeTypePolicyGroup").textValue() != null
				&& !jsonNode.findPath("nodeTypePolicyGroup").textValue().isEmpty()) {
			nodeType = jsonNode.findPath("nodeTypePolicyGroup").textValue();

		}

		else if (jsonNode.findPath("nodeTypePolicy").textValue() != null
				&& !jsonNode.findPath("nodeTypePolicy").textValue().isEmpty()) {
			nodeType = jsonNode.findPath("nodeTypePolicy").textValue();

		}
		listOfType.add(nodeType);
		listOfType.add(nodeTypeData);

		return listOfType;
	}

	public void changeStatusOfPermaStoreConfig(String tenantId,String siteId,String featureGroup,String featureName,
			String configName,boolean isEnable)throws PermaStoreConfigurationException {
		ConfigurationContext permaStoreRequestContext = new ConfigurationContext(tenantId, siteId, featureGroup, featureName);
		iPermaStoreConfigurationService.changeStatusOfPermaStoreConfig(permaStoreRequestContext, configName, isEnable);
	}
	
	
	public boolean changePolicyStatus(String tenantId, String siteId, String featureGroup, String featureName, String policyName, boolean isEnable) throws PolicyConfigurationException, PolicyConfigXMLParserException {
		
		ConfigurationContext policyRequestContext2 = new ConfigurationContext(tenantId, siteId, featureGroup, featureName);
		
		iPolicyConfigurationService.changePolicyStatus(policyRequestContext2, policyName, isEnable);
		
		return false;
	}
	public static void main(String[] args) throws ConfigPersistenceException {
	ArrayList<String> list=	new NodeServiceImpl().getParentNodeNames(857);
	System.out.println("List OF config Nodes "+list);
	}
	
}
