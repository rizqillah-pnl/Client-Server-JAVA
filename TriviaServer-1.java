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
public class TriviaServer extends Thread {
    private ServerSocket server;
    private Socket client;

    DataInputStream din;
    DataOutputStream dout;
    BufferedReader bf;

    String send = "";
    String read = "";

    public TriviaServer(int port) throws IOException {
        server = new ServerSocket(port);
        client = server.accept();

        din = new DataInputStream(client.getInputStream());
        dout = new DataOutputStream(client.getOutputStream());
        bf = new BufferedReader(new InputStreamReader(System.in));
    }

    public void setPesan(String pesan) throws IOException {
        send = pesan;
        if (pesan.equals("")) {
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
        server.close();
    }

    public boolean cekJawaban(String[] soal, int noSoal, String jawaban) {
        boolean hasil = false;
        if (soal[noSoal].equals(jawaban)) {
            hasil = true;
        }
        return hasil;
    }

    public static void main(String[] args) throws IOException {
        String jawaban = "";
        boolean hasilCek = false;
        int nomor = 0;

        String[] jawabannya = {"James Gosling", "Joko Widodo", "Muhammad Arhami",
            "Indonesia Raya", "Agustus 1945"};
        String[] soal = {"0#Siapa Nama Pembuat Java?", "1#Siapa Nama Presdiden Indonesia ke-6?",
            "2#Siapa Nama Ketua Jurusan TIK di PNL?", "3#Judul Lagu Kebangsaan Indonesia?",
            "4#Bulan dan Tahun apa Indonesia Merdeka?(Format contoh: mei 2015)"};
        jawabannya[0] = jawabannya[0].toLowerCase();
        jawabannya[1] = jawabannya[1].toLowerCase();
        jawabannya[2] = jawabannya[2].toLowerCase();
        jawabannya[3] = jawabannya[3].toLowerCase();
        jawabannya[4] = jawabannya[4].toLowerCase();

        while (true) {
            try {
                TriviaServer ts = new TriviaServer(1234);

                ts.getPesan();
                System.out.println("Client : " + ts.read);

                if (ts.read.equals("permintaan")) {
                    if (nomor >= 5) {
                        nomor = 0;
                    }

                    ts.setPesan(soal[nomor]);
                    ts.setPesan("Berikan jawabanmu dengan format: <nomor pertanyaan>#<jawaban anda>");
                    ts.getPesan();
                    jawaban = ts.read;

                    String kata = jawaban.substring(0, 1);
                    if (kata.matches("[0-9]+")) {
                        if (nomor < 10 && nomor >= 0) {
                            jawaban = jawaban.substring(2, jawaban.length());
                            hasilCek = ts.cekJawaban(jawabannya, nomor, jawaban);

                        } else if (nomor >= 10) {
                            jawaban = jawaban.substring(3, jawaban.length());
                            hasilCek = ts.cekJawaban(jawabannya, nomor, jawaban);
                        }
                    } else {
                        jawaban = jawaban.substring(0, jawaban.length());
                        hasilCek = ts.cekJawaban(jawabannya, nomor, jawaban);
                    }

                    System.out.println("Nomor Soal : " + nomor + "\nJawaban : " + jawabannya[nomor]);
                    if (hasilCek) {
                        ts.setPesan("Kerja Yang Bagus!");
                    } else {
                        ts.setPesan("Anda Kurang Beruntung!");
                    }
                    if (ts.exit()) {
                        ts.setPesan("2");
                        System.exit(0);
                    }
                    nomor++;
                    System.out.println("");
                }

                ts.Disconnect();
            } catch (Exception e) {
            }
        }
    }

    public boolean exit() throws Exception {
        setPesan("Lanjut?\n1. Iya\n2. Tidak\nInput : ");
        getPesan();

        if (read.equals("2")) {
            return true;
        }
        return false;
    }
}
