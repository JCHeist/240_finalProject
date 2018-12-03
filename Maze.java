import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.border.Border;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Maze implements Serializable{
		
		//declare variables for head andGUI
		Node start = new Node();
		Node current = start;
		Node mazeWeb[][];
		Node finish;
		List<Node> visited;
		boolean solved;
		int width;
		int height;


	//create maze and set widht and height	
	public Maze(int width, int height){
		makeGraph(current, width, height);
		this.height = height;
		this.width = width;

	}
	
	public void setFinish(Node f){
		this.finish = f;
	}


	public Node[][] getMazeWeb(){
		return this.mazeWeb;	
	}

	public int getHeight(){
		return this.height;
	}
	
	public int getWidth(){
		return this.width;

	}

	public void setSolved(boolean s){
		this.solved = s;
	}//end setSolved
	

	//delete walls and paths made
	public void clear_maze(){

			for(int i = 0; i < this.width; i ++){
				for(int j = 0; j < this.height; j ++){
					if(mazeWeb[i][j].getWall()){
						mazeWeb[i][j].setWall(false);
					}else if(mazeWeb[i][j].checkFinish()){
						mazeWeb[i][j].setBackground(Color.BLUE);
					}else if((mazeWeb[i][j].getCoordinates()).equals("0, 0")){
						mazeWeb[i][j].setGreen();

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
		

	//for testing and possible dungeon craweler type function
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

	public void startSolve(){
		visited = new ArrayList<Node>();	
		int counter = 0;
		solve(mazeWeb[0][0], visited, counter);
		
	}

	//recursive solve function
	public boolean solve(Node searching, List<Node> visited, int nodes_passed ){
		nodes_passed = nodes_passed + 1;
		visited.add(searching);
		System.out.println(searching.getCoordinates());
		searching.setGreen();
		
		//check for finish
		if((searching.getCoordinates()).equals(this.finish.getCoordinates())){
			solved = true;
		}

		//look right
		if(solved == false){
			if(check_node(searching.getRight(), visited)){
				solved = solve(searching.getRight(), visited, nodes_passed);
			}
		}
		
		//look down
		if(solved == false){
			if(check_node(searching.getDown(), visited)){
				solved = solve(searching.getDown(), visited, nodes_passed);
			}
		}
	
		//look up
		if(solved == false){
			if(check_node(searching.getUp(), visited)){
				solved = solve(searching.getUp(), visited, nodes_passed);
			}
		}

		//look left
		if(solved == false){
			if(check_node(searching.getLeft(), visited)){
				solved = solve(searching.getLeft(), visited, nodes_passed);
			}
		}
		
		//if this node is visited and not part of the solution, turn it red, otherwise keep it green
		if(solved == false){
			searching.setRed();
		}else{
			searching.setGreen();
		}
		
		//return this to recursive function that called this
		return solved;
	}//end solve


	//check node used by solve to make sure wall is not there
	private boolean check_node(Node node, List<Node> visited){
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
	
	//returns a true or false value for the maze generation
	private boolean change_master_says_change(){
		
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
			}//end for i
		
		

			//horizontal rows
			for(int look = 0; look < width; look =  look + 2){
				//variales to store old key values so all can be changed to new value when merged... set to -1 at beginning of loop because no key will ever be -1
				old_place = -1;
				old_place2 = -1;

				
				if(look > 0){	
					if(horz[look - 1] != horz[look + 1]){
						if(change_master_says_change()){	//get ture of false to randomly decide wall
							horz[look] = key;
							old_place = horz[look+1];//store old value
							horz[look+1] = key;//new key for cells to be merged
							old_place2 = horz[look - 1];
							horz[look - 1] = key;//same new key
							
							for(int change = 0; change < width; change++){
								if(horz[change] == old_place || horz[change] == old_place2){
									horz[change] = key;
								}
							}							
						}//end contact to change master wizard dude... woo jokes	
					}//end if			
				}//end check for being greater than 0 
				
			key ++;

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
			}//end for i
			
			//check for last row
			//look at wall and compare paths next to wall
			if(i == this.height - 1){
				for(int v = 1; v < width - 2 ; v = v + 2){
					if(vert[v] != vert[v + 2]){
						vert[v + 1] = -1;
					}
				}
				
			}else{
	
				for(int v = 1; v < width; v = v + 2){
					boolean make_wall = false;
					
					//if it is ok to make a wall here and change master said yes.. change it
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
			//store values to bring down key values to next row
			temp = vert;
		}
	}//end make maze	


	//used by maze generator for vertical rows
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
