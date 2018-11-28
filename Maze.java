import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.border.Border;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Maze extends JFrame implements ActionListener{
		
		//declare variables for head andGUI
		Node start = new Node();
		Node current = start;
		Node mazeWeb[][];
		Node finish;
		List<Node> visited;
		boolean solved;

		//store width and height to be used in program
		int width;
		int height;
		
		JPanel mazeGUI = new JPanel();
		JPanel move = new JPanel();
		
		JButton solve = new JButton("Solve");		
		JButton make = new JButton("Make Maze");
		JButton clear = new JButton("Clear");
		JButton save = new JButton("Save");
		JButton load = new JButton("Load");	

//		Timer timer = new Timer(100000000,this);	
	
	public Maze(int width, int height){
		makeGraph(current, width, height);		

		//store height and width variables
		this.width = width;
		this.height = height;		

		//gui setup
		this.setUpGUI();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setSize(640,480);
}//end constructor



	public void setUpGUI(){
		Container pnlMain = this.getContentPane();
		mazeGUI.setLayout(new GridLayout(width, height,1,1));


		for(int h = 0; h < this.height; h ++){
			for(int w = 0; w < this.width; w ++){
				mazeGUI.add(mazeWeb[w][h]);//add node to web
				mazeWeb[w][h].addActionListener(this);//add action listener
			}//end for w
		}//end for h

		move.add(clear);
		move.add(solve);
		move.add(make);
		move.add(save);
		move.add(load);		
		
		clear.addActionListener(this);
		solve.addActionListener(this);
		make.addActionListener(this);		
		save.addActionListener(this);

		pnlMain.add(mazeGUI, BorderLayout.CENTER);
		pnlMain.add(move, BorderLayout.SOUTH);
	}//end setUpGUI



	public void actionPerformed(ActionEvent e){
		for(int h = 0; h < this.height; h ++){
			for(int w = 0; w < this.width; w ++){
				if(e.getSource() == mazeWeb[w][h]){
					mazeWeb[w][h].setWall();
				}//end if get source of the buttons
			}//end for w
		}//end for h
		
		//remove path boxed if they are not walls
		if(e.getSource() == solve){
			visited = new ArrayList();
			int counter = 0;
			this.finish = mazeWeb[this.width-1][this.height-1];		
	//		timer.start();
			for(int i = 0; i < this.width; i ++){
				for(int j = 0; j < this.height; j ++){
					if(mazeWeb[i][j].getWall() == false){
						mazeWeb[i][j].setBackground(Color.WHITE);
					}//end change if not wall
				}//enf for j
			}//end for i
		
			this.solved = false;			
			solve(this.start, visited, counter);
				
	//		timer.stop();
		}else if(e.getSource() == make){
			clear_maze();			

			make_maze(this.width, 0, this.height, 0, width - 1, 0);
		}else if(e.getSource() == clear){
			clear_maze();
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
				FileInputStream fInS = new FileInputStream("animation.dat"); 
				ObjectInputStream obInS = new ObjectInputStream(fInS); 
				this.start = (Node)obInS.readObject(); 
				this.current = start; 
			} catch (Exception error){ 
				System.out.println(error.getMessage()); 
			} // end try 
		}
	}//end ActionEvent



	public void clear_maze(){

			for(int i = 0; i < this.width; i ++){
				for(int j = 0; j < this.height; j ++){
					if(mazeWeb[i][j].getWall()){
						mazeWeb[i][j].setWall();
					}else{
						mazeWeb[i][j].setBackground(Color.WHITE);
					}			
				}//end for j
			}//end for i
	}//end clear_maze

	

	//move to node as directed by user
	//I know this looks a little over compact, It just looks more organized to me to put it all on one line since its so repetitive
	public void transverse(String direction){
		switch(direction){
			case "u": 
				if(this.current.getUp() != null){this.current = this.current.getUp();}
				break;
			case "d":
					if(this.current.getDown() != null){this.current = this.current.getDown();}
				break;
			case "l":
					if(this.current.getLeft() != null){this.current = this.current.getLeft();}
				break;
			case "r":	
					if(this.current.getRight() != null){this.current = this.current.getRight();}
				break;
			default: System.out.println("Command not found");
				break;
		}//end direction switch	
		showPlace(current);
	}//end transverse()
		


	public void showPlace(Node here){
		
		String str = new String();

		Node around[] = new Node[4];
		
		around[0] = here.getUp();//up
		around[1] = here.getDown();//down
		around[2] = here.getLeft();//left
		around[3] = here.getRight();//right
		
		String coords[] =  new String[4];

		for(int i = 0; i < 4; i ++){
			if(around[i] == null){
				coords[i] = "null, null";
			}else{
				coords[i] = around[i].getCoordinates();
			}//end if/else
		}//end for loop
		
		str = "                " + coords[0] + "                     \n" +
				coords[2] + "             " + here.getCoordinates() + "           " + coords[3] + "\n" +
			  "            " + coords[1] + "\n";
		System.out.println(str);
			
	}//end showPlace


	
	public boolean solve(Node searching, List<Node> visited, int nodes_passed ){
		nodes_passed = nodes_passed + 1;
		visited.add(searching);
		System.out.println(searching.getCoordinates());
		searching.setGreen();

/*	
		try{
			Thread.sleep(2000);
			
			searching.setBackground(Color.GREEN);
		}catch(InterruptedException e){
			e.printStackTrace();
		}	
*/	

		if(searching.getCoordinates().equals(this.finish.getCoordinates())){
			solved = true;
		}

		if(solved == false){
//			timer.start();
			if(check_node(searching.getRight(), visited)){
				solved = solve(searching.getRight(), visited, nodes_passed);
			}
		}
	
		if(solved == false){
//			timer.start();
			if(check_node(searching.getDown(), visited)){
				solved = solve(searching.getDown(), visited, nodes_passed);
			}
		}
	
		if(solved == false){
//			timer.start();
			//look up
			if(check_node(searching.getUp(), visited)){
				solved = solve(searching.getUp(), visited, nodes_passed);
			}
		}

		if(solved == false){
//			timer.start();
			if(check_node(searching.getLeft(), visited)){
				solved = solve(searching.getLeft(), visited, nodes_passed);
			}
		}
		
		if(solved == false){
			searching.setRed();
		}else{
			searching.setGreen();
		}
		
		return solved;
	}//end solve


		
	public boolean check_node(Node node, List visited){
		//check for null
		if(node == null){
			return false;
		}
		//check for wall at this node
		if(node.getWall()){
			return false;
		}
		//check arrayList to see if visited
		int size = visited.size();
		for(int i = 0; i < size; i ++){
			if(visited.contains(node)){
				return false;
			}	
		}//end for i
		return true;
	}//end check_node
	



	public void makeGraph(Node start, int width, int height){
		Node above[] = new Node[width];
		mazeWeb = new Node[width][height];		

		//set coordinates of start(top left node)
		above[0] = new Node();
		above[0].setCoordinates(0,0);
		mazeWeb[0][0] = above[0];//set this to be the GUI_Maze array			
		this.start = above[0];
		this.current = this.start;
		//make width number of nodes for the top rowi
		for(int k = 1; k < width; k ++){
			Node n = new Node();

			above[k] = n;
			above[k].setLeft(above[k-1]);
			above[k-1].setRight(above[k]);
			above[k].setCoordinates(k, 0);
			
			mazeWeb[k][0] = above[k];
		}//end add new nodes	
		
		
		//make a double linked list of nodes for
		for (int i = 1; i < height; i ++){
			Node below[] = new Node[width];
				
			//link first nodes too each other since they dont have a left
			Node leftMost = new Node();
			below[0] = leftMost;
			below[0].setUp(above[0]);
			above[0].setDown(below[0]);
			below[0].setCoordinates(0, i);
			
			mazeWeb[0][i] = below[0];
			for (int j = 1; j < width; j ++){
				//double link this line	
				Node toTheRight = new Node();
				below[j] = toTheRight;
				below[j].setLeft(below[j-1]);
				below[j-1].setRight(below[j]);
				below[j].setCoordinates(j, i);
				
				//double link above and below in same collumn
				below[j].setUp(above[j]);
				above[j].setDown(below[j]);	
				
				mazeWeb[j][i] = below[j];
				//check to see if this is the finish		
			}//end for j
			//set above to equal below to make new row below this one to link
			above = below.clone();
		
		}//end for i	
	}//end makeGraph
	


	public void make_maze(int x_max, int x_min, int y_max, int y_min, int oldWidth, int oldHeight){
		//select two random number in range
		Random rand = new Random();
		
		int mid_h = (y_max + y_min)/2;
		int mid_w = (x_max + x_min)/2;

		if((x_max - x_min) > 3){
			for(int w = x_min - 1; w < x_max; w ++){
				mazeWeb[w][mid_h].setWall();
			}
			for(int h = y_min - 1; h < y_max; h ++){
				mazeWeb[mid_w][h].setWall();
			}

		}else{
			for(int x = 0; x < (x_max - x_min); x ++){
				
			}		
		}


/*		
		//getRandom height to draw line
		int h = rand.nextInt(y_max-1) + y_min;
		if(h == oldHeight){
			h = h + 1;
		}//end hblock check
	
		//get random width to draw line
		int w = rand.nextInt(x_max-1) + x_min;
		if(w == oldWidth){
			w = w + 1;
		}//end wblock check	
		
		for(int x = x_min; x < x_max; x ++){
			if(!mazeWeb[x][h].getWall()){
				mazeWeb[x][h].setWall();
			}
		}//end for x
			
		for(int y = y_min; y < y_max; y ++){
			if(!mazeWeb[w][y].getWall()){
				mazeWeb[w][y].setWall();
			}
		}//end for y

//		mazeWeb[w][h].setWall();//flip middle node back to wall
		
		int spotL = w;
		int spotR = w;
		int spotT = h;
		int spotB = h;

		mazeWeb[spotL][h].setWall();		
		mazeWeb[spotR][h].setWall();
		mazeWeb[w][spotT].setWall();
		mazeWeb[w][spotB].setWall();

		make_maze(w, x_min, h, y_min, w, h);		
		make_maze(x_max, w, h, y_min, w, h);
		make_maze(w, x_min, y_max, h, w, h);
		make_maze(x_max, w, y_max, h, w, h);
*/
	}//end make maze	
}//end maze class def
