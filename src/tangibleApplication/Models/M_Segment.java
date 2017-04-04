package tangibleApplication.Models;


import TUIO.TuioObject;
import tangibleApplication.Views.V_JComponentMain;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;

/**
 * Created by Zachizac on 04/04/2017.
 */
public class M_Segment {

    private Line2D line;
    private M_Point p1;
    private M_Point p2;

    /**
     * Constructeur Segment
     * @param p1 le point de départ du segment
     * @param p2 le point de fin du segment
     */
    public M_Segment(M_Point p1, M_Point p2) {

        int size = V_JComponentMain.object_size/2;
        this.p1 = p1;
        this.p2 = p2;
        line = new Line2D.Float(p1.getX(),p1.getY(),p2.getX(),p2.getY());

    }

    public void paint(Graphics2D g, int width, int height) {

        Shape l2 = new Line2D.Float(p1.getX()*width,p1.getY()*height,p2.getX()*width,p2.getY()*height);
        g.setPaint(Color.black);
        g.draw(l2);
        g.setPaint(Color.white);

    }

    public void update(M_Point p1, M_Point p2) {

        this.p1 = p1;
        this.p2 = p2;
        line = new Line2D.Float(p1.getX(),p1.getY(),p2.getX(),p2.getY());

    }


}
