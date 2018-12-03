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

	Node mazeWeb[][];

	List<Node> visited;
	
	int width;
	int height;
		
		//variables to store data
	JPanel mazeGUI = new JPanel();
	JPanel menu = new JPanel();
		
	JButton solve = new JButton("Solve");		
	JButton make = new JButton("Make Maze");
	JButton clear = new JButton("Clear");
	JButton save = new JButton("Save");
	JButton load = new JButton("Load");	

	public GUI_Maze(){
		maze = new Maze(16, 16);
		mazeWeb = maze.getMazeWeb();
		this.height = maze.getHeight();
		this.width = maze.getWidth();		


		this.setUpGUI();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setSize(640,480);
	}//end GUI_Maze


	public void setUpGUI(){
		Container pnlMain = this.getContentPane();
		mazeGUI.setLayout(new GridLayout(width, height,1,1));


		for(int h = 0; h < this.height; h ++){
			for(int w = 0; w < this.width; w ++){
				mazeGUI.add(mazeWeb[w][h]);//add node to web
				mazeWeb[w][h].addActionListener(this);//add action listener
			}//end for w
		}//end for h

		menu.add(clear);
		menu.add(solve);
		menu.add(make);
		menu.add(save);
		menu.add(load);		
		
		clear.addActionListener(this);
		solve.addActionListener(this);
		make.addActionListener(this);		
		save.addActionListener(this);

		pnlMain.add(mazeGUI, BorderLayout.CENTER);
		pnlMain.add(menu, BorderLayout.SOUTH);
	}//end setUpGUI


	public void actionPerformed(ActionEvent e){
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
			for(int i = 0; i < this.width; i ++){
				for(int j = 0; j < this.height; j ++){
					if(mazeWeb[i][j].getWall() == false){
						mazeWeb[i][j].setBackground(Color.WHITE);
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
			
			try{	
				FileOutputStream fOS = new FileOutputStream("Saved_Mazes/" + path + ".dat");
				ObjectOutputStream oOS = new ObjectOutputStream(fOS);
			}catch (Exception error){
				System.out.println(error.getMessage());
			}
			
		}else if(e.getSource() == load){
			try { 
				FileInputStream fInS = new FileInputStream(".dat"); 
				ObjectInputStream obInS = new ObjectInputStream(fInS); 
//				this.maze = obInS;
			} catch (Exception error){ 
				System.out.println(error.getMessage()); 
			} // end try 
		}
	}//end ActionEvent

	public static void main(String[] args){
		GUI_Maze GM = new GUI_Maze();
		

	}//end main method

	

}//end GUI_Maze class def
