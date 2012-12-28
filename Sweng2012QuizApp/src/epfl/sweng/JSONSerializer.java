package epfl.sweng;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 */
public interface JSONSerializer {
	JSONObject toJSON() throws JSONException;
}
