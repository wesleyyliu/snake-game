package org.cis1200.snake;

import java.awt.*;

/**
 * An object in the game.
 *
 * Game objects exist in the game court. They have a position, velocity, size
 * and bounds. Their velocity controls how they move; their position should
 * always be within their bounds.
 */
public abstract class GameObject {
    /*
     * Current position of the object (in terms of graphics coordinates)
     *
     * Coordinates are given by the upper-left hand corner of the object. This
     * position should always be within bounds:
     * 0 <= px <= maxX 0 <= py <= maxY
     */
    private final int x;
    private final int y;

    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructor
     */

    // **********************************************************************************
    // * GETTERS
    // **********************************************************************************
    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    // **************************************************************************
    // * UPDATES AND OTHER METHODS
    // **************************************************************************

    /**
     * Default draw method that provides how the object should be drawn in the
     * GUI. This method does not draw anything. Subclass should override this
     * method based on how their object should appear.
     *
     * @param g The <code>Graphics</code> context used for drawing the object.
     *          Remember graphics contexts that we used in OCaml, it gives the
     *          context in which the object should be drawn (a canvas, a frame,
     *          etc.)
     */
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