package mapper.models.description;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mapper.models.math.LatLong;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter @ToString(onlyExplicitlyIncluded=true)
public class Landmass {
	@Getter @RequiredArgsConstructor
	public static enum Type {
		LAND("black", 0.8),
		ICE("white", 0.5);
		
		private final String cSSColor;
		private final double fractalStrength;
	};
	
	@ToString.Include
	private Type type;
	private List<LatLong> points;
}
