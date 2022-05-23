
import java.io.*;
import java.net.Socket;
import java.util.Scanner;



public class Klient{
    public static int port = 8888;

    public static void main(String[] args) throws Exception {new Klient();}


    public Klient() throws Exception{
        Socket socket = new Socket("localhost", port);


        if (socket.isConnected()) {
            System.out.println("Połączono z serwerem");
        }

        CipherClass cipherClassKlient = new CipherClass();


        Thread thread1 = new Thread(() -> {

            try {



                while (true) {



                    //wysyłanie
                    PrintWriter pWriter = new PrintWriter(socket.getOutputStream());
                    Scanner scannerZnakow = new Scanner(System.in);
                    String wiadomoscDlaSerwera = scannerZnakow.nextLine();



                    if (wiadomoscDlaSerwera != "") {

                        //SZYFROWANIE

                        //Drugi sposób zapisu bitow do zmienne
                        byte[] wiadomoscDlaSerweraBytes = wiadomoscDlaSerwera.getBytes();
                        pWriter.println("" + cipherClassKlient.Encrypt(wiadomoscDlaSerweraBytes, cipherClassKlient.getPublicKey()));
                        pWriter.flush();


                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject(cipherClassKlient);
                        oos.flush();



                    } else {
                        System.out.println("Podaj scieżke do pliku:  (np: C:/windows/windows32)");
                        String sciezka = scannerZnakow.nextLine();
                        if(sciezka == ""){
                            System.out.println("Podaj ścieżke do pliku :)");

                        }else {
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

                    System.out.println("Serwer zaszyfrowana wiadomość: " + str);



                }
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }


        });

        thread1.start();
        thread2.start();
    }



}

