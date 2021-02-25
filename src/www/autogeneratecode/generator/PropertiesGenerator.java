package www.autogeneratecode.generator;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

		File file = new File(dir, psiClass.getName() + ".properties");
		file = IOUtils.write(file, getSource_zh_cn());
		System.out.println("Generate JavaFile source path:" + file.getPath());
		return file;
	}

	
	public File write_en(File dir) throws IOException {

		dir.mkdirs();

		File file = new File(dir, psiClass.getName() + "_en.properties");
		file = IOUtils.write(file, getSource_en());
		System.out.println("Generate JavaFile source path:" + file.getPath());
		return file;
	}
	
	public File write_zh_cn(File dir) throws IOException {

		dir.mkdirs();

		File file = new File(dir, psiClass.getName() + "_zh_CN.properties");
		file = IOUtils.write(file, getSource_zh_cn());
		System.out.println("Generate JavaFile source path:" + file.getPath());
		return file;
	}

	
	private String getSource_en() {
		return generateSource(true);
	}
	private String getSource_zh_cn() {
		return generateSource(false);
	}

	private String getNewTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(new Date());
	}
	private String generateSource(boolean isEnglish) {

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
			if(isEnglish) {
				sb.append(fieldName);
			}else {
				psiAnnotation = psiField.getAnnotation("www.autogeneratecode.model.Comment");
				commentName = getAnnotateText(psiAnnotation, "content", "");
				sb.append(commentName);
			}
		}
		
		return sb.toString();
	}


}
