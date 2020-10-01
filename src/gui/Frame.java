package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;

import database.DatabaseImplementation;
import database.IDatabase;
import database.IRepository;
import database.RepositoryImplementation;
import error_handling.ExceptionHandler;
import error_handling.IExceptionHandler;
import model.InformationResource;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Frame extends JFrame {

	private static Frame fr=null;
	
	private DefaultTreeModel treeModel;
	private ViewTree jTree;
	private JSplitPane tables;
	private JSplitPane jsp;
	private TablesView mainView,relView;
	private InformationResource ir;
	private IDatabase iDatabase;
	private JScrollPane sPane;
	private IExceptionHandler exceptionHandler;
	
	private Frame() {
		jTree = new ViewTree();
		iDatabase = new DatabaseImplementation();
		exceptionHandler = new ExceptionHandler();
	}

	public static Frame getInstance() {
		if(fr==null) {
			fr=new Frame();
			fr.initialiseFrame();
		}
		return fr;
	}

	private void initialiseFrame() {
		try {
			ir=iDatabase.loadResource();
			treeModel=new DefaultTreeModel(ir);
			jTree.setModel(treeModel);
			Toolkit kit = Toolkit.getDefaultToolkit();
			mainView = new TablesView(true);
			relView = new TablesView(false);
			sPane = new JScrollPane(jTree,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			tables = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mainView,relView);
			tables.setDividerLocation(tables.getHeight()/2);
			jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sPane,tables);
			Dimension screenSize = kit.getScreenSize();
	        int screenHeight = screenSize.height;
	        int screenWidth = screenSize.width;
	        this.add(jsp);
	        setSize(screenWidth / 2, screenHeight / 2);
	        setTitle("SUBP");
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setLocationRelativeTo(null);
	        this.addComponentListener(new ComponentAdapter() {
	            public void componentResized(ComponentEvent componentEvent) {
	            	tables.setDividerLocation(tables.getHeight()/2);
	            }
	        });
		}
		catch(Exception e) {
			exceptionHandler.handleException("Database not avalible!");
		}
    }
	
	public ViewTree getTree() {
		return jTree;
	}
	
	public TablesView getMainView() {
		return mainView;
	}
	
	public TablesView getRelView() {
		return relView;
	}
	
	public IDatabase getIDatabase() {
		return iDatabase;
	}
	
	public IExceptionHandler getIExceptionHandler() {
		return exceptionHandler;
	}
	
	public InformationResource getInformationResource() {
		return ir;
	}
	
	public void refreshData() {
		mainView.refreshData();
		relView.refreshData();
		this.repaint();
	}
}
