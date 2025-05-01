package task.service.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task.service.clients.AuthClient;

import java.util.Map;

@ApplicationScoped
public final class GoogleAuthService
{
    private final static Logger LOGGER = LoggerFactory.getLogger(GoogleAuthService.class);

    @Inject
    @ConfigProperty(name = "url.redirect-url")
    String CLIENT_REDIRECT_URL;

    @Inject
    @RestClient
    AuthClient authServiceClient;
    public Response processLogin(final String provider)
    {
        try
        {
            var urlResponse = authServiceClient.login(provider);
            var url = urlResponse.get("url");

            LOGGER.debug("Login url received successfully. Url: " + url);

            return Response.ok(Map.of("url", url)).build();

        } catch (Exception e)
        {
            LOGGER.error("Login request failed", e);

            // Todo: handle login error, make sure client handles this
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
                    Map.of("error", "Login failed", "details", "Redirect to Auth provider could not be received"))
                    .build();
        }
    }

    public Response handleTokenResponse(final String token)
    {
        if (token == null || token.isEmpty())
        {
            LOGGER.error("No token received from clients service");

            var errorUri = UriBuilder.fromUri(CLIENT_REDIRECT_URL)
                    .queryParam("error", "Authentication failed: No token received").build();

            return Response.seeOther(errorUri).build();
        }

        try
        {
            LOGGER.debug("Authentication successful, redirecting to frontend");

            var redirectUri = UriBuilder.fromUri(CLIENT_REDIRECT_URL + "/auth/callback").queryParam("token", token)
                    .build();

            return Response.seeOther(redirectUri).build();

        } catch (final Exception e)
        {
            LOGGER.error("Error processing token", e);

            var errorRedirectUri = UriBuilder.fromUri(CLIENT_REDIRECT_URL)
                    .queryParam("error", "System error during authentication").build();

            return Response.seeOther(errorRedirectUri).build();
        }
    }

    public Response validate(final String token)
    {
        try (var response = authServiceClient.validateAuth(token))
        {

            if (response.getStatus() == Response.Status.OK.getStatusCode())
            {
                LOGGER.debug("Token validation successful");
                return Response.ok().build();
            }
        }
        LOGGER.debug("Token validation failed");
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    public Response processLogout(final String token)
    {
        // Todo: invalidate jwt token
        return Response.ok().build();
    }
}
