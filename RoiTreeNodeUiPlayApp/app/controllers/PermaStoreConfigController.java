package controllers;

import static play.data.Form.form;

import java.sql.SQLException;
import java.util.ArrayList;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;

import play.Logger;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.sites;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.getusroi.config.ConfigurationContext;
import com.getusroi.config.persistence.ConfigPersistenceException;
import com.getusroi.permastore.config.PermaStoreConfigParserException;
import com.getusroi.permastore.config.PermaStoreConfigRequestException;
import com.getusroi.permastore.config.PermaStoreConfigurationException;
import com.getusroi.permastore.config.jaxb.PermaStoreConfiguration;
import com.getusroi.ui.permastoreconfig.IPermaStoreConfigOpertion;
import com.getusroi.ui.permastoreconfig.PermaStoreBuilderConstant;
import com.getusroi.ui.permastoreconfig.impl.PermstoreConfigOperation;
import com.getusroi.ui.treenode.INodeService;
import com.getusroi.ui.treenode.impl.NodeServiceImpl;

public class PermaStoreConfigController extends Controller {

	static INodeService nodeService = new NodeServiceImpl();

	static IPermaStoreConfigOpertion iPermaStoreConfigOpertion = new PermstoreConfigOperation();

	
	
	

	/**
	 * get permastore configuration data for given nodeID
	 * @param nodeId
	 * @return
	 */
	public static Result getPermaStoreData(int nodeId) {
		JSONArray configNodeDatas = null;

		Logger.debug("inside getPermastoreMethod with nodeId " + nodeId);
		try {
			configNodeDatas = nodeService.getConfigData(nodeId,
					PermaStoreBuilderConstant.TYPE);
			Logger.debug("configNode data size " + configNodeDatas.length());
		} catch (JsonProcessingException | ConfigPersistenceException
				| JSONException e) {
			Logger.error("json processing exception or Config Pressistance Exception "
					+ e);
		}

		return ok(configNodeDatas.toString());
	}

