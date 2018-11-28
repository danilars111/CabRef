package se.kau.cs.serg.cabref.server;

import static spark.Spark.*;

/*
 * Configures all filters for this server
 */
public class FilterSetup {

	public static void setupFilters(CabRefServer server) {
		// filter for authenticating any request
		before((request, response) -> {
			boolean userAuthenticated = server.authenticate(request.queryParams("login"));
			if(request.url().contains("/cabref") && !request.url().contains("/authenticate") && !userAuthenticated) {
				halt(401, "Access denied");
			}
		});
	}

}
