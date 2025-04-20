package task.service.model;

import lombok.Data;

@Data
public class Task 
{
    public Task(String title)
    {
        this.title = title;
    }

    private String title;
}
