package com.getusroi.ui.permastoreconfig.impl;

import java.util.ArrayList;

import play.Logger;
import play.mvc.Http.Request;

import com.getusroi.config.ConfigurationContext;
import com.getusroi.config.persistence.ConfigPersistenceException;
import com.getusroi.permastore.config.IPermaStoreConfigurationService;
import com.getusroi.permastore.config.PermaStoreConfigParserException;
import com.getusroi.permastore.config.PermaStoreConfigurationException;
import com.getusroi.permastore.config.impl.PermaStoreConfigurationService;
import com.getusroi.permastore.config.jaxb.ConfigurationBuilder;
import com.getusroi.permastore.config.jaxb.ConfigurationBuilderType;
import com.getusroi.permastore.config.jaxb.CustomBuilder;
import com.getusroi.permastore.config.jaxb.Event;
import com.getusroi.permastore.config.jaxb.FeatureInfo;
import com.getusroi.permastore.config.jaxb.InlineBuilder;
import com.getusroi.permastore.config.jaxb.PermaStoreConfiguration;
import com.getusroi.permastore.config.jaxb.PermaStoreEvent;
import com.getusroi.permastore.config.jaxb.PublishPermaStoreEvent;
import com.getusroi.permastore.config.jaxb.SubscribePermaStoreEvents;
import com.getusroi.ui.permastoreconfig.IPermaStoreConfigOpertion;
import com.getusroi.ui.permastoreconfig.PermaStoreBuilderConstant;
import com.getusroi.ui.permastoreconfig.PermaStoreEventHandlerConstant;
import com.getusroi.ui.treenode.INodeService;
import com.getusroi.ui.treenode.impl.NodeServiceImpl;

public class PermstoreConfigOperation implements IPermaStoreConfigOpertion{

	
	
	IPermaStoreConfigurationService iPermaStoreConfigurationService = new PermaStoreConfigurationService();
	static INodeService nodeService = new NodeServiceImpl();

	/**
	 * Check permastore builderType and add needed permastore details 
	 */
	public PermaStoreConfiguration checkPermastoreBuilderType(String eventName,
			String feature, String feartureGroup, String configName,
			String builderType, String isEnabled, String dataType,
			String[] publishPermaStoreEvents,String inlineBuilderType,String inlineJson) {

		Logger.debug("inside checkPermastoreBuilderType method ");
		PermaStoreConfiguration permaStoreConfiguration = new PermaStoreConfiguration();
		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
		FeatureInfo fetFeatureInfo = new FeatureInfo();
		SubscribePermaStoreEvents subscribePermaStoreEvents = new SubscribePermaStoreEvents();
		PermaStoreEvent permaStoreEvent = new PermaStoreEvent();
		boolean isConfigEnabled = false;
		// set feature information
		fetFeatureInfo.setFeatureGroup(feartureGroup);
		fetFeatureInfo.setFeatureName(feature);
		Logger.debug("feature details  " + fetFeatureInfo.getFeatureGroup());

		// set permastore configEvents
		PublishPermaStoreEvent PublishPermaStoreEvent = getPermaStoreEvents(publishPermaStoreEvents);

		// set builder type and class
		if (builderType
				.equalsIgnoreCase(PermaStoreBuilderConstant.CUSTOM_BUILDER)) {

			Logger.debug("builder type is  "
					+ PermaStoreBuilderConstant.CUSTOM_BUILDER);

			CustomBuilder customBuilder = new CustomBuilder();

			customBuilder
					.setBuilder(PermaStoreBuilderConstant.CUSTOM_BUILDER_CLASS);
			configurationBuilder.setType(ConfigurationBuilderType
					.valueOf(PermaStoreBuilderConstant.CUSTOM_BUILDER));
			configurationBuilder.setCustomBuilder(customBuilder);
		} else if (builderType
				.equalsIgnoreCase(PermaStoreBuilderConstant.INLINE_BUILDER)) {

			Logger.debug("builder type is  "
					+ PermaStoreBuilderConstant.INLINE_BUILDER);

			InlineBuilder ilBuilder = new InlineBuilder();
			/*String inlineBuilderValue = "   {\"variable\" :\"" + configName
					+ "\",\"locale_id\":\"US_ENGLISH\"}";*/

			ilBuilder
					.setType(inlineBuilderType);
			ilBuilder.setValue(inlineJson);
			configurationBuilder.setType(ConfigurationBuilderType
					.valueOf(PermaStoreBuilderConstant.INLINE_BUILDER));
			configurationBuilder.setInlineBuilder(ilBuilder);

		}

		if (isEnabled != null && isEnabled.equalsIgnoreCase("true")) {
			isConfigEnabled = true;
		}

		// set isconfigenabled

		permaStoreConfiguration.setIsEnabled(isConfigEnabled);

		// set permastoresubscribe event value
		permaStoreEvent.setEventName(eventName);
		permaStoreEvent.setOnEvent(Event.RELOAD);
		permaStoreEvent
				.setPermaStoreEventHandler(PermaStoreEventHandlerConstant.PERMASTORE_EVENT_HANDLER_CLASS);
		subscribePermaStoreEvents.setPermaStoreEvent(permaStoreEvent);

		// sett all feilds to permastoreconfigarations
		permaStoreConfiguration.setConfigurationBuilder(configurationBuilder);

		permaStoreConfiguration.setDataType(dataType);
		permaStoreConfiguration.setName(configName);
		permaStoreConfiguration.setFeatureInfo(fetFeatureInfo);
		permaStoreConfiguration
				.setPublishPermaStoreEvent(PublishPermaStoreEvent);
		permaStoreConfiguration
				.setSubscribePermaStoreEvents(subscribePermaStoreEvents);

		return permaStoreConfiguration;
	}

