

/**
 * 文件上传接口
 */
public interface UploadFilesService{
	/**
	 * 文件上传方法
	 */
	public List<FileRelation> uploadImg(List<File> fileList, List<String> fileNameList, String bizId, String bizType, String userId, String areaCode) throws Exception;
}