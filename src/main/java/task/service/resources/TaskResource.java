package task.service.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import task.service.auth.AuthServiceClient;
import task.service.model.Task;

import java.util.List;
import java.util.Map;

@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON)
public class TaskResource
{
    @Inject
    @RestClient
    AuthServiceClient authService;

    public TaskResource()
    {
    }

    // TODO: this is a test endpoint for communication and should be modified
    @GET
    @Operation(summary = "")
    public List<Task> getAllTasks()
    {
        System.out.println("Getting tasks");

        return List.of(new Task("my title"));
    }

    // TODO: this is a test endpoint for communication and should be modified
    @POST
    @Operation(summary = "Create a new task")
    public Response createTask(Task task)
    {
        System.out.println("Client service received task: " + task.getTitle());

        try
        {
            Map<String, String> response = authService.validate("validate");
            System.out.println("Response from auth service: " + response.get("message"));

            Map<String, Object> result = Map.of("task", task, "authResponse", response);

            return Response.status(Response.Status.CREATED).entity(result).build();

        } catch (Exception e)
        {
            System.err.println("Error calling auth service: " + e.getMessage());
            e.printStackTrace();

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Failed to communicate with auth service")).build();
        }
    }
}
