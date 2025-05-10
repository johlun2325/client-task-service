package task.service.model.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.reactive.messaging.Message;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class FeedbackEvent
{
    private String event;
    private String userUid;
    private String type;
    private Message<String> feedback;
    private long time;
}
