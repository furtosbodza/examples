package task;

import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class Task {

	private int numberCount;
	private int maxNumber;
	private Random random = new Random();

	public Task(int numberCount, int maxNumber) {
		this.numberCount = numberCount;
		this.maxNumber = maxNumber;
	}

	public Set<Integer> processLottery() {
		Set<Integer> numbers = new TreeSet<>();
		while (numbers.size() < numberCount) {
			numbers.add(this.random.nextInt(maxNumber) + 1);
		}
		return numbers;
	}

}
