package tangibleApplication.Controlers;

import TUIO.TuioClient;
import tangibleApplication.Models.M_Point;
import tangibleApplication.Models.M_Segment;
import tangibleApplication.Views.V_JComponentMain;

import java.util.Enumeration;

/**
 * Created by Zachizac on 04/04/2017.
 */
public class C_Client {

    V_JComponentMain comp;

    public C_Client(V_JComponentMain comp){
        this.comp = comp;
    }

    public void newSegment(){

        if(comp.getObjectList().size()==2) {

            Enumeration<M_Point> objects = comp.getObjectList().elements();
            M_Point p1 = objects.nextElement();
            M_Point p2 = objects.nextElement();

            M_Segment segment = new M_Segment(p1, p2);
            comp.addSegmentList(segment);
        }

    }



}
