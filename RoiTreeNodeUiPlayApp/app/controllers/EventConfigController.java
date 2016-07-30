package controllers;

import java.util.ArrayList;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.getusroi.config.ConfigurationContext;
import com.getusroi.config.persistence.ConfigPersistenceException;
import com.getusroi.eventframework.config.EventFrameworkConfigurationException;
import com.getusroi.ui.eventconfig.IEventConfigService;
import com.getusroi.ui.eventconfig.impl.EventConfigServiceImpl;
import com.getusroi.ui.treenode.INodeService;
import com.getusroi.ui.treenode.impl.NodeServiceImpl;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;

public class EventConfigController extends Controller {

	static INodeService nodeService = new NodeServiceImpl();
	static IEventConfigService ecConfigService = new EventConfigServiceImpl();

	/**
	 * get events based on given Node ID and given type 
	 * @param nodeId
	 * @param type
	 * @return
	 */
	public static Result getEventConfig(int nodeId, String type) {

		JSONArray configNodeDatas = null;

		Logger.debug("inside getEventConfig with nodeId " + nodeId +" type "+type);
		try {
			configNodeDatas = nodeService.getConfigData(nodeId, type);
			Logger.debug("configNode data size " + configNodeDatas.length());
		} catch (JsonProcessingException | ConfigPersistenceException
				| JSONException e) {
			Logger.error("json processing exception or Config Pressistance Exception "
					+ e);
		}

		return ok(configNodeDatas.toString());
	}

	/**
	 * change status of event to enable or disable 
	 * @param nodeId
	 * @param nodeName
	 * @param configName
	 * @param isEnable
	 * @param eventType
	 * @return
	 */
	public static Result changeStatusOfEventFramework(int nodeId,
			String nodeName, String configName, boolean isEnable,
			String eventType) {

		ArrayList<String> listOfParentNodes;
		try {
			listOfParentNodes = nodeService.getParentNodeNames(nodeId + "");

			ConfigurationContext configurationContext = null;
			if (listOfParentNodes != null) {
				configurationContext = getConfigurationContext(
						listOfParentNodes, nodeName);
			}

			ecConfigService.changeStatusOfEventFrameworkConfiguration(
					configurationContext, configName, isEnable, eventType);

		} catch (ConfigPersistenceException
				| EventFrameworkConfigurationException e) {
			Logger.error("error in changing the status of Event config  " + e);
		}
		return redirect(controllers.routes.EventConfigController
				.getEventConfig(nodeId, eventType));

	}
	
	/**
	 * delete eventFramework Configuration based on given event type
	 * @param nodeId
	 * @param nodeName
	 * @param configName
	 * @param eventType
	 * @return
	 */
	public static Result deleteEventFrameworkConfiguration(int nodeId,
			String nodeName, String configName,
			String eventType){
		

		ArrayList<String> listOfParentNodes;
		try {
			listOfParentNodes = nodeService.getParentNodeNames(nodeId + "");

			ConfigurationContext configurationContext = null;
			if (listOfParentNodes != null) {
				configurationContext = getConfigurationContext(
						listOfParentNodes, nodeName);
			}

			ecConfigService.deleteEventFrameworkConfiguration(configurationContext, configName, eventType);

		} catch (ConfigPersistenceException
				| EventFrameworkConfigurationException e) {
			Logger.error("error in changing the status of Event config  " + e);
		}
		return redirect(controllers.routes.EventConfigController
				.getEventConfig(nodeId, eventType));
	}

	/**
	 * to map given parent node list to Configuration Object 
	 * @param list
	 * @param nodeName
	 * @return
	 */
	public  static ConfigurationContext getConfigurationContext(
			ArrayList<String> list, String nodeName) {

		ConfigurationContext configurationContext = null;
		if (list != null) {
			if (list.size() == 2) {
				configurationContext = new ConfigurationContext(list.get(1),
						list.get(0), nodeName);
			} else if (list.size() == 1) {
				configurationContext = new ConfigurationContext(list.get(0),
						nodeName);

			} else if (list.size() == 3) {
				configurationContext = new ConfigurationContext(list.get(2),
						list.get(1), list.get(0), nodeName);

			}
		}
		return configurationContext;
	}

}
