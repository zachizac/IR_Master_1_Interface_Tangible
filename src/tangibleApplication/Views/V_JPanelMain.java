package tangibleApplication.Views;

import TUIO.*;
import tangibleApplication.Controlers.C_TuioListener;
import tangibleApplication.Models.M_Point;
import tangibleApplication.Models.M_Segment;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * Created by Zachizac on 04/04/2017.
 */
public class V_JPanelMain extends JPanel{


    public static final int finger_size = 15;
    public static final int object_size = 20;
    public static final int table_size = 760;
    public static final int id_segment = 10;

    public static final Border BORDER = new LineBorder(Color.black, 5);

    private final int panelMenu_width = 150;

    public static int width, height;
    private float scale = 1.0f;
    private C_TuioListener controlClient;


    public V_JPanelMain(){
        super();
        this.setBorder(BORDER);
        controlClient = new C_TuioListener(this);
    }

    public void setSize(int w, int h) {
        super.setSize(w,h);
        width = w;
        height = h;
        scale  = height/(float) V_JPanelMain.table_size;
    }

    public void paint(Graphics g) {
        update(g);
    }

    public void update(Graphics g) {

        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2.setColor(Color.white);
        g2.fillRect(5,5,width-10,height-10);

        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.black);
        g2.drawLine(panelMenu_width,0, panelMenu_width, height );

        for(int i=1;i<4;i++){
            g2.drawLine(0, i*(height/4), panelMenu_width, i*(height/4));
        }
        Font font = new Font("Courier", Font.BOLD,12);
        g2.setFont(font);
        g2.drawString("Zone point", 30, height/8);
        g2.drawString("Zone segment", 30, 3*(height/8));
        g2.setStroke(new BasicStroke(1));

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
            if(managePointDisplay(tobj) && (tobj!=null))
                tobj.paint(g2, width,height);

            System.out.println(tobj.getX()+ "\n");
            System.out.println(tobj.getY());
        }
        Enumeration<M_Segment> segments = controlClient.getSegmentList().elements();
        while (segments.hasMoreElements()){
            M_Segment s = segments.nextElement();
            if(s!=null) s.paint(g2, width, height);
        }

        super.paintBorder(g2);

    }

    public boolean managePointDisplay(M_Point point){

        float coordX = point.getX();
        float coordY = point.getY();
        int symbolID = point.getSymbolID();

        if(coordX < 0.16 && symbolID == 10){
            return true;
        }
        else if(coordX > 0.18 && symbolID != 10){
            return true;
        }

        return false;
    }
    public C_TuioListener getTuioListener() {
        return controlClient;
    }


}