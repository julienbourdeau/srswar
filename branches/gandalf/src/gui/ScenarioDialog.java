package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import files.Scenario;

/**
 * @author kbok
 * Provides a Scenario-selection dialog with a list of scenarios and a description of the 
 * scenario. Fires an event with a Scenario object as argument.
 */
public class ScenarioDialog extends JDialog implements ListSelectionListener, ActionListener{
	private static final long serialVersionUID = 1L;
	
	protected JList sList;
	protected JLabel sDescr;
	protected JButton OKButton;
	protected String directory;
	protected File[] files;
	protected Scenario[] scenarios;
	protected Scenario curScenario;
	protected String[] names;
	protected String[] descr;
	
	protected Vector<ScenarioDialogListener> listeners;
	
	private class XMLFilter implements FilenameFilter{
		public boolean accept(File dir, String name) {
			if(name.endsWith(".xml")) return true;
			return false;
		}
	}

	/**
	 * Constructs a new ScenarioDialog using dir as the directory in which to find the scenario
	 * files. All files ending with .xml are considered as Scenario files.
	 * @param dir The directory in which to look for Scenario files.
	 * @throws IOException In case of an error reading directory.
	 */
	public ScenarioDialog(String dir) throws IOException
	{
		setTitle("Select a scenario");
		listeners = new Vector<ScenarioDialogListener>();
		
		directory = dir;
		readFiles();
		readContents();
		
		getContentPane().setLayout(new BorderLayout());
		
		sList = new JList(names);
		sList.addListSelectionListener(this);
		sDescr = new JLabel();
		OKButton = new JButton("OK");
		OKButton.setActionCommand("OK");
		OKButton.addActionListener(this);
		
		add(sList, BorderLayout.WEST);
		add(sDescr, BorderLayout.CENTER);
		add(OKButton, BorderLayout.SOUTH);
		
		setPreferredSize(new Dimension(300, 300));
		
		pack();
		setVisible(true);
	}
	
	private void readContents() throws IOException {
		scenarios = new Scenario[files.length];
		names = new String[files.length];
		descr = new String[files.length];
		
		for(int i=0; i<files.length; i++)
		{
			scenarios[i] = new Scenario(files[i]);
			names[i] = scenarios[i].getName();
			descr[i] = scenarios[i].getDescription();
		}
	}

	private void readFiles() {
		File dir = new File(directory);
		files = dir.listFiles(new XMLFilter());
		
	}

	public void valueChanged(ListSelectionEvent e) {
		sDescr.setText(descr[e.getLastIndex()]);
		curScenario = scenarios[e.getLastIndex()];
	}
	
	/**
	 * Adds a client listener to the client list. The clients are notified when the OK button
	 * is clicked and are provided with the Scenario object chosen.
	 * @param l The Listener to add.
	 */
	public void addClient(ScenarioDialogListener l)
	{
		listeners.add(l);
	}
	
	/**
	 * Removes a client from the client list. If a client appears several times in the list, it
	 * will be removed only once.
	 * @param l The listener to remove.
	 */
	public void removeClient(ScenarioDialogListener l)
	{
		listeners.remove(l);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("OK"))
		{
			for(int i=0; i<listeners.size(); i++)
				listeners.get(i).scenarioChosen(curScenario);
			setVisible(false);
		}
	}

}
