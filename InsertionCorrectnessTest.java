import static org.junit.Assert.*;

import org.junit.Test;

public class InsertionCorrectnessTest {

	@Test
	public void test() {
		CFG g = new CFG();
		g.addRule("S -> SS");
		assertTrue(g.size() == 3);
		assertTrue(g.containsRule("S -> SS"));
		assertFalse(g.containsRule("S -> A"));
		g.addRule("S -> SS");
		assertTrue(g.size() == 3);
		assertTrue(g.containsRule("S -> SS"));
		g.addRule("S -> A");
		assertTrue(g.size() == 5);
		g.addRule("A -> SS");
		assertTrue(g.size() == 8);
		g.addRule("S -> a");
		assertTrue(g.size() == 10);
		g.addRule("S -> b");
		assertTrue(g.size() == 12);
		g.addRule("B -> b");
		assertTrue(g.size() == 14);
		assertTrue(g.containsRule("S -> SS"));
		assertTrue(g.containsRule("A -> SS"));
		assertTrue(g.containsRule("B -> b"));
		assertFalse(g.containsRule("A -> b"));
	}

}
