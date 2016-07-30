package controllers;

import static play.data.Form.form;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import play.Logger;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.login;
import views.html.sites;

import com.getusroi.config.persistence.ConfigPersistenceException;
import com.getusroi.config.persistence.impl.ConfigPersistenceServiceMySqlImpl;
import com.getusroi.ui.tenantconfig.AuthenticationFailedException;
import com.getusroi.ui.tenantconfig.CheckCredentialsDAO;
import com.getusroi.ui.treenode.INodeService;
import com.getusroi.ui.treenode.impl.NodeServiceImpl;

public class TenantController extends Controller {

	static INodeService nodeService = new NodeServiceImpl();
	static CheckCredentialsDAO checkcredentials = new CheckCredentialsDAO();

	/**
	 * 
	 * 
	 * @return
	 */
	public static Result loginPage() {
		Logger.info("inside loginPage() method of Tenant Contoller.");
		return ok(login.render(""));
	}

	/**
	 * method which accepts the login form data and sends sends the data to
	 * verifyLoginDetails for authentication if, authenticated sets the returned
	 * tenant on the session
	 * 
	 * @return
	 * @throws AuthenticationFailedException 
	 */
	public static Result login() throws AuthenticationFailedException {
		
		Logger.debug("===================================================");
		Logger.debug("(.) inside login() ");
		DynamicForm dynamicForm = form().bindFromRequest();
		String user = dynamicForm.get("tenantid");
		String password = DigestUtils.md5Hex(dynamicForm.get("password"));
		Logger.debug("username " + user + " password " + password);
		
		String tenantname = checkcredentials.verifyLoginDetails(user, password);
		setSessionVariable(tenantname);
		Logger.debug("tenantname is :" + tenantname);
		Logger.debug("===================================================");
		if (!tenantname.equalsIgnoreCase("")) {
			return ok(sites.render(""));
		} else{
			return ok(login.render("Username and Password Doesn't match..!!"));
			}
	}
	
	public static Result getLoginPageByTenantid(){
		String tenantname = request().getQueryString("tenantid");
		setSessionVariable(tenantname);
		if (!tenantname.equalsIgnoreCase("")) {
			return ok(sites.render(""));
		} else{
			return ok(login.render("Username and Password Doesn't match..!!"));
			}
		
	}

	/**
	 * method to filter data specific to the tenant
	 * 
	 * @return
	 * @throws JSONException
	 * @throws ConfigPersistenceException
	 */
	public static Result getTreeNodesSpecificToTenant() throws JSONException,
			ConfigPersistenceException {

		String tenant = session().get("tenant");
		JSONObject tenanttree = null;
		JSONArray tenantdata = new JSONArray();

		String treeNode = new ConfigPersistenceServiceMySqlImpl()
				.getConfigPolicyNodeTreeAsJson();

		JSONObject jsonTree = new JSONObject(treeNode);
		Logger.debug("jsonTree is :" + jsonTree);

		JSONArray jsonArrayTreeNode = jsonTree.getJSONArray("children");
		for (int i = 0; i < jsonArrayTreeNode.length(); i++) {
			tenanttree = jsonArrayTreeNode.getJSONObject(i);
			Logger.debug("tenanttree value is :: " + tenanttree);
			if (tenanttree.get("nodeName").equals(tenant)) {
				tenantdata = new JSONArray();
				tenantdata.put(tenanttree);
				Logger.debug("tenantdata value :: " + tenantdata);
				break;
			}
		}
		;
		
		
		return ok(tenantdata.toString());

	}

	/**
	 * method to clear out the session data and redirect to login page
	 * 
	 * @return
	 */
	public static Result logout() {
		session().clear();
		return ok(login.render(""));
	}

	/**
	 * method to set the tenant on the sesson variable
	 * 
	 * @param variable
	 */
	public static void setSessionVariable(String variable) {
		session("tenant", variable);
	}

}
