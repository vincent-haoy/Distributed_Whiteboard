package whiteboardServer;
import javax.swing.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
/**
 * Haoyu Bai
 * 956490
 *
 *
 * description: The server drwaing pannel, containing panel UI and logics of action listener
 **/
public class CavasPanel extends JComponent {
	 
	  private Image image;
	  public Graphics2D g2;
	  // knowing this whiteboard belongings
	  WhiteBoard belongsto;
	  
	  // Mouse coordinates
	  private int currentX, currentY, oldX, oldY;
	  public canvasController canvascontroller;
	  public CavasPanel(WhiteBoard w) {
		  this.belongsto = w;
		  this.canvascontroller	 = canvasController.getInstance();
		  setDoubleBuffered(false);
	   	  
		  //adding mouse listener 
		  addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mousePressed(MouseEvent e) {
		        oldX = e.getX();
		        oldY = e.getY();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
		        currentX = e.getX();
		        currentY = e.getY();
		        if (g2 != null) {
		          // draw if g2 context not null
		        	try {
						w.addShapetoList(canvascontroller.shapeFactory(oldX, oldY, currentX, currentY));
					} catch (RemoteException | NotBoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		        }
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
	    	
	    });

	  }
	 
	  protected void paintComponent(Graphics g) {
	    if (image == null) {
	      // image to draw null ==> we create
	      image = createImage(getSize().width, getSize().height);
	      g2 = (Graphics2D) image.getGraphics();
	      // enable antialiasing
	      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	      // clear draw area
	      clear();
	    }
	 
	    g.drawImage(image, 0, 0, null);
	  }
	  
	  public Image getImage() {
		  return this.image;
	  }
	 
	  // now we create exposed methods
	  public void clear() {
	    g2.setPaint(Color.white);
	    // draw white on entire draw area to clear
	    g2.fillRect(0, 0, getSize().width, getSize().height);
	    g2.setPaint(Color.black);
	    repaint();
	  }
	 public void setimage(Image i) {
		 this.image = i;
	 }
	 
	}
