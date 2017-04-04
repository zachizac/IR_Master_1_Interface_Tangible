package tangibleApplication.Controlers;

import tangibleApplication.Views.V_JComponentMain;
import tangibleApplication.Views.V_MainWindow;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by Zachizac on 04/04/2017.
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
        if (e.getKeyCode()==KeyEvent.VK_ESCAPE) System.exit(0);
        else if (e.getKeyCode()==KeyEvent.VK_F1) {
            window.destroyWindow();
            window.setupWindow();
            window.setFullscreen(!window.isFullscreen());
            window.showWindow();
        }
        else if (e.getKeyCode()==KeyEvent.VK_V) window.getComp().setVerbose(!window.getComp().isVerbose());
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
