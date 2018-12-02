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
					mazeWeb[w][h].setWall(!mazeWeb[w][h].getWall());
				}//end if get source of the buttons
			}//end for w
		}//end for h
		
		//remove path boxed if they are not walls
		if(e.getSource() == solve){
			visited = new ArrayList<Node>();
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

			make_maze();
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
						mazeWeb[i][j].setWall(false);
					}else if(mazeWeb[i][j].checkFinish()){

					}else if((mazeWeb[i][j].getCoordinates()).equals("0, 0")){


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


		
	public boolean check_node(Node node, List<Node> visited){
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
				//check to see if this is the finish (but only if it is the last row)
				if(i == height - 1){
					String coords = mazeWeb[j][i].getCoordinates();
					String finish = String.valueOf(width - 1) + ", " + String.valueOf(height - 1);

					if(coords.equals(finish)){
						mazeWeb[j][i].setFinish(true);
					}
				}
			}//end for j
			//set above to equal below to make new row below this one to link
			above = below.clone();

		}//end for i	
	}//end makeGraph
	
	public boolean change_master_says_change(){
//		Random rand = new Random(System.currentTimeMillis());
//		int random = (rand.nextInt()) % 2;
		
		int random;
		random = (int)(Math.random() * 51 + 1) % 2;
		
	
		if(random == 1){
			return true;
		}else{
			return false;
		}
	}

	public void make_maze(){
		int height = this.height;
		int newa[] = new int[width];
		int key = 1;
		String arr_string;

		//assign a 0 or key to each spot to each spot
		for(int a = 0; a < width; a = a + 2){
			newa[a] = 0;
			newa[a+1] = key;
			key ++;
			System.out.println(key);
		}//end for i
		
		int temp[] = newa;

		for(int i = 0; i < height; i ++){
			int old_place = -1;
			int old_place2 = -1;
			int horz[] = temp;
			//assign a 0 or key to each spot to each spot
			for(int a = 0; a < width; a = a + 2){
				horz[a] = 0;
				if(horz[a + 1] == 0){
					horz[a + 1] = key;
					key ++;
				}
//				System.out.println(key);
			}//end for i
		
		
			

			//horizontal rows
			for(int look = 0; look < width; look =  look + 2){
	
				old_place = -1;
				old_place2 = -1;

				boolean deletable = false;
				boolean checked = false;
				if(look > 0){	
					if(horz[look - 1] != horz[look + 1]){
						if(change_master_says_change()){	
							horz[look] = key;
							old_place = horz[look+1];//store old value
							horz[look+1] = key;
							old_place2 = horz[look - 1];
							horz[look - 1] = key;
							
							for(int change = 0; change < width; change++){
								if(horz[change] == old_place || horz[change] == old_place2){
									horz[change] = key;
								}
							}							
						}//end contact to change master wizard dude... woo jokes
					
					}//end if			
				}//end check for being greater than 0 
				
			key ++;
/*			

				String horizontal = "h: ";
				
				for (int display_h = 0; display_h < width; display_h ++){
					horizontal = horizontal + String.valueOf(horz[display_h]) + " ";
				}
				System.out.println(horizontal);
	*/

		}

	
			//look at every index to see if the value was changed... if it was set the value to the new value
			for(int change = 0; change < width;	change ++){

				if(horz[change] == 0){
					mazeWeb[change][i].setWall(true);
				}		
			}//end for change			
			i++;
	
			//vertical rows
			int vert[] = horz;
			//assign a 0 or key to each spot to each spot
			for(int a = 0; a < width; a = a + 2){
				newa[a] = 0;
//				System.out.println(key);
			}//end for i
			

			if(i == this.height - 1){
				for(int v = 1; v < width - 2 ; v = v + 2){
					boolean exists_in_last_row = true;
					if(vert[v] != vert[v + 2]){
						vert[v + 1] = 1;
					}
					
					if(exists_in_last_row){
				
					}else{
						vert[v + 1] = vert[v];
						vert[v + 2] = vert[v];
					}
				}
				
			}else{
	
	 			String vetical = "v: "; 
	
				for(int v = 1; v < width; v = v + 2){
					boolean make_wall = false;
					
					make_wall = vertical_check(vert, v);
	
					if(make_wall){
						vert[v] = 0;
					}
				}//end for v
			}
			for(int change = 0; change < width; change ++){
				if(vert[change] == 0){
					mazeWeb[change][i].setWall(true);
				}
			}
		temp = vert;
			
		}
	}//end make maze	



	private boolean vertical_check(int arr[], int this_index){
		boolean change_this = false;

		for(int look = 1; look < this.width; look = look + 2){
			//check all indexes to see if another key like this key exists
				if(look != this_index){
					if(arr[look] == arr[this_index]){	
						if(change_master_says_change()){
							change_this = true;
						}//end get random value
					}//end check to see if theres an index equal to this
				}//end check to make sure not same index

		}//end for look

		return change_this;

	}//end vertical check	

}//end maze class def
















