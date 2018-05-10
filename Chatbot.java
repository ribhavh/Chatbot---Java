
import java.util.*;
import java.io.*;

public class Chatbot {
	private static String filename = "/Users/RibhavHora/Documents/WARC201709_wid.txt";
	private static Scanner sc;
	private static ArrayList<Integer> corpus = new ArrayList<Integer>();

	private static ArrayList<Integer> readCorpus() {

		try {
			File f = new File(filename);
			sc = new Scanner(f);
			while (sc.hasNext()) {
				if (sc.hasNextInt()) {
					int i = sc.nextInt();
					corpus.add(i);
				} else {
					sc.next();
				}
			}
		} catch (FileNotFoundException ex) {
			System.out.println("File Not Found.");
		}
		return corpus;
	}

	static public void main(String[] args) {
		ArrayList<Integer> corpus = readCorpus();
		int flag = Integer.valueOf(args[0]);
		if (flag == 100) {
			int w = Integer.valueOf(args[1]);
			double[] p = returnUni();
			int count = (int) (p[w] * corpus.size());
			System.out.println(count);
			System.out.println(String.format("%.7f", p[w]));
		} else if (flag == 200) {
			int n1 = Integer.valueOf(args[1]);
			int n2 = Integer.valueOf(args[2]);
			double prob = (n1 * 1.0) / (n2 * 1.0);
			double[] ans = checkIntervals(1, prob, 0, 0, 0);
			System.out.println((int) ans[0]);
			System.out.println(String.format("%.7f", ans[1]));
			System.out.println(String.format("%.7f", ans[2]));
		} else if (flag == 300) {
			int h = Integer.valueOf(args[1]);
			int w = Integer.valueOf(args[2]);
			List<Double> list = returnBi(h);
			double size = list.get(list.size() - 1);
			list.remove(list.size() - 1);
			System.out.println((int) (size * list.get(w)));
			System.out.println((int) size);
			System.out.println(String.format("%.7f", list.get(w)));
		} else if (flag == 400) {
			int n1 = Integer.valueOf(args[1]);
			int n2 = Integer.valueOf(args[2]);
			int h = Integer.valueOf(args[3]);
			double prob = (n1 * 1.0) / (n2 * 1.0);
			double[] ans = checkIntervals(2, prob, h, 0, 0);
			System.out.println((int) ans[0]);
			System.out.println(String.format("%.7f", ans[1]));
			System.out.println(String.format("%.7f", ans[2]));

		} else if (flag == 500) {
			int h1 = Integer.valueOf(args[1]);
			int h2 = Integer.valueOf(args[2]);
			int w = Integer.valueOf(args[3]);
			List<Double> list = returnTri(h1, h2);
			double size = list.get(list.size() - 1);
			list.remove(list.size() - 1);
			System.out.println((int) (size * list.get(w)));
			System.out.println((int) size);
			if (size == 0.0)
				System.out.println("undefined");
			else
				System.out.println(String.format("%.7f", list.get(w)));
		} else if (flag == 600) {
			int n1 = Integer.valueOf(args[1]);
			int n2 = Integer.valueOf(args[2]);
			int h1 = Integer.valueOf(args[3]);
			int h2 = Integer.valueOf(args[4]);
			double prob = (n1 * 1.0) / (n2 * 1.0);
			double[] ans = checkIntervals(3, prob, 0, h1, h2);
			if (ans[2] == 0.0)
				System.out.println("undefined");
			else {
				System.out.println((int) ans[0]);
				System.out.println(String.format("%.7f", ans[1]));
				System.out.println(String.format("%.7f", ans[2]));
			}
		}

		else if (flag == 700) {
			int seed = Integer.valueOf(args[1]);
			int t = Integer.valueOf(args[2]);
			int h1 = 0, h2 = 0;
			Random rng = new Random();
			if (seed != -1)
				rng.setSeed(seed);
			if (t == 0) {
				double r = rng.nextDouble();
				double[] ans = checkIntervals(1, r, 0, 0, 0);
				h1 = (int) ans[0];
				System.out.println(h1);
				if (h1 == 9 || h1 == 10 || h1 == 12) {
					return;
				}
				r = rng.nextDouble();
				ans = checkIntervals(2, r, h1, 0, 0);
				h2 = (int) ans[0];
				System.out.println(h2);
			} else if (t == 1) {
				h1 = Integer.valueOf(args[3]);
				// TODO Generate second word using r
				double r = rng.nextDouble();
				double[] ans = checkIntervals(2, r, h1, 0, 0);
				h2 = (int) ans[0];
				System.out.println(h2);
			} else if (t == 2) {
				h1 = Integer.valueOf(args[3]);
				h2 = Integer.valueOf(args[4]);
			}

			while (h2 != 9 && h2 != 10 && h2 != 12) {
				double r = rng.nextDouble();
				int w = 0;
				double[] ans = checkIntervals(3, r, 0, h1, h2);
				w = (int) ans[0];
				System.out.println(w);
				h1 = h2;
				h2 = w;
			}
		}
	}

