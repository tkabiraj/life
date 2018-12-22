import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * 
 * @author Tania Kabiraj
 * 2/29/16 
 * Pd. 3
 * 
 * This lab took me about 2.5 hours.
 * 
 * This lab was medium for me. It took me a while to get the logic down on how to solve this problem. But when
 * I got the logic, coding was very simple. My code from the previous time was very good so there were very
 * little things to fix. It took me a long time to figure out that the dimensions I used were different than
 * the ones I used in simple draw so it took some time to fix all the problems regarding that.
 *
 */
public class P3_Kabiraj_Tania_LifeGUI implements ActionListener, ChangeListener, MouseListener, MouseMotionListener{
	static int[][] grid;
	static int[][] neighbor = new int[21][21];
	Color[][] pic = new Color[21][21];

	static int row = 1;
	static int col = 1;
	static int maxRow = 21;
	static int maxCol = 21;
	Color color = Color.WHITE;
	MyDrawingPanel drawingPanel;
	int generationCount = 0;
	JFrame window;
	JSlider timeslid;
	int delay = 1000;
	javax.swing.Timer timer = new javax.swing.Timer(delay,this);

	public static void main(String[] args) {
		P3_Kabiraj_Tania_LifeGUI cell = new P3_Kabiraj_Tania_LifeGUI();
	}

