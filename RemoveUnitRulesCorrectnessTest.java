import static org.junit.Assert.*;

import org.junit.Test;

public class RemoveUnitRulesCorrectnessTest {

	@Test
	public void test() {
		CFG g = new CFG();
		g.addRule("S -> a");
		g = g.removeUnitRules();
		assertTrue(g.noOfRules() == 1);
		assertTrue(g.containsRule("S -> a"));
	}

	@Test
	public void test2() {
		CFG g = new CFG();
		g.addRule("S -> A");
		g.addRule("A -> a");
		g = g.removeUnitRules();
		assertTrue(g.noOfRules() == 2);
		assertTrue(g.containsRule("S -> a"));
		assertTrue(g.containsRule("A -> a"));
	}

	@Test
	public void test3() {
		CFG g = new CFG();
		g.addRule("S -> A");
		g.addRule("A -> B");
		g.addRule("B -> C");
		g.addRule("C -> a");
		g = g.removeUnitRules();
		assertTrue(g.noOfRules() == 4);
		assertTrue(g.containsRule("S -> a"));
		assertTrue(g.containsRule("A -> a"));
		assertTrue(g.containsRule("B -> a"));
		assertTrue(g.containsRule("C -> a"));
	}

	@Test
	public void test4() {
		CFG g = new CFG();
		g.addRule("S -> A");
		g.addRule("S -> B");
		g.addRule("A -> a");
		g.addRule("A -> Ca");
		g.addRule("B -> bb");
		g.addRule("B -> A");
		g.addRule("B -> a");
		g.addRule("B -> Aaa");
		g.addRule("C -> D");
		g.addRule("D -> dddd");
		g = g.removeUnitRules();
		assertTrue(g.noOfRules() == 12);
		assertTrue(g.containsRule("S -> a"));
		assertTrue(g.containsRule("S -> Ca"));
		assertTrue(g.containsRule("S -> bb"));
		assertTrue(g.containsRule("S -> Aaa"));
		assertTrue(g.containsRule("A -> a"));
		assertTrue(g.containsRule("A -> Ca"));
		assertTrue(g.containsRule("B -> bb"));
		assertTrue(g.containsRule("B -> a"));
		assertTrue(g.containsRule("B -> Aaa"));
		assertTrue(g.containsRule("B -> Ca"));
		assertTrue(g.containsRule("C -> dddd"));
		assertTrue(g.containsRule("D -> dddd"));
	}

}
