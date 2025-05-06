package task.service.model;

import lombok.Data;

@Data
public class Note
{
    private String uid;
    private String userUid;
    private String type;
    private String title;
    private String text;
    private Long createdAt;
    private Long updatedAt;

    @Override
    public String toString()
    {
        return "Note{" + "\nuid='" + uid + '\'' + "\n, userUid='" + userUid + '\'' + "\n, title='" + title + '\''
                + "\n, text='" + text + '\'' + "\n, createdAt=" + createdAt + "\n, updatedAt=" + updatedAt + '}';
    }
}
