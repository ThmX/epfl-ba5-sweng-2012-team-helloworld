package epfl.sweng.test.utils.requests;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpClientConnection;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

import epfl.sweng.JSONSerializer;

/**
 * Mock for the Questions tests.
 */
public class MockJSONsExecutor extends AbstractMockExecutor {

	private List<? extends JSONSerializer> mSerializers;

	public MockJSONsExecutor() {
		this(null);
	}
	
	public MockJSONsExecutor(List<? extends JSONSerializer> serializers) {
		this(serializers, false);
	}

	public MockJSONsExecutor(List<? extends JSONSerializer> serializers, boolean authenticated) {
		super(authenticated);
		this.mSerializers = serializers;
	}


	@Override
	public HttpResponse execute(final HttpRequest request, final HttpClientConnection conn, final HttpContext context)
		throws IOException {

		if (this.mSerializers == null) {
			Log.i(getClass().getSimpleName(), "null serializers");
			return generateResponse(HTTP_NOT_FOUND);
		}

		try {
			
			HttpResponse response = super.execute(request, conn, context);
			
			JSONArray array = new JSONArray();
			
			for (JSONSerializer q: this.mSerializers) {
				array.put(q.toJSON());
			}
			
			response.setEntity(new StringEntity(array.toString()));
			return response;
			
		} catch (JSONException e) {
			// Should not happen :-/
			e.printStackTrace();
		}

		return generateResponse(HTTP_NOT_FOUND);
	}

	public void setQuestions(List<? extends JSONSerializer> serializers) {
		this.mSerializers = serializers;
	}
}