package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import backend.Cell;

public class MainGui extends JFrame implements ActionListener, Runnable {

	private final static int XCOUNT = 50;
	private final static int YCOUNT = 50;
	
	private Cell[][] cells;
	private JButton next = new JButton("next Round");
	private JButton run = new JButton("run Rounds");
	private JTextField numRounds = new JTextField(5);
	
	private int rounds;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new MainGui();
	}

	public MainGui() {
		setTitle("Game Of Life");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 600);
		setLayout(new BorderLayout());
		
		JPanel center = new JPanel(new GridLayout(YCOUNT, XCOUNT));
		JPanel bottom = new JPanel(new FlowLayout());
		
		cells = new Cell[YCOUNT][XCOUNT];
		for (int y = 0; y < cells.length; y++) {
			Cell[] cs = cells[y];
			for (int x = 0; x < cs.length; x++) {
				cs[x] = new Cell(x, y, cells);
				cs[x].addMouseListener(cs[x]);
				center.add(cs[x]);
			}
			
		}
		
		next.addActionListener(this);
		run.addActionListener(this);
		
		bottom.add(numRounds);
		bottom.add(run);
		bottom.add(next);
		
		add(bottom, BorderLayout.SOUTH);
		add(center);
		
		setVisible(true);

		cells[0][0].nextRound();
		cells[0][5].nextRound();
		cells[0][XCOUNT-1].nextRound();
		
		cells[5][0].nextRound();
		cells[5][10].nextRound();
		cells[5][XCOUNT-1].nextRound();
		
		cells[YCOUNT-1][0].nextRound();
		cells[YCOUNT-1][5].nextRound();
		cells[YCOUNT-1][XCOUNT-1].nextRound();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == next){
			nextRound();
		}
		else if(e.getSource() == run){
			rounds = Integer.parseInt(numRounds.getText());
			Thread th = new Thread(this);
			th.start();
		}
	}
	
	private void nextRound(){
		for (int y = 0; y < cells.length; y++) {
			for (int x = 0; x < cells[y].length; x++) {
				cells[y][x].nextRound();
			}
		}
		for (int y = 0; y < cells.length; y++) {
			for (int x = 0; x < cells[y].length; x++) {
				cells[y][x].finishRound();
				cells[y][x].repaint();
			}
		}
	}

	@Override
	public void run() {
		for(int i=0;i<rounds;i++){
			nextRound();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}
