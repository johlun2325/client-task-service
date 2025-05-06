package task.service.clients;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import task.service.model.payloads.NotePayload;

@RegisterRestClient(configKey = "note-client")
public interface NoteItemClient
{
    @GET
    @Path("/all/{userUid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response allNotesByUser(@PathParam("userUid") final String userUid);

    @POST
    @Path("/create/{userUid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response create(@PathParam("userUid") final String userUid, final NotePayload payload);

    @PUT
    @Path("/update/{itemUid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response update(@PathParam("itemUid") final String itemUid, final NotePayload payload);

    @DELETE
    @Path("/delete/{itemUid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response delete(@PathParam("itemUid") final String itemUid);
}
