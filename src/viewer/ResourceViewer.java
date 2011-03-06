package viewer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import files.GraphicsFile;
import files.GraphicsFile.Slice;
import files.IndexedImage;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;

public class ResourceViewer extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private GraphicsFile resourceFile;
    private String[] names;
    private JTable table;
    private JScrollPane scrollPane;
    private Viewport viewport;
    private JMenuBar menuBar;
    private SliceModel tableModel = new SliceModel();
    final JFileChooser fileChooser = new JFileChooser();

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Open...")) {
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                loadFile(fileChooser.getSelectedFile());
            }
        }
        if (e.getActionCommand().equals("Exit")) {
            dispose();
        }
    }

    private void loadFile(File file) {
        try {
            resourceFile = new GraphicsFile(new RandomAccessFile(file, "r"));
            resourceFile.readIndex();
            tableModel.update();
        } catch (Exception ex) {
            Logger.getLogger(ResourceViewer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private class SelectionListener implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            int index = e.getFirstIndex();
            if (table.isCellSelected(index, 0)) {
                loadResource(index);
        
            }
        }
    }

    private class Viewport extends JPanel {

        private static final long serialVersionUID = 1L;
        private JTextArea textArea;
        private JComponent curShowing;

        public Viewport() {
            textArea = new JTextArea();
            add(textArea);
            textArea.setVisible(false);
        }

        public void setObject(String object) {
            try {
                String text = resourceFile.getString(object);
                if (curShowing != null) {
                    curShowing.setVisible(false);
                }
                textArea.setText(text);
                textArea.setVisible(true);
                curShowing = textArea;
                return;
            } catch (Exception e) {
            }

            try {
                IndexedImage[] img = resourceFile.getMultiImage(object);
                System.out.println("MultiImage");
                return;
            } catch (Exception e) {
            }

            if (curShowing != null) {
                curShowing.setVisible(false);
            }
            textArea.setText("Unknown format");
            textArea.setVisible(true);
            curShowing = textArea;
        }
    }

    private class SliceModel extends AbstractTableModel {

        private final String[] columns = {"Name", "Size", "Type"};
        private final Class[] classes = {String.class, Integer.class, String.class};
        private ArrayList<Slice> info = null;

        public int getRowCount() {
            if (info == null) {
                return 0;
            }
            return info.size();
        }

        public int getColumnCount() {
            return 3;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return info.get(rowIndex).getNameString();
                case 1:
                    return info.get(rowIndex).getSize();
                case 2:
                    return "";
                default:
                    return null;
            }
        }

        @Override
        public String getColumnName(int index) {
            return columns[index];
        }

        @Override
        public Class getColumnClass(int index) {
            return classes[index];
        }

        private void update() {
            info = resourceFile.getSliceInfo();
            fireTableDataChanged();
        }
    }

    private void loadResource(int index) {
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    public ResourceViewer() {
        setTitle("M.A.X. Resource File Viewer");

        menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menu.add(createMenuItem("Open..."));
        menu.add(createMenuItem("Exit"));
        menuBar.add(menu);
        setJMenuBar(menuBar);

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(new SelectionListener());
        scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.WEST);

        viewport = new Viewport();
        getContentPane().add(viewport, BorderLayout.CENTER);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 600);

        fileChooser.addChoosableFileFilter(new FileFilters.ResourceFileFilter());
    }

    private JMenuItem createMenuItem(String name) {
        JMenuItem menuItem = new JMenuItem(name);
        menuItem.addActionListener(this);
        return menuItem;
    }

    public static void main(String[] args) throws IOException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) { /* Yeah, no big deal ;-) */}
        
        ResourceViewer viewer = new ResourceViewer();
        viewer.setVisible(true);
    }
}
