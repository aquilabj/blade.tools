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
			"problem.title=Replaced the ReservedUserEmailAddressException with UserEmailAddressException",
			"problem.url=https://github.com/liferay/liferay-portal/blob/master/readme/7.0/BREAKING_CHANGES.markdown#replaced-the-reserveduseremailaddressexception-with-useremailaddressexception-inner-classes-in-user-services",
			"problem.summary=Replaced the ReservedUserEmailAddressException with UserEmailAddressException Inner Classes in User Services",
			"problem.tickets=LPS-53279",
		},
		service = FileMigrator.class
)
public class ReplacedReservedUserEmailAddressException extends JavaFileMigrator {

	@Override
	protected List<SearchResult> searchJavaFile(File file) {
		final JavaFileChecker javaFileChecker = new JavaFileChecker(file);

		return javaFileChecker.findCatchExceptions(new String[] {"ReservedUserEmailAddressException"});
	}
}