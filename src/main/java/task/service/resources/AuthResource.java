package task.service.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task.service.auth.AuthServiceClient;

@Path("/api/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource
{

    @Inject
    @RestClient
    AuthServiceClient authServiceClient;

    private final static Logger LOGGER = LoggerFactory.getLogger(AuthResource.class);
    @POST
    @Path("/login")
    public Response login(final String message)
    {
        // Todo: implement actual login flow

        LOGGER.info("Message received: " + message);

        var token = authServiceClient.login(message);

        return Response.ok(token).build();
    }

    @GET
    @Path("/validate")
    public Response validateToken(@HeaderParam("Authorization") final String authHeader)
    {
        LOGGER.info("Token validation requested: " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer mock-jwt-"))
        {
            LOGGER.info("Token validation successful");

            return Response.ok().build();
        }
        LOGGER.info("Token validation failed");

        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @POST
    @Path("/logout")
    public Response logout(@HeaderParam("Authorization") final String authHeader)
    {
        // Todo: invalidate jwt token
        LOGGER.info("Logout requested with token: " + authHeader);
        return Response.ok().build();
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
