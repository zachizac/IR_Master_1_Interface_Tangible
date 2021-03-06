/*
 TUIO Java library
 Copyright (c) 2005-2014 Martin Kaltenbrunner <martin@tuio.org>
 
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 3.0 of the License, or (at your option) any later version.
 
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 Lesser General Public License for more details.
 
 You should have received a copy of the GNU Lesser General Public
 License along with this library.
*/

package TUIO;

/**
 * The M_Point class on the one hand is a simple container and utility class to handle TUIO positions in general,
 * on the other hand the M_Point is the base class for the TuioCursor and TuioObject classes.
 *
 * @author Martin Kaltenbrunner
 * @version 1.1.0
 */ 
public class TuioPoint {
	
	/**
	 * X coordinate, representated as a floating point value in a range of 0..1  
	 */
	protected float xpos;
	/**
	 * Y coordinate, representated as a floating point value in a range of 0..1  
	 */
	protected float ypos;
	/**
	 * The time stamp of the last update represented as TuioTime (time since session start)
	 */
	protected TuioTime currentTime;
	/**
	 * The creation time of this M_Point represented as TuioTime (time since session start)
	 */
	protected TuioTime startTime;
	
	/**
	 * The default constructor takes no arguments and sets   
	 * its coordinate attributes to zero and its time stamp to the current session time.
	 */
	public TuioPoint() {
		xpos = 0.0f;
		ypos = 0.0f;
		currentTime = TuioTime.getSessionTime();
		startTime = new TuioTime(currentTime);
	}
	
	/**
	 * This constructor takes two floating point coordinate arguments and sets   
	 * its coordinate attributes to these values and its time stamp to the current session time.
	 *
	 * @param	xp	the X coordinate to assign
	 * @param	yp	the Y coordinate to assign
	 */
	public TuioPoint(float xp, float yp) {
		xpos = xp;
		ypos = yp;
		currentTime = TuioTime.getSessionTime();
		startTime = new TuioTime(currentTime);
	}

	/**
	 * This constructor takes a M_Point argument and sets its coordinate attributes
	 * to the coordinates of the provided M_Point and its time stamp to the current session time.
	 *
	 * @param	tpoint	the M_Point to assign
	 */
	public TuioPoint(TuioPoint tpoint) {
		xpos = tpoint.getX();
		ypos = tpoint.getY();
		currentTime = TuioTime.getSessionTime();
		startTime = new TuioTime(currentTime);
	}
	
	/**
	 * This constructor takes a TuioTime object and two floating point coordinate arguments and sets   
	 * its coordinate attributes to these values and its time stamp to the provided TUIO time object.
	 *
	 * @param	ttime	the TuioTime to assign
	 * @param	xp	the X coordinate to assign
	 * @param	yp	the Y coordinate to assign
	 */
	public TuioPoint(TuioTime ttime, float xp, float yp) {
		xpos = xp;
		ypos = yp;
		currentTime = new TuioTime(ttime);
		startTime = new TuioTime(currentTime);
	}
	
	/**
	 * Takes a M_Point argument and updates its coordinate attributes
	 * to the coordinates of the provided M_Point and leaves its time stamp unchanged.
	 *
	 * @param	tpoint	the M_Point to assign
	 */
	public void update(TuioPoint tpoint) {
		xpos = tpoint.getX();
		ypos = tpoint.getY();
	}

	/**
	 * Takes two floating point coordinate arguments and updates its coordinate attributes 
	 * to the coordinates of the provided M_Point and leaves its time stamp unchanged.
	 *
	 * @param	xp	the X coordinate to assign
	 * @param	yp	the Y coordinate to assign
	 */
	public void update(float xp, float yp) {
		xpos = xp;
		ypos = yp;
	}
	
	/**
	 * Takes a TuioTime object and two floating point coordinate arguments and updates its coordinate attributes 
	 * to the coordinates of the provided M_Point and its time stamp to the provided TUIO time object.
	 *
	 * @param	ttime	the TuioTime to assign
	 * @param	xp	the X coordinate to assign
	 * @param	yp	the Y coordinate to assign
	 */
	public void update(TuioTime ttime, float xp, float yp) {
		xpos = xp;
		ypos = yp;
		currentTime = new TuioTime(ttime);
	}
	
