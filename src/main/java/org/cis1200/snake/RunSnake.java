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

        final JDialog instructions = new JDialog(frame, "Instructions");
        JTextArea instructionText = new JTextArea();
        instructionText.setText(
                "Instructions:\n\n " +
                        "This game is an implementation of traditional snake with a few extra features. " +
                        "\n\n The goal of the game is to eat as many fruits as possible and make the " +
                        "snake as large as possible. When the snake runs out of bounds or into itself, " +
                        "the snake dies and the game ends. Controls: W - turn up; S - turn down; " +
                        "A - turn left; D - turn right; Space - reset game\n\n " +
                        "A few game modes have been introduced beyond normal snake:\n\n " +
                        "Speed mode will allow the generation of different types of fruits. " +
                        "Bananas slow the snake down, apples return it back to normal speed, " +
                        "and peppers speed the snake up.\n\n Sinkhole mode results in the generation " +
                        "of sinkholes when the snake eats fruits. Running into the sinkhole will result " +
                        "in death and the game ending.\n\n Enemy mode will spawn an enemy snake that competes " +
                        "with you for the fruit. Running into the enemy will result in the game ending. " +
                        "It is possible (although hard) to kill the enemy"
        );
        instructionText.setEditable(false);
        instructionText.setLineWrap(true);
        instructionText.setWrapStyleWord(true);
        instructionText.setPreferredSize(new Dimension(800, 350));
        instructions.add(instructionText);
        instructions.setVisible(true);
        instructions.pack();

        // put frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // start game
        field.reset();
    }
}