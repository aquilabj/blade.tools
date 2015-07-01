package blade.migrate.liferay70;

public class DDMStructureLocalServiceUtilTest {

	public static void main(String[] args) {
		try
		{
			DDMStructureLocalService localService = new DDMStructureLocalServiceWrapper();
			localService.updateXSDFieldMetadata(
					structureId,
					fieldName,
					metadataEntryName,
					metadataEntryValue,
					serviceContext);
		}
		catch (DuplicateUserScreenNameException e)
		{
			
		}
	}
}
