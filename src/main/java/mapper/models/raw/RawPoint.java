package mapper.models.raw;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class RawPoint {
	private long x;
	private long y;
	
	@JsonCreator
	public RawPoint(long[] coordinates) {
		if(coordinates.length != 2)
			throw new IllegalArgumentException();
		x = coordinates[0];
		y = coordinates[1];
	}
	
	@JsonValue
	public long[] toArray() {
		return new long[] {x, y};
	}
}
