package se.kau.cs.serg.cabref;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.pac4j.core.config.Config;

import se.kau.cs.serg.cabref.server.CabRefConfigFactory;
import se.kau.cs.serg.cabref.server.CabRefHttpActionAdapter;
import se.kau.cs.serg.cabref.server.CabRefServer;
import se.kau.cs.serg.cabref.server.FilterSetup;
import se.kau.cs.serg.cabref.server.RouteSetup;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import static spark.Spark.*;

public class CabRefServerStartup {

	public static void main(String[] args) {
		CabRefServer server = new CabRefServer();
		ThymeleafTemplateEngine engine = new ThymeleafTemplateEngine();
		final Config config = new CabRefConfigFactory().build();
		config.setHttpActionAdapter(new CabRefHttpActionAdapter(engine));
		
		configureRoutes(server, engine, config);
		configureFilters(server, engine, config);
		printMessage();
		waitForShutdown();
	}

	private static void configureRoutes(CabRefServer server, ThymeleafTemplateEngine engine, Config config) {
		RouteSetup.setupRoutes(server, engine, config);
	}
	
	private static void configureFilters(CabRefServer server, ThymeleafTemplateEngine engine, Config config) {
		FilterSetup.setupFilters(server, engine, config);
	}

	private static void printMessage() {
		System.out.println("---This is CabRef---");
		System.out.println("Server started at http://localhost:4567/cabref");
		System.out.println("Press ENTER to shut down");
	}

	private static void waitForShutdown() {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
			reader.readLine();
			System.out.println("Shutting down...");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			stop();
		}
	}

}