	public P3_Kabiraj_Tania_LifeGUI(){
		window = new JFrame("Generation # " + generationCount);
		window.setBounds(100, 100, 445, 600);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		drawingPanel = new MyDrawingPanel();
		drawingPanel.setBounds(20, 20, 400,400);
		drawingPanel.setBorder(BorderFactory.createEtchedBorder());
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				drawingPanel.setPixel(i, j);
			}
		}
		color = Color.RED;
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(null);
		mainPanel.addMouseListener(this);
		mainPanel.addMouseMotionListener(this);
		
		timeslid = new JSlider(0, 1000, delay);
		timeslid.setMajorTickSpacing(100);
		timeslid.setBounds(20, 440, 400, 50);
		timeslid.setPaintLabels(true);
		timeslid.setPaintTicks(true);
		timeslid.addChangeListener(this);		
		mainPanel.add(timeslid);
		
		JButton cobutton = new JButton("Continous");
		cobutton.setBounds(130, 510, 95, 20);
		cobutton.addActionListener(this);
		mainPanel.add(cobutton);
		
		JButton stop = new JButton("Stop");
		stop.setBounds(240, 510, 75, 20);
		stop.addActionListener(this);
		mainPanel.add(stop);
		
		JButton clbutton = new JButton("Reset");
		clbutton.setBounds(20, 510, 75, 20);
		clbutton.addActionListener(this);
		clbutton.setActionCommand("Reset");
		mainPanel.add(clbutton);
		
		JButton rubutton = new JButton("Run");
		rubutton.setBounds(345, 510, 75, 20);
		rubutton.addActionListener(this);
		mainPanel.add(rubutton);
		
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		JMenu editMenu = new JMenu("Edit");
		
		JMenuItem openItem = new JMenuItem("Open", 'o');
		openItem.addActionListener(this);
		JMenuItem saveItem = new JMenuItem("Save", 's');
		saveItem.addActionListener(this);
		JMenuItem clearItem = new JMenuItem("Clear", 'a');
		clearItem.addActionListener(this);
		
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		editMenu.add(clearItem);
		
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		
		window.setJMenuBar(menuBar);

		mainPanel.add(drawingPanel);
		
		window.getContentPane().add(mainPanel);
		window.setVisible(true);
		
		
	}
	private class MyDrawingPanel extends JPanel {

		public String getPixel(int x, int y){
			return "" + pic[y][x].getRed() + " " +  pic[y][x].getGreen() + " "+ pic[y][x].getBlue() + " ";
		}
		
		public void setPixel(int x, int y){
			pic[y][x] = color;
		}
		public void paintComponent(Graphics g) {
			for(int i = 0; i < 20; i++){
				for(int j = 0; j < 20; j++){
					g.setColor(pic[i][j]);
					g.fillRect(20*j, 20*i, 20, 20);
					g.setColor(Color.lightGray);
					g.drawRect(20*j, 20*i, 20, 20);
				}
			}
		}
	}
	
	public void runLife(int numGenerations) {
		int neighborCount = 0;
		for (int num = 1; num <= numGenerations; num++) {

			for (int i = 0; i < maxCol; i++) {
				for (int j = 0; j < maxRow; j++) {

					neighborCount = getNeighborCount(i, j);

					if (pic[i][j] == color && (neighborCount < 2 || neighborCount > 3)) {

						pic[i][j] = Color.WHITE;
					}
					if (pic[i][j] == color && (neighborCount == 2 || neighborCount == 3)) {

						pic[i][j] = color;
					}
					if (pic[i][j] == Color.WHITE && neighborCount == 3) {

						pic[i][j] = color;
					}
					if (pic[i][j] == Color.WHITE && neighborCount != 3) {
						pic[i][j] = Color.WHITE;
					}

				}
			}

			nextGeneration();
			calculateNeighborCount();

		}
	}

	public int rowCount(int row) {
		int counterForRow = 0;
		for (int i = 0; i < maxCol; i++) {
			if (pic[row][i] == color) {
				counterForRow++;
			}
		}
		return counterForRow;
	}

	public int colCount(int col) {
		int counterForCol = 0;
		for (int i = 0; i < maxCol; i++) {
			if (pic[i][col] == color) {
				counterForCol++;
			}
		}
		return counterForCol;
	}

	public int totalCount() {
		int counterForTotal = 0;
		for (int i = 0; i < maxCol; i++) {
			for (int j = 0; j < maxRow; j++) {
				if (pic[i][j] == color) {
					counterForTotal++;
				}
			}
		}
		return counterForTotal;
	}

	public void nextGeneration() {
		for (int i = 0; i < maxCol; i++) {
			for (int j = 0; j < maxRow; j++) {

				if (pic[i][j] == color) {
					pic[i][j] = color;
				}

				if (pic[i][j] == Color.WHITE) {
					pic[i][j] = Color.WHITE;
				}

			}
		}
		Graphics g = drawingPanel.getGraphics();
		drawingPanel.paintComponent(g);

	}

	public void calculateNeighborCount() {
		int counterForNeighbor = 0;
		for (int i = 0; i < maxRow; i++) {

			for (int j = 0; j < maxCol; j++) {

				neighbor[i][j] = counterForNeighbor;
			}
		}

		for (int i = 0; i < maxRow; i++) {

			for (int j = 0; j < maxCol; j++) {

				counterForNeighbor = 0;
				if (i > 0 && pic[i - 1][j] == color) {
					counterForNeighbor++;
					neighbor[i][j] = counterForNeighbor;
				}

				if (i < maxRow-1) {
					if (pic[i + 1][j] == color) {
						counterForNeighbor++;
						neighbor[i][j] = counterForNeighbor;
					}
				}

				if (j < maxCol-1) {
					if (pic[i][j + 1] == color) {
						counterForNeighbor++;
						neighbor[i][j] = counterForNeighbor;
					}
				}

				if (j > 0 &&pic[i][j - 1] == color) {
					counterForNeighbor++;
					neighbor[i][j] = counterForNeighbor;
				}
				if (i > 0 && j > 0 && pic[i - 1][j - 1] == color) {
					counterForNeighbor++;
					neighbor[i][j] = counterForNeighbor;
				}
				if (i > 0 && j < maxCol-1) {
					if (pic[i - 1][j + 1] == color) {
						counterForNeighbor++;
						neighbor[i][j] = counterForNeighbor;
					}
				}
				if (j > 0 && i < maxRow-1) {
					if (pic[i + 1][j - 1] == color) {
						counterForNeighbor++;
						neighbor[i][j] = counterForNeighbor;
					}
				}

				if (i <maxRow-1 && j < maxCol-1) {
					if (pic[i + 1][j + 1] == color) {
						counterForNeighbor++;
						neighbor[i][j] = counterForNeighbor;
					}
				}

			}
		}

	}

	public int getNeighborCount(int row, int col) {
		return neighbor[row][col];
	}
	public void clearDraw() {
		Color temp = color;
		color = Color.WHITE;
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				drawingPanel.setPixel(i, j);
			}
		}
		color = temp;
		drawingPanel.repaint();
		drawingPanel.paintComponent(drawingPanel.getGraphics());
	}
	
	public String writeFile(){
		String file = "P3\n";
		file+= (maxCol-1) + "\n" + (maxRow-1)+ "\n";
		file+= "255\n";
		for(int i = 0; i < maxCol-1; i++){
			for(int j = 0; j < maxRow-1; j++){
				file += drawingPanel.getPixel(j, i);
			}
			file+="\n";
		}
		return file;
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(e.getModifiersEx() == MouseEvent.BUTTON1_DOWN_MASK){
			if(e.getX() >=20 && e.getY() >= 20 && e.getX() <= 420 && e.getY() <= 420){
				int xpos = ((e.getX()-20) - (e.getX()%20))/20;
				int ypos = ((e.getY() -20)- (e.getY()%20))/20;
				Graphics g = drawingPanel.getGraphics();
				drawingPanel.setPixel(xpos, ypos);
				drawingPanel.paintComponent(g);
			}
		}
		if(e.getModifiersEx() == MouseEvent.BUTTON3_DOWN_MASK){
			if(e.getX() >= 20 && e.getY() >= 20 && e.getX() <= 420 && e.getY() <= 420){
				Color temp = color;
				int xpos = ((e.getX()-20) - (e.getX()%20))/20;
				int ypos = ((e.getY() -20)- (e.getY()%20))/20;
				color = Color.WHITE;
				Graphics g = drawingPanel.getGraphics();
				drawingPanel.setPixel(xpos, ypos);
				drawingPanel.paintComponent(g);
				color = temp;
			}
		}
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1){
			if(e.getX() >= 20 && e.getY() >= 20 && e.getX() <= 420 && e.getY() <=420){
				int xpos = ((e.getX()-20) - (e.getX()%20))/20;
				int ypos = ((e.getY() -20)- (e.getY()%20))/20;
				Graphics g = drawingPanel.getGraphics();
				drawingPanel.setPixel(xpos, ypos);
				drawingPanel.paintComponent(g);
			}
		}
		if(e.getButton() == MouseEvent.BUTTON3){
			if(e.getX() >= 20 && e.getY() >= 20 && e.getX() <= 420 && e.getY() <= 420){
				Color temp = color;
				int xpos = ((e.getX()-20) - (e.getX()%20))/20;
				int ypos = ((e.getY() -20)- (e.getY()%20))/20;
				Graphics g = drawingPanel.getGraphics();
				color = Color.WHITE;
				drawingPanel.setPixel(xpos, ypos);
				drawingPanel.paintComponent(g);
				color = temp;
			}
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}

	@Override
	public void actionPerformed(ActionEvent e) {

		System.out.println("Action -> " + e.getActionCommand());

		if (e.getActionCommand() != null) {
			
			if (e.getActionCommand().equals("Red"))
				color = Color.RED;
			else if (e.getActionCommand().equals("Green"))
				color = Color.GREEN;
			else if (e.getActionCommand().equals("Blue"))
				color = Color.BLUE;

			if(e.getActionCommand().equals("Continous")){
				if(!timer.isRunning()){
					timer.setInitialDelay(delay);
					timer.setDelay(delay);
					timer.start();
					timer.setActionCommand("Running");
				}
			}
			if(e.getActionCommand().equals("Running")){
				generationCount++;
				calculateNeighborCount();
				runLife(1);
				window.setTitle("Generation # " + generationCount);
			}
			if(e.getActionCommand().equals("Stop")){
				if(timer.isRunning()){
					timer.stop();
				}
			}
			if (e.getActionCommand().equals("Reset")) {
				generationCount = 0;
				window.setTitle("Generation # " + generationCount);
				if(timer.isRunning()){
					timer.stop();
				}
				clearDraw();
			}
			if(e.getActionCommand().equals("Run")){
				generationCount++;
				calculateNeighborCount();
				runLife(1);
				window.setTitle("Generation # " + generationCount);
			}
			if(e.getActionCommand().equals("Open")){
				JFileChooser chooser = new JFileChooser(".");
				chooser.showOpenDialog(null);
				File fileChosen = chooser.getSelectedFile();
				Scanner in;
				String line = "";
				String[] vals;
				try{
					in = new Scanner(fileChosen);
					try{
						if(in.nextLine().equals("P3")){
							while(in.hasNext()){
								line = in.nextLine(); 
								if(line.charAt(0) != '#'){
									col = Integer.parseInt(line);
									line = in.nextLine();
									row = Integer.parseInt(line);
									
									line = in.nextLine();
									line = in.nextLine();
									int count = 0;
									vals = line.split("\\s+");
									count = 0;
									for (int i = 0; i < col; i++) {
										for (int j = 0; j < row; j++) {
											color = new Color(Integer.parseInt(vals[count]), Integer.parseInt(vals[++count]),Integer.parseInt(vals[++count]));
											count++;
											drawingPanel.setPixel(j, i);
											if(count == vals.length){
												line = in.nextLine();
												vals = line.split("\\s+");
												count= 0;
											}
										}
									}
								}
							}
						}
						}catch(java.util.NoSuchElementException g){
												
					}
				}catch (FileNotFoundException f) {
					f.printStackTrace();
				}
				color = Color.RED;
			}
			if(e.getActionCommand().equals("Clear")){
				generationCount = 0;
				window.setTitle("Generation # " + generationCount);
				if(timer.isRunning()){
					timer.stop();
				}
				clearDraw();
			}
			if(e.getActionCommand().equals("Save")){
				JFileChooser chooser = new JFileChooser(".");
				if(chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
					File fileChosen = chooser.getSelectedFile();
					FileWriter file;
					try {
						file = new FileWriter(fileChosen);
						file.write(writeFile());
						file.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
				}
				
			}
		}

	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == timeslid){
			delay = timeslid.getValue();
			timer.setInitialDelay(delay);
			timer.setDelay(delay);
		}
	}

}
