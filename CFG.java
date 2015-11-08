import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * This class represents a context-free grammar. Available actions for a CFG 
 * include, among others:
 * 		Inserting a new rule.
 * 		Checking if it contains a specific rule.
 * 		Checking if a specific word can be derived.
 * 		Constructing a simplified CFG without useless symbols and rules.
 * 		Constructing a CFG in CNF with the same language.
 * 
 * @author Patrik Hornqvist, c12pht
 *
 */
public class CFG {
	// These variables are always up to date.
	private ArrayList<HashSet<Integer>> rules;
	private ArrayList<ArrayList<Integer>> bodies;
	private ArrayList<String> nts;
	private HashMap<ArrayList<Integer>, Integer> bodiesMap;
	private HashMap<String, Integer> ntsMap;
	private int startSymbolID;
	private int epsilonID;
	private int noOfRules;
	private int size;
	
	// These variables might need to be updated or reset before used.
	private ArrayList<HashSet<Integer>> rulesInv;
	private ArrayList<ArrayList<Integer>> bodiesInv;
	private ArrayList<HashSet<Integer>> nonUnitRules;
	private boolean[] hasVisitedSymbol;
	private boolean[] hasVisitedBody;
	private boolean[] isReachable;
	private boolean[] isStacked;
	private int[] bodySymbolCount;
	private int[] ntUnitRuleCount;
	private int[] scc;
	private int[] indexes;
	private int index;
	private boolean[] onStack;
	private Stack<Integer> stack;
	private int[] lowLink;

	/**
	 * Construct an empty CFG.
	 */
	public CFG() {
		rules = new ArrayList<HashSet<Integer>>();
		bodies = new ArrayList<ArrayList<Integer>>();
		bodiesMap = new HashMap<ArrayList<Integer>, Integer>();
		nts = new ArrayList<String>();
		ntsMap = new HashMap<String, Integer>();
		startSymbolID = addNonterminal("S");
		epsilonID = -1;
		noOfRules = size = 0;
	}

	/**
	 * Add a new rule to this CFG (unless it already exists). The parameter
	 * takes the form "A -> X" where A is a nonterminal and X is a sequence of
	 * symbols (e.g. "B1 -> aAbB1c"). This method executes in constant time.
	 */
	public void addRule(String rule) {
		String lhs = CFGParser.getLeftHandSide(rule);
		String rhs = CFGParser.getRightHandSide(rule);
		int lhsID = addNonterminal(lhs);
		String[] bodySymbols = CFGParser.splitIntoSymbols(rhs);
		ArrayList<Integer> body = new ArrayList<Integer>();
		for (int i = 0; i < bodySymbols.length; i++) {
			if (isEpsilon(bodySymbols[i])) {
				body.add(epsilonID);
			} else if (isTerminal(bodySymbols[i])) {
				body.add(CFGParser.terminalToInt(bodySymbols[i]));
			} else {
				int ntID = addNonterminal(bodySymbols[i]);
				body.add(ntID);
			}
		}
		int bodyID = addBody(body);
		if (!rules.get(lhsID).contains(bodyID)) {
			rules.get(lhsID).add(bodyID);
			noOfRules++;
			size += 1 + body.size();
		}
	}

	/**
	 * Add a nonterminal to this CFG (unless it already exists) and return its
	 * integer ID. This method is used by addRule().
	 */
	private int addNonterminal(String nonterminal) {
		int ntID;
		if (ntsMap.containsKey(nonterminal)) {
			ntID = ntsMap.get(nonterminal);
		} else {
			ntID = nts.size();
			nts.add(ntID, nonterminal);
			ntsMap.put(nonterminal, ntID);
			rules.add(ntID, new HashSet<Integer>());
		}
		return ntID;
	}

	/**
	 * Add a rule body to this CFG (unless it already exists) and return its
	 * integer ID. This method is used by addRule().
	 */
	private int addBody(ArrayList<Integer> body) {
		int bodyID;
		if (bodiesMap.containsKey(body)) {
			bodyID = bodiesMap.get(body);
		} else {
			bodyID = bodies.size();
			bodies.add(bodyID, body);
			bodiesMap.put(body, bodyID);
		}
		return bodyID;
	}

