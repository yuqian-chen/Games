import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.border.MatteBorder;

public class grid {
	private final Font font = new Font("Lithograph", Font.BOLD, 30);
	private JLabel label = new JLabel("",JLabel.CENTER);
	private int value;
	boolean update;
	
	public grid(){
		this.label.setOpaque(true);
		this.label.setBorder(new MatteBorder(5, 5, 5, 5, new Color(187, 173, 160)));
		this.label.setFont(font);
		this.label.setSize(90, 90);
		update = false;
	}
	
	public JLabel getLabel(){
		return label;
	}
	public void setLabel(int value){
		if(value ==  0){
			this.label.setText("");
		}else{
			this.label.setText(Integer.toString(value));
		}
		this.value = value;
		if (value <= 4) {
			label.setForeground(new Color(119, 110, 101));
		} else {
			label.setForeground(new Color(249, 246, 242));
		}
	}
	public int getValue(){
		return value;
	}
	public boolean getupdate(){
		return update;
	}
	public void setupdate(boolean update){
		 this.update = update;
	}
	
	
}
