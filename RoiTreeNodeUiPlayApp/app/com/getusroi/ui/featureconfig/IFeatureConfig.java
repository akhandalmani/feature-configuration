package com.getusroi.ui.featureconfig;

import java.util.Map;

import com.getusroi.config.ConfigurationContext;
import com.getusroi.config.persistence.ConfigPersistenceException;
import com.getusroi.feature.config.FeatureConfigRequestContext;
import com.getusroi.feature.config.FeatureConfigurationException;
import com.getusroi.feature.config.FeatureConfigurationUnit;

public interface IFeatureConfig {

	

	public FeatureConfigurationUnit getFeatureConfiguration(
			FeatureConfigRequestContext requestContext, String configName)
			throws FeatureConfigurationException;


		public void changeStatusOfFeatureConfig(
			ConfigurationContext featureRequestContext,
			String featureName, boolean isConfigEnabled)
			throws FeatureConfigurationException;

	public boolean deleteFeatureConfiguration(String nodeID, String NodeDataId,
			String featureName) throws FeatureConfigurationException,
			ConfigPersistenceException;

	public String getFeatureConfigartionByNodeIdByType(int nodeIdint,
			String string) throws FeatureConfigurationException;

	public void changeStatusOfFeatureService(
			ConfigurationContext featureRequestContext,
			String configName, Map<String, Boolean> statusEnable
		)
			throws FeatureConfigurationException;

}
