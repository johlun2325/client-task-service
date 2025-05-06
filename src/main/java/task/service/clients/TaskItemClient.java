package task.service.clients;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import task.service.model.payloads.TaskPayload;

@RegisterRestClient(configKey = "task-client")
public interface TaskItemClient
{
    @GET
    @Path("/all/{userUid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response allTasksByUser(@PathParam("userUid") final String userUid);

    @GET
    @Path("/completed/{userUid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response completedTasksByUser(@PathParam("userUid") final String userUid);

    @GET
    @Path("/priority/{userUid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response priorityTasksByUser(@PathParam("userUid") final String userUid);

    @POST
    @Path("/create/{userUid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response create(@PathParam("userUid") final String userUid, final TaskPayload payload);

    @PUT
    @Path("/update/{itemUid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response update(@PathParam("itemUid") final String itemUid, final TaskPayload payload);

    @DELETE
    @Path("/delete/{itemUid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response delete(@PathParam("itemUid") final String itemUid);
}
