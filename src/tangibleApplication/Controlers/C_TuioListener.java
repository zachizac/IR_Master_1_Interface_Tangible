package tangibleApplication.Controlers;

import TUIO.*;
import tangibleApplication.Models.M_Cercle;
import tangibleApplication.Models.M_Point;
import tangibleApplication.Models.M_Segment;
import tangibleApplication.Views.V_JPanelMain;
import java.util.*;
import static tangibleApplication.Views.V_JPanelMain.id_tagAction;

/**
 * Created by Zachizac on 04/04/2017.
 */
public class C_TuioListener implements TuioListener {

    private Hashtable<Long,M_Point> actualObjectList = new Hashtable<Long, M_Point>(); //Liste des points présents actuellement
    private Hashtable<Long,M_Point> globalObjectList = new Hashtable<Long, M_Point>(); //Liste de tous les points apparut au oours de cette instance
    private Hashtable<Long, TuioCursor> cursorList = new Hashtable<Long,TuioCursor>();
    private Hashtable<Long, M_Segment> segmentList = new Hashtable<Long, M_Segment>();
    private Hashtable<Long, M_Cercle> cercleList = new Hashtable<Long, M_Cercle>();

    private int [] symboleIDFigure = {-1, -1};

    int incrPointId = 1;

    V_JPanelMain comp;
    private boolean verbose = false;
    private int nbrSegments = 0;
    private int nbrCercle = 0;
    public int nbMenu;
    public int activeMenu;


    public C_TuioListener(V_JPanelMain comp, int nbMenu){
        this.comp = comp;
        this.nbMenu = comp.getNbMenu();
    }
    
    public void addTuioObject(TuioObject tobj) {

        M_Point point = new M_Point(tobj);

        actualObjectList.put(tobj.getSessionID(), point);

        if(checkId(tobj, globalObjectList)) {
            majSegments(tobj);
            majCercle(tobj);
            removeId(tobj, globalObjectList);
            globalObjectList.put(tobj.getSessionID(), point);
            setDispa(tobj, globalObjectList, 0);
            M_Point pointModif = (M_Point) actualObjectList.get(tobj.getSessionID());
            pointModif.update(tobj);

        } else {
            globalObjectList.put(tobj.getSessionID(), point);
        }


        if (verbose)
            System.out.println("add obj "+tobj.getSymbolID()+" ("+tobj.getSessionID()+") "+tobj.getX()+" "+tobj.getY()+" "+tobj.getAngle());
    }

    public void updateTuioObject(TuioObject tobj) {

        if(activeMenu == nbMenu){ //Si on se trouve dans le menu effacer
            if(tobj.getSymbolID()==id_tagAction){
                    M_Point point = (M_Point) actualObjectList.get(tobj.getSessionID()); // on recup le point dans la liste actuelle et on l update
                point.update(tobj);
            }
        }else {
            M_Point point = (M_Point) actualObjectList.get(tobj.getSessionID());
            if(point != null) { // Empeche d'appeler update sur un point null
                point.update(tobj);
                majSegments(tobj);
                majCercle(tobj);
            }
        }

        if (verbose)
            System.out.println("set obj "+tobj.getSymbolID()+" ("+tobj.getSessionID()+") "+tobj.getX()+" "+tobj.getY()+" "+tobj.getAngle()+" "+tobj.getMotionSpeed()+" "+tobj.getRotationSpeed()+" "+tobj.getMotionAccel()+" "+tobj.getRotationAccel());
    }

