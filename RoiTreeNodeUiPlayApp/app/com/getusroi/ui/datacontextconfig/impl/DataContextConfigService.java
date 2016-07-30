package com.getusroi.ui.datacontextconfig.impl;

import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import play.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.getusroi.config.persistence.ConfigPersistenceException;
import com.getusroi.config.persistence.impl.ConfigPersistenceServiceMySqlImpl;
import com.getusroi.datacontext.config.DataContextConstant;
import com.getusroi.datacontext.config.IDataContextConfigurationService;
import com.getusroi.datacontext.config.impl.DataContextConfigurationService;
import com.getusroi.ui.datacontextconfig.IDataContextCofigService;

public class DataContextConfigService implements IDataContextCofigService {

	
	IDataContextConfigurationService idatacontext=new DataContextConfigurationService();
	ObjectMapper objectMapper = new ObjectMapper();
	@Override
	public String getDataContextObject(int  nodeId) throws ConfigPersistenceException {
		String dataContextConfiguration = "";

		List<com.getusroi.config.persistence.ConfigNodeData> configNodeData = new ConfigPersistenceServiceMySqlImpl()
		.getConfigNodeDataByNodeIdAndByType(nodeId,DataContextConstant.DATACONTEXT_CONFIG_TYPE );
		
		try {
			dataContextConfiguration =objectMapper.writeValueAsString(configNodeData);
		} catch (JsonProcessingException e) {
			throw new ConfigPersistenceException("Error In getting dataContext conduguration  for given node Id : "+nodeId,e);
		}
		return dataContextConfiguration;
	}
	
	public static void main(String[] args) throws JSONException {

String myvar = "{\"iss\":\"accounts.google.com\",\"at_hash\":\"KuCIpE6IfPcCjxNCC8ElrA\",\"aud\":\"998574665340-5lm343c9o3vo4vnprre994lbj50b8bes.apps.googleusercontent.com\",\"sub\":\"114066232538539325933\",\"azp\":\"998574665340-5lm343c9o3vo4vnprre994lbj50b8bes.apps.googleusercontent.com\",\"iat\":1464426650,\"exp\":146443025}";
	JSONObject jsonObject=new JSONObject(myvar);
	Logger.debug("json Object : "+jsonObject);
	
	}

}