	/**
	 * Check if this CFG contains the given rule. 
	 */
	public boolean containsRule(String rule) {
		String lhs = CFGParser.getLeftHandSide(rule);
		String rhs = CFGParser.getRightHandSide(rule);
		if (!ntsMap.containsKey(lhs)) {
			return false;
		}
		int ntID = ntsMap.get(lhs);
		String[] bodySymbols = CFGParser.splitIntoSymbols(rhs);
		ArrayList<Integer> body = new ArrayList<Integer>();
		for (int i = 0; i < bodySymbols.length; i++) {
			if (isEpsilon(bodySymbols[i])) {
				body.add(epsilonID);
			} else if (isTerminal(bodySymbols[i])) {
				body.add(CFGParser.terminalToInt(bodySymbols[i]));
			} else {
				int nonterminalID = addNonterminal(bodySymbols[i]);
				body.add(nonterminalID);
			}
		}
		if (!bodiesMap.containsKey(body)) {
			return false;
		}
		int bodyID = bodiesMap.get(body);
		return rules.get(ntID).contains(bodyID);
	}
	
	/**
	 * Return the number of rules in this CFG.
	 */
	public int noOfRules() {
		return noOfRules;
	}
	
	/**
	 * Return the size of this CFG.
	 */
	public int size() {
		return size;
	}

	/**
	 * Use the CYK algorithm to check if word can be derived from this CFG.
	 * This method executes in cubic time for a fixed-size CFG.
	 */
	public boolean derives(String word) {
		final int n = word.length();
		int r = nts.size();
		boolean[][][] P = new boolean[n][n][r];		// All false as default
		for (int i = 0; i < n; i++) {
			boolean terminalExists = false;
			for (int ntID = 0; ntID < rules.size(); ntID++) {
				for (int bodyID : rules.get(ntID)) {
					if (bodies.get(bodyID).size() == 1) {
						int symbolID = CFGParser.terminalToInt(word.charAt(i));
						if (bodies.get(bodyID).get(0) == symbolID) {
							P[0][i][ntID] = terminalExists = true;
						}
					}
				}
			}
			if (!terminalExists) {
				return false;
			}
		}
		for (int i = 1; i < n; i++) {
			for (int j = 0; j < n - i; j++) {
				for (int k = 0; k < i; k++) {
					for (int ntID = 0; ntID < rules.size(); ntID++) {
						for (int bodyID : rules.get(ntID)) {
							if (bodies.get(bodyID).size() == 2) {
								int a = ntID;
								int b = bodies.get(bodyID).get(0);
								int c = bodies.get(bodyID).get(1);
								if (P[k][j][b] && P[i - k - 1][j + k + 1][c]) {
									P[i][j][a] = true;
								}
							}
						}
					}
				}
			}
		}
		return P[n - 1][0][startSymbolID];
	}

	/**
	 * Create and return a new CFG which contains only the useful symbols and
	 * rules.
	 */
	public CFG removeUselessSymbols() {
		return this.removeNonGeneratingSymbols().removeUnreachableSymbols();
	}
	
	public CFG removeNonGeneratingSymbols() {
		setBodySymbolCount();
		Queue<Integer> q = initiateQueue();
		Queue<Boolean> nextIsABodyNode = new LinkedList<Boolean>();
		for (int i = 0; i < q.size(); i++) {
			nextIsABodyNode.add(true);
		}
		createInvertedAdjacencyArrays();
		boolean[] isGenerating = new boolean[nts.size()];
		while (!q.isEmpty()) {
			if (nextIsABodyNode.poll()) {
				int bodyID = q.poll();
				for (int ntID : rulesInv.get(bodyID)) {
					if (!isGenerating[ntID]) {
						isGenerating[ntID] = true;
						q.add(ntID);
						nextIsABodyNode.add(false);
					}
				}
			} else {
				int ntID = q.poll();
				for (int bodyID : bodiesInv.get(ntID)) {
					bodySymbolCount[bodyID]--;
					if (bodySymbolCount[bodyID] == 0) {
						q.add(bodyID);
						nextIsABodyNode.add(true);
					}
				}
			}
		}
		return createNewCFGKeepingNonterminalsIn(isGenerating);
	}
	
