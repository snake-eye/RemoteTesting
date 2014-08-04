package com.rt.rest;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/service")
public class service {

	public static String exec(String cmd){
		// ObjectMapper mapper = new ObjectMapper();
        // json = mapper.writeValueAsString(person); 
		java.util.Scanner s = null;
		try {
			s = new java.util.Scanner(Runtime.getRuntime().exec(cmd).getInputStream()).useDelimiter("\\A");
		} catch (IOException e) {
			e.printStackTrace();
		}
        if(s.hasNext())return s.next();
        else{
        	try {
    			s = new java.util.Scanner(Runtime.getRuntime().exec(cmd).getErrorStream()).useDelimiter("\\A");
    		} catch (IOException e) {
    			e.printStackTrace();
    		}	
        	return s.next();
        }
}
	
	@GET
	@Path("/findjob/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public String findjobget(@PathParam("name") String name) {
		return name;
	}

	@POST
	@Path("/findjob")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String findjob(String s){
		System.out.println(s);
		String op= exec("java -jar jenkins-cli.jar -s http://localhost:8888/ get-job "+s);	
		if(op.startsWith("Exception"))op="Jenkins is not available ! ";
		else if(op.startsWith("No such job"))op="Job "+ s +" does not exist !";
		else op="Job "+ s +" Exists !";
		return op;
		//return exec("ls");	
	}

	@POST
	@Path("/build")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String build(String s){
		String op= exec("java -jar jenkins-cli.jar -s http://localhost:8888/ build "+s+" -s");	
		if(op.startsWith("Exception"))op="Jenkins is not available ! ";
		else if(op.startsWith("No such job"))op="Job "+ s +" does not exist !";
		else if(op.contains("SUCCESS"))op="SUCCESS";
		else if(op.contains("FAILURE"))op="FAILURE";
		else op="Other";
		return op;
		//return exec("ls");	
	}


}