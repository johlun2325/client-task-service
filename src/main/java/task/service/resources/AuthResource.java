package task.service.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task.service.services.GoogleAuthService;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public final class AuthResource
{
    private final static Logger LOGGER = LoggerFactory.getLogger(AuthResource.class);

    @Inject
    GoogleAuthService authService;

    @GET
    @Path("/login")
    public Response login(@QueryParam("provider") final String provider)
    {
        LOGGER.debug("Login request received: " + provider);

        return authService.processLogin(provider);
    }

    @GET
    @Path("/token")
    public Response handleTokenResponse(@QueryParam("token") final String token)
    {
        LOGGER.debug("Received token from clients service: {}",
                token != null ? token.substring(0, token.length() / 10) : "null");

        return authService.handleTokenResponse(token);
    }

    @GET
    @Path("/validate")
    public Response validateToken(@HeaderParam("Authorization") final String token)
    {
        LOGGER.debug("Token validation requested: {}",
                token != null ? token.substring(0, token.length() / 10) : "null");

        return authService.validate(token);
    }

    @POST
    @Path("/logout")
    public Response logout(@HeaderParam("Authorization") final String token)
    {
        LOGGER.debug("Logout requested with token: {}",
                token != null ? token.substring(0, token.length() / 10) : "null");

        return authService.processLogout(token);
    }

    @OPTIONS
    @Path("/{path : .*}")
    public Response options()
    {
        return Response.ok("").header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                .header("Access-Control-Max-Age", "1209600").build();
    }
}