	/**
	 * Label each body node with the number of outgoing edges.
	 */
	private void setBodySymbolCount() {
		bodySymbolCount = new int[bodies.size()];	// All 0 as default.
		for (int bodyID = 0; bodyID < bodies.size(); bodyID++) {
			for (int symbolID : bodies.get(bodyID)) {
				if (isNonterminal(symbolID)) {
					bodySymbolCount[bodyID]++;
				}
			}
		}
	}

	/**
	 * Create and return a queue containing all body nodes of the CFG whose
	 * outgoing edges all lead to terminal or epsilon nodes.
	 */
	private Queue<Integer> initiateQueue() {
		Queue<Integer> q = new LinkedList<Integer>();
		for (int bodyId = 0; bodyId < bodies.size(); bodyId++) {
			boolean addBodyId = true;
			for (int j = 0; j < bodies.get(bodyId).size(); j++) {
				if (isNonterminal(bodies.get(bodyId).get(j))) {
					addBodyId = false;
					break;
				}
			}
			if (addBodyId) {
				q.add(bodyId);
			}
		}
		return q;
	}
	
	/**
	 * Create the inverted adjacency arrays rulesInv and bodiesInv. These are 
	 * used when traversing the CFG graph edges in their opposite direction.
	 */
	private void createInvertedAdjacencyArrays() {
		rulesInv = new ArrayList<HashSet<Integer>>();
		for (int i = 0; i < bodies.size(); i++) {
			rulesInv.add(new HashSet<Integer>());
		}
		for (int ntID = 0; ntID < nts.size(); ntID++) {
			for (int bodyID : rules.get(ntID)) {
				rulesInv.get(bodyID).add(ntID);
			}
		}
		bodiesInv = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < rules.size(); i++) {
			bodiesInv.add(new ArrayList<Integer>());
		}
		for (int bodyID = 0; bodyID < bodies.size(); bodyID++) {
			for (int symbolID : bodies.get(bodyID)) {
				if (isNonterminal(symbolID)) {
					bodiesInv.get(symbolID).add(bodyID);
				}
			}
		}
	}
	
	/**
	 * Create and return a new CFG with only the rules of this CFG that consist
	 * solely of nonterminals with IDs equal to the indexes of all true elements
	 * in ntsToKeep.
	 */
	private CFG createNewCFGKeepingNonterminalsIn(boolean[] ntsToKeep) {
		CFG g = new CFG();
		for (int ntID = 0; ntID < rules.size(); ntID++) {
			if (!ntsToKeep[ntID]) {
				continue;
			}
			for (int bodyID : rules.get(ntID)) {
				boolean addRule = true;
				for (int symbolID : bodies.get(bodyID)) {
					if (isNonterminal(symbolID) && !ntsToKeep[symbolID]) {
						addRule = false;
						break;
					}
				}
				if (addRule) {
					g.addRule(nts.get(ntID) + " -> " + bodyToString(bodyID));
				}
			}
		}
		return g;
	}

	public CFG removeUnreachableSymbols() {
		isReachable = new boolean[nts.size()];
		hasVisitedSymbol = new boolean[nts.size()];
		hasVisitedBody = new boolean[bodies.size()];
		findReachableBodies(startSymbolID);
		return createNewCFGKeepingNonterminalsIn(isReachable);
	}

	private void findReachableBodies(int ntID) {
		isReachable[ntID] = hasVisitedSymbol[ntID] = true;
		for (int bodyID : rules.get(ntID)) {
			if (!hasVisitedBody[bodyID]) {
				hasVisitedBody[bodyID] = true;
				for (int ntID2 : bodies.get(bodyID)) {
					if (isNonterminal(ntID2) && !hasVisitedSymbol[ntID2]) {
						findReachableBodies(ntID2);
					}
				}
			}
		}
	}
	
	public boolean isInCNF() {
		for (int ntID = 0; ntID < rules.size(); ntID++) {
			for (int bodyID : rules.get(ntID)) {
				ArrayList<Integer> b = bodies.get(bodyID);
				if (b.size() > 2) {
					return false;
				} else if (b.size() == 2) {
					if (!isNonterminal(b.get(0)) || !isNonterminal(b.get(1))) {
						return false;
					}
				} else {
					if (ntID > 0 && !isTerminal(b.get(0))) {
						return false;
					} else if (ntID == 0 && isNonterminal(b.get(0))) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Create and return a new equivalent CFG that is in Chomsky Normal Form.
	 */
	public CFG toCNF() {
		CFG g = this;
		g = g.createNewStartSymbol();
		g = g.substituteTerminals();
		g = g.splitUpLongRules();
		g = g.removeEpsilonRules();
		g = g.removeUnitRules();
		g = g.removeUselessSymbols();
		return g;
	}
	
	public CFG substituteTerminals() {
		CFG g = new CFG();
		HashMap<Integer, String> substitutedNames;
		substitutedNames = new HashMap<Integer, String>();
		for (int i = -2, k = 0; i > -128; k++) {
			if (!nts.contains("U" + k)) {
				substitutedNames.put(i--, "U" + k);
			}
		}
		for (int ntID = 0; ntID < rules.size(); ntID++) {
			for (int bodyID : rules.get(ntID)) {
				StringBuilder rule = new StringBuilder(nts.get(ntID) + " -> ");
				ArrayList<Integer> body = bodies.get(bodyID);
				if (body.size() >= 2) {
					for (int i = 0; i < body.size(); i++) {
						if (isTerminal(body.get(i))) {
							rule.append(substitutedNames.get(body.get(i)));
							g.addRule(substitutedNames.get(body.get(i)) + " -> " 
									+ CFGParser.terminalToString(body.get(i)));
						} else {
							rule.append(nts.get(body.get(i)));
						}
					}
				} else {
					rule.append(bodyToString(bodyID));
				}
				g.addRule(rule.toString());
			}
			
		}
		return g;
	}
	
	public CFG splitUpLongRules() {
		CFG g = new CFG();
		int k = 0;
		for (int ntID = 0; ntID < rules.size(); ntID++) {
			String nt = symbolIDToString(ntID);
			for (int bodyID : rules.get(ntID)) {
				ArrayList<Integer> body = bodies.get(bodyID);
				if (body.size() <= 2) {
					g.addRule(nt + " -> " + bodyToString(bodyID));
				} else {
					String lastNt = symbolIDToString(ntID);
					int i = 0;
					for (; i < body.size() - 2; i++) {
						k = findUnusedNonterminalName("V", k);
						String newNt = "V" + k++;
						String rule = lastNt + " -> ";
						rule += symbolIDToString(body.get(i)) + newNt;
						g.addRule(rule);
						lastNt = newNt;
					}
					String rule = lastNt + " -> ";
					rule += symbolIDToString(body.get(i));
					rule += symbolIDToString(body.get(i + 1));
					g.addRule(rule);
				}
			}
		}
		return g;
	}
	
	/**
	 * Create and return a new CFG with equal language with all epsilon rules
	 * removed (except possibly S -> epsilon). NOTE: All rule bodies must have
	 * length at most 2!
	 */
	public CFG removeEpsilonRules() {
		CFG g = new CFG();
		setBodySymbolCount();
		Queue<Integer> q = initiateQueueWithEpsilonBody();
		Queue<Boolean> nextIsABodyNode = new LinkedList<Boolean>();
		nextIsABodyNode.add(true);
		createInvertedAdjacencyArrays();
		boolean[] isNullable = new boolean[nts.size()];
		while (!q.isEmpty()) {
			if (nextIsABodyNode.poll()) {
				int bodyID = q.poll();
				for (int ntID : rulesInv.get(bodyID)) {
					if (!isNullable[ntID]) {
						isNullable[ntID] = true;
						q.add(ntID);
						nextIsABodyNode.add(false);
					}
				}
			} else {
				int ntID = q.poll();
				for (int bodyID : bodiesInv.get(ntID)) {
					bodySymbolCount[bodyID]--;
					if (bodySymbolCount[bodyID] == 0) {
						q.add(bodyID);
						nextIsABodyNode.add(true);
					}
				}
			}
		}
		for (int ntID = 0; ntID < rules.size(); ntID++) {
			String nt = symbolIDToString(ntID);
			for (int bodyID : rules.get(ntID)) {
				ArrayList<Integer> body = bodies.get(bodyID);
				if (!isEpsilon(body.get(0))) {
					g.addRule(nt + " -> " + bodyToString(bodyID));
				}
				if (body.size() == 2) {
					if (isNonterminal(body.get(0)) && isNullable[body.get(0)]) {
						g.addRule(nt + " -> " + symbolIDToString(body.get(1)));
					}
					if (isNonterminal(body.get(1)) && isNullable[body.get(1)]) {
						g.addRule(nt + " -> " + symbolIDToString(body.get(0)));
					}
				}
			}
		}
		if (this.containsRule("S -> epsilon")) {
			g.addRule("S -> epsilon");
		}
		return g;
	}

	/**
	 * Create and return a queue containing the body node of the CFG whose
	 * outgoing edge leads to epsilon.
	 */
	private Queue<Integer> initiateQueueWithEpsilonBody() {
		Queue<Integer> q = new LinkedList<Integer>();
		for (int bodyId = 0; bodyId < bodies.size(); bodyId++) {
			if (isEpsilon(bodies.get(bodyId).get(0))) {
				q.add(bodyId);
				break;
			}
		}
		return q;
	}
	
	/**
	 * Find and return the next integer k such that the nonterminal Uk (e.g.
	 * U0 or U123) does not already exist in the grammar.
	 */
	private int findUnusedNonterminalName(String name, int k) {
		if (nts.contains(name + k)) {
			return findUnusedNonterminalName(name, k + 1);
		}
		return k;
	}

	/**
	 * Check if the language generated by this CFG is empty.
	 */
	public boolean languageIsEmpty() {
		return this.removeUselessSymbols().noOfRules() == 0;
	}

	/**
	 * Check if the language generated by this CFG is finite.
	 */
	public boolean languageIsFinite() {
		return !this.toCNF().isCyclicInit(startSymbolID);
	}
	
	public boolean isCyclicInit(int ntID) {
		isStacked = new boolean[nts.size()];
		hasVisitedSymbol = new boolean[nts.size()];
		return isCyclic(startSymbolID);
	}
	
	public boolean isCyclic(int ntID) {
		isStacked[ntID] = hasVisitedSymbol[ntID] = true;
		for (int bodyID : rules.get(ntID)) {
			for (int ntID2 : bodies.get(bodyID)) {
				if (isNonterminal(ntID2)) {
					if (isStacked[ntID2]) {
						return true;
					}
					if (!hasVisitedSymbol[ntID2] && isCyclic(ntID2)) {
						return true;
					}
				}
			}
		}
		isStacked[ntID] = false;
		return false;
	}
	
	public CFG createNewStartSymbol() {
		CFG g = new CFG();
		String newStartSymbol = "S" + findUnusedNonterminalName("S", 0);
		g.addRule("S -> " + newStartSymbol);
		for (int ntID = 0; ntID < rules.size(); ntID++) {
			String nt = symbolIDToString(ntID);
			if (ntID == startSymbolID) {
				nt = newStartSymbol;
			}
			for (int bodyID : rules.get(ntID)) {
				ArrayList<Integer> body = bodies.get(bodyID);
				StringBuilder rule = new StringBuilder(nt + " -> ");
				for (int symbolID : body) {
					if (symbolID == startSymbolID) {
						rule.append(newStartSymbol);
					} else {
						rule.append(symbolIDToString(symbolID));
					}
				}
				g.addRule(rule.toString());
			}
		}
		if (this.containsRule("S -> epsilon")) {
			g.addRule("S -> epsilon");
		}
		return g;
	}
	
	public CFG removeUnitRules() {
		findSCCInit();
		CFG g = this.removeUnitCycles();
		g = g.removeUnitRules2();
		return g.removeUnitRules3();
	}
	
	/**
	 * Creates and return a new CFG which has all nonterminal nodes in the same 
	 * strongly connected component contracted into one nonterminal. It also
	 * removes rules of the form A -> A.
	 */
	public CFG removeUnitCycles() {
		findSCCInit();
		CFG g = new CFG();
		for (int ntID = 0; ntID < nts.size(); ntID++) {
			for (int bodyID : rules.get(ntID)) {
				ArrayList<Integer> body = bodies.get(bodyID);
				StringBuilder rule = new StringBuilder(nts.get(scc[ntID]));
				rule.append(" -> ");
				for (int i = 0; i < body.size(); i++) {
					if (isNonterminal(body.get(i))) {
						rule.append(nts.get(scc[body.get(i)]));
					} else if (isTerminal(body.get(i))) {
						rule.append(CFGParser.terminalToString(body.get(i)));
					} else {
						rule.append("epsilon");
					}
				}
				if (!(body.size() == 1 && isNonterminal(body.get(0)) && scc[ntID] == scc[body.get(0)])) {
					g.addRule(rule.toString());
				}
			}
			rules.get(scc[ntID]).addAll(rules.get(ntID));
		}
		return g;
	}
	
	/**
	 * Initiation method for findSCC.
	 */
	public void findSCCInit() {
		index = 0;
		stack = new Stack<Integer>();
		scc = new int[nts.size()];
		for (int i = 0; i < scc.length; i++) {
			scc[i] = i;
		}
		indexes = new int[nts.size()];
		lowLink = new int[nts.size()];
		onStack = new boolean[nts.size()];
		for (int i = 0; i < indexes.length; i++) {
			indexes[i] = -1;
		}
		for (int ntID = 0; ntID < indexes.length; ntID++) {
			if (indexes[ntID] == -1) {
				findSCC(ntID);
			}
		}
	}
	
	/**
	 * Find all strongly connected components using Tarjan's algorithm. This
	 * creates a mapping in the variable scc, where scc[ntID] = rootNtID, that
	 * is the root nonterminal node of the strongly connected component ntID is
	 * inside.
	 */
	public void findSCC(int ntID) {
		indexes[ntID] = lowLink[ntID] = index++;
		stack.push(ntID);
		onStack[ntID] = true;
		
		for (int bodyID : rules.get(ntID)) {
			if (!(bodies.get(bodyID).size() == 1 && isNonterminal(bodies.get(bodyID).get(0)))) {
				continue;
			}
			int ntID2 = bodies.get(bodyID).get(0);
			if (indexes[ntID2] == -1) {
				findSCC(ntID2);
				lowLink[ntID] = Math.min(lowLink[ntID], lowLink[ntID2]);
			} else if (onStack[ntID2]) {
				lowLink[ntID] = Math.min(lowLink[ntID], indexes[ntID2]);
			}
		}
		
		if (lowLink[ntID] == indexes[ntID]) {
			int ntID2 = stack.peek();
			do {
				ntID2 = stack.pop();
				onStack[ntID2] = false;
				scc[ntID2] = ntID;
			} while (ntID2 != ntID);
		}
	}
	
	/**
	 * Iteratively traverse the graph in inverted order, and whenever we
	 * encounter a unit rule, we copy the rules of the child to the parent node.
	 * If we encounter a non-unit rule, we simply keep traversing without any
	 * copying. We can only keep traversing a nonterminal A if its
	 * ntUnitRuleCount is 0, that is there no longer exists a rule A -> B
	 * (for which we delete whenever we find them).
	 * 
	 * Note: This method actually changes the rules of THIS grammar! (In most
	 * other methods, we create a new grammar).
	 */
	public CFG removeUnitRules2() {
		hasVisitedSymbol = new boolean[nts.size()];
		setNtUnitRuleCount();
		setBodySymbolCount();
		Queue<Integer> q = initiateQueueWithNonUnitNts();
		createInvertedAdjacencyArrays();
		while (!q.isEmpty()) {
			int ntID = q.poll();
			for (int bodyID : bodiesInv.get(ntID)) {
				for (int ntID2 : rulesInv.get(bodyID)) {
					if (!hasVisitedSymbol[ntID2]) {
						if (bodySymbolCount[bodyID] == 1) {
							ntUnitRuleCount[ntID2]--;
							rules.get(ntID2).addAll(rules.get(ntID));
						}
						if (ntUnitRuleCount[ntID2] == 0) {
							q.add(ntID2);
							hasVisitedSymbol[ntID2] = true;
						}
					}
				}
			}
		}
		return this;
	}
	
	public CFG removeUnitRules3() {
		CFG g = new CFG();
		for (int ntID = 0; ntID < rules.size(); ntID++) {
			for (int bodyID : rules.get(ntID)) {
				if (!(bodies.get(bodyID).size() == 1 && isNonterminal(bodies.get(bodyID).get(0)))) {
					g.addRule(nts.get(ntID) + " -> " + bodyToString(bodyID));
				}
			}
		}
		return g;
	}
	
	private Queue<Integer> initiateQueueWithNonUnitNts() {
		Queue<Integer> q = new LinkedList<Integer>();
		for (int ntID = 0; ntID < nts.size(); ntID++) {
			if (ntUnitRuleCount[ntID] == 0) {
				q.add(ntID);
				hasVisitedSymbol[ntID] = true;
			}
		}
		return q;
	}
	
	private void setNtUnitRuleCount() {
		ntUnitRuleCount = new int[nts.size()];
		for (int ntID = 0; ntID < rules.size(); ntID++) {
			for (int bodyID : rules.get(ntID)) {
				if (bodies.get(bodyID).size() == 1 && isNonterminal(bodies.get(bodyID).get(0))) {
					ntUnitRuleCount[ntID]++;
				}
			}
		}
	}
	
	private boolean isEpsilon(String a) {
		return a.equals("epsilon");
	}
	
	private boolean isEpsilon(int a) {
		return a == epsilonID;
	}

	private boolean isTerminal(String a) {
		return !Character.isUpperCase(a.charAt(0)) && !a.equals("epsilon");
	}

	private boolean isTerminal(int a) {
		return a < -1;
	}
	
	private boolean isNonterminal(String a) {
		return Character.isUpperCase(a.charAt(0));
	}
	
	private boolean isNonterminal(int a) {
		return a >= 0;
	}

	/**
	 * Format the rules of this CFG into a string. Useful for printing the CFG.
	 */
	public String toString() {
		StringBuilder s = new StringBuilder("Rules: ");
		for (int i = 0; i < nts.size(); i++) {
			String head = nts.get(i);
			for (int bodyID : rules.get(i)) {
				s.append(head + " -> ");
				for (int symbolID : bodies.get(bodyID)) {
					s.append(symbolIDToString(symbolID));
				}
				s.append(", ");
			}
		}
		return s.delete(s.length() - 2, s.length()).append(".").toString();
	}

	/**
	 * Convert a body to a string, given its body ID.
	 */
	private String bodyToString(int bodyID) {
		StringBuilder s = new StringBuilder();
		for (int symbolID : bodies.get(bodyID)) {
			s.append(symbolIDToString(symbolID));
		}
		return s.toString();
	}
	
	/**
	 * Convert a symbol ID to its string representation.
	 */
	private String symbolIDToString(int symbolID) {
		if (isEpsilon(symbolID)) {
			return "epsilon";
		} else if (isTerminal(symbolID)) {
			return CFGParser.terminalToString(symbolID);
		} else {
			return nts.get(symbolID);
		}
	}
}