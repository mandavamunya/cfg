public class CFGParser {
	
	public static String getLeftHandSide(String s) {
		return s.split(" *-> *")[0].trim();
	}
	
	public static String getRightHandSide(String s) {
		return s.split(" *-> *")[1].trim();
	}

	/**
	 * Split a string s into an array of symbols.
	 * 
	 * Example: "aaS1bS2c" -> ["a", "a", "S1", "b", "S2", "c"]
	 */
	public static String[] splitIntoSymbols(String s) {
		if (s.equals("epsilon")) {
			return new String[]{s};
		}
		int symbolsCount = getSymbolCount(s);
		String[] symbols = new String[symbolsCount];
		for (int i = 0, j = 0; i < symbolsCount; i++) {
			symbols[i] = extractSymbol(s, j);
			j += symbols[i].length();
		}
		return symbols;
	}

	/**
	 * Get the symbol at position i in s.
	 */
	private static String extractSymbol(String s, int start) {
		if (start >= s.length() || start < 0) {
			return null;
		}
		int end = start + 1;
		while (end < s.length() && Character.isDigit(s.charAt(end))) {
			end++;
		}
		return s.substring(start, end);
	}

	/**
	 * Count the number of symbols in the string s.
	 */
	private static int getSymbolCount(String s) {
		int count = 0;
		for (int i = 0; i < s.length(); i++) {
			if (!Character.isDigit(s.charAt(i))) {
				count++;
			}
		}
		return count++;
	}
	
	public static int terminalToInt(String a) {
		return -(int) a.charAt(0);
	}
	
	public static int terminalToInt(char a) {
		return -(int) a;
	}
	
	public static String terminalToString(int a) {
		return String.valueOf((char) -a);
	}
}