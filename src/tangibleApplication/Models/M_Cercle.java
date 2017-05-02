package tangibleApplication.Models;

import tangibleApplication.Views.V_JPanelMain;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

/**
 * Created by Zachizac on 02/05/2017.
 */
public class M_Cercle {

    private Ellipse2D cercle;
    private M_Point centre;
    private M_Point point;
    private M_Segment rayon;
    private int SymbolCentre;
    private int SymbolRayon;
    private int numCercle;

    /**
     * Constructeur Segment
     * @param p1 le point de départ du segment
     * @param p2 le point de fin du segment
     */
    public M_Cercle(M_Point p1, M_Point p2, int sP1, int sP2, int numCercle) {

        //int size = V_JPanelMain.object_size/2;
        this.centre = p1;
        this.point = p2;
        this.rayon = new M_Segment(p1,p2,sP1,sP2,-1);
        this.SymbolCentre = sP1;
        this.SymbolRayon = sP2;
        this.numCercle = numCercle;
        cercle = new Ellipse2D.Float(-centre.getX()-rayon.getDistance(),-centre.getY()-rayon.getDistance(),rayon.getDistance(),rayon.getDistance());
    }

    public void paint(Graphics2D g, int width, int height) {

        Shape c1 = new Ellipse2D.Float(-centre.getX()-rayon.getDistance(),-centre.getY()-rayon.getDistance(),rayon.getDistance(),rayon.getDistance());
        g.setPaint(Color.black);
        g.draw(c1);
        g.setPaint(Color.white);

    }

    public void update(M_Point p1, M_Point p2) {

        this.centre = p1;
        this.point = p2;
        this.rayon = new M_Segment(p1,p2,p1.getSymbolID(),p2.getSymbolID(),numCercle);
        cercle = new Ellipse2D.Float(-centre.getX()-rayon.getDistance(),-centre.getY()-rayon.getDistance(),rayon.getDistance(),rayon.getDistance());

    }

    public M_Point getCentre() {
        return centre;
    }

    public void setCentre(M_Point centre) {
        this.centre = centre;
    }

    public M_Point getPoint() {
        return point;
    }

    public void setPoint(M_Point point) {
        this.point = point;
    }

    public M_Segment getRayon() {
        return rayon;
    }

    public void setRayon(M_Segment rayon) {
        this.rayon = rayon;
    }

    public int getSymbolCentre() {
        return SymbolCentre;
    }

    public void setSymbolCentre(int symbolCentre) {
        SymbolCentre = symbolCentre;
    }

    public int getSymbolRayon() {
        return SymbolRayon;
    }

    public void setSymbolRayon(int symbolRayon) {
        SymbolRayon = symbolRayon;
    }

    public int getNumCercle() {
        return numCercle;
    }

    public void setNumCercle(int numCercle) {
        this.numCercle = numCercle;
    }
}
