import static org.junit.Assert.*;

import org.junit.Test;

public class FinitenessCorrectionTest {

	@Test
	public void testThatEmptyLanguageIsFinite() {
		CFG g = new CFG();
		assertTrue(g.languageIsFinite());
	}

	@Test
	public void testThatLanguageWithOnlyEpsilonIsFinite() {
		CFG g = new CFG();
		g.addRule("S -> epsilon");
		assertTrue(g.languageIsFinite());
	}

	@Test
	public void testThatLanguageWithOnlyOneTerminalRuleIsFinite() {
		CFG g = new CFG();
		g.addRule("S -> f");
		assertTrue(g.languageIsFinite());
	}

	@Test
	public void testThatChainedRuleGrammarIsFinite() {
		CFG g = new CFG();
		g.addRule("S -> A");
		g.addRule("A -> B");
		g.addRule("A -> a");
		g.addRule("B -> C");
		g.addRule("C -> c");
		assertTrue(g.languageIsFinite());
	}

	@Test
	public void testThatGrammarWithUnreachableCycleIsFinite() {
		CFG g = new CFG();
		g.addRule("S -> a");
		g.addRule("A -> aA");
		g.addRule("A -> a");
		assertTrue(g.languageIsFinite());
	}

	@Test
	public void testThatGrammarWhichLoopsSToItselfIsInfinite() {
		CFG g = new CFG();
		g.addRule("S -> aS");
		g.addRule("S -> a");
		assertFalse(g.languageIsFinite());
	}

	@Test
	public void testThatGrammarWithShortCycleIsInfinite() {
		CFG g = new CFG();
		g.addRule("S -> aA");
		g.addRule("A -> aA");
		g.addRule("A -> a");
		assertFalse(g.languageIsFinite());
	}

	@Test
	public void testThatGrammarWithLongCycleIsInfinite() {
		CFG g = new CFG();
		g.addRule("S -> Ab");
		g.addRule("A -> aB");
		g.addRule("B -> bCc");
		g.addRule("C -> dDe");
		g.addRule("D -> fAg");
		g.addRule("D -> h");
		g = g.removeEpsilonRules().removeUselessSymbols();
		System.out.println(g.toString());
		assertFalse(g.languageIsFinite());
	}

}
