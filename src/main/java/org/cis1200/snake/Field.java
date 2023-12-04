package org.cis1200.snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Random;

/**
 * GameCourt
 *
 * This class holds the primary game logic for how different objects interact
 * with one another. Take time to understand how the timer interacts with the
 * different methods and how it repaints the GUI on every tick().
 */
public class Field extends JPanel {

    // the state of the game logic
    private final GameObject[][] gameField;
    private Snake snake; // the Black Square, keyboard control
    private Enemy enemy; // the Golden Snitch, bounces
    private boolean enemyIsAlive;
    private HashSet<Grass> grasses = new HashSet<>();
    private HashSet<Sinkhole> sinkholes = new HashSet<>();
    private boolean snakeJustAteFruit = false;
    private boolean enemyJustAteFruit = false;
    private boolean speedMode = false;
    private boolean sinkholeMode = false;
    private boolean enemyMode = false;
    private boolean playing = false; // whether the game is running
    private final JLabel status; // Current status text, i.e. "Running..."
    private int currScore = 0;
    private int highScore = 0;
    private final JLabel scoreLabel;
    private final JLabel highScoreLabel;
    private final JLabel timeLabel;
    // Game constants
    public static final int FIELD_WIDTH = 600;
    public static final int FIELD_HEIGHT = 600;

    public static final int BLOCK_WIDTH = 40;
    public static final int BLOCK_HEIGHT = 40;

    // Update interval for timer, in milliseconds
    private Timer startTimer;
    private int currTime;
    private Timer intervalTimer;
    private final int initInterval = 150;

