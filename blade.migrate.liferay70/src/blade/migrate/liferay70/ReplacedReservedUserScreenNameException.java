package blade.migrate.liferay70;

import blade.migrate.api.FileMigrator;
import blade.migrate.core.JavaFileChecker;
import blade.migrate.core.JavaFileMigrator;
import blade.migrate.core.SearchResult;

import java.io.File;
import java.util.List;

import org.osgi.service.component.annotations.Component;

@Component(
		property = {
			"file.extensions=java",
			"problem.title=Replaced the ReservedUserScreenNameException",
			"problem.url=https://github.com/liferay/liferay-portal/blob/master/readme/7.0/BREAKING_CHANGES.markdown#replaced-the-reserveduserscreennameexception-with-userscreennameexceptionmustnotbereserved-in-userlocalservice",
			"problem.summary=Replaced the ReservedUserScreenNameException with UserScreenNameException.MustNotBeReserved in UserLocalService",
			"problem.tickets=LPS-53113",
		},
		service = FileMigrator.class
)
public class ReplacedReservedUserScreenNameException extends JavaFileMigrator {

	@Override
	protected List<SearchResult> searchJavaFile(File file) {
		final JavaFileChecker javaFileChecker = new JavaFileChecker(file);

		return javaFileChecker.findCatchExceptions(new String[] {"ReservedUserScreenNameException"});
	}
}