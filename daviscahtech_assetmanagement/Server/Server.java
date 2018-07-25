/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daviscahtech_assetmanagement.Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author davis
 */
public class Server implements Runnable{

    
    private ClientDBRequests clientDBRequests;
    
    private List<ServerClient> clients = new ArrayList<>();
    private List<Integer> clientResponse = new ArrayList<>();
    
    private DatagramSocket socket;
    private int port;
    private boolean running = false;
    private final int MAX_ATTEMPTS = 5;
    
    
    private Thread run,send,receive,manage;

    public Server(int port) {
        this.port = port;
        
        clientDBRequests = new ClientDBRequests();
        
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        run = new Thread(this, "server");
        run.start();
    }

    @Override
    public void run() {
        running = true;
        System.out.println("Server started on port " + port);
        manageClients();
        receiveMessages();
        Scanner scanner = new  Scanner(System.in);       
        while (running) {            
            String text = scanner.nextLine();
            if (!text.startsWith("/")) {
                sendToAll("/m/Server: "+text+"/e/");
                continue;
            }
            
        }
    }

    //method to manage multiple clients
      private void manageClients() {
        manage = new Thread("Manage clients"){
            
            @Override
            public void run(){
                while (running) { 
                    //mnaging
                    sendToAll("/i/Server");
                    
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    for (int i = 0; i < clients.size(); i++) {
                        ServerClient c = clients.get(i);
                        if (!clientResponse.contains(c.getID())) {
                            if (c.attempt >= MAX_ATTEMPTS) {
                                disconnect(c.getID(), false);
                            } else{
                                c.attempt++;
                            }
                        } else {
                            clientResponse.remove(new Integer(c.getID()));
                            c.attempt = 0;
                        }
                    }
                    
                }
            }        
        };
        manage.start();               
        
    }

