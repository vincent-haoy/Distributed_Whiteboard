package whiteboardServer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.io.Serializable;
/**
 * Haoyu Bai
 * 956490
 *
 *
 * description: Canvas controller, remember the enviournment of the board
 * Shpe factory, using the board info to create shape automatically
 **/

public class Shape implements Serializable{
	private String shape;
	private Color color;
	private int oldx, oldy, newx,newy;
	private Stroke stroke;
	private String front;
	private String inputxt;
	private String fill;
	private String content;
	
	//designed for object 
	public Shape(int oldx, int newx, int oldy, int newy, String shape, Color color ) {
		this.color = color;
		this.shape = shape;
		this.newx = newx;
		this.newy = newy;
		this.oldx = oldx;
		this.oldy = oldy;
	}
	
	//text object
	public Shape(int newx,int newy,String content, String shape, Color color ) {
		this.newx = newx;
		this.newy = newy;
		this.content = content;
		this.shape = shape;
		this.color = color;
		
	
	}
	
	//Draw the shape on the baord
	public void addtoboard(Graphics2D g2) {
		 g2.setPaint(this.color);
		 switch(this.shape) {
		 case "RECT":
			 g2.drawRect(Math.min(oldx, newx), Math.min(oldy, newy), Math.abs(oldx - newx), Math.abs(oldy - newy));
			 break;
		 case "LINE":
			 g2.drawLine(oldx, oldy, newx, newy);
			 break;
		 case "OVAL":
			 g2.drawOval(Math.min(oldx,newx), Math.min(oldy,newy), Math.abs(newx - oldx), Math.abs(newy - oldy));
			 break;
		 case "TRI":
			 Double alpha = (double) ((newy - oldy)/(oldx - newx+1));
			 Double length = Math.sqrt(( (oldy - newy)*  (oldy - newy)) +((oldx - newx)*(oldx - newx))); 
			 int x3= (int) (newx + length*Math.cos(Math.toRadians(alpha+60)));
			 int y3 = (int) (oldy + length*Math.sin(Math.toRadians(alpha+60)));
			 int xpoints[] = {oldx,newx,x3};
			 int ypoints[] = {oldy,newy,y3};
			 g2.drawPolygon(xpoints, ypoints, 3);
			 break;
		 case "STRING":
			 g2.drawString(content, newx, newy);
		 }
	}
}
