package net.dorokhov.pony.web.shared.response;

import java.io.Serializable;

public class ResponseWithResult<T> extends Response implements Serializable {

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
