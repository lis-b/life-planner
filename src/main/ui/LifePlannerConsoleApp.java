package ui;

import model.*;
import persistence.JsonLoader;
import persistence.JsonSaver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

// scheduling application with appointments, tasks, and habit trackers
public class LifePlannerConsoleApp {
    private static final String DATE_PATTERN = "d MMMM YYYY";
    private static final String TIME_PATTERN = "KK:mma";

    private static final String FILE_PATH = "./data/savedSchedule.json";

    private ScheduledList<Appointment> apptList;
    private ScheduledList<Task> taskList;
    private HabitTrackers habits;
    private Scanner input;

    private JsonSaver jsonSaver;
    private JsonLoader jsonLoader;

    // EFFECTS: runs the application
    public LifePlannerConsoleApp() {
        runApp();
    }

    // MODIFIES: this
    // EFFECTS: displays main menu and processes input
    private void runApp() {
        boolean keepRunning = true;
        String command;

        initialize();

        while (keepRunning) {
            displayMainMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                System.out.println("\nClosing application...");
                keepRunning = false;
            } else {
                processCommand(command);
            }
        }

    }

    // MODIFIES: this
    // EFFECTS: initializes all the trackers and lists
    private void initialize() {
        apptList = new ScheduledList<>("appointments");
        taskList = new ScheduledList<>("tasks");
        habits = new HabitTrackers();
        input = new Scanner(System.in);

        jsonSaver = new JsonSaver(FILE_PATH);
        jsonLoader = new JsonLoader(FILE_PATH);
    }

    // EFFECTS: displays main menu
    private void displayMainMenu() {
        System.out.println("\nWhat would you like to view?");
        System.out.println("\ta -> appointments");
        System.out.println("\tt -> todo list");
        System.out.println("\th -> habit trackers");
        System.out.println("\n\ts -> save");
        System.out.println("\tl -> load");
        System.out.println("\tq -> quit");
    }

    // MODIFIES: this
    // EFFECTS: processes main menu command
    private void processCommand(String command) {
        switch (command) {
            case "a":
                viewAppointments();
                break;
            case "t":
                viewTasks();
                break;
            case "h":
                viewHabits();
                break;
            case "s":
                save();
                break;
            case"l":
                load();
                break;
            default:
                invalidInput();
        }
    }

    /* A P P O I N T M E N T S */

    // MODIFIES: this
    // EFFECTS: displays appointment menu and processes input
    private void viewAppointments() {
        boolean keepRunning = true;
        String command;
        input = new Scanner(System.in);

        while (keepRunning) {
            displayAppointmentMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("b")) {
                keepRunning = false;
            } else {
                processAppointmentCommand(command);
            }
        }
    }

    // EFFECTS: displays appointment menu
    private void displayAppointmentMenu() {
        System.out.println("\nWhich appointments would you like to view?");
        System.out.println("\ta -> all appointments");
        System.out.println("\tt -> only appointments today");
        System.out.println("\tb -> back to main menu");
    }

    // MODIFIES: this
    // EFFECTS: processes input from appointment menu
    private void processAppointmentCommand(String command) {
        switch (command) {
            case "a":
                allAppointments();
                break;
            case "t":
                todayAppointments();
                break;
            default:
                invalidInput();
        }
    }

    // MODIFIES: this
    // EFFECTS: displays all appointments and processes management input
    private void allAppointments() {
        System.out.println("\nShowing all appointments...");
        printAppointments(apptList);
        appointmentManagement("");
    }

    // MODIFIES: this
    // EFFECTS: displays today's appointments and processes management input
    private void todayAppointments() {
        System.out.println("\nShowing appointments today...");
        printAppointments(apptList.listToday());
        appointmentManagement("today");
    }

    // EFFECTS: prints given list of appointments
    private void printAppointments(ScheduledList<Appointment> list) {
        if (list.length() == 0) {
            System.out.println("\tNo appointments.");
        } else {
            for (int i = 0; i < list.length(); i++) {
                Appointment temp = list.getItem(i);
                String naming = "\t" + (i + 1) + ". " + temp.getName();
                String time = appointmentTimeToString(temp);
                System.out.println(naming + " - " + time);
                if (!temp.getDescription().equals("")) {
                    System.out.println("\t\t" + temp.getDescription());
                }
            }
        }
    }

    // REQUIRES: selection of either "" for all, or "today" from appointment display
    // MODIFIES: this
    // EFFECTS: displays appointment management and processes input based on selection
    private void appointmentManagement(String selection) {
        boolean keepRunning = true;
        String command;
        input = new Scanner(System.in);

        while (keepRunning) {
            appointmentManagementMenu(selection);
            command = input.next();
            command = command.toLowerCase();

            if (!command.equals("b")) {
                processAppointmentManagement(command, selection);
            }
            keepRunning = false;
        }
    }

    // REQUIRES: selection of "" for all appointments, or "today"
    // EFFECTS: displays appointment management menu based on selection
    private void appointmentManagementMenu(String selection) {
        switch (selection) {
            case "":
                System.out.println("\nCurrently viewing all appointments.");
                break;
            case "today":
                System.out.println("\nCurrently only viewing appointments today.");
        }
        System.out.println("What would you like to do?");
        System.out.println("\ta -> add appointment");
        System.out.println("\tr -> remove appointment");
        System.out.println("\tb -> back to appointment menu");
    }

    // REQUIRES: selection of either "" for all appointments, or "today"
    // MODIFIES: this
    // EFFECTS: processes input for appointment management, using selection if removing
    private void processAppointmentManagement(String command, String selection) {
        switch (command) {
            case "a":
                addAppointment();
                break;
            case "r":
                removeAppointment(selection);
                break;
            default:
                invalidInput();
        }
    }

    // MODIFIES: this
    // EFFECTS: displays menu and processes input for adding new appointment,
    //          will not add new appointment if name and time frame are not fulfilled
    private void addAppointment() {
        boolean keepRunning = true;
        String command;
        input = new Scanner(System.in);

        Appointment temp = new Appointment();

        while (keepRunning) {
            displayAppointmentAddition(temp);
            command = input.next();

            if (command.equals("b")) {
                keepRunning = false;
            } else if (command.equals("c")) {
                if (temp.getTimePeriod() != null && !temp.getName().equals("")) {
                    apptList.addItem(temp);
                    confirmAppointment();
                    keepRunning = false;
                } else {
                    invalidFields();
                }
            } else {
                processAppointmentAddition(command, temp);
            }
        }
    }

    // EFFECTS: display new appointment addition screen with given appointment
    private void displayAppointmentAddition(Appointment appointment) {
        System.out.println("\nNew appointment...");
        if (appointment.getName().equals("")) {
            System.out.println("\tNo name\t*REQUIRED");
        } else {
            System.out.println("\tName: " + appointment.getName());
        }
        if (appointment.getDescription().equals("")) {
            System.out.println("\tNo description");
        } else {
            System.out.println("\tDescription: " + appointment.getDescription());
        }
        if (appointment.getTimePeriod() == null) {
            System.out.println("\tNo time\t*REQUIRED");
        } else {
            System.out.println("\tTime: " + appointmentTimeToString(appointment)); // ***
        }

        System.out.println("\nWhat would you like to adjust?");
        System.out.println("\tn -> name");
        System.out.println("\td -> description");
        System.out.println("\tt -> time");
        System.out.println("\tc -> confirm appointment");
        System.out.println("\tb -> back to appointment menu");
    }

    // EFFECTS: prints confirmation of appointment to user
    private void confirmAppointment() {
        System.out.print("Appointment added.\n");
    }

    // EFFECTS: processes new appointment input to adjust current appointment
    private void processAppointmentAddition(String command, Appointment appointment) {
        switch (command) {
            case "n":
                newAppointmentName(appointment);
                break;
            case "d":
                newAppointmentDescription(appointment);
                break;
            case "t":
                newAppointmentDate(appointment);
        }
    }

    // EFFECTS: displays name change menu & changes name of given appointment to user input
    private void newAppointmentName(Appointment appointment) {
        boolean keepRunning = true;
        String command;
        input = new Scanner(System.in).useDelimiter("\\n");

        while (keepRunning) {
            displayNewAppointmentNameMenu();
            command = input.next();

            if (!command.equals("b")) {
                processNewAppointmentName(command, appointment);
            }
            keepRunning = false;
        }
    }

    // EFFECTS: displays appointment name change menu
    private void displayNewAppointmentNameMenu() {
        System.out.println("\nEnter a name for the appointment:");
        System.out.println("\tb -> back");
    }

    // EFFECTS: sets name of given appointment to user input
    private void processNewAppointmentName(String input, Appointment appointment) {
        appointment.setName(input);
    }

    // EFFECTS: displays description change menu & changes description of given appointment to user input
    private void newAppointmentDescription(Appointment appointment) {
        boolean keepRunning = true;
        String command;
        input = new Scanner(System.in).useDelimiter("\\n");

        while (keepRunning) {
            displayNewAppointmentDescriptionMenu();
            command = input.next();

            if (!command.equals("b")) {
                processNewAppointmentDescription(command, appointment);
            }
            keepRunning = false;
        }
    }

    // EFFECTS: displays appointment description change menu
    private void displayNewAppointmentDescriptionMenu() {
        System.out.println("\nEnter a description for the appointment:");
        System.out.println("\tN/A for no description");
        System.out.println("\tb -> back");
    }

    // EFFECTS: sets name of given appointment to user input or blank if given "N/A"
    private void processNewAppointmentDescription(String input, Appointment appointment) {
        if (!input.equals("N/A")) {
            appointment.setDescription(input);
        } else {
            appointment.setDescription("");
        }
    }

    // EFFECTS: displays appointment date set menu & changes date of appointment to user input
    private void newAppointmentDate(Appointment appointment) {
        boolean keepRunning = true;
        String command;
        input = new Scanner(System.in);

        appointment.setTimePeriod(new CalendarInterval(Calendar.getInstance()));

        while (keepRunning) {
            displayNewAppointmentDateMenu();
            command = input.next();

            if (command.equals("b")) {
                keepRunning = false;
                appointment.setTimePeriod(null);
            } else {
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    appointment.getTimePeriod().getStart().setTime(formatter.parse(command));
                    newAppointmentStartTime(appointment);
                    keepRunning = false;
                } catch (Exception e) {
                    invalidInput();
                }
            }
        }
    }

    // EFFECTS: displays new appointment date set menu
    private void displayNewAppointmentDateMenu() {
        System.out.println("\nEnter a date for the appointment:");
        System.out.println("\tYYYY-MM-DD format");
        System.out.println("\tb -> back");
    }

    // EFFECTS: displays new appointment time set menu & processes user input
    private void newAppointmentStartTime(Appointment appointment) {
        boolean keepRunning = true;
        String command;
        input = new Scanner(System.in);

        while (keepRunning) {
            displayNewAppointmentStartTimeMenu();
            command = input.next();

            if (command.equals("b")) {
                keepRunning = false;
                appointment.setTimePeriod(null);
            } else {
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                    Calendar formattedDate = Calendar.getInstance();
                    formattedDate.setTime(formatter.parse(command));
                    setAppointmentTimeToDate(appointment, formattedDate);
                    newAppointmentLengthHour(appointment);
                    keepRunning = false;
                } catch (Exception e) {
                    invalidInput();
                }
            }
        }
    }

    // EFFECTS: displays new appointment time set menu
    private void displayNewAppointmentStartTimeMenu() {
        System.out.println("\nEnter a start time for the appointment:");
        System.out.println("\thh:mm format using 24-hour clock");
        System.out.println("\tb -> back");
    }

    // EFFECTS: changes the time on given appointment start date to match given time
    private void setAppointmentTimeToDate(Appointment appointment, Calendar time) {
        appointment.getTimePeriod().getStart().set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
        appointment.getTimePeriod().getStart().set(Calendar.MINUTE, time.get(Calendar.MINUTE));
    }

    // EFFECTS: displays new appointment hour length menu & processes user input
    private void newAppointmentLengthHour(Appointment appointment) {
        boolean keepRunning = true;
        String command;
        input = new Scanner(System.in);

        while (keepRunning) {
            displayNewAppointmentLengthHourMenu();
            command = input.next();

            if (command.equals("b")) {
                keepRunning = false;
                appointment.setTimePeriod(null);
            } else {
                try {
                    if (Integer.parseInt(command) >= 0) {
                        processAppointmentHours(Integer.parseInt(command), appointment);
                        newAppointmentLengthMinute(appointment);
                        keepRunning = false;
                    } else {
                        invalidInput();
                    }
                } catch (NumberFormatException nfe) {
                    invalidInput();
                }
            }
        }
    }

    // EFFECTS: displays new appointment hour length menu
    private void displayNewAppointmentLengthHourMenu() {
        System.out.println("\nEnter the number of hours:");
        System.out.println("\tMinutes will be entered next.");
        System.out.println("\tb -> back");
    }

    // EFFECTS: processes user input to set hours for the new appointment
    private void processAppointmentHours(int hours, Appointment appointment) {
        appointment.getTimePeriod().setHours(hours);
    }

    // EFFECTS: displays new appointment minute length menu & processes user input
    private void newAppointmentLengthMinute(Appointment appointment) {
        boolean keepRunning = true;
        String command;
        input = new Scanner(System.in);

        while (keepRunning) {
            displayNewAppointmentLengthMinuteMenu();
            command = input.next();

            if (command.equals("b")) {
                keepRunning = false;
                appointment.setTimePeriod(null);
            } else {
                try {
                    int minutes = Integer.parseInt(command);
                    if (minutes >= 0) {
                        processAppointmentMinutes(minutes, appointment);
                        keepRunning = false;
                    } else {
                        invalidInput();
                    }
                } catch (NumberFormatException nfe) {
                    invalidInput();
                }
            }
        }
    }

    // EFFECTS: displays new appointment minute length menu
    private void displayNewAppointmentLengthMinuteMenu() {
        System.out.println("\nEnter the number of minutes:");
        System.out.println("\tb -> back");
    }

    // EFFECTS: processes user input to set minute length for new appointment
    private void processAppointmentMinutes(int minutes, Appointment appointment) {
        appointment.getTimePeriod().setMinutes(minutes);
    }

    // REQUIRES: selection based on "" for all, or "today"
    // MODIFIES: this
    // EFFECTS: displays appointment removal screen based on selection and processes user input
    private void removeAppointment(String selection) {
        boolean keepRunning = true;
        String command;
        input = new Scanner(System.in);

        while (keepRunning) {
            displayAppointmentRemoval(selection);
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("b")) {
                keepRunning = false;
            } else {
                try {
                    int index = Integer.parseInt(command) - 1;
                    if (isValidIndex(index, "appointment", selection)) {
                        processAppointmentRemoval(index, selection);
                        keepRunning = false;
                    } else {
                        invalidInput();
                    }
                } catch (NumberFormatException nfe) {
                    invalidInput();
                }
            }
        }
    }

    // REQUIRES: selection of either "" for all, or "today"
    // EFFECTS: displays menu for appointment removal based on selection
    private void displayAppointmentRemoval(String selection) {
        switch (selection) {
            case "":
                System.out.println("\nWhich appointment would you like to remove?");
                break;
            case "today":
                System.out.println("\nWhich of today's appointments would you like to remove?");
        }
        System.out.println("\tInput bullet point number of corresponding appointment.");
        System.out.println("\t b -> back to appointment management");
    }

    // REQUIRES: selection of either "" for all, or "today"
    // MODIFIES: this
    // EFFECTS: processes appointment removal
    private void processAppointmentRemoval(int index, String selection) {
        Appointment toRemove;
        switch (selection) {
            case "":
                toRemove = apptList.getItem(index);
                apptList.removeItem(toRemove);
                break;
            case "today":
                toRemove = apptList.listToday().getItem(index);
                apptList.removeItem(toRemove);
        }
        completeAppointmentRemoval();
    }

    // EFFECTS: displays confirmation of appointment removal
    private void completeAppointmentRemoval() {
        System.out.println("Appointment successfully removed.");
    }

    // EFFECTS: converts appointment time to a string
    private String appointmentTimeToString(Appointment appointment) {
        String startDate = toDate(appointment.getStartTime());
        String startTime = toTime(appointment.getStartTime());
        String endDate = toDate(appointment.getEndTime());
        String endTime = toTime(appointment.getEndTime());
        String date;
        if (endDate.equals(startDate)) {
            date = startDate + " from " + startTime + " to " + endTime;
        } else {
            date = startDate + " " + startTime + " to " + endDate + " " + endTime;
        }
        return date;
    }

    /* T A S K S */

    // MODIFIES: this
    // EFFECTS: displays task menu and processes input
    private void viewTasks() {
        boolean keepRunning = true;
        String command;
        input = new Scanner(System.in);

        while (keepRunning) {
            displayTaskMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("b")) {
                keepRunning = false;
            } else {
                processTaskCommand(command);
            }
        }
    }

    // EFFECTS: displays task menu
    private void displayTaskMenu() {
        System.out.println("\nWhich task would you like to view?");
        System.out.println("\ta -> all tasks");
        System.out.println("\tt -> only tasks today");
        System.out.println("\tb -> back to main menu");
    }

    // EFFECTS: processes input for task menu
    private void processTaskCommand(String command) {
        switch (command) {
            case "a":
                viewAllTasks();
                break;
            case "t":
                viewTodayTasks();
        }
    }

    // MODIFIES: this
    // EFFECTS: displays all tasks and processes management input
    private void viewAllTasks() {
        System.out.println("\nShowing all tasks...");
        printTasks(taskList);
        taskManagement("");
    }

    // MODIFIES: this
    // EFFECTS: displays today's tasks and processes management input
    private void viewTodayTasks() {
        System.out.println("\nShowing today's tasks...");
        printTasks(taskList.listToday());
        taskManagement("today");
    }

    // EFFECTS: prints given task list
    private void printTasks(ScheduledList<Task> list) {
        if (list.length() == 0) {
            System.out.println("\tNo tasks.");
        } else {
            for (int i = 0; i < list.length(); i++) {
                Task temp = list.getItem(i);
                String naming = (i + 1) + ". " + temp.getName();
                String time = taskTimeToString(list.getItem(i));
                String completion = "[ ]";
                if (temp.isComplete()) {
                    completion = "[X]";
                }
                System.out.println("\t" + completion + " " + naming + " - due " + time);
                if (!temp.getDescription().equals("")) {
                    System.out.println("\t\t" + temp.getDescription());
                }
            }
        }
    }

    // REQUIRES: selection of either "" for all, or "today" from task display
    // MODIFIES: this
    // EFFECTS: displays task management and processes input based on selection
    private void taskManagement(String selection) {
        boolean keepRunning = true;
        String command;
        input = new Scanner(System.in);

        while (keepRunning) {
            taskManagementMenu(selection);
            command = input.next();
            command = command.toLowerCase();

            if (!command.equals("b")) {
                processTaskManagement(command, selection);
            }
            keepRunning = false;
        }
    }

    // REQUIRES: selection of either "" for all, or "today"
    // EFFECTS: displays task management
    private void taskManagementMenu(String selection) {
        switch (selection) {
            case "":
                System.out.println("\nCurrently viewing all tasks.");
                break;
            case "today":
                System.out.println("\nCurrently only viewing today's tasks.");
        }
        System.out.println("What would you like to do?");
        System.out.println("\ta -> add task");
        System.out.println("\tr -> remove task");
        System.out.println("\tt -> toggle task completion");
        System.out.println("\tb -> back to task menu");
    }

    // REQUIRES: selection of either "" for all, or "today"
    // MODIFIES: this
    // EFFECTS: processes uer input, utilizing selection if removing or toggling
    private void processTaskManagement(String command, String selection) {
        switch (command) {
            case "a":
                addTask();
                break;
            case "r":
                removeTask(selection);
                break;
            case "t":
                toggleTaskCompletion(selection);
                break;
            default:
                invalidInput();
        }
    }

    // MODIFIES: this
    // EFFECTS: displays task addition menu and processes user input,
    //          will not allow confirmation of task creation if no time or name given
    private void addTask() {
        boolean keepRunning = true;
        String command;
        input = new Scanner(System.in);

        Task temp = new Task();

        while (keepRunning) {
            displayTaskAddition(temp);
            command = input.next();

            if (command.equals("b")) {
                keepRunning = false;
            } else if (command.equals("c")) {
                if (temp.getDue() != null && !temp.getName().equals("")) {
                    taskList.addItem(temp);
                    confirmTask();
                    keepRunning = false;
                } else {
                    invalidFields();
                }
            } else {
                processTaskAddition(command, temp);
            }
        }
    }

    // EFFECTS: display new task addition screen with given task
    private void displayTaskAddition(Task task) {
        System.out.println("\nNew task...");
        if (task.getName().equals("")) {
            System.out.println("\tNo name\t*REQUIRED");
        } else {
            System.out.println("\tName: " + task.getName());
        }
        if (task.getDescription().equals("")) {
            System.out.println("\tNo description");
        } else {
            System.out.println("\tDescription: " + task.getDescription());
        }
        if (task.getDue() == null) {
            System.out.println("\tNo time\t*REQUIRED");
        } else {
            System.out.println("\tTime: " + taskTimeToString(task)); // ***
        }

        System.out.println("\nWhat would you like to adjust?");
        System.out.println("\tn -> name");
        System.out.println("\td -> description");
        System.out.println("\tt -> time");
        System.out.println("\tc -> confirm task");
        System.out.println("\tb -> back to task menu");
    }

    // EFFECTS: prints confirmation of task to user
    private void confirmTask() {
        System.out.println("Task added.");
    }

    // EFFECTS: processes new appointment input to adjust current task in the making
    private void processTaskAddition(String command, Task task) {
        switch (command) {
            case "n":
                newTaskName(task);
                break;
            case "d":
                newTaskDescription(task);
                break;
            case "t":
                newTaskDate(task);
        }
    }

    // EFFECTS: displays new task name change menu & changes name of given task to user input
    private void newTaskName(Task task) {
        boolean keepRunning = true;
        String command;
        input = new Scanner(System.in).useDelimiter("\\n");

        while (keepRunning) {
            displayNewTaskNameMenu();
            command = input.next();

            if (!command.equals("b")) {
                processNewTaskName(command, task);
            }
            keepRunning = false;
        }
    }

    // EFFECTS: displays new task name change menu
    private void displayNewTaskNameMenu() {
        System.out.println("\nEnter a name for the task:");
        System.out.println("\tb -> back");
    }

    // EFFECTS: changes name of given new task to user input
    private void processNewTaskName(String input, Task task) {
        task.setName(input);
    }

    // EFFECTS: displays description change menu & changes description of the new task to user input
    private void newTaskDescription(Task task) {
        boolean keepRunning = true;
        String command;
        input = new Scanner(System.in).useDelimiter("\\n");

        while (keepRunning) {
            displayNewTaskDescriptionMenu();
            command = input.next();

            if (!command.equals("b")) {
                processNewTaskDescription(command, task);
            }
            keepRunning = false;
        }
    }

    // EFFECTS: displays new task description change menu
    private void displayNewTaskDescriptionMenu() {
        System.out.println("\nEnter a description for the task:");
        System.out.println("\tN/A for no description");
        System.out.println("\tb -> back");
    }

    // EFFECTS: sets name of new task to user input or blank if given "N/A"
    private void processNewTaskDescription(String input, Task task) {
        if (!input.equals("N/A")) {
            task.setDescription(input);
        } else {
            task.setDescription("");
        }
    }

    // EFFECTS: displays new task due date set menu & changes due date of task to user input
    private void newTaskDate(Task task) {
        boolean keepRunning = true;
        String command;
        input = new Scanner(System.in);

        task.setDue(Calendar.getInstance());

        while (keepRunning) {
            displayNewTaskDateMenu();
            command = input.next();

            if (command.equals("b")) {
                keepRunning = false;
                task.setDue(null);
            } else {
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    task.getDue().setTime(formatter.parse(command));
                    newTaskTime(task);
                    keepRunning = false;
                } catch (Exception e) {
                    invalidInput();
                }
            }
        }
    }

    // EFFECTS: displays new task due date set menu
    private void displayNewTaskDateMenu() {
        System.out.println("\nEnter a due date for the task:");
        System.out.println("\tYYYY-MM-DD format");
        System.out.println("\tb -> back");
    }

    // EFFECTS: displays new task due time set menu & processes user input
    private void newTaskTime(Task task) {
        boolean keepRunning = true;
        String command;
        input = new Scanner(System.in);

        while (keepRunning) {
            displayNewTaskTimeMenu();
            command = input.next();

            if (command.equals("b")) {
                keepRunning = false;
                task.setDue(null);
            } else {
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                    Calendar formattedDate = Calendar.getInstance();
                    formattedDate.setTime(formatter.parse(command));
                    setTaskTimeToDate(task, formattedDate);
                    keepRunning = false;
                } catch (Exception e) {
                    invalidInput();
                }
            }
        }
    }

    // EFFECTS: displays new task due time set menu
    private void displayNewTaskTimeMenu() {
        System.out.println("\nEnter a due time for the task:");
        System.out.println("\thh:mm format using 24-hour clock");
        System.out.println("\tb -> back");
    }

    // EFFECTS: changes the time on given task due date to match given time
    private void setTaskTimeToDate(Task task, Calendar time) {
        task.getDue().set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
        task.getDue().set(Calendar.MINUTE, time.get(Calendar.MINUTE));
    }

    // REQUIRES: selection based on "" for all, or "today"
    // MODIFIES: this
    // EFFECTS: displays task removal screen based on selection and processes user input
    private void removeTask(String selection) {
        boolean keepRunning = true;
        String command;
        input = new Scanner(System.in);

        while (keepRunning) {
            displayTaskRemoval(selection);
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("b")) {
                keepRunning = false;
            } else {
                try {
                    int index = Integer.parseInt(command) - 1;
                    if (isValidIndex(index, "task", selection)) {
                        processTaskRemoval(index, selection);
                        keepRunning = false;
                    } else {
                        invalidInput();
                    }
                } catch (NumberFormatException nfe) {
                    invalidInput();
                }
            }
        }
    }

    // REQUIRES: selection of either "" for all, or "today"
    // EFFECTS: displays menu for task removal based on selection
    private void displayTaskRemoval(String selection) {
        switch (selection) {
            case "":
                System.out.println("\nWhich task would you like to remove?");
                break;
            case "today":
                System.out.println("\nWhich of today's tasks would you like to remove?");
        }
        System.out.println("\tInput bullet point number of corresponding task.");
        System.out.println("\t b -> back to task management");
    }

    // REQUIRES: selection of either "" for all, or "today"
    // MODIFIES: this
    // EFFECTS: processes task removal
    private void processTaskRemoval(int index, String selection) {
        Task toRemove;
        switch (selection) {
            case "":
                toRemove = taskList.getItem(index);
                taskList.removeItem(toRemove);
                break;
            case "today":
                toRemove = taskList.listToday().getItem(index);
                taskList.removeItem(toRemove);
        }
        completeTaskRemoval();
    }

    // EFFECTS: displays task of appointment removal
    private void completeTaskRemoval() {
        System.out.println("Task successfully removed.");
    }

    // REQUIRES: selection based on "" for all, or "today"
    // MODIFIES: this
    // EFFECTS: displays task toggle screen based on selection and processes user input
    private void toggleTaskCompletion(String selection) {
        boolean keepRunning = true;
        String command;
        input = new Scanner(System.in);

        while (keepRunning) {
            displayAppointmentRemoval(selection);
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("b")) {
                keepRunning = false;
            } else {
                try {
                    int index = Integer.parseInt(command) - 1;
                    if (isValidIndex(index, "task", selection)) {
                        processToggleTaskCompletion(index, selection);
                        keepRunning = false;
                    } else {
                        invalidInput();
                    }
                } catch (NumberFormatException nfe) {
                    invalidInput();
                }
            }
        }
    }

    // REQUIRES: selection based on "" for all, or "today"
    // MODIFIES: this
    // EFFECTS: processes user input for toggle of task based on selection
    private void processToggleTaskCompletion(int index, String selection) {
        switch (selection) {
            case "":
                taskList.getItem(index).toggleCompletion();
                break;
            case "today":
                taskList.listToday().getItem(index).toggleCompletion();
        }
        System.out.println("Task completion toggled.");
    }

    // EFFECTS: converts task time toa string
    private String taskTimeToString(Task task) {
        String date = toDate(task.getDue());
        String time = toTime(task.getDue());
        return date + " at " + time;
    }

    /* H A B I T S */

    // MODIFIES: this
    // EFFECTS: displays habit trackers and menu and processes input
    private void viewHabits() {
        boolean keepRunning = true;
        String command;

        while (keepRunning) {
            displayHabitMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("b")) {
                keepRunning = false;
            } else {
                processHabitCommand(command);
            }
        }
    }

    // EFFECTS: displays habit trackers and menu
    private void displayHabitMenu() {
        System.out.println("\nShowing all habits...");
        printHabits();

        System.out.println("\nWhat would you like to do?");
        System.out.println("\tm -> mark a habit done today");
        System.out.println("\tu -> undo a habit marked done today");
        System.out.println("\ta -> add a habit tracker");
        System.out.println("\tr -> remove a habit tracker");
        System.out.println("\tb -> back to main menu");
    }

    // EFFECTS: prints out all habit trackers with today's completion and time, if applicable
    private void printHabits() {
        if (habits.numberOfTrackers() != 0) {
            for (int i = 0; i < habits.numberOfTrackers(); i++) {
                Tracker temp = habits.getTracker(i);
                String naming = "\t" + (i + 1) + ". " + temp.getName();
                if (temp.isDoneToday()) {
                    String time = habitTimeComplete(temp);
                    System.out.println("\t[X]" + naming + " - done at " + time);
                } else {
                    System.out.println("\t[ ]" + naming);
                }
            }
        } else {
            System.out.println("No habit trackers.");
        }
    }

    // EFFECTS: converts last completed time in tracker to string
    private String habitTimeComplete(Tracker tracker) {
        return toTime(tracker.getLastElement());
    }

    // MODIFIES: this
    // EFFECTS: processes user input for habit tracker management
    private void processHabitCommand(String command) {
        switch (command) {
            case "m":
                markHabitDone();
                break;
            case "u":
                unmarkHabitDone();
                break;
            case "a":
                addHabitTracker();
                break;
            case "r":
                removeHabitTracker();
                break;
            default:
                invalidInput();
        }
    }

    // MODIFIES: this
    // EFFECTS: displays menu to mark habit as complete today and processes user input
    private void markHabitDone() {
        boolean keepRunning = true;
        String command;
        input = new Scanner(System.in);

        while (keepRunning) {
            displayMarkHabitMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("b")) {
                keepRunning = false;
            } else {
                try {
                    int index = Integer.parseInt(command) - 1;
                    if (isValidIndex(index, "habits", "")) {
                        processMarkHabit(index);
                        keepRunning = false;
                    } else {
                        invalidInput();
                    }
                } catch (NumberFormatException nfe) {
                    invalidInput();
                }
            }
        }
    }

    // EFFECTS: displays menu to mark habit as complete today
    private void displayMarkHabitMenu() {
        System.out.println("\nWhich habit would you like to mark complete today?");
        System.out.println("\tInput bullet point number of corresponding habit.");
        System.out.println("\t b -> back to habit menu");
    }

    // REQUIRES: valid index
    // MODIFIES: this
    // EFFECTS: marks incomplete tracker as complete, nothing if already complete,
    //          and displays message saying what was done
    private void processMarkHabit(int index) {
        if (!habits.getTracker(index).isDoneToday()) {
            habits.getTracker(index).markDone();
            System.out.println("Tracker marked as done today.");
        } else {
            System.out.println("Tracker already marked as done today.");
        }
    }

    // MODIFIES: this
    // EFFECTS: displays menu to mark habit as incomplete today and processes user input
    private void unmarkHabitDone() {
        boolean keepRunning = true;
        String command;
        input = new Scanner(System.in);

        while (keepRunning) {
            displayUnmarkHabitMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("b")) {
                keepRunning = false;
            } else {
                try {
                    int index = Integer.parseInt(command) - 1;
                    if (isValidIndex(index, "habits", "")) {
                        processUnmarkHabit(index);
                        keepRunning = false;
                    } else {
                        invalidInput();
                    }
                } catch (NumberFormatException nfe) {
                    invalidInput();
                }
            }
        }
    }

    // EFFECTS: displays menu to mark habit as incomplete today
    private void displayUnmarkHabitMenu() {
        System.out.println("\nWhich habit would you like to undo today?");
        System.out.println("\tInput bullet point number of corresponding habit.");
        System.out.println("\t b -> back to habit menu");
    }

    // REQUIRES: valid index
    // MODIFIES: this
    // EFFECTS: marks complete tracker as incomplete, nothing if already incomplete,
    //          and displays message saying what was done
    private void processUnmarkHabit(int index) {
        if (habits.getTracker(index).isDoneToday()) {
            habits.getTracker(index).unmarkDone();
            System.out.println("Tracker unmarked as done today.");
        } else {
            System.out.println("Tracker not done today.");
        }
    }

    // MODIFIES: this
    // EFFECTS: displays prompt to add name for a new tracker and processes input
    private void addHabitTracker() {
        boolean keepRunning = true;
        String command;
        input = new Scanner(System.in).useDelimiter("\\n");

        while (keepRunning) {
            displayAddHabitMenu();
            command = input.next();
            command = command.toLowerCase();

            if (!command.equals("b")) {
                habits.addTracker(new Tracker(command));
                System.out.println("New tracker successfully added!");
            }
            keepRunning = false;
        }
    }

    // EFFECTS: displays prompt to add name for a new tracker
    private void displayAddHabitMenu() {
        System.out.println("\nWhat additional habit would you like to track?");
    }

    // MODIFIES: this
    // EFFECTS: displays prompt to remove a tracker and processes input
    private void removeHabitTracker() {
        boolean keepRunning = true;
        String command;
        input = new Scanner(System.in);

        while (keepRunning) {
            displayRemoveHabitMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("b")) {
                keepRunning = false;
            } else {
                try {
                    int index = Integer.parseInt(command) - 1;
                    if (isValidIndex(index, "habits", "")) {
                        processRemoveHabit(index);
                        keepRunning = false;
                    } else {
                        invalidInput();
                    }
                } catch (NumberFormatException nfe) {
                    invalidInput();
                }
            }
        }
    }

    // EFFECTS: displays prompt to remove a tracker
    private void displayRemoveHabitMenu() {
        System.out.println("\nWhich habit tracker would you like to remove?");
        System.out.println("\tInput bullet point number of corresponding habit.");
        System.out.println("\t b -> back to habit menu");
    }

    // REQUIRES: valid index
    // MODIFIES: this
    // EFFECTS: processes input to remove a tracker
    private void processRemoveHabit(int index) {
        habits.removeTracker(index);
        System.out.println("Tracker successfully removed!");
    }

    /* S A V I N G  &  L O A D I N G */

    // EFFECTS: saves the whole app to FILE_PATH
    private void save() {
        try {
            jsonSaver.open();
            jsonSaver.save(apptList, taskList, habits);
            jsonSaver.close();
            System.out.println("Saved to " + FILE_PATH);
        } catch (FileNotFoundException e) {
            System.out.println("Error saving to file: " + FILE_PATH);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads the whole app from FILE_PATH
    private void load() {
        try {
            apptList = jsonLoader.loadAppointments();
            taskList = jsonLoader.loadTasks();
            habits = jsonLoader.loadHabits();
            System.out.println("Loaded from " + FILE_PATH);
        } catch (IOException e) {
            System.out.println("Error loading from file: " + FILE_PATH);
        }

    }

    /* H E L P E R   F U N C T I O N S */

    // EFFECTS: displays that input is invalid
    private void invalidInput() {
        System.out.println("Invalid input.");
    }

    // EFFECTS: displays that there has been no name and/or time input
    private void invalidFields() {
        System.out.println("Error: No name and/or time has been input.");
    }

    // REQUIRES: category of "appointment", "task", or "habits" and
    //           selection of "" for all or "today" (appointments and tasks only
    // EFFECTS: determines if given integer is a valid index for the list associated
    //          with the category and selection
    private boolean isValidIndex(int index, String category, String selection) {
        int length = 0;
        switch (selection) {
            case "":
                switch (category) {
                    case "appointment":
                        length = apptList.length();
                        break;
                    case "task":
                        length = taskList.length();
                        break;
                    case "habits":
                        length = habits.numberOfTrackers();
                }
                break;
            case "today":
                if (category.equals("appointment")) {
                    length = apptList.listToday().length();
                } else if (category.equals("task")) {
                    length = taskList.listToday().length();
                }
        }
        return index >= 0 && index < length;
    }

    // EFFECTS: formats the given calendar into a string with just the date
    private String toDate(Calendar given) {
        SimpleDateFormat formatted = new SimpleDateFormat(DATE_PATTERN);
        return formatted.format(given.getTime());
    }

    // EFFECTS: formats the given calendar into a string with just the time
    private String toTime(Calendar given) {
        SimpleDateFormat formatted = new SimpleDateFormat(TIME_PATTERN);
        return formatted.format(given.getTime());
    }
}