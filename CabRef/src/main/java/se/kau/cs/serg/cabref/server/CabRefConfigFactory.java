package se.kau.cs.serg.cabref.server;

import org.pac4j.http.client.indirect.FormClient;
import org.pac4j.http.credentials.authenticator.test.SimpleTestUsernamePasswordAuthenticator;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.config.ConfigFactory;

public class CabRefConfigFactory implements ConfigFactory {

	@Override
	public Config build(Object... parameters) {
		final FormClient formClient = new FormClient("http://127.0.0.1:4567/login", new SimpleTestUsernamePasswordAuthenticator());
		Clients Clients = new Clients("http://127.0.0.1:4567/callback", formClient);
		final Config config = new Config(Clients);
		return config;
	}

}
