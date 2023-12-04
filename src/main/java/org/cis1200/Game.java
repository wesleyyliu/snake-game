package org.cis1200;

import javax.swing.*;

public class Game {
    /**
     * Main method run to start and run the game. Initializes the runnable game
     * and runs it
     */
    public static void main(String[] args) {
        Runnable game = new org.cis1200.snake.RunSnake();

        SwingUtilities.invokeLater(game);
    }
}
