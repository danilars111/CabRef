package se.kau.cs.serg.cabref.server;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pac4j.core.config.Config;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.http.client.indirect.FormClient;
import org.pac4j.mongo.profile.MongoProfile;
import org.pac4j.mongo.profile.service.MongoProfileService;
import org.pac4j.sparkjava.CallbackRoute;
import org.pac4j.sparkjava.SparkWebContext;

import com.mongodb.MongoClient;

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
		
		get("/cabref", (req, res) -> index(req, res, server, config), engine);
		get("/cabref/:key", (req, res) -> entryPage(req, res, server), engine);

		post("/cabref/addNew", (req, res) -> addNewEntry(req, res, server));
		post("/cabref/importFromDiVa", (req, res) -> importFromDiVa(req, res, server));
		post("/cabref/export", (req, res) -> export(req, res, server));

		delete("/cabref/:key", (req, res) -> deleteEntry(req, res, server));
		get("/cabref/doDelete/:key", (req, res) -> deleteEntry(req, res, server));
		post("/cabref/doUpdate/:key", (req, res) -> updateEntry(req, res, server));
		
		get("/login", (req, res) -> login(req, res, server, config), engine);
		get("/logout", (req, res) -> logout(req, res));
		get("/adminpage/update/", (req, res) -> editUser(req, res, server, config), engine);
		get("/adminpage/update/:key", (req, res) -> editUser(req, res, server, config), engine);
		post("/adminpage/update/username/:key", (req, res) -> editUsername(req, res, server, config));
		post("/adminpage/update/password/:key", (req, res) -> editPassword(req, res, server, config));
		post("/adminpage/update/role/:key", (req, res) -> editRole(req, res, server, config));
		post("/adminpage/update/create", (req, res) -> addUser(req, res, server, config));
		get("/adminpage/delete/:key", (req, res) -> deleteUser(req, res, server, config));
		/*
		get("/api/cabref", (req, res) -> APIindex(req, res, server, config), engine);
		get("api/cabref/:key", (req, res) -> APIentryPage(req, res, server), engine);
		post("api/addNew", (req, res) -> APIaddNewEntry(req, res, server));
		post("api/importFromDiVa", (req, res) -> APIimportFromDiVa(req, res, server));
		post("api/export", (req, res) -> APIexport(req, res, server));
		
		delete("api/cabref/:key", (req, res) -> APIdeleteEntry(req, res, server));
		get("api/doDelete/:key", (req, res) -> APIdeleteEntry(req, res, server));
		post("api/doUpdate/:key", (req, res) -> APIupdateEntry(req, res, server));
		
		post("api/adminpage/update/username/:key", (req, res) -> APIeditUsername(req, res, server, config));
		post("api/adminpage/update/password/:key", (req, res) -> APIeditPassword(req, res, server, config));
		post("api/adminpage/update/role/:key", (req, res) -> APIeditRole(req, res, server, config));
		post("api/adminpage/update/create", (req, res) -> APIaddUser(req, res, server, config));
		get("api/adminpage/delete/:key", (req, res) -> APIdeleteUser(req, res, server, config));
				
		get("/api/entry/:key", (req, res) -> APIgetEntry(req, res, server, config));
		*/
	}
	/*
	private static ModelAndView APIindex(Request req, Response res, CabRefServer server, Config config) {
		System.out.println("Endpoint requested!");
		index(req, res, server, config);		
		return null;
	}
	
	private static ModelAndView APIentryPage(Request req, Response res, CabRefServer server) {
		System.out.println("Endpoint requested!");
		res.redirect("/cabref/" + req.params(":key"));		
		return null;
	}
	
	private static ModelAndView APIaddNewEntry(Request req, Response res, CabRefServer server) {
		System.out.println("Endpoint requested!");
		addNewEntry(req, res, server);		
		return null;
	}
	
	private static Object APIimportFromDiVa(Request req, Response res, CabRefServer server) {
		System.out.println("Endpoint requested!");
		importFromDiVa(req, res, server);
		
		return null;
	}

	private static Object APIexport(Request req, Response res, CabRefServer server) {
		System.out.println("Endpoint requested!");
		export(req, res, server);		
		return null;
	}
	
	private static Object APIdeleteEntry(Request req, Response res, CabRefServer server) {
		System.out.println("Endpoint requested!");
		deleteEntry(req, res, server);		
		return null;
	}
	private static Object APIupdateEntry(Request req, Response res, CabRefServer server) {
		System.out.println("Endpoint requested!");
		updateEntry(req, res, server);		
		return null;
	}
	private static Object APIeditUsername(Request req, Response res, CabRefServer server, Config config) {
		System.out.println("Endpoint requested!");
		editUsername(req, res, server, config);
		return null;
	}
	private static Object APIeditPassword(Request req, Response res, CabRefServer server, Config config) {
		System.out.println("Endpoint requested!");
		editUsername(req, res, server, config);
		return null;
	}
	private static Object APIeditRole(Request req, Response res, CabRefServer server, Config config) {
		System.out.println("Endpoint requested!");
		editRole(req, res, server, config);
		return null;
	}
	private static Object APIaddUser(Request req, Response res, CabRefServer server, Config config) {
		System.out.println("Endpoint requested!");
		addUser(req, res, server, config);
		return null;
	}
	private static Object APIdeleteUser(Request req, Response res, CabRefServer server, Config config) {
		System.out.println("Endpoint requested!");
		deleteUser(req, res, server, config);
		return null;
	}
	
	
	private static Object APIgetEntry(Request req, Response res, CabRefServer server, Config config) {
		System.out.println("Endpoint requested!");
		res.body("Works!");
		return null;
	}
	
*/

	private static Object editUsername(Request req, Response res, CabRefServer server, Config config) {
		MongoClient mongoClient = new MongoClient();
		MongoProfileService mongoProfileService = new MongoProfileService(mongoClient);
		MongoProfile profile;
		mongoProfileService.setUsersDatabase("CabRefDB");
		mongoProfileService.setUsersCollection("Users");
		mongoProfileService.setPasswordEncoder(new CabRefPasswordEncoder("$2a$10$GMiBKrVECNh9e05OrFlqwe"));
		if(req.params(":key") != null) {
			profile = mongoProfileService.findById(req.params(":key"));
			profile.removeAttribute("username");
			profile.addAttribute("username", req.queryParams("username"));
			System.out.println("PARAMS: " + req.queryParams());
			mongoProfileService.update(profile, "");
		}
		res.redirect("/adminpage/update/" + req.params(":key"));
		return "";
	}
	
	private static Object editPassword(Request req, Response res, CabRefServer server, Config config) {
		MongoClient mongoClient = new MongoClient();
		MongoProfileService mongoProfileService = new MongoProfileService(mongoClient);
		MongoProfile profile;
		mongoProfileService.setUsersDatabase("CabRefDB");
		mongoProfileService.setUsersCollection("Users");
		mongoProfileService.setPasswordEncoder(new CabRefPasswordEncoder("$2a$10$GMiBKrVECNh9e05OrFlqwe"));
		if(req.params(":key") != null) {
			profile = mongoProfileService.findById(req.params(":key"));
			mongoProfileService.remove(profile);
			mongoProfileService.create(profile, req.queryParams("password"));
			//mongoProfileService.update(profile, "");
		}
		res.redirect("/adminpage/update/" + req.params(":key"));
		return "";
	}
	
	private static Object editRole(Request req, Response res, CabRefServer server, Config config) {
		MongoClient mongoClient = new MongoClient();
		MongoProfileService mongoProfileService = new MongoProfileService(mongoClient);
		MongoProfile profile;
		mongoProfileService.setUsersDatabase("CabRefDB");
		mongoProfileService.setUsersCollection("Users");
		mongoProfileService.setPasswordEncoder(new CabRefPasswordEncoder("$2a$10$GMiBKrVECNh9e05OrFlqwe"));
		if(req.params(":key") != null) {
			profile = mongoProfileService.findById(req.params(":key"));
			profile.removeAttribute("role");
			profile.addAttribute("role", req.queryParams("role"));
			System.out.println("PARAMS: " + req.queryParams());
			System.out.println("role: " + profile.getAttribute("role"));
			mongoProfileService.update(profile, "");

		}
		res.redirect("/adminpage/update/" + req.params(":key"));
		return "";
	}

	private static ModelAndView editUser(Request req, Response res, CabRefServer server, Config config) {
		Map<String, Object> model = new HashMap<>();
		MongoClient mongoClient = new MongoClient();
		MongoProfileService mongoProfileService = new MongoProfileService(mongoClient);
		mongoProfileService.setUsersDatabase("CabRefDB");
		mongoProfileService.setUsersCollection("Users");
		mongoProfileService.setPasswordEncoder(new CabRefPasswordEncoder("$2a$10$GMiBKrVECNh9e05OrFlqwe"));
		if(req.params(":key") != null) {
			model.put("user", mongoProfileService.findById(req.params(":key")));
		}
		model.put("role", mongoProfileService.findById(req.params(":key")).getAttribute("role"));
		return new ModelAndView(model, "userPage");
	}

	private static Object deleteUser(Request req, Response res, CabRefServer server, Config config) {
		MongoClient mongoClient = new MongoClient();
		MongoProfileService mongoProfileService = new MongoProfileService(mongoClient);
		mongoProfileService.setUsersDatabase("CabRefDB");
		mongoProfileService.setUsersCollection("Users");
		mongoProfileService.setPasswordEncoder(new CabRefPasswordEncoder("$2a$10$GMiBKrVECNh9e05OrFlqwe"));
		mongoProfileService.remove(mongoProfileService.findById(req.params(":key")));
		res.redirect("/cabref");
		return "";
	}
	
	private static Object addUser(Request req, Response res, CabRefServer server, Config config) {
		MongoClient mongoClient = new MongoClient();
		MongoProfileService mongoProfileService = new MongoProfileService(mongoClient);
		mongoProfileService.setUsersDatabase("CabRefDB");
		mongoProfileService.setUsersCollection("Users");
		mongoProfileService.setPasswordEncoder(new CabRefPasswordEncoder("$2a$10$GMiBKrVECNh9e05OrFlqwe"));
		MongoProfile profile, iterProfile;

		int index = 1;
		while((iterProfile = mongoProfileService.findById(String.valueOf(index))) != null) {
			if(iterProfile.getAttribute("username").equals(req.queryParams("username"))) {
				res.redirect("/cabref");
				return "";
			}
			index++;
		}
		profile = new MongoProfile();
		profile.setId(String.valueOf(index));
		profile.addAttribute("username", req.queryParams("username"));
		profile.addAttribute("role", req.queryParams("role"));
		profile.setLinkedId(String.valueOf(index));
		mongoProfileService.create(profile, req.queryParams("password"));
		System.out.println(profile.getAttributes());
		
		res.redirect("/cabref");
		return "";
	}

	public static ModelAndView login(Request req, Response res, CabRefServer server, Config config) {
		Map<String, Object> model = new HashMap<>();
		model.put("callbackUrl", config.getClients().findClient(FormClient.class).getCallbackUrl());
		if(req.queryMap("emptyField").hasValue()) { model.put("emptyField", req.queryParams("emptyField")); }
		else if(req.queryMap("error").hasValue()) { model.put("error", true); }
		else { model.put("error", false); }
		return new ModelAndView(model, "login");
	}
	
	public static Object logout(Request req, Response res) {

		final SparkWebContext context = new SparkWebContext(req, res);
	    final ProfileManager manager = new ProfileManager(context);
	    manager.logout();

	    final Session session = req.session();
	    if (session != null) {
	        session.invalidate();
	    }
	    res.redirect("/cabref");
		return "";
	}
	
	
	public static ModelAndView index(Request req, Response res, CabRefServer server, Config config) {
		Map<String, Object> model = new HashMap<>();
		if(getProfile(req, res).getRoles().contains("admin")) {
			MongoClient mongoClient = new MongoClient();
			MongoProfile profile;
			MongoProfileService mongoProfileService = new MongoProfileService(mongoClient);
			List<MongoProfile> profiles = new ArrayList<MongoProfile>();
			mongoProfileService.setUsersDatabase("CabRefDB");
			mongoProfileService.setUsersCollection("Users");
			mongoProfileService.setPasswordEncoder(new CabRefPasswordEncoder("$2a$10$GMiBKrVECNh9e05OrFlqwe"));
			
			int index = 1;
			while((profile = mongoProfileService.findById(String.valueOf(index))) != null) {
				profiles.add(profile);
				index++;
			}
			model.put("profiles", profiles);
			return new ModelAndView(model, "adminpage");
		} else {
			model.put("profile", getProfile(req, res));
			model.put("entries", server.getEntries());
			model.put("role", getProfile(req, res).getAttribute("role"));
			
			if(req.queryMap("emptyid") != null) {
				model.put("emptyid", req.queryParams("emptyid"));
			}
			if(req.queryMap("idnotfound") != null) {
				model.put("idnotfound", req.queryParams("idnotfound"));
			}
			return new ModelAndView(model, "index");
		}
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
	
	private static CommonProfile getProfile(Request req, Response res) {
		final SparkWebContext context = new SparkWebContext(req, res);
		final ProfileManager<CommonProfile> manager = new ProfileManager<CommonProfile>(context);
		manager.get(true).get().addRole(manager.get(true).get().getAttribute("role").toString());
		System.out.println(manager.get(true).get().getAttributes());
		return manager.get(true).get();
	}

}
