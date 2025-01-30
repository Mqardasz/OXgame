package lab.oxgame.model;

public enum OXEnum {
	O("O"),
	X("X"),
	BRAK("");

	String str;
	private OXEnum(String str) {
		this.str = str;
	}
	
	@Override
	public String toString() {
		return this.str;
	}
	
	public static OXEnum fromString(String value) {
		switch (value != null ? value.trim().toLowerCase() : "") {
		case "": return BRAK;
		case "o": return O;
		case "x": return X;
		default:
			throw new IllegalArgumentException(
				String.format("Niepoprawna wartosc %s", value));
		}
	}
	
	
}
