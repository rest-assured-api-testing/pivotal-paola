package entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Stories {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String kind;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date created_at;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date updated_at;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int estimate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String story_type;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String current_state;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Integer request_by_id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String url;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long project_id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String[] labels;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long owned_by_id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Long> owner_ids;

    public Stories() {
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

    public Integer getRequest_by_id() {
        return request_by_id;
    }

    public void setRequest_by_id(Integer request_by_id) {
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
