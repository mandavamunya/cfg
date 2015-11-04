import static org.junit.Assert.*;
import org.junit.Test;

public class SimplificationCorrectnessTest {
	
	@Test
	public void testThatEmptyGrammarDoesNotCrash() {
		CFG g = new CFG();
		g = g.removeUselessSymbols();
	}
	
	@Test
	public void testMinimalGrammar1() {
		CFG g = new CFG();
		g.addRule("S -> a");
		g = g.removeUselessSymbols();
		assertTrue(g.containsRule("S -> a"));
	}
	
	@Test
	public void testMinimalGrammar2() {
		CFG g = new CFG();
		g.addRule("S -> A");
		g = g.removeUselessSymbols();
		assertFalse(g.containsRule("S -> A"));
	}
	
	@Test
	public void testMinimalDisconnectedGrammar() {
		CFG g = new CFG();
		g.addRule("A -> b");
		g = g.removeUselessSymbols();
		assertFalse(g.containsRule("A -> b"));
	}
	
	@Test
	public void testMinimalEpsilonGrammar() {
		CFG g = new CFG();
		g.addRule("S -> epsilon");
		g = g.removeUselessSymbols();
		assertTrue(g.containsRule("S -> epsilon"));
	}
	
	@Test
	public void testMinimalGrammarWhereReverseOrderFails() {
		CFG g = new CFG();
		g.addRule("S -> AB");
		g.addRule("A -> a");
		g = g.removeUselessSymbols();
		assertFalse(g.containsRule("S -> AB"));
		assertFalse(g.containsRule("A -> a"));
	}
	
	@Test
	public void testSmallEpsilonGrammar() {
		CFG g = new CFG();
		g.addRule("S -> A");
		g.addRule("A -> epsilon");
		g.addRule("B -> epsilon");
		g = g.removeUselessSymbols();
		assertTrue(g.containsRule("S -> A"));
		assertTrue(g.containsRule("A -> epsilon"));
		assertFalse(g.containsRule("B -> epsilon"));
	}
	
	@Test
	public void testParallelEdges() {
		CFG g = new CFG();
		g.addRule("S -> ddd");
		g.addRule("S -> CCCC");
		g = g.removeUselessSymbols();
		assertTrue(g.containsRule("S -> ddd"));
		assertFalse(g.containsRule("S -> CCCC"));
	}
	
	@Test
	public void testExampleFromReport() {
		CFG g = new CFG();
		g.addRule("S -> A");
		g.addRule("S -> B");
		g.addRule("S -> CD");
		g.addRule("A -> a");
		g.addRule("B -> Bb");
		g.addRule("C -> b");
		g.addRule("E -> Ab");
		g = g.removeUselessSymbols();
		assertTrue(g.containsRule("S -> A"));
		assertTrue(g.containsRule("A -> a"));
		assertFalse(g.containsRule("S -> B"));
		assertFalse(g.containsRule("S -> CD"));
		assertFalse(g.containsRule("B -> Bb"));
		assertFalse(g.containsRule("C -> b"));
		assertFalse(g.containsRule("E -> Ab"));
	}
	
	@Test
	public void testBigExampleFromReport() {
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
		g = g.removeUselessSymbols();
		assertTrue(g.containsRule("S -> SS"));
		assertTrue(g.containsRule("S -> ABA"));
		assertTrue(g.containsRule("S -> epsilon"));
		assertTrue(g.containsRule("A -> ADDa"));
		assertTrue(g.containsRule("A -> a"));
		assertTrue(g.containsRule("A -> epsilon"));
		assertTrue(g.containsRule("B -> ADDa"));
		assertTrue(g.containsRule("B -> b"));
		assertTrue(g.containsRule("D -> EE"));
		assertTrue(g.containsRule("E -> cc"));
		assertFalse(g.containsRule("C -> ABA"));
		assertFalse(g.containsRule("C -> bcc"));
		assertFalse(g.containsRule("E -> FF"));
		assertFalse(g.containsRule("G -> g"));
	}

}
