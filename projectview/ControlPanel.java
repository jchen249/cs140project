package projectview;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class ControlPanel implements Observer{
	private ViewMediator mediator;
	private JButton stepButton = new JButton("Step");
	private JButton clearButton= new JButton("Clear");
	private JButton runButton= new JButton("Run");
	private JButton reloadButton= new JButton("Reload");
	
	public ControlPanel(ViewMediator med) {
		this.mediator=med;
		mediator.addObserver((Observer) this);
		
	}
	
	public JComponent createControlDisplay() {
		JPanel panel= new JPanel();
		panel.setLayout(new GridLayout(1,0));
		
		stepButton.setBackground(Color.WHITE);
		stepButton.addActionListener(e->mediator.step());
		panel.add(stepButton);
		
		clearButton.setBackground(Color.WHITE);
		clearButton.addActionListener(e->mediator.clear());
		panel.add(clearButton);
		
		runButton.setBackground(Color.WHITE);
		runButton.addActionListener(e->mediator.toggleAutoStep());
		panel.add(runButton);
		
		reloadButton.setBackground(Color.WHITE);
		reloadButton.addActionListener(e->mediator.reload());

		panel.add(reloadButton);
		
		JSlider slider = new JSlider(5, 1000);
		slider.addChangeListener(e-> mediator.setPeriod(slider.getValue()));//unwriten method
		panel.add(slider);
		
		return panel; //return update method when completed viewMediator
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		runButton.setEnabled(mediator.getCurrentState().getRunPauseActive());
		stepButton.setEnabled(mediator.getCurrentState().getStepActive());
		clearButton.setEnabled(mediator.getCurrentState().getClearActive());
		reloadButton.setEnabled(mediator.getCurrentState().getReloadActive());		
	}

//	@Override
//	public void update(Observable o, Object arg) {
//		// TODO Auto-generated method stub
//		
//	}
	

}
