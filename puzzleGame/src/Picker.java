import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Picker implements ActionListener {
	String[] name = { "lol", "painting", "mhxy", "cartoon" };;
	int index = 0;
	int diff = 0;
	JButton[] theme, level;
	JPanel holder,container;
	boolean showPuzzle = false;

	public Picker() {
		theme = new JButton[4];
		level = new JButton[4];
		holder = new JPanel();
		container = new JPanel();

		holder.setLayout(new GridLayout(2, 2));
		holder.setBounds(20, 90, 600, 450);
		holder.setOpaque(false);
		
		container.setLayout(new GridLayout(1, 3));
		container.setBounds(80, 40, 480, 40);
		container.setOpaque(false);
		
		
		for (int i = 0; i < 4; i++) {
			Image image = new ImageIcon(getClass().getResource(
					"/" + name[i] + ".jpg")).getImage();
			theme[i] = new JButton(new ImageIcon(image.getScaledInstance(290,
					217, Image.SCALE_SMOOTH)));
			theme[i].addActionListener(this);
			holder.add(theme[i]);
		}
		
		level[0] = new JButton("Easy 4 X 5");
		level[1] = new JButton("Medium 5 X 6");
		level[2] = new JButton("Hard 6 X 8");
		level[3] = new JButton("Ready!");

		for (int i = 0; i < 4; i++) {
			level[i].addActionListener(this);
			container.add(level[i]);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(level[0])) {
			diff = 0;
		}else if (e.getSource().equals(level[1])) {
			diff = 1;
		}else if (e.getSource().equals(level[2])) {
			diff = 2;
			
		}else if(e.getSource().equals(level[3])){
				holder.removeAll();
				container.removeAll();
				showPuzzle = true;			
		}else{
			for (int i = 0; i < 4; i++) {
				theme[i].removeActionListener(this);
				if (e.getSource().equals(theme[i])) {
					index = i;
					holder.removeAll();
					holder.setLayout(null);
					holder.revalidate();
					holder.repaint();
					JLabel cover = new JLabel(new ImageIcon(getClass().getResource(
							"/" + name[i] + ".jpg")));
					cover.setBounds(0, 0, 600, 450);
					holder.add(cover);
				}
			}
		}

	}

}
