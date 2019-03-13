package castanhao;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class Task {

    public Task(String taskId) {
        this.taskId = taskId;
    }

    public Task(long taskId) {
        this.taskId = String.valueOf(taskId);
    }

    public Task(int taskId) {
        this.taskId = String.valueOf(taskId);
    }

    public Task(int taskId, int durationInDays) {
        this.taskId = String.valueOf(taskId);
        this.duration = Duration.ofDays(durationInDays);
    }

    private String taskId;

    private Duration duration = Duration.ofDays(0);

    private Instant calculatedStart;

    private String calculatedParentTaskId;

    private Instant calculatedEnd;

    /**
     * CHILD TASKS!
     */
    private Set<TaskDependency> taskDependencies = new HashSet<>();

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Set<TaskDependency> getTaskDependencies() {
        return taskDependencies;
    }

    public void addDependency(TaskDependency taskDependency) {
        taskDependencies.add(taskDependency);
    }

    public void setTaskDependencies(Set<TaskDependency> taskDependencies) {
        this.taskDependencies = taskDependencies;
    }

    public Instant getCalculatedStart() {
        return calculatedStart;
    }

    public Instant getCalculatedEnd() {
        if (calculatedEnd == null && calculatedStart != null) {
            return calculatedStart.plus(duration);
        }
        return calculatedEnd;
    }

    public void setCalculatedEnd(Instant calculatedEnd) {
        this.calculatedEnd = calculatedEnd;
    }

    public void setCalculatedStart(Instant calculatedStart) {
        this.calculatedStart = calculatedStart;
    }

    public String getCalculatedParentTaskId() {
        return calculatedParentTaskId;
    }

    public void setCalculatedParentTaskId(String calculatedParentTaskId) {
        this.calculatedParentTaskId = calculatedParentTaskId;
    }

}
