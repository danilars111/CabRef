package se.kau.cs.serg.cabref.server;

import org.pac4j.http.client.indirect.FormClient;
import org.pac4j.mongo.profile.service.MongoProfileService;

import com.mongodb.MongoClient;

import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.config.ConfigFactory;

public class CabRefConfigFactory implements ConfigFactory {

	@Override
	public Config build(Object... parameters) {
		MongoClient mongoClient = new MongoClient();
		MongoProfileService mongoProfileService = new MongoProfileService(mongoClient);
		mongoProfileService.setUsersDatabase("CabRefDB");
		mongoProfileService.setUsersCollection("Users");
		mongoProfileService.setPasswordEncoder(new CabRefPasswordEncoder("$2a$10$GMiBKrVECNh9e05OrFlqwe"));
		
		final FormClient formClient = new FormClient("http://127.0.0.1:4567/login", mongoProfileService);
		Clients Clients = new Clients("http://127.0.0.1:4567/callback", formClient);
		final Config config = new Config(Clients);
		return config;
	}

}
