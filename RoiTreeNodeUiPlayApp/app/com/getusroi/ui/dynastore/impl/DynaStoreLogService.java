package com.getusroi.ui.dynastore.impl;

import java.sql.Date;
import java.util.ArrayList;

import org.json.JSONArray;

import play.Logger;

import com.getusroi.dynastore.persistence.DynaStoreLog;
import com.getusroi.dynastore.persistence.DynaStorePersistenceException;
import com.getusroi.dynastore.persistence.IDynaStorePersistenceService;
import com.getusroi.dynastore.persistence.impl.DynaStorePersistenceServiceMySqlImpl;
import com.getusroi.ui.dynastore.IDynaStoreLogService;

public class DynaStoreLogService implements IDynaStoreLogService {
	
	
	IDynaStorePersistenceService iDynaStorePersistenceService=new DynaStorePersistenceServiceMySqlImpl();

	/**
	 * get DynastoreLog by site ID
	 */
	public JSONArray getDynaStoreLogBySiteId(int siteId) {
		ArrayList<DynaStoreLog> listOfDynaStoreList=null;
		JSONArray jsonDynaStoreLog=new JSONArray();
		
		try {
			listOfDynaStoreList=	(ArrayList<DynaStoreLog>) iDynaStorePersistenceService.getDynaStoreLogBySiteId(siteId);
			if(listOfDynaStoreList!=null)
				jsonDynaStoreLog=new JSONArray(listOfDynaStoreList);
		Logger.debug("Json format Data of DynaStore : "+jsonDynaStoreLog);
		} catch (DynaStorePersistenceException e) {
			Logger.error("Error in Getting DynastoreLog From DB with siteNodeId="+siteId,e);
		}
		return jsonDynaStoreLog;
	}


	/**
	 * getDynStore Log by site ID and status
	 */
	public JSONArray getDynaStoreLogBySiteIdAndStatus(int siteId, String status) {
		ArrayList<DynaStoreLog> listOfDynaStoreList=null;
		JSONArray jsonDynaStoreLog=new JSONArray();
		
		try {
			listOfDynaStoreList=	(ArrayList<DynaStoreLog>) iDynaStorePersistenceService.getDynaStoreLogByStatus(siteId, status);
		if(listOfDynaStoreList!=null)
			jsonDynaStoreLog=new JSONArray(listOfDynaStoreList);
		Logger.debug("Json format Data of DynaStore : "+jsonDynaStoreLog);
		} catch (DynaStorePersistenceException e) {
			Logger.error("Error in Getting DynastoreLog From DB with siteNodeId="+siteId,e);
		}catch (Exception e) {
			Logger.error("error in getting DynaSToreLog"+e);
		}
		return jsonDynaStoreLog;
	}

/**
 * To get DynastoreLog Details By Site ID and Date 
 */
	public JSONArray getDynaStoreLogBySiteIdAndDate(int siteId, Date date) {
		ArrayList<DynaStoreLog> listOfDynaStoreList=null;
		JSONArray jsonDynaStoreLog=new JSONArray();
		
		try {
			listOfDynaStoreList=	(ArrayList<DynaStoreLog>) iDynaStorePersistenceService.getDynaStoreLogByGreaterThanGivenDate(siteId, date);
			if(listOfDynaStoreList!=null)
				jsonDynaStoreLog=new JSONArray(listOfDynaStoreList);
		Logger.debug("Json format Data of DynaStore : "+jsonDynaStoreLog);
		} catch (DynaStorePersistenceException e) {
			Logger.error("Error in Getting DynastoreLog From DB with siteNodeId="+siteId,e);
		}catch (Exception e) {
			Logger.error("error in getting DynaSToreLog"+e);
		}
		return jsonDynaStoreLog;
	}


	/**
	 * To getDynaStore Log By Site ID ,Date and Status 
	 */
	public JSONArray getDynaStoreLogBySiteIdAndDateByStatus(int siteId, Date date,
			String status) {
		ArrayList<DynaStoreLog> listOfDynaStoreList=null;
		JSONArray jsonDynaStoreLog=new JSONArray();
		
		try {
			listOfDynaStoreList=	(ArrayList<DynaStoreLog>) iDynaStorePersistenceService.getDynaStoreLogByGreaterThanGivenDateAndStatus(siteId, date, status);
			if(listOfDynaStoreList!=null)
				jsonDynaStoreLog=new JSONArray(listOfDynaStoreList);
		Logger.debug("Json format Data of DynaStore : "+jsonDynaStoreLog);
		} catch (DynaStorePersistenceException e) {
			Logger.error("Error in Getting DynastoreLog From DB with siteNodeId="+siteId,e);
		}catch (Exception e) {
			Logger.error("error in getting DynaSToreLog  "+e);
		}
		return jsonDynaStoreLog;
	}

}
