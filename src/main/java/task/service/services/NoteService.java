package task.service.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task.service.clients.NoteItemClient;
import task.service.model.payloads.NotePayload;

@ApplicationScoped
public class NoteService
{
    private final static Logger LOGGER = LoggerFactory.getLogger(NoteService.class);

    @Inject
    @RestClient
    NoteItemClient noteItemClient;

    // todo: get 1 note by uid

    public Response getAll(final String userUid)
    {
        LOGGER.debug("Sending get all request to note client");
        return noteItemClient.allNotesByUser(userUid);
    }

    public Response create(final String userUid, final NotePayload payload)
    {
        LOGGER.debug("Sending create request to note client");
        return noteItemClient.create(userUid, payload);
    }

    public Response update(final String itemUid, final NotePayload payload)
    {
        LOGGER.debug("Sending update request to note client");
        return noteItemClient.update(itemUid, payload);
    }

    public Response delete(final String itemUid)
    {
        LOGGER.debug("Sending delete request to note client");
        return noteItemClient.delete(itemUid);
    }
}
