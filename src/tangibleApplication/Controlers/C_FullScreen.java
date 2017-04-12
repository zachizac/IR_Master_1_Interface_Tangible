package tangibleApplication.Controlers;

import tangibleApplication.Views.V_MainWindow;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by Zachizac on 04/04/2017.
 * Class fullscreen qui ajoute un keylistener pour mettre l'applicatin en plein ecran
 *
 */
public class C_FullScreen implements KeyListener{

    private V_MainWindow window;

    /**
     * constructeur de controleur fullscreen
     * @param window la fenetre sur laquelle s'applique le controleur
     */
    public C_FullScreen(V_MainWindow window){ this.window = window; }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        //gere l'appuie sur une touche du clavier, notamment F1 pour mettre en plein ecran
        if (e.getKeyCode()==KeyEvent.VK_ESCAPE) System.exit(0);
        else if (e.getKeyCode()==KeyEvent.VK_F1) {
            window.destroyWindow();
            window.setupWindow();
            window.setFullscreen(!window.isFullscreen());
            window.showWindow();
        }
        else if (e.getKeyCode()==KeyEvent.VK_V) window.getComp().getTuioListener().setVerbose(!window.getComp().getTuioListener().isVerbose());
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
