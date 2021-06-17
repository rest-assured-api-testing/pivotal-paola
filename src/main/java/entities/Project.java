package entities;

public class Project {
    private Long id;
    private String kind;
    private String name;
    private Long version;
    private Long iteration_length;
    private String week_start_day;
    private String point_scale;
    private boolean point_scale_is_custom;
    private boolean bugs_and_chores_are_estimatable;
    private boolean automatic_planning;
    private boolean enable_tasks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getIteration_length() {
        return iteration_length;
    }

    public void setIteration_length(Long iteration_length) {
        this.iteration_length = iteration_length;
    }

    public String getWeek_start_day() {
        return week_start_day;
    }

    public void setWeek_start_day(String week_start_day) {
        this.week_start_day = week_start_day;
    }

    public String getPoint_scale() {
        return point_scale;
    }

    public void setPoint_scale(String point_scale) {
        this.point_scale = point_scale;
    }

    public boolean isPoint_scale_is_custom() {
        return point_scale_is_custom;
    }

    public void setPoint_scale_is_custom(boolean point_scale_is_custom) {
        this.point_scale_is_custom = point_scale_is_custom;
    }

    public boolean isBugs_and_chores_are_estimatable() {
        return bugs_and_chores_are_estimatable;
    }

    public void setBugs_and_chores_are_estimatable(boolean bugs_and_chores_are_estimatable) {
        this.bugs_and_chores_are_estimatable = bugs_and_chores_are_estimatable;
    }

    public boolean isAutomatic_planning() {
        return automatic_planning;
    }

    public void setAutomatic_planning(boolean automatic_planning) {
        this.automatic_planning = automatic_planning;
    }

    public boolean isEnable_tasks() {
        return enable_tasks;
    }

    public void setEnable_tasks(boolean enable_tasks) {
        this.enable_tasks = enable_tasks;
    }
}
