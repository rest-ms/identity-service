package it.siletto.ms.identity.test;

import static org.fest.assertions.api.Assertions.assertThat;
import io.dropwizard.testing.junit.DropwizardAppRule;
import it.siletto.ms.auth.service.CypherService;
import it.siletto.ms.auth.service.impl.CypherServiceRSAImpl;
import it.siletto.ms.identity.AppConfiguration;
import it.siletto.ms.identity.IdentityServiceApp;
import it.siletto.ms.identity.dto.UserResponseDTO;

import java.util.Date;
import java.util.Set;

import org.junit.ClassRule;
import org.junit.Test;

import com.google.common.collect.Sets;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class TestUserIdentity {
	
	@ClassRule
    public static final DropwizardAppRule<AppConfiguration> RULE = new DropwizardAppRule<AppConfiguration>(IdentityServiceApp.class, "app.yml");

	@Test
	public void testLogin() throws Exception {
		
		Client client = Client.create();

		System.out.println("login...");
		
		Set<String> roles = Sets.newHashSet();
		roles.add("service");
		
		Injector injector = Guice.createInjector(new Module(){
        	@Override
        	public void configure(Binder binder) {
        		binder.bind(CypherService.class).to(CypherServiceRSAImpl.class);
        	}
        });
		CypherService cypherService = injector.getInstance(CypherServiceRSAImpl.class);
		String serviceToken = cypherService.generateToken(RULE.getConfiguration().getPublicKeyFile(), "svc", roles, new Date().getTime() + 1000 * 60 * 60 * 24);
		
		WebResource login = client.resource(
					String.format("http://localhost:%d/user/login?username=pippo&password=pluto", RULE.getLocalPort())
				);
		ClientResponse response = login.header("Authentication", "Bearer "+serviceToken).accept("application/json").get(ClientResponse.class);

		assertThat(response.getStatus()).isEqualTo(200);
		
		UserResponseDTO res = response.getEntity(UserResponseDTO.class);

		System.out.println("user:" + res.getUsername());
		
		assertThat("pippo").isEqualTo(res.getUsername());
	}
}
