import java.util.ArrayList;

public class FinitenessEfficiencyTest {
	private static ArrayList<String> words;

	private static void generate(StringBuilder sb, int n, ArrayList<String> words) {

		char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		if (n == sb.length()) {
			words.add(sb.toString());
			return;
		}
		for (char letter : alphabet) {
			sb.setCharAt(n, letter);
			generate(sb, n + 1, words);
		}
	}

	public static void main(String[] args) {
		final int LOOP_COUNT = 15;
		final int I_MAX = 48000;
		createWords();
		CFG g = new CFG();
		System.out.println("Time efficiency test on deciding finiteness.");

		System.out.println("\nTesting best-case grammar:");
		for (int i = 1500; i <= I_MAX * 16; i *= 2) {
			long totalTime = 0;
			for (int j = 0; j < LOOP_COUNT; j++) {
				g = new CFG();
				int k = 1000000;
				for (; g.size() < i; k++) {
					g.addRule("S" + k + " -> S" + (k + 1) + "S" + (k + 1));
				}
				long startTime = System.nanoTime();
				g.languageIsFinite();
				totalTime += System.nanoTime() - startTime;
			}
			System.out.print("Size of the CFG: " + i);
			System.out.print(", Average time: ");
			System.out.println((totalTime / 1000000 / LOOP_COUNT) + " ms.");
		}

		System.out.println("\nTesting worst-case grammar:");
		for (int i = 1500; i <= I_MAX; i *= 2) {
			long totalTime = 0;
			for (int j = 0; j < LOOP_COUNT; j++) {
				g = new CFG();
				g.addRule("S -> S0");
				int k = 0;
				for (; g.size() < i / 2; k++) {
					g.addRule("S" + k + " -> S" + (k + 1));
				}
				for(int l = 0; g.size() < i; l++) {
					g.addRule("S" + k + " -> " + words.get(l));
				}
				long startTime = System.nanoTime();
				g.languageIsFinite();
				totalTime += System.nanoTime() - startTime;
			}
			System.out.print("Size of the CFG: " + i);
			System.out.print(", Average time: ");
			System.out.println((totalTime / 1000000 / LOOP_COUNT) + " ms.");
		}

		System.out.println("\nTesting a random grammar:");
		for (int i = 1500; i <= I_MAX * 16; i *= 2) {
			long totalTime = 0;
			for (int j = 0; j < LOOP_COUNT; j++) {
				g = RandomCFGGenerator.createRandomCFG(i);
				long startTime = System.nanoTime();
				g.languageIsFinite();
				totalTime += System.nanoTime() - startTime;
			}
			System.out.print("Size of the CFG: " + i);
			System.out.print(", Average time: ");
			System.out.println((totalTime / 1000000 / LOOP_COUNT) + " ms.");
		}
	}
	
	public static void createWords() {
		words = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i <= 3; i++) {
			sb.setLength(i);
			generate(sb, 0, words);
		}
	}
}
