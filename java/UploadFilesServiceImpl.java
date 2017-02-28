
/**
 * 文件上传接口实现
 */

@Transactional
@Service("uploadFilesService")
public class UploadFilesServiceImpl implements UploadFilesService{

	@Override
	public List<FileRelation> uploadImg(List<File> fileList, List<String> fileNameList, String bizId, String bizType, String userId, String areaCode) throws Exception{
		List<FileRelation> fileRelationList = new ArrayList<FileRelation>();
		String relationId = Dbid.getID();
		if(fileList.size()>0){ //保存上传的图片
			for(int i=0;i<fileList.size();i++){
				File file = fileList.get(i);
				String fileName = fileNameList.get(i);
				if(file == null || !file.exists() || !file.isFile()){
					throw new Exception("上传附件出错file值为null、不存在或不是一个文件");
				}
				if(StringUtils.isBlank(fileName) || fileName.length() > 2048 || fileName.contains("\\") 
						|| fileName.contains("/") || fileName.contains(":") || fileName.contains("*") || fileName.contains("?") 
						|| fileName.contains("\"") || fileName.contains("<") || fileName.contains(">") || fileName.contains("|")){
					throw new Exception("上传附件出错fileName值为null或超出2048位长度、或文件名含特殊字符无法上传");
				}
				String attachmentFolder = this.getAttachmentFolderPath(userId, bizId, bizType);
				//创建附件的所有文件夹
				File folder = new File(attachmentFolder);
				File descFile = new File(folder.getAbsolutePath() , StringHelper.getString(relationId, "." , FileHelper.getExtendName(fileName)));
				if(!folder.exists() || !folder.isDirectory()){
					folder.mkdirs();
				}else{
					this.clearSameNameFile(bizId,fileName,folder.getAbsolutePath());
				}
				//磁盘保存附件
				FileUtil.copyFile(file, descFile);
				//保存关系信息DB
				FileRelation fileRelation = new FileRelation(relationId);
				fileRelation.setCreateTime(TimeHelper.getCurrentTime());
				fileRelation.setBizId(bizId);
				fileRelation.setBizType(bizType);
				fileRelation.setFileUser(userId);
				fileRelation.setFileType(FileHelper.getExtendName(fileName));
				fileRelation.setFileName(fileName);
				fileRelation.setFileSize((int)descFile.length());
				fileRelation.setFilePath(StringHelper.getString(attachmentFolder, File.separatorChar, StringHelper.getString(relationId, ".", fileRelation.getFileType())));
				fileRelation.setFileUrl(this.getAttachmentUrlPath(relationId));
				fileRelation.setFileCode(areaCode);
				fileRelation.setStste("1");
				this.save(fileRelation);
				fileRelationList.add(fileRelation);
			}
			return fileRelationList;
		}else{
			return null;
		}
	}
}