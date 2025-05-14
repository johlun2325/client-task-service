package task.service.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task.service.model.messages.FeedbackEvent;
import task.service.services.FeedbackService;

@ApplicationScoped
public class ItemFeedbackConsumer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemFeedbackConsumer.class);

    @Inject
    FeedbackService feedbackService;

    @Inject
    ObjectMapper objectMapper;

    @Incoming("completed")
    public void onFeedbackCompleted(String json)
    {
        LOGGER.info("Received completed feedback: {}", json);
        handleFeedback(json);
    }

    @Incoming("priority")
    public void onFeedbackPriority(String json)
    {
        LOGGER.info("Received priority feedback: {}", json);
        handleFeedback(json);
    }

    private void handleFeedback(String json)
    {
        try
        {
            var feedback = objectMapper.readValue(json, FeedbackEvent.class);
            feedbackService.saveFeedback(feedback.getUserUid(), feedback);
        } catch (Exception e)
        {
            LOGGER.error("Failed to process feedback", e);
        }
    }
}
