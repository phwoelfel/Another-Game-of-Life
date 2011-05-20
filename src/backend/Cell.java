package backend;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Cell extends JPanel implements MouseListener {

	private boolean alive = false;
	private boolean tempAlive = false;
	private int xPos = -1;
	private int yPos = -1;
	private Cell[][] cells;

	public Cell(int x, int y, Cell[][] c) {
		super();
		xPos = x;
		yPos = y;
		cells = c;
		/*
		 * Random r = new Random(); alive = r.nextBoolean();
		 */
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Rectangle rect = g.getClipBounds();

		if (rect == null) {
			rect = new Rectangle(getLocation().x, getLocation().y, getWidth(), getHeight());
		}

		if (alive) {
			g.setColor(Color.black);
		}
		else {
			g.setColor(Color.white);
		}
		g.fillRect(rect.x, rect.y, rect.x + rect.width, rect.y + rect.height);
		
		g.setColor(Color.lightGray);
		g.drawLine(rect.x, rect.y, rect.x, rect.y + rect.height - 1);
		g.drawLine(rect.x + rect.width - 1, rect.y, rect.x + rect.width - 1, rect.y + rect.height - 1);
		g.drawLine(rect.x, rect.y, rect.x + rect.width - 1, rect.y);
		g.drawLine(rect.x, rect.y + rect.height - 1, rect.x + rect.width - 1, rect.y + rect.height - 1);
	}

	/**
	 * @return is there anybody?
	 */
	public boolean isAlive() {
		return alive;
	}

	/**
	 * @param alive bring live to the cell
	 */
	public void setAlive(boolean alive) {
		this.alive = alive;
		tempAlive = alive;
		repaint();
	}

	public void nextRound() {
		ArrayList<Cell> neigh = getAliveNeighbours();
		// System.out.println(xPos + "," + yPos + ": " + neigh.size());
		if (alive) {
			if (neigh.size() < 2 || neigh.size() > 3) {
				tempAlive = false;
			}
		}
		else {
			if (neigh.size() == 3) {
				tempAlive = true;
			}
		}
	}

	private ArrayList<Cell> getAliveNeighbours() {
		ArrayList<Cell> out = new ArrayList<Cell>();

		if (yPos > 0) {
			if (cells[yPos - 1][xPos].isAlive()) {
				out.add(cells[yPos - 1][xPos]);
			}
			if (xPos > 0) {
				if (cells[yPos - 1][xPos - 1].isAlive()) {
					out.add(cells[yPos - 1][xPos - 1]);
				}
			}
			if (xPos < (cells[yPos].length - 1)) {
				if (cells[yPos - 1][xPos + 1].isAlive()) {
					out.add(cells[yPos - 1][xPos + 1]);
				}
			}
		}
		if (xPos > 0) {
			if (cells[yPos][xPos - 1].isAlive()) {
				out.add(cells[yPos][xPos - 1]);
			}
		}
		if (xPos < (cells.length - 1)) {
			if (cells[yPos][xPos + 1].isAlive()) {
				out.add(cells[yPos][xPos + 1]);
			}
		}
		if (yPos < (cells.length - 1)) {
			if (cells[yPos + 1][xPos].isAlive()) {
				out.add(cells[yPos + 1][xPos]);
			}
			if (xPos > 0) {
				if (cells[yPos + 1][xPos - 1].isAlive()) {
					out.add(cells[yPos + 1][xPos - 1]);
				}
			}
			if (xPos < (cells[yPos].length - 1)) {
				if (cells[yPos + 1][xPos + 1].isAlive()) {
					out.add(cells[yPos + 1][xPos + 1]);
				}
			}
		}
		return out;
	}

	public void finishRound() {
		alive = tempAlive;
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		alive = !alive;
		tempAlive = alive;
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

}
