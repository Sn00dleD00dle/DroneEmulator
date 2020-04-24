package sample;

import javafx.scene.canvas.GraphicsContext;

public class Drone extends DrawableObject {
    private int x = 0;
    private int y = 0;
    private int sizeX = 20;
    private int sizeY = 20;
    private int speed = 5;
    private String shape;

    @Override
    public void draw(GraphicsContext graphicsContext) {
        graphicsContext.setFill(color);
        graphicsContext.setStroke(color);
        graphicsContext.fillRect(x,y,sizeX,sizeY);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return sizeX;
    }

    public void setWidth(int width) {
        this.sizeX = width;
    }

    public int getHeight() {
        return sizeY;
    }

    public void setHeight(int height) {
        this.sizeY = height;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }
}
