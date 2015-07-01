package blade.migrate.liferay70;

import org.osgi.service.component.annotations.Component;

import blade.migrate.api.FileMigrator;

@Component(
		property = {
			"file.extension=java",
			"method.type=invocation",
			"method.name=updateXSDFieldMetadata",
			"method.expression=DDMStructureLocalService",
			"problem.title=DDM Structure Local Service API No Longer Has the updateXSDFieldMetadata operation",
			"problem.url=https://github.com/liferay/liferay-portal/blob/master/readme/7.0/BREAKING_CHANGES.markdown#ddm-structure-local-service-api-no-longer-has-the-updatexsdfieldmetadata-operation",
			"problem.summary=DDM Structure Local Service API No Longer Has the updateXSDFieldMetadata operation",
			"problem.type=java",
			"problem.tickets=LPS-47559",
		},
		service = FileMigrator.class
)
public class DDMStructureLocalServiceAPI extends JavaMethodMigrator {

}
