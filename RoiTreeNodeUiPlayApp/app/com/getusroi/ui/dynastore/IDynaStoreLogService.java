package com.getusroi.ui.dynastore;


import java.sql.Date;

import org.codehaus.jettison.json.JSONException;
import org.json.JSONArray;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.getusroi.config.persistence.ConfigPersistenceException;



public interface IDynaStoreLogService {
	
	
	public JSONArray getDynaStoreLogBySiteId(int siteId); 
	public JSONArray getDynaStoreLogBySiteIdAndStatus(int siteId,String status); 
	public JSONArray getDynaStoreLogBySiteIdAndDate(int siteId,Date date); 

	public JSONArray getDynaStoreLogBySiteIdAndDateByStatus(int siteId,Date date,String status); 

}