    public Field(JLabel status, JLabel scoreLabel, JLabel highScoreLabel, JLabel timeLabel) {
        int arrayWidth = FIELD_WIDTH / BLOCK_WIDTH;
        int arrayHeight = FIELD_HEIGHT / BLOCK_HEIGHT;
        gameField = new GameObject[arrayHeight][arrayWidth];
        for (int i = 0; i < arrayHeight; i++) {
            for (int j = 0; j < arrayWidth; j++) {
                Grass grassBlock = new Grass(j, i);
                gameField[i][j] = grassBlock;
                grasses.add(grassBlock);
            }
        }
        snake = new Snake();
        for (GameObject snakeBlock : snake.getMovingObject()) {
            int y = snakeBlock.getY();
            int x = snakeBlock.getX();
            grasses.remove((Grass) gameField[y][x]);
            gameField[y][x] = snakeBlock;
        }
        generateFruit();
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // The timer is an object which triggers an action periodically with the
        // given INTERVAL. We register an ActionListener with this timer, whose
        // actionPerformed() method is called each time the timer triggers. We
        // define a helper method called tick() that actually does everything
        // that should be done in a single time step.
        intervalTimer = new Timer(initInterval, e -> tick());
        intervalTimer.start(); // MAKE SURE TO START THE TIMER!

        startTimer = new Timer(1000, e -> currTime++);
        startTimer.start();

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // This key listener allows the square to move as long as an arrow key
        // is pressed, by changing the square's velocity accordingly. (The tick
        // method below actually moves the square.)
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int snakeHeadX = snake.getHead().getX();
                int snakeHeadY = snake.getHead().getY();
                int secondSnakeBlockX = snake.getSecondBlock().getX();
                int secondSnakeBlockY = snake.getSecondBlock().getY();
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> {
                        if (!(snakeHeadX == secondSnakeBlockX + 1
                                && snakeHeadY == secondSnakeBlockY)) {
                            snake.setDirection(Direction.LEFT);
                        }
                    }
                    case KeyEvent.VK_RIGHT -> {
                        if (!(snakeHeadX == secondSnakeBlockX - 1
                                && snakeHeadY == secondSnakeBlockY)) {
                            snake.setDirection(Direction.RIGHT);
                        }
                    }
                    case KeyEvent.VK_DOWN -> {
                        if (!(snakeHeadX == secondSnakeBlockX
                                && snakeHeadY == secondSnakeBlockY - 1)) {
                            snake.setDirection(Direction.DOWN);
                        }
                    }
                    case KeyEvent.VK_UP -> {
                        if (!(snakeHeadX == secondSnakeBlockX
                                && snakeHeadY == secondSnakeBlockY + 1)) {
                            snake.setDirection(Direction.UP);
                        }
                    }
                    case KeyEvent.VK_SPACE -> reset();
                    default -> System.out.println("Not a keybind");
                }
            }
        });
        this.status = status;
        this.scoreLabel = scoreLabel;
        this.highScoreLabel = highScoreLabel;
        this.timeLabel = timeLabel;
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        playing = true;
        status.setText("Running...");
        currScore = 0;
        grasses = new HashSet<>();
        sinkholes = new HashSet<>();
        int arrayWidth = FIELD_WIDTH / BLOCK_WIDTH;
        int arrayHeight = FIELD_HEIGHT / BLOCK_HEIGHT;
        for (int i = 0; i < arrayHeight; i++) {
            for (int j = 0; j < arrayWidth; j++) {
                Grass grassBlock = new Grass(j, i);
                gameField[i][j] = grassBlock;
                grasses.add(grassBlock);
            }
        }
        snake = new Snake();
        for (GameObject snakeBlock : snake.getMovingObject()) {
            int y = snakeBlock.getY();
            int x = snakeBlock.getX();
            grasses.remove((Grass) gameField[y][x]);
            gameField[y][x] = snakeBlock;
        }
        if (enemyMode) {
            enemy = new Enemy();
            for (GameObject enemyBlock : enemy.getMovingObject()) {
                int y = enemyBlock.getY();
                int x = enemyBlock.getX();
                grasses.remove((Grass) gameField[y][x]);
                gameField[y][x] = enemyBlock;
            }
            enemyIsAlive = true;
        }
        generateFruit();
        setSpeed(initInterval);
        startTimer.stop();
        currTime = 0;
        startTimer = new Timer(1000, e -> currTime++);
        startTimer.start();
        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }

    /**
     * This method is called every time the timer defined in the constructor
     * triggers.
     */
    void tick() {
        if (snake.getSize() == FIELD_WIDTH / BLOCK_WIDTH * FIELD_HEIGHT / BLOCK_HEIGHT) {
            scoreLabel.setText("Score: " + currScore);
            highScoreLabel.setText("High Score: " + highScore);
            status.setText("You win!");
        } else if (playing) {
            GameObject nextBlock = snake.getNextBlock(this);
            if (nextBlock != null) {
                nextBlock.interactWith(snake, this);
            }
            snake.move(this);
            if (enemyMode && enemyIsAlive) {
                enemy.chooseGoodDirection(this);
                GameObject enemyNextBlock = enemy.getNextBlock(this);
                // double check what happens when null
                if (enemyNextBlock != null) {
                    enemyNextBlock.interactWith(enemy, this);
                }
                enemy.move(this);
            }

            scoreLabel.setText("Score: " + currScore);
            highScoreLabel.setText("High Score: " + highScore);
            timeLabel.setText("Time: " + currTime);

            // update the display
            repaint();
        } else {
            status.setText("You lose!");
        }
    }

    public GameObject getObjectInField(int x, int y) {
        if (x >= 0 && x < FIELD_WIDTH / BLOCK_WIDTH && y >= 0 && y < FIELD_HEIGHT / BLOCK_HEIGHT) {
            return gameField[y][x];
        }
        return null;
    }

    public void setObjectInField(GameObject obj) {
        if (obj == null) {
            throw new IllegalArgumentException();
        }
        int x = obj.getX();
        int y = obj.getY();
        if (x >= 0 && x < FIELD_WIDTH && y >= 0 && y < FIELD_HEIGHT) {
            gameField[y][x] = obj;
        }
    }

    public void removeGrass(GameObject obj) {
        if (obj == null) {
            throw new IllegalArgumentException();
        }
        grasses.remove(obj);
    }

    public void addGrass(Grass obj) {
        if (obj == null) {
            throw new IllegalArgumentException();
        }
        grasses.add(obj);
    }

    public void setPlaying(boolean b) {
        this.playing = b;
    }

    public boolean getSnakeJustAteFruit() {
        return snakeJustAteFruit;
    }

    public void setSnakeJustAteFruit(boolean snakeJustAteFruit) {
        this.snakeJustAteFruit = snakeJustAteFruit;
    }

    public boolean getEnemyJustAteFruit() {
        return enemyJustAteFruit;
    }

    public void setEnemyJustAteFruit(boolean enemyJustAteFruit) {
        this.enemyJustAteFruit = enemyJustAteFruit;
    }

    public void increaseScore() {
        currScore++;
        if (currScore > highScore) {
            highScore = currScore;
        }
    }

    public void setSpeed(int speed) {
        intervalTimer.stop();
        intervalTimer = new Timer(speed, e -> tick());
        intervalTimer.start();
    }

    public boolean getSpeedMode() {
        return speedMode;
    }

    public void toggleSpeedMode() {
        speedMode = !speedMode;
        requestFocusInWindow();
    }

    public boolean getSinkholeMode() {
        return sinkholeMode;
    }

    public void toggleSinkholeMode() {
        sinkholeMode = !sinkholeMode;
        requestFocusInWindow();
    }

    public void toggleEnemyMode() {
        enemyMode = !enemyMode;
        reset();
        requestFocusInWindow();
    }

    private Grass chooseRandomGrass() {
        if (!grasses.isEmpty()) {
            Random rand = new Random();
            int i = 0;
            int index = rand.nextInt(grasses.size());
            for (Grass grass : grasses) {
                if (i == index) {
                    return grass;
                }
                i++;
            }
        }
        return null;
    }

    public void generateFruit() {
        Grass grass = chooseRandomGrass();
        if (grass == null) {
            return;
        }
        setObjectInField(new Fruit(grass.getX(), grass.getY()));
        removeGrass(grass);
    }

    public void generateRandomFruit() {
        Grass grass = chooseRandomGrass();
        if (grass == null) {
            return;
        }
        Random rand = new Random();
        int random = rand.nextInt(3);
        switch (random) {
            case 0 -> setObjectInField(new Fruit(grass.getX(), grass.getY()));
            case 1 -> setObjectInField(new SlowFruit(grass.getX(), grass.getY()));
            case 2 -> setObjectInField(new FastFruit(grass.getX(), grass.getY()));
            default -> System.out.println("Random generator issue");
        }
        removeGrass(grass);
    }

    private void removeSinkholes() {
        for (Sinkhole sinkhole : sinkholes) {
            int sinkholeX = sinkhole.getX();
            int sinkholeY = sinkhole.getY();
            Grass grass = new Grass(sinkholeX, sinkholeY);
            gameField[sinkholeY][sinkholeX] = grass;
            grasses.add(grass);
        }
        sinkholes = new HashSet<>();
    }

    public void generateSinkholes() {
        removeSinkholes();
        for (int i = 1; i < grasses.size() / 20; i++) {
            Grass grass = chooseRandomGrass();
            if (grass == null) {
                break;
            }
            int x = grass.getX();
            int y = grass.getY();
            // next block is location of where head will be since generateSinkhole
            // will be called before snake.move()
            int snakeX = snake.getNextBlock(this).getX();
            int snakeY = snake.getNextBlock(this).getY();
            if (!(x <= snakeX + 2 && x >= snakeX - 2 && y <= snakeY + 2 && y >= snakeY - 2)) {
                Sinkhole sinkhole = new Sinkhole(x, y);
                setObjectInField(sinkhole);
                sinkholes.add(sinkhole);
                removeGrass(grass);
            }
        }
    }

    public void killEnemy() {
        for (GameObject enemyBlock : enemy.getMovingObject()) {
            int y = enemyBlock.getY();
            int x = enemyBlock.getX();
            Grass grass = new Grass(x, y);
            grasses.add(grass);
            gameField[y][x] = grass;
        }
        enemy.removeEnemy();
        enemyIsAlive = false;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int arrayWidth = FIELD_WIDTH / BLOCK_WIDTH;
        int arrayHeight = FIELD_HEIGHT / BLOCK_HEIGHT;
        for (int i = 0; i < arrayHeight; i++) {
            for (int j = 0; j < arrayWidth; j++) {
                GameObject obj = gameField[i][j];
                obj.draw(g);
            }
        }
        snake.drawHead(g);
        if (enemyMode && enemyIsAlive) {
            enemy.drawHead(g);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(FIELD_WIDTH, FIELD_HEIGHT);
    }
}