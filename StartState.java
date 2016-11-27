package flux.hk.state;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;

import flux.hk.main.FileReader;
import flux.hk.main.Main;

public class StartState extends State {

	private BufferedImage startScreen;

	private Color blue = new Color(4, 125, 255);
	private int bufferTime = 30, fill = 0;
	private float fillVel = 1.0f;

	public StartState() {
		startScreen = FileReader.loadImage("/assets/splash/start.png");
	}

	public void update() {
		if (bufferTime > 0) {
			bufferTime--;
			newDraw = true;
		} else if (fill < 360) {
			fill += (int) (5f * fillVel);
			fillVel += 0.05f;
			newDraw = true;
		} else {
			nextState = States.DASHBOARD.getState();
		}
	}

	public void draw(Graphics2D g) {
		g.drawImage(startScreen, null, 0, 0);

		int size = 500;
		Arc2D pie = new Arc2D.Double(
				new Rectangle((Main.WIDTH / 2) - (size / 2), (Main.HEIGHT / 2) - (size / 2), size, size), 90, fill,
				Arc2D.PIE);
		g.setColor(blue);
		g.fill(pie);

		newDraw = false;
	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
	}

	public void mouseDragged(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

}
