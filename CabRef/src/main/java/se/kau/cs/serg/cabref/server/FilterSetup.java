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
		before("/cabref", new SecurityFilter(config, "FormClient,HeaderClient"));
		before("/cabref/*", new SecurityFilter(config, "FormClient,HeaderClient", "standardAuthorizer"));
		before("/adminpage/*", new SecurityFilter(config, "FormClient,HeaderClient", "adminAuthorizer"));
	}	
}
