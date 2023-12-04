package org.cis1200.snake;

import java.awt.*;

public class Sinkhole extends GameObject {
    public Sinkhole(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics g) {
        this.drawGrassBackground(g);
        g.setColor(new Color(52, 8, 8));
        g.fillOval(
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
