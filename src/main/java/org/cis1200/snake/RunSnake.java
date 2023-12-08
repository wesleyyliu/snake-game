package org.cis1200.snake;

import javax.swing.*;
import java.awt.*;

/**
 * Game Main class
 */
public class RunSnake implements Runnable {
    public void run() {
        // top level frame
        final JFrame frame = new JFrame("Snake");
        frame.setLocation(300, 300);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.NORTH);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);
        final JLabel score = new JLabel("Score: 0");
        status_panel.add(score);
        final JLabel highScore = new JLabel("High Score: 0");
        status_panel.add(highScore);
        final JLabel time = new JLabel("Time: 0");
        status_panel.add(time);

        // Main playing area
        final Field field = new Field(status, score, highScore, time);
        frame.add(field, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.SOUTH);

        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> field.reset());
        control_panel.add(reset);

        final JRadioButton speedMode = new JRadioButton("Speed Mode");
        speedMode.addActionListener(e -> field.toggleSpeedMode());
        control_panel.add(speedMode);

        final JRadioButton sinkholeMode = new JRadioButton("Sinkhole Mode");
        sinkholeMode.addActionListener(e -> field.toggleSinkholeMode());
        control_panel.add(sinkholeMode);

        final JRadioButton enemyMode = new JRadioButton("Enemy Mode");
        enemyMode.addActionListener(e -> field.toggleEnemyMode());
        control_panel.add(enemyMode);

        // put frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // start game
        field.reset();
    }
}