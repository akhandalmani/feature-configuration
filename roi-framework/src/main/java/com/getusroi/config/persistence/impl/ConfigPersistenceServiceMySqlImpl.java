package com.getusroi.config.persistence.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.getusroi.config.persistence.ConfigNode;
import com.getusroi.config.persistence.ConfigNodeData;
import com.getusroi.config.persistence.ConfigPersistenceException;
import com.getusroi.config.persistence.ConfigurationTreeNode;
import com.getusroi.config.persistence.IConfigPersistenceService;
import com.getusroi.config.persistence.ITenantConfigTreeService;
import com.getusroi.config.persistence.InvalidNodeTreeException;
import com.getusroi.config.persistence.dao.ConfigFeatureMasterDAO;
import com.getusroi.config.persistence.dao.ConfigNodeDAO;
import com.getusroi.config.persistence.dao.ConfigNodeDataDAO;

public  class ConfigPersistenceServiceMySqlImpl implements IConfigPersistenceService {
	static final Logger logger = LoggerFactory.getLogger(ConfigPersistenceServiceMySqlImpl.class);
	private ConfigNodeDAO configNodeDAO;
	private ConfigNodeDataDAO configDataDao;
	private ITenantConfigTreeService tenantConfigTreeService;
	private ConfigFeatureMasterDAO configFeatureMasterDao;
	
	public ConfigPersistenceServiceMySqlImpl(){
		 configNodeDAO=new ConfigNodeDAO();
		 configDataDao=new ConfigNodeDataDAO();
		 //change to singleton
		 //tenantConfigTreeService=new TenantConfigTreeServiceImpl();
		 tenantConfigTreeService=TenantConfigTreeServiceImpl.getTenantConfigTreeServiceImpl();
		 configFeatureMasterDao=new ConfigFeatureMasterDAO();
	}
	
	public ConfigurationTreeNode getConfigPolicyNodeTree() throws ConfigPersistenceException {
		logger.debug(".getConfigPolicyNodeTree method");
		ConfigurationTreeNode configTreeNode=tenantConfigTreeService.getAllConfigTreeNode();
		if(configTreeNode==null){
			logger.debug("Configuration tree Node is null");
			//DataGrid not initialized
			ConfigNodeDAO configNodeDAO = new ConfigNodeDAO();
			try {
				configTreeNode= configNodeDAO.getNodeTree();
				logger.debug("configTreeNode : "+configTreeNode);
				tenantConfigTreeService.initialize(configTreeNode);
			} catch (SQLException | IOException sqlexp) {
				throw new ConfigPersistenceException("Failed to get node tree",sqlexp);
			}
			
		}
		return configTreeNode;
	}

	
	public String getConfigPolicyNodeTreeAsJson() throws ConfigPersistenceException {
		ConfigurationTreeNode treeNode=getConfigPolicyNodeTree();
		logger.debug("inside getConfigPolicyNodeTreeAsJson :: "+treeNode);
		
		StringBuffer jsonBuffer = new StringBuffer();
		treeNode.getConfigTreeNodeAsJSONString(jsonBuffer);
		return jsonBuffer.toString();
	}

	public int insertConfigNode(ConfigNode node) throws ConfigPersistenceException {
		try{
			int genNodeId=configNodeDAO.insertConfigNode(node);
			node.setNodeId(genNodeId);
			ConfigurationTreeNode configTreeNode=buildConfigTreeNode(node);
			//Add to DataGrid Representation of this Tree
			tenantConfigTreeService.addConfigurationTreeNode(configTreeNode);
			logger.debug("insertConfigNode()--Generated NodeId is="+genNodeId);
			return genNodeId;
		} catch (SQLException | IOException sqlexp) {
			throw new ConfigPersistenceException("Failed to insert ConfigNode with ConfigNode="
					+ node, sqlexp);
		}
		
	}

