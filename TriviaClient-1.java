/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Latihan;

/**
 *
 * @author LENOVO
 */
import java.net.*;
import java.io.*;
import java.util.*;
public class TriviaClient {
    private Socket client;

    DataInputStream din;
    DataOutputStream dout;
    BufferedReader bf;

    String send = "";
    String read = "";

    public TriviaClient(String hostname, int port) throws IOException {
        client = new Socket(hostname, port);

        din = new DataInputStream(client.getInputStream());
        dout = new DataOutputStream(client.getOutputStream());
        bf = new BufferedReader(new InputStreamReader(System.in));
    }

    public void setPesan(String pesan) throws IOException {
        send = pesan;
        if (send.equals("")) {
            send = bf.readLine();
        }

        dout.writeUTF(send);
        dout.flush();
    }

    public String getPesan() throws IOException {
        read = din.readUTF().toLowerCase();
        return read;
    }

    public void Disconnect() throws Exception {
        din.close();
        dout.close();
        client.close();
    }

    public static void main(String[] args) throws Exception {
        TriviaClient ts = null;
        Scanner sc = new Scanner(System.in);

        while (true) {
            try {
                ts = new TriviaClient("localhost", 1234);
                

                System.out.println("Client : permintaan");
                ts.setPesan("permintaan");
                ts.getPesan();
                System.out.println("Server : " + ts.read);
                System.out.println("Client : Jawaban");
                ts.getPesan();
                System.out.println("Server : " + ts.read);

                System.out.print("Client : ");
                String jawab = sc.nextLine();
                ts.setPesan(jawab);

                ts.getPesan();
                System.out.println("Server : " + ts.read);

                ts.getPesan();
                System.out.print("\nServer : " + ts.read);
                jawab = sc.nextLine();
                ts.setPesan(jawab);

                System.out.println("");
                ts.getPesan();
                if (Integer.parseInt(ts.read) == 2) {
                    System.exit(0);
                }
                
                ts.Disconnect();

            } catch (Exception e) {
            }
        }
    }
}
