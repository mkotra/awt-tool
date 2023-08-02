package org.example;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class AppFrame extends Frame {

    private static final Logger LOGGER = Logger.getLogger(AppFrame.class.getName());

    boolean enabled = true;
    Button startButton = startButton();
    Button stopButton = stopButton();
    Button exitButton = exitButton();
    Label label = label();

    public AppFrame() {
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
        try {
            final Robot robot = new Robot();
            final Timer timer = new Timer((int) TimeUnit.SECONDS.toMillis(5), null);
            timer.addActionListener(l -> {
                if (enabled) {
                    Point location = MouseInfo.getPointerInfo().getLocation().getLocation();
                    robot.mouseMove(location.x + 15, location.y + 15);
                    robot.mouseMove(location.x, location.y);
                    LOGGER.info("Running...");
                } else {
                    timer.stop();
                }
            });
            timer.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
}