import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Test;

public class CYKCorrectnessTest {

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

	/*
	 * S -> a, should derive "a".
	 */
	@Test
	public void Sa_shouldDerive_a() {
		CFG g = new CFG();
		g.addRule("S -> a");
		assertTrue(g.derives("a"));
	}

	/*
	 * S -> a, should not derive "aa".
	 */
	@Test
	public void Sa_shouldNotDerive_aa() {
		CFG g = new CFG();
		g.addRule("S -> a");
		assertFalse(g.derives("aa"));
	}

	/*
	 * S -> a, should not derive "e".
	 */
	@Test
	public void Sa_shouldNotDerive_e() {
		CFG g = new CFG();
		g.addRule("S -> a");
		assertFalse(g.derives("e"));
	}

	/*
	 * S -> AB, A -> a, B -> b, should derive "ab".
	 */
	@Test
	public void SAB_Aa_Bb_shouldDerive_ab() {
		CFG g = new CFG();
		g.addRule("S -> AB");
		g.addRule("A -> a");
		g.addRule("B -> b");
		assertTrue(g.derives("ab"));
	}

	/*
	 * S -> AB, A -> a, B -> b, should not derive "a".
	 */
	@Test
	public void SAB_Aa_Bb_shouldNotDerive_a() {
		CFG g = new CFG();
		g.addRule("S -> AB");
		g.addRule("A -> a");
		g.addRule("B -> b");
		assertFalse(g.derives("a"));
	}

	/*
	 * S -> AB, A -> AA, A -> a, B -> b, should derive "aaaaaab".
	 */
	@Test
	public void SAB_AAA_Aa_Bb_shoulDerive_aaaaaab() {
		CFG g = new CFG();
		g.addRule("S -> AB");
		g.addRule("A -> AA");
		g.addRule("A -> a");
		g.addRule("B -> b");
		assertTrue(g.derives("aaaaaab"));
	}

	/*
	 * X -> a, X -> b, X -> AA, X -> AB, X -> BA, X -> BB, for X = [S, A, B]
	 * should derive every word consisting of "a" and "b" up to length 6.
	 */
	@Test
	public void shouldDeriveAllCombinationsOf_a_And_b() {
		CFG g = new CFG();
		g.addRule("S -> a");
		g.addRule("S -> b");
		g.addRule("S -> AA");
		g.addRule("S -> AB");
		g.addRule("S -> BA");
		g.addRule("S -> BB");
		g.addRule("A -> a");
		g.addRule("A -> b");
		g.addRule("A -> AA");
		g.addRule("A -> AB");
		g.addRule("A -> BA");
		g.addRule("A -> BB");
		g.addRule("B -> a");
		g.addRule("B -> b");
		g.addRule("B -> AA");
		g.addRule("B -> AB");
		g.addRule("B -> BA");
		g.addRule("B -> BB");

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
	public void balancedParanthesis() {
		CFG g = new CFG();
		g.addRule("S -> SS");
		g.addRule("S -> LS1");
		g.addRule("S -> AA1");
		g.addRule("S -> a");
		g.addRule("S1 -> SR");
		g.addRule("L -> (");
		g.addRule("R -> )");
		g.addRule("A -> a");
		g.addRule("A1 -> AA1");
		g.addRule("A1 -> a");
		System.out.println(g.toString());

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

}