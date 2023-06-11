//Task 4. Task Management System internship

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;


class t {
    private int id;
    private String name;
    private String description;
    private Date deadline;
    private boolean completed;


    public t(int id, String name, String description, Date deadline, boolean completed) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getDeadline() {
        return deadline;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override

    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return "Task ID: " + id +
                "\nName: " + name +
                "\nDescription: " + description +
                "\nDeadline: " + dateFormat.format(deadline) +
                "\nCompleted: " + completed;
    }

}
class taskManager {
    private Connection connection;
    private List<t> tasks;

    public taskManager() {
        connection = createConnection();
        tasks = new ArrayList<>();
    }

    private Connection createConnection() {
        // Database connection setup
        // Replace with your database connection details

        String url = "jdbc:mysql://localhost:3306/first";
        String username = "root";
        String password = "vihashah";


        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connection established.");
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database.");
            e.printStackTrace();
        }

        return connection;
    }

    public void addtask(t task) {
        String addTaskSQL = "INSERT INTO tasks (name, description, deadline, completed) VALUES (?, ?, ?, ?);";

        try (PreparedStatement preparedStatement = connection.prepareStatement(addTaskSQL)) {
            preparedStatement.setString(1, task.getName());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.setDate(3, new java.sql.Date(task.getDeadline().getTime()));
            preparedStatement.setBoolean(4, task.isCompleted());
            preparedStatement.executeUpdate();
            System.out.println("Task added.");
        } catch (SQLException e) {
            System.out.println("Failed to add task.");
            e.printStackTrace();
        }
    }

    public void updatetask(t task) {
        String updateTaskSQL = "UPDATE tasks SET name = ?, description = ?, deadline = ?, completed = ? WHERE id = ?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateTaskSQL)) {
            preparedStatement.setString(1, task.getName());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.setDate(3, new java.sql.Date(task.getDeadline().getTime()));
            preparedStatement.setBoolean(4, task.isCompleted());
            preparedStatement.setInt(5, task.getId());
            preparedStatement.executeUpdate();
            System.out.println("Task updated.");
        } catch (SQLException e) {
            System.out.println("Failed to update task.");
            e.printStackTrace();
        }
    }

    public void deletetask(int taskId) {
        String deleteTaskSQL = "DELETE FROM tasks WHERE id = ?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteTaskSQL)) {
            preparedStatement.setInt(1, taskId);
            preparedStatement.executeUpdate();
            System.out.println("Task deleted.");
        } catch (SQLException e) {
            System.out.println("Failed to delete task.");
            e.printStackTrace();
        }
    }

    public void marktaskAsCompleted(int taskId) {
        String markTaskCompletedSQL = "UPDATE tasks SET completed = TRUE WHERE id = ?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(markTaskCompletedSQL)) {
            preparedStatement.setInt(1, taskId);
            preparedStatement.executeUpdate();
            System.out.println("Task marked as completed.");
        } catch (SQLException e) {
            System.out.println("Failed to mark task as completed.");
            e.printStackTrace();
        }
    }

    public List<t> gettasks() {
        String getTasksSQL = "SELECT * FROM task;";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(getTasksSQL)) {
            tasks.clear();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                Date deadline = resultSet.getDate("deadline");
                boolean completed = resultSet.getBoolean("completed");

                t task = new t(id, name, description, deadline, completed);
                tasks.add(task);
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve tasks.");
            e.printStackTrace();
        }

        return tasks;
    }

    public List<t> sortTasksByDeadline() {
        List<t> sortedTasks = new ArrayList<>(tasks);
        sortedTasks.sort(Comparator.comparing(t::getDeadline));
        return sortedTasks;
    }

    public List<t> filterTasksByCompletionStatus(boolean completed) {
        List<t> filteredTasks = new ArrayList<>();

        for (t task : tasks) {
            if (task.isCompleted() == completed) {
                filteredTasks.add(task);
            }
        }

        return filteredTasks;
    }

    public List<t> filterTasksByDeadlineRange(Date startDate, Date endDate) {
        List<t> filteredTasks = new ArrayList<>();

        for (t task : tasks) {
            Date deadline = task.getDeadline();
            if (deadline.compareTo(startDate) >= 0 && deadline.compareTo(endDate) <= 0) {
                filteredTasks.add(task);
            }
        }

        return filteredTasks;
    }

    public void displayTasks() {
        String getTasksSQL = "SELECT * FROM tasks;";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(getTasksSQL)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                Date deadline = resultSet.getDate("deadline");
                boolean completed = resultSet.getBoolean("completed");

                System.out.println("Task ID: " + id +
                        ", Name: " + name +
                        ", Description: " + description +
                        ", Deadline: " + deadline +
                        ", Completed: " + completed);
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve tasks.");
            e.printStackTrace();
        }
    }

}
public class Task {
    public static void main(String[] args) {
        taskManager taskManager = new taskManager();
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        while (true) {
            System.out.println("Task Management System");
            System.out.println("1. Add Task");
            System.out.println("2. Update Task");
            System.out.println("3. Delete Task");
            System.out.println("4. Mark Task as Completed");
            System.out.println("5. List All Tasks");
            System.out.println("6. Sort Tasks by Deadline");
            System.out.println("7. Filter Tasks by Completion Status");
            System.out.println("8. Filter Tasks by Deadline Range");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 0:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                case 1:
                    System.out.print("Enter task name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter task description: ");
                    String description = scanner.nextLine();
                    System.out.print("Enter task deadline (YYYY-MM-DD): ");
                    String deadlineStr = scanner.nextLine();
                    try {
                        Date deadline = dateFormat.parse(deadlineStr);
                        t task = new t(0, name, description, deadline, false);
                        taskManager.addtask(task);
                    } catch (Exception e) {
                        System.out.println("Invalid date format. Task not added.");
                    }
                    break;
                case 2:
                    System.out.print("Enter task ID: ");
                    int taskId = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character
                    System.out.print("Enter updated task name: ");
                    String updatedName = scanner.nextLine();
                    System.out.print("Enter updated task description: ");
                    String updatedDescription = scanner.nextLine();
                    System.out.print("Enter updated task deadline (YYYY-MM-DD): ");
                    String updatedDeadlineStr = scanner.nextLine();
                    try {
                        Date updatedDeadline = dateFormat.parse(updatedDeadlineStr);
                        t updatedTask = new t(taskId, updatedName, updatedDescription, updatedDeadline, false);
                        taskManager.updatetask(updatedTask);
                    } catch (Exception e) {
                        System.out.println("Invalid date format. Task not updated.");
                    }
                    break;
                case 3:
                    System.out.print("Enter task ID: ");
                    int deleteTaskId = scanner.nextInt();
                    taskManager.deletetask(deleteTaskId);
                    break;
                case 4:
                    System.out.print("Enter task ID: ");
                    int markCompletedTaskId = scanner.nextInt();
                    taskManager.marktaskAsCompleted(markCompletedTaskId);
                    break;
//                case 5:
//                    List<t> allTasks = taskManager.gettasks();
//                    System.out.println("All Tasks:");
//                    for (t task : allTasks) {
//                        System.out.println(task);
//                    }
                case 5:
                    System.out.println("All Tasks:");
                    taskManager.displayTasks();
                    break;

//                break;
                case 6:
                    List<t> sortedTasks = taskManager.sortTasksByDeadline();
                    System.out.println("Sorted Tasks by Deadline:");
                    for (t task : sortedTasks) {
                        System.out.println(task);
                    }
                    break;
                case 7:
                    System.out.print("Enter completion status (true/false): ");
                    boolean completionStatus = scanner.nextBoolean();
                    List<t> filteredByCompletionTasks = taskManager.filterTasksByCompletionStatus(completionStatus);
                    System.out.println("Filtered Tasks by Completion Status:");
                    for (t task : filteredByCompletionTasks) {
                        System.out.println(task);
                    }
                    break;
                case 8:
                    System.out.print("Enter start date (YYYY-MM-DD): ");
                    String startDateStr = scanner.nextLine();
                    System.out.print("Enter end date (YYYY-MM-DD): ");
                    String endDateStr = scanner.nextLine();
                    try {
                        Date startDate = dateFormat.parse(startDateStr);
                        Date endDate = dateFormat.parse(endDateStr);
                        List<t> filteredByDeadlineRangeTasks = taskManager.filterTasksByDeadlineRange(startDate, endDate);
                        System.out.println("Filtered Tasks by Deadline Range:");
                        for (t task : filteredByDeadlineRangeTasks) {
                            System.out.println(task);
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid date format. Unable to filter tasks.");
                    }
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            System.out.println();
        }
    }
}