	private static double[] returnUni() {
		double[] p = new double[4700];
		for (int i = 0; i < p.length; i++) {
			for (int j = 0; j < corpus.size(); j++) {
				if (corpus.get(j) == i)
					p[i]++;
			}
			p[i] = p[i] / (double) corpus.size();
		}
		return p;
	}

	private static List<Double> returnBi(int h) {
		List<Double> list = new ArrayList<Double>();
		ArrayList<Integer> words_after_h = new ArrayList<Integer>();
		for (int j = 1; j < corpus.size(); j++) {
			if (corpus.get(j - 1) == h)
				words_after_h.add(corpus.get(j));
		}
		for (int i = 0; i < 4700; i++) {
			int count = 0;
			for (int k = 0; k < corpus.size() - 1; k++) {
				if (corpus.get(k) == h && corpus.get(k + 1) == i)
					count++;
			}
			list.add(count / (double) words_after_h.size());
		}
		list.add(words_after_h.size() * 1.0);
		return list;
	}

	private static List<Double> returnTri(int h1, int h2) {
		List<Double> list = new ArrayList<Double>();
		ArrayList<Integer> words_after_h = new ArrayList<Integer>();
		for (int j = 2; j < corpus.size(); j++) {
			if (corpus.get(j - 1) == h2 && corpus.get(j - 2) == h1)
				words_after_h.add(corpus.get(j));
		}
		for (int i = 0; i < 4700; i++) {
			int count = 0;
			for (int k = 0; k < corpus.size() - 2; k++) {
				if (corpus.get(k) == h1 && corpus.get(k + 1) == h2 && corpus.get(k + 2) == i)
					count++;
			}
			list.add(count / (double) words_after_h.size());
		}
		list.add(words_after_h.size() * 1.0);
		return list;
	}

	public static double[] checkIntervals(int option, double prob, int h, int h1, int h2) {
		double[] answer = new double[3];
		if (option == 1) {
			double[] p = returnUni();
			if (prob >= 0.0 && prob <= p[0]) {
				answer[0] = 0;
				answer[1] = 0.0;
				answer[2] = p[0];
			} else {
				for (int i = 1; i < p.length; i++) {
					// left is the summation of 0 to i - 1
					Double left = 0.0;
					Double right = 0.0;
					for (int j = 0; j <= i - 1; j++) {
						left = left + p[j];
						right = right + p[j];
						if (j == i - 1) {
							right = right + p[j + 1];
						}
					}
					if (prob > left && prob <= right) {
						answer[0] = i;
						answer[1] = left;
						answer[2] = right;
						break;
					}
				}
			}
		} else if (option == 2) {
			List<Double> bigram = returnBi(h);
			bigram.remove(bigram.size() - 1);
			int start = 0;
			for (int i = 0; i < bigram.size(); i++) {
				if (bigram.get(i) != 0) {
					start = i;
					break;
				}
			}
			if (prob >= 0.0 && prob <= bigram.get(start)) {
				answer[0] = start;
				answer[1] = 0.0;
				answer[2] = bigram.get(start);
			} else {
				for (int i = start + 1; i < bigram.size(); i++) {
					// left is the summation of 0 to i - 1
					Double left = 0.0;
					Double right = 0.0;
					if (bigram.get(i) != 0) {
						for (int j = 0; j <= i - 1; j++) {
							left = left + bigram.get(j);
							right = right + bigram.get(j);
							if (j == i - 1) {
								right = right + bigram.get(j + 1);
							}

						}
						if (prob > left && prob <= right) {
							answer[0] = i;
							answer[1] = left;
							answer[2] = right;
							break;
						}
					}
				}
			}

		} else {
			List<Double> trigram = returnTri(h1, h2);
			trigram.remove(trigram.size() - 1);
			int start = 0;
			for (int i = 0; i < trigram.size(); i++) {
				if (trigram.get(i) != 0) {
					start = i;
					break;
				}

			}
			if (prob >= 0.0 && prob <= trigram.get(start)) {
				answer[0] = start;
				answer[1] = 0.0;
				answer[2] = trigram.get(start);
			} else {
				for (int i = start + 1; i < trigram.size(); i++) {
					// left is the summation of 0 to i - 1
					Double left = 0.0;
					Double right = 0.0;
					if (trigram.get(i) != 0) {
						for (int j = 0; j <= i - 1; j++) {
							left = left + trigram.get(j);
							right = right + trigram.get(j);
							if (j == i - 1) {
								right = right + trigram.get(j + 1);
							}
						}
						if (prob > left && prob <= right) {
							answer[0] = i;
							answer[1] = left;
							answer[2] = right;
							break;
						}
					}
				}
			}
		}
		return answer;
	}

}