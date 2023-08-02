package org.example;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class AppFrame extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(AppFrame.class.getName());

    boolean enabled = true;
    JButton button1 = button1();
    JButton button2 = button2();
    JLabel label = label();

    public AppFrame() {
        add(button1);
        add(button2);
        add(label);

        setSize(170,  200);
        setTitle("awt-tool");
        setLayout(null);
        setVisible(true);
        setResizable(false);
        start();
    }

    private JButton button1() {
        JButton button = new JButton("Start");
        button.setBounds(40, 30, 80, 30);
        button.addMouseListener(new StartHandler());
        return button;
    }

    private JButton button2() {
        JButton button = new JButton("Stop");
        button.setBounds(40, 70, 80, 30);
        button.addMouseListener(new StopHandler());
        button.setEnabled(false);
        return button;
    }

    private JLabel label() {
        JLabel label = new JLabel("Started", JLabel.CENTER);
        label.setBounds(40, 120, 80, 15);
        return  label;
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

    private void start() {
        label.setText("Started");
        LOGGER.info("Started!");
        enabled = true;
        button1.setEnabled(false);
        button2.setEnabled(true);
        try {
            final Robot robot = new Robot();
            final Timer timer = new Timer((int) TimeUnit.SECONDS.toMillis(5), null);
            timer.addActionListener(l -> {
                if(enabled) {
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
        button1.setEnabled(true);
        button2.setEnabled(false);
    }
}