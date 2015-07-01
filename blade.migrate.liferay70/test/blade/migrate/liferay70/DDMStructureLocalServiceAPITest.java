
package blade.migrate.liferay70;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import blade.migrate.api.Problem;

public class DDMStructureLocalServiceAPITest
{
	final File testFile = new File( "projects/filetests/DDMStructureLocalServiceAPITest.java" );
	DDMStructureLocalServiceAPI ddmLocalService;

	@Before
	public void beforeTest()
	{
		assertTrue( testFile.exists() );
		ddmLocalService = new DDMStructureLocalServiceAPI();
		ddmLocalService.methodType = "invocation";
		ddmLocalService.methodName = "updateXSDFieldMetadata";
		ddmLocalService.methodExpression = "DDMStructureLocalService";
	}

    @Test
    public void DDMStructureLocalServicTest() throws Exception
    {
        List<Problem> problems = ddmLocalService.analyzeFile(testFile);

        assertNotNull( problems );
        assertEquals( 1, problems.size() );
    }

    @Test
    public void DDMStructureLocalServicTwice() throws Exception
    {
    	List<Problem> problems = ddmLocalService.analyzeFile(testFile);
    	problems = ddmLocalService.analyzeFile(testFile);

        assertNotNull( problems );
        assertEquals( 1, problems.size() );
    }
}
