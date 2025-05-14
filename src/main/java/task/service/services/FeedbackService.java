package task.service.services;

import jakarta.enterprise.context.ApplicationScoped;
import task.service.model.messages.FeedbackEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@ApplicationScoped
public final class FeedbackService
{
    // Map: userUid -> Map<eventType, FeedbackEvent>
    private final Map<String, Map<String, FeedbackEvent>> feedbackCompleted = new ConcurrentHashMap<>();
    private final Map<String, Map<String, FeedbackEvent>> feedbackPriority = new ConcurrentHashMap<>();

    public void saveFeedback(String userUid, FeedbackEvent feedback)
    {
        if (feedback.getEvent().equals("feedback-completed"))
        {
            feedbackCompleted.computeIfAbsent(userUid, uid -> new ConcurrentHashMap<>()).put(feedback.getEvent(),
                    feedback);
        }

        if (feedback.getEvent().equals("feedback-priority"))
        {
            feedbackPriority.computeIfAbsent(userUid, uid -> new ConcurrentHashMap<>()).put(feedback.getEvent(),
                    feedback);
        }
    }

    public Collection<FeedbackEvent> getAllFeedback(String userUid)
    {
        return Stream
                .concat(feedbackCompleted.getOrDefault(userUid, Map.of()).values().stream(),
                        feedbackPriority.getOrDefault(userUid, Map.of()).values().stream())
                .sorted(Comparator.comparingLong(FeedbackEvent::getTime).reversed()).toList();
    }

    public void clearAllFeedback(String userUid)
    {
        feedbackCompleted.remove(userUid);
        feedbackPriority.remove(userUid);
    }

}
