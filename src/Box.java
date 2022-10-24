import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class Box extends JPanel
{

	private JLabel text;
	
	public Box() 
	{
		text = new JLabel();
		
		this.setOpaque(false);
		this.setBorder(BorderFactory.createLineBorder(Color.black, 2));
		this.add(text);
	}

	public void setText(String input)
	{
		text.setText(input);
		text.setFont(new Font("Arial", Font.PLAIN, 48));
	}
	
	public void setColor(Color color)
	{
		this.setOpaque(true);
		this.setBackground(color);
	}
}