package controllers;

import java.util.ArrayList;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.getusroi.config.ConfigurationContext;
import com.getusroi.config.persistence.ConfigPersistenceException;
import com.getusroi.policy.config.PolicyConfigXMLParserException;
import com.getusroi.policy.config.PolicyConfigurationException;
import com.getusroi.ui.policyconfig.IPolicyConfigOprations;
import com.getusroi.ui.policyconfig.impl.PolicyConfigConstant;
import com.getusroi.ui.policyconfig.impl.PolicyConfigurationOperations;
import com.getusroi.ui.treenode.INodeService;
import com.getusroi.ui.treenode.impl.NodeServiceImpl;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.sites;

public class PolicyConfigController extends Controller {
	static INodeService nodeService = new NodeServiceImpl();
	static IPolicyConfigOprations policyConfigOprations = new PolicyConfigurationOperations();

		
	/**
	 * to get Policy config node Data 
	 * @param nodeId
	 * @return
	 */
	public static Result getPolicyConfigData(int nodeId) {
		JSONArray policyConfigData = null;

		Logger.info("inside getpolicyConfigData method");

		try {
			policyConfigData = nodeService.getConfigData(nodeId,
					PolicyConfigConstant.POLICYTYPE);
		} catch (JsonProcessingException | ConfigPersistenceException
				| JSONException e) {
			Logger.error("json processing exception or Config Pressistance Exception "
					+ e);
		}
		return ok(policyConfigData.toString());

	}

	/**
	 * To change status of policy configauration to enable /disable 
	 * @return
	 */
	public static Result changeStatusOfPolicyConfig() {

		INodeService iNodeService = new NodeServiceImpl();
		JsonNode jsonNode = request().body().asJson();
		String policyName = jsonNode.findPath("policyName").textValue();
		String vendor = jsonNode.findPath("vendorname").textValue();
		String isEnabl = jsonNode.findPath("isEnbled").textValue();
		String featureNodeId = jsonNode.findPath("featureNodeId").textValue();
		String version = jsonNode.findPath("version").textValue();
	try {
			ArrayList<String> list = iNodeService.getParentNodeNames(Integer.parseInt(featureNodeId));
			ConfigurationContext configurationContext=getConfigRequestContext(list, vendor, version);
			Boolean isEnable = Boolean.valueOf(isEnabl);
			
			policyConfigOprations.changePolicyStatus(configurationContext, policyName,isEnable);

		} catch (PolicyConfigurationException pe) {
			Logger.error("Error in change status policy config ", pe);
		} catch (PolicyConfigXMLParserException e) {
			Logger.error("Error in change status policy config ", e);
		} catch (NumberFormatException e) {
			Logger.error("Error in parsing give Node Id = "+featureNodeId,e);
		} catch (ConfigPersistenceException e) {
			Logger.error("error in getting parent Nodes of give node Id= "+featureNodeId,e);			
		}
		return ok(sites.render(""));
	}

	public static Result homerender() {
		return ok(sites.render(""));
	}

	
	/**
	 * To map node details into configurationRequest context 
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
