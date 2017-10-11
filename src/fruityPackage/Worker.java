package fruityPackage;

//Worker class made by Andrew Kerwin with some plant class features made by Nathan Williams
public class Worker implements Runnable {

	// We make the lines each worker needs to know about (which ain't all)
	AssemblyLine assemblylinein;
	AssemblyLine assemblylineout;

	// Creation of worker thread
	private final Thread workerThread;

	// Worker variable for them to operate
	private volatile boolean getToWork;

	// Give the workers their lines in constructor
	Worker(AssemblyLine in, AssemblyLine out) {
		workerThread = new Thread(this, "Worker");
		assemblylinein = in;
		assemblylineout = out;
	}

	// Worker begins their tasks
	public void startWorker() {
		getToWork = true;
		workerThread.start();
		// System.out.println("Worker initiated.");
	}

	// Run method for the workers, with assembly lines initialized and assigned
	// they may begin
	public void run() {

		// System.out.println(workerThread.getName() +
		// ", GET TO WORK!!!!!!!!!!!!!!!");

		while (getToWork) {

			Orange o = assemblylinein.getOrange();
			// System.out.println("I have my work.");

			// Remember that runProcess method also has the validation for
			// processed oranges
			o.runProcess();
			// System.out.println("I did my work.");

			assemblylineout.addOrange(o);
			// System.out.println("I sent my work.");
		}
	}

	// Worker variable is false, no more work done
	public void stopWorker() {
		getToWork = false;
	}

	// Waits for worker threads to cease (named differently from other get to
	// stop for diagnostic purposes)
	public void wWaitToStop() {
		try {
			workerThread.join();
			// System.out.println("waiting");
		} catch (Exception e) {
			System.err.println(workerThread.getName() + " needs a break, man.");
		}
	}

}