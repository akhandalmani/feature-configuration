package com.getusroi.ui.policyconfig;

import com.getusroi.config.ConfigurationContext;
import com.getusroi.policy.config.PolicyConfigXMLParserException;
import com.getusroi.policy.config.PolicyConfigurationException;

public interface IPolicyConfigOprations {

	public boolean changePolicyStatus(ConfigurationContext configurationContext, String policyName,
			boolean isEnable) throws PolicyConfigurationException,
			PolicyConfigXMLParserException;
}
