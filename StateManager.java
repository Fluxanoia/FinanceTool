package flux.hk.state;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import flux.hk.main.Main;
import flux.hk.state.State.States;

public class StateManager {

	private boolean holdingWindow = false, hoveringBar = false;
	private float barTrans = 0.0f, barLimit = 0.5f;
	private int barHeight = 20;
	private int windowX, windowY;
	private int holdX, holdY;

	private State currentState;

	public StateManager() {

		currentState = States.START.getState();

		Toolkit t = Toolkit.getDefaultToolkit();
		windowX = (int) ((t.getScreenSize().getWidth() / 2) - (Main.WIDTH / 2));
		windowY = (int) ((t.getScreenSize().getHeight() / 2) - (Main.HEIGHT / 2));

	}

	public void update() {
		currentState.update();
		
		if (currentState.getNextState() != null) {
			currentState = currentState.getNextState();
		}
		
		if (hoveringBar) {
			if (barTrans < barLimit) {
				barTrans += 0.05f;
				if (barTrans > barLimit) { barTrans = barLimit; }
				currentState.forceDraw();
			}
		} else {
			if (barTrans > 0) {
				barTrans -= 0.05f;
				if (barTrans < 0) { barTrans = 0; }
				currentState.forceDraw();
			}
		}
	}

	public boolean draw(Graphics2D g) {
		boolean success = currentState.getNewDraw();
		if (success) {
			currentState.draw(g);
			if (barTrans > 0.0f) {
				g.setColor(new Color(0, 0, 0, (int) (255f * barTrans)));
				g.fillRect(0, 0, Main.WIDTH, barHeight);
			}
		}
		return success;
	}

	public void keyPressed(KeyEvent e) {
		currentState.keyPressed(e);
	}

	public void keyReleased(KeyEvent e) {
		currentState.keyReleased(e);
	}

	public void keyTyped(KeyEvent e) {
		currentState.keyTyped(e);
	}
	
	public void mouseWheelMoved(MouseWheelEvent e) {
		currentState.mouseWheelMoved(e);
	}

	public void mouseDragged(MouseEvent e) {
		currentState.mouseDragged(e);
		if (holdingWindow) {
			windowX = (int) (MouseInfo.getPointerInfo().getLocation().getX() - holdX);
			windowY = (int) (MouseInfo.getPointerInfo().getLocation().getY() - holdY);
		}
	}

	public void mouseMoved(MouseEvent e) {
		currentState.mouseMoved(e);

		if (e.getY() <= barHeight && e.getY() > -1) {
			hoveringBar = true;
		} else {
			hoveringBar = false;
		}

	}

	public void mouseClicked(MouseEvent e) {
		currentState.mouseClicked(e);
	}

	public void mouseEntered(MouseEvent e) {
		currentState.mouseEntered(e);
	}

	public void mouseExited(MouseEvent e) {
		currentState.mouseExited(e);
	}

	public void mousePressed(MouseEvent e) {
		currentState.mousePressed(e);
		if (hoveringBar) {
			holdingWindow = true;
			holdX = e.getX();
			holdY = e.getY();
		}
	}

	public void mouseReleased(MouseEvent e) {
		currentState.mouseReleased(e);
		holdingWindow = false;
	}

	public int getWindowX() {
		return windowX;
	}

	public int getWindowY() {
		return windowY;
	}

}