	/**
	 * To get Permastore Events Based Given String array of permastore Events 
	 * @param perStoreEvents
	 * @return
	 */
	private PublishPermaStoreEvent getPermaStoreEvents(String[] perStoreEvents) {
		PublishPermaStoreEvent publishPermaStoreEvent = new PublishPermaStoreEvent();

		for (int i = 0; i < perStoreEvents.length; i++) {

			switch (perStoreEvents[i]) {
			case "DeletedEvent":
				publishPermaStoreEvent.setOnConfigDelete("DeletedEvent");
				break;

			case "StatusChangedEvent":
				publishPermaStoreEvent
						.setOnConfigStatusChange("StatusChangedEvent");
				break;

			case "EntryDeletedEvent":
				publishPermaStoreEvent
						.setOnConfigEntryDelete("EntryDeletedEvent");
				break;

			case "AddedEvent":
				publishPermaStoreEvent.setOnConfigEntryAdd("AddedEvent");
				break;

			case "UpdatedEvent":
				publishPermaStoreEvent.setOnConfigEntryUpdate("UpdatedEvent");

				break;

			default:
				break;
			}
		}
		return publishPermaStoreEvent;
	}

	/**
	 * To get BuilderType based on given permastore Configuration 
	 * @param permaStoreConfiguration
	 * @return
	 */
	public String getBuilderType(PermaStoreConfiguration permaStoreConfiguration) {
		String builderName = "";
		if (permaStoreConfiguration.getConfigurationBuilder() != null) {

			if (permaStoreConfiguration.getConfigurationBuilder().getType() != null) {
				builderName = permaStoreConfiguration.getConfigurationBuilder()
						.getType().name();
			}
		}

		return builderName;
	}

