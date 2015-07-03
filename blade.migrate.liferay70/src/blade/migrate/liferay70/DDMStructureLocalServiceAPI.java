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
		"file.extension=java",
		"problem.summary=DDM Structure Local Service API No Longer Has the updateXSDFieldMetadata operation",
		"problem.tickets= LPS-47559",
		"problem.title=DDM Structure Local Service API No Longer Has the updateXSDFieldMetadata operation", 
		"problem.url=https://github.com/liferay/liferay-portal/blob/master/readme/7.0/BREAKING_CHANGES.markdown#ddm-structure-local-service-api-no-longer-has-the-updatexsdfieldmetadata-operation"
	},
	service = FileMigrator.class
)
public class DDMStructureLocalServiceAPI extends JavaFileMigrator {

	@Override
	protected List<SearchResult> searchJavaFile(File file) {
		final JavaFileChecker javaFileChecker = new JavaFileChecker(file);

		return javaFileChecker.findMethodInvocations(null, "DDMStructureLocalServiceUtil", "updateXSDFieldMetadata",null);
	}
}