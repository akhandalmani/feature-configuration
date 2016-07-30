package controllers;

import java.util.ArrayList;

import org.codehaus.jettison.json.JSONArray;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;

import com.getusroi.config.RequestContext;
import com.getusroi.config.persistence.ConfigPersistenceException;
import com.getusroi.dynastore.config.DynaStoreConfigurationException;
import com.getusroi.ui.dynastore.config.IDynaStoreConfigService;
import com.getusroi.ui.dynastore.config.impl.DynaStoreConfigService;
import com.getusroi.ui.treenode.INodeService;
import com.getusroi.ui.treenode.impl.NodeServiceImpl;

public class DynaStoreConfigController extends Controller {

	static INodeService nodeService = new NodeServiceImpl();

	static IDynaStoreConfigService iDynaStoreConfigurationService = new DynaStoreConfigService();

	public static Result getDynaStoreCoguration(int nodeId) {

		JSONArray jsonArray = iDynaStoreConfigurationService
				.getDynaStoreConfig(nodeId);

		return ok(jsonArray.toString());
	}
	
	
	/**
	 * change status of dynastoreConfig to enable /Disable
	 * @param nodeId
	 * @param nodeName
	 * @param configName
	 * @param isEnable
	 * @param version
	 * @return
	 */
	public static Result changeStatusOfDynaStoreConfig(int nodeId,String nodeName,String configName,boolean isEnable,String version){
		
		Logger.debug("inside changeStatusOfDynaStoreConfig method with node Id= "+nodeId  +" nodeName ="+nodeName  +" configName= "+configName +" isEnable = "+isEnable  );
		ArrayList<String> listOfParentNodes;
	
			try {
				listOfParentNodes = nodeService.getParentNodeNames(nodeId);
			

				RequestContext requestContext = null;
			if (listOfParentNodes != null) {
				requestContext = getConfigRequestContext(
						listOfParentNodes, nodeName,version);
			}
		iDynaStoreConfigurationService.changeStatusDynaStoreConfig(requestContext, configName, version, isEnable);
			} catch (ConfigPersistenceException | DynaStoreConfigurationException e) {

			Logger.error("Error in changeing the status of dynaStoreConfiguration with configNAme ="+configName,e);
			}
	return	redirect(controllers.DynaStoreConfigController.getDynaStoreCoguration(nodeId).toString());
	}
	
	
	/**
	 * delete dynastoreConfiguration based on given Node Id
	 * @param nodeId
	 * @param nodeName
	 * @param configName
	 * @return
	 */
	public static Result deleteDynaStoreConfig(int nodeId,String nodeName,String configName){
	Logger.debug("inside deleteDynaStoreConfig method with node Id= "+nodeId  +" nodeName ="+nodeName  +" configName= "+configName );

		ArrayList<String> listOfParentNodes;
	String version="";
			try {
				listOfParentNodes = nodeService.getParentNodeNames(nodeId + "");
			

				RequestContext requestContext = null;
			if (listOfParentNodes != null) {
				requestContext = getConfigRequestContext(
						listOfParentNodes, nodeName,version);
			}
		iDynaStoreConfigurationService.deleteDynaStoreConfiguration(requestContext, configName, version);
			} catch (ConfigPersistenceException | DynaStoreConfigurationException e) {
			

			Logger.error("Error in deleting dynaStoreConfiguration with configNAme ="+configName,e);
			}
	return	redirect(controllers.DynaStoreConfigController.getDynaStoreCoguration(nodeId).toString());
	
	}
	
	/**
	 * To get map node into request context object 
	 * @param list
	 * @param nodeName
	 * @param version
	 * @return
	 */
	public static RequestContext getConfigRequestContext(
			ArrayList<String> list, String nodeName,String version) {

		RequestContext dyContext = null;
		
		if(list!=null && list.size()==4){
			dyContext=new RequestContext(list.get(3), list.get(2), list.get(1), list.get(0), nodeName, version);
		}
		return dyContext;
	}
}
