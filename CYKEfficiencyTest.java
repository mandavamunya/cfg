import java.util.Random;

public class CYKEfficiencyTest {

	public static void main(String[] args) {
		final int LOOP_COUNT = 15;
		final int I_MAX = 4096;
		
		CFG cfg = new CFG();
		cfg.addRule("S -> AA");
		cfg.addRule("S -> a");
		cfg.addRule("S -> b");
		cfg.addRule("A -> a");
		cfg.addRule("A -> b");
		cfg.addRule("A -> AA");
		System.out.println("Time efficiency test on the following CFG:");
		System.out.println(cfg.toString());
		System.out.println();

		// Test words with random letters.
		System.out.println("Test with words containing random letters");
		for (int i = 128; i <= I_MAX; i *= 2) {
			long totalTime = 0;
			Random random = new Random();
			for (int j = 0; j < LOOP_COUNT; j++) {
				char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
				StringBuilder word = new StringBuilder();
				for (int k = 0; k < i; k++) {
					char c = chars[random.nextInt(chars.length)];
					word.append(c);
				}
				long startTime = System.nanoTime();
				cfg.derives(word.toString());
				totalTime += System.nanoTime() - startTime;
			}
			System.out.println("Word length: " + i + ", Average time: "
					+ (totalTime / 1000000 / LOOP_COUNT) + " ms");
		}
		System.out.println();

		// Test words that consist entirely of the letter "a".
		System.out.println("Test with words containing only 'a'");
		for (int i = 128; i <= I_MAX; i *= 2) {
			long totalTime = 0;
			for (int j = 0; j < LOOP_COUNT; j++) {
				StringBuilder word = new StringBuilder();
				for (int k = 0; k < i; k++) {
					word.append("a");
				}
				long startTime = System.nanoTime();
				cfg.derives(word.toString());
				totalTime += System.nanoTime() - startTime;
			}
			System.out.println("Word length: " + i + ", Average time: "
					+ (totalTime / 1000000 / LOOP_COUNT) + " ms");
		}
		System.out.println();

		// Test words that consist entirely of the letter "c".
		System.out.println("Test with words containing only 'c'");
		for (int i = 128; i <= I_MAX; i *= 2) {
			long totalTime = 0;
			for (int j = 0; j < LOOP_COUNT; j++) {
				StringBuilder word = new StringBuilder();
				for (int k = 0; k < i; k++) {
					word.append("c");
				}
				long startTime = System.nanoTime();
				cfg.derives(word.toString());
				totalTime += System.nanoTime() - startTime;
			}
			System.out.println("Word length: " + i + ", Average time: "
					+ (totalTime / 1000000 / LOOP_COUNT) + " ms");
		}
		System.out.println();

	}

}