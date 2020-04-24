package sample;

import java.io.IOException;
import java.net.*;

public class UdpConnector implements Runnable{
    private DatagramSocket socket;
    private int udpPort = 7000;
    private int udpPortEcho = 7007;
    private Controller messageHandler;
    private boolean receiveMessages = true;

    public UdpConnector(Controller messageHandler) // Handles messages
    {
        this.messageHandler = messageHandler;
        setupSocket();
    }

    public void closeSocket()
    {
        this.socket.close();
    } //Closes connection

    public void setupSocket() { //Opens connection
        try {

            socket = new DatagramSocket(udpPort);
        } catch (SocketException e) {
            System.out.println("IOEXCEPTION: Tried to create new datagramsocket on "+udpPort);
            System.out.println(e.getMessage());
        }
    }

    public void sendMessage(String string, InetAddress address) // Send message method 1
    {
        sendMessage(string.getBytes(), address);
    }

    public void sendMessage(byte[] bytes, InetAddress address) // Send message method 2
    {
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, udpPortEcho);
        try {
            //socket = new DatagramSocket(udpPort);
            socket.send(packet);
        } catch (IOException e) {
            System.out.println("IOEXCEPTION: Tried to send packet");
        } finally {
            //socket.close();
        }

    }

    public UdpMessage receiveMessage() { //Receives message and prints in terminal
        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        try {
            //socket = new DatagramSocket(udpPort);
            System.out.println("waiting for a udp packet on port: "+ udpPort);
            socket.receive(packet);
            UdpMessage message = new UdpMessage(packet.getData(), packet.getLength(), packet.getAddress() , packet.getPort());
            System.out.println("received: "+ message);

            if (receiveMessages) messageHandler.receiveMessage(message);
            return message;
        } catch (IOException e) {
            System.out.println("IOEXCEPTION: Tried to receive packet");
            return null;
        } finally {
            //socket.close();
        }
    }

    public void echoServer() // Echoes received message back to sender
    {
        UdpMessage message = receiveMessage();
        try {
            if (receiveMessages) sendMessage("echoserver receiver: "+message.getMessage(), InetAddress.getByName(message.getIp()));
        } catch (UnknownHostException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void run() { // Run *dun dundundundun*
        connectionLoop();

    }

    public void connectionLoop() { //Loops the echoserver while messages are being received
        System.out.println("Started UdpConnector Thread");
        do
        {
            echoServer();
            //receiveMessage();
        }
        while(isReceiveMessages());
        // socket.close();
    }

    public boolean isReceiveMessages() {
        return receiveMessages;
    } //Are there are messages to receive?

    public void setReceiveMessages(boolean receiveMessages) { //Yes there are messages to receive
        this.receiveMessages = receiveMessages;
    }
}
