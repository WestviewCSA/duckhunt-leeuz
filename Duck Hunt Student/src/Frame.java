import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Frame extends JPanel implements ActionListener, MouseListener, KeyListener {
	//Create an Fish Object
	Font bigFont = new Font("Serif", Font.BOLD, 100);
	Font medFont = new Font("Serif", Font.BOLD, 50);
	Shark2 shark2 = new Shark2();
	Fish1 fish1 = new Fish1();
	Background background = new Background("background.png");
	Ground ground = new Ground("ground.png");
	

	
	//Score related vars and timer
	int		roundTimer = 0;
	int		score = 0;
	long	time = 0;
	int 	currRound = 1;
	
	/*
	 * init any variables, objects etc for the "start"
	 * of the game
	 */
	public void init() {
		
		shark2.setScale(0.5, 0.5);
		shark2.setWidthHeight(30, 30);
		shark2.setXY(400, 430);
		shark2.setVx(0);
		shark2.setVy(0);

		fish1.setXY(50, 410);
		fish1.setScale(0.5,0.5);
		fish1.setVx((int)(Math.random()*(4+4+1))-4);
		fish1.setVy((int)(Math.random()*(4+4+1))-4);
		
		//background state
		background.setScale(2,2);
		background.setXY(0, 0);
		
		ground.setScale(2, 2);
		ground.setXY(0, 0);
	}
	
	
	public void reset() {
		//stuff to init the fish object
		fish1.setScale(1, 1);
		fish1.setVx((int)(Math.random()*(4+4+1))-4);
		fish1.setVy((int)(Math.random()*(4+4+1))-4);

		
	}
	
	/*
	 * initialize objects and vars for the next round
	 */
	public void nextRound() {
		
		//reset the roundCounter
		roundTimer = 30;
		currRound++; //next round
		
		//re-calibrate your objects
		shark2.setXY(250,250);
		
		/*
		 * Maybe additional objects appear?
		 * they start off OFF the screen like in negative -1000 y
		 * maybe previously the additional characters have 0 vx and vy and off the screen
		 * 
		 */
		
		/*
		 * maybe the speed of the objects get faster over time each round
		 * int val = (int)(Math.random()*(max-min_+1)) + min
		 */
		int randVx = (int)(Math.random()*(4))+1;
		shark2.setVx(randVx + currRound);
	}
	
	
	public void paint(Graphics g) {
		super.paintComponent(g);
		
		//add 16 to time since paint is called every 20ms
		time += 20; //time elapse updates :)
		
		if(time%1000 == 0) { //has it been 1 second?
			roundTimer -= 1;
			if(roundTimer > 0) { 
				roundTimer++;
			}
		}
			if(roundTimer<=30) {
				shark2.setXY(550, 430);
			}
		
		
		
		//LAYER your objects as you want them to layer visually!\
		g.setFont(bigFont);
		background.paint(g); 
		ground.paint(g);
		shark2.paint(g);
		fish1.paint(g);
		
		
		//logic for resetting the dog position
		// and or making it bounce around
		if((fish1.getX()<0) || (fish1.getX()>800)) {
			fish1.setVx(fish1.getVx()*-1);
		}
		if((fish1.getY()<0) || (fish1.getY()>500)) {
			fish1.setVy(fish1.getVy()*-1);
		}
		if(((fish1.getVx() == 0) && (fish1.getVy()>0))) {
			fish1.setVy(0);
		}
		
		
		//draw time related strings last so they are overlayed on top of anything else
				g.setFont(bigFont);
				
				//draw the round String
				g.setColor(Color.white);

				g.drawString(""+this.roundTimer, 120, 100);
				g.drawString("Score: "+this.score, 600, 100 );
				
				//round text is slightly smaller
				g.setFont(medFont);
				g.setColor(Color.white);
				g.drawString("Round "+this.currRound, 5, 690);
				
				//Text for moving to next round
				if(roundTimer==30) {
					g.setFont(medFont);
					g.drawString("Press the space bar for the next round", 50, 0);
				}
		//draw time related 
	}
	
	
	public static void main(String[] arg) {
		Frame f = new Frame();
	}
	
	public Frame() {
		JFrame f = new JFrame("Duck Hunt");
		f.setSize(new Dimension(1070,735)); //change it
		f.setBackground(Color.blue);
		f.add(this);
		f.setResizable(false);
		f.setLayout(new GridLayout(1,2));
		f.addMouseListener(this);
		f.addKeyListener(this);
		
		init(); //call your init method to give properties to the objects and variables
		
		t.start();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	} 
	
	//Make the timer visible to the other methods
	Timer t = new Timer(16, this);
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		shark2.setVx(5);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent mouse) {
		// TODO Auto-generated method stub
		
		//perform a rectangle collision with the mouse and
		//the object
		Rectangle rMouse = new Rectangle(mouse.getX(), mouse.getY(), 25, 25); //guess-and-check size for now
		
		//2nd rectangle will be for your OBJECT (aka the duck)
		Rectangle rMain = new Rectangle(
									fish1.getX(),fish1.getY(),
									fish1.getWidth(), fish1.getHeight() // add number if it is bigger than 25,25
								);
		
		//check if they're colliding
		if(rMouse.intersects(rMain)) { //do the 2 rect intersect?
			fish1.setVy(10);
			fish1.setVx(0);
			
			//There was a successful click!
			//dog needs to move in the same ~x [position as the duck or your main character
			//make sure dog is currently off the screen (bottom) - y location related
			//make the dog y velocity negative (so it goes up)
			shark2.setX(fish1.getX()); //may need to add some offset (positive or negative to center
			shark2.setY(500); //in case the dog is is way in the abyss, let's bring it back to some y position
			shark2.setVy(-3); //dog goes up
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent arg0) {

		System.out.println(arg0.getKeyCode());
		
		//space bar continues the round
		//if the timer is stopped we can start it again
		if(arg0.getKeyCode()==32) {
			//start the timer again
			if(!t.isRunning()) {
				shark2.setVy(5);
				fish1.setVx(3);
				fish1.setVy(2);
				roundTimer = 30;
				t.start();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}

