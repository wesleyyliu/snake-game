package org.cis1200.snake;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

public class Enemy extends MovingObject {
    public Enemy() {
        super(Direction.LEFT);
        int numBlocks = Field.FIELD_HEIGHT / Field.BLOCK_HEIGHT;
        obj.add(new EnemyBlock(numBlocks - 1, numBlocks / 2 - 2));
        obj.add(new EnemyBlock(numBlocks - 1, numBlocks / 2 - 1));
        obj.add(new EnemyBlock(numBlocks - 1, numBlocks / 2));
        obj.add(new EnemyBlock(numBlocks - 1, numBlocks / 2 + 1));
    }

    @Override
    public void drawHead(Graphics g) {
        g.setColor(new Color(255, 91, 91));
        g.fillRect(
                obj.getFirst().getX() * Field.BLOCK_WIDTH,
                obj.getFirst().getY() * Field.BLOCK_HEIGHT,
                Field.BLOCK_WIDTH, Field.BLOCK_HEIGHT
        );
    }

    private boolean idealMove(GameObject obj) {
        if (obj instanceof Fruit) {
            return true;
        }
        if (obj instanceof FastFruit) {
            return true;
        }
        return obj instanceof SlowFruit;
    }

    private boolean goodMove(GameObject obj) {
        return obj instanceof Grass;
    }

    private int distance(GameObject obj1, GameObject obj2) {
        int xDistance = obj1.getX() - obj2.getX();
        int yDistance = obj1.getY() - obj2.getY();
        return (int) Math.sqrt(xDistance * xDistance + yDistance * yDistance);
    }

    public void chooseGoodDirection(Field f) {
        //HashSet<Direction> possibleDirections = new HashSet<>();
        GameObject head = getHead();
        int headX = head.getX();
        int headY = head.getY();
        GameObject rightBlock = f.getObjectInField(headX + 1, headY);
        GameObject leftBlock = f.getObjectInField(headX - 1, headY);
        GameObject topBlock = f.getObjectInField(headX, headY - 1);
        GameObject bottomBlock = f.getObjectInField(headX, headY + 1);
        if (idealMove(rightBlock)) {
            setDirection(Direction.RIGHT);
            return;
        }
        if (idealMove(leftBlock)) {
            setDirection(Direction.LEFT);
            return;
        }
        if (idealMove(bottomBlock)) {
            setDirection(Direction.DOWN);
            return;
        }
        if (idealMove(topBlock)) {
            setDirection(Direction.UP);
            return;
        }
        // if no ideal moves, choose move that guides the snake to the fruit
        int lowestDistanceToFruit = Integer.MAX_VALUE;
        Direction[] possibleDirections = new Direction[10];
        Direction bestMove = null;
        int currIndex = 0;
        if (goodMove(rightBlock)) {
            int newDistance = distance(rightBlock, f.getCurrFruit());
            if (newDistance < lowestDistanceToFruit) {
                lowestDistanceToFruit = newDistance;
                bestMove = Direction.RIGHT;
            }
            possibleDirections[currIndex++] = Direction.RIGHT;
        }
        if (goodMove(leftBlock)) {
            int newDistance = distance(leftBlock, f.getCurrFruit());
            if (newDistance < lowestDistanceToFruit) {
                lowestDistanceToFruit = newDistance;
                bestMove = Direction.LEFT;
            }
            possibleDirections[currIndex++] = Direction.LEFT;
        }
        if (goodMove(topBlock)) {
            int newDistance = distance(topBlock, f.getCurrFruit());
            if (newDistance < lowestDistanceToFruit) {
                lowestDistanceToFruit = newDistance;
                bestMove = Direction.UP;
            }
            possibleDirections[currIndex++] = Direction.UP;
        }
        if (goodMove(bottomBlock)) {
            int newDistance = distance(bottomBlock, f.getCurrFruit());
            if (newDistance < lowestDistanceToFruit) {
                bestMove = Direction.DOWN;
            }
            possibleDirections[currIndex++] = Direction.DOWN;
        }
        // increase the probability of the bestMove occurring
        for (int i = 0; i < 5; i++) {
            possibleDirections[currIndex++] = bestMove;
        }
        if (possibleDirections[0] != null) {
            Random rand = new Random();
            int randIndex = rand.nextInt(currIndex);
            setDirection(possibleDirections[randIndex]);
        }
    }

    public void removeEnemy() {
        obj = new LinkedList<>();
    }

    @Override
    public void move(Field f) {
        GameObject nextBlock = getNextBlock(f);
        if (nextBlock == null) {
            f.killEnemy();
            return;
        }
        int nextBlockX = nextBlock.getX();
        int nextBlockY = nextBlock.getY();
        // remove grass, doesn't do anything if nextblock is not grass
        f.removeGrass(f.getObjectInField(nextBlockX, nextBlockY));
        // create new head
        EnemyBlock newHead = new EnemyBlock(nextBlockX, nextBlockY);
        obj.addFirst(newHead);
        f.setObjectInField(newHead);
        if (f.getEnemyJustAteFruit()) {
            f.setEnemyJustAteFruit(false);
        } else {
            GameObject tail = obj.getLast();
            Grass newGrass = new Grass(tail.getX(), tail.getY());
            f.setObjectInField(newGrass);
            f.addGrass(newGrass);
            obj.removeLast();
        }
    }
}
