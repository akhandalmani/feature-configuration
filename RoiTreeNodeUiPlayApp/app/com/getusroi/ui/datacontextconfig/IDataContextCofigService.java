package com.getusroi.ui.datacontextconfig;

import com.getusroi.config.persistence.ConfigPersistenceException;


public interface IDataContextCofigService {
	
	
	public String getDataContextObject(int nodeId)throws ConfigPersistenceException;
}
