package task.service.model.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class FeedbackEvent
{
    private String event;
    private String feedbackUid;
    private String userUid;
    private String type;
    private String feedback;
    private long time;
}
