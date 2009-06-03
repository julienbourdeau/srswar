package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class OptionsPanel extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	protected JButton gridToggle;
	protected JButton radarToggle;
	protected JButton rangeToggle;
	protected JButton colorToggle;
	protected Options options;
	protected BoxLayout layout;
	
	public OptionsPanel()
	{
		layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(layout);
		
		gridToggle = new JButton("Toggle grid");
		gridToggle.setActionCommand("toggleGrid");
		gridToggle.addActionListener(this);
		this.add(gridToggle);
		
		rangeToggle = new JButton("Toggle range");
		rangeToggle.setActionCommand("toggleRange");
		rangeToggle.addActionListener(this);
		this.add(rangeToggle);
		
		radarToggle = new JButton("Toggle radar");
		radarToggle.setActionCommand("toggleRadar");
		radarToggle.addActionListener(this);
		this.add(radarToggle);
		
		colorToggle = new JButton("Toggle color");
		colorToggle.setActionCommand("toggleColor");
		colorToggle.addActionListener(this);
		this.add(colorToggle);
	}
	
	public void setOptions(Options o)
	{
		options = o;
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("toggleGrid"))
		{
			options._grid = !options._grid;
		} else if(e.getActionCommand().equals("toggleRange"))
		{
			options._range = !options._range;
		} else if(e.getActionCommand().equals("toggleRadar"))
		{
			options._scanner = !options._scanner;
		} else if(e.getActionCommand().equals("toggleColor"))
		{
			options._color = !options._color;
		}
	}

}
