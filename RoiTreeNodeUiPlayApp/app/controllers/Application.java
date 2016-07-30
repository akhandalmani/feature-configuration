package controllers;

import static play.data.Form.form;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import play.Logger;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.editsites;
import views.html.login;
import views.html.sites;

import com.fasterxml.jackson.databind.JsonNode;
import com.getusroi.config.persistence.ConfigNode;
import com.getusroi.config.persistence.ConfigPersistenceException;
import com.getusroi.config.persistence.impl.ConfigPersistenceServiceMySqlImpl;
import com.getusroi.feature.config.FeatureConfigurationException;
import com.getusroi.ui.treenode.INodeService;
import com.getusroi.ui.treenode.impl.NodeServiceImpl;

public class Application extends Controller {

	static INodeService nodeService = new NodeServiceImpl();

	public static Result index() throws JSONException {

		return ok(sites.render(""));
	}

	/**
	 * to get All Tree node data in json Format form DB
	 * 
	 * @return
	 * @throws ConfigPersistenceException
	 * @throws org.json.JSONException
	 * @throws IOException
	 */
	public static Result getTreeNodes() throws ConfigPersistenceException,
			JSONException {
		String treeNode = new ConfigPersistenceServiceMySqlImpl()
				.getConfigPolicyNodeTreeAsJson();

		JSONObject jsonTree = new JSONObject(treeNode);
		JSONArray jsonArrayTreeNode = new JSONArray();
		jsonArrayTreeNode.put(jsonTree);

		;
		return ok(jsonArrayTreeNode.toString());

	}

