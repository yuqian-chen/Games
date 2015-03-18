import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.Timer;

import javax.swing.*;
import javax.swing.border.*;

public class puzzlePieces implements MouseListener {
	private List<BufferedImage> orgPiece;
	private List<BufferedImage> temp;
	private List<pair> mouseClick = new ArrayList<pair>();
	JLabel[][] container;
	JPanel holder = new JPanel();
	boolean success;
	private int col;

	public puzzlePieces(int row, int col, Image img, int time) {
		this.col = col;
		orgPiece = new ArrayList<BufferedImage>();
		temp = new ArrayList<BufferedImage>();
		container = new JLabel[row][col];

		holder.setLayout(new GridLayout(row, col));
		holder.setBounds(20, 90, 600, 450);
		holder.setOpaque(false);

		int height = img.getHeight(null) / row;
		int width = img.getWidth(null) / col;
		int count = 0;
		// initializing puzzle pieces
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				orgPiece.add(new BufferedImage(width, height, 5));
				// draws the image chunk
				Graphics2D gr = orgPiece.get(count).createGraphics();
				gr.drawImage(img, 0, 0, width, height, width * j, height * i,
						width * j + width, height * i + height, null);
				gr.dispose();
				temp.add(orgPiece.get(count));
				count++;
			}
		}
		shufflePices();

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				remove();
			}
		}, time * 1000);
	}
	
	public void remove(){
		for (int i = 0; i < container.length; i++) {
			for (int j = 0; j < container[i].length; j++) {
				container[i][j].removeMouseListener(this);
			}
		}
	}
	public void shufflePices() {
		ArrayList<Integer> index = new ArrayList<Integer>();
		for (int i = orgPiece.size() / 2; i < orgPiece.size(); i++) {
			index.add(i);
		}
		Random shuffle = new Random();
		for (int i = 0; i < orgPiece.size() / 2; i++) {
			int num = shuffle.nextInt(index.size());
			temp.set(i, orgPiece.get(index.get(num)));
			temp.set(index.get(num), orgPiece.get(i));
			index.remove(num);
		}
		int count = 0;
		for (int i = 0; i < container.length; i++) {
			for (int j = 0; j < container[i].length; j++) {
				container[i][j] = new JLabel(new ImageIcon(temp.get(count)));
				container[i][j].setBorder(new LineBorder(Color.white, 2));
				holder.add(container[i][j]);
				container[i][j].addMouseListener(this);
				count++;
			}
		}
	}

	public void mouseClicked(MouseEvent e) {
		for (int i = 0; i < container.length; i++) {
			for (int j = 0; j < container[i].length; j++) {
				if (e.getSource().equals(container[i][j])) {
					container[i][j].setBorder(new LineBorder(Color.red, 3));
					mouseClick.add(new pair(i, j));
				}
			}
		}
		if (mouseClick.size() == 2) {
			int sRow = mouseClick.get(0).i;
			int sCol = mouseClick.get(0).j;
			int dRow = mouseClick.get(1).i;
			int dCol = mouseClick.get(1).j;
			if (sRow == dRow && sCol == dCol) {
				container[sRow][sCol].setBorder(new LineBorder(Color.white, 2));
			} else {
				int source = sRow * col + sCol;
				int dest = dRow * col + dCol;
				BufferedImage tempImg = temp.get(source);
				temp.set(source, temp.get(dest));
				temp.set(dest, tempImg);
				container[sRow][sCol].setIcon(new ImageIcon(temp.get(source)));
				container[dRow][dCol].setIcon(new ImageIcon(temp.get(dest)));
				container[sRow][sCol].setBorder(new LineBorder(Color.white, 2));
				container[dRow][dCol].setBorder(new LineBorder(Color.white, 2));
				if (orgPiece.get(source).equals(temp.get(source))) {
					container[sRow][sCol].removeMouseListener(this);
					container[sRow][sCol].setBorder(null);
				}
				if (orgPiece.get(dest).equals(temp.get(dest))) {
					container[dRow][dCol].removeMouseListener(this);
					container[dRow][dCol].setBorder(null);

				}
			}
			mouseClick = new ArrayList<pair>();
		}

		success = true;
		for (int i = 0; i < temp.size(); i++) {
			if (!orgPiece.get(i).equals(temp.get(i))) {
				success = false;
			}
		}
		if (success) {
			success = true;
		}

	}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	class pair {
	int i;
	int j;

		pair(int i, int j) {
			this.i = i;
			this.j = j;
		}

		
	}

}
