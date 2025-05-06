package task.service.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task.service.clients.TaskItemClient;
import task.service.model.payloads.TaskPayload;

@ApplicationScoped
public class TaskService
{
    private final static Logger LOGGER = LoggerFactory.getLogger(TaskService.class);

    @Inject
    @RestClient
    TaskItemClient taskItemClient;

    // todo: get 1 task by uid

    public Response getAll(final String userUid)
    {
        LOGGER.debug("Sending get all request to task client");
        return taskItemClient.allTasksByUser(userUid);
    }

    public Response getCompleted(final String userUid)
    {
        LOGGER.debug("Sending get completed request to task client");
        return taskItemClient.completedTasksByUser(userUid);
    }

    public Response getPriority(final String userUid)
    {
        LOGGER.debug("Sending get priority request to task client");
        return taskItemClient.priorityTasksByUser(userUid);
    }

    public Response create(final String userUid, final TaskPayload payload)
    {
        LOGGER.debug("Sending create request to task client");
        return taskItemClient.create(userUid, payload);
    }

    public Response update(final String itemUid, final TaskPayload payload)
    {
        LOGGER.debug("Sending update request to task client");
        return taskItemClient.update(itemUid, payload);
    }

    public Response delete(final String itemUid)
    {
        LOGGER.debug("Sending delete request to task client");
        return taskItemClient.delete(itemUid);
    }
}
