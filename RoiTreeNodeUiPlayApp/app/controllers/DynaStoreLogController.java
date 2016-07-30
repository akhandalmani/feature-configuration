package controllers;
import java.sql.Date;
import java.util.Calendar;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;

import com.getusroi.ui.dynastore.IDynaStoreLogService;
import com.getusroi.ui.dynastore.impl.DynaStoreLogService;

public class DynaStoreLogController extends Controller { 
	
 static	IDynaStoreLogService iDynaStoreService=new DynaStoreLogService();
	
 	/**
 	 * 
 	 * 
 	 * @param siteNodeId
 	 * @param status
 	 * @param history
 	 * @return
 	 */
	public static Result getDynaStoreLogBySiteId(int siteNodeId,String status,String history){
		
		Date date=null;
		if(status!=null && !status.isEmpty() && history!=null && !history.isEmpty()){
			if(status.equalsIgnoreCase("both") && history.equalsIgnoreCase("all"))
				return ok(iDynaStoreService.getDynaStoreLogBySiteId(siteNodeId).toString());
			
			if(status.equalsIgnoreCase("both")&& !history.equalsIgnoreCase("all")){
				date=getDateDiffernce(history);
				return ok(iDynaStoreService.getDynaStoreLogBySiteIdAndDate(siteNodeId, date).toString());

			}else if(!status.equalsIgnoreCase("both")&& history.equalsIgnoreCase("all")){
				return ok(iDynaStoreService.getDynaStoreLogBySiteIdAndStatus(siteNodeId, status).toString());

			}else{
				Logger.debug("siteNodeId="+siteNodeId +"status="+status +"History="+history);

				date=getDateDiffernce(history);
				return ok(iDynaStoreService.getDynaStoreLogBySiteIdAndDateByStatus(siteNodeId, date, status).toString());

			}
	}
		return ok("");
	}

	/**
	 * 
	 * 
	 * @param history
	 * @return
	 */
	private static Date getDateDiffernce(String history){
		
		Calendar calendar=Calendar.getInstance();
		
		int days=Integer.parseInt(history);
		calendar.add(Calendar.DATE, -days);
		java.util.Date utilDate=calendar.getTime();
		Date date=new Date(utilDate.getTime());
		Logger.debug("----------------Date------------------- "+date);
		return date;
	}
	
	

}
