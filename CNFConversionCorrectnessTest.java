import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class CNFConversionCorrectnessTest {
	
	private void generate(StringBuilder sb, int n, ArrayList<String> words) {

		char[] alphabet = "ab".toCharArray();
		if (n == sb.length()) {
			words.add(sb.toString());
			return;
		}
		for (char letter : alphabet) {
			sb.setCharAt(n, letter);
			generate(sb, n + 1, words);
		}
	}

	@Test
	public void testThatEmptyGrammarStaysEmpty() {
		assertEquals(0, new CFG().toCNF().noOfRules());
	}

	@Test
	public void testThatGrammarWithSingleTerminalRuleStaysTheSame() {
		CFG g = new CFG();
		g.addRule("S -> a");
		g = g.toCNF();
		assertTrue(g.isInCNF());
		assertTrue(g.containsRule("S -> a"));
	}

	@Test
	public void testThatGrammarWithSingleEpsilonRuleStaysTheSame() {
		CFG g = new CFG();
		g.addRule("S -> epsilon");
		g = g.toCNF();
		assertTrue(g.isInCNF());
		assertTrue(g.containsRule("S -> epsilon"));
	}

	@Test
	public void testThatGrammarWithSingleNonterminalRuleBecomesEmpty() {
		CFG g = new CFG();
		g.addRule("S -> A");
		g = g.toCNF();
		assertTrue(g.isInCNF());
		assertEquals(g.noOfRules(), 0);
	}

	@Test
	public void testThatGrammarWithOnlyUnreachableSymbolsBecomesEmpty() {
		CFG g = new CFG();
		g.addRule("A -> A");
		g.addRule("A -> Bb");
		g.addRule("B -> Addf");
		g.addRule("A -> a");
		g.addRule("B -> bb");
		g.addRule("C -> Dg");
		g.addRule("D -> E");
		g.addRule("E -> efg");
		g = g.toCNF();
		assertTrue(g.isInCNF());
		assertEquals(g.noOfRules(), 0);
	}

	@Test
	public void testGrammarThatShouldDeriveAllCombinationsOfAAndB() {
		CFG g = new CFG();
		g.addRule("S -> a");
		g.addRule("S -> b");
		g.addRule("S -> SS");
		g = g.toCNF();
		assertTrue(g.isInCNF());

		ArrayList<String> words = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i <= 6; i++) {
			sb.setLength(i);
			generate(sb, 0, words);
		}
		for (String word : words) {
			assertTrue(g.derives(word));
		}
		
		assertFalse(g.derives("abc"));
		assertFalse(g.derives("abbada"));
		assertFalse(g.derives("cdefg"));
	}

	@Test
	public void testBalancedParenthesesGrammar() {
		CFG g = new CFG();
		g.addRule("S -> SS");
		g.addRule("S -> (S)");
		g.addRule("S -> A");
		g.addRule("A -> aA");
		g.addRule("A -> a");
		g = g.toCNF();
		assertTrue(g.isInCNF());
		assertTrue(g.derives("a"));
		assertTrue(g.derives("(a)"));
		assertTrue(g.derives("(aaa)"));
		assertTrue(g.derives("(((aaa)))"));
		assertTrue(g.derives("(a)(a)(a)"));
		assertTrue(g.derives("((a)(a))"));
		assertTrue(g.derives("(a)((aa)((aaa))(a))"));
		
		assertFalse(g.derives("("));
		assertFalse(g.derives(")"));
		assertFalse(g.derives("(((a))"));
		assertFalse(g.derives("((a)))"));
		assertFalse(g.derives(")a("));
		assertFalse(g.derives(")))aa((("));
		assertFalse(g.derives("(a)(a)("));
		assertFalse(g.derives("((a)((aa)))((a)((a))"));
	}

	@Test
	public void tesGrammarFromSipser() {
		CFG g = new CFG();
		g.addRule("S -> ASA");
		g.addRule("S -> aB");
		g.addRule("A -> B");
		g.addRule("A -> S");
		g.addRule("B -> b");
		g.addRule("B -> epsilon");
		g = g.toCNF();
		assertTrue(g.isInCNF());
		assertTrue(g.containsRule("S -> a"));
		assertTrue(g.containsRule("A -> a"));
		assertTrue(g.containsRule("A -> b"));
		assertTrue(g.containsRule("B -> b"));
		assertFalse(g.containsRule("S -> ASA"));
		assertFalse(g.containsRule("S -> aB"));
		assertFalse(g.containsRule("A -> B"));
		assertFalse(g.containsRule("A -> S"));
		assertFalse(g.containsRule("B -> epsilon"));
	}

	@Test
	public void testGrammarThatGeneratesMoreLeftAsThanRightBs() {
		CFG g = new CFG();
		g.addRule("S -> aSb");
		g.addRule("S -> a");
		g.addRule("S -> A");
		g.addRule("A -> a");
		g.addRule("A -> aA");
		g = g.toCNF();
		assertTrue(g.isInCNF());
		assertTrue(g.derives("a"));
		assertTrue(g.derives("aab"));
		assertTrue(g.derives("aaaaaabbbbb"));
		assertTrue(g.derives("aaaaaaaaaaaaaaaaaaaaaaaaaaab"));
		assertTrue(g.derives("aaaaaa"));
		assertFalse(g.derives("ab"));
		assertFalse(g.derives("aabb"));
		assertFalse(g.derives("aaaaabbbbbbbbbbbb"));
		assertFalse(g.derives("b"));
		assertFalse(g.derives("ba"));
		assertFalse(g.derives("aba"));
	}

	@Test
	public void testGrammarThatGeneratesEqualNoOfAsAndBsThenAnyNoOfCs() {
		CFG g = new CFG();
		g.addRule("S -> XY");
		g.addRule("X -> aXb");
		g.addRule("X -> epsilon");
		g.addRule("Y -> cY");
		g.addRule("Y -> epsilon");
		g = g.toCNF();
		assertTrue(g.isInCNF());
		assertTrue(g.derives("abc"));
		assertTrue(g.derives("abcccccccc"));
		assertTrue(g.derives("aaabbbc"));
		assertTrue(g.derives("aabb"));
		assertTrue(g.derives("ccccc"));
		assertFalse(g.derives("a"));
		assertFalse(g.derives("b"));
		assertFalse(g.derives("ac"));
		assertFalse(g.derives("bc"));
		assertFalse(g.derives("aaaabbbbbc"));
		assertFalse(g.derives("aaaabbbcccccccc"));
	}

}
