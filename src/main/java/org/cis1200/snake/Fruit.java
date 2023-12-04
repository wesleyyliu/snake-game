package org.cis1200.snake;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Fruit extends GameObject {
    public static final String IMG_FILE = "files/apple.png";
    private static BufferedImage img;

    public Fruit(int x, int y) {
        super(x, y);
        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Error getting Apple image" + e.getMessage());
        }
    }

    @Override
    public void draw(Graphics g) {
        this.drawGrassBackground(g);
        g.drawImage(
                img, this.getX() * Field.BLOCK_WIDTH, this.getY() * Field.BLOCK_HEIGHT,
                Field.BLOCK_WIDTH, Field.BLOCK_HEIGHT, null
        );
    }

    @Override
    public void interactWith(MovingObject obj, Field f) {
        if (obj instanceof Snake) {
            f.increaseScore();
            f.setSnakeJustAteFruit(true);
        } else {
            f.setEnemyJustAteFruit(true);
        }
        if (f.getSinkholeMode()) {
            f.generateSinkholes();
        }
        if (f.getSpeedMode()) {
            f.generateRandomFruit();
            f.setSpeed(150);
        } else {
            f.generateFruit();
        }
    }
}
