import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class ControllerTest {
	Controller c;
	
	@Before
	public void setUp() throws Exception {
		
	}
	
	@Test
	public void clusterCoefficientTest() {
		try {
			c = new Controller("clustering_coefficient_graph");
			assertEquals(0.3333333333, c.getClusteringCoefficient(1), 0.000001);
			assertEquals(0.6666666666, c.getClusteringCoefficient(5), 0.000001);
			assertEquals(0, c.getClusteringCoefficient(2), 0.000001);
			assertEquals(1, c.getClusteringCoefficient(3), 0.000001);
			assertEquals(1, c.getClusteringCoefficient(4), 0.000001);
		} 
		catch (UserNotFoundException e) {
			assertTrue(false);
		}
		catch (IOException e) {
			assertTrue(false);
		}
	}

}
