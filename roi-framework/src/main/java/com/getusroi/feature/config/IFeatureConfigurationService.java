package com.getusroi.feature.config;

import java.util.Map;

import com.getusroi.config.ConfigurationContext;
import com.getusroi.feature.jaxb.Feature;
import com.getusroi.feature.jaxb.Service;


public interface IFeatureConfigurationService {
	public void addFeatureConfiguration(ConfigurationContext configContext,Feature feature) throws FeatureConfigurationException;

	public FeatureConfigurationUnit getFeatureConfiguration(FeatureConfigRequestContext requestContext, String configName)
			throws FeatureConfigurationException;
	public void addNewServiceInFeatureConfiguration(ConfigurationContext configContext, Service service)throws FeatureConfigurationException;
	public int updateFeatureConfiguration(ConfigurationContext configContext,String groupName,Feature fsConfig, int configNodedataId) throws FeatureConfigurationException;
	public void changeStatusOfFeatureConfig(ConfigurationContext configContext, String featureName, boolean isConfigEnabled) throws FeatureConfigurationException;
	public void changeStatusOfFeatureService(ConfigurationContext configContextt, String configName, Map<String, Boolean> enabled) throws FeatureConfigurationException;
	public boolean deleteFeatureConfiguration(ConfigurationContext configContext, String configName)throws FeatureConfigurationException;
    public boolean checkFeatureExistInDBAndCache(ConfigurationContext configContext, String configName)throws FeatureConfigurationException,FeatureConfigRequestException;
    	
    
}
