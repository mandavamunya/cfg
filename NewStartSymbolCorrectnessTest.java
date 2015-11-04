import static org.junit.Assert.*;

import org.junit.Test;

public class NewStartSymbolCorrectnessTest {

	@Test
	public void testBasicGrammar() {
		CFG g = new CFG();
		g = g.createNewStartSymbol();
		assertTrue(g.containsRule("S -> S0"));
		g = g.createNewStartSymbol();
		assertTrue(g.containsRule("S -> S1"));
		assertTrue(g.containsRule("S1 -> S0"));
		assertFalse(g.containsRule("S -> S0"));
	}

	@Test
	public void testSmallGrammar() {
		CFG g = new CFG();
		g.addRule("S -> a");
		g.addRule("S -> A");
		g.addRule("A -> SSS0");
		g.addRule("B -> S");
		g = g.createNewStartSymbol();
		assertTrue(g.containsRule("S -> S1"));
		assertTrue(g.containsRule("S1 -> a"));
		assertTrue(g.containsRule("S1 -> A"));
		assertTrue(g.containsRule("A -> S1S1S0"));
		assertTrue(g.containsRule("B -> S1"));
		assertFalse(g.containsRule("A -> SSS0"));
		assertFalse(g.containsRule("B -> S"));
	}

}
