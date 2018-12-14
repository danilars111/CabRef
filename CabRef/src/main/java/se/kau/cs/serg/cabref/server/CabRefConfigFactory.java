package se.kau.cs.serg.cabref.server;

import org.pac4j.http.client.direct.HeaderClient;
import org.pac4j.http.client.indirect.FormClient;
import org.pac4j.mongo.profile.MongoProfile;
import org.pac4j.mongo.profile.service.MongoProfileService;

import com.mongodb.MongoClient;

import org.pac4j.core.authorization.authorizer.RequireAllRolesAuthorizer;
import org.pac4j.core.authorization.authorizer.RequireAnyRoleAuthorizer;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.config.ConfigFactory;
import org.pac4j.core.credentials.TokenCredentials;

public class CabRefConfigFactory implements ConfigFactory {

	@Override
	public Config build(Object... parameters) {
		MongoClient mongoClient = new MongoClient();
		MongoProfileService userProfileService = new MongoProfileService(mongoClient);
		MongoProfileService tokenProfileService = new MongoProfileService(mongoClient);
		userProfileService.setUsersDatabase("CabRefDB");
		userProfileService.setUsersCollection("Users");
		userProfileService.setPasswordEncoder(new CabRefPasswordEncoder("$2a$10$GMiBKrVECNh9e05OrFlqwe"));
		tokenProfileService.setUsersDatabase("CabRefDB");
		tokenProfileService.setUsersCollection("Tokens");
		tokenProfileService.setPasswordEncoder(new CabRefPasswordEncoder("$2a$10$GMiBKrVECNh9e05OrFlqwe"));

		RequireAllRolesAuthorizer adminAuthorizer = new RequireAllRolesAuthorizer();
		RequireAnyRoleAuthorizer standardAuthorizer = new RequireAnyRoleAuthorizer();
		
		standardAuthorizer.setElements("standard", "admin");
		adminAuthorizer.setElements("admin");
		
		String benchmarkToken = tokenProfileService.getPasswordEncoder().encode("GgetQeWVAx");
		
		HeaderClient apiClient = new HeaderClient("Authorization", "Bearer ", (credentials, ctx) -> {
			//If want to print incoming token: System.out.println("credentials: " + credentials);
		    String token = ((TokenCredentials) credentials).getToken();
		    
		    if (benchmarkToken.equals(tokenProfileService.getPasswordEncoder().encode(token))) {
		        MongoProfile profile = tokenProfileService.findById("0");
		        profile.addRole(tokenProfileService.findById("0").getAttribute("role").toString());
		        // save in the credentials to be passed to the default AuthenticatorProfileCreator
		        credentials.setUserProfile(profile);
		    }
		});
		final FormClient formClient = new FormClient("http://127.0.0.1:4567/login", userProfileService);
		Clients Clients = new Clients("http://127.0.0.1:4567/callback", formClient, apiClient);
		final Config config = new Config(Clients);
		config.addAuthorizer("adminAuthorizer", adminAuthorizer);
		config.addAuthorizer("standardAuthorizer", standardAuthorizer);
		return config;
	}

}
