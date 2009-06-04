package viewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import files.GraphicsFile;
import files.IndexedImage;
import files.GraphicsFile.Slice;

public class ResourceViewer extends JFrame{
	private static final long serialVersionUID = 1L;
	private GraphicsFile resFile;
	private Vector<String> names;
	
	private JList list;
	private JScrollPane scrollPane;
	private ViewPort viewport;
	
	private class ViewPort extends JPanel{
		private static final long serialVersionUID = 1L;
		
		private JTextArea textArea;
		private JComponent curShowing;
		
		public ViewPort()
		{
			textArea = new JTextArea();
			add(textArea);
			textArea.setVisible(false);
		}
		
		public void setObject(String object)
		{
			try{
				String text = resFile.getString(object);
				if(curShowing != null) curShowing.setVisible(false);
				textArea.setText(text);
				textArea.setVisible(true);
				curShowing = textArea;
				return;
			} catch(Exception e){}
			
			try{
				IndexedImage[] img = resFile.getMultiImage(object);
				System.out.println("MultiImage");
				return;
			} catch(Exception e){}
			
			if(curShowing != null) curShowing.setVisible(false);
			textArea.setText("Unknown format");
			textArea.setVisible(true);
			curShowing = textArea;
		}
	}
	
	private class ListListener implements ListSelectionListener{
		int index = -1;
		
		public void valueChanged(ListSelectionEvent e) {
			if(e.getValueIsAdjusting()) return;
			int index = list.getLeadSelectionIndex();
			viewport.setObject(names.get(index));
		}	
	}
	
	public ResourceViewer(String filename) throws IOException{
		setTitle("M.A.X. Resource File Viewer");
		resFile = new GraphicsFile(new RandomAccessFile(filename, "r"));
		resFile.readIndex();
		names = resFile.getNames();
		
		Vector<String> formattedNames = new Vector<String>(names);
		for(int i=0; i<formattedNames.size(); i++)
			formattedNames.set(i, formattedNames.get(i).trim());
		
		list = new JList(formattedNames);
		scrollPane = new JScrollPane(list);
		getContentPane().add(scrollPane, BorderLayout.WEST);
		list.addListSelectionListener(new ListListener());
		
		viewport = new ViewPort();
		getContentPane().add(viewport, BorderLayout.CENTER);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(600, 500);
	}
	
	public static void main(String[] args) throws IOException
	{
		ResourceViewer viewer = new ResourceViewer("/home/kbok/jmax/MAX.RES");
		viewer.setVisible(true);
	}
}
