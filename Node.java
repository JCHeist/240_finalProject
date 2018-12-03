import javax.swing.*;
import java.awt.*;
//import javax.swing.BorderFactory;
public class Node extends JButton{
	private boolean wall;
	private boolean finish;
	
	//Border emptyBorder = BorderFactory.createEmptyBorder();

	private int x;
	private int y;
	
	private Node up;
	private Node down;
	private Node left;
	private Node right;


	public Node(){

		//set up visual aspects of buttons
		super();		
        setBorder(null);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(true);	
		this.setBackground(Color.WHITE);	

		wall = false;
		finish = false;		

		x = 0;
		y = 0;

		up = null;
		down = null;
		left = null;
		right = null;
	}//end constructor
	
	void setFinish(boolean finish){
		this.finish = finish;
		this.setBackground(Color.BLUE);
	}//end setFinish

	boolean checkFinish(){
		return this.finish;
	}//end checkFinish

	void setWall(boolean wall){
		//set wall if it is not the finish or end
		if((this.checkFinish() == false) &&  (this.getCoordinates().equals("0, 0")==false)){ 
			this.wall = wall;
			if(this.wall){
				this.setBackground(Color.BLACK);
			}else{
				this.setBackground(Color.WHITE);
			}
			this.setOpaque(true);
		}	
	}//end setWall	

	//for maze solving visual
	public void setRed(){
		setBackground(Color.RED);
	}
	
	//for mae solving visual
	public void setGreen(){
		setBackground(Color.GREEN);
	}

	boolean getWall(){
		return this.wall;
	}//end getWall

	
	void setCoordinates(int x, int y){
		if (x == 0 && y == 0){
			this.setBackground(Color.GREEN);
		}
			this.x = x;
			this.y = y;
	}//end setCoordiantes

	//return x and y coorinates in a string
	String getCoordinates(){
		String xy = new String();
		
		if (this == null){
			xy = "N/A";
		}else{
			xy = String.valueOf(x) + ", " + String.valueOf(y);
		}
		
		return xy;
	}//end getCoordinates

	void setUp(Node up){
		this.up = up;
	}//end setUp

	Node getUp(){
		return this.up;
	}//end getUp

	void setDown(Node down){
		this.down = down;
	}//emd sietDown

	Node getDown(){
		return this.down;
	}//end getDown

	void setLeft(Node left){
		this.left = left;
	}//end setLeft

	Node getLeft(){
		return this.left;
	}//end getLeft

	void setRight(Node right){
		this.right = right;
	}//end setRight

	Node getRight(){
		return this.right;
	}//end getRight
}//end class definition
