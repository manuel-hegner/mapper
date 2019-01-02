package mapper.models.raw;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class RawLandmass {

	public static enum Type {landmass};
	
	private Type type;
	private RawPoint[] points;
}
