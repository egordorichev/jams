package org.egordorichev.ldjam;

public class Language {
	public static String getArticle(String word) {
		if("aeiou".contains(word.subSequence(0, 1))) {
			return "an";
		}

		return "a";
	}

	public static String addArticle(String word) {
		if("aeiou".contains(word.subSequence(0, 1))) {
			return "an " + word;
		}

		return "a " + word;
	}
}