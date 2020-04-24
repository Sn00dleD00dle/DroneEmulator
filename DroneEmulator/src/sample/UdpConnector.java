package sample;

import java.io.IOException;
import java.net.*;

public class UdpConnector implements Runnable{
    private DatagramSocket socket;
    private int udpPort = 7000;
    private int udpPortEcho = 7007;
    private Controller messageHandler;
    private boolean receiveMessages = true;

    public UdpConnector(Controller messageHandler)
    {
        this.messageHandler = messageHandler;
        setupSocket();
    }

    public void closeSocket()
    {
        this.socket.close();
    }

    public void setupSocket() {
        try {

            socket = new DatagramSocket(udpPort);
        } catch (SocketException e) {
            System.out.println("IOEXCEPTION: Tried to create new datagramsocket on "+udpPort);
            System.out.println(e.getMessage());
        }
    }

    public void sendMessage(String string, InetAddress address)
    {
        sendMessage(string.getBytes(), address);
    }

    public void sendMessage(byte[] bytes, InetAddress address)
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

    public UdpMessage receiveMessage() {
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

    public void echoServer()
    {
        UdpMessage message = receiveMessage();
        try {
            if (receiveMessages) sendMessage("echoserver receiver: "+message.getMessage(), InetAddress.getByName(message.getIp()));
        } catch (UnknownHostException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void run() {
        connectionLoop();

    }

    public void connectionLoop() {
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
    }

    public void setReceiveMessages(boolean receiveMessages) {
        this.receiveMessages = receiveMessages;
    }
}
