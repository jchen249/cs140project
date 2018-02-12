package projectview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import project.Instruction;
import project.Loader;
import project.MachineModel;
import project.Memory;

public class CodeViewPanel implements Observer{
	private MachineModel model;
	private Instruction instr;
	private JScrollPane scroller;
	private JTextField[] codeText= new JTextField[Memory.CODE_SIZE];
	private JTextField[] codeBinHex= new JTextField[Memory.CODE_SIZE];
	private int previousColor=-1;
	
	public CodeViewPanel(ViewMediator view, MachineModel m) {
		this.model=m;
		view.addObserver(this);
	}
	
	public JComponent createCodeDisplay() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		//creating boarder object
		Border border = BorderFactory.createTitledBorder(
		        BorderFactory.createLineBorder(Color.BLACK),
		        "Code Memory View",
		        TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
		//set border around panel
		panel.setBorder(border);
		
		JPanel innerPanel = new JPanel();// Set innerpanel to same border as panel or similar to that of panel?
		innerPanel.setLayout(new BorderLayout());
//		Border border1 = BorderFactory.createTitledBorder(
//		        BorderFactory.createLineBorder(Color.BLACK),
//		        "Data Memory View ["+ lower +"-"+ upper +"]",
//		        TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
		//innerPanel.setBorder(border);
		
		JPanel numPanel = new JPanel();
		numPanel.setLayout(new GridLayout(0,1));//1 column Grid layout
		
		JPanel decimalPanel= new JPanel();
		decimalPanel.setLayout(new GridLayout(0,1));
		
		JPanel hexPanel = new JPanel();
		hexPanel.setLayout(new GridLayout(0,1));
		JPanel textPanel= new JPanel();
		textPanel.setLayout(new GridLayout(0,1));
		innerPanel.add(numPanel, BorderLayout.LINE_START);
	//	innerPanel.add(decimalPanel, BorderLayout.CENTER);
		innerPanel.add(textPanel, BorderLayout.CENTER);
		innerPanel.add(hexPanel, BorderLayout.LINE_END);
		
		
		//instantiate codeBinHex and CodeText as JTextField array of len(upper-lower)


		for(int i=0; i<Memory.CODE_SIZE;i++) {
			numPanel.add(new JLabel(i+": ", JLabel.RIGHT));
			codeText[i]=new JTextField(10);
			codeBinHex[i]=new JTextField(12);
			textPanel.add(codeText[i]);
			hexPanel.add(codeBinHex[i]);

			
		}
		
		scroller = new JScrollPane(innerPanel);
		panel.add(scroller);
		//System.out.println("hi");
		return panel;
		
	}


	@Override
	public void update(Observable arg0, Object arg1) {
		if(arg1 != null && arg1.equals("Load Code")) {
			for(int i = 0; i <= model.getProgramSize(); i++) {
				instr = model.getCode(i);
				codeText[i].setText(instr.getText());
				codeBinHex[i].setText(instr.getBinHex());
			}	
			previousColor = model.getPC();			
			codeBinHex[previousColor].setBackground(Color.YELLOW);
			codeText[previousColor].setBackground(Color.YELLOW);
		} else if(arg1 != null && arg1 instanceof String 
				&& ((String)arg1).equals("Clear")) {
			for(int i = 0; i <= model.getProgramSize(); i++) {
				codeText[i].setText("");
				codeBinHex[i].setText("");
			}	
			if(previousColor >= 0 && previousColor < Memory.CODE_SIZE) {
				codeText[previousColor].setBackground(Color.WHITE);
				codeBinHex[previousColor].setBackground(Color.WHITE);
			}
			previousColor = -1;
		}		
		if(this.previousColor >= 0 && previousColor < Memory.CODE_SIZE) {
			codeText[previousColor].setBackground(Color.WHITE);
			codeBinHex[previousColor].setBackground(Color.WHITE);
		}
		previousColor = model.getPC();
		if(this.previousColor >= 0 && previousColor < Memory.CODE_SIZE) {
			codeText[previousColor].setBackground(Color.YELLOW);
			codeBinHex[previousColor].setBackground(Color.YELLOW);
		} 
		if(scroller != null && instr != null && model!= null) {
			JScrollBar bar= scroller.getVerticalScrollBar();
			int pc = model.getPC();
			if(pc >= 0 && pc < Memory.CODE_SIZE && codeBinHex[pc] != null) { 
				Rectangle bounds = codeBinHex[pc].getBounds();
				bar.setValue(Math.max(0, bounds.y - 15*bounds.height));	
			}
		}
	}
	public static void main(String[] args) throws FileNotFoundException, IOException {
		ViewMediator mediator = new ViewMediator(); 
		MachineModel model = new MachineModel();
		CodeViewPanel panel = new CodeViewPanel(mediator, model);
		JFrame frame = new JFrame("TEST");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 700);
		frame.setLocationRelativeTo(null);
		frame.add(panel.createCodeDisplay());
		frame.setVisible(true);
		System.out.println(Loader.load(model, new File("factorial.pexe")));
		panel.update(mediator, "Load Code");
	}

}
