import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.border.Border;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class GUI_Maze extends JFrame implements ActionListener{
	Maze maze;
	//store nodes from maze in GUIMaze for searching
	Node mazeWeb[][];

	//variables to store values from maze	
	private int width;
	private int height;
		
	//variables to store GUI elements
	Container pnlMain;
	JPanel mazeGUI = new JPanel();
	JPanel menu = new JPanel();
		
	JButton solve = new JButton("Solve");		
	JButton make = new JButton("Make Maze");
	JButton clear = new JButton("Clear");
	JButton save = new JButton("Save");
	JButton load = new JButton("Load");	

	public GUI_Maze(){
		maze = new Maze(100, 100);
		mazeWeb = maze.getMazeWeb();
		this.height = maze.getHeight();
		this.width = maze.getWidth();		


		this.setUpGUI();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setSize(640,480);
	}//end GUI_Maze


	public void setUpGUI(){
		pnlMain = this.getContentPane();

		//set up JButtons for maze nodes
		initializeMazePanel();

		//add menu buttons adn action listeners
		menu.add(clear);
		menu.add(solve);
		menu.add(make);
		menu.add(save);
		menu.add(load);		
		
		clear.addActionListener(this);
		solve.addActionListener(this);
		make.addActionListener(this);		
		save.addActionListener(this);
		load.addActionListener(this);

	
		
		//add panels to main
		pnlMain.add(mazeGUI, BorderLayout.CENTER);
		pnlMain.add(menu, BorderLayout.SOUTH);
	}//end setUpGUI

	private void initializeMazePanel(){
		mazeGUI.removeAll();
	
		//set up maze GUI
		mazeGUI.setLayout(new GridLayout(height, width,1,1));

		//go through maze web
		for(int h = 0; h < this.height; h ++){
			for(int w = 0; w < this.width; w ++){
				this.mazeGUI.add(mazeWeb[w][h]);//add node to web
				mazeWeb[w][h].addActionListener(this);//add action listener
			}//end for w
		}//end for h

	}//end initialize maze


	public void actionPerformed(ActionEvent e){
		
		//check for input from maze nodes
		for(int h = 0; h < this.height; h ++){
			for(int w = 0; w < this.width; w ++){
				if(e.getSource() == mazeWeb[w][h]){
					mazeWeb[w][h].setWall(!mazeWeb[w][h].getWall());
				}//end if get source of the buttons
			}//end for w
		}//end for h
		
		//remove path boxed if they are not walls
		if(e.getSource() == solve){
			
			int counter = 0;
			maze.setFinish(mazeWeb[this.width-1][this.height-1]);		
			for(int i = 0; i < this.height; i ++){
				for(int j = 0; j < this.width; j ++){
					if(mazeWeb[j][i].getWall() == false){
						mazeWeb[j][i].setBackground(Color.WHITE);
					}//end change if not wall
				}//enf for j
			}//end for i
		
			maze.setSolved(false);				
			maze.startSolve();
				

		}else if(e.getSource() == make){
			maze.clear_maze();			

			maze.make_maze();
		}else if(e.getSource() == clear){
			maze.clear_maze();
		}else if(e.getSource() == save){
			String path = JOptionPane.showInputDialog("What would you like to save this maze as?");
			//serialize maze and save it to saved mazes folder	
			try{	
				FileOutputStream fOS = new FileOutputStream("Saved_Mazes/" + path + ".dat");
				ObjectOutputStream oOS = new ObjectOutputStream(fOS);
				oOS.writeObject(maze);
			}catch (Exception error){
				System.out.println(error.getMessage());
			}
			
		}else if(e.getSource() == load){
			//open JFileChooser and get seelcted file as new file
			try {
				JFileChooser j = new JFileChooser(System.getProperty("user.dir"));

				File workingDirectory = new File(System.getProperty("user.dir"));
				j.setCurrentDirectory(workingDirectory);
				j.showOpenDialog(null);
				File file = j.getSelectedFile();
							 
				FileInputStream fInS = new FileInputStream(file); 
				ObjectInputStream obInS = new ObjectInputStream(fInS); 
				
				//set loaded maze to be this maze... reset mazeWeb map, ehight and width
				this.maze = (Maze)obInS.readObject();
				this.mazeWeb = maze.getMazeWeb();
				this.height = maze.getHeight();
				this.width = maze.getWidth();
				

			
				
				pnlMain.removeAll();			
				
				//add correct nodes to panel	
				initializeMazePanel();

				pnlMain.add(mazeGUI, BorderLayout.CENTER);
				
				//to reload fram
//				mazeGUI.revalidate();
//				mazeGUI.repaint();
//				mazeGUI.setVisible(true);
		
				pnlMain.add(menu, BorderLayout.SOUTH);
	
				pnlMain.revalidate();
				pnlMain.repaint();
				setVisible(true);
			} catch (Exception error){ 
				System.out.println(error.getMessage()); 
			} // end try 

			
		}
	}//end ActionEvent

	public static void main(String[] args){
		GUI_Maze GM = new GUI_Maze();
	}//end main method

}//end GUI_Maze class def
