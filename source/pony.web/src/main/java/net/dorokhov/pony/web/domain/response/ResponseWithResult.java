package net.dorokhov.pony.web.domain.response;

public class ResponseWithResult<T> extends Response {

	private final T result;

	public ResponseWithResult() {
		this(null);
	}

	public ResponseWithResult(T aResult) {

		super(aResult != null);

		result = aResult;
	}

	public T getResult() {
		return result;
	}
}
