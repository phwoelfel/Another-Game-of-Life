package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import backend.Cell;

@SuppressWarnings("serial")
public class MainGui extends JFrame implements ActionListener, Runnable {

	private final static int XCOUNT = 50;
	private final static int YCOUNT = 50;
	
	private Cell[][] cells;
	private JButton next = new JButton("next Round");
	private JButton run = new JButton("run Rounds");
	private JButton reset = new JButton("reset");
	private JButton random = new JButton("random");
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
		reset.addActionListener(this);
		random.addActionListener(this);
		
		bottom.add(numRounds);
		bottom.add(run);
		bottom.add(next);
		bottom.add(reset);
		bottom.add(random);
		
		add(bottom, BorderLayout.SOUTH);
		add(center, BorderLayout.CENTER);
		
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
		else if(e.getSource() == reset){
			resetCells();
		}
		else if(e.getSource() == random){
			randomizeCells();
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
			}
		}
	}

	private void resetCells(){
		for (int y = 0; y < cells.length; y++) {
			for (int x = 0; x < cells[y].length; x++) {
				cells[y][x].setAlive(false);
			}
		}
	}
	
	private void randomizeCells(){
		Random rand = new Random();
		for (int y = 0; y < cells.length; y++) {
			for (int x = 0; x < cells[y].length; x++) {
				cells[y][x].setAlive(rand.nextBoolean());
			}
		}
	}

	private void activateButtons(){
		next.setEnabled(true);
		run.setEnabled(true);
		reset.setEnabled(true);
		random.setEnabled(true);
		numRounds.setEditable(true);
	}

	private void deactivateButtons(){
		next.setEnabled(false);
		run.setEnabled(false);
		reset.setEnabled(false);
		random.setEnabled(false);
		numRounds.setEditable(false);
	}
	
	@Override
	public void run() {
		deactivateButtons();
		for(int i=0;i<rounds;i++){
			nextRound();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		activateButtons();
		
	}
}