	/**#TODO not completed still need to work on this 
	 * to addpermastore configuration 
	 * @return
	 * @throws PermaStoreConfigurationException
	 */
	public static Result addPermastoreConfigData()
			throws PermaStoreConfigurationException {
		JsonNode jsonPermaStoreData = request().body().asJson();

		Logger.debug("Inside addPermastoreConfigData method ");
		String nodeId = jsonPermaStoreData.findPath("configNodeId").textValue();
		String tenantId = "";
		String siteId = "";
		String featureName = jsonPermaStoreData.findPath("featureName")
				.textValue();
		String featureGroup = "";
		String dataType = jsonPermaStoreData.findPath("dataType").textValue();
		String builderType = jsonPermaStoreData.findPath("BuilderType")
				.textValue();
		String isConfigEnabled = jsonPermaStoreData.findPath("ConfigEnable")
				.textValue();
		String eventName = jsonPermaStoreData.findPath("EventName").textValue();
		String publishEventsfromjson = jsonPermaStoreData.findPath("EventType")
				.textValue();
		String inilineBuilderType = jsonPermaStoreData.findPath(
				"inlineBuilderType").textValue();

		String inlineJsonValue = jsonPermaStoreData.findPath("jsonval")
				.textValue();
		JSONArray jsonArray = null;
		try {
			jsonArray = new JSONArray(publishEventsfromjson);
		} catch (JSONException e1) {
			Logger.error("JsonExcepion: " + e1);

		}

		String[] publishEvents = new String[jsonArray.length()];
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				publishEvents[i] = jsonArray.getString(i);

			} catch (JSONException e) {
				Logger.error("JsonExcepion: " + e);
			}

		}

		String configName = jsonPermaStoreData.findPath("configName")
				.textValue();
		PermaStoreConfiguration permaStoreConfiguration = null;

		Logger.debug("configNAme : " + configName + " dataType : " + dataType
				+ " buiderType : " + builderType + " \nisConfigEnabled : "
				+ isConfigEnabled + " eventName : " + eventName
				+ " published events " + publishEvents);

		if (configName != null && !configName.isEmpty() && dataType != null
				&& !dataType.isEmpty() && builderType != null
				&& !builderType.isEmpty()

				&& nodeId != null && !nodeId.isEmpty()) {

			boolean sucess = false;

			try {

				// get parents nodenames of featured node
				ArrayList<String> listOfParentNodes = nodeService
						.getParentNodeNames(Integer.parseInt(nodeId));

				if (listOfParentNodes.size() == 4) {

					featureGroup = listOfParentNodes.get(0);
					siteId = listOfParentNodes.get(1);
					tenantId = listOfParentNodes.get(2);
				}

				ConfigurationContext context = new ConfigurationContext(
						tenantId, siteId, featureGroup);
				permaStoreConfiguration = iPermaStoreConfigOpertion
						.checkPermastoreBuilderType(eventName, featureName,
								featureGroup, configName, builderType,
								isConfigEnabled, dataType, publishEvents,
								inilineBuilderType, inlineJsonValue);
				iPermaStoreConfigOpertion.addPermastoreData(context,
						permaStoreConfiguration);

				Logger.info("This is the thing which is getting inserted : "
						+ permaStoreConfiguration);

				sucess = true;
			} catch (PermaStoreConfigurationException e) {
				Logger.error("error PermaStoreConfigurationException  " + e);
			} catch (ConfigPersistenceException e) {
				Logger.error("error ConfigPersistenceException  " + e);
			} catch (PermaStoreConfigParserException e) {
				Logger.error("error PermaStoreConfigParserException  " + e);
			}
			if (sucess) {
				return ok("Success");
				/*
				 * return
				 * redirect(controllers.routes.Application.getPermaStoreData
				 * (nodeIdint));
				 */
			} else {
				return ok(sites.render("The Config Node Already Exist"));

			}

		} else {
			return ok(sites.render("Please select the proper details"));
		}
	}

	public static Result updatePermastoreConfigdata() {
		Logger.debug("Inside updatePermastoreConfigData method ");
		String confignodeId = null;
		String tenantId = "";
		String siteId = "";
		String featureName = "";
	
		String featureGroup = "";
		String dataType = null;
		String builderType = null;
		String isConfigEnabled = null;
		String eventName = null;
		String[] publishEvents = null;
		String confignodedataIdstr = "";
		int confignodedataId = 0;
		PermaStoreConfiguration permaStoreConfiguration = null;
		DynamicForm dynamicForm = form().bindFromRequest();

		featureName = dynamicForm.get("configName");
		dataType = dynamicForm.get("dataType");
		builderType = dynamicForm.get("BuilderType");
		isConfigEnabled = dynamicForm.get("ConfigEnable");
		eventName = dynamicForm.get("EventName");
		confignodeId = dynamicForm.get("configNodeId");
		featureName = dynamicForm.get("featureName");
		confignodedataIdstr = dynamicForm.get("confignodedataIdstr");

		publishEvents = request().body().asFormUrlEncoded().get("EventType");

		try {
			confignodedataId = Integer.parseInt(confignodedataIdstr);
		} catch (NumberFormatException e) {
			Logger.error("Error in parsing configNodeDataID");
		}

		if (publishEvents == null) {
			publishEvents = new String[0];
		}

		Logger.debug("featureName : " + featureName + " dataType : " + dataType
				+ " buiderType : " + builderType + " \nisConfigEnabled : "
				+ isConfigEnabled + " eventName : " + eventName
				+ " published events " + publishEvents + " configNodeID "
				+ confignodeId);

		if (featureName != null && !featureName.isEmpty() && dataType != null
				&& !dataType.isEmpty() && builderType != null
				&& !builderType.isEmpty()

				&& confignodeId != null && !confignodeId.isEmpty()) {
			boolean sucess = false;

			try {

				// get parents nodenames of featured node
				ArrayList<String> listOfParentNodes = nodeService
						.getParentNodeNames(Integer.parseInt(confignodeId));

				if (listOfParentNodes.size() == 4) {

					featureName = listOfParentNodes.get(0);
					featureGroup = listOfParentNodes.get(1);
					siteId = listOfParentNodes.get(2);
					tenantId = listOfParentNodes.get(3);
				}
				ConfigurationContext context = new ConfigurationContext(
						tenantId, siteId, featureGroup);

				iPermaStoreConfigOpertion.updatePermastoreData(context,
						permaStoreConfiguration, confignodedataId);
				sucess = true;
			} catch (PermaStoreConfigurationException e) {
				Logger.error("error PermaStoreConfigurationException  " + e);
			} catch (ConfigPersistenceException e) {
				Logger.error("error ConfigPersistenceException  " + e);
			} catch (PermaStoreConfigParserException e) {
				Logger.error("error PermaStoreConfigParserException  " + e);
			}
			if (sucess) {
				return ok(sites.render(""));
				// return redirect(controllers.routes.Application.index());
			} else {
				return ok(sites.render("The Config Node Already Exist"));

			}

		} else {
			return ok(sites.render("Please select the proper details"));
		}
	}

	/**
	 * to change status of permastore configuration to enable or disable 
	 * @return
	 */
	public static Result changeStatusOfPermaStore() {
		Logger.debug("inside (.) changeStatusofPermaStore method");

		JsonNode jsonNode = request().body().asJson();
		DynamicForm dynamicForm = form().bindFromRequest();
		String nodId = jsonNode.findPath("nodeid").textValue();
		Logger.debug(" nodeId " + nodId);
		String isenable = jsonNode.findPath("isenable").textValue();
		String configname = jsonNode.findPath("configName").textValue();
		Logger.debug("configname: " + configname);
		String vendorName = dynamicForm.get("nodename");
		String version = dynamicForm.get("version");

		try {
			Boolean isEna = Boolean.valueOf(isenable);
			ArrayList<String> list = nodeService.getParentNodeNames(Integer
					.parseInt(nodId));

			ConfigurationContext configurationContext = getConfigRequestContext(
					list, vendorName, version);
			iPermaStoreConfigOpertion.changeStatusOfPermaStoreConfig(
					configurationContext, configname, isEna);
		} catch (ConfigPersistenceException e) {
			Logger.error("Error in getting parent nodes  of given configName ="
					+ configname, e);
		} catch (PermaStoreConfigurationException e) {
			Logger.error(
					"Error in changing status of permastore of given config Name ="
							+ configname, e);
		} catch (Exception e) {
			Logger.error(
					"Error in changing status of permastore of given config Name ="
							+ configname, e);
		}

		return redirect(controllers.routes.Application.index());

	}

	

	/**
	 * delete permastore configuration by node Id
	 * @return
	 * @throws PermaStoreConfigRequestException
	 * @throws SQLException
	 */
	public static Result deleteNodeByNodeIdPermastore()
			throws PermaStoreConfigRequestException, SQLException {
		String configName1;
		String nodeId1;
		String nodeName1;
		String version;
		
		JsonNode jsonNode = request().body().asJson();
		configName1 = jsonNode.findPath("configName").textValue();
		nodeId1 = jsonNode.findPath("nodeId").textValue();
		nodeName1 =jsonNode.findPath("nodeName").textValue();//vendor name 
		version = jsonNode.findPath("version").textValue();
		boolean success = false;
		try {
			
			ArrayList<String> list = nodeService.getParentNodeNames(Integer
					.parseInt(nodeId1));

			ConfigurationContext configurationContext = getConfigRequestContext(
					list, nodeName1, version);
			iPermaStoreConfigOpertion.deletePermastoreConfig(configurationContext,configName1
				);
			success = true;
		} catch (ConfigPersistenceException cpe) {
			Logger.error("Error occured while deleting the configuration: "
					+ cpe);
		}
		if (success == true) {
			return redirect("/homerender");
		} else {
			return ok("Deletion Failed for further information check logs");
		}
	}

	/**
	 * to map list of  parent Nodes  into configurationrequestContext   object 
	 * @param list
	 * @param nodeName
	 * @param version
	 * @return
	 */
	public static ConfigurationContext getConfigRequestContext(
			ArrayList<String> list, String nodeName, String version) {

		ConfigurationContext dyContext = null;

		if (list != null && list.size() == 4) {
			dyContext = new ConfigurationContext(list.get(3), list.get(2),
					list.get(1), list.get(0), nodeName, version);
		}
		return dyContext;
	}
}
