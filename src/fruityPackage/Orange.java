package fruityPackage;

//Orange class from moodle, coded by Nate Williams with minor edits from Andrew Kerwin
  public class Orange {
  
	//State of oranges
    public enum State {
    	Provided(1),
        Fetched(2),
        Peeled(3),
        Squeezed(4),
        Bottled(5),
        QualityAssuranceManned(6),
        Processed(7);

    	//logic for processed oranges
        private static final int finalIndex = State.values().length - 1;

        final int timeToComplete;

        //State may not change unless timeToComplete has passed
        State(int timeToComplete) {
            this.timeToComplete = timeToComplete;
        }

        //Selects next state unless processed
        State getNext() {
            int currIndex = this.ordinal();
            if (currIndex >= finalIndex) {
                throw new IllegalStateException("Already at final state");
            }
            return State.values()[currIndex + 1];
        }
    }

    //making state a class variable
    private State state;

    //ensures that oranges are started at provided state
    public Orange() {
        state = State.Provided;
        doWork();
    }

    //get method for value of state
    public State getState() {
        return state;
    }

    //do work plus validation so processed oranges are not handled
    public void runProcess() {
    	
        // Don't attempt to process an already completed orange
        if (state == State.Processed) {
            throw new IllegalStateException("This orange has already been processed");
        }
        doWork();
    }

    //workers take time to do the task at hand
    public void doWork() {
      
    	// Sleep for the amount of time necessary to do the work
    	try {
            Thread.sleep(state.timeToComplete);
        } catch (InterruptedException e) {
            System.err.println("Incomplete orange processing, juice may be bad");
        }
    	
    	//ensures that after time passes oranges have been handled by workers based on task
        state = state.getNext();
    	
    }
}
