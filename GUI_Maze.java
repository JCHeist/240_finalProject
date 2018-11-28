import java.awt.*;
import java.swing.*;
import java.io.*;

public class GUI_Maze extends JFrame implements ActionListener{
	Maze maze;
	Node spot[][] = new Node[10][10];
	Node start = maze.getStart();
	Node current = start;
	
	Timer timer = new Timer(1000,this);
	public GUI_Maze(){
		this.setUpGUI();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible = true;
		this.setSize(640,480);
	}//end GUI_Maze
	
	public void setUpGUI(){
		Container pnlMaze = this.Container();

		

	}//end setUpGUI


}//end GUI_Maze class def
