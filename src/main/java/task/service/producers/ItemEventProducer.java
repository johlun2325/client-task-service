package task.service.producers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task.service.model.Task;
import task.service.model.messages.ItemEvent;

import java.util.HashMap;

@ApplicationScoped
public final class ItemEventProducer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemEventProducer.class);

    @Inject
    @Channel("created")
    Emitter<String> itemCreatedEmitter;

    @Inject
    @Channel("updated")
    Emitter<String> itemUpdatedEmitter;

    @Inject
    @Channel("deleted")
    Emitter<String> itemDeletedEmitter;

    @Inject
    ObjectMapper objectMapper;

    public void sendItemCreatedEvent(final String key, final Task task)
    {
        try
        {
            var event = buildEvent(task, "item-created");

            var eventJson = objectMapper.writeValueAsString(event);
            LOGGER.debug("Sending item-created event");

            itemCreatedEmitter.send(KafkaRecord.of(key, eventJson));

            LOGGER.debug("Event item-created sent successfully");

        } catch (final Exception e)
        {
            LOGGER.error("Failed to send item created event", e);
            throw new RuntimeException("Failed to send event", e);
        }
    }

    public void sendItemUpdatedEvent(final String key, final Task task)
    {
        try
        {
            var event = buildEvent(task, "item-updated");

            var eventJson = objectMapper.writeValueAsString(event);
            LOGGER.debug("Sending item-updated event");

            itemUpdatedEmitter.send(KafkaRecord.of(key, eventJson));

            LOGGER.debug("Event item-updated sent successfully");

        } catch (final Exception e)
        {
            LOGGER.error("Failed to send item created event", e);
            throw new RuntimeException("Failed to send event", e);
        }
    }

    public void sendItemDeletedEvent(final String key, final String itemUid)
    {
        try
        {
            var event = buildDeletedEvent(itemUid);

            var eventJson = objectMapper.writeValueAsString(event);
            LOGGER.debug("Sending item-deleted event");

            itemDeletedEmitter.send(KafkaRecord.of(key, eventJson));

            LOGGER.debug("Event item-deleted sent successfully");

        } catch (final Exception e)
        {
            LOGGER.error("Failed to send item created event", e);
            throw new RuntimeException("Failed to send event", e);
        }
    }

    private ItemEvent buildEvent(final Task task, final String eventType)
    {
        var event = new ItemEvent();
        event.setEvent(eventType);
        event.setItemUid(task.getUid());
        event.setUserUid(task.getUserUid());
        event.setType(task.getType());

        var content = new HashMap<String, Object>();
        content.put("title", task.getTitle());
        content.put("description", task.getDescription());
        content.put("priority", task.isPriority());
        content.put("completed", task.isCompleted());
        content.put("createdAt", task.getCreatedAt());
        content.put("updatedAt", task.getUpdatedAt());
        content.put("completedAt", task.getCompletedAt());

        event.setContent(content);
        event.setTime(System.currentTimeMillis());

        return event;
    }

    private ItemEvent buildDeletedEvent(final String itemUid)
    {
        var event = new ItemEvent();
        event.setEvent("item-deleted");
        event.setItemUid(itemUid);
        return event;
    }
}
