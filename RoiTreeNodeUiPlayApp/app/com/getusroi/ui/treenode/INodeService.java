package com.getusroi.ui.treenode;

import java.sql.SQLException;
import java.util.ArrayList;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.getusroi.config.persistence.ConfigNode;
import com.getusroi.config.persistence.ConfigPersistenceException;
import com.getusroi.feature.config.FeatureConfigurationException;

public interface INodeService {
	

	public int getNodeIdByNameAndByType(String nodeName, String type,
			int parentId) throws ConfigPersistenceException ;
	
	public  boolean checkNodeNameExist(String[] nodeArray,String nodeType ,int parentNodeidInt);
	
	public ConfigNode getNodeData(int nodeId)throws ConfigPersistenceException;
	
	public boolean addNode(String nodeType,String [] nodeArray,int parentNodeidInt);
	
	public void deleteNodeByNodeId(int nodeId,String type,String nodeName)
			throws ConfigPersistenceException, FeatureConfigurationException ;
		
	public ArrayList<String> getParentNodeNames(String featureNodeId)
			throws ConfigPersistenceException ;

	public ArrayList<String> getParentNodeNames(int vendorId)
			throws ConfigPersistenceException ;
	
	public int updateNodeByName(int nodeId, String nodeName) throws SQLException ;
	
	public int getNodeIdByNameAndByTypeNotWithUpdatingID(String nodeName,String type,int parentId,int updatingNodeId) throws SQLException ;

		
	public ConfigNode getNodeDataToEditService(int nodeId) throws ConfigPersistenceException ;
	
	public JSONArray getConfigData(int nodeId, String typeOfConfig)
			throws ConfigPersistenceException, JsonProcessingException,
			JSONException;

	


	public ArrayList<String> getNodeTypeAndNodeDataType(JsonNode jsonNode);

		

	
}
