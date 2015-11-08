import static org.junit.Assert.*;

import org.junit.Test;

public class RemoveEpsilonCorrectnessTest {

	@Test
	public void testEmptyGrammar() {
		CFG g = new CFG();
		g = g.removeEpsilonRules();
		assertTrue(g.noOfRules() == 0);
	}

	@Test
	public void test() {
		CFG g = new CFG();
		g.addRule("A -> BC");
		g.addRule("A -> DE");
		g.addRule("B -> epsilon");
		g.addRule("C -> epsilon");
		g.addRule("D -> epsilon");
		g.addRule("E -> e");
		g = g.removeEpsilonRules();
		System.out.println(g.toString());
	}
	
	@Test
	public void test2() {
		CFG g = new CFG();
		g.addRule("S -> AB");
		g.addRule("A -> aAA");
		g.addRule("B -> bBB");
		g.addRule("A -> epsilon");
		g.addRule("B -> epsilon");
		g = g.removeEpsilonRules();
		assertTrue(g.containsRule("S -> AB"));
		assertTrue(g.containsRule("A -> aAA"));
		assertTrue(g.containsRule("B -> bBB"));
		assertTrue(g.containsRule("S -> A"));
		assertTrue(g.containsRule("S -> B"));
		assertFalse(g.containsRule("A -> epsilon"));
		assertFalse(g.containsRule("B -> epsilon"));
	}
	
	@Test
	public void test3() {
		CFG g = new CFG();
		g.addRule("S -> AB");
		g.addRule("A -> epsilon");
		g.addRule("B -> b");
		g = g.removeEpsilonRules();
		System.out.println(g.toString());
	}


}
