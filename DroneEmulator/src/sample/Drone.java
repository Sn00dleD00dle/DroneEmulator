package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Drone extends DrawableObject { // Declares how we use DrawableObject to draw on canvas
    private int x = 0;
    private int y = 0;
    private int sizeX = 20;
    private int sizeY = 20;
    private int speed = 5;
    private Color color = Color.RED;

    @Override
    public void draw(GraphicsContext graphicsContext) { // Draw function
        graphicsContext.setFill(getColor());
        graphicsContext.setStroke(getColor());
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
