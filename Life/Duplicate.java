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
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;


/**
 * 
 * @author Tania Kabiraj
 * 1/31/16 
 * Pd. 3
 * 
 * This lab took me about 2.5 hours.
 * 
 * This lab was fairly hard to complete. First calculating the amount of neighbors was extremely hard. It took
 * a while to work out all the conditions. Then getting the next generation to work correctly was hard because
 * you can't change it directly, but save the condition somewhere. The part where I was stuck was at the end
 * because some of the dead cells were still "alive" in my code so I had to reset them all for it to work.
 *
 */
public class Duplicate implements ActionListener, MouseListener, MouseMotionListener{
	static int[][] grid;
	static int[][] neighbor;
	Color[][] pic = new Color[21][21];

	static int row = 1;
	static int col = 1;
	static int maxRow;;
	static int maxCol;
	Color color = Color.WHITE;
	MyDrawingPanel drawingPanel;
	int generationCount = 0;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Duplicate cell = new Duplicate();
		//P3_Kabiraj_Tania_Life cell = new P3_Kabiraj_Tania_Life("life100.txt");
//		cell.calculateNeighborCount();
//		cell.runLife(5);
	}

	public Duplicate(){
		JFrame window = new JFrame("Generation # " + generationCount);
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
		mainPanel.add(drawingPanel);
		
		JButton clbutton = new JButton("Reset");
		clbutton.setBounds(100, 510, 75, 20);
		clbutton.addActionListener(this);
		clbutton.setActionCommand("Reset");
		mainPanel.add(clbutton);
		
		JButton rubutton = new JButton("Run");
		rubutton.setBounds(250, 510, 75, 20);
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
		
		calculateNeighborCount();
		runLife(1);
		
		window.getContentPane().add(mainPanel);
		window.setVisible(true);
		
		
	}
	private class MyDrawingPanel extends JPanel {

		// Not required, but gets rid of the serialVersionUID warning.  Google it, if desired.
		//static final long serialVersionUID = 1234567890L;
//		pic = new Color[21][21];

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
	public Duplicate(String fileName) {
		Scanner in;
		int lineNumber = 1;
		try {
			in = new Scanner(new File(fileName));
			while (in.hasNext()) {
				if (lineNumber == 1) {
					maxRow = in.nextInt();
					maxCol = in.nextInt();
					grid = new int[maxRow + 1][maxCol + 1];
					neighbor = new int[maxRow + 1][maxCol + 1];
				} else {
					row = in.nextInt();
					col = in.nextInt();
					grid[row + 1][col + 1] = 10;
				}
				lineNumber++;

			}
			printBoard();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void runLife(int numGenerations) {
		int neighborCount = 0;
		for (int num = 1; num <= numGenerations; num++) {

			for (int i = 1; i < maxCol + 1; i++) {
				for (int j = 1; j < maxRow + 1; j++) {

					neighborCount = getNeighborCount(i, j);

					if (grid[i][j] == 10 && (neighborCount < 2 || neighborCount > 3)) {

						grid[i][j] = 1;
					}
					if (grid[i][j] == 10 && (neighborCount == 2 || neighborCount == 3)) {

						grid[i][j] = 10;
					}
					if (grid[i][j] == 0 && neighborCount == 3) {

						grid[i][j] = 0;
					}
					if (grid[i][j] == 0 && neighborCount != 3) {
						grid[i][j] = 1;
					}

				}
			}

			nextGeneration();
			calculateNeighborCount();

		}
	}

	public int rowCount(int row) {
		int counterForRow = 0;
		for (int i = 1; i < maxCol + 1; i++) {
			if (grid[row + 1][i] == 10) {
				counterForRow++;
			}
		}
		return counterForRow;
	}

	public int colCount(int col) {
		int counterForCol = 0;
		for (int i = 1; i < maxCol + 1; i++) {
			if (grid[i][col + 1] == 10) {
				counterForCol++;
			}
		}
		return counterForCol;
	}

	public int totalCount() {
		int counterForTotal = 0;
		for (int i = 1; i < maxCol + 1; i++) {
			for (int j = 1; j < maxRow + 1; j++) {
				if (grid[i][j] == 10) {
					counterForTotal++;
				}
			}
		}
		return counterForTotal;
	}

	public void printBoard() {
		for (int i = 0; i < maxRow; i++) {
			for (int j = 0; j < maxCol; j++) {
				if (i == 0 && j == 0) {
					System.out.println("   01234567890123456789");
					System.out.print(" 0 ");

				}
				if (i != 0 && j == 0) {
					if (i < 10) {
						System.out.print(" " + i + " ");
					} else {
						System.out.print(i + " ");
					}

				}

				if (grid[i + 1][j + 1] == 10) {
					System.out.print("*");
				} else {
					System.out.print(" ");
				}
				if (j == maxCol + 1) {
					System.out.println();
				}

			}
			System.out.println();
		}
		System.out.println();
		System.out.println("Number of living cells in row 9 ->" + rowCount(9));
		System.out.println("Number of living cells in col 9 ->" + colCount(9));
		System.out.println("Number of living cells total ->" + totalCount());

	}

	public void nextGeneration() {
		for (int i = 1; i < maxCol + 1; i++) {
			for (int j = 1; j < maxRow + 1; j++) {

				if (grid[i][j] == 0) {
					grid[i][j] = 10;
				}

				if (grid[i][j] == 1) {
					grid[i][j] = 0;
				}

			}
		}
		printBoard();

	}

	public void calculateNeighborCount() {
		int counterForNeighbor = 0;
		for (int i = 1; i < maxRow + 1; i++) {

			for (int j = 1; j < maxCol + 1; j++) {

				neighbor[i][j] = counterForNeighbor;
			}
		}

		for (int i = 1; i < maxRow + 1; i++) {

			for (int j = 1; j < maxCol + 1; j++) {

				counterForNeighbor = 0;
				if (grid[i - 1][j] == 10) {
					counterForNeighbor++;
					neighbor[i][j] = counterForNeighbor;
				}

				if (i < maxRow) {
					if (grid[i + 1][j] == 10) {
						counterForNeighbor++;
						neighbor[i][j] = counterForNeighbor;
					}
				}

				if (j < maxCol) {
					if (grid[i][j + 1] == 10) {
						counterForNeighbor++;
						neighbor[i][j] = counterForNeighbor;
					}
				}

				if (grid[i][j - 1] == 10) {
					counterForNeighbor++;
					neighbor[i][j] = counterForNeighbor;
				}
				if (grid[i - 1][j - 1] == 10) {
					counterForNeighbor++;
					neighbor[i][j] = counterForNeighbor;
				}
				if (j < maxCol) {
					if (grid[i - 1][j + 1] == 10) {
						counterForNeighbor++;
						neighbor[i][j] = counterForNeighbor;
					}
				}
				if (i < maxRow) {
					if (grid[i + 1][j - 1] == 10) {
						counterForNeighbor++;
						neighbor[i][j] = counterForNeighbor;
					}
				}

				if (i <maxRow && j < maxCol) {
					if (grid[i + 1][j + 1] == 10) {
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
		file+= col + "\n" + row+ "\n";
		file+= "255\n";
		for(int i = 0; i < col; i++){
			for(int j = 0; j < row; j++){
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

			if (e.getActionCommand().equals("Reset")) {
				clearDraw();
			}
			if(e.getActionCommand().equals("Run")){
				
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
			}
			if(e.getActionCommand().equals("Clear")){
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

}