package tangibleApplication.Controlers;

import TUIO.*;
import tangibleApplication.Models.M_Point;
import tangibleApplication.Models.M_Segment;
import tangibleApplication.Views.V_JPanelMain;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by Zachizac on 04/04/2017.
 */
public class C_TuioListener implements TuioListener {

    private Hashtable<Long,M_Point> actualObjectList = new Hashtable<Long, M_Point>(); //Liste des points présents actuellement
    private Hashtable<Long,M_Point> global0bjectList = new Hashtable<Long, M_Point>(); //Liste de tous les points apparut au oours de cette instance
    private Hashtable<Long, TuioCursor> cursorList = new Hashtable<Long,TuioCursor>();
    private Hashtable<Long, M_Segment> segmentList = new Hashtable<Long, M_Segment>();

    V_JPanelMain comp;
    private boolean verbose = false;
    private Long nbrSegments = new Long(0);

    public C_TuioListener(V_JPanelMain comp){
        this.comp = comp;
    }

    /*public void addTuioObject(TuioObject tobj) {
        if(tobj.getSymbolID() == comp.id_segment){
            newSegment();
        }
        else {
            M_Point point = new M_Point(tobj);
            actualObjectList.put(tobj.getSessionID(), point);
            if(!checkId(point))
                global0bjectList.put(tobj.getSessionID(), point);
        }
        if (verbose)
            System.out.println("add obj "+tobj.getSymbolID()+" ("+tobj.getSessionID()+") "+tobj.getX()+" "+tobj.getY()+" "+tobj.getAngle());
    }*/

    public void addTuioObject(TuioObject tobj) {

        M_Point point = new M_Point(tobj);
        actualObjectList.put(tobj.getSessionID(), point);
        if(!checkId(point))
            global0bjectList.put(tobj.getSessionID(), point);
        if (verbose)
            System.out.println("add obj "+tobj.getSymbolID()+" ("+tobj.getSessionID()+") "+tobj.getX()+" "+tobj.getY()+" "+tobj.getAngle());
    }
    public void updateTuioObject(TuioObject tobj) {
       // if(tobj.getSymbolID() != comp.id_segment) {
            M_Point point = (M_Point) actualObjectList.get(tobj.getSessionID()); //on recup le pts de la liste actuelle et on l update
            point.update(tobj);
            point = (M_Point) global0bjectList.get(tobj.getSessionID()); // on recup le point dans la liste globale et on l update
            point.update(tobj);
       // }
        if (verbose)
            System.out.println("set obj "+tobj.getSymbolID()+" ("+tobj.getSessionID()+") "+tobj.getX()+" "+tobj.getY()+" "+tobj.getAngle()+" "+tobj.getMotionSpeed()+" "+tobj.getRotationSpeed()+" "+tobj.getMotionAccel()+" "+tobj.getRotationAccel());
    }

    public void removeTuioObject(TuioObject tobj) {
        actualObjectList.remove(tobj.getSessionID());

        if (verbose)
            System.out.println("del obj "+tobj.getSymbolID()+" ("+tobj.getSessionID()+")");
    }

    public void addTuioCursor(TuioCursor tcur) {

        if (!cursorList.containsKey(tcur.getSessionID())) {
            cursorList.put(tcur.getSessionID(), tcur);
            comp.repaint();
        }

        if (verbose)
            System.out.println("add cur "+tcur.getCursorID()+" ("+tcur.getSessionID()+") "+tcur.getX()+" "+tcur.getY());
    }

    public void updateTuioCursor(TuioCursor tcur) {

        comp.repaint();

        if (verbose)
            System.out.println("set cur "+tcur.getCursorID()+" ("+tcur.getSessionID()+") "+tcur.getX()+" "+tcur.getY()+" "+tcur.getMotionSpeed()+" "+tcur.getMotionAccel());
    }

    public void removeTuioCursor(TuioCursor tcur) {

        cursorList.remove(tcur.getSessionID());
        comp.repaint();

        if (verbose)
            System.out.println("del cur "+tcur.getCursorID()+" ("+tcur.getSessionID()+")");
    }

    public void newSegment(){

        if(actualObjectList.size()==2) {

            Enumeration<M_Point> objects = actualObjectList.elements();
            M_Point p1 = objects.nextElement();
            M_Point p2 = objects.nextElement();

            M_Segment segment = new M_Segment(p1, p2);
            addSegmentList(segment);
        }

    }

    public void addSegmentList(M_Segment segment) {
        nbrSegments++;
        this.segmentList.put(nbrSegments,segment);
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public Hashtable<Long, TuioCursor> getCursorList() {
        return cursorList;
    }

    public Hashtable<Long, M_Point> getActualObjectList() {
        return actualObjectList;
    }

    public Hashtable<Long, M_Point> getGlobal0bjectList() {
        return global0bjectList;
    }

    public Hashtable<Long, M_Segment> getSegmentList() {
        return segmentList;
    }

    public boolean checkId(M_Point point){
        if(global0bjectList.contains(point))
            return true;
        return false;
    }

    public void addTuioBlob(TuioBlob tblb) {}

    public void updateTuioBlob(TuioBlob tblb) {}

    public void removeTuioBlob(TuioBlob tblb) {}

    public void refresh(TuioTime frameTime) {
        comp.repaint();
    }



}
