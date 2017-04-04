package tangibleApplication.Views;

import TUIO.TuioListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

/**
 * Created by Zachizac on 04/04/2017.
 * Class de creation de la fenetre de l'application
 */
public class V_MainWindow {

    private final int window_width  = 640;
    private final int window_height = 480;

    private boolean fullscreen = false;

    private V_JComponentMain comp;
    private JFrame frame;
    private GraphicsDevice device;
    private Cursor invisibleCursor;

    public V_MainWindow() {
        comp = new V_JComponentMain();
        device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        invisibleCursor = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "invisible cursor");
        setupWindow();
        showWindow();
    }

    public TuioListener getTuioListener() {
        return comp;
    }

    public void setupWindow() {

        frame = new JFrame();
        frame.add(comp);

        frame.setTitle("TuioDemo");
        frame.setResizable(false);

        frame.addWindowListener( new WindowAdapter() { public void windowClosing(WindowEvent evt) {
            System.exit(0);
        } });

        frame.addKeyListener( new KeyAdapter() { public void keyPressed(KeyEvent evt) {
            if (evt.getKeyCode()==KeyEvent.VK_ESCAPE) System.exit(0);
            else if (evt.getKeyCode()==KeyEvent.VK_F1) {
                destroyWindow();
                setupWindow();
                fullscreen = !fullscreen;
                showWindow();
            }
            else if (evt.getKeyCode()==KeyEvent.VK_V) comp.verbose=!comp.verbose;
        } });
    }

    public void destroyWindow() {

        frame.setVisible(false);
        if (fullscreen) {
            device.setFullScreenWindow(null);
        }
        frame = null;
    }

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
}
