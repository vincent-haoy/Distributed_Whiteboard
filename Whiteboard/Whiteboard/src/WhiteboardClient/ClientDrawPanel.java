package WhiteboardClient;
import javax.imageio.ImageIO;
import javax.swing.*;
import WhiteboardClient.Shape;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
/**
 * Haoyu Bai
 * 956490
 *
 *
 * description: The client drwaing pannel, contining panel UI and logics of action listener
 **/
public class ClientDrawPanel extends JComponent {
	 
	  // Image in which we're going to draw
	  private Image image;
	  // Graphics2D object ==> used to draw on
	  public Graphics2D g2;
	  // Mouse coordinates
	  WhiteboardClient belongsto;
	  private int currentX, currentY, oldX, oldY;
	  public canvasController canvascontroller;
	  public ClientDrawPanel(WhiteboardClient w) {
		  this.belongsto = w;
		  this.canvascontroller = canvasController.getInstance();
	    setDoubleBuffered(false);
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
		          // draw line if g2 context not null
		        	Shape temp = canvascontroller.shapeFactory(oldX, oldY, currentX, currentY);
		        	try {
						w.RequestforGobelupdate(temp);
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
	      image = createImage(getSize().width, getSize().height);
	      g2 = (Graphics2D) image.getGraphics();
	      // enable antialiasing
	      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	      clear();
	    }
	 
	    g.drawImage(image, 0, 0, null);
	  }
	  
	  //setting image from out sources
	  public void setImage(byte[] bytes) throws IOException {
		  clear();
		  ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		  BufferedImage bfimage = ImageIO.read(in);
		  image = (Image)bfimage;
		  g2 = (Graphics2D) image.getGraphics();
		  g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		  repaint();
	  }
	  
	  // painting the entire panel to white
	  public void clear() {
	    g2.setPaint(Color.white);
	    g2.fillRect(0, 0, getSize().width, getSize().height);
	    g2.setPaint(Color.black);
	    repaint();
	  }

}
