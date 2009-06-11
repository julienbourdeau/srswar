package files;

import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Image;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * These are test routines. Don't mind it.
 * @author kbok
 */
public class Test {
	@SuppressWarnings("unused")
	private Image minimap;
	private Map map;
	
	private class ViewPanel extends JPanel
	{
		private static final long serialVersionUID = 1L;

		public void paint(Graphics g)
		{
			for(int y=0; y<8; y++)
				for(int x=0; x<8; x++)
					g.drawImage(map.getTile(x, y), x*64, y*64, null);
		}
	}
	
	public static void main(String[] args) throws IOException
	{
		Test t = new Test("/home/kbok/jmax_res/maps/Bottleneck.wrl");
		t.start();
	}
	
	private Test(String fn) throws IOException
	{
		map = new Map("/home/kbok/jmax_res/maps/Bottleneck.wrl");
	}
	
	private void start() throws IOException
	{
		map.readFully();
		minimap = map.getMiniMap();
		
		JFrame wnd = new JFrame();
		wnd.setPreferredSize(new Dimension(512, 512));
		wnd.getContentPane().add(new ViewPanel());
		wnd.pack();
		wnd.setVisible(true);
	}
}
