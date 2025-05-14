package task.service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task.service.clients.TaskItemClient;
import task.service.model.Task;
import task.service.model.payloads.TaskPayload;
import task.service.producers.ItemEventProducer;

@ApplicationScoped
public final class TaskService
{
    private final static Logger LOGGER = LoggerFactory.getLogger(TaskService.class);

    @Inject
    @RestClient
    TaskItemClient taskItemClient;

    @Inject
    ItemEventProducer itemEventProducer;

    @Inject
    ObjectMapper objectMapper;

    public Response getTask(final String itemUid)
    {
        LOGGER.debug("Sending get task request to task client");
        try
        {
            return taskItemClient.getTask(itemUid);

        } catch (final Exception e)
        {
            LOGGER.error("Failed to get task", e);
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).build();
        }
    }

    public Response getAll(final String userUid)
    {
        LOGGER.debug("Sending get all request to task client");
        try
        {
            return taskItemClient.allTasksByUser(userUid);

        } catch (final Exception e)
        {
            LOGGER.error("Failed to get all tasks", e);
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).build();
        }
    }

    public Response getCompleted(final String userUid)
    {
        LOGGER.debug("Sending get completed request to task client");
        try
        {
            return taskItemClient.completedTasksByUser(userUid);

        } catch (final Exception e)
        {
            LOGGER.error("Failed to get completed tasks", e);
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).build();
        }
    }

    public Response getPriority(final String userUid)
    {
        LOGGER.debug("Sending get priority request to task client");

        try
        {
            return taskItemClient.priorityTasksByUser(userUid);

        } catch (final Exception e)
        {
            LOGGER.error("Failed to get priority tasks", e);
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).build();
        }
    }

    public Response create(final String userUid, final TaskPayload payload)
    {
        LOGGER.debug("Sending create request to task client");

        try (var response = taskItemClient.create(userUid, payload))
        {
            if (response.getStatus() == 201 && response.hasEntity())
            {
                var res = response.readEntity(String.class);
                var node = objectMapper.readTree(res);
                var taskNode = node.get("data");
                var task = objectMapper.treeToValue(taskNode, Task.class);

                itemEventProducer.sendItemCreatedEvent(userUid, task);
                LOGGER.debug("Sent create event to feedback service");

                return Response.status(response.getStatus()).entity(res).type(MediaType.APPLICATION_JSON).build();
            } else
            {
                return Response.status(response.getStatus()).type(MediaType.APPLICATION_JSON).build();
            }

        } catch (final Exception e)
        {
            LOGGER.error("Failed to create task", e);
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).build();
        }
    }

    public Response update(final String userUid, final String itemUid, final TaskPayload payload)
    {
        LOGGER.debug("Sending update request to task client");

        try (var response = taskItemClient.update(itemUid, payload))
        {
            if (response.getStatus() == 200 && response.hasEntity())
            {
                var res = response.readEntity(String.class);
                var node = objectMapper.readTree(res);
                var taskNode = node.get("data");
                var task = objectMapper.treeToValue(taskNode, Task.class);

                itemEventProducer.sendItemUpdatedEvent(userUid, task);

                LOGGER.debug("Sent update event to feedback service");
                return Response.status(response.getStatus()).entity(res).type(MediaType.APPLICATION_JSON).build();
            } else
            {
                return Response.status(response.getStatus()).type(MediaType.APPLICATION_JSON).build();
            }

        } catch (final Exception e)
        {
            LOGGER.error("Failed to update task", e);
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).build();
        }
    }

    public Response delete(final String userUid, final String itemUid)
    {
        LOGGER.debug("Sending delete request to task client");

        try (var response = taskItemClient.delete(itemUid))
        {
            if (response.getStatus() == 200 && response.hasEntity())
            {
                var res = response.readEntity(String.class);

                itemEventProducer.sendItemDeletedEvent(userUid, itemUid);
                LOGGER.debug("Sent delete event to feedback service");
                return Response.status(response.getStatus()).entity(res).type(MediaType.APPLICATION_JSON).build();
            } else
            {
                return Response.status(response.getStatus()).type(MediaType.APPLICATION_JSON).build();
            }

        } catch (final Exception e)
        {
            LOGGER.error("Failed to delete task", e);
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).build();
        }
    }
}
