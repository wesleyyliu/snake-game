package org.cis1200.snake;

import java.awt.*;

public class SnakeBlock extends GameObject {
    public SnakeBlock(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(73, 118, 236));
        g.fillRect(
                this.getX() * Field.BLOCK_WIDTH, this.getY() * Field.BLOCK_HEIGHT,
                Field.BLOCK_WIDTH, Field.BLOCK_HEIGHT
        );
    }

    @Override
    public void interactWith(MovingObject obj, Field f) {
        if (obj instanceof Snake) {
            f.setPlaying(false);
        } else {
            f.killEnemy();
        }
    }
}
