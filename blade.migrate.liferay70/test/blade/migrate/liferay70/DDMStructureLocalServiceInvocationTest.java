
package blade.migrate.liferay70;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import blade.migrate.api.Problem;

public class DDMStructureLocalServiceInvocationTest
{
	final File testFile = new File( "projects/filetests/DDMStructureLocalServiceAPITest.java" );
	DDMStructureLocalServiceAPI ddm;

	@Before
	public void beforeTest()
	{
		assertTrue( testFile.exists() );
		ddm = new DDMStructureLocalServiceAPI();
	}

    @Test
    public void DDMTemplateAnalyzeTest() throws Exception
    {
        List<Problem> problems = ddm.analyzeFile(testFile);

        assertNotNull( problems );
        assertEquals( 1, problems.size() );
        assertEquals( 7,problems.get(0).lineNumber);
        assertEquals( 144,problems.get(0).startOffset);
        assertEquals( 270,problems.get(0).endOffset);
    }

}
