package castanhao

enum class TaskDependencyType {
    START_TO_START, FINISH_TO_START,
    FINISH_TO_FINISH, START_TO_FINISH
}