	/**
	 * Returns the X coordinate of this M_Point.
	 * @return	the X coordinate of this M_Point
	 */
	public float getX() {
		return xpos;
	}
	
	/**
	 * Returns the Y coordinate of this M_Point.
	 * @return	the Y coordinate of this M_Point
	 */
	public float getY() {
		return ypos;
	}
	
	/**
	 * Returns the distance to the provided coordinates 
	 *
	 * @param	xp	the X coordinate of the distant point
	 * @param	yp	the Y coordinate of the distant point
	 * @return	the distance to the provided coordinates
	 */
	public float getDistance(float xp, float yp) {
		float dx = xpos-xp;
		float dy = ypos-yp;
		return (float)Math.sqrt(dx*dx+dy*dy);
	}

	/**
	 * Returns the distance to the provided M_Point
	 *
	 * @param	tpoint	the distant M_Point
	 * @return	the distance to the provided M_Point
	 */
	public float getDistance(TuioPoint tpoint) {
		return getDistance(tpoint.getX(),tpoint.getY());
	}

	/**
	 * Returns the angle to the provided coordinates 
	 *
	 * @param	xp	the X coordinate of the distant point
	 * @param	yp	the Y coordinate of the distant point
	 * @return	the angle to the provided coordinates
	 */
	public float getAngle(float xp, float yp) {
		
		float side = xpos-xp;
		float height = ypos-yp;
		float distance = getDistance(xp,yp);
		
		float angle = (float)(Math.asin(side/distance)+Math.PI/2);
		if (height<0) angle = 2.0f*(float)Math.PI-angle;
		
		return angle;
	}
	
	/**
	 * Returns the angle to the provided M_Point
	 *
	 * @param	tpoint	the distant M_Point
	 * @return	the angle to the provided M_Point
	 */
	public float getAngle(TuioPoint tpoint) {						
		return getAngle(tpoint.getX(),tpoint.getY());
	}

	/**
	 * Returns the angle in degrees to the provided coordinates 
	 *
	 * @param	xp	the X coordinate of the distant point
	 * @param	yp	the Y coordinate of the distant point
	 * @return	the angle in degrees to the provided M_Point
	 */
	public float getAngleDegrees(float xp, float yp) {		
		return (getAngle(xp,yp)/(float)Math.PI)*180.0f;
	}
	
	/**
	 * Returns the angle in degrees to the provided M_Point
	 *
	 * @param	tpoint	the distant M_Point
	 * @return	the angle in degrees to the provided M_Point
	 */
	public float getAngleDegrees(TuioPoint tpoint) {		
		return (getAngle(tpoint)/(float)Math.PI)*180.0f;
	}
	
	/**
	 * Returns the X coordinate in pixels relative to the provided screen width. 
	 *
	 * @param	width	the screen width
	 * @return	the X coordinate of this M_Point in pixels relative to the provided screen width
	 */
	public int getScreenX(int width) {
		return (int)Math.round(xpos*width);
	}
	
	/**
	 * Returns the Y coordinate in pixels relative to the provided screen height. 
	 *
	 * @param	height	the screen height
	 * @return	the Y coordinate of this M_Point in pixels relative to the provided screen height
	 */
	public int getScreenY(int height) {
		return (int)Math.round(ypos*height);
	}
	
	/**
	 * Returns the time stamp of this M_Point as TuioTime.
	 *
	 * @return	the time stamp of this M_Point as TuioTime
	 */
	public TuioTime getTuioTime() {
		return new TuioTime(currentTime);
	}
	
	/**
	 * Returns the start time of this M_Point as TuioTime.
	 *
	 * @return	the start time of this M_Point as TuioTime
	 */
	public TuioTime getStartTime() {
		return new TuioTime(startTime);
	}
}
