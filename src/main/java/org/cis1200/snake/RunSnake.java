package org.cis1200.snake;

// imports necessary libraries for Java swing

import javax.swing.*;
import java.awt.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class RunSnake implements Runnable {
    public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for
        // local variables.

        // Top-level frame in which game components live.
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
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

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
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

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        field.reset();
    }
}