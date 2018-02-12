package projectview;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class MenuBarBuilder implements Observer{
	private JMenuItem assemble = new JMenuItem("Assemble Source...");
	private JMenuItem load = new JMenuItem("Load File...");
	private JMenuItem exit = new JMenuItem("Exit");
	private JMenuItem go = new JMenuItem("Go");
	private ViewMediator mediator;

	public MenuBarBuilder(ViewMediator view) {
		mediator = view;
		view.addObserver(this);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		assemble.setEnabled(mediator.getCurrentState().getAssembleFileActive());
		load.setEnabled(mediator.getCurrentState().getLoadFileActive());
		go.setEnabled(mediator.getCurrentState().getStepActive());
	}
	//creating file Menu
	public JMenu createFileMenu() {
		JMenu menu=new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);//ALT-F
		assemble.setMnemonic(KeyEvent.VK_M);
		assemble.setAccelerator(KeyStroke.getKeyStroke(//ALT-M CTRL-M
			KeyEvent.VK_M, ActionEvent.CTRL_MASK));
		assemble.addActionListener(e -> {
			try {
				mediator.assembleFile();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});//actionListener trigger when keys are pressed or clicked
		menu.add(assemble);
		//return menu
		load.setMnemonic(KeyEvent.VK_L);
		load.setAccelerator(KeyStroke.getKeyStroke(//ALT-L CTRL-L
			KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		load.addActionListener(e -> {
			try {
				mediator.loadFile();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});//actionListener trigger when keys are pressed or clicked
		menu.add(load);
		menu.addSeparator();
		exit.setMnemonic(KeyEvent.VK_E);
		exit.setAccelerator(KeyStroke.getKeyStroke(//ALT-E CTRL-E
			KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		exit.addActionListener(e -> mediator.exit());//actionListener trigger when keys are pressed or clicked
		menu.add(exit);
		return menu;
	}
	public JMenu createExecuteMenu() {
		JMenu menu=new JMenu("Execute");
		menu.setMnemonic(KeyEvent.VK_E);
		go.setMnemonic(KeyEvent.VK_G);
		go.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_G, ActionEvent.CTRL_MASK));
		go.addActionListener(e->mediator.execute());
		menu.add(go);
		return menu;
		
	}
}
