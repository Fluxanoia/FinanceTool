package flux.hk.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class Listener implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

	private Main main;
	
	public Listener(Main m) {
		main = m;
	}
	
	public void keyPressed(KeyEvent e) {
		main.keyPressed(e);
	}

	public void keyReleased(KeyEvent e) {
		main.keyReleased(e);
	}

	public void keyTyped(KeyEvent e) {
		main.keyTyped(e);
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		main.mouseWheelMoved(e);
	}

	public void mouseDragged(MouseEvent e) {
		main.mouseDragged(e);
	}

	public void mouseMoved(MouseEvent e) {
		main.mouseMoved(e);
	}

	public void mouseClicked(MouseEvent e) {
		main.mouseClicked(e);
	}

	public void mouseEntered(MouseEvent e) {
		main.mouseEntered(e);
	}

	public void mouseExited(MouseEvent e) {
		main.mouseExited(e);
	}

	public void mousePressed(MouseEvent e) {
		main.mousePressed(e);
	}

	public void mouseReleased(MouseEvent e) {
		main.mouseReleased(e);
	}

}
