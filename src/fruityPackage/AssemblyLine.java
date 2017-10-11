package fruityPackage;

import java.util.ArrayList;
import java.util.List;

//class file and formatting pulled from Nathan Williams during lecture, minor changes from Andrew Kerwin
//Additional assistance with concepts from Cordell Andersen
public class AssemblyLine {
	private final List<Orange> oranges;

	// Constructor
	AssemblyLine() {
		oranges = new ArrayList<Orange>();
	}

	// Orange is added to Assembly Line
	public synchronized void addOrange(Orange o) {
		oranges.add(o);
		if (countOranges() == 1) {
			try {
				notifyAll();
			} catch (Exception e) {
			}
		}
	}

	// Orange is removed from list so that it isn't stuck in first line
	public synchronized Orange getOrange() {
		while (countOranges() == 0) {
			try {
				this.wait();
			} catch (InterruptedException ignored) {
			}
		}
		return oranges.remove(0);
	}

	// Provides count of current oranges
	public synchronized int countOranges() {
		return oranges.size();
	}

}
