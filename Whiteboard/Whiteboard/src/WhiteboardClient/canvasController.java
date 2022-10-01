package WhiteboardClient;

import java.awt.Color;
/**
 * Haoyu Bai
 * 956490
 *
 *
 * description: Canvas controller, remember the enviournment of the board
 * Shpe factory, using the board info to create shape automatically
 **/

public class canvasController {
	// record all the setting in the board and generate shape accordinaly
	 private String shape;
	 private Color color;
	 private String string;
	 private static canvasController instance;
	 
	 private canvasController() {
		 this.color = Color.black;
		 this.shape = "LINE";
		 this.string = "";
	 }
	 
	 //setter
	 public void setVariabels(Color color, String shape, String string) {
		if(color != null) {
			this.color = color;
		}
		if(string != null) {
			this.string = string;
		}else {
			this.string = "";
		}
		if (shape != null) {
			this.shape = shape;
		}
	 }
	 //shape factory using board info to creat shape
	 public Shape shapeFactory(int oldx, int newx, int oldy, int newy) {
		 if(shape == "STRING") {
			 return  new Shape(oldy, newy, this.string, this.shape, this.color );
		 }
		return new Shape(oldx, oldy,newx, newy, this.shape, this.color );
	 }
	 // singleton get method
	 synchronized public static canvasController getInstance() {
		 if (instance == null){
			 instance = new canvasController();
			 }
		 return instance;
		 }
	}