
package blade.migrate.liferay70;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import blade.migrate.api.Problem;

public class ChangesUserServicesThrownExceptionsTest
{
	final File testFile = new File( "projects/filetests/ChangesUserServicesThrownExceptionsTest.java" );
	ChangesUserServicesThrownExceptions cte;

	@Before
	public void beforeTest()
	{
		assertTrue( testFile.exists() );
		cte = new ChangesUserServicesThrownExceptions();
		cte.methodType = "exception";
		cte.methodName = "*";
		cte.methodExpression = "*";
		cte.methodParamTypes = new String[]{"DuplicateUserScreenNameException","DuplicateUserEmailAddressException"};
	}

    @Test
    public void ChangesUserServicesAnalyzeTest() throws Exception
    {
        List<Problem> problems = cte.analyzeFile(testFile);

        assertNotNull( problems );
        assertEquals( 2, problems.size() );
    }

    @Test
    public void ChangesUserServicesAnalyzeTestTwice() throws Exception
    {
    	List<Problem> problems = cte.analyzeFile(testFile);
    	problems = cte.analyzeFile(testFile);

        assertNotNull( problems );
        assertEquals( 2, problems.size() );
    }
}
