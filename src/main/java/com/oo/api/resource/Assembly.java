package com.oo.api.resource;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.oo.api.APIClient;
import com.oo.api.OOInstance;
import com.oo.api.ResourceObject;
import com.oo.api.exception.OneOpsClientAPIException;
import com.oo.api.util.JsonUtil;

public class Assembly extends APIClient {

	public Assembly(OOInstance instance) throws OneOpsClientAPIException {
		super(instance);
	}
	
	static String ASSEMBLY_URI = "/assemblies/";
	
	/**
	 * Fetches specific assembly details
	 * 
	 * @param assemblyName
	 * @return
	 * @throws OneOpsClientAPIException
	 */
	public JsonPath getAssembly(String assemblyName) throws OneOpsClientAPIException {
		if(assemblyName == null || assemblyName.length() == 0) {
			String msg = String.format("Missing assembly name to fetch details");
			throw new OneOpsClientAPIException(msg);
		}
		
		RequestSpecification request = createRequest();
		Response response = request.get(ASSEMBLY_URI + assemblyName);
		if(response != null) {
			if(response.getStatusCode() == 200 || response.getStatusCode() == 302) {
				return response.getBody().jsonPath();
			} else {
				String msg = String.format("Failed to get assembly with name %s due to %s", assemblyName, response.getStatusLine());
				throw new OneOpsClientAPIException(msg);
			}
		} 
		String msg = String.format("Failed to get assembly with name %s due to null response", assemblyName);
		throw new OneOpsClientAPIException(msg);
	}
	
	/**
	 * Lists all the assemblies
	 * 
	 * @return
	 * @throws OneOpsClientAPIException
	 */
	public JsonPath listAssemblies() throws OneOpsClientAPIException {
		RequestSpecification request = createRequest();
		Response response = request.get(ASSEMBLY_URI);
		if(response != null) {
			if(response.getStatusCode() == 200 || response.getStatusCode() == 302) {
				return response.getBody().jsonPath();
			} else {
				String msg = String.format("Failed to get list of assemblies due to %s", response.getStatusLine());
				throw new OneOpsClientAPIException(msg);
			}
		} 
		String msg = String.format("Failed to get list of assemblies due to null response");
		throw new OneOpsClientAPIException(msg);
	}
	
	
	/**
	 * Creates assembly for the given @assemblyName
	 * 
	 * @param assemblyName {mandatory} 
	 * @param ownerEmail a valid email address is {mandatory}
	 * @param comments
	 * @param description
	 * @return
	 * @throws OneOpsClientAPIException
	 */
	public JsonPath createAssembly(String assemblyName, String ownerEmail, String comments, String description) throws OneOpsClientAPIException {
		ResourceObject ro = new ResourceObject();
		Map<String ,String> attributes = new HashMap<String ,String>();
		Map<String ,String> properties= new HashMap<String ,String>();
		
		if(assemblyName != null && assemblyName.length() > 0) {
			properties.put("ciName", assemblyName);
		} else {
			String msg = String.format("Missing assembly name to create one");
			throw new OneOpsClientAPIException(msg);
		}
		
		properties.put("comments", comments);
		ro.setProperties(properties);
		
		if(ownerEmail != null && ownerEmail.length() > 0) {
			attributes.put("owner", ownerEmail);
		} else {
			String msg = String.format("Missing assembly owner email address");
			throw new OneOpsClientAPIException(msg);
		}
		
		attributes.put("description", description);
		ro.setAttributes(attributes);
		
		RequestSpecification request = createRequest();
		JSONObject jsonObject = JsonUtil.createJsonObject(ro , "cms_ci");
		Response response = request.body(jsonObject.toString()).post(ASSEMBLY_URI);
		
		if(response != null) {
			if(response.getStatusCode() == 200 || response.getStatusCode() == 302) {
				return response.getBody().jsonPath();
			} else {
				String msg = String.format("Failed to create assembly with name %s due to %s", assemblyName, response.getStatusLine());
				throw new OneOpsClientAPIException(msg);
			}
		} 
		String msg = String.format("Failed to create assembly with name %s due to null response", assemblyName);
		throw new OneOpsClientAPIException(msg);
	}
	
	/**
	 * Deletes the given assembly
	 * 
	 * @param assemblyName
	 * @return
	 * @throws OneOpsClientAPIException
	 */
	public JsonPath deleteAssembly(String assemblyName) throws OneOpsClientAPIException {
		if(assemblyName == null || assemblyName.length() == 0) {
			String msg = String.format("Missing assembly name to delete one");
			throw new OneOpsClientAPIException(msg);
		}
		
		RequestSpecification request = createRequest();
		Response response = request.delete(ASSEMBLY_URI + assemblyName);
		if(response != null) {
			if(response.getStatusCode() == 200 || response.getStatusCode() == 302) {
				return response.getBody().jsonPath();
			} else {
				String msg = String.format("Failed to delete assembly with name %s due to %s", assemblyName, response.getStatusLine());
				throw new OneOpsClientAPIException(msg);
			}
		} 
		String msg = String.format("Failed to delete assembly with name %s due to null response", assemblyName);
		throw new OneOpsClientAPIException(msg);
	}
}