package org.cis1200.snake;

import java.awt.*;
import java.util.HashSet;
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
        if (obj instanceof SlowFruit) {
            return true;
        }
        return false;
    }

    private boolean goodMove(GameObject obj) {
        return obj instanceof Grass;
    }

    public void chooseGoodDirection(Field f) {
        HashSet<Direction> possibleDirections = new HashSet<>();
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
        if (goodMove(rightBlock)) {
            possibleDirections.add(Direction.RIGHT);
        }
        if (goodMove(leftBlock)) {
            possibleDirections.add(Direction.LEFT);
        }
        if (goodMove(topBlock)) {
            possibleDirections.add(Direction.UP);
        }
        if (goodMove(bottomBlock)) {
            possibleDirections.add(Direction.DOWN);
        }
        if (!possibleDirections.isEmpty()) {
            Random rand = new Random();
            int i = 0;
            int index = rand.nextInt(possibleDirections.size());
            for (Direction direction : possibleDirections) {
                if (i == index) {
                    setDirection(direction);
                }
                i++;
            }
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
