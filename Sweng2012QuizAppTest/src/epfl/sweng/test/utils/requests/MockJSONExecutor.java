package epfl.sweng.test.utils.requests;

import java.io.IOException;

import org.apache.http.HttpClientConnection;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;

import epfl.sweng.JSONSerializer;

/**
 * Mock for the Rating tests.
 */
public class MockJSONExecutor extends AbstractMockExecutor {

	private JSONSerializer mSerializer;

	public MockJSONExecutor() {
		this(null);
	}
	
	public MockJSONExecutor(JSONSerializer serializer) {
		this(serializer, false);
	}

	public MockJSONExecutor(JSONSerializer serializer, boolean authenticated) {
		super(authenticated);
		this.mSerializer = serializer;
	}


	@Override
	public HttpResponse execute(final HttpRequest request, final HttpClientConnection conn, final HttpContext context)
		throws IOException {

		try {
			if (this.mSerializer == null) {
				return generateResponse(HTTP_NOT_FOUND);
			}
			
			HttpResponse response = super.execute(request, conn, context);
			response.setEntity(new StringEntity(this.mSerializer.toJSON().toString()));
			return response;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return generateResponse(HTTP_NOT_FOUND);
	}
}