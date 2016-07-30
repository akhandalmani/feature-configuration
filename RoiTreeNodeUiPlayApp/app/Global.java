import play.Application;
import play.GlobalSettings;
import play.Logger;

public class Global extends GlobalSettings {

	
	@Override
	public void onStart(Application app) {
		
		Logger.debug("inside startup method ");
		/*try {
			//new FeatureConfigurationService().loadConfigurationFromDB();
		} catch (ConfigPersistenceException | FeatureParserException
				| FeatureConfigurationException e) {
			// TODO Auto-generated catch block
			
			Logger.error("error to load feature config from DB to Hazel cast");
		}*/
	}
	
	
	@Override
	public void onStop(Application app) {
		// TODO Auto-generated method stub
		super.onStop(app);
	}
}
