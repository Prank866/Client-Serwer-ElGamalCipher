import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.lang.Exception;

public class Serwer{
    public static int port = 8888;

    public static void main(String[] args) throws Exception {new Serwer();}


    public Serwer()throws Exception{

        System.out.println("Czekanie na połączenie od klienta ...");
        ServerSocket serverSocket = new ServerSocket(port);
        Socket socket = serverSocket.accept();


        System.out.println("Klient Połączony");
        System.out.println("Czekaj Na wiadomosc od klienta!");

        CipherClass cipherClassSerwer = new CipherClass();


        Thread thread1 = new Thread(() -> {
            try {

                while (true) {


                    //wysyłanie
                    PrintWriter pWriter = new PrintWriter(socket.getOutputStream());
                    Scanner scannerZnakow = new Scanner(System.in);
                    String wiadomoscDlaKlienta = scannerZnakow.nextLine();


                    if(wiadomoscDlaKlienta != ""){

                        //dugi sposób zapisu bitow do zmiennej

                        byte[] wiadomoscDlaKlientaByte = wiadomoscDlaKlienta.getBytes();

                        pWriter.println("" + cipherClassSerwer.Encrypt(wiadomoscDlaKlientaByte, cipherClassSerwer.getPublicKey()));
                        pWriter.flush();



                    }else{
                        System.out.println("Podaj scieżke do pliku:  (np: C:/windows/windows32)");
                        String sciezka = scannerZnakow.nextLine();
                        if(sciezka == ""){
                            System.out.println("Podaj ścieżke do pliku :)");
                        }else{
                            Scanner scannerPlikow = new Scanner(new File(sciezka));

                            String linia = "";
                            while (scannerPlikow.hasNextLine()) {
                                linia += scannerPlikow.nextLine();
                                linia += " ";

                            }


                            pWriter.println("" + linia);
                            pWriter.flush();





                        }

                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
                try {
                    socket.close();
                    serverSocket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });


        Thread thread2 = new Thread(() -> {


            try {

                while (true) {
                    //odbieranie
                    InputStreamReader isReader = new InputStreamReader(socket.getInputStream());
                    BufferedReader buffReader = new BufferedReader(isReader);


                    String str = buffReader.readLine();

                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    CipherClass cipherClassKlient = (CipherClass) ois.readObject();

                    System.out.println("Klient zaszyfrowana wiadomosc: " + cipherClassKlient.Decrypt(str.getBytes(), cipherClassKlient.getPrivateKey()));


                }


            } catch (Exception e) {
                e.printStackTrace();
                try {
                    socket.close();
                    serverSocket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        });



        thread1.start();
        thread2.start();



    }

}
