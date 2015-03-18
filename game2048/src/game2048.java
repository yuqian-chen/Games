import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.MatteBorder;

public class game2048 implements KeyListener,ActionListener {
	public static final int RANDOM = 2;
	public static int col = 4;
	public static int row = 4;
	
	public int highest = 0;
	public int totalTiles;
	public int count;
	public boolean gamewin;
	
	public final Font font = new Font("Lithograph", Font.BOLD, 40);
	public final Font big_number = new Font("Lithograph", Font.BOLD, 60);
	// Colors of different numbers
	public static final Color BACKGROUND = new Color(187, 173, 160);
	public static final Color DEFAULT_TILE = new Color(204, 192, 179);
	public static final Color TWO = new Color(238, 228, 218);
	public static final Color FOUR = new Color(237, 224, 200);
	public static final Color EIGHT = new Color(242, 177, 121);
	public static final Color SIXTEEN = new Color(245, 149, 98);
	public static final Color THIRTYTWO = new Color(246, 124, 95);
	public static final Color SIXTYFOUR = new Color(246, 94, 59);
	public static final Color REMAINING = new Color(237, 204, 97);

	public JFrame frame = new JFrame("2048");
	public grid tile[][] = new grid[row][col];
    public JLayeredPane layout = new JLayeredPane();
	public JPanel gameboard = new JPanel(null);
	public JLabel text = new JLabel("", JLabel.CENTER);
	public JLabel score = new JLabel("", JLabel.CENTER);
	public JLabel best = new JLabel("", JLabel.CENTER);
	public JLabel designer = new JLabel("", JLabel.CENTER);
	public JButton start = new JButton();
	
	public static void main(String argc[]) {
		game2048 game = new game2048();
		game.boardSetup();
		game.init();
	}

	public void init() {
		
		totalTiles = 0;
		gamewin = false;
		if (highest < count) {
			highest = count;
		}
		best.setText("<html><center>Best<br>" + Integer.toString(highest)
				+ "<center><html>");
		count = 0;
		score.setText("<html><center>Score<br>0<center><html>");
		
		layout.moveToFront(gameboard);
		for (int i = 0; i < tile.length; i++) {
			for (int j = 0; j < tile[i].length; j++) {
				tile[i][j].setLabel(0);
				tile[i][j].getLabel().setBackground(DEFAULT_TILE);
			}
		}
		for (int i = 0; i < 2; i++) {
			tiles();
		}
		frame.addKeyListener(this);		

	}

	public void boardSetup() {
		frame.getContentPane().setBackground(new Color(252,255,249));
		frame.setPreferredSize(new Dimension(430, 580));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setResizable(false);
		//set game board
		gameboard.setLayout(new GridLayout(row, col));
		gameboard.setBounds(15, 140, 400, 400);
		gameboard.setBackground(BACKGROUND);
		gameboard.setBorder(new MatteBorder(5, 5, 5, 5, BACKGROUND));		
		for (int i = 0; i < tile.length; i++) {
			for (int j = 0; j < tile[i].length; j++) {
				tile[i][j] = new grid();
				gameboard.add(tile[i][j].getLabel());
			}
		}
		
        layout.add(gameboard);
        
        //set message display
        text.setFont(big_number);
		text.setBackground(new Color(237, 204, 97,150));
		text.setOpaque(true);
		text.setBounds(15,140,400,400);
		layout.add(text);
		
		//set 2048 label
		JLabel label2048 = new JLabel("<html>2048</html>", JLabel.CENTER);
		label2048.setOpaque(true);
		label2048.setBackground(new Color(237, 244, 97));
		label2048.setForeground(Color.white);
		label2048.setFont(new Font("Lithograph", Font.BOLD, 35));
		label2048.setBounds(15,15,110,100);
		frame.add(label2048);
		
		
		//set score
		score.setBackground(BACKGROUND);
		score.setOpaque(true);
		score.setForeground(Color.white);
		score.setFont(new Font("Lithograph",Font.BOLD, 25));
		score.setBounds(150,15,110,60);
		frame.add(score);
		
		//set best score
		best.setBackground(BACKGROUND);
		best.setOpaque(true);
		best.setForeground(Color.white);
		best.setFont(new Font("Lithograph",Font.BOLD, 25));
		best.setBounds(290,15,110,60);
		frame.add(best);
		
		//set designer
		designer.setText("<html><center>Designer<br>Yuqian Chen<center><html>");
		designer.setForeground(Color.pink);
		designer.setFont(new Font("Lithograph",Font.BOLD, 15));
		designer.setBounds(290,80,110,40);
		frame.add(designer);
		
		//set button
		start.setText("New game");
		start.setBounds(150, 80, 110, 40);
		frame.add(start);
		start.setFocusable(false);
		start.addActionListener( this );


        frame.add(layout);
		frame.setVisible(true);
	}

