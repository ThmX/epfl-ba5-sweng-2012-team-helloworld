package epfl.sweng.test.utils;

import java.util.Arrays;
import java.util.HashSet;

import epfl.sweng.quizquestions.QuizQuestion;

/**
 * 
 * @author thmx
 * 
 */
public final class QuestionsBank {

	public final static QuizQuestion SWENG_CAP_GEO = new QuizQuestion(
			"What is the capital of Antigua and Barbuda? :o)",
			Arrays.asList("Chisinau", "Saipan", "St. John's", "Plymouth"),
			2,
			new HashSet<String>(Arrays.asList("capitals", "geography")),
			2973,
			"sweng"
	);
	
	public final static QuizQuestion SWENG_ABC_DEF = new QuizQuestion(
			"What is the capital of Antigua and Barbuda? :o)",
			Arrays.asList("Chisinau", "Saipan", "St. John's", "Plymouth"),
			2,
			new HashSet<String>(Arrays.asList("abc", "def")),
			2973,
			"sweng"
	);
	
	public final static QuizQuestion THMX_CAP_GEO = new QuizQuestion(
			"What is the capital of Antigua and Barbuda? :o)",
			Arrays.asList("Chisinau", "Saipan", "St. John's", "Plymouth"),
			2,
			new HashSet<String>(Arrays.asList("capitals", "geography")),
			2973,
			"thmx"
	);
	
	public final static QuizQuestion THMX_ABC_DEF = new QuizQuestion(
			"What is the capital of Antigua and Barbuda? :o)",
			Arrays.asList("Chisinau", "Saipan", "St. John's", "Plymouth"),
			2,
			new HashSet<String>(Arrays.asList("abc", "def")),
			2973,
			"thmx"
	);
	
	public final static QuizQuestion WRONG_CAP_GEO = new QuizQuestion(
			"What is the capital of Antigua and Barbuda? :o)",
			Arrays.asList("Chisinau", "Saipan", "St. John's", "Plymouth"),
			-1,
			new HashSet<String>(Arrays.asList("capitals", "geography")),
			2973,
			"wrong"
	);
}
