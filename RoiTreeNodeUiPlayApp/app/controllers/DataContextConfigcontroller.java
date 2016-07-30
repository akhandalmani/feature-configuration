package controllers;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;

import com.getusroi.config.persistence.ConfigPersistenceException;
import com.getusroi.ui.datacontextconfig.IDataContextCofigService;
import com.getusroi.ui.datacontextconfig.impl.DataContextConfigService;

public class DataContextConfigcontroller extends Controller {

	static IDataContextCofigService dataContext=new DataContextConfigService();
	public static Result getDataContextConfiguration(int nodeId) {

		Logger.debug("(.) inside getDataContextConfiguration  ");
	String dataConetextConfig="";
	try {
		dataConetextConfig = dataContext.getDataContextObject(nodeId);
	} catch (ConfigPersistenceException e) {
		Logger.error("Error in getting datacontextconfig objext : ",e);
	}
		return ok(dataConetextConfig);
	}
	
}