    //method to receive messages from clients
    private void receiveMessages() {
        
        receive = new Thread("Receive Messages"){
            
            @Override
            public void run(){
                while (running) { 
                    //receiving
                    byte[] data = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(data, data.length);
                    try {
                        socket.receive(packet);
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //call method that process received data from socket
                    process(packet);                   
                    
                }
            }        
        };
        receive.start();         
        
    }
    
    //method that process received data from socket
    private void process(DatagramPacket packet) {
        String string = new String(packet.getData());
        if (string.startsWith("/c/")) {
            int id = UniqueIdentifier.getIdentifier();
            clients.add(new ServerClient(string.substring(3, string.length()), packet.getAddress(), packet.getPort(), id));
            System.out.println(string.substring(3, string.length()) +"; Identifier = "+ id +" IP Address; "+ packet.getAddress() +" port: "+ packet.getPort() );
            
            String messageforClient = "/c/" + id;
            sendToClientAsString(messageforClient, packet.getAddress(), packet.getPort());
            
        } else if(string.startsWith("/m/")) {
            sendToAll(string);
            System.out.println(string);
        }else if (string.startsWith("/d/")) {
            String clientId = string.split("/d/|/e/")[1];
            System.out.println(clientId);
            disconnect(Integer.parseInt(clientId),true);
        } else if (string.startsWith("/i/")) {
            clientResponse.add(Integer.parseInt(string.split("/i/|/e/")[1]));
        } else if (string.startsWith("/stocktrace/")) {
            String stocktracedata = clientDBRequests.updateStockTraceData().toString();
                       
            stocktracedata = "/stocktrace/" + stocktracedata;            
            sendToClientAsString(stocktracedata, packet.getAddress(), packet.getPort());
            
        } else if (string.startsWith("/generalstock/")) {
            String generalStore = clientDBRequests.updateGeneralStockData().toString();
           // System.out.println(generalStore); 
            
            generalStore = "/generalstock/" + generalStore;
            sendToClientAsString(generalStore, packet.getAddress(), packet.getPort());
            
        } else if (string.startsWith("/issued/")) {
            String issueddata = clientDBRequests.updateIssuedUnitsData().toString();
           // System.out.println(generalStore); 
            
            issueddata = "/issued/" + issueddata;
            sendToClientAsString(issueddata, packet.getAddress(), packet.getPort());
            
        } else if (string.startsWith("/supplier/")) {
            String suppliers = clientDBRequests.updateSuppliersData().toString();
           // System.out.println(generalStore); 
            
            suppliers = "/supplier/" + suppliers;
            sendToClientAsString(suppliers, packet.getAddress(), packet.getPort());
            
        } else if (string.startsWith("/gendp/")) {
            String dp = string.substring(7, string.length()).trim();
            
            String gendp = clientDBRequests.updateGeneralStockByDb(dp).toString();
           // System.out.println(gendp); 
            
            gendp = "/gendp/" + gendp;
            sendToClientAsString(gendp, packet.getAddress(), packet.getPort());
            
        } else if (string.startsWith("/thisdate/")) {
            String date = string.substring(10, string.length()).trim();
            
            String stocktracethisdate = clientDBRequests.updateStockTraceDataSpecificDate(date).toString();
                       
            stocktracethisdate = "/thisdate/" + stocktracethisdate;            
            sendToClientAsString(stocktracethisdate, packet.getAddress(), packet.getPort());
            
        }  else if (string.startsWith("/issuedByDate/")) {
            String bydate = string.substring(14, string.length()).trim();
            
            String issuedbydate = clientDBRequests.updateIssuedUnitsSpecificDate(bydate).toString();
                       
            issuedbydate = "/issuedByDate/" + issuedbydate;            
            sendToClientAsString(issuedbydate, packet.getAddress(), packet.getPort());
            
        } else if (string.startsWith("/deperts/")) {
            String dps = clientDBRequests.updateDepartments().toString();
                       
            dps = "/deperts/" + dps;            
            sendToClientAsString(dps, packet.getAddress(), packet.getPort());
            
        } else if (string.startsWith("/stockdp/")) {
            String date = string.substring(9, string.length()).trim();
            
            String stocktracethisdate = clientDBRequests.updateStockTraceDataByDepart(date).toString();
                       
            stocktracethisdate = "/stockdp/" + stocktracethisdate;            
            sendToClientAsString(stocktracethisdate, packet.getAddress(), packet.getPort());
            
        }  else if (string.startsWith("/issuedp/")) {
            String dp = string.substring(9, string.length()).trim();
            
            String issuedepartment = clientDBRequests.updateIssuedUnitsByDepart(dp).toString();
                       
            issuedepartment = "/issuedp/" + issuedepartment;            
            sendToClientAsString(issuedepartment, packet.getAddress(), packet.getPort());
            
        } else {
            System.out.println(string);        
        }
    }

    // method to send messages to all the clients at once
    private void sendToAll(String message) {
        for (int i = 0; i < clients.size(); i++) {
            ServerClient client = clients.get(i);
            sendToClient(message.getBytes(), client.address, client.port);
        }
    }
    
    // ################### methods to send data to the client #################
    private void sendToClient(final  byte[] data, final  InetAddress address, final int  port){
        send = new Thread("SendToClient"){
            
            @Override
            public void run(){
                DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
                try {
                    socket.send(packet);
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            
            }
        };
        send.start();
    }
    
    private void sendToClientAsString(String message , final  InetAddress address, final int  port){
        message += "/e/";
        sendToClient(message.getBytes(), address, port);
    
    }
    // ################### end of methods to send data to the client ###########

    private void disconnect(int id, boolean status) {
        String clientName = null;
        String message = null;
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getID() == id) {
                clientName = clients.get(i).name;
                clients.remove(i);
                break;
            }
        }
        
        if (status) {
            message = clientName+" Logged out.";
        } else {
            message = clientName+" Timmed out.";
        }
        System.out.println(message);
    }
    
//######################################### client database request ############

   
 
    
    
}// end of class
