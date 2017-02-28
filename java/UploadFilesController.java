
/**
 * 文件上传控制类
 */

@Controller
@RequestMapping(value = "/upload")
public class UploadFilesController {

	@Autowired
	private UploadFilesService uploadFilesService;

	/**
	 * 附件上传
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	
	@RequestMapping("/uploadImg")
	public void uploadImg(HttpServletRequest request,HttpServletResponse response)throws Exception{
		
		Map<String, Object> map=new HashMap<String, Object>();
		boolean isOk = false;
		User user = (User) request.getSession().getAttribute("user");
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		String id = Dbid.getID();
		// 检查form是否有enctype="multipart/form-data"
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			List<MultipartFile> multipartFileList = multiRequest.getFiles("imgFile");
			if (multipartFileList.size() > 0) {
				for (int i = 0; i < multipartFileList.size(); i++) {
					List<File> fileList = new ArrayList<File>();
					List<String> fileNameList = new ArrayList<String>();
					MultipartFile multipartFile = multipartFileList.get(i);
					CommonsMultipartFile commonsMultipartFile = (CommonsMultipartFile) multipartFile;
					DiskFileItem diskFileItem = (DiskFileItem) commonsMultipartFile.getFileItem();
					fileList.add(diskFileItem.getStoreLocation());
					fileNameList.add(diskFileItem.getName());
					uploadFilesService.uploadImg(fileList, fileNameList, id, "02", user.getUserId(), user.getAreaCode()); 
				}
				isOk = true;
				map.put("status", true);
				map.put("id", id);
			}
		}
		if(!isOk){
			map.put("status", false);
			map.put("id", "");
		}
		response.getWriter().write(JSONObject.toJSONString(map));
	}