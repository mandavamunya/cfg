public class EmptinessEfficiencyTest {

	public static void main(String[] args) {
		final int LOOP_COUNT = 15;
		final int I_MAX = 4096000;
		CFG g = new CFG();
		System.out.println("Time efficiency test on deciding emptiness.");

		System.out.println("\nTesting chain rules excluding S -> S0:");
		for (int i = 64000; i <= I_MAX; i *= 2) {
			long totalTime = 0;
			for (int j = 0; j < LOOP_COUNT; j++) {
				g = new CFG();
				int k = 0;
				for (; g.size() < i; k++) {
					g.addRule("S" + k + " -> S" + (k + 1));
				}
				g.addRule("S" + k + " -> a");
				long startTime = System.nanoTime();
				g.languageIsEmpty();
				totalTime += System.nanoTime() - startTime;
			}
			System.out.print("Size of the CFG: " + i);
			System.out.print(", Average time: ");
			System.out.println((totalTime / 1000000 / LOOP_COUNT) + " ms.");
		}

		System.out.println("\nTesting chain rules including S -> S0:");
		for (int i = 64000; i <= I_MAX; i *= 2) {
			long totalTime = 0;
			for (int j = 0; j < LOOP_COUNT; j++) {
				g = new CFG();
				int k = 0;
				g.addRule("S -> S0");
				for (; g.size() < i; k++) {
					g.addRule("S" + k + " -> S" + (k + 1));
				}
				g.addRule("S" + k + " -> a");
				long startTime = System.nanoTime();
				g.languageIsEmpty();
				totalTime += System.nanoTime() - startTime;
			}
			System.out.print("Size of the CFG: " + i);
			System.out.print(", Average time: ");
			System.out.println((totalTime / 1000000 / LOOP_COUNT) + " ms.");
		}

		System.out.print("\nTesting chain rules excluding S -> S0 and with ");
		System.out.println("no terminal:");
		for (int i = 64000; i <= I_MAX; i *= 2) {
			long totalTime = 0;
			for (int j = 0; j < LOOP_COUNT; j++) {
				g = new CFG();
				int k = 0;
				for (; g.size() < i; k++) {
					g.addRule("S" + k + " -> S" + (k + 1));
				}
				long startTime = System.nanoTime();
				g.languageIsEmpty();
				totalTime += System.nanoTime() - startTime;
			}
			System.out.print("Size of the CFG: " + i);
			System.out.print(", Average time: ");
			System.out.println((totalTime / 1000000 / LOOP_COUNT) + " ms.");
		}

		System.out.println("\nTesting a randomly built grammar:");
		for (int i = 64000; i <= I_MAX; i *= 2) {
			long totalTime = 0;
			for (int j = 0; j < LOOP_COUNT; j++) {
				g = RandomCFGGenerator.createRandomCFG(i);
				long startTime = System.nanoTime();
				g.languageIsEmpty();
				totalTime += System.nanoTime() - startTime;
			}
			System.out.print("Size of the CFG: " + i);
			System.out.print(", Average time: ");
			System.out.println((totalTime / 1000000 / LOOP_COUNT) + " ms.");
		}
	}
}
