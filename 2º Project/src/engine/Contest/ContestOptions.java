package engine.Contest;

public class ContestOptions {

	public enum poolDimensions {
		SHORT(25),
		LONG(50);

		private final int length;

		poolDimensions(int length) {
			this.length = length;
		}

		public int getLenght() {
			return length;
		}
	}

	public enum gender {
		FEMALE('F'),
		MALE('M');

		private final char gender;

		gender(char c) {
			this.gender = c;
		}
	}

	public enum SwimmingStyle {
		BUTTERFLY(1, "Butterfly"),
		BACKSTROKE(2, "Backstrocke"),
		BREASTSTROKE(3, "Breaststroke"),
		FREESTYLE(4, "Freestyle"),
		MEDLEY(5, "Medley");

		private final int swimmingType;
		private final String typeName;

		SwimmingStyle(int style, String name) {
			this.swimmingType = style;

			this.typeName = name;
		}

		public String getTypeName () {
			return typeName;
		}

		public SwimmingStyle getSwimmingStyle(String style) {      	
			if(style.equals("Butterfly")) {
				return BUTTERFLY;
			}
			else if(style.equals("Backstrocke")) {
				return BACKSTROKE;
			}
			else if(style.equals("Breaststroke")) {
				return BREASTSTROKE;
			}
			else if(style.equals("Freestyle")) {
				return FREESTYLE;
			}
			else if(style.equals("Medley")) {
				return MEDLEY;
			}
			else {
				return null;
			}
		}
	}
}
