package ui;

import model.*;
import persistence.JsonLoader;
import persistence.JsonSaver;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

// life planner app including GUI with habits you can track
public class LifePlannerApp extends JFrame {
    private static final String TIME_PATTERN = "KK:mma";
    private static final String FILE_PATH = "./data/savedSchedule.json";
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final String MUSIC_FILE_PATH = "./data/ding.wav";

    private ScheduledList<Appointment> apptList;
    private ScheduledList<Task> taskList;
    private HabitTrackers habits;

    private JsonSaver jsonSaver;
    private JsonLoader jsonLoader;

    private JPanel mainPanel;
    private JPanel habitPanel;
    private JPanel habitButtonPanel;

    private GridBagConstraints gridBagConstraints;

    public LifePlannerApp() {
        super("Life Planner");
        initialize();

        SwingUtilities.invokeLater(() -> {
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(WIDTH, HEIGHT);

            initializePanels();
            add(mainPanel);

            setVisible(true);
        });
    }

    // MODIFIES: this
    // EFFECTS: initializes all the trackers and lists
    private void initialize() {
        apptList = new ScheduledList<>("appointments");
        taskList = new ScheduledList<>("tasks");
        habits = new HabitTrackers();

        jsonSaver = new JsonSaver(FILE_PATH);
        jsonLoader = new JsonLoader(FILE_PATH);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        habitPanel = new JPanel(new GridBagLayout());
        habitButtonPanel = new JPanel();

        gridBagConstraints = new GridBagConstraints();
    }

    // MODIFIES: this
    // EFFECTS: initializes display panels
    private void initializePanels() {
        initializeHabitButtonPanel();

        JPanel savePanel = getSavePanel();
        setHabits(habits, habitPanel);

        mainPanel.add(savePanel);
        mainPanel.add(habitPanel);
        mainPanel.add(habitButtonPanel);
    }

    // MODIFIES: this
    // EFFECTS: initializes habit button panel to show & toggle habits, and add new habits
    private void initializeHabitButtonPanel() {
        JButton addButton = new JButton("Add Habit");
        addButton.addActionListener(e -> addNewHabitScreen());

        habitButtonPanel.add(addButton);
    }

    // MODIFIES: this
    // EFFECTS: shows habits in the given panel in which completion is toggleable
    private void setHabits(HabitTrackers habits, JPanel panel) {
        if (habits.numberOfTrackers() == 0) {
            gridBagConstraints.anchor = GridBagConstraints.CENTER;
            panel.add(new JLabel("No habits to show."));
        }
        for (int i = 0; i < habits.numberOfTrackers(); i++) {
            gridBagConstraints.anchor = GridBagConstraints.LINE_START;
            gridBagConstraints.weightx = 1;
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = i + 1;
            Tracker tracker = habits.getTracker(i);

            String name = tracker.getName();
            String combined;
            if (tracker.isDoneToday()) {
                String time = habitTimeComplete(tracker);
                combined = name + " - done at " + time;
            } else {
                combined = name;
            }

            JLabel label = new JLabel(combined);

            panel.add(label, gridBagConstraints);
        }

        displayToggleButtons(panel);
    }

    // MODIFIES: this
    // EFFECTS: allows the user to add a new habit
    private void addNewHabitScreen() {
        habitPanel.removeAll();
        gridBagConstraints.weightx = 1;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;

        JTextField nameField = new JTextField(10);
        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> addHabitToList(nameField.getText()));
        JButton backButton = new JButton("Back");
        confirmButton.addActionListener(e -> refreshHabits());

        addNewHabitButtons(nameField, confirmButton, backButton);

        mainPanel.validate();
        mainPanel.repaint();
    }

    // MODIFIES: this
    // EFFECTS: adds buttons to the new habit addition menu
    private void addNewHabitButtons(JTextField nameField, JButton confirmButton, JButton backButton) {
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        habitPanel.add(new JLabel("Name: "), gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        habitPanel.add(nameField, gridBagConstraints);

        gridBagConstraints.gridy = 1;
        habitPanel.add(confirmButton, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        habitPanel.add(backButton, gridBagConstraints);
    }

    // MODIFIES: this
    // EFFECTS: adds habit with given name and refreshes window
    private void addHabitToList(String name) {
        habits.addTracker(new Tracker(name));
        refreshHabits();
    }

    // MODIFIES: this
    // EFFECTS: displays buttons which show habit completion to toggle completion
    private void displayToggleButtons(JPanel panel) {
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        for (int i = 0; i < habits.numberOfTrackers(); i++) {
            gridBagConstraints.weightx = 1;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = i + 1;
            String text = "  ";
            if (habits.getTracker(i).isDoneToday()) {
                text = "X";
            }
            JButton toggleButton = new JButton(text);
            int index = i;
            toggleButton.addActionListener(e -> {
                processToggleHabit(index);
                refreshHabits();
            });
            panel.add(toggleButton, gridBagConstraints);
        }

        mainPanel.validate();
        mainPanel.repaint();
    }

    // MODIFIES: this
    // EFFECTS: if habit is incomplete, mark as complete, otherwise mark as incomplete
    private void processToggleHabit(int index) {
        if (!habits.getTracker(index).isDoneToday()) {
            habits.getTracker(index).markDone();
        } else if (habits.getTracker(index).isDoneToday()) {
            habits.getTracker(index).unmarkDone();
        }
    }

    // MODIFIES: this
    // EFFECTS: creates panel to save/load habits
    private JPanel getSavePanel() {
        JPanel buttonPanel = new JPanel();

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> save());
        JButton loadButton = new JButton("Load");
        loadButton.addActionListener(e -> {
            load();
            refreshHabits();
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);

        return buttonPanel;
    }

    // EFFECTS: converts last completed time in tracker to string
    private String habitTimeComplete(Tracker tracker) {
        return toTime(tracker.getLastElement());
    }

    // EFFECTS: formats the given calendar into a string with just the time
    private String toTime(Calendar given) {
        SimpleDateFormat formatted = new SimpleDateFormat(TIME_PATTERN);
        return formatted.format(given.getTime());
    }

    // EFFECTS: saves the whole app to FILE_PATH and plays a sound
    private void save() {
        try {
            jsonSaver.open();
            jsonSaver.save(apptList, taskList, habits);
            jsonSaver.close();
            playDing();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading from file: " + FILE_PATH);
        }
    }

    // MODIFIES: this
    // EFFECTS: refreshes habit display screen
    private void refreshHabits() {
        habitPanel.removeAll();
        setHabits(habits, habitPanel);
        mainPanel.validate();
        mainPanel.repaint();
    }

    // EFFECTS: plays a sound (royalty-free from Dreamstime.com)
    private void playDing() {
        try {
            File dingFile = new File(MUSIC_FILE_PATH);
            Clip ding = AudioSystem.getClip();
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(dingFile);
            ding.open(audioInput);
            ding.start();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            e.printStackTrace();
            System.out.println("Exception encountered while playing sound.");
        }
    }
}
