package tangibleApplication.Views;

import TUIO.*;
import tangibleApplication.Controlers.C_Client;
import tangibleApplication.Models.M_Point;
import tangibleApplication.Models.M_Segment;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * Created by Zachizac on 04/04/2017.
 */
public class V_JComponentMain extends JComponent{


    public static final int finger_size = 15;
    public static final int object_size = 20;
    public static final int table_size = 760;
    public static final int id_segment = 10;


    public static int width, height;
    private float scale = 1.0f;
    private C_Client controlClient;

    public V_JComponentMain(){
        super();
        controlClient = new C_Client(this);
    }

    public void setSize(int w, int h) {
        super.setSize(w,h);
        width = w;
        height = h;
        scale  = height/(float) V_JComponentMain.table_size;
    }

    public void paint(Graphics g) {
        update(g);
    }

    public void update(Graphics g) {

        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2.setColor(Color.white);
        g2.fillRect(0,0,width,height);

        int w = (int)Math.round(width-scale*finger_size/2.0f);
        int h = (int)Math.round(height-scale*finger_size/2.0f);

        Enumeration<TuioCursor> cursors = controlClient.getCursorList().elements();
        while (cursors.hasMoreElements()) {
            TuioCursor tcur = cursors.nextElement();
            if (tcur==null) continue;
            ArrayList<TuioPoint> path = tcur.getPath();
            TuioPoint current_point = path.get(0);
            if (current_point!=null) {
                // draw the cursor path
                g2.setPaint(Color.blue);
                for (int i=0;i<path.size();i++) {
                    TuioPoint next_point = path.get(i);
                    g2.drawLine(current_point.getScreenX(w), current_point.getScreenY(h), next_point.getScreenX(w), next_point.getScreenY(h));
                    current_point = next_point;
                }
            }

            // draw the finger tip
            g2.setPaint(Color.lightGray);
            int s = (int)(scale*finger_size);
            g2.fillOval(current_point.getScreenX(w-s/2),current_point.getScreenY(h-s/2),s,s);
            g2.setPaint(Color.black);
            g2.drawString(tcur.getCursorID()+"",current_point.getScreenX(w),current_point.getScreenY(h));
        }

        // draw the objects
        Enumeration<M_Point> objects = controlClient.getActualObjectList().elements();
        while (objects.hasMoreElements()) {
            M_Point tobj = objects.nextElement();
            if (tobj!=null) tobj.paint(g2, width,height);
        }

        Enumeration<M_Segment> segments = controlClient.getSegmentList().elements();
        while (segments.hasMoreElements()){
            M_Segment s = segments.nextElement();
            if(s!=null) s.paint(g2, width, height);
        }

    }

    public C_Client getControlClient() {
        return controlClient;
    }

}