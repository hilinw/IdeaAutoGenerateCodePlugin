package www.autogeneratecode.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.intellij.psi.impl.source.PsiJavaFileImpl;
import www.autogeneratecode.utils.IOUtils;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiField;

public class PropertiesGenerator extends JavaFileGenerator {


	public PropertiesGenerator(PsiJavaFileImpl psiJavaFileImpl, String packageName) {
		super(psiJavaFileImpl, packageName);

	}

	@Override
	protected void generateJavaFile() {

	}
	
	@Override
	public File write(File dir) throws IOException {
		dir.mkdirs();

		File file = new File(dir, psiClass.getName() + "VO.properties");
		file = IOUtils.write(file, getSource());
		System.out.println("Generate JavaFile source path:" + file.getPath());
		return file;
	}

	
	public File write_en(File dir) throws IOException {

		dir.mkdirs();

		File file = new File(dir, psiClass.getName() + "VO_en.properties");
		FileOutputStream out = null;
		Properties resource = getSource_en();
		try {
			out = new FileOutputStream(file);
			resource.store(out, (String) null);
		} finally {
			IOUtils.close(out);
		}
		System.out.println("Generate JavaFile source path:" + file.getPath());
		return file;
	}
	
	public File write_zh_cn(File dir) throws IOException {

		dir.mkdirs();

		File file = new File(dir, psiClass.getName() + "VO_zh_CN.properties");
		FileOutputStream out = null;
		Properties resource = getSource_zh_cn();
		try {
			out = new FileOutputStream(file);
			resource.store(out, (String) null);
		} finally {
			IOUtils.close(out);
		}
		System.out.println("Generate JavaFile source path:" + file.getPath());
		return file;
	}

	private String getSource() {
		return generateSource();
	}
	private Properties getSource_en() {
		return generateSource(true);
	}
	private Properties getSource_zh_cn() {
		return generateSource(false);
	}

	private String getNewTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(new Date());
	}
	private Properties generateSource(boolean isEnglish) {
		Properties resource = new Properties();

		
		//类名，用Vo名
		String fileName = psiClass.getName()+"VO";
		PsiField[] psiAllFields = psiClass.getAllFields();
		//字段名
		String fieldName = "";
		//备注
		PsiAnnotation psiAnnotation = null;
		String commentName = "";
		for (PsiField psiField : psiAllFields) {
			fieldName = psiField.getName();
			if(isEnglish) {
				commentName = fieldName;
			}else {
				psiAnnotation = psiField.getAnnotation("www.autogeneratecode.model.Comment");
				commentName = getAnnotateText(psiAnnotation, "content", "");
				if (StringUtils.isBlank(commentName)) {
					commentName = fieldName;
	            }
			}
			resource.put(fileName + "." + fieldName, commentName);
		}
		
		return resource;
	}

	private String generateSource() {

		StringBuilder sb = new StringBuilder();
		sb.append("### CreatTime:");
		sb.append(getNewTime());

		//类名，用Vo名
		String fileName = psiClass.getName()+"VO";
		PsiField[] psiAllFields = psiClass.getAllFields();
		//字段名
		String fieldName = "";
		//备注
		PsiAnnotation psiAnnotation = null;
		String commentName = "";
		for (PsiField psiField : psiAllFields) {

			fieldName = psiField.getName();
			sb.append("\n");
			sb.append(fileName).append(".");
			sb.append(fieldName).append("=");

			psiAnnotation = psiField.getAnnotation("www.autogeneratecode.model.Comment");
			commentName = getAnnotateText(psiAnnotation, "content", "");
			if (StringUtils.isBlank(commentName)) {
				commentName = fieldName;
			}
			sb.append(commentName);
		}
		return sb.toString();
	}


}
