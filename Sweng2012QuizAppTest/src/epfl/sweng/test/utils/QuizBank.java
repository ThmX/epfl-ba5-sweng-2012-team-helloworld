package epfl.sweng.test.utils;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import epfl.sweng.quizquestions.QuizMessageException;
import epfl.sweng.quizzes.Quiz;
import epfl.sweng.quizzes.QuizException;

/**
 * 
 * @author thmx
 *
 */
public class QuizBank {

	public final static String AVAILABLE_QUIZZES_JSON = "["
			+ "  {"
			+ "    \"id\": 125,"
			+ "    \"title\": \"The hardest quiz ever\""
			+ "  }, {"
			+ "    \"id\": 453,"
			+ "    \"title\": \"Piece of cake\""
			+ "  }"
			+ "]";
	
	public final static String QUIZ_JSON = "{"
			+ "  \"id\": 125,"
			+ "  \"title\": \"The hardest quiz ever\","
			+ "  \"questions\": ["
			+ "    {"
			+ "      \"question\": \"How much is 2 + 2 ?\","
			+ "      \"answers\": ["
			+ "        \"5, for very large values of 2\","
			+ "        \"4, if you're out of inspiration\","
			+ "        \"10, for some carefully chosen base\""
			+ "      ]"
			+ "    }, {"
			+ "      \"question\": \"How much is 1 + 1 ?\","
			+ "      \"answers\": ["
			+ "        \"2\","
			+ "        \"10\","
			+ "        \"11\","
			+ "        \"It all depends on the semantics of the '+' operator\""
			+ "      ]"
			+ "    }"
			+ "  ]"
			+ "}";
	
	public static List<Quiz> stringToQuizList(String json) throws JSONException, QuizMessageException, QuizException {
		
		List<Quiz> quizzes = new LinkedList<Quiz>();
		
		JSONArray array = new JSONArray(json);
		
		int length = array.length();
		for (int i = 0; i < length; i++) {
			Quiz q = new Quiz(array.getJSONObject(i));
			
			quizzes.add(q);
		}
		
		return quizzes;
	}
}