	public boolean merge(int rowN, int colN, int horizontal, int vertical) {
		boolean legalMove = false;
		int currV = tile[rowN][colN].getValue();
		int newR = rowN;
		int newC = colN;
		boolean check = false;
		while (!tile[rowN][colN].getupdate() && !check) {
			newR += vertical;
			newC += horizontal;
			try {
				if (currV == tile[newR][newC].getValue()
						&& canCombine(rowN, colN, newR, newC)) {
					// update doubles
					totalTiles--;
					count += currV*2;
					legalMove = true;
					if (currV * 2 == 2048){
						gamewin = true;
					}
					setBkgdColor(tile[rowN][colN].getLabel(), currV * 2);
					tile[rowN][colN].setLabel(currV * 2);
					tile[rowN][colN].setupdate(true);
					// clear
					setBkgdColor(tile[newR][newC].getLabel(), 0);
					tile[newR][newC].setLabel(0);
					tile[newR][newC].setupdate(true);

				}
			} catch (ArrayIndexOutOfBoundsException e) {
				check = true;
			}

		}
		return legalMove;
	}

	public boolean canCombine(int rowN, int colN, int newR, int newC) {
		if (rowN == newR) {
			if (Math.abs(colN - newC) == 1) {
				return true;
			}
			for (int i = Math.min(colN, newC) + 1; i < Math.max(colN, newC); i++) {
				if (tile[rowN][i].getValue() != 0) {
					return false;
				}
			}
			return true;
		} else {
			if (Math.abs(rowN - newR) == 1) {
				return true;
			}
			for (int i = Math.min(rowN, newR) + 1; i < Math.max(rowN, newR); i++) {
				if (tile[i][colN].getValue() != 0) {
					return false;
				}
			}
			return true;
		}
	}

	public boolean shift(int rowN, int colN, int horizontal, int vertical) {
		boolean legalMove = false;
		boolean check = false;
		int currV = tile[rowN][colN].getValue();
		int newR = rowN;
		int newC = colN;
		while (!check) {
			newR -= vertical;
			newC -= horizontal;
			try {
				if (tile[newR][newC].getValue() == 0) {
					legalMove = true;
					setBkgdColor(tile[rowN][colN].getLabel(), 0);
					tile[rowN][colN].setLabel(0);
					setBkgdColor(tile[newR][newC].getLabel(), currV);
					tile[newR][newC].setLabel(currV);
				} else {
					check = true;
				}
				rowN = newR;
				colN = newC;
			} catch (ArrayIndexOutOfBoundsException e) {
				check = true;
			}
		}
		return legalMove;
	}
	public void setBkgdColor(JLabel tile, int number) {
		int newC = (int) (Math.log(number) / Math.log(2)) - 7;
		switch (number) {
		case 0:
			tile.setBackground(DEFAULT_TILE);
			break;
		case 2:
			tile.setBackground(TWO);
			break;
		case 4:
			tile.setBackground(FOUR);
			break;
		case 8:
			tile.setBackground(EIGHT);
			break;
		case 16:
			tile.setBackground(SIXTEEN);
			break;
		case 32:
			tile.setBackground(THIRTYTWO);
			break;
		case 64:
			tile.setBackground(SIXTYFOUR);
			break;
		default: // up to 4096
			tile.setBackground(new Color(237, 204 + newC * 10, 97));
		}
	}
	public void tiles() {
		totalTiles++;
		Random position = new Random();
		int r = position.nextInt(row);
		int c = position.nextInt(col);
		while (tile[r][c].getValue() != 0) {
			r = position.nextInt(row);
			c = position.nextInt(col);
		}
		int two_four = position.nextInt(RANDOM);
		if (two_four % 2 == 0) {
			tile[r][c].getLabel().setBackground(TWO);
			tile[r][c].setLabel(2);
		} else {
			tile[r][c].getLabel().setBackground(FOUR);
			tile[r][c].setLabel(4);
		}
	}
	public boolean fullmove(){
		for (int i = 0; i < tile.length; i++) {
			for (int j = 0; j < tile[i].length; j++) {
				try {
					if (tile[i][j].getValue() == tile[i][j + 1].getValue()) {
						return true;
					}
				} catch (ArrayIndexOutOfBoundsException except) {}
				try {
					if (tile[i][j].getValue() == tile[i][j - 1].getValue()) {
						return true;
					}
				} catch (ArrayIndexOutOfBoundsException except) {}
				try {
					if (tile[i][j].getValue() == tile[i + 1][j].getValue()) {
						return true;
					}
				} catch (ArrayIndexOutOfBoundsException except) {}
				try {
					if (tile[i][j].getValue() == tile[i - 1][j].getValue()) {
						return true;
					}
				} catch (ArrayIndexOutOfBoundsException except) {}
			}
		}
		return false;
	}
	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		boolean canMerge = false;
		boolean canShift = false;
		boolean legalMove = false;

		// keycode left 37 right 39 up 38 down 40
		int code = e.getKeyCode();
		for (int i = 0; i < tile.length; i++) {
			for (int j = 0; j < tile[i].length; j++) {
				tile[i][j].setupdate(false);
			}
		}
		// merge
		if (code == 37) {
			for (int i = 0; i < row; i++) {
				for (int j = 0; j < col; j++) {
					if (tile[i][j].getValue() != 0) {
						canMerge = merge(i, j, 1, 0);
						canShift = shift(i, j, 1, 0);
						if (canMerge || canShift) {
							legalMove = true;
						}
					}
				}
			}
		} else if (code == 39) {
			for (int i = 0; i < row; i++) {
				for (int j = col - 1; j >= 0; j--) {
					if (tile[i][j].getValue() != 0) {
						canMerge = merge(i, j, -1, 0);
						canShift = shift(i, j, -1, 0);
						if (canMerge || canShift) {
							legalMove = true;
						}
					}
				}
			}
		} else if (code == 38) {
			for (int i = col - 1; i >= 0; i--) {
				for (int j = 0; j < row; j++) {
					if (tile[j][i].getValue() != 0) {
						canMerge = merge(j, i, 0, 1);
						canShift = shift(j, i, 0, 1);
						if (canMerge || canShift) {
							legalMove = true;
						}
					}
				}
			}
		} else if (code == 40) {
			for (int i = col - 1; i >= 0; i--) {
				for (int j = row - 1; j >= 0; j--) {
					if (tile[j][i].getValue() != 0) {
						canMerge = merge(j, i, 0, -1);
						canShift = shift(j, i, 0, -1);
						if (canMerge || canShift) {
							legalMove = true;
						}
					}
				}
			}
		}
		score.setText("<html><center>Score<br>"+Integer.toString(count)+"<center><html>");
		// new tiles showing
		if (legalMove) {
			tiles();
		}
		if (gamewin) {
			frame.removeKeyListener(this);
			text.setText("You win!");
			text.setForeground(Color.white);
			layout.moveToFront(text);
			
		}
		System.out.println("totalTiles!" + totalTiles);

		// check if dead
		if (totalTiles == 16) {

			if (!fullmove()) {
				System.out.println("gameover");
				frame.removeKeyListener(this);
				text.setText("Game over!");
				text.setForeground(new Color(119, 110, 101));
				text.setBackground(new Color(255,255,255,80));
				layout.moveToFront(text);
			}
		}
	}

	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		System.out.println("new game starts");
		frame.removeKeyListener(this);
		init();		
	}

}
