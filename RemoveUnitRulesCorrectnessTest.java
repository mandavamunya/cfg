import static org.junit.Assert.*;

import org.junit.Test;

public class RemoveUnitRulesCorrectnessTest {

	@Test
	public void testThatSingleEpsilonRuleStays() {
		CFG g = new CFG();
		g.addRule("S -> epsilon");
		g = g.removeUnitRules();
		assertTrue(g.noOfRules() == 1);
		assertTrue(g.containsRule("S -> epsilon"));
	}

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
		System.out.println("HEJHRTHERTGHWERTGHRTEHRT " + g.toString());
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

	@Test
	public void test5() {
		CFG g = new CFG();
		g.addRule("S -> A");
		g.addRule("A -> Aa");
		g.addRule("A -> c");
		g.addRule("A -> d");
		g.addRule("A -> E");
		g.addRule("E -> b");
		g = g.removeUnitRules();
		assertTrue(g.containsRule("S -> Aa"));
		assertTrue(g.containsRule("S -> b"));
		assertTrue(g.containsRule("S -> c"));
		assertTrue(g.containsRule("S -> d"));
		assertTrue(g.containsRule("A -> Aa"));
		assertTrue(g.containsRule("A -> b"));
		assertTrue(g.containsRule("A -> c"));
		assertTrue(g.containsRule("A -> d"));
		assertFalse(g.containsRule("S -> A"));
		assertFalse(g.containsRule("A -> E"));
	}
	

	@Test
	public void testNonUnitCycle() {
		CFG g = new CFG();
		g.addRule("S -> S0");
		g.addRule("S0 -> aA");
		g.addRule("S0 -> C");
		g.addRule("A -> B");
		g.addRule("B -> S0");
		g.addRule("C -> b");
		g = g.removeUnitRules();
		g = g.removeUselessSymbols();
		System.out.println(g.toString());
	}
	

	@Test
	public void testExampleFromTheReport() {
		CFG g = new CFG();
		g.addRule("S -> A");
		g.addRule("A -> B");
		g.addRule("A -> E");
		g.addRule("B -> a");
		g.addRule("B -> C");
		g.addRule("B -> D");
		g.addRule("C -> c");
		g.addRule("C -> A");
		g.addRule("D -> d");
		g.addRule("D -> C");
		g.addRule("E -> b");
		g.addRule("E -> F");
		g.addRule("F -> S");
		g = g.removeUnitRules();
		assertTrue(g.containsRule("S -> a"));
		assertTrue(g.containsRule("S -> b"));
		assertTrue(g.containsRule("S -> c"));
		assertTrue(g.containsRule("S -> d"));
		assertFalse(g.containsRule("S -> A"));
		assertFalse(g.containsRule("A -> E"));
		assertFalse(g.containsRule("A -> B"));
		assertFalse(g.containsRule("B -> C"));
		assertFalse(g.containsRule("B -> D"));
		assertFalse(g.containsRule("C -> A"));
		assertFalse(g.containsRule("D -> C"));
	}
	

	@Test
	public void testLongChainOfUnitRules() {
		CFG g = new CFG();
		g.addRule("S -> S0");
		g.addRule("S0 -> S1");
		g.addRule("S1 -> S2");
		g.addRule("S2 -> S3");
		g.addRule("S3 -> S4");
		g.addRule("S4 -> S5");
		g.addRule("S5 -> S6");
		g.addRule("S6 -> aabc");
		g.addRule("S6 -> Ab");
		g.addRule("S6 -> A");
		g.addRule("S6 -> AaBbAcB");
		g.addRule("A -> a");
		g.addRule("B -> b");
		g = g.removeUnitRules();
		System.out.println("REAL DEAL: " + g.toString());
		assertTrue(g.containsRule("S -> aabc"));
		assertTrue(g.containsRule("S -> Ab"));
		assertTrue(g.containsRule("S -> a"));
		assertTrue(g.containsRule("S -> AaBbAcB"));
		assertTrue(g.containsRule("A -> a"));
		assertTrue(g.containsRule("B -> b"));
		assertFalse(g.containsRule("S -> b"));
		assertFalse(g.containsRule("S -> S0"));
		assertFalse(g.containsRule("S0 -> S1"));
		assertFalse(g.containsRule("S1 -> S2"));
		assertFalse(g.containsRule("S2 -> S3"));
		assertFalse(g.containsRule("S3 -> S4"));
		assertFalse(g.containsRule("S4 -> S5"));
		assertFalse(g.containsRule("S5 -> S6"));
		assertFalse(g.containsRule("S6 -> A"));
	}
	
}
