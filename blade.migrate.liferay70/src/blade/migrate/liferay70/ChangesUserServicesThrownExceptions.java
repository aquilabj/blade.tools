package blade.migrate.liferay70;

import blade.migrate.api.FileMigrator;

import org.osgi.service.component.annotations.Component;

@Component(
		property = {
			"file.extension=java",
			"method.type=exception",
			"method.name=*",
			"method.expression=*",
			"method.param.types=DuplicateUserScreenNameException,DuplicateUserEmailAddressException",
			"problem.title=Changes in Exceptions Thrown by User Services",
			"problem.url=https://github.com/liferay/liferay-portal/blob/master/readme/7.0/BREAKING_CHANGES.markdown#changes-in-exceptions-thrown-by-user-services",
			"problem.summary=Changes in Exceptions Thrown by User Services",
			"problem.type=java",
			"problem.tickets=LPS-47130",
		},
		service = FileMigrator.class
)
public class ChangesUserServicesThrownExceptions extends JavaMethodMigrator {
}