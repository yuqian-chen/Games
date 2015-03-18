import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Random;

import javax.swing.*;

public class Puzzle extends Thread implements ActionListener {
	public static int row = 4;
	public static int col = 5;
	public static JFrame frame;
	public static JLayeredPane layer = new JLayeredPane();
	public static JLabel background;
	public static Image org;
	public static Picker theme;
	public static puzzlePieces puzzleBoard;
	public static JLabel before;
	public JLabel message;
	public int timeRemaining = 120;
	public Timer countdownTimer;
	public JButton[] menu;
	public JPanel menuPanel;

	public static void main(String args[]) throws IOException {
		Puzzle main = new Puzzle();
		frame.setVisible(true);
		main.select();
	}

	public Puzzle() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.LIGHT_GRAY);
		frame.setPreferredSize(new Dimension(640, 580));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		message = new JLabel("", JLabel.CENTER);
		message.setBounds(0, 10, 640, 30);
		message.setFont(new Font("Lithograph", Font.BOLD, 20));
		message.setForeground(Color.gray);
		menuPanel = new JPanel();
		menuPanel.setLayout(new GridLayout(1, 3));
		menu = new JButton[3];
		menu[0] = new JButton("Restart");
		menu[1] = new JButton("New Puzzle");
		menu[2] = new JButton("Main Menu");
		for (int i = 0; i < 3; i++) {
			menu[i].addActionListener(this);
			menuPanel.add(menu[i]);
		}
		menuPanel.setBounds(140, 40, 360, 40);
		menuPanel.setOpaque(false);

		layer.add(message);
		background = new JLabel(new ImageIcon(getClass().getResource(
				"/background.jpg")));
		background.setBounds(0, 0, 640, 580);
		layer.add(background);
		frame.add(layer);

	}

	public void select() {
		theme = new Picker();
		layer.add(theme.holder, 0);
		layer.add(theme.container, 0);
		message.setText("Please select a theme and a Level");
		Thread worker = new Thread() {
			public void run() {
				while (!theme.showPuzzle) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						if (theme.diff == 0) {
							row = 4;
							col = 5;
							timeRemaining = 60;
						} else if (theme.diff == 1) {
							row = 5;
							col = 6;
							timeRemaining = 90;
						} else if (theme.diff == 2) {
							row = 6;
							col = 8;
							timeRemaining = 180;
						}
						layer.revalidate();
						layer.repaint();
						layer.add(menuPanel, 0);
						selectImage();
					}
				});
			}
		};
		worker.start();

	}

	public void selectImage() {
		menu[0].setEnabled(false);
		menu[1].setEnabled(false);
		menu[2].setEnabled(false);
		Random num = new Random();
		org = new ImageIcon(getClass().getResource(
				"/" + theme.name[theme.index] + "/" + num.nextInt(10) + ".jpg"))
				.getImage();
		org = org.getScaledInstance(600, 450, Image.SCALE_SMOOTH);
		showPuzzle();

	}

	public void showPuzzle() {
		before = new JLabel(new ImageIcon(org));
		before.setBounds(20, 90, 600, 450);
		layer.add(before, 0);
		Thread worker = new Thread() {
			public void run() {
				int review = 5;
				while (review > 0) {
					message.setText("you have " + review + " seconds to review");
					review--;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				layer.remove(before);
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						menu[0].setEnabled(true);
						menu[1].setEnabled(true);
						menu[2].setEnabled(true);
						puzzleBoard = new puzzlePieces(row, col, org,
								timeRemaining);
						layer.add(puzzleBoard.holder, 0);
						countdownTimer = new Timer(1000,
								new CountdownTimerListener());
						countdownTimer.start();
						message.setText("");
					}
				});
			}
		};

		worker.start();

	}

	public void actionPerformed(ActionEvent e) {
		layer.remove(puzzleBoard.holder);
		message.setForeground(Color.gray);
		if (theme.diff == 0) {
			timeRemaining = 60;
		} else if (theme.diff == 1) {
			timeRemaining = 90;
		} else {
			timeRemaining = 180;
		}
		if (e.getSource().equals(menu[0])) {
			menu[0].setEnabled(false);
			menu[1].setEnabled(false);
			menu[2].setEnabled(false);
			countdownTimer.stop();
			showPuzzle();
		} else if (e.getSource().equals(menu[1])) {
			countdownTimer.stop();
			selectImage();
		} else if (e.getSource().equals(menu[2])) {
			countdownTimer.stop();
			layer.remove(menuPanel);
			layer.revalidate();
			layer.repaint();
			select();

		}
	}

	class CountdownTimerListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (timeRemaining > 0) {
				timeRemaining--;
				if (puzzleBoard.success) {
					message.setForeground(Color.red);
					message.setText("Success!");
					countdownTimer.stop();

				} else {
					if (timeRemaining < 10) {
						message.setForeground(Color.red);
					}
					if (timeRemaining % 60 < 10) {
						message.setText("0"
								+ String.valueOf(timeRemaining / 60) + ":0"
								+ String.valueOf(timeRemaining % 60));
					} else {
						message.setText("0"
								+ String.valueOf(timeRemaining / 60) + ":"
								+ String.valueOf(timeRemaining % 60));
					}
				}
			} else {
				message.setText("Time's up!");
				countdownTimer.stop();
			}
		}
	}

}
