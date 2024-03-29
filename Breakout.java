/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 150;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of turns */
	private static final int NTURNS = 3;

/* Method: run() */
/** Runs the Breakout program. */
	public void run() {
		/* You fill this in, along with any subsidiary methods */
		setupBricks();
		setupPaddle();
		addMouseListeners();
		brick_num = NBRICKS_PER_ROW * NBRICK_ROWS;
		while(brick_num >0 && life > 0) {
			addBall();
			waitForClick();
			ballMove();
		}
	}
	
	private void setupBricks() {
		setSize(WIDTH, HEIGHT); // set application size
		
		for(int i=0; i<NBRICK_ROWS; i++) { // row index (x)
			for(int j=0; j<NBRICKS_PER_ROW; j++) { // column index (y)
				brick = new GRect(j*(BRICK_SEP + BRICK_WIDTH), BRICK_Y_OFFSET + i*(BRICK_SEP+BRICK_HEIGHT), BRICK_WIDTH, BRICK_HEIGHT);
				add(brick);
				brick.setFilled(true);
				if(i<2) {
					brick.setColor(Color.RED);
				}else if(i<4) {
					brick.setColor(Color.ORANGE);
				}else if(i<6) {
					brick.setColor(Color.YELLOW);
				}else if(i<8) {
					brick.setColor(Color.GREEN);
				}else {
					brick.setColor(Color.CYAN);
				}		
			}
		}
	}
	
	private void setupPaddle() {
		paddle = new GRect(WIDTH/2-PADDLE_WIDTH/2,HEIGHT-PADDLE_Y_OFFSET-PADDLE_HEIGHT,PADDLE_WIDTH,PADDLE_HEIGHT);
		add(paddle);
		paddle.setFilled(true);
		paddle.setFillColor(Color.BLACK);
	}
	
	public void mouseMoved(MouseEvent e) {
		double x = paddle.getX();
		if (e.getX() > WIDTH - PADDLE_WIDTH) {
			paddle.move(WIDTH - PADDLE_WIDTH - x, 0);
		} else {
			paddle.move(e.getX()-x, 0);
		}
	}
	
	private void addBall() {
		ball = new GOval(WIDTH/2-BALL_RADIUS,HEIGHT/2-BALL_RADIUS,2*BALL_RADIUS,2*BALL_RADIUS);
		add(ball);
		ball.setFilled(true);
		ball.setFillColor(Color.BLACK);
	}
	
	private void ballMove() {
		vy = 3.0;
		vx = rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean(0.5)) vx = -vx;
		while(true) {
			ball.move(vx, vy);
			pause(10);
			
			if ((ball.getX()<=0)||(ball.getX()>=WIDTH-2*BALL_RADIUS)) vx = -vx;
			if (ball.getY()<=0) vy = -vy;
			
			GObject collider = getCollidingObject(); // pay attention to the order of statements
			if (collider == paddle) vy = -vy;
			if (collider!=null && collider!=paddle) {
				vy = -vy;
				remove(collider);
				brick_num --;
			}
			if (ball.getY()>=HEIGHT-2*BALL_RADIUS) {
				life --;
				remove(ball);
				break;
			}
		}
	}
	
	private GObject getCollidingObject() {
		if(getElementAt(ball.getX(), ball.getY())!=null) {
			return getElementAt(ball.getX(), ball.getY());
		} else if (getElementAt(ball.getX()+2*BALL_RADIUS, ball.getY())!=null) {
			return getElementAt(ball.getX()+2*BALL_RADIUS, ball.getY());
		} else if (getElementAt(ball.getX(), ball.getY()+2*BALL_RADIUS)!=null) {
			return getElementAt(ball.getX(), ball.getY()+2*BALL_RADIUS);
		} else if (getElementAt(ball.getX()+2*BALL_RADIUS, ball.getY()+2*BALL_RADIUS)!=null) {
			return getElementAt(ball.getX()+2*BALL_RADIUS, ball.getY()+2*BALL_RADIUS);
		}
		return null;
	}
	
	private GRect brick;
	private GRect paddle;
	private GOval ball;
	private double vx, vy;
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private int life = NTURNS;
	private int brick_num;
}
