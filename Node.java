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
	}//end setFinish

	boolean checkFinish(){
		return this.finish;
	}//end checkFinish

	void setWall(){
		this.wall = !this.wall;
		if(this.wall){
			this.setBackground(Color.BLACK);
		}else{
			this.setBackground(Color.WHITE);
		}
		this.setOpaque(true);	
	}//end setWall	

	public void setRed(){
		setBackground(Color.RED);
	}
	
	public void setGreen(){
		setBackground(Color.GREEN);
	}

	boolean getWall(){
		return this.wall;
	}//end getWall

	
	void setCoordinates(int x, int y){
		this.x = x;
		this.y = y;
	}//end setCoordiantes

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
