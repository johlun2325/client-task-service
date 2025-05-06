package task.service.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task.service.clients.AuthClient;
import task.service.model.payloads.TaskPayload;
import task.service.services.TaskService;

import java.util.Map;

@Path("/task")
public final class TaskResource
{
    private final static Logger LOGGER = LoggerFactory.getLogger(TaskResource.class);

    @Inject
    @RestClient
    AuthClient authService;

    @Inject
    TaskService taskService;

    TaskResource()
    {
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all tasks for user")
    public Response getAllTasks(@HeaderParam("Authorization") final String authHeader)
    {

        try (var response = authService.validateAuth(authHeader))
        {
            var res = response.readEntity(new GenericType<Map<String, String>>() {
            });
            var userUid = res.get("userUid");

            LOGGER.debug("Auth ok, calling task service getAll");
            return taskService.getAll(userUid);

        } catch (final Exception e)
        {
            LOGGER.debug("Failed to fetch all tasks", e);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @GET
    @Path("/completed")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all completed tasks for user")
    public Response getCompletedTasks(@HeaderParam("Authorization") final String authHeader)
    {
        try (var response = authService.validateAuth(authHeader))
        {
            var res = response.readEntity(new GenericType<Map<String, String>>() {
            });
            var userUid = res.get("userUid");

            LOGGER.debug("Auth ok, calling task service getCompleted");
            return taskService.getCompleted(userUid);

        } catch (final Exception e)
        {
            LOGGER.debug("Failed to create note", e);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @GET
    @Path("/priority")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all priority tasks for user")
    public Response getPriorityTasks(@HeaderParam("Authorization") final String authHeader)
    {
        try (var response = authService.validateAuth(authHeader))
        {
            var res = response.readEntity(new GenericType<Map<String, String>>() {
            });
            var userUid = res.get("userUid");

            LOGGER.debug("Auth ok, calling task service getPriority");
            return taskService.getPriority(userUid);

        } catch (final Exception e)
        {
            LOGGER.debug("Failed to create note", e);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create a new task for user")
    public Response createTask(@HeaderParam("Authorization") final String authHeader, final TaskPayload payload)
    {
        try (var response = authService.validateAuth(authHeader))
        {
            var res = response.readEntity(new GenericType<Map<String, String>>() {
            });
            var userUid = res.get("userUid");

            LOGGER.debug("Auth ok, calling task service create");
            return taskService.create(userUid, payload);

        } catch (final Exception e)
        {
            LOGGER.debug("Failed to create note", e);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @PUT
    @Path("/update/{itemUid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update specified task")
    public Response updateTask(@HeaderParam("Authorization") final String authHeader,
            @PathParam("itemUid") final String itemUid, final TaskPayload payload)
    {
        try (var response = authService.validateAuth(authHeader))
        {
            LOGGER.debug("Auth ok, calling task service update");
            return taskService.update(itemUid, payload);
        } catch (final Exception e)
        {
            LOGGER.debug("Failed to create note", e);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @DELETE
    @Path("/delete/{itemUid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Delete specified task")
    public Response deleteTask(@HeaderParam("Authorization") final String authHeader,
            @PathParam("itemUid") final String itemUid)
    {
        try (var response = authService.validateAuth(authHeader))
        {
            LOGGER.debug("Auth ok, calling task service update");
            return taskService.delete(itemUid);
        } catch (final Exception e)
        {
            LOGGER.debug("Failed to create note", e);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
}
