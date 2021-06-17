package entities;

import java.util.ArrayList;
import java.util.List;

public class Workspace {
    private String kind;
    private Long id;
    private String name;
    private Long person_id;
    private List<Long> projects_id;

    public Workspace() {
        projects_id = new ArrayList<>();
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPerson_id() {
        return person_id;
    }

    public void setPerson_id(Long person_id) {
        this.person_id = person_id;
    }

    public List<Long> getProjects_id() {
        return projects_id;
    }

    public void setProjects_id(List<Long> projects_id) {
        this.projects_id = projects_id;
    }
}
