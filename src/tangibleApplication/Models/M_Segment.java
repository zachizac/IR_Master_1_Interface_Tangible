package tangibleApplication.Models;


import tangibleApplication.Views.V_JPanelMain;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Created by Zachizac on 04/04/2017.
 */
public class M_Segment {

    private Line2D line;
    private M_Point p1;
    private M_Point p2;
    private int Symbolp1;
    private int Symbolp2;
    private int numSegment;

    /**
     * Constructeur Segment
     * @param p1 le point de départ du segment
     * @param p2 le point de fin du segment
     */
    public M_Segment(M_Point p1, M_Point p2, int sP1, int sP2, int numSegment) {

        this.p1 = p1;
        this.p2 = p2;
        this.Symbolp1 = sP1;
        this.Symbolp2 = sP2;
        this.numSegment = numSegment;
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

    public M_Point getP1() {
        return p1;
    }

    public M_Point getP2() {
        return p2;
    }

    public int getSymbolp1() {
        return Symbolp1;
    }

    public int getSymbolp2() {
        return Symbolp2;
    }

    public void setSymbolp1(int symbolp1) {
        Symbolp1 = symbolp1;
    }

    public void setSymbolp2(int symbolp2) {
        Symbolp2 = symbolp2;
    }

    public int getNumSegment() {
        return numSegment;
    }

    public void setNumSegment(int numSegment) {
        this.numSegment = numSegment;
    }

    public float getDistance(){
        return p1.getDistance(p2);
    }
}
