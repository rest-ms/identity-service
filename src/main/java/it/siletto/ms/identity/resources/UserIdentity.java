package it.siletto.ms.identity.resources;

import io.dropwizard.jersey.caching.CacheControl;
import it.siletto.ms.auth.RestrictedTo;
import it.siletto.ms.auth.User;
import it.siletto.ms.base.resources.BaseResource;
import it.siletto.ms.identity.IdentityServiceApp;
import it.siletto.ms.identity.dto.UserResponseDTO;
import it.siletto.ms.identity.model.Identity;
import it.siletto.ms.identity.service.IdentityDAO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserIdentity extends BaseResource {
	
	@Inject
	IdentityDAO identityDao;
	
	@GET
	@Timed
	@Path("login")
	@CacheControl(noCache = true)
	public UserResponseDTO getUser(@RestrictedTo("service") User service, @QueryParam("username") String username, @QueryParam("password") String password) throws Exception {
		
		UserResponseDTO ret = null;

		Identity user = identityDao.getUser(username, password, IdentityServiceApp.getConfig().getRealm());
		if(user!=null){
			ret = new UserResponseDTO();
			ret.setUsername(user.getUsername());
			ret.setRoles(user.getRoles());
		}
		
		return ret;
		
	}

	//TODO: getProfile
}
