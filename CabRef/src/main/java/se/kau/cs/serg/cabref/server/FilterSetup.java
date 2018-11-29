package se.kau.cs.serg.cabref.server;

import static spark.Spark.*;

import org.pac4j.core.config.Config;
import org.pac4j.sparkjava.SecurityFilter;

import spark.template.thymeleaf.ThymeleafTemplateEngine;

/*
 * Configures all filters for this server
 */
public class FilterSetup {

	public static void setupFilters(CabRefServer server, ThymeleafTemplateEngine engine, Config config) {
		// filter for authenticating any request
		before("/index", new SecurityFilter(config, "FormClient"));
		/*before((request, response) -> {
			boolean userAuthenticated = server.authenticate(request.queryParams("login"));
			if(request.url().contains("/cabref") && !request.url().contains("/authenticate") && !userAuthenticated) {
				halt(401, "Access denied");
			}
		});*/
	}
	
	

}
