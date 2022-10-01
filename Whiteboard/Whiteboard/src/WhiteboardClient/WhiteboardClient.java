package WhiteboardClient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import remote.IRemoteClient;
import remote.IRemoteDrawing;
import WhiteboardClient.canvasController;
import WhiteboardClient.Shape;
/**
 * Haoyu Bai
 * 956490
 *
 *
 * description: creating the Client white board UI and some locigcs
 **/

public class WhiteboardClient extends UnicastRemoteObject implements IRemoteClient{
	  ArrayList<String> Userslist;
	  private JTextField drawtextinput;
	  private JTextField ChatInput;
	  JTextArea chatroom;
	  ClientDrawPanel drawArea;
	  JFrame frame;
	  public boolean isjoined;
	  ArrayList<Shape> shapelist = new ArrayList<>();
	  String hostname = "whiteboard";
	  String RMIADDRESS;
	  String USERNAME;
	  private JTable table;
	  DefaultTableModel model;
	  
	  public WhiteboardClient(String Username, String rmiaddress) throws RemoteException {
		  RMIADDRESS = rmiaddress;
		  USERNAME = Username;
		  isjoined = false;
		  drawArea = new ClientDrawPanel(this);
		  // add to content pane
		  drawArea.setBounds(135, 84, 542, 423);
		  canvasController canvascontroller = drawArea.canvascontroller;
		  // add to content pane
	    
	  	
		  frame = new JFrame(USERNAME + "  drawing board");
		  frame.getContentPane().setFont(new Font("SimSun", Font.PLAIN, 18));
		  Container content = frame.getContentPane();
		  frame.getContentPane().setLayout(null);
		  
		  // create draw area
		  content.add(drawArea);
		  
		  //shape and color stuff
		  JButton CircleBtn = new JButton("Circle");
		  CircleBtn.setBounds(10, 133, 115, 39);
		  CircleBtn.setFont(new Font("SimSun", Font.PLAIN, 18));
		  CircleBtn.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		canvascontroller.setVariabels(null, "OVAL", null);
	    		}});
		  frame.getContentPane().add(CircleBtn);
		  JButton LineBttn = new JButton("Line");
		  LineBttn.setBounds(10, 84, 115, 39);
		  LineBttn.setFont(new Font("SimSun", Font.PLAIN, 18));
		  LineBttn.addActionListener(new ActionListener() {
			  public void actionPerformed(ActionEvent e) {
	    		canvascontroller.setVariabels(null, "LINE", null);}});
		  frame.getContentPane().add(LineBttn);
		  
		  JButton TriangleBtn = new JButton("Triangle");
		  TriangleBtn.setBounds(10, 182, 115, 39);
		  TriangleBtn.setFont(new Font("SimSun", Font.PLAIN, 15));
		  TriangleBtn.addActionListener(new ActionListener() {
			  public void actionPerformed(ActionEvent e) {
	    		canvascontroller.setVariabels(null, "TRI", null);}});
		  frame.getContentPane().add(TriangleBtn);
	    
		  JButton RecBtn = new JButton("Rectangle");
		  RecBtn.setBounds(10, 231, 115, 39);
		  RecBtn.setFont(new Font("SimSun", Font.PLAIN, 15));
		  RecBtn.addActionListener(new ActionListener() {
			  public void actionPerformed(ActionEvent e) {
	    		canvascontroller.setVariabels(null, "RECT", null);}});
		  frame.getContentPane().add(RecBtn);
	    
		  JButton TextInBtn = new JButton("Text");
		  TextInBtn.setBounds(10, 362, 115, 39);
		  TextInBtn.setFont(new Font("SimSun", Font.PLAIN, 18));
		  TextInBtn.addActionListener(new ActionListener() {
			  public void actionPerformed(ActionEvent e) {
	    		String inputtextfieldvalue = drawtextinput.getText();
	    		canvascontroller.setVariabels(null, "STRING",inputtextfieldvalue);
	    	}});

		  frame.getContentPane().add(TextInBtn);
	    
	    JButton ColorBtn = new JButton("Color");
	    	ColorBtn.setBounds(10, 411, 115, 39);
	    ColorBtn.setFont(new Font("SimSun", Font.PLAIN, 18));
	    ColorBtn.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		Color color = JColorChooser.showDialog(null,"Pick a color", new Color(1));
	    		canvascontroller.setVariabels(color, null, null);
	    	}
	    });
	    frame.getContentPane().add(ColorBtn);
	    
	    //chart room here 
	    drawtextinput = new JTextField();
	    drawtextinput.setBounds(10, 280, 115, 57);
	    frame.getContentPane().add(drawtextinput);
	    drawtextinput.setColumns(10);
	    
	    ChatInput = new JTextField();
	    ChatInput.setBounds(687, 429, 262, 39);
	    frame.getContentPane().add(ChatInput);
	    ChatInput.setColumns(10);
	    // chat room stuff
	    JScrollPane scrollPane = new JScrollPane();
	    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    scrollPane.setBounds(687, 85, 262, 334);
	    frame.getContentPane().add(scrollPane);
	    
	    chatroom = new JTextArea();
	    scrollPane.setViewportView(chatroom);
	    frame.setSize(973, 668);
	    
	    //connected users
	    //connected user pannel
	    JScrollPane scrollPane_1 = new JScrollPane();
	    scrollPane_1.setBounds(135, 517, 542, 94);
	    frame.getContentPane().add(scrollPane_1);
	     
	    //connected users table
	    model = new DefaultTableModel();
	    model.addColumn("Connected Users");
	    table = new JTable(model);
	    table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);		 
	    table.getColumnModel().getColumn(0).setPreferredWidth(243);
	    model.addRow(new String[]{USERNAME});
	    scrollPane_1.setViewportView(table);
	    
	    //tell the server that client is leaving beforeclose frame
	    frame.addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
				try {
					IRemoteDrawing service;
					try {
						service = (IRemoteDrawing) Naming.lookup(RMIADDRESS + "whiteboard");
						service.leave(USERNAME);
					} catch (MalformedURLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				} catch (RemoteException | NotBoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	            
	        }
	    });
	    
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setVisible(true);
	    // sending btn 
	    JButton SendBtn = new JButton("Send");
	    SendBtn.setBounds(769, 468, 115, 39);
	    SendBtn.setFont(new Font("SimSun", Font.PLAIN, 18));
	    SendBtn.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		String inputtextfieldvalue = ChatInput.getText();
	    		try {
					askForBoadcastingAstring(inputtextfieldvalue);
				} catch (RemoteException | NotBoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    		
	    	}
	    });
	    frame.getContentPane().add(SendBtn);
		
	}
	  
	public void paintlist() {
		for(Shape s: shapelist) {
			s.addtoboard(drawArea.g2);
		}
		drawArea.repaint();
		
	}
	
	public void RequestforGobelupdate(Shape s) throws RemoteException, NotBoundException {
		if (isjoined) {
			IRemoteDrawing service;
			try {
				service = (IRemoteDrawing) Naming.lookup(RMIADDRESS + "whiteboard");
				service.boardcastingtoallboard(s);
			} catch (MalformedURLException | RemoteException | NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	public void addShapetoList(Shape temp) {
		//adding drawn shape to a list, just for maintaince properse
		temp.addtoboard(drawArea.g2);
		drawArea.repaint();
	}
	
	public void askForBoadcastingAstring(String string) throws AccessException, RemoteException, NotBoundException {
		if (isjoined) {
			IRemoteDrawing service;
			try {
				service = (IRemoteDrawing) Naming.lookup(RMIADDRESS + "whiteboard");
				service.updateAllChartRoom(USERNAME + ": " + string);
			} catch (MalformedURLException | RemoteException | NotBoundException e) {
				System.out.println("cann not ask the server to boadcasting a chat");
				e.printStackTrace();
			}
			
		}

	}
	
	public void addtoboard(Shape s)throws RemoteException {
		s.addtoboard(drawArea.g2);
		drawArea.repaint();
	}
	
	@Override
	public void addtoboard(whiteboardServer.Shape s) throws RemoteException {
		s.addtoboard(drawArea.g2);
		drawArea.repaint();
	}
	// this is the page to notify user when they are kicked out
	public void exitmessage(String s) throws RemoteException{
		System.gc();
		System.runFinalization();
		/*  ooooooooooops channel    */
		frame.dispose();
		JFrame frame = new JFrame("Swing Paint");
		JTextField txtOppYouAre = new JTextField();
		frame.setSize(973, 668);
		txtOppYouAre.setFont(new Font("SimSun", Font.PLAIN, 30));
		txtOppYouAre.setText("       "+s);
		txtOppYouAre.setEditable(false);
		frame.getContentPane().add(txtOppYouAre, BorderLayout.CENTER);
		txtOppYouAre.setColumns(10);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		/*        ooooops chanel               */
	}
	
	// reset the whiteboard
	public void clear() {
		  drawArea.g2.setPaint(Color.white);
		    // draw white on entire draw area to clear
		  drawArea.g2.fillRect(0, 0, drawArea.getSize().width, drawArea.getSize().height);
		  drawArea.g2.setPaint(Color.black);
		  drawArea.repaint();
	}

	public void addstringtochatRoom(String s) {
		chatroom.append(s + "\n");
	}

	@Override
	public void setisjoined()throws RemoteException {
		this.isjoined = true;	
	}
	
	@Override
	public void setimage(byte[] bytes) throws NotBoundException {
		try {
			drawArea.setImage(bytes);
		} catch (IOException e) {
			System.out.println("Cannot set the image as given, probily bad input stream");
			e.printStackTrace();
		}
		
	}

	@Override
	public void updateConnectedUsersByList(ArrayList<String> allusers) throws RemoteException, NotBoundException {
		// TODO Auto-generated method stub
		model.setRowCount(0);
		for (String s : allusers)
		{
			model.addRow(new String[]{s});
		}
	}
}