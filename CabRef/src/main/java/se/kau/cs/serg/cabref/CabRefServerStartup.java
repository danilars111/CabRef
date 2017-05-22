package se.kau.cs.serg.cabref;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import se.kau.cs.serg.cabref.server.CabRefServer;
import se.kau.cs.serg.cabref.server.FilterSetup;
import se.kau.cs.serg.cabref.server.RouteSetup;

import static spark.Spark.*;

public class CabRefServerStartup {

	public static void main(String[] args) {
		CabRefServer server = new CabRefServer();
		
		configureRoutes(server);
		configureFilters(server);
		printMessage();
		waitForShutdown();
	}

	private static void configureRoutes(CabRefServer server) {
		RouteSetup.setupRoutes(server);
	}
	
	private static void configureFilters(CabRefServer server) {
		FilterSetup.setupFilters(server);
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
