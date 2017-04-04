package tangibleApplication;

import TUIO.TuioClient;
import tangibleApplication.Views.V_MainWindow;

/**
 * Created by Zachizac on 04/04/2017.
 * class Main de l'application TangibleApplication dans le cadre de l'initiation a la recherche
 */
public class Main {

    public static void main(String argv[]) {

        //creation de la fenetre principale de l'application
        V_MainWindow v_mainWindow = new V_MainWindow();

        TuioClient client = null;

        //le main accepte un argument pour pouvoir selectionner le port de lecture pour l'application
        switch (argv.length) {
            case 1:
                try {
                    client = new TuioClient( Integer.parseInt(argv[0]));
                } catch (Exception e) {
                    System.out.println("usage: java Main [port]");
                    System.exit(0);
                }
                break;
            case 0:
                client = new TuioClient();
                break;
            default:
                System.out.println("usage: java Main [port]");
                System.exit(0);
                break;
        }

        //Ainsi on connecte le client si il a pu être crée (si le numéro de port était libre)
        if (client!=null) {
            client.addTuioListener(v_mainWindow.getTuioListener());
            client.connect();
        } else {
            System.out.println("usage: java Main [port]");
            System.exit(0);
        }
    }

}
