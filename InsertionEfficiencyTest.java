public class InsertionEfficiencyTest {

	public static void main(String[] args) {
		final int LOOP_COUNT = 5;
		final int I_MAX = 30000000;
		System.out.println("Time efficiency test for inserting rules:");

		// Test with randomly built rules.
		for (int i = 64000; i < I_MAX; i *= 2) {
			long totalTime = 0;
			for (int j = 0; j < LOOP_COUNT; j++) {
				long startTime = System.nanoTime();
				RandomCFGGenerator.createRandomCFG(i);
				totalTime += System.nanoTime() - startTime;
			}
			System.out.println(
					"No. of inserted rules: " + i + ", Average time: " 
					+ (totalTime / 1000000 / LOOP_COUNT) + " ms");
		}
		System.out.println();
	}
}