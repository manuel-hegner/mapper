package mapper.models.math;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class MercatorTest {

	@ParameterizedTest @CsvSource(value= {
		" 0,-180",
		" 1,-144",
		" 2,-108",
		" 3, -72",
		" 4, -36",
		" 5,   0",
		" 6,  36",
		" 7,  72",
		" 8, 108",
		" 9, 144",
		"10, 180",
	})
	public void testX(long in, double expected) {
		assertThat(Mercator.pixelXToLongitude(in, 10)).isCloseTo(expected, within(0.001));
		assertThat(Mercator.longitudeToPixelX(expected, 10)).isCloseTo(in, within(0.001));
	}
	
	@ParameterizedTest @CsvSource(value= {
		" 0, 85.05",
		" 1, 80.74",
		" 2, 72.73",
		" 3, 58.23",
		" 4, 33.84",
		" 5,  0",
		" 6,-33.84",
		" 7,-58.23",
		" 8,-72.73",
		" 9,-80.74",
		"10,-85.05",
	})
	public void testY(long in, double expected) {
		assertThat(Mercator.pixelYToLatitude(in, 10)).isCloseTo(expected, within(0.01));
		assertThat(Mercator.latitudeToPixelY(expected, 10)).isCloseTo(in, within(0.01));
	}
}