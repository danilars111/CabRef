package se.kau.cs.serg.cabref.server;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;

import java.util.HashMap;
import java.util.Map;

import org.pac4j.core.config.Config;
import org.pac4j.http.client.indirect.FormClient;
import org.pac4j.sparkjava.CallbackRoute;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

/**
 * Configures all routes for this server
 */
public class RouteSetup {

	public static void setupRoutes(CabRefServer server, ThymeleafTemplateEngine engine, Config config) {
		final CallbackRoute callback = new CallbackRoute(config, "/cabref");
		get("/callback", callback);
		post("/callback", callback);
		
		get("/cabref", (req, res) -> index(req, res, server), engine);
		get("/cabref/:key", (req, res) -> entryPage(req, res, server), engine);

		post("/cabref/addNew", (req, res) -> addNewEntry(req, res, server));
		post("/cabref/importFromDiVa", (req, res) -> importFromDiVa(req, res, server));
		post("/cabref/export", (req, res) -> export(req, res, server));

		delete("/cabref/:key", (req, res) -> deleteEntry(req, res, server));
		// unfortunately, delete and put cannot be called from thymeleaf, so we
		// need a workaround via another http verb (either get or post)

		// here is an example for substituting 'delete' with 'get'
		get("/cabref/doDelete/:key", (req, res) -> deleteEntry(req, res, server));

		// here is an example for substituting 'put' with 'post'
		// the same applies for put
		post("/cabref/doUpdate/:key", (req, res) -> updateEntry(req, res, server));
		
		get("/login", (req, res) -> login(req, res, server, config), engine);
	}
	
	public static ModelAndView login(Request req, Response res, CabRefServer server, Config config) {
		Map<String, Object> model = new HashMap<>();
		model.put("callbackUrl", config.getClients().findClient(FormClient.class).getCallbackUrl());
		return new ModelAndView(model, "login");
	}
	
	public static ModelAndView index(Request req, Response res, CabRefServer server) {
		Map<String, Object> model = new HashMap<>();
		model.put("login", req.queryParams("login"));
		model.put("entries", server.getEntries());
		if(req.queryMap("emptyid") != null) {
			model.put("emptyid", req.queryParams("emptyid"));
		}
		if(req.queryMap("idnotfound") != null) {
			model.put("idnotfound", req.queryParams("idnotfound"));
		}
		return new ModelAndView(model, "index");
	}

	private static ModelAndView entryPage(Request req, Response res, CabRefServer server) {
		Map<String, Object> model = new HashMap<>();
		model.put("login", req.queryParams("login"));
		model.put("entry", server.getEntry(req.params(":key")));
		return new ModelAndView(model, "entryPage");
	}
	
	private static Object importFromDiVa(Request req, Response res, CabRefServer server) {
		if(req.queryParams("id").length() == 0) {
			res.redirect("/cabref" + "?login=" + req.queryParams("login") + "&emptyid=true");
			return "";
		}
		
		String key = server.importFromDiVa(req.queryParams("id"));
		System.out.println(key);
		if(key == null) {
			res.redirect("/cabref" + "?login=" + req.queryParams("login") + "&idnotfound=true");
		} else {
			res.redirect("/cabref/" + key + "?login=" + req.queryParams("login"));
		}
		
		return "";
	}
	
	private static Object export(Request req, Response res, CabRefServer server) 
	{
		server.export(req.queryParams("exportFormat"), res);
		return "";
	}
	
	private static Object addNewEntry(Request req, Response res, CabRefServer server) {
		server.addNewEntry(req.queryParams("key"));
		res.redirect("/cabref/" + req.queryParams("key") + "?login=" + req.queryParams("login"));
		return "";
	}

	private static Object deleteEntry(Request req, Response res, CabRefServer server) {
		server.deleteEntry(req.params(":key"));
		res.redirect("/cabref?login=" + req.queryParams("login"));
		return "";
	}

	private static Object updateEntry(Request req, Response res, CabRefServer server) {
		server.updateEntry(req.params(":key"), req.queryParams("type"), req.queryParams("author"),
				req.queryParams("title"), req.queryParams("journal"), req.queryParams("volume"),
				req.queryParams("number"), req.queryParams("year"));
		res.redirect("/cabref/" + req.params(":key") + "?login=" + req.queryParams("login"));
		return "";
	}

}