	/**
	 * #TODO changes need to be done based on changes of vendor ,version To add
	 * Tenant Details to DB
	 * 
	 * @return
	 */
	public static Result addTenantNode() {

		JsonNode jsonNode = request().body().asJson();

		String nodeType = "";
		String nodeTypeData = "";

		String parentNodeId = jsonNode.findPath("parentNodeId").textValue();
		String nodeArray1 = jsonNode.findPath("nodeArray").textValue();
		Logger.debug("got the parentnodeId: " + parentNodeId);
		Logger.debug("got the array of entered node names: " + nodeArray1);
		JSONArray jsonArray = null;
		try {
			jsonArray = new JSONArray(nodeArray1);
		} catch (org.json.JSONException e1) {
			Logger.error("JsonExcepion: " + e1);

		}

		String[] nodeArray = new String[jsonArray.length()];
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				nodeArray[i] = jsonArray.getString(i);
				Logger.debug("parsed json array: " + nodeArray[i]);
			} catch (org.json.JSONException e) {
				Logger.error("JsonExcepion: " + e);
			}

		}

		// getting nodeType and NodeTypedata
		ArrayList<String> listOfTypeNode = nodeService
				.getNodeTypeAndNodeDataType(jsonNode);

		try {
			nodeType = listOfTypeNode.get(0);
			nodeTypeData = listOfTypeNode.get(1);
			Logger.debug("Node parent Id : " + parentNodeId + " nodeTypeData: "
					+ listOfTypeNode.get(1) + "nodeType: " + nodeType);
		} catch (NullPointerException e) {
			Logger.error("error in getting nodetYpe and nodeTypedata from array list "
					+ e);
		}

		if (nodeArray != null && nodeArray.length != 0 && parentNodeId != null
				&& !parentNodeId.isEmpty() && nodeType != null
				&& !nodeType.isEmpty() && nodeTypeData != null
				&& !nodeTypeData.isEmpty()) {

			boolean nodeExist = false;
			int parentNodeidInt = 0;
			try {
				parentNodeidInt = Integer.parseInt(parentNodeId);
			} catch (NumberFormatException e) {
				Logger.error("parent id is not a number " + e);
			}

			// chcek node name exist with enterdname (to avoid dupliction at
			// same node level and under same parent)
			nodeExist = nodeService.checkNodeNameExist(nodeArray, nodeTypeData,
					parentNodeidInt);

			if (!nodeExist) {
				Logger.debug("nodetype: " + nodeType);
				Logger.debug("entering the data to node with : " + nodeTypeData
						+ " " + nodeArray.length + " " + parentNodeidInt);
				boolean sucess = nodeService.addNode(nodeTypeData, nodeArray,
						parentNodeidInt);
				if (sucess) {
					return ok(sites.render(""));
				} else {
					return ok(sites.render("Error in adding node"));

				}
			} else {
				return ok(sites
						.render("Enter Nodes data already Exsist, Please Enter Unique data "));

			}

		} else {
			Logger.debug(" Data not enter");
			return ok(sites.render("Please Enter proper Data"));

		}

	}

	/**
	 * #TODO changes need to be done in code based on change of vendor , version
	 * get Node details for Editing based on Given Node ID
	 * 
	 * @return
	 */
	public static Result getDataToEdit() {

		String nodeId = null;
		ConfigNode configNode = null;
		Logger.debug("inside getDataToEdit  method   -----------");

		nodeId = request().getQueryString("id");
		Logger.debug("node id " + nodeId);

		if (nodeId != null && !nodeId.isEmpty()) {
			int nodeIdint = 0;

			try {

				nodeIdint = Integer.parseInt(nodeId);
			} catch (NumberFormatException e) {
				Logger.error("number fomat excetion in node Id" + e);
			}
			if (nodeIdint != 0) {
				try {
					configNode = nodeService
							.getNodeDataToEditService(nodeIdint);
				} catch (ConfigPersistenceException e) {
					// TODO Auto-generated catch block
					Logger.error("error ConfigPersistenceException " + e);
				}
				return ok(editsites.render("", configNode.getNodeName(),
						configNode.getNodeId(), configNode.getParentNodeId(),
						configNode.getType()));
			} else {
				return ok(sites.render("Please Select ProperNode"));

			}
		} else {
			return ok(sites.render("Please Select ProperNode"));

		}

	}

	// click on cancel redirect to home page
	public static Result home() {

		return redirect(controllers.routes.Application.index());

	}

	/**
	 * #TODO changes need to done based on given Node ID To update Node details
	 * 
	 * @return
	 */
	public static Result updateNodeByNodeId() {

		DynamicForm dynamicForm = form().bindFromRequest();
		Logger.debug("inside updateNodeByNodeId method -----------");

		String nodeName = dynamicForm.get("nodename");
		String nodeId = dynamicForm.get("nodeid");
		String parentId = dynamicForm.get("parentId");
		String nodeType = dynamicForm.get("nodeType");
		int parentIdInt = 0;
		int nodeIdint = 0;
		Logger.debug("node Name to Update : " + nodeName);
		Logger.debug("node id  : " + nodeId);
		Logger.debug("node type :" + nodeType);
		Logger.debug("node parentId : " + parentId);

		try {
			nodeIdint = Integer.parseInt(nodeId);
			parentIdInt = Integer.parseInt(parentId);
		} catch (NumberFormatException e) {
			Logger.error("number format exception in nodeId parsing " + e);
		}

		if (nodeName != null && !nodeName.isEmpty()) {
			int duplicatId = 0;
			int sucess = 0;

			try {
				duplicatId = nodeService
						.getNodeIdByNameAndByTypeNotWithUpdatingID(nodeName,
								nodeType, parentIdInt, nodeIdint);

				if (duplicatId == 0) {
					sucess = nodeService.updateNodeByName(nodeIdint,
							nodeName.trim());

				} else {
					Logger.info("Node name Already Exist with Given Name "
							+ nodeName);

					return ok(editsites.render(
							"Node name Already Exist with Given Name",
							nodeName, nodeIdint, parentIdInt, nodeType));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				Logger.error("error in updating node name " + e);
			}
			if (sucess == 1) {
				Logger.info("update name of node is sucess " + sucess);

				return redirect(controllers.routes.Application.index());

			} else {
				return ok(editsites.render("Error in updating node with name",
						"", nodeIdint, parentIdInt, nodeType));
			}

		} else {
			return ok(editsites.render("Please Enter the node Name", "",
					nodeIdint, parentIdInt, nodeType));
		}

	}

	/**
	 * Delete Node Based on Given Node ID
	 * 
	 * @return
	 */
	public static Result deleteNode() {

		String nodeId = null;
		String type = null;
		String nodeName = null;
		nodeId = request().getQueryString("id");
		type = request().getQueryString("type");
		nodeName = request().getQueryString("name");
		Logger.debug("delete node with this id : " + nodeId);
		boolean deletionSucess = false;
		if (nodeId != null && !nodeId.isEmpty() && type != null
				&& !type.isEmpty()) {
			Logger.debug("inside node delete node if condtion ");
			// call configdao deletnode service method to delete node
			int nodeIdint = 0;
			try {
				nodeIdint = Integer.parseInt(nodeId);
			} catch (NumberFormatException e) {
				Logger.error("node id selected for delete is not number " + e);
			}
			Logger.debug("inside node delete node if condtion " + nodeIdint);

			if (nodeIdint != 0) {
				try {
					nodeService.deleteNodeByNodeId(nodeIdint, type, nodeName);
					deletionSucess = true;
					Logger.info(" delete node sucess full");

				} catch (ConfigPersistenceException e) {
					// TODO Auto-generated catch block
					Logger.error(" ConfigPersistenceException   " + e);
				} catch (FeatureConfigurationException e) {
					Logger.error(" FeatureConfigurationException   " + e);

				}

				if (deletionSucess) {
					return redirect(controllers.routes.Application.index());

				} else {
					return ok(sites.render("Deletion of node failed"));

				}
			} else {
				return ok(sites.render("Please select Proper Node to Delete"));

			}

		} else {
			return ok(sites.render("Please select Proper Node to Delete"));

		}

	}

	public static Result homerender() {
		return ok(sites.render(""));
	}

}
