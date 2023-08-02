package org.example;


import java.awt.AWTException;
import java.awt.Button;
import java.awt.Frame;
import java.awt.Label;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class AppFrame extends Frame {

    private static Logger LOGGER = null;
    static {
        InputStream stream = AppFrame.class.getClassLoader().getResourceAsStream("logging.properties");
        try {
            LogManager.getLogManager().readConfiguration(stream);
            LOGGER = Logger.getLogger(AppFrame.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean enabled = true;
    Button startButton = startButton();
    Button stopButton = stopButton();
    Button exitButton = exitButton();
    Label label = label();
    Robot robot = new Robot();

    public AppFrame() throws AWTException {
        add(startButton);
        add(stopButton);
        add(exitButton);
        add(label);

        setSize(170, 180);
        setTitle("awt-tool");
        setLayout(null);
        setVisible(true);
        setResizable(false);
        start();
    }

    private Button startButton() {
        Button button = new Button("Start");
        button.setBounds(40, 30, 80, 30);
        button.addMouseListener(new StartHandler());
        return button;
    }

    private Button stopButton() {
        Button button = new Button("Stop");
        button.setBounds(40, 70, 80, 30);
        button.addMouseListener(new StopHandler());
        button.setEnabled(false);
        return button;
    }

    private Button exitButton() {
        Button button = new Button("Exit");
        button.setBounds(40, 110, 80, 30);
        button.addMouseListener(new ExitHandler());
        return button;
    }

    private Label label() {
        Label label = new Label("Stopped", Label.CENTER);
        label.setBounds(40, 160, 80, 15);
        return label;
    }

    private class StartHandler extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            start();
        }
    }

    private class StopHandler extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            stop();
        }
    }

    private class ExitHandler extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            exit();
        }
    }

    private void start() {
        label.setText("Started");
        LOGGER.info("Started!");
        enabled = true;
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        ScheduledExecutorService timerService = Executors.newSingleThreadScheduledExecutor();
        Task task = new Task(timerService);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, TimeUnit.SECONDS.toMillis(5));
    }

    private void stop() {
        label.setText("Stopped");
        LOGGER.info("Stopped!");
        enabled = false;
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
    }

    private void exit() {
        stop();
        LOGGER.info("Exit!");
        System.exit(0);
    }

    class Task extends TimerTask {
        private final ScheduledExecutorService scheduledExecutorService;

        Task(ScheduledExecutorService scheduledExecutorService) {
            this.scheduledExecutorService = scheduledExecutorService;
        }

        public void run() {
            try {
                if (enabled) {
                    Point location = MouseInfo.getPointerInfo().getLocation().getLocation();
                    robot.mouseMove(location.x + 1, location.y + 1);
                    robot.mouseMove(location.x, location.y);
                    LOGGER.info("Running...");
                } else {
                    this.cancel();
                    scheduledExecutorService.shutdown();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}