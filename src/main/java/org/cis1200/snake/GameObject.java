package org.cis1200.snake;

import java.awt.*;

/**
 * An object in the game.
 * Game objects exist in the game court. They have a position, velocity, size
 * and bounds. Their velocity controls how they move; their position should
 * always be within their bounds.
 */
public abstract class GameObject {
    private final int x;
    private final int y;
    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    void drawGrassBackground(Graphics g) {
        if (this.getX() % 2 == 0 && this.getY() % 2 == 0
                || this.getX() % 2 == 1 && this.getY() % 2 == 1) {
            g.setColor(new Color(170, 215, 80));
        } else {
            g.setColor(new Color(160, 209, 72));
        }
        g.fillRect(
                this.getX() * Field.BLOCK_WIDTH, this.getY() * Field.BLOCK_HEIGHT,
                Field.BLOCK_WIDTH, Field.BLOCK_HEIGHT
        );
    }

    public abstract void draw(Graphics g);

    public abstract void interactWith(MovingObject obj, Field f);
}