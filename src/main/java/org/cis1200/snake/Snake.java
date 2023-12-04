package org.cis1200.snake;

import java.awt.*;
import java.util.LinkedList;

public class Snake extends MovingObject {
    public Snake() {
        super(Direction.RIGHT);
        int numBlocks = Field.FIELD_HEIGHT / Field.BLOCK_HEIGHT;
        obj.add(new SnakeBlock(3, numBlocks / 2));
        obj.add(new SnakeBlock(2, numBlocks / 2));
        obj.add(new SnakeBlock(1, numBlocks / 2));
        obj.add(new SnakeBlock(0, numBlocks / 2));
    }

    public void drawHead(Graphics g) {
        g.setColor(new Color(121, 151, 229));
        g.fillRect(
                obj.getFirst().getX() * Field.BLOCK_WIDTH,
                obj.getFirst().getY() * Field.BLOCK_HEIGHT,
                Field.BLOCK_WIDTH, Field.BLOCK_HEIGHT
        );
    }

    @Override
    public void move(Field f) {
        GameObject nextBlock = getNextBlock(f);
        if (nextBlock == null) {
            f.setPlaying(false);
            return;
        }
        int nextBlockX = nextBlock.getX();
        int nextBlockY = nextBlock.getY();
        // remove grass, doesn't do anything if nextblock is not grass
        f.removeGrass(f.getObjectInField(nextBlockX, nextBlockY));
        // create new head
        SnakeBlock newHead = new SnakeBlock(nextBlockX, nextBlockY);
        obj.addFirst(newHead);
        f.setObjectInField(newHead);
        if (f.getSnakeJustAteFruit()) {
            f.setSnakeJustAteFruit(false);
        } else {
            GameObject tail = obj.getLast();
            Grass newGrass = new Grass(tail.getX(), tail.getY());
            f.setObjectInField(newGrass);
            f.addGrass(newGrass);
            obj.removeLast();
        }
    }
}
