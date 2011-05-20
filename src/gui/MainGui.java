package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import backend.Cell;
import backend.SaveLoadHandler;


@SuppressWarnings("serial")
public class MainGui extends JFrame implements ActionListener {

	private static int XCOUNT = 50;
	private static int YCOUNT = 50;
	private final String RUN = "run Rounds";
	private final String RUNSTOP = "stop run Rounds";

	private boolean runStopped = false;
	private Cell[][] cells;
	private JButton next = new JButton("next Round");
	private JButton run = new JButton(RUN);
	private JButton reset = new JButton("reset");
	private JButton random = new JButton("random");
	private JButton save = new JButton("save");
	private JButton load = new JButton("load");
	private JTextField numRounds = new JTextField(3);
	private JTextField pauseTime = new JTextField(4);
	private JLabel pauseTimeLab = new JLabel("ms pause");
	private JPanel center = new JPanel();

	private final JFileChooser chooser = new JFileChooser(System.getProperty("user.home"));
	
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
		setSize(805, 615);
		setLayout(new BorderLayout());

		JPanel bottom = new JPanel(new FlowLayout());

		initializeGOL();
		initializeGrid();

		next.addActionListener(this);
		run.addActionListener(this);
		reset.addActionListener(this);
		random.addActionListener(this);
		save.addActionListener(this);
		load.addActionListener(this);

		bottom.add(pauseTime);
		bottom.add(pauseTimeLab);
		bottom.add(numRounds);
		bottom.add(run);
		bottom.add(next);
		bottom.add(reset);
		bottom.add(random);
		bottom.add(save);
		bottom.add(load);

		add(bottom, BorderLayout.SOUTH);
		add(center, BorderLayout.CENTER);

		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		deactivateButtons();
		System.out.println(getSize());
		if (e.getSource() == next) {
			new Thread(new Runnable() {
				public void run() {
					nextRound();
					activateButtons();
				};
			}).start();
		}
		else if (e.getSource() == run) {
			if (RUNSTOP.equals(run.getText())) {
				run.setEnabled(false);
				run.setText(RUN);
				runStopped = true;
			}
			else if (RUN.equals(run.getText())) {
				runStopped = false;
				rounds = Integer.parseInt(numRounds.getText().equals("") ? "0" : numRounds.getText());

				new Thread(new Runnable() {
					@Override
					public void run() {
						run.setText(RUNSTOP);
						run.setEnabled(true);
						for (int i = 0; i < rounds; i++) {
							if (runStopped) {
								break;
							}
							numRounds.setText((rounds - (i + 1)) + "");
							nextRound();
							try {
								Thread.sleep(Integer.parseInt(pauseTime.getText().equals("") ? "500" : pauseTime.getText()));
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						numRounds.setText(rounds + "");
						run.setText(RUN);
						activateButtons();
					}
				}).start();
			}
		}
		else if (e.getSource() == reset) {
			new Thread(new Runnable() {
				public void run() {
					resetCells();
					activateButtons();
				};
			}).start();
		}
		else if (e.getSource() == random) {
			new Thread(new Runnable() {
				public void run() {
					randomizeCells();
					activateButtons();
				};
			}).start();
		}
		else if (e.getSource() == save) {
			int returnVal = chooser.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				System.out.println("You chose to save to this file: " + chooser.getSelectedFile().getAbsolutePath());
				SaveLoadHandler.saveToFile(cells, chooser.getSelectedFile());
			}
			activateButtons();
		}
		else if (e.getSource() == load) {
			int returnVal = chooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				System.out.println("You chose to open this file: " + chooser.getSelectedFile().getAbsolutePath());
				cells = SaveLoadHandler.loadFromFile(chooser.getSelectedFile());
				initializeGrid();
			}
			activateButtons();
		}
	}

	/**
	 * create a new, empty grid
	 */
	private void initializeGOL() {
		cells = new Cell[YCOUNT][XCOUNT];
		for (int y = 0; y < cells.length; y++) {
			Cell[] cs = cells[y];
			for (int x = 0; x < cs.length; x++) {
				cs[x] = new Cell(x, y, cells);
				cs[x].addMouseListener(cs[x]);
			}
		}
	}

	/**
	 * initializes the GUI
	 */
	private void initializeGrid() {
		YCOUNT = cells.length;
		XCOUNT = cells[0].length;
		center.removeAll();
		center.setLayout(new GridLayout(YCOUNT, XCOUNT));
		for (int y = 0; y < cells.length; y++) {
			for (int x = 0; x < cells[y].length; x++) {
				center.add(cells[y][x]);
			}
		}
		center.revalidate();
	}

	private void nextRound() {
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

	private void resetCells() {
		for (int y = 0; y < cells.length; y++) {
			for (int x = 0; x < cells[y].length; x++) {
				cells[y][x].setAlive(false);
			}
		}
	}

	private void randomizeCells() {
		Random rand = new Random();
		for (int y = 0; y < cells.length; y++) {
			for (int x = 0; x < cells[y].length; x++) {
				cells[y][x].setAlive(rand.nextBoolean());
			}
		}
	}

	private void activateButtons() {
		next.setEnabled(true);
		run.setEnabled(true);
		reset.setEnabled(true);
		random.setEnabled(true);
		save.setEnabled(true);
		load.setEnabled(true);
		numRounds.setEditable(true);
	}

	private void deactivateButtons() {
		next.setEnabled(false);
		run.setEnabled(false);
		reset.setEnabled(false);
		random.setEnabled(false);
		save.setEnabled(false);
		load.setEnabled(false);
		numRounds.setEditable(false);
	}
}
