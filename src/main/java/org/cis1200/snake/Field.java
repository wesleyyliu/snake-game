package org.cis1200.snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Random;

/**
 * Field
 * This class keeps control of the internal state of the game and controls the
 * logic
 * for the different objects. It calls the tick() method to move objects in each
 * time
 * interval.
 */
public class Field extends JPanel {

    // 2D array storing all GameObjects
    private final GameObject[][] gameField;
    private Snake snake;
    private Enemy enemy;
    private boolean enemyIsAlive;
    private GameObject currFruit;
    private HashSet<Grass> grasses = new HashSet<>();
    private HashSet<Sinkhole> sinkholes = new HashSet<>();
    private boolean snakeJustAteFruit = false;
    private boolean enemyJustAteFruit = false;
    private boolean speedMode = false;
    private boolean sinkholeMode = false;
    private boolean enemyMode = false;
    private boolean playing = true; // whether the game is running
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

    // timer keeping track of each game attempt
    private Timer startTimer;
    private int currTime;
    // timer that changes to control tick speed for speed mode
    private Timer intervalTimer;
    private final int initInterval = 150;

    // assumes all labels are not null
    public Field(JLabel status, JLabel scoreLabel, JLabel highScoreLabel, JLabel timeLabel) {
        int arrayWidth = FIELD_WIDTH / BLOCK_WIDTH;
        int arrayHeight = FIELD_HEIGHT / BLOCK_HEIGHT;
        // instantiates the appropriate size 2D array
        gameField = new GameObject[arrayHeight][arrayWidth];
        // fills the 2D array with grass
        for (int i = 0; i < arrayHeight; i++) {
            for (int j = 0; j < arrayWidth; j++) {
                Grass grassBlock = new Grass(j, i);
                gameField[i][j] = grassBlock;
                grasses.add(grassBlock);
            }
        }
        // create a snake
        snake = new Snake();
        // replace the corresponding grass blocks with snake blocks
        for (GameObject snakeBlock : snake.getMovingObject()) {
            int y = snakeBlock.getY();
            int x = snakeBlock.getX();
            grasses.remove((Grass) gameField[y][x]);
            gameField[y][x] = snakeBlock;
        }
        // generates a fruit in the field
        generateFruit();

        // timer for speed mode
        intervalTimer = new Timer(initInterval, e -> tick());
        intervalTimer.start();

        // timer for game
        startTimer = new Timer(1000, e -> currTime++);
        startTimer.start();

        // creates border around the field
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        // keyboard focus for the field
        setFocusable(true);

        // key listener for the game
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
     * Set game back to initial state
     */
    public void reset() {
        playing = true;
        status.setText("Running...");
        currScore = 0;
        grasses = new HashSet<>();
        sinkholes = new HashSet<>();
        // re-populate grass
        int arrayWidth = FIELD_WIDTH / BLOCK_WIDTH;
        int arrayHeight = FIELD_HEIGHT / BLOCK_HEIGHT;
        for (int i = 0; i < arrayHeight; i++) {
            for (int j = 0; j < arrayWidth; j++) {
                Grass grassBlock = new Grass(j, i);
                gameField[i][j] = grassBlock;
                grasses.add(grassBlock);
            }
        }
        // populate snake
        snake = new Snake();
        for (GameObject snakeBlock : snake.getMovingObject()) {
            int y = snakeBlock.getY();
            int x = snakeBlock.getX();
            grasses.remove((Grass) gameField[y][x]);
            gameField[y][x] = snakeBlock;
        }
        // if the enemy mode is set on, create an enemy
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
        // keyboard focus for component
        requestFocusInWindow();
    }

    /**
     * Method called by timer to move the game
     */
    void tick() {
        if (snake.getSize() == FIELD_WIDTH / BLOCK_WIDTH * FIELD_HEIGHT / BLOCK_HEIGHT) {
            scoreLabel.setText("Score: " + currScore);
            highScoreLabel.setText("High Score: " + highScore);
            status.setText("You win!");
            playing = false;
        } else if (playing) {
            GameObject nextBlock = snake.getNextBlock(this);
            if (nextBlock != null) {
                nextBlock.interactWith(snake, this);
            }
            snake.move(this);
            if (enemyMode && enemyIsAlive) {
                enemy.chooseGoodDirection(this);
                GameObject enemyNextBlock = enemy.getNextBlock(this);
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
        if (obj instanceof Grass) {
            grasses.remove(obj);
        }
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

    public GameObject getCurrFruit() {
        return currFruit;
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
        GameObject fruit = new Fruit(grass.getX(), grass.getY());
        setObjectInField(fruit);
        currFruit = fruit;
        removeGrass(grass);
    }

    public void generateRandomFruit() {
        Grass grass = chooseRandomGrass();
        if (grass == null) {
            return;
        }
        Random rand = new Random();
        int random = rand.nextInt(3);
        GameObject fruit = null;
        switch (random) {
            case 0 -> {
                fruit = new Fruit(grass.getX(), grass.getY());
                setObjectInField(fruit);
            }
            case 1 -> {
                fruit = new SlowFruit(grass.getX(), grass.getY());
                setObjectInField(fruit);
            }
            case 2 -> {
                fruit = new FastFruit(grass.getX(), grass.getY());
                setObjectInField(fruit);
            }
            default -> System.out.println("Random generator issue");
        }
        removeGrass(grass);
        currFruit = fruit;
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
            // make sure that person isn't about to lose (or else null exception occurs)
            if (snake.getNextBlock(this) == null) {
                return;
            }
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

    /**
     * Functions for testing
     */
    public int getCurrScore() {
        return currScore;
    }

    public int getHighScore() {
        return highScore;
    }

    public HashSet<Grass> getGrasses() {
        return new HashSet<>(grasses);
    }

    public int getNumBlocks() {
        return (FIELD_HEIGHT / BLOCK_HEIGHT) * (FIELD_WIDTH / BLOCK_WIDTH);
    }

    public boolean getPlaying() {
        return playing;
    }
}