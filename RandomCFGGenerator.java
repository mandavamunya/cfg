import java.util.Random;

public class RandomCFGGenerator {

	/**
	 * Create and return a CFG with a specific number of random rules with body
	 * length 4.
	 */
	public static CFG createRandomCFG(final int GRAMMAR_SIZE) {
		CFG g = new CFG();
		final char[] nonterminals = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		final char[] terminals = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		Random random = new Random();
		while (g.size() < GRAMMAR_SIZE) {
			StringBuilder rule = new StringBuilder();
			rule.append(nonterminals[random.nextInt(nonterminals.length)]);
			rule.append(" -> ");
			int bodyLength = random.nextInt(30) + 1;
			for (int l = 0; l < bodyLength; l++) {
				if (random.nextBoolean()) {
					rule.append(terminals[random.nextInt(terminals.length)]);
				} else {
					rule.append(nonterminals[random.nextInt(nonterminals.length)]);
				}
			}
			g.addRule(rule.toString());
		}
		return g;
	}
}
