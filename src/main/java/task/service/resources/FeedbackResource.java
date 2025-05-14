package task.service.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task.service.clients.AuthClient;
import task.service.services.FeedbackService;

import java.util.ArrayList;
import java.util.Map;

@Path("/feedback")
public final class FeedbackResource
{
    private final static Logger LOGGER = LoggerFactory.getLogger(FeedbackResource.class);

    @Inject
    @RestClient
    AuthClient authService;

    @Inject
    FeedbackService feedbackService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFeedback(@HeaderParam("Authorization") final String authHeader)
    {
        LOGGER.debug("Feedback get all request");
        try (var authResponse = authService.validateAuth(authHeader))
        {
            var authData = authResponse.readEntity(new GenericType<Map<String, String>>() {
            });
            var userUid = authData.get("userUid");

            var feedbackList = new ArrayList<>(feedbackService.getAllFeedback(userUid));

            if (feedbackList.isEmpty())
            {
                return Response.noContent().build(); // 204
            }

            // feedbackService.clearAllFeedback(userUid);

            return Response.ok(feedbackList).build();

        } catch (final Exception e)
        {
            LOGGER.error("Failed to fetch feedback", e);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
}
