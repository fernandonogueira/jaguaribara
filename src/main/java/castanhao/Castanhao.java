package castanhao;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class Castanhao {

    public static void main(String[] args) {
        Instant startDate = Instant.now();

        // Root
        Task task1 = new Task(1, 1);

        Task task2 = new Task(2, 2);

        TaskDependency td1 = new TaskDependency(task1, task2);
        task1.addDependency(td1);


        Task task3 = new Task(3, 3);
        TaskDependency task2_3 = new TaskDependency(task2, task3);
        task2.addDependency(task2_3);

        Task task4 = new Task(4, 4);

        TaskDependency task2_4 = new TaskDependency(task2, task4);
        task2.addDependency(task2_4);

        Task task5 = new Task(5, 5);
        TaskDependency task5_2 = new TaskDependency(task5, task2);
        task5.addDependency(task5_2);


        Set<Task> taskList = new HashSet<>();
        taskList.add(task1);
        taskList.add(task2);
        taskList.add(task3);
        taskList.add(task4);
        taskList.add(task5);

        GraphResolver resolver = new GraphResolver();
        resolver.setStartDate(startDate);
        resolver.setTasks(taskList);
        resolver.resolve();

        resolver.print();



    }
}
