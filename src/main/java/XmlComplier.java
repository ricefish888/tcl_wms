
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.vtradex.auth.util.DES;
import com.vtradex.wms.webservice.utils.CommonHelper;

/****
 * 五型页面编译类
 *
 */
public class XmlComplier {

	public static final String FILE_SEPARATOR = File.separator;

	public static String projectPath = "";// "/Users/administrator/workspace/tcl_wms";

	private static final String[] pathIncludes = { "/config/page/", "/config/rfPage/", "/config/pageWorkflow/" };
	private static final String deploy = FILE_SEPARATOR + "deploy" + FILE_SEPARATOR;
	private static final String config = FILE_SEPARATOR + "config" + FILE_SEPARATOR;

	private static List<File> getFileList(File res) {
		List<File> result = new ArrayList<File>();
		File[] files = res.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				if(pathname.isDirectory()) {
					return true;
				}
				String temp = pathname.getAbsolutePath();
				if (temp.endsWith(".xml")) {
					boolean include = false;
					for (int j = 0; j < pathIncludes.length; j++) {
						String t = pathIncludes[j];
						t = t.replace("/", FILE_SEPARATOR).replace("\\", FILE_SEPARATOR);
						if (temp.indexOf(t) > 0) {
							include = true;
							break;
						}
					}
					if (include) {
						File deployFile = new File(pathname.getAbsolutePath().replace(config, deploy));
						if (!deployFile.exists())
							include = true;
						else if (pathname.lastModified() <= deployFile.lastModified()) {
							include = false;
						}
					}
					return include;
				}
				return false;
			}
		});
		for (File f : files) {
			if (f.isDirectory()) {
				result.addAll(getFileList(f));
			} else {
				result.add(f);
			}
		}
		return result;
	}

	public static void complier(String projectPath) throws Exception {
		String resourcesPath = projectPath + "/src/main/resources/config";// 后面不能有/
		String deployPath = projectPath + "/src/main/resources/deploy";
		resourcesPath = resourcesPath.replace("/", FILE_SEPARATOR).replace("\\", FILE_SEPARATOR);
		deployPath = deployPath.replace("/", FILE_SEPARATOR).replace("\\", FILE_SEPARATOR);

		CommonHelper.log("开始编译"+resourcesPath);
		DES des = DES.getInstacne();

		File deployDir = new File(deployPath);
		if (!deployDir.exists()) {
			deployDir.mkdirs();
		}
		CommonHelper.log("开始复制不需要加密的文件");
		FileUtils.copyDirectory(new File(resourcesPath), deployDir, new FileFilter() {
			public boolean accept(File file) {
				String temp = file.getAbsolutePath();
				if (file.getName().endsWith(".xml")) {
					boolean include = false;
					for (int j = 0; j < pathIncludes.length; j++) {
						String t = pathIncludes[j];
						t = t.replace("/", FILE_SEPARATOR).replace("\\", FILE_SEPARATOR);
						if (temp.indexOf(t) > 0) {
							include = true;
							break;
						}
					}
					return !include;
				}
				return true;
			}
		});

		CommonHelper.log("开始获取需要加密的文件");
		File res = new File(resourcesPath);
		List<File> files = getFileList(res);
		CommonHelper.log("需要加密的文件数量为：" + files.size() + ",开始加密");
		for (File f : files) {
			System.out.println(f.getAbsolutePath());
			File deployFile = new File(f.getAbsolutePath().replace(config, deploy));
			des.encrypt(f, deployFile);
		}
		CommonHelper.log("加密完成,加密文件数" + files.size());

	}

	public static void main(String[] args) throws Exception {
		File directory = new File("");// 设定为当前文件夹
//		System.out.println(directory.getCanonicalPath());// 获取标准的路径
//		System.out.println(directory.getAbsolutePath());// 获取绝对路径

		String projectPath = directory.getAbsolutePath();
		CommonHelper.log("项目路径:"+projectPath);
		complier(projectPath);
	}

}
