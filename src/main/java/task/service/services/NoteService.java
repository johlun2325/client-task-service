package task.service.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task.service.clients.NoteItemClient;
import task.service.model.payloads.NotePayload;

@ApplicationScoped
public final class NoteService
{
    private final static Logger LOGGER = LoggerFactory.getLogger(NoteService.class);

    @Inject
    @RestClient
    NoteItemClient noteItemClient;

    public Response getAll(final String userUid)
    {
        LOGGER.debug("Sending get all request to note client");
        try
        {
            return noteItemClient.allNotesByUser(userUid);

        } catch (final Exception e)
        {
            LOGGER.error("Failed to get all notes", e);
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).build();
        }
    }

    public Response create(final String userUid, final NotePayload payload)
    {
        LOGGER.debug("Sending create request to note client");
        try
        {
            return noteItemClient.create(userUid, payload);

        } catch (final Exception e)
        {
            LOGGER.error("Failed to create note", e);
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).build();
        }
    }

    public Response update(final String itemUid, final NotePayload payload)
    {
        LOGGER.debug("Sending update request to note client");
        try
        {
            return noteItemClient.update(itemUid, payload);

        } catch (final Exception e)
        {
            LOGGER.error("Failed to update note", e);
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).build();
        }
    }

    public Response delete(final String itemUid)
    {
        LOGGER.debug("Sending delete request to note client");
        try
        {
            return noteItemClient.delete(itemUid);

        } catch (final Exception e)
        {
            LOGGER.error("Failed to delete note", e);
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).build();
        }
    }
}
