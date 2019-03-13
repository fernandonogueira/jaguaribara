package castanhao

import java.time.Instant
import java.time.ZoneId

class GraphResolver {

    private var tasks: Set<Task> = emptySet()
    private var taskMap: MutableMap<String, Task> = mutableMapOf()

    private var dependencySet: Set<TaskDependency> = emptySet()

    private var rootTasks: Set<Task> = emptySet()

    private var startDate: Instant = Instant.now()

    private var dependsOn: MutableMap<String, MutableSet<String>> = mutableMapOf()
    private var isParentOf: MutableMap<String, MutableSet<String>> = mutableMapOf()


    fun setTasks(taskList: Set<Task>) {
        this.tasks = taskList
    }

    fun setStartDate(startDate: Instant) {
        this.startDate = startDate
    }

    fun resolve() {

        tasks.forEach {
            taskMap[it.taskId] = it
        }

        dependencySet = tasks
                .filter { it.taskDependencies != null && it.taskDependencies.isNotEmpty() }
                .flatMap { it.taskDependencies }
                .toSet()

        dependencySet.forEach {
            if (!dependsOn.containsKey(it.childTask.taskId)) {
                dependsOn[it.childTask.taskId] = mutableSetOf()
            }

            dependsOn[it.childTask.taskId]!!.add(it.parentTask.taskId)

            if (!isParentOf.containsKey(it.parentTask.taskId)) {
                isParentOf[it.parentTask.taskId] = mutableSetOf()
            }

            isParentOf[it.parentTask.taskId]!!.add(it.childTask.taskId)
        }

        rootTasks = tasks.filter {
            !dependsOn.containsKey(it.taskId)
        }.toMutableSet()

        rootTasks.forEach {
            it.calculatedStart = startDate

            if (isParentOf.containsKey(it.taskId)) {
                val childs = isParentOf[it.taskId]!!

                childs.forEach { child ->
                    calculateChild(cursorDate = it.calculatedStart.plus(it.duration), parentTask = it, currentTask = taskMap[child]!!)
                }

            }

        }


    }

    fun calculateChild(cursorDate: Instant, parentTask: Task, currentTask: Task) {

        if (currentTask.calculatedParentTaskId != parentTask.taskId || currentTask.calculatedParentTaskId == null) {

            val parents = dependsOn[currentTask.taskId]

            if (parents != null && parents.isNotEmpty()) {
                val calculatedParents = parents.map { taskMap[it] }.filter { it!!.calculatedStart != null }
                val biggerDateTask = calculatedParents.maxBy { it!!.calculatedStart }

                val dependency: TaskDependency = getDependency(parentTask, currentTask)!!

                when (dependency.type) {
                    TaskDependencyType.FINISH_TO_START -> {
                        currentTask.calculatedParentTaskId = biggerDateTask!!.taskId
                        currentTask.calculatedStart = biggerDateTask.calculatedStart.plus(biggerDateTask.duration)
                    }
                    TaskDependencyType.FINISH_TO_FINISH -> {
                        currentTask.calculatedParentTaskId = biggerDateTask!!.taskId
                        currentTask.calculatedEnd = biggerDateTask.calculatedEnd
                        currentTask.calculatedStart = currentTask.calculatedEnd.minus(currentTask.duration)
                    }
                }

            } else {
                currentTask.calculatedParentTaskId = parentTask.taskId
                currentTask.calculatedStart = cursorDate
            }

        }

        if (isParentOf.containsKey(currentTask.taskId)) {
            isParentOf[currentTask.taskId]!!.forEach { calculateChild(parentTask = currentTask, currentTask = taskMap[it]!!, cursorDate = currentTask.calculatedStart) }
        }

    }

    private fun getDependency(parentTask: Task, currentTask: Task): TaskDependency? {
        // TODO improve
        for (it in dependencySet) {
            if (it.parentTask.taskId == parentTask.taskId && it.childTask.taskId == currentTask.taskId) {
                return it
            }
        }
        return null
    }

    fun print() {
        rootTasks.forEach {
            println(recursivePrint(it.taskId))
        }
    }

    private fun recursivePrint(taskId: String): String {

        if (isParentOf.containsKey(taskId)) {
            //has children

            val children = isParentOf[taskId]!!
            val childrenPrint = children.map { recursivePrint(it) }

            val task = taskMap[taskId]!!
            return if (task.calculatedStart != null) {
                val startDate = task.calculatedStart.atZone(ZoneId.systemDefault()).toLocalDate()
                val endDate = startDate.plusDays(task.duration.toDays())
                "[$taskId]($startDate-$endDate) => $childrenPrint"
            } else {
                "[$taskId](null) => $childrenPrint"
            }

        } else {
            val task = taskMap[taskId]!!
            return if (task.calculatedStart != null) {
                val startDate = task.calculatedStart.atZone(ZoneId.systemDefault()).toLocalDate()
                val endDate = startDate.plusDays(task.duration.toDays())
                "[$taskId]($startDate-$endDate)"
            } else {
                "[$taskId](null)"
            }

        }

    }
}
