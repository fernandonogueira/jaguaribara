package castanhao;

public class TaskDependency {

    public TaskDependency(Task parentTask, Task childTask) {
        this.parentTask = parentTask;
        this.childTask = childTask;
    }

    private Task parentTask;

    private Task childTask;

    private TaskDependencyType type;

    public Task getParentTask() {
        return parentTask;
    }

    public void setParentTask(Task parentTask) {
        this.parentTask = parentTask;
    }

    public Task getChildTask() {
        return childTask;
    }

    public void setChildTask(Task childTask) {
        this.childTask = childTask;
    }

    public TaskDependencyType getType() {
        return type;
    }

    public void setType(TaskDependencyType type) {
        this.type = type;
    }
}
