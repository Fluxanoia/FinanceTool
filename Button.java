package flux.hk.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

public class Button {

	private boolean newDraw = false;

	private int r, g, b, a, f;
	private Rectangle rect;
	private String text;
	private boolean hover = false;

	public Button(Rectangle rect, String text, int r, int g, int b, int a, int f) {
		this.rect = rect;
		this.text = text;
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		this.f = f;
	}

	public void update() {
	}

	public void draw(Graphics2D gr, Font f) {
		gr.setColor(new Color(r, g, b, a));
		gr.fill(rect);

		if (hover) {
			gr.setColor(new Color(255, 255, 255, 75));
			gr.fill(rect);
		}

		gr.setFont(f);
		gr.setColor(Color.WHITE);
		gr.drawString(text, rect.x + ((rect.width - gr.getFontMetrics().stringWidth(text)) / 2),
				rect.y + rect.height - ((rect.height - gr.getFontMetrics().getHeight()) / 2));
		newDraw = false;
	}

	public int getFunction() {
		return f;
	}

	public void mouseMoved(MouseEvent e) {
		Point p = new Point(e.getX(), e.getY());
		if (rect.contains(p)) {
			if (!hover) {
				newDraw = true;
			}
			hover = true;
		} else {
			if (hover) {
				newDraw = true;
			}
			hover = false;
		}
	}

	public void mousePressed(MouseEvent e) {
	}

	public boolean mouseReleased(MouseEvent e) {
		Point p = new Point(e.getX(), e.getY());
		if (rect.contains(p)) {
			return true;
		} else {
			return false;
		}
	}

	public void resetNewDraw() {
		newDraw = false;
	}

	public boolean getNewDraw() {
		return newDraw;
	}

}