    public void removeTuioObject(TuioObject tobj) {
        if(tobj.getSymbolID()!=id_tagAction) {

            switch (activeMenu) {
                case 2:
                    if (tobj.getSymbolID() != symboleIDFigure[0] || tobj.getSymbolID() != symboleIDFigure[1]) {
                        //j'évite que le les deux id du tableau ait la meme valeur, par ailleurs je ne rempli l'indice 0 que si il est vide
                        if (symboleIDFigure[0] == -1 || symboleIDFigure[0] == tobj.getSymbolID()) {
                            symboleIDFigure[0] = tobj.getSymbolID();
                        } else {
                            symboleIDFigure[1] = tobj.getSymbolID();
                            newSegment();
                            symboleIDFigure[0] = -1;
                            symboleIDFigure[1] = -1;
                        }
                    }
                    break;
                case 4:
                    if (tobj.getSymbolID() != symboleIDFigure[0] || tobj.getSymbolID() != symboleIDFigure[1]) {
                        //j'évite que le les deux id du tableau ait la meme valeur, par ailleurs je ne rempli l'indice 0 que si il est vide
                        if (symboleIDFigure[0] == -1 || symboleIDFigure[0] == tobj.getSymbolID()) {
                            symboleIDFigure[0] = tobj.getSymbolID();
                        } else {
                            symboleIDFigure[1] = tobj.getSymbolID();
                            newCercle();
                            symboleIDFigure[0] = -1;
                            symboleIDFigure[1] = -1;
                        }
                    }
                    break;
                default:
                    symboleIDFigure[0] = -1;
                    symboleIDFigure[1] = -1;
            }


            removeId(tobj, actualObjectList);

            setDispa(tobj, globalObjectList, System.currentTimeMillis());

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!checkId(tobj, actualObjectList) && dispaTime(tobj, globalObjectList)) {
                        setSymbolId(tobj, globalObjectList);
                        timer.cancel();
                    }
                }
            }, 3 * 1000);

            if (verbose)
                System.out.println("del obj " + tobj.getSymbolID() + " (" + tobj.getSessionID() + ")");
        }
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

        Iterator itKey = actualObjectList.keySet().iterator();
        Object o;
        M_Point p1 = null;
        M_Point p2 = null;
        while(itKey.hasNext()) {
            o = itKey.next();
            if (actualObjectList.get(o).getSymbolID() == symboleIDFigure[0]) p1 = actualObjectList.get(o);
            if (actualObjectList.get(o).getSymbolID() == symboleIDFigure[1]) p2 = actualObjectList.get(o);
        }
        if(p1 != null && p2 != null){
            itKey = segmentList.keySet().iterator();
            while(itKey.hasNext()) {
                o = itKey.next();
                if ((segmentList.get(o).getSymbolp1()==symboleIDFigure[0] && segmentList.get(o).getSymbolp2()==symboleIDFigure[1])
                        || (segmentList.get(o).getSymbolp1()==symboleIDFigure[1] && segmentList.get(o).getSymbolp2()==symboleIDFigure[0])) return;
            }
            nbrSegments++;
            M_Segment segment = new M_Segment(p1, p2, symboleIDFigure[0], symboleIDFigure[1],nbrSegments);
            addSegmentList(segment);
        }

    }

    public void majSegments(TuioObject tobj){
        Iterator itKey = segmentList.keySet().iterator();
        Object o;
        M_Point point = new M_Point(tobj);
        while(itKey.hasNext()) {
            o = itKey.next();
            if (segmentList.get(o).getSymbolp1() == tobj.getSymbolID()){
                segmentList.get(o).update(point,segmentList.get(o).getP2());
            }
            if (segmentList.get(o).getSymbolp2() == tobj.getSymbolID()){
                segmentList.get(o).update(segmentList.get(o).getP1(),point);
            }
        }
    }

    public void newCercle(){
        Iterator itKey = actualObjectList.keySet().iterator();
        Object o;
        M_Point centre = null;
        M_Point point = null;
        while(itKey.hasNext()) {
            o = itKey.next();
            if (actualObjectList.get(o).getSymbolID() == symboleIDFigure[0]) centre = actualObjectList.get(o);
            if (actualObjectList.get(o).getSymbolID() == symboleIDFigure[1]) point = actualObjectList.get(o);
        }
        if(centre != null && point != null){
            itKey = cercleList.keySet().iterator();
            while(itKey.hasNext()) {
                o = itKey.next();
                if ((cercleList.get(o).getSymbolCentre()==symboleIDFigure[0] && cercleList.get(o).getSymbolPoint()==symboleIDFigure[1])
                        || (cercleList.get(o).getSymbolCentre()==symboleIDFigure[1] && cercleList.get(o).getSymbolPoint()==symboleIDFigure[0])) return;
            }
            nbrCercle++;
            M_Cercle cercle = new M_Cercle(centre, point, symboleIDFigure[0], symboleIDFigure[1],nbrCercle);
            addCercleList(cercle);
        }
    }

    public void majCercle(TuioObject tobj){
        Iterator itKey = cercleList.keySet().iterator();
        Object o;
        M_Point point = new M_Point(tobj);
        while(itKey.hasNext()) {
            o = itKey.next();
            if (cercleList.get(o).getSymbolCentre() == tobj.getSymbolID()){
                cercleList.get(o).update(point,cercleList.get(o).getPoint());
            }
            if (cercleList.get(o).getSymbolPoint() == tobj.getSymbolID()){
                cercleList.get(o).update(cercleList.get(o).getCentre(),point);
            }
        }
    }


    public boolean checkId(TuioObject tobj, Hashtable<Long,M_Point> h){

        Iterator itKey = h.keySet().iterator();
        Object o;
        while(itKey.hasNext()) {
            o = itKey.next();
            if (h.get(o).getSymbolID() == tobj.getSymbolID()) {
                return true;
            }
        }
        return false;
    }

    public void removeId(TuioObject tobj, Hashtable<Long,M_Point> h){

        Iterator itKey = h.keySet().iterator();
        Object o;
        while(itKey.hasNext()){
            o = itKey.next();
            if(h.get(o).getSymbolID()== tobj.getSymbolID()){
                h.remove(o);
                return;
            }
        }
    }

    public void setSymbolId(TuioObject tobj, Hashtable<Long,M_Point> h){

        Iterator itKey = h.keySet().iterator();
        Object o;
        while(itKey.hasNext()){
            o = itKey.next();
            if(h.get(o).getSymbolID() == tobj.getSymbolID()){
                h.get(o).setSymbolID(-incrPointId);
                System.out.println(h.get(o).getSymbolID());
            }
        }
        itKey = segmentList.keySet().iterator();
        while(itKey.hasNext()) {
            o = itKey.next();
            if (segmentList.get(o).getSymbolp1()==tobj.getSymbolID()){
                segmentList.get(o).setSymbolp1(-incrPointId);
            }
            if (segmentList.get(o).getSymbolp2()==tobj.getSymbolID()){
                segmentList.get(o).setSymbolp2(-incrPointId);
            }
        }
        incrPointId++;

        itKey = cercleList.keySet().iterator();
        while(itKey.hasNext()) {
            o = itKey.next();
            if (cercleList.get(o).getSymbolCentre()==tobj.getSymbolID()){
                cercleList.get(o).setSymbolCentre(-incrPointId);
            }
            if (cercleList.get(o).getSymbolPoint()==tobj.getSymbolID()){
                cercleList.get(o).setSymbolPoint(-incrPointId);
            }
        }
        incrPointId++;
    }

    public void setDispa(TuioObject tobj, Hashtable<Long,M_Point> h, long timeDispa){

        Iterator itKey = h.keySet().iterator();
        Object o;
        while(itKey.hasNext()){
            o = itKey.next();
            if(h.get(o).getSymbolID() == tobj.getSymbolID()){
                h.get(o).setDisparition(timeDispa);
                return;
            }
        }
    }

    public boolean dispaTime(TuioObject tobj, Hashtable<Long,M_Point> h) {

        Iterator itKey = h.keySet().iterator();
        Object o;
        while (itKey.hasNext()) {
            o = itKey.next();
            if(h.get(o).getSymbolID() == tobj.getSymbolID()){
                if(System.currentTimeMillis()-h.get(o).getDisparition()>=3000) return true;
            }
        }
        return false;
    }

    public void resetHashTable(TuioObject tobj, Hashtable<Long, M_Point> h){

        Iterator itKey = h.keySet().iterator();
        Object o;
        while(itKey.hasNext()){
            o = itKey.next();
            if(h.get(o).getSymbolID()!= tobj.getSymbolID()){
                h.remove(o);
                return;
            }
        }
    }


    public void resetDisplay(TuioObject tobj){

        resetHashTable(tobj, globalObjectList);
        resetHashTable(tobj, actualObjectList);
        getSegmentList().clear();
        getCercleList().clear();
        comp.repaint();
    }


    public void addSegmentList(M_Segment segment) {
        this.segmentList.put((long)nbrSegments,segment);
    }

    public void addCercleList(M_Cercle cercle) {
        this.cercleList.put((long)nbrCercle,cercle);
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

    public Hashtable<Long, M_Point> getGlobalObjectList() {
        return globalObjectList;
    }

    public Hashtable<Long, M_Segment> getSegmentList() {
        return segmentList;
    }

    public Hashtable<Long, M_Cercle> getCercleList() {
        return cercleList;
    }

    public void addTuioBlob(TuioBlob tblb) {}

    public void updateTuioBlob(TuioBlob tblb) {}

    public void removeTuioBlob(TuioBlob tblb) {}

    public void refresh(TuioTime frameTime) {
        comp.repaint();
    }

    public int getActiveMenu() {
        return activeMenu;
    }

    public void setActiveMenu(int activeMenu) {
        this.activeMenu = activeMenu;
    }
}
