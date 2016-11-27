package flux.hk.state;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public abstract class State {

	protected boolean newDraw = true;
	protected State nextState = null;
	
	public State() {
	}
	
	public abstract void update();
	public abstract void draw(Graphics2D g);
	
	public abstract void keyPressed(KeyEvent e);
	public abstract void keyReleased(KeyEvent e);
	public abstract void keyTyped(KeyEvent e);
	public abstract void mouseWheelMoved(MouseWheelEvent e);
	public abstract void mouseDragged(MouseEvent e);
	public abstract void mouseMoved(MouseEvent e);
	public abstract void mouseClicked(MouseEvent e);
	public abstract void mouseEntered(MouseEvent e);
	public abstract void mouseExited(MouseEvent e);
	public abstract void mousePressed(MouseEvent e);
	public abstract void mouseReleased(MouseEvent e);

	public State getNextState() {
		return nextState;
	}
	
	public boolean getNewDraw() {
		return newDraw;
	}
	
	public void forceDraw() {
		newDraw = true;
	}
	
	public enum States {
		
		START(0, new StartState()),
		DASHBOARD(1, new DashboardState());
		
		private int number;
		private State state;
		States(int no, State s) {
			number = no;
			state = s;
		}
		
		State getState() {
			return state;
		}
		
		int getNumber() {
			return number;
		}
		
	}
	
	public static State getState(int g) {
		States[] states = States.values();
		for (int i = 0; i < states.length; i++) {
			if (states[i].getNumber() == g) { return states[i].getState(); }
		}
		return null;
	}
	
	public static int getStateNumber(State gs) {
		if (gs instanceof StartState) {
			return States.START.getNumber();
		}
		if (gs instanceof DashboardState) {
			return States.DASHBOARD.getNumber();
		}
		return -1;
	}
	
}
