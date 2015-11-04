import static org.junit.Assert.*;

import org.junit.Test;

public class SplittingRulesCorrectnessTest {

	@Test
	public void test() {
		CFG g = new CFG();
		g.addRule("B -> ABCDEFG");
		g.addRule("U96 -> a");
		g = g.substituteTerminals().splitUpLongRules();
		System.out.println(g.toString());
	}

}
