package projectview;

import javax.swing.Timer;


public class StepControl {
	private static final int TICK=500;
	private boolean autoStepOn= false;
	private Timer timer;
	private ViewMediator mediator;
	
	public StepControl(ViewMediator med) {
		this.mediator=med;

	}

	boolean isAutoStepOn() {
		return autoStepOn;
	}

	void setAutoStepOn(boolean autoStepOn) {
		this.autoStepOn = autoStepOn;
	}
	
	void toggleAutoStep() {
		autoStepOn=!autoStepOn;
	}
	
	void setPeriod(int period) {
		timer.setDelay(period);
	}
	
	void start() {//method from chapter 10 of the textbook on listeners
		timer= new Timer(TICK, e->  {if(autoStepOn) mediator.step();});
		timer.start();
	}

}
