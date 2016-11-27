package flux.hk.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import flux.hk.main.FileReader;

public class ButtonManager {

	private boolean newDraw = false;
	
	private Font font;
	private ArrayList<Button> buttons = new ArrayList<Button>();
	private ArrayList<Integer> heldFunctions = new ArrayList<Integer>();
	
	public ButtonManager() {
		font = FileReader.getFont(24, "/assets/font/font.TTF");
	}

	public void addButton(Rectangle r, String t, Color c, int f) {
		buttons.add(new Button(r, t, c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha(), f));
	}
	
	public void update() {
		for (Button b : buttons) {
			b.update();
			if (b.getNewDraw()) {
				newDraw = true;
			}
		}
	}
	
	public void draw(Graphics2D g) {
		for (Button b : buttons) {
			b.draw(g, font);
		}
	}
	
	public void mouseMoved(MouseEvent e) {
		for (Button b : buttons) {
			b.mouseMoved(e);
		}
	}

	public void mousePressed(MouseEvent e) {
		for (Button b : buttons) {
			b.mousePressed(e);
		}
	}

	public void mouseReleased(MouseEvent e) {
		for (Button b : buttons) {
			if (b.mouseReleased(e)) {
				heldFunctions.add(b.getFunction());
			}
		}
	}
	
	public ArrayList<Integer> popFunctions() {
		ArrayList<Integer> send = new ArrayList<Integer>();
		for (Integer i : heldFunctions) {
			send.add(i);
		}
		heldFunctions.clear();
		return send;
	}
	
	public void resetNewDraw() {
		newDraw = false;
	}
	
	public boolean getNewDraw() {
		return newDraw;
	}
	
}
