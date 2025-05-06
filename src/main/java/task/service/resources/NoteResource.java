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
import task.service.model.payloads.NotePayload;
import task.service.services.NoteService;

import java.util.Map;

@Path("/note")
public class NoteResource
{
    private final static Logger LOGGER = LoggerFactory.getLogger(NoteResource.class);

    @Inject
    @RestClient
    AuthClient authService;

    @Inject
    NoteService noteService;

    NoteResource()
    {
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all notes for user")
    public Response getAllNotes(@HeaderParam("Authorization") final String authHeader)
    {
        LOGGER.debug("Note get all request");

        try (var response = authService.validateAuth(authHeader))
        {
            var res = response.readEntity(new GenericType<Map<String, String>>() {
            });
            var userUid = res.get("userUid");

            LOGGER.debug("Auth ok, calling note service getAll");
            return noteService.getAll(userUid);

        } catch (final Exception e)
        {
            LOGGER.debug("Failed to fetch all notes", e);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create a new note for user")
    public Response createNote(@HeaderParam("Authorization") final String authHeader, final NotePayload payload)
    {
        LOGGER.debug("Note create request");

        try (var response = authService.validateAuth(authHeader))
        {
            var res = response.readEntity(new GenericType<Map<String, String>>() {
            });
            var userUid = res.get("userUid");

            LOGGER.debug("Auth ok, calling note service create");
            return noteService.create(userUid, payload);

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
    @Operation(summary = "Update specified note")
    public Response updateNote(@HeaderParam("Authorization") final String authHeader,
            @PathParam("itemUid") final String itemUid, final NotePayload payload)
    {
        LOGGER.debug("Note update request");

        try (var response = authService.validateAuth(authHeader))
        {

            LOGGER.debug("Auth ok, calling note service update");
            return noteService.update(itemUid, payload);
        } catch (final Exception e)
        {
            LOGGER.debug("Failed to update note", e);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @DELETE
    @Path("/delete/{itemUid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Delete specified note")
    public Response deleteNote(@HeaderParam("Authorization") final String authHeader,
            @PathParam("itemUid") final String itemUid)
    {
        LOGGER.debug("Note delete request");

        try (var response = authService.validateAuth(authHeader))
        {

            LOGGER.debug("Auth ok, calling note service delete");
            return noteService.delete(itemUid);
        } catch (final Exception e)
        {
            LOGGER.debug("Failed to delete note", e);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
}
