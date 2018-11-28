import java.util.*;

public class Main{

	public static void main(String[] args){
	
		Maze maze = new Maze(16,16);

		Scanner in = new Scanner(System.in);

		boolean keepGoing = true;
	
		while(keepGoing){
		
			System.out.println("enter direction(u,d,l,r)");
		
			String resp = in.next();
		
			if(resp.equals("u") || resp.equals("d") || resp.equals("l") || resp.equals("r")){

				maze.transverse(resp);
			
			}else{
				System.out.println("whats that home dog?");
			}	
					

		

		}
		
		/*Node up = new Node();
		Node down = new Node();
		Node left = new Node();
		Node right = new Node();
		
		current.setCoordinates(0,0);		


		up.setDown(current);
		current.setUp(up);
		up.setCoordinates(0,1);	

		down.setUp(current);
		current.setDown(down);
		down.setCoordinates(0,-1);		

		left.setRight(current);
		current.setLeft(left);
		left.setCoordinates(-1,0);
	
		right.setLeft(current);
		current.setRight(right);	
		right.setCoordinates(1,0);	

		showPlace(current);
*/
		}//end main method
}//end Main class def
