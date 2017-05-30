package tangibleApplication.Views;

import TUIO.TuioListener;
import tangibleApplication.Controlers.C_FullScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

/**
 * Created by Zachizac on 04/04/2017.
 * Class de creation de la fenetre de l'application
 */
public class V_MainWindow {

    private final int window_width  = 1024;
    private final int window_height = 1024;
    private final int panelMenu_width = 100;

    private boolean fullscreen = false;

    private V_JPanelMain comp;
    private JFrame frame;
    private GraphicsDevice device;
    private Cursor invisibleCursor;

    /**
     * Constructeur de MainWindow, la fenetre de l'application
     */
    public V_MainWindow() {
        //on cree le component
        comp = new V_JPanelMain();
        device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        invisibleCursor = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "invisible cursor");
        setupWindow();
        showWindow();

    }

    /**
     * getter du Listener
     * @return le TuioListener correspondant au JComponent
     */
    public TuioListener getTuioListener() {
        return comp.getTuioListener();
    }

    /**
     * methode de creation du JFrame
     */
    public void setupWindow() {

        frame = new JFrame();
        frame.setContentPane(comp);
        frame.setTitle("TangibleApplication");
        frame.setResizable(false);
        frame.addWindowListener( new WindowAdapter() { public void windowClosing(WindowEvent evt) {
            System.exit(0);
        } });
        frame.addKeyListener( new C_FullScreen(this));
    }

    /**
     * Methode de destruction de la fenetre pour switcher en mode plein ecran
     */
    public void destroyWindow() {

        frame.setVisible(false);
        if (fullscreen) {
            device.setFullScreenWindow(null);
        }
        frame = null;
    }

    /**
     * Methode permettant l'affichage de la frame
     */
    public void showWindow() {

        if (fullscreen) {
            int width  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
            int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
            comp.setSize(width,height);

            frame.setSize(width,height);
            frame.setUndecorated(true);
            device.setFullScreenWindow(frame);
            frame.setCursor(invisibleCursor);
        } else {
            int width  = window_width;
            int height = window_height;
            comp.setSize(width,height);

            frame.pack();
            Insets insets = frame.getInsets();
            frame.setSize(width,height +insets.top);
            frame.setCursor(Cursor.getDefaultCursor());
        }
        frame.setVisible(true);
        frame.repaint();
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    public V_JPanelMain getComp() {
        return comp;
    }
}
