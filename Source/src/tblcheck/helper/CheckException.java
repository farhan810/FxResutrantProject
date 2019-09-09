package tblcheck.helper;

public class CheckException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ExceptionType type;

	public CheckException(ExceptionType type, String message) {
		super(message);
		this.type = type;
	}

	public ExceptionType getType() {
		return type;
	}

}
