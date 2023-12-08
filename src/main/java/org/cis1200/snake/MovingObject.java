package org.cis1200.snake;

import java.util.LinkedList;
import java.awt.*;

public abstract class MovingObject {
    Direction direction;
    LinkedList<GameObject> obj;

    public MovingObject(Direction direction) {
        this.direction = direction;
        this.obj = new LinkedList<>();
    }

    public int getSize() {
        return obj.size();
    }

    public GameObject getHead() {
        if (obj.isEmpty() || obj.peekFirst() == null) {
            return null;
        }
        return new SnakeBlock(obj.peekFirst().getX(), obj.peekFirst().getY());
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public GameObject getNextBlock(Field f) {
        if (getHead() == null) {
            return null;
        }
        int currHeadX = getHead().getX();
        int currHeadY = getHead().getY();
        switch (this.direction) {
            case UP -> {
                return f.getObjectInField(currHeadX, currHeadY - 1);
            }
            case DOWN -> {
                return f.getObjectInField(currHeadX, currHeadY + 1);
            }
            case LEFT -> {
                return f.getObjectInField(currHeadX - 1, currHeadY);
            }
            case RIGHT -> {
                return f.getObjectInField(currHeadX + 1, currHeadY);
            }
            default -> {
            }
        }
        return null;
    }

    public GameObject getSecondBlock() {
        if (obj.size() < 2) {
            return null;
        }
        return obj.get(1);
    }

    public LinkedList<GameObject> getMovingObject() {
        return new LinkedList<>(obj);
    }

    public abstract void drawHead(Graphics g);

    public abstract void move(Field f);
}
