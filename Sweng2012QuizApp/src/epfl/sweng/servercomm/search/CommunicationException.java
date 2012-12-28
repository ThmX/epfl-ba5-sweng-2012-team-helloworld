package epfl.sweng.servercomm.search;

/**
 * Exception thrown in case of an error during the server communication.
 * 
 * DO NOT CHANGE THIS FILE. ANY CHANGES WILL CAUSE OUR TESTS TO FAIL AND YOU
 * WILL LOSE ALL THE POINTS.
 *
 */
public class CommunicationException extends Exception {

    private static final long serialVersionUID = 166965688776926015L;

    public CommunicationException() {
    }

    public CommunicationException(String detailMessage) {
        super(detailMessage);
    }

    public CommunicationException(Throwable throwable) {
        super(throwable);
    }

    public CommunicationException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

}