	public List<ConfigNode> getChildNodes(Integer parentNodeId) throws ConfigPersistenceException {
		List<ConfigNode> childNodeList=null;
		try{
			configNodeDAO = new ConfigNodeDAO();
			childNodeList = configNodeDAO.getChildNodes(parentNodeId);
		} catch (SQLException | IOException sqlexp) {
			throw new ConfigPersistenceException("Failed to get Child Nodes parentNodeId="+ parentNodeId, sqlexp);
		}
		
		return childNodeList;
	}

	public ConfigNode getNodeById(Integer nodeId) throws ConfigPersistenceException {
		//configNodeDAO = new ConfigNodeDAO();
		ConfigNode configNode=null;
		try {
			configNode = configNodeDAO.getNodeById(nodeId);
		} catch (SQLException | IOException sqlexp) {

			throw new ConfigPersistenceException("Failed to get Node by Id="+ nodeId, sqlexp);
		}
		return configNode;
	}

	public boolean deleteNodeByNodeId(Integer nodeId) throws ConfigPersistenceException {
		//Delete the node Next
		try {
			ConfigNode configNode=configNodeDAO.getNodeById(nodeId);
			int numRows=configNodeDAO.deleteNodeByNodeId(nodeId);
			//Remove it from the cacahe
			ConfigurationTreeNode configTreeNode=buildConfigTreeNode(configNode);
			tenantConfigTreeService.deleteConfigurationTreeNode(configTreeNode);
			if(numRows>0)
				return true;
		} catch (SQLException | IOException sqlexp) {

			throw new ConfigPersistenceException("Failed to Delete ConfigNode by Id="+ nodeId, sqlexp);
		}
		return false;
	}

	private ConfigurationTreeNode buildConfigTreeNode(ConfigNode node){
		ConfigurationTreeNode nodeTree=new ConfigurationTreeNode();
		nodeTree.setNodeId(node.getNodeId());
		nodeTree.setLevel(node.getLevel());
		nodeTree.setNodeName(node.getNodeName());
		nodeTree.setParentNodeId(node.getParentNodeId());
		nodeTree.setType(node.getType());
		return nodeTree;
	}
	//-----------------------------TenantConfigTree---
	public  ConfigurationTreeNode getConfigTreeNodeForFeatureGroup(String tenantName,String siteName,String featureGroup) throws ConfigPersistenceException{
		if(!tenantConfigTreeService.isInitialized()){
			getConfigPolicyNodeTree();
		}
		ConfigurationTreeNode configtreeNode=tenantConfigTreeService.getConfigTreeNodeForFeatureGroup(tenantName, siteName, featureGroup);
		
		return configtreeNode;
	}
	
	public ConfigurationTreeNode getConfigTreeNodeForTenantById(Integer tenantId) throws ConfigPersistenceException{
		if(!tenantConfigTreeService.isInitialized()){
			getConfigPolicyNodeTree();
		}
		ConfigurationTreeNode	tenantConfigNodeTree=tenantConfigTreeService.getConfigTreeNodeForTenantById(tenantId);
		return tenantConfigNodeTree;
	}

	public Integer getApplicableNodeId(String tenantId, String siteId, String featureGroup, String featureName,String vendorName,String version)
															throws InvalidNodeTreeException, ConfigPersistenceException{
		if(!tenantConfigTreeService.isInitialized()){
			logger.debug("config tree is not initialiazed");
			getConfigPolicyNodeTree();
		}
		
		Integer applicableNodeId=tenantConfigTreeService.getApplicableNodeId(tenantId, siteId, featureGroup, featureName,vendorName,version);
		return applicableNodeId;
	}
	
	@Override
	public Integer getApplicableNodeId(String tenantId, String siteId)
			throws InvalidNodeTreeException, ConfigPersistenceException {
		if(!tenantConfigTreeService.isInitialized()){
			getConfigPolicyNodeTree();
		}
		Integer applicableNodeId=tenantConfigTreeService.getApplicableNodeId(tenantId, siteId);

		return applicableNodeId;
	}

	
	
	
	// ------Node Data calls------
	public List<ConfigNodeData> getConfigNodeDataByNodeId(Integer nodeId) throws ConfigPersistenceException {
		 configDataDao = new ConfigNodeDataDAO();
		List<ConfigNodeData> nodeDataList=null;
		try {
			nodeDataList = configDataDao.getConfigNodeDataByNodeId(nodeId);
		} catch (SQLException | IOException sqlexp) {
			throw new ConfigPersistenceException("Failed to get Config Node data by  Node Id="+ nodeId, sqlexp);
		}
		return nodeDataList;
	}

	public ConfigNodeData getConfigNodeDatabyNameAndNodeId(Integer nodeId, String configName,String configType) throws ConfigPersistenceException {
		 configDataDao = new ConfigNodeDataDAO();
		try {
			return configDataDao.getConfigNodeDatabyNameAndNodeId(nodeId, configName,configType);
		} catch (SQLException | IOException sqlexp) {
			throw new ConfigPersistenceException("Failed to get Config Node data by Name="+configName+" and Node Id="+ nodeId, sqlexp);
		}
	}
	
	
	public boolean updateConfigdataInConfigNodeData(String xmlString,Integer nodeId,String configName,String configType) throws ConfigPersistenceException{
		boolean configdataSuccessUpdate;
		try {
			configDataDao.updateConfigDataByNameAndNodeId(xmlString,nodeId,configName,configType);
			configdataSuccessUpdate=true;
		} catch (SQLException | IOException sqlexp) {
			throw new ConfigPersistenceException("Failed to get Node by Id="+ nodeId, sqlexp);
		}
		
		return configdataSuccessUpdate;
	}

	public boolean deleteConfigNodeData(Integer configNodeDataId) throws ConfigPersistenceException {
		try {
			 configDataDao = new ConfigNodeDataDAO();
			int numofRows = configDataDao.deleteConfigNodeData(configNodeDataId);
			if (numofRows > 0)
				return true;
			return false;
		} catch (SQLException | IOException sqlexp) {
			throw new ConfigPersistenceException("Failed to delete ConfigNodeData with configNodeDataId="
					+ configNodeDataId, sqlexp);
		}
	}

	public int deleteConfigNodeDataByNodeId(Integer nodeId) throws ConfigPersistenceException {
		 configDataDao = new ConfigNodeDataDAO();
		int numofRows;
		try {
			numofRows = configDataDao.deleteConfigNodeDataByNodeId(nodeId);
			return numofRows;
		} catch (SQLException | IOException sqlexp) {
			throw new ConfigPersistenceException("Failed to delete ConfigNodeData with nodeId="
					+ nodeId, sqlexp);
		}
		
	}

	public int insertConfigNodeData(ConfigNodeData nodeData) throws ConfigPersistenceException {
		 configDataDao = new ConfigNodeDataDAO();
		try {
			return configDataDao.insertConfigNodeData(nodeData);
		} catch (SQLException | IOException sqlexp) {
			throw new ConfigPersistenceException("Failed to insert ConfigNodeData with nodeId="
					+ nodeData.getParentConfigNodeId()+",configName="+nodeData.getConfigName(), sqlexp);
		}
	}
	
	public void enableConfigNodeData(boolean setEnable,Integer nodeDataId) throws ConfigPersistenceException{
		 configDataDao = new ConfigNodeDataDAO();
		try {
			configDataDao.enableConfigNodeData(setEnable, nodeDataId);
		} catch (SQLException | IOException sqlexp) {
			throw new ConfigPersistenceException("Failed to enable/disable nodeDataId="+nodeDataId, sqlexp);
		}
	}

	public List<ConfigNodeData> getConfigNodeDataByNodeIdAndByType(
			Integer nodeId, String type) throws ConfigPersistenceException {
		configDataDao = new ConfigNodeDataDAO();
		List<ConfigNodeData> nodeDataList = null;
		try {
			nodeDataList = configDataDao
					.getConfigNodeDataByNodeIdByConfigType(nodeId, type);
		} catch (SQLException | IOException sqlexp) {
			throw new ConfigPersistenceException(
					"Failed to get Config Node data by  Node Id=" + nodeId,
					sqlexp);
		}
		return nodeDataList;
	}

	/**
	 * @param configName
	 * @param nodeId
	 * 
	 */
	public int deleteConfigNodeDataByNodeIdAndConfigName(String configName,
			int nodeId) throws ConfigPersistenceException {
		configDataDao = new ConfigNodeDataDAO();
		int numofRows = 0;

		try {
			numofRows = configDataDao
					.deleteConfigNodeDataByNodeIdAndByConfigName(configName,
							nodeId);
		} catch (SQLException | IOException sqlexp) {
			// TODO Auto-generated catch block
			throw new ConfigPersistenceException(
					"Failed to delete ConfigNodeData with nodeId=" + nodeId
							+ " and configName=" + configName, sqlexp);
		}
		return numofRows;
	}

	@Override
	public int getNodeIdByNodeNameAndByTypeNotWithGivenNodeId(String nodeName,
			String type, int parentNodeId, int updatingNodeId) throws ConfigPersistenceException
			{
		int nodeId=0;
		try {
			nodeId = configNodeDAO.getNodeIdByNodeNameAndByTypeNotNodeId(
					nodeName, type, parentNodeId, updatingNodeId);
		} catch (SQLException | IOException sqlexp) {
			throw new ConfigPersistenceException(
					"Failed to insert ConfigNodeData with nodeName="
							+ nodeName + ",type="
							+ type+", parentnode id= "+parentNodeId+", updateNodeID= "+updatingNodeId, sqlexp);
		}

		return nodeId;
	}

	public int updateConfigNodeData(ConfigNodeData nodeData)
			throws ConfigPersistenceException {
		configDataDao = new ConfigNodeDataDAO();
		try {
			return configDataDao.updateConfigNodeData(nodeData);
		} catch (SQLException | IOException sqlexp) {
			throw new ConfigPersistenceException(
					"Failed to insert ConfigNodeData with nodeId="
							+ nodeData.getParentConfigNodeId() + ",configName="
							+ nodeData.getConfigName(), sqlexp);
		}
	}
	/**
	 * getting of nodeId from DB by nodeName , nodeType and parentNodeId
	 * 
	 * @param nodeName
	 * @param type
	 * @param parentNodeId
	 * @return int
	 * @throws ConfigPersistenceException 
	 * @throws SQLException
	 */
	public int getNodeIdByNodeNameAndByType(String nodeName, String type,
			int parentNodeId) throws ConfigPersistenceException {

		int nodeId=0;
		try {
			nodeId = configNodeDAO.getNodeIdByNodeNameAndByType(nodeName, type,
					parentNodeId);
		} catch (SQLException | IOException sqlexp) {
			throw new ConfigPersistenceException(
					"Failed to insert ConfigNodeData with nodeName="
							+ nodeName + ",type="
							+ type+", parentnode id= "+parentNodeId, sqlexp);
		}
		return nodeId;
	}

	@Override
	public int getNodeIDByNameAndType(String nodename,String nodeType) throws ConfigPersistenceException{
		int nodeId=0;
		try {
			nodeId=configNodeDAO.getNodeIdByNodeNameAndByType(nodename,nodeType);
		} catch (SQLException | IOException sqlexp) {
			throw new ConfigPersistenceException("Failed to get Node by Name="+ nodename+" Type = "+nodeType, sqlexp);
		}
		return nodeId;
}


	public int getFeatureMasterIdByFeatureAndFeaturegroup(String featureName,String featureGroup,int siteId) throws ConfigPersistenceException{

		int featureMasterNodeId=0;
		try {
			featureMasterNodeId=configFeatureMasterDao.getFeatureMasterIdByFeatureAndFeaturegroup(featureName,featureGroup,siteId);
		} catch (SQLException | IOException sqlexp) {
			throw new ConfigPersistenceException("Failed to get featureMasterNodeId  by featureName="+ featureName+" featureGroup = "+featureGroup, sqlexp);
		}
		return featureMasterNodeId;
	}

}
