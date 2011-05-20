package backend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SaveLoadHandler {

	
	public static Cell[][] loadFromFile(File f){
		Cell[][] cells=null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(f));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (br != null) {
			try {
				ArrayList<char[]> lines = new ArrayList<char[]>();
				String line = "";
				while ((line = br.readLine()) != null) {
					lines.add(line.toCharArray());
				}
				cells = new Cell[lines.size()][];
				for (int y = 0; y < cells.length; y++) {
					cells[y] = new Cell[lines.get(y).length];
					for (int x = 0; x < cells[y].length; x++) {
						cells[y][x] = new Cell(x, y, cells);
						cells[y][x].setAlive(Integer.parseInt(lines.get(y)[x]+"")>0);
						cells[y][x].addMouseListener(cells[y][x]);
					}
				}
				System.out.println("finished loading");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return cells;
	}
	public static Cell[][] loadFromFile(String f){
		return loadFromFile(new File(f));
	}
	
	
	public static boolean saveToFile(Cell[][] cells, File f){
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(f));
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
		if (bw != null) {
			try {
				for (int y = 0; y < cells.length; y++) {
					for (int x = 0; x < cells[y].length; x++) {
						bw.write(cells[y][x].isAlive()?1+"":0+"");
					}
					bw.newLine();
				}
				bw.flush();
				bw.close();
			} catch (IOException e1) {
				e1.printStackTrace();
				return false;
			}
		}
		return true;
	}
	public static boolean saveToFile(Cell[][] cells, String f){
		return saveToFile(cells, new File(f));
	}
}
