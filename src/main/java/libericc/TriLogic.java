package libericc;

public enum TriLogic {
	True, False, Unknown;
	
	static public TriLogic toTriLogic(boolean b) {
		if (b) return True;
		else return False;
	}
	
	public boolean isTrue() {
		return this == True;
	}
	
	public boolean isFalse() {
		return this == False;
	}
	
	public boolean isUnknown() {
		return this == Unknown;
	}
	
	public TriLogic not(){
		switch (this) {
		case True: return False;
		case False: return True;
		default: return Unknown;
		}
	}
	
	public TriLogic and(TriLogic rhs) {
		if (this == True && rhs == True) return True;
		if (this == False || rhs == False) return False;
		return Unknown;
	}
	
	public TriLogic or(TriLogic rhs) {
		if (this == True || rhs == True) return True;
		if (this == False && rhs == False) return False;
		return Unknown;
	}
	
	static public TriLogic parseString(String str) {
		if ("True".equals(str)) return True;
		else if ("False".equals(str)) return False;
		else return Unknown;
	}
	
}
