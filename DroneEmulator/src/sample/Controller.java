package sample;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Controller {
    @FXML
    private Label labelMouseStatus;
    @FXML
    private Canvas canvas;
    boolean mouseOnCanvas = false;
    DrawableObject selectedObject = new Drone();
    GraphicsContext graphicsContext;
    public void initialize()
    {
        graphicsContext = canvas.getGraphicsContext2D();
    }
    @FXML
    void canvasMouseClicked(MouseEvent event) {
        //System.out.println("mouse clicked");
        selectedObject.setX((int) event.getX());
        selectedObject.setY((int) event.getY());
        selectedObject.setHeight(6);
        selectedObject.setWidth(6);
        selectedObject.setColor(Color.RED);
        selectedObject.draw(graphicsContext);
    }

    @FXML
    void canvasMouseEntered(MouseEvent event) {
        //System.out.println("mouse entered");
        mouseOnCanvas = true;
    }
    @FXML
    void canvasMouseExited(MouseEvent event) {
        //System.out.println("mouse exited");
        mouseOnCanvas = false;
    }
    @FXML
    void canvasMouseMoved(MouseEvent event) {
        //System.out.println("mouse moved... (f#¤k)");
        if (mouseOnCanvas) {
            labelMouseStatus.setText("X: "+ event.getX() + " Y: " + event.getY());
        }
    }
}