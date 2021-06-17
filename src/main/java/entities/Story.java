package entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Story {
    private String kind;
    private Long id;
    private Date created_at;
    private Date updated_at;
    private int estimate;
    private String story_type;
    private String name;
    private String description;
    private String current_state;
    private Long request_by_id;
    private String url;
    private Long project_id;
    private String[] labels;
    private Long owned_by_id;
    private List<Long> owner_ids;

    public Story() {
        owner_ids = new ArrayList<>();
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

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public int getEstimate() {
        return estimate;
    }

    public void setEstimate(int estimate) {
        this.estimate = estimate;
    }

    public String getStory_type() {
        return story_type;
    }

    public void setStory_type(String story_type) {
        this.story_type = story_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurrent_state() {
        return current_state;
    }

    public void setCurrent_state(String current_state) {
        this.current_state = current_state;
    }

    public Long getRequest_by_id() {
        return request_by_id;
    }

    public void setRequest_by_id(Long request_by_id) {
        this.request_by_id = request_by_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getProject_id() {
        return project_id;
    }

    public void setProject_id(Long project_id) {
        this.project_id = project_id;
    }

    public String[] getLabels() {
        return labels;
    }

    public void setLabels(String[] labels) {
        this.labels = labels;
    }

    public Long getOwned_by_id() {
        return owned_by_id;
    }

    public void setOwned_by_id(Long owned_by_id) {
        this.owned_by_id = owned_by_id;
    }

    public List<Long> getOwner_ids() {
        return owner_ids;
    }

    public void addOwner_ids(Long owner_id) {
        this.owner_ids.add(owner_id);
    }
}
