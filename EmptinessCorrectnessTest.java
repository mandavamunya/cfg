import static org.junit.Assert.*;

import org.junit.Test;

public class EmptinessCorrectnessTest {

	@Test
	public void testEmptyGrammar() {
		CFG g = new CFG();
		assertTrue(g.languageIsEmpty());
	}

	@Test
	public void testMinimalNonemptyGrammar() {
		CFG g = new CFG();
		g.addRule("S -> f");
		assertFalse(g.languageIsEmpty());
	}

	@Test
	public void testGrammarWithGeneratingButDisconnectedRules() {
		CFG g = new CFG();
		g.addRule("S1 -> a");
		g.addRule("S2 -> b");
		g.addRule("S3 -> S4");
		g.addRule("S4 -> S5");
		g.addRule("S5 -> x");
		g.addRule("S4 -> S6");
		g.addRule("S6 -> y");
		assertTrue(g.languageIsEmpty());
	}

	@Test
	public void testGrammarWithConnectedButNongeneratingRules() {
		CFG g = new CFG();
		g.addRule("S -> S1");
		g.addRule("S1 -> S2");
		g.addRule("S3 -> S2");
		g.addRule("S3 -> S4");
		g.addRule("S4 -> w");
		assertTrue(g.languageIsEmpty());
	}

	@Test
	public void testGrammarWithReachableButNongeneratingRules() {
		CFG g = new CFG();
		g.addRule("S -> S1");
		g.addRule("S1 -> S1a");
		g.addRule("S -> S2");
		g.addRule("S2 -> Sb");
		g.addRule("S -> cS3d");
		assertTrue(g.languageIsEmpty());
	}

	@Test
	public void testGrammarWithChainedRules() {
		CFG g = new CFG();
		g.addRule("S -> S1");
		g.addRule("S1 -> S2");
		g.addRule("S2 -> S3");
		g.addRule("S3 -> S4");
		g.addRule("S4 -> abcde");
		assertFalse(g.languageIsEmpty());
	}
	
	@Test
	public void testGrammarFromReportSection6() {
		CFG g = new CFG();
		g.addRule("S -> SS");
		g.addRule("S -> ABA");
		g.addRule("S -> epsilon");
		g.addRule("A -> ADDa");
		g.addRule("A -> a");
		g.addRule("A -> epsilon");
		g.addRule("B -> ADDa");
		g.addRule("B -> b");
		g.addRule("C -> ABA");
		g.addRule("C -> bcc");
		g.addRule("D -> EE");
		g.addRule("E -> cc");
		g.addRule("E -> FF");
		g.addRule("G -> g");
		assertFalse(g.languageIsEmpty());
	}

}
