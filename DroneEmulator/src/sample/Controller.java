package sample;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;


public class Controller {

    // Our fxml elements we need to grab.
    @FXML
    TableView<UdpMessage> table;
    @FXML
    ToggleButton toggleBtnEcho;
    @FXML
    ToggleButton toggleBtnBroadcast;
    @FXML
    private Canvas canvas;
    public Drone drone = new Drone();

    private UdpConnector udpConnector;
    private UdpBroadcastServer broadcastServer;
    private GraphicsContext graphicsContext;

    public void updateCanvas(){
        graphicsContext.clearRect(0,0,400,400);
        drone.draw(graphicsContext);
    }

    public void toggleBtnEchoServer()
    {
        System.out.println("togglebtnECHO clicked");
        if (udpConnector.isReceiveMessages())
        {
            udpConnector.setReceiveMessages(false);
            toggleBtnEcho.setText("OFF");
        }
        else
        {
            startUdpConnection();
            toggleBtnEcho.setText("ON");
        }
    }

    public void toggleBtnBroadcastServer()
    {
        System.out.println("togglebtnBROADCAST clicked");
        if (broadcastServer.isBroadcast())
        {
            broadcastServer.setBroadcast(false);
            toggleBtnBroadcast.setText("OFF");
        }
        else
        {
            startBroadcasting();
            toggleBtnBroadcast.setText(("ON"));
        }
    }

    public void clearLog()
    {
        table.getItems().clear();
    }

    public void initialize()
    {
        System.out.println("initialize");

        startUdpConnection();
        startBroadcasting();
        graphicsContext = canvas.getGraphicsContext2D();
    }

    private void startBroadcasting() {
        broadcastServer = new UdpBroadcastServer();
        new Thread(broadcastServer).start();
    }

    private void startUdpConnection() {
        if (udpConnector != null) udpConnector.closeSocket();
        udpConnector = new UdpConnector(this);
        new Thread(udpConnector).start();
    }

    public void receiveMessage(UdpMessage udpMessage)
    {
        if(drone.getX()!= 0 && drone.getY() != 0) {
            if (udpMessage.getMessage().equals("moveleft")) {
                drone.setX(drone.getX() - drone.getSpeed());
            }

            if (udpMessage.getMessage().equals("moveright")) {
                drone.setX(drone.getX() + drone.getSpeed());
            }
            if (udpMessage.getMessage().equals("moveup")) {
                drone.setY(drone.getY() - drone.getSpeed());
            }
            if (udpMessage.getMessage().equals("movedown")) {
                drone.setY(drone.getY() + drone.getSpeed());
            }
            if(udpMessage.getMessage().equals("changecolor")){
                if(drone.getColor().equals(Color.BLUE)){
                    drone.setColor(Color.GREEN);
                } else if(drone.getColor().equals(Color.RED)){
                    drone.setColor(Color.BLUE);
                } else if(drone.getColor().equals(Color.GREEN)){
                    drone.setColor(Color.RED);
                }
            }
            table.getItems().add(0, udpMessage);
            updateCanvas();
        }
        if(udpMessage.getMessage().equals("initialize drone")){
            drone.setX((int)canvas.getWidth()/2);
            drone.setY((int)canvas.getHeight()/2);
            table.getItems().add(0, udpMessage);
            updateCanvas();
        }
        if(drone.getX() == 0 && drone.getY() == 0) {
            System.out.println("Command not accepted!");
        }
    }
}