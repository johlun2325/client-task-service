package task.service.services;

import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task.service.model.messages.FeedbackEvent;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@ApplicationScoped
public final class FeedbackService
{
    private final static Logger LOGGER = LoggerFactory.getLogger(FeedbackService.class);
    private final Map<String, Map<String, FeedbackEvent>> feedbackCompleted = new ConcurrentHashMap<>();
    private final Map<String, Map<String, FeedbackEvent>> feedbackPriority = new ConcurrentHashMap<>();

    public void saveFeedback(final String userUid, final FeedbackEvent feedback)
    {
        var event = feedback.getEvent();

        LOGGER.debug("Received feedback event: {} for user: {}", event, userUid);

        if ("feedback-completed".equals(event))
        {
            LOGGER.debug("Saving feedback for user: {}", userUid);
            feedbackCompleted.computeIfAbsent(userUid, uid -> new ConcurrentHashMap<>()).put(event, feedback);
        }

        if ("feedback-priority".equals(event))
        {
            LOGGER.debug("Saving priority feedback for user: {}", userUid);
            feedbackPriority.computeIfAbsent(userUid, uid -> new ConcurrentHashMap<>()).put(event, feedback);
        }
    }

    public Collection<FeedbackEvent> getAllFeedback(final String userUid)
    {
        LOGGER.debug("Received request for all feedback for user: {}", userUid);
        return Stream
                .concat(feedbackCompleted.getOrDefault(userUid, Map.of()).values().stream(),
                        feedbackPriority.getOrDefault(userUid, Map.of()).values().stream())
                .sorted(Comparator.comparingLong(FeedbackEvent::getTime).reversed()).toList();
    }

    public void clearAllFeedback(final String userUid)
    {
        feedbackCompleted.remove(userUid);
        feedbackPriority.remove(userUid);
    }

}
