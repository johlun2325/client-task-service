package task.service.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task.service.auth.AuthServiceClient;

import java.util.Map;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource
{

    @Inject
    @RestClient
    AuthServiceClient authServiceClient;

    private final static Logger LOGGER = LoggerFactory.getLogger(AuthResource.class);
    @GET
    @Path("/login")
    public Response login(final String message)
    {
        // Todo: implement actual login flow

        LOGGER.info("Message received: " + message);

        var token = authServiceClient.login(message);

        return Response.ok(token).build();
    }

    @GET
    @Path("/token")
    public Response handleTokenResponse(@QueryParam("response") String token)
    {
        LOGGER.info("Received token from auth service: {}",
                token != null ? token.substring(0, Math.min(10, token.length())) + "..." : "null");

        if (token == null || token.isEmpty())
        {
            LOGGER.error("No token received from auth service");
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", "No ID token received")).build();
        }

        try
        {
            // Send simple confirmation to browser
            return Response.ok(Map.of("status", "authenticated", "message", "Successfully authenticated with Google",
                    "token_received", true)).build();

        } catch (Exception e)
        {
            LOGGER.error("Error processing token", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Failed to process authentication token")).build();
        }
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
