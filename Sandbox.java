import java.util.Scanner;

/**
 * This class is for users who want to experiment by creating their own CFGs,
 * test if they can derive certain words or convert them to CNF.
 */
public class Sandbox {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		CFG cfg = new CFG();
		System.out.print("Enter commands ('quit' to exit). ");
		System.out.println("Make sure to format the commands properly!");
		System.out.println("1: To add a new rule, type it as 'add A -> BCDE'.");
		System.out.println("2: To print the CFG, type 'print'.");
		System.out.println("3: To empty the CFG, type 'empty'.");
		System.out.println("4: Convert to CNF by typing 'cnf'.");
		System.out.print("5: Ask if the CFG derives 'abc' by typing 'abc?'. ");
		System.out.println("CONVERT TO CNF FIRST!");
		System.out.println("6: Remove useless symbols by typing 'simplify'.");
		System.out.println("7: To check emptiness, type 'emptiness'.");
		System.out.println("8: To check finiteness, type 'finitess'.");
		String word;
		while (!(word = sc.nextLine()).equals("quit")) {
			if (word.endsWith("?")) {
				word = word.substring(0, word.length() - 1);
				if (cfg.derives(word)) {
					System.out.println("Yes.");
				} else {
					System.out.println("No.");
				}
			} else if (word.equals("print")) {
				System.out.println(cfg.toString());
			} else if (word.equals("empty")) {
				cfg = new CFG();
			} else if (word.equals("simplify")) {
				cfg = cfg.removeUselessSymbols();
			} else if (word.equals("cnf")) {
				cfg = cfg.toCNF();
			} else if (word.equals("emptiness")) {
				if (cfg.languageIsEmpty()) {
					System.out.println("The language is empty.");
				} else {
					System.out.println("The language is not empty.");
				}
			} else if (word.equals("finiteness")) {
				if (cfg.languageIsFinite()) {
					System.out.println("The language is finite.");
				} else {
					System.out.println("The language is infinite.");
				}
			} else if (word.startsWith("add")){
				cfg.addRule(word.substring(4));
			} else {
				System.out.println("Unknown command.");
			}
		}
		sc.close();
	}
}