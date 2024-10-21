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
	
	Shark shark = new Shark();
	Fish fish = new Fish();
	
	Background background = new Background("background.png");
	
	//Score related vars and timer
	int		roundTimer;
	int		score;
	long	time; //long is bigger int that can hold bigger whole numbers
	int 	wave;
	int 	randVx = (int)(Math.random()*(4+4+1)-4);
	int 	randVy = (int)(Math.random()*(4+4+1)-4);
	int 	randX = (int)(Math.random()*(400)+10);

	
	/*
	 * init any variables, objects etc for the "start"
	 * of the game
	 */
	public void init() {
		
		roundTimer = 30;
		score = 0;
		time = 0;
		wave = 1;
		
		shark.setScale(0.5, 0.5);
		shark.setXY(170, 430);

		fish.setWidthHeight(60, 60);
		fish.setScale(0.5,0.5);
		fish.setVx(randVx);
		fish.setVy(randVy);
		fish.setX(randX);
		fish.setY(350);
		//StdAudio.playInBackground("blablabla.wav");
		
		//background state
		background.setScale(2,2);
		background.setXY(0, 0);
	}
	
	
	public void reset() {
		//stuff to init the fish object
		
	}
	
	/*
	 * initialize objects and vars for the next round
	 */
	public void nextRound() {
		
		//reset the roundCounter
		wave += 1;
		roundTimer += 30;
		
		fish.setXY(420,  350);
		fish.setVx(2*wave);
		fish.setVy(-2*wave);
		
		shark.setXY(140,  430);
		shark.setVx(0);
		shark.setVy(0);
	}
	
	
	public void paint(Graphics g) {
		super.paintComponent(g);
		
		//add 16 to time since paint is called every 20ms
		time += 20; //time elapse updates :)
		
		if(time%500 == 0) { //has it been 1 second?
			roundTimer -= 1;
		}
			if(roundTimer==0) {
				t.stop();
				nextRound();
			}
		
		
		
		//LAYER your objects as you want them to layer visually!\
		background.paint(g); 
		shark.paint(g);
		fish.paint(g);
		
		
		//logic for resetting the dog position
		// and or making it bounce around
		if((fish.getY() > 500) || (fish.getY() == 350)) {
			fish.setY(430);
			if(shark.getX()!=0) {
				fish.setX((int)(Math.random()*(691)+10));
				fish.setVx((int)(Math.random()*(5+5+1)-5));
				fish.setVy((int)(Math.random()*(-5+3+1)-3));

			}
			if(wave >1) {
				fish.setX((int)(Math.random()*(691)+10));
				fish.setVx((int)(Math.random()*(5+5+1)-5)*wave);
				fish.setVy((int)(Math.random()*(-5+3+1)-3)*wave);
			}
		}
		
		if(shark.getY() < 240) {
			shark.setVy(4);
		}
		if(shark.getY()>350) {
			shark.setXY(170, 350);
			shark.setVy(0);
		}
		
		if(shark.getY() <0) {
			shark.setVy(fish.getVy()*-1);
			//StdAudio.playInBackground("blabla.wav");
			//more
		}
	
		
		//draw time related strings last so they are overlayed on top of anything else
		if(!t.isRunning()) {
			g.setFont(medFont);
			g.setColor(Color.white);
			g.drawString("Press the space bar for enext round!", 170, 200);
		}
		
		g.setFont(medFont);
		g.setColor(Color.white);
		
		g.drawString("Score: " + this.score, 400, 550);
		
		if(roundTimer >= 10) {
			g.drawString("Time "+this.roundTimer, 660, 550);
		}
		else if(roundTimer >= 0 && roundTimer <10) {
			g.drawString("Time: "+this.roundTimer, 660, 550);
		}
		else {
			fish.setVx(0);;
			fish.setXY(420,430);
		}
		
		//waves
		g.setFont(medFont);
		g.drawString("Round: " +this.wave, 10, 40);
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
		Rectangle rMouse = new Rectangle(mouse.getX(), mouse.getY(), 120, 200); //guess-and-check size for now
		
		//2nd rectangle will be for your OBJECT (aka the duck)
		Rectangle rMain = new Rectangle(shark.getX()+8, shark.getY()+6, shark.getWidth(), shark.getHeight());
			
		
		//check if they're colliding
		if(rMouse.intersects(rMain)) { //do the 2 rect intersect?
			if(roundTimer>0) {
				fish.setVy(10);;
				fish.setVx(0);;
				score += 1;
				shark.setVy(-4);;
				//StdAudio.playInBackground("blabla.wav");
			}
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
		if(arg0.getKeyCode() == 32) {
			if(!t.isRunning()) {
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

