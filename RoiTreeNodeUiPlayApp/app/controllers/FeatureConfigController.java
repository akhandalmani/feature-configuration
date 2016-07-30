package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.sites;

import com.fasterxml.jackson.databind.JsonNode;
import com.getusroi.config.ConfigurationContext;
import com.getusroi.config.persistence.ConfigPersistenceException;
import com.getusroi.feature.config.FeatureConfigurationException;
import com.getusroi.ui.featureconfig.IFeatureConfig;
import com.getusroi.ui.featureconfig.impl.FeatureConfig;
import com.getusroi.ui.featureconfig.impl.FeatureConstants;
import com.getusroi.ui.treenode.INodeService;
import com.getusroi.ui.treenode.impl.NodeServiceImpl;

public class FeatureConfigController extends Controller {

	static INodeService iNodeService = new NodeServiceImpl();
	static IFeatureConfig iFeatureConfig = new FeatureConfig();



	
	/**
	 * get feature configdata  for given nodeId
	 * @param nodeId
	 * @return
	 */
	public static Result getFeatureConfigDataByNodeIdAndType(String nodeId) {
		String featureConfigdata = "";

		int nodeIdint = 0;
		try {
			nodeIdint = Integer.parseInt(nodeId);
		} catch (NumberFormatException e) {
			Logger.error("error in parsing the nodeId to int " + e);
		}
		try {
				featureConfigdata = iFeatureConfig
						.getFeatureConfigartionByNodeIdByType(nodeIdint,
								FeatureConstants.FEATURE_NODE);
					} catch (FeatureConfigurationException e) {
				Logger.error("Error in getting featureconfiguration details for given NodeID="+nodeIdint,e);
			} catch (Exception e) {
			Logger.error("Error in parsing feature config string data to json Data for nodeId="+nodeIdint,e);
			}
		
		return ok(featureConfigdata.toString());

	}

	/**
	 * change status enable/ disable of feature  and given  services 
	 * @return
	 */
	public static Result changeStatusFeature() {
		Logger.debug("inside changeStatusFeature  method ");

		JsonNode jsonFeatureData = request().body().asJson();
		Logger.debug("JSon data  " + jsonFeatureData);

		boolean Enable = jsonFeatureData.findPath("isEnable").asBoolean();
		String nodeId = jsonFeatureData.findPath("nodeid").textValue();
		String vendorName = jsonFeatureData.findPath("nodename").textValue();
		String version = jsonFeatureData.findPath("version").textValue();

		org.json.JSONObject jsonObjectOfFeature = null;

		try {
			jsonObjectOfFeature = new org.json.JSONObject(jsonFeatureData.toString());

			Logger.debug("jsonObjectOfFeature  :" + jsonObjectOfFeature);

			Map<String, Boolean> serviceStatusMap = new HashMap<String, Boolean>();

			org.json.JSONArray jsonArrayOfserivices = jsonObjectOfFeature.getJSONArray("service");
			org.json.JSONObject jsonObject = (org.json.JSONObject) jsonArrayOfserivices
					.get(0);
			serviceStatusMap = jsonToMap(jsonObject.toString());
			ArrayList<String> listOfParentNodes = iNodeService
					.getParentNodeNames(Integer.parseInt(nodeId));

			ConfigurationContext configRequestContext = getConfigRequestContext(
					listOfParentNodes, vendorName, version);
			if (Enable) {

				iFeatureConfig.changeStatusOfFeatureConfig(
						configRequestContext,
						configRequestContext.getFeatureName(), true);

				iFeatureConfig
						.changeStatusOfFeatureService(configRequestContext,
								configRequestContext.getFeatureName(),
								serviceStatusMap);

			} else {
				Logger.debug("Its going to be false: "
						+ configRequestContext.getFeatureName());
				iFeatureConfig.changeStatusOfFeatureConfig(
						configRequestContext,
						configRequestContext.getFeatureName(), false);
			}

			return redirect(controllers.routes.FeatureConfigController
					.getFeatureConfigDataByNodeIdAndType(nodeId));

		} catch (org.json.JSONException e) {
			Logger.error(
					"Error in parsing input given in json format for feature",
					e);
		} catch (FeatureConfigurationException e) {
			Logger.error("Error in changing status of featue ", e);
		} catch (NumberFormatException e) {
			Logger.error("Error in parsing given node id to Integer ", e);
		} catch (ConfigPersistenceException e) {
			Logger.error("Error in get parent nodes based on giveb node Id ", e);
		}
		return ok(sites.render(""));

	}

	/**
	 *  to map service name and key into Map Object 
	 * @param json
	 * @return map of services
	 * @throws org.json.JSONException
	 */
	public static Map<String, Boolean> jsonToMap(String json)
			throws org.json.JSONException {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		org.json.JSONObject jObject = new org.json.JSONObject(json);
		Iterator<?> keys = jObject.keys();

		while (keys.hasNext()) {
			String key = (String) keys.next();
			Boolean value = jObject.getBoolean(key);
			map.put(key, value);

		}
		return map;
	}

	/**
	 * To map configuration object for given list parent nodes
	 * @param list
	 * @param nodeName
	 * @param version
	 * @return ConfigurationContext
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
