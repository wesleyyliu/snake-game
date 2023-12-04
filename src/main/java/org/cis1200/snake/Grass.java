package org.cis1200.snake;

import java.awt.*;

public class Grass extends GameObject {
    public Grass(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics g) {
        this.drawGrassBackground(g);
    }

    @Override
    public void interactWith(MovingObject obj, Field f) {
    }

}
