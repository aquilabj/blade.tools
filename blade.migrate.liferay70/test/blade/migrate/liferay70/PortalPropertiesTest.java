package blade.migrate.liferay70;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import blade.migrate.api.Problem;

public class PortalPropertiesTest {
	final File file = new File(
			"projects/knowledge-base-portlet-6.2.x/docroot/WEB-INF/src/portal.properties");

	@Before
	public void beforeTest() {
		assertTrue(file.exists());
	}

	@Test
	public void portalPropertiesAnalyzeTest() throws Exception {
		List<Problem> problems = new PortalProperties().analyzeFile(file);

		assertNotNull(problems);
		assertEquals(1, problems.size());
		assertEquals(19, problems.get(0).lineNumber);
	}

	@Test
	public void portalPropertiesAnalyzeTest2() throws Exception {
		PortalProperties portalProperties = new PortalProperties();
		List<Problem> problems = portalProperties.analyzeFile(file);
		problems = portalProperties.analyzeFile(file);

		assertNotNull(problems);
		assertEquals(1, problems.size());
	}

}
