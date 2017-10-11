package fruityPackage;

//Plant Double Threaded class from moodle, coded by Nate Williams and edited heavily by Andrew Kerwin
//Additional assistance with concepts from Cordell Andersen
public class Plant implements Runnable {

	// How long do we want to run the juice processing
	public static final long PROCESSING_TIME = 5 * 1000;

	// How many manufactories
	private static final int NUM_PLANTS = 2;

	// Workers
	private static final int NUM_WORKERS = Orange.State.values().length - 1;

	// Remember, we want 1 more line than worker so each has an in + out
	private static final int NUM_LINES = NUM_WORKERS + 1;

	// Create thread for plants
	public final int ORANGES_PER_BOTTLE = 2;
	private final Thread thread;

	// Statistic ints
	public int orangesProvided;
	public int orangesUnused;
	public int orangesFetched;
	public int orangesPeeled;
	public int orangesSqueezed;
	public int orangesBottled;
	public int orangesProcessed;
	public int orangesQualityAssuranceManned;

	// Enables work when true (by default, yes)
	private volatile boolean timeToWork;

	// Our worker array
	public Worker[] workers;

	public static void main(String[] args) {

		// Startup the plants
		Plant[] plants = new Plant[NUM_PLANTS];
		for (int i = 0; i < NUM_PLANTS; i++) {
			plants[i] = new Plant();
			plants[i].startPlant();
		}

		// Give the plants time to do work
		delay(PROCESSING_TIME, "Plant malfunction");

		// Stop the plants and workers in each plant, and wait for them to
		// shutdown
		for (Plant p : plants) {
			for (Worker w : p.workers) {
				w.stopWorker();
				// System.out.println("Told him to stop");
			}

			p.stopPlant();

		}

		// Tells plants and workers in plants to wait to stop
		for (Plant p : plants) {
			for (Worker w : p.workers) {
				w.wWaitToStop();
				// System.out.println("Worker is done.");
			}
			p.waitToStop();
			// System.out.println("Plant stoppage detected.");
		}

		// Summarize the results (Provided is oranges handled while unused is
		// non-fetched (stage 1)
		int totalProvided = 0;
		int totalUnused = 0;
		int totalFetched = 0;
		int totalPeeled = 0;
		int totalJuiced = 0;
		int totalBottled = 0;
		int totalQualityAssuranceManned = 0;
		int totalProcessed = 0;
		int totalBottles = 0;
		int totalWasted = 0;
		for (Plant p : plants) {
			totalProvided += p.getProvidedOranges();
			totalUnused += p.getUnusedOranges();
			totalFetched += p.getFetchedOranges();
			totalPeeled += p.getPeeledOranges();
			totalJuiced += p.getSqueezedOranges();
			totalBottled += p.getBottledOranges();
			totalQualityAssuranceManned += p.getQualityAssuranceMannedOranges();
			// processed is kind of useless since we won't do work on a
			// processed orange
			totalProcessed += p.getProcessedOranges();
			totalBottles += p.getBottles();
			totalWasted += p.getWaste();
		}
		System.out.println("Total provided/processed = " + totalProvided + "/"
				+ totalProcessed);
		System.out.println("Created " + totalBottles
				+ " bottles of Juice, wasted " + totalWasted + " oranges");
		System.out.println("Advanced details: ");
		System.out.println("Total unused: " + totalUnused);
		System.out.println("Total fetched: " + totalFetched);
		System.out.println("Total peeled: " + totalPeeled);
		System.out.println("Total squeezed: " + totalJuiced);
		System.out.println("Total bottled: " + totalBottled);
		System.out.println("Total quality assurance manned: "
				+ totalQualityAssuranceManned);
		System.out.println("Total processed: " + totalProcessed);
	}

	// delay method for workers to work
	private static void delay(long time, String errMsg) {
		long sleepTime = Math.max(1, time);
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			System.err.println(errMsg);
		}
	}

	// making assembly line a class variable which we initialize in constructor
	AssemblyLine[] lines;

	// Plant constructor
	Plant() {

		// Statistics and thread creation for plants
		orangesProvided = 0;
		orangesUnused = 0;
		orangesFetched = 0;
		orangesPeeled = 0;
		orangesSqueezed = 0;
		orangesBottled = 0;
		orangesQualityAssuranceManned = 0;
		orangesProcessed = 0;
		thread = new Thread(this, "Plant");

		// initialized lines
		lines = new AssemblyLine[NUM_LINES];

		// creation of assembly lines for workers
		for (int i = 0; i < NUM_LINES; i++) {
			lines[i] = new AssemblyLine();
		}

		// initialized workers
		workers = new Worker[NUM_WORKERS];

		// creates workers and assigns them their lines
		for (int i = 0; i < NUM_WORKERS; i++) {
			workers[i] = new Worker(lines[i], lines[i + 1]);
			workers[i].startWorker();

		}

	}

	// Start plant thread which will provide oranges
	public void startPlant() {
		timeToWork = true;
		thread.start();
	}

	// run method will state plant is running and spawn oranges while timeToWork
	// is true
	public void run() {
		System.out.println(Thread.currentThread().getName()
				+ " is processing oranges.");
		while (timeToWork) {

			// defines orange to be created
			Orange orange = new Orange();

			// increments per orange made
			orangesProvided++;
			System.out.print(".");

			// places created orange on first line for first worker
			lines[0].addOrange(orange);
		}
		System.out.println("");
	}

	// stops plant operations
	public void stopPlant() {
		timeToWork = false;
	}

	// waits for plant to cease work
	public void waitToStop() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			System.err.println(thread.getName() + " stop malfunction");
		}
	}

	// Oranges that have been handled in any way
	public int getProvidedOranges() {
		return orangesProvided;
	}

	// Oranges in beginning state that don't get handled after provision
	public int getUnusedOranges() {
		return lines[0].countOranges();
	}

	public int getFetchedOranges() {
		return lines[1].countOranges();
	}

	public int getPeeledOranges() {
		return lines[2].countOranges();
	}

	public int getSqueezedOranges() {
		return lines[3].countOranges();
	}

	public int getBottledOranges() {
		return lines[4].countOranges();
	}

	public int getQualityAssuranceMannedOranges() {
		return lines[5].countOranges();
	}

	public int getProcessedOranges() {
		return lines[6].countOranges();
	}

	// Since oranges will not be technically "processed" we do Q/A'd oranges as
	// dividend
	public int getBottles() {
		return orangesQualityAssuranceManned / ORANGES_PER_BOTTLE;
	}

	// We want oranges unused since provided would be all handled oranges (which
	// isn't necessarily waste)
	public int getWaste() {
		return orangesUnused % ORANGES_PER_BOTTLE;
	}
}