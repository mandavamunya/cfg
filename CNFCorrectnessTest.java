import static org.junit.Assert.*;

import org.junit.Test;

public class CNFCorrectnessTest {

	@Test
	public void test() {
		CFG g = new CFG();
		assertTrue(g.isInCNF());
		g.addRule("S -> epsilon");
		assertTrue(g.isInCNF());
		g.addRule("S -> a");
		assertTrue(g.isInCNF());
		g.addRule("S -> b");
		assertTrue(g.isInCNF());
		g.addRule("S -> AA");
		assertTrue(g.isInCNF());
		g.addRule("S -> BC");
		assertTrue(g.isInCNF());
		g.addRule("A -> CC");
		assertTrue(g.isInCNF());
		g.addRule("C -> c");
		assertTrue(g.isInCNF());
	}
	
	@Test
	public void testDoubleTerminals() {
		CFG g = new CFG();
		g.addRule("S -> aa");
		assertFalse(g.isInCNF());
	}
	
	@Test
	public void testSingleNonterminal() {
		CFG g = new CFG();
		g.addRule("S -> A");
		assertFalse(g.isInCNF());
	}
	
	@Test
	public void testThreeNonterminals() {
		CFG g = new CFG();
		g.addRule("S -> CDE");
		assertFalse(g.isInCNF());
	}
	
	@Test
	public void testThatNonStartSymbolCannotDeriveEpsilon() {
		CFG g = new CFG();
		g.addRule("R -> epsilon");
		assertFalse(g.isInCNF());
	}
}
