package com.getusroi.ui.policyconfig.impl;

import com.getusroi.config.ConfigurationContext;
import com.getusroi.policy.config.IPolicyConfigurationService;
import com.getusroi.policy.config.PolicyConfigXMLParserException;
import com.getusroi.policy.config.PolicyConfigurationException;
import com.getusroi.policy.config.impl.PolicyConfigurationService;
import com.getusroi.ui.policyconfig.IPolicyConfigOprations;

public class PolicyConfigurationOperations implements IPolicyConfigOprations {

	IPolicyConfigurationService iPolicyConfigurationService = new PolicyConfigurationService();

	public boolean changePolicyStatus(ConfigurationContext configurationContext, String policyName,
			boolean isEnable) throws PolicyConfigurationException,
			PolicyConfigXMLParserException {
		return	iPolicyConfigurationService.changePolicyStatus(configurationContext,
				policyName, isEnable);

		
	}

}