	/**
	 * To get Event name based On given PermaStoreConfiguration
	 * @param permaStoreConfiguration
	 * @return
	 */
	public String getEventName(PermaStoreConfiguration permaStoreConfiguration) {
		String eventName = "";
		if (permaStoreConfiguration.getSubscribePermaStoreEvents() != null) {

			if (permaStoreConfiguration.getSubscribePermaStoreEvents()
					.getPermaStoreEvent() != null) {
				eventName = permaStoreConfiguration
						.getSubscribePermaStoreEvents().getPermaStoreEvent()
						.getEventName();
			}
		}

		return eventName;
	}

	/**
	 * To get list of Events based on PermaStore Configuration
	 * @param permaStoreConfiguration
	 * @return
	 */
	public ArrayList<String> getPuhblihedEvents(
			PermaStoreConfiguration permaStoreConfiguration) {
		String eventOndelete = "";
		String eventOnStatusChange = "";
		String entryEventDelete = "";
		String addEvent = "";
		String updateEvent = "";
		ArrayList<String> listEvents = new ArrayList<String>();
		if (permaStoreConfiguration.getPublishPermaStoreEvent() != null) {
			PublishPermaStoreEvent permaStoreEvent = permaStoreConfiguration
					.getPublishPermaStoreEvent();

			if (permaStoreEvent.getOnConfigDelete() != null) {
				eventOndelete = permaStoreEvent.getOnConfigDelete();
			} else if (permaStoreEvent.getOnConfigStatusChange() != null) {
				eventOnStatusChange = permaStoreEvent.getOnConfigStatusChange();
			} else if (permaStoreEvent.getOnConfigEntryDelete() != null) {
				entryEventDelete = permaStoreEvent.getOnConfigEntryDelete();
			} else if (permaStoreEvent.getOnConfigEntryAdd() != null) {
				addEvent = permaStoreEvent.getOnConfigEntryAdd();
			} else if (permaStoreEvent.getOnConfigEntryUpdate() != null) {
				updateEvent = permaStoreEvent.getOnConfigEntryUpdate();
			}

		}
		listEvents.add(eventOndelete);
		listEvents.add(eventOnStatusChange);
		listEvents.add(entryEventDelete);
		listEvents.add(addEvent);
		listEvents.add(updateEvent);

		return listEvents;
	}
	

	/**
	 * #TODO Functinality of adding is not completed 
	 * To add permastore configuration 
	 * 
	 */
	public void addPermastoreData(ConfigurationContext context,
			PermaStoreConfiguration permaStoreConfiguration)
			throws PermaStoreConfigurationException,
			PermaStoreConfigParserException {
		iPermaStoreConfigurationService.addPermaStoreConfiguration(context, permaStoreConfiguration);
	}
	

	/**
	 * #TODO update permastore details completely not Still need change 
	 * To update permastore details 
	 */
		public void updatePermastoreData(ConfigurationContext context,
				PermaStoreConfiguration permaStoreConfiguration,
				int confignodedataId) throws PermaStoreConfigurationException,
				PermaStoreConfigParserException {

			iPermaStoreConfigurationService.updatePermaStoreConfiguration(context,
					 permaStoreConfiguration, confignodedataId);
		}
	

	
		/**
		 * delete permastore Configuration based on given node ID
		 */
	public boolean deletePermastoreConfig(ConfigurationContext configuration,
			String configName) throws ConfigPersistenceException {
		boolean isDeleted=false;
		
		try {
			isDeleted = iPermaStoreConfigurationService
					.deletePermaStoreConfiguration(configuration,
							configName);

		} catch (PermaStoreConfigurationException e) {
			Logger.error("Cache delete not succeded cause: " + e);
		}
		return isDeleted;
	}

	/**
	 * To change status of PermaStore configuration 
	 */
	public void changeStatusOfPermaStoreConfig(
			ConfigurationContext configurationContext, String configName,
			boolean isEnable) throws PermaStoreConfigurationException {
		Logger.debug("inside (.) changeStatusOfPermaStoreConfig method  with context="+configurationContext);
		iPermaStoreConfigurationService.changeStatusOfPermaStoreConfig(configurationContext, configName, isEnable);
	}
}
