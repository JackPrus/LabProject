package by.prus.LabProject.model.dto;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class TagDTO {

    private static final long serialVersionUID = 2344432456723452346L;

    private long id;
    private String name;

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
