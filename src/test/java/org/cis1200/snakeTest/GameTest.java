package org.cis1200.snakeTest;

import org.cis1200.snake.*;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    @Test
    public void testFruitsUpdateScore() {
        Field field = new Field(null, null, null, null);
        Snake snake = new Snake();
        Fruit fruit = new Fruit(1, 1);
        fruit.interactWith(snake, field);
        assertEquals(1, field.getCurrScore());
        assertEquals(1, field.getHighScore());
        FastFruit fastFruit = new FastFruit(1, 1);
        fastFruit.interactWith(snake, field);
        assertEquals(2, field.getCurrScore());
        assertEquals(2, field.getHighScore());
        SlowFruit slowFruit = new SlowFruit(1, 1);
        slowFruit.interactWith(snake, field);
        assertEquals(3, field.getCurrScore());
        assertEquals(3, field.getHighScore());
    }

    @Test
    public void testHighScoreAfterReset() {
        JLabel label = new JLabel();
        Field field = new Field(label, label, label, label);
        Snake snake = new Snake();
        Fruit fruit = new Fruit(1, 1);
        fruit.interactWith(snake, field);
        assertEquals(1, field.getCurrScore());
        assertEquals(1, field.getHighScore());
        fruit.interactWith(snake, field);
        assertEquals(2, field.getCurrScore());
        assertEquals(2, field.getHighScore());
        field.reset();
        SlowFruit slowFruit = new SlowFruit(1, 1);
        slowFruit.interactWith(snake, field);
        assertEquals(1, field.getCurrScore());
        assertEquals(2, field.getHighScore());
    }

    @Test
    public void testGrassesHashSetAfterFruit() {
        JLabel label = new JLabel();
        Field field = new Field(label, label, label, label);
        Snake snake = new Snake();
        // these numbers are based off a 15x15 grid
        assertEquals(220, field.getGrasses().size());
        Fruit fruit = new Fruit(1, 1);
        fruit.interactWith(snake, field);
        assertEquals(219, field.getGrasses().size());
    }

    @Test
    public void testGenerateSinkholes() {
        JLabel label = new JLabel();
        Field field = new Field(label, label, label, label);
        int numGrass = field.getNumBlocks() - 4 - 1;
        assertEquals(numGrass, field.getGrasses().size());
        field.generateSinkholes();
        assertTrue(field.getGrasses().size() >= numGrass - numGrass / 20);
    }

    @Test
    public void testSinkholeEndsGame() {
        JLabel label = new JLabel();
        Field field = new Field(label, label, label, label);
        Snake snake = new Snake();
        assertTrue(field.getPlaying());
        Sinkhole sinkhole = new Sinkhole(1, 1);
        sinkhole.interactWith(snake, field);
        assertFalse(field.getPlaying());
    }

    @Test
    public void testGenerateSinkholesDoesNotReplaceFruit() {
        JLabel label = new JLabel();
        Field field = new Field(label, label, label, label);
        field.generateFruit();
        int currFruitX = field.getCurrFruit().getX();
        int currFruitY = field.getCurrFruit().getY();
        field.generateSinkholes();
        assertTrue(field.getObjectInField(currFruitX, currFruitY) instanceof Fruit);
    }

    @Test
    public void testRunIntoSelfEndsGame() {
        JLabel label = new JLabel();
        Field field = new Field(label, label, label, label);
        Snake snake = new Snake();
        GameObject snakeBody = snake.getSecondBlock();
        assertTrue(field.getPlaying());
        snakeBody.interactWith(snake, field);
        assertFalse(field.getPlaying());
    }

    @Test
    public void testRunIntoEnemyEndsGame() {
        JLabel label = new JLabel();
        Field field = new Field(label, label, label, label);
        field.toggleEnemyMode();
        field.reset();
        Snake snake = new Snake();
        EnemyBlock enemy = new EnemyBlock(1, 1);
        assertTrue(field.getPlaying());
        enemy.interactWith(snake, field);
        assertFalse(field.getPlaying());
    }

    @Test
    public void testEnemyDies() {
        JLabel label = new JLabel();
        Field field = new Field(label, label, label, label);
        Enemy enemy = new Enemy();
        Sinkhole sinkhole = new Sinkhole(1, 1);
        field.toggleEnemyMode();
        field.reset();
        sinkhole.interactWith(enemy, field);
        // numblocks - 4 (size of snake) - 1 (fruit)
        assertEquals(field.getNumBlocks() - 4 - 1, field.getGrasses().size());
        assertTrue(field.getPlaying());
    }

    @Test
    public void testRunIntoEnemyHeadLose() {
        JLabel label = new JLabel();
        Field field = new Field(label, label, label, label);
        Snake snake = new Snake();
        field.toggleEnemyMode();
        field.reset();
        Enemy enemy = new Enemy();
        GameObject head = enemy.getHead();
        assertTrue(field.getPlaying());
        head.interactWith(snake, field);
        assertFalse(field.getPlaying());
    }

    @Test
    public void testRunIntoRightBoundaryLoses() {
        JLabel label = new JLabel();
        Field field = new Field(label, label, label, label);
        Snake snake = new Snake();
        assertTrue(field.getPlaying());
        for (int i = 0; i <= Math.sqrt(field.getNumBlocks()) - 4; i++) {
            snake.move(field);
        }
        assertFalse(field.getPlaying());
    }

    @Test
    public void testRunIntoTopBoundaryLoses() {
        JLabel label = new JLabel();
        Field field = new Field(label, label, label, label);
        Snake snake = new Snake();
        snake.setDirection(Direction.UP);
        assertTrue(field.getPlaying());
        for (int i = 0; i <= Math.sqrt(field.getNumBlocks()) - 8; i++) {
            snake.move(field);
        }
        assertFalse(field.getPlaying());
    }

    @Test
    public void testRunIntoLeftBoundaryLoses() {
        JLabel label = new JLabel();
        Field field = new Field(label, label, label, label);
        Snake snake = new Snake();
        snake.setDirection(Direction.DOWN);
        snake.move(field);
        snake.setDirection(Direction.LEFT);
        assertTrue(field.getPlaying());
        for (int i = 0; i <= Math.sqrt(field.getNumBlocks()) - 12; i++) {
            snake.move(field);
        }
        assertFalse(field.getPlaying());
    }

    @Test
    public void testGetObjectInFieldOutOfBounds() {
        JLabel label = new JLabel();
        Field field = new Field(label, label, label, label);
        assertDoesNotThrow(() -> field.getObjectInField(-1, -1));
        assertDoesNotThrow(() -> field.getObjectInField(100, 100));
    }

}
