package www.autogeneratecode.generator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiField;
import com.intellij.psi.impl.source.PsiJavaFileImpl;
import www.autogeneratecode.model.Column;
import www.autogeneratecode.model.Comment;
import www.autogeneratecode.utils.IOUtils;

public class JavaServiceGenerator extends JavaFileGenerator {

	public JavaServiceGenerator(PsiJavaFileImpl psiJavaFileImpl, String packageName) {
		super(psiJavaFileImpl, packageName);

	}

	@Override
	protected void generateJavaFile() {

	}

	public File write(File dir) throws IOException {

		dir.mkdirs();

		File file = new File(dir, psiClass.getName() + "Service" + ".java");
		file = IOUtils.write(file, getInterfaceSource());
		System.out.println("Generate JavaFile source path:" + file.getPath());
		return file;
	}

	public File writeImpl(File dir) throws IOException {

		dir.mkdirs();

		File file = new File(dir, psiClass.getName() + "ServiceImpl" + ".java");
		file = IOUtils.write(file, getImplSource());
		System.out.println("Generate JavaFile source path:" + file.getPath());
		return file;
	}

	private String getInterfaceSource() {

		StringBuilder sb = new StringBuilder();
		String sourceName = psiClass.getName();
		String varSourceName = sourceName.substring(0, 1).toLowerCase() + sourceName.substring(1);
		String voName = sourceName + "VO";
		String varVoName = varSourceName + "VO";
//		String daoName = sourceName +"Dao";
		String varDaoName = varSourceName + "Dao";
		// 包名
		sb.append("package ");
		sb.append(packageName);
		if (isSameDir()) {
			sb.append(";");
		} else {
			sb.append(".service;");
		}

		sb.append("\n\n");

		// 导入import
//		sb.append("import java.util.HashMap;\n");
		sb.append("import java.util.List;\n");
		sb.append("import java.util.Map;\n");
		sb.append("\n");
		// sb.append(this.generateImports());

		sb.append("import ");
		sb.append(packageName);

		if (isSameDir()) {
			sb.append(".");
		} else {
			sb.append(".vo.");
		}
		sb.append(voName).append(";\n");

//		Comment comment = javaClass.getAnnotation(Comment.class);
//		sb.append(getCommentContent(comment, "", "Service of "));
		PsiAnnotation psiAnnotation = psiClass.getAnnotation("www.autogeneratecode.model.Comment");
		sb.append(getAnnotate(psiAnnotation,"","Service of "));

		sb.append("\n");
		sb.append("public interface ").append(psiClass.getName()).append("Service").append(" {");
		sb.append("\n");
		sb.append(getAddMethod(voName, varVoName, varDaoName, false));
		sb.append("\n");
		sb.append(getUpdateMethod(voName, varVoName, varDaoName, false));
		sb.append("\n");
		sb.append(getDeleteById(voName, varVoName, varDaoName, false));
		sb.append("\n");
		sb.append(getQueryById(voName, varVoName, varDaoName, false));
		sb.append("\n");
		sb.append(getQueryListById(voName, varVoName, varDaoName, false));
		sb.append("\n");
		sb.append(getQueryListMethod(voName, varVoName, varDaoName, false));
		sb.append("\n");
		sb.append(getQueryListCountMethod(voName, varVoName, varDaoName, false));
		sb.append("\n");

		// file last '}'
		sb.append("\n");
		sb.append("}");

		return sb.toString();
	}

	private String getImplSource() {

		StringBuilder sb = new StringBuilder();
		String sourceName = psiClass.getName();
		String varSourceName = sourceName.substring(0, 1).toLowerCase() + sourceName.substring(1);
		String voName = sourceName + "VO";
		String varVoName = varSourceName + "VO";
		String daoName = sourceName + "Dao";
		String varDaoName = varSourceName + "Dao";

		// 包名
		sb.append("package ");
//		sb.append(packageName).append(".service.impl;");
		sb.append(packageName);
		if (isSameDir()) {
			sb.append(";");
		} else {
			sb.append(".service.impl;");
		}
		sb.append("\n\n");

		// 导入import

//		sb.append("import java.util.HashMap;\n");
		sb.append("import java.util.List;\n");
		sb.append("import java.util.Map;\n");

		sb.append("\n");
		sb.append("import javax.annotation.Resource;\n\n");
		sb.append("import org.springframework.stereotype.Service;\n\n");

		if (isAddTransactional()) {
			sb.append("import org.springframework.transaction.annotation.Propagation;\n");
			sb.append("import org.springframework.transaction.annotation.Transactional;\n");
			sb.append("import org.springframework.util.Assert;\n");
			sb.append("\n");
		}

		// sb.append(this.generateImports());
		sb.append("import ");
//		sb.append(packageName).append(".vo.").append(voName).append(";\n");
		sb.append(packageName);
		if (isSameDir()) {
			sb.append(".");
		} else {
			sb.append(".vo.");
		}
		sb.append(voName).append(";\n");

		sb.append("import ");
//		sb.append(packageName).append(".dao.").append(sourceName).append("Dao").append(";\n");
		sb.append(packageName);
		if (isSameDir()) {
			sb.append(".");
		} else {
			sb.append(".dao.");
		}
		sb.append(sourceName).append("Dao").append(";\n");
		sb.append("import ");
//		sb.append(packageName).append(".service.").append(sourceName).append("Service").append(";\n");
		sb.append(packageName);
		if (isSameDir()) {
			sb.append(".");
		} else {
			sb.append(".service.");
		}
		sb.append(sourceName).append("Service").append(";\n");

//		Comment comment = javaClass.getAnnotation(Comment.class);
//		sb.append(getCommentContent(comment, "", "ServiceImpl of "));
		PsiAnnotation psiAnnotation = psiClass.getAnnotation("www.autogeneratecode.model.Comment");
		sb.append(getAnnotate(psiAnnotation,"","ServiceImpl of "));

		sb.append("\n");
		sb.append("@Service(\"");
		sb.append(varSourceName).append("\")");
		sb.append("\n");

		sb.append("public class ").append(psiClass.getName()).append("ServiceImpl ");
		sb.append("implements ").append(sourceName).append("Service");
		sb.append("{");
		sb.append("\n");

		sb.append("\n");
		sb.append("\t@Resource\n");
		sb.append("\tprivate ");
		sb.append(daoName).append(" ").append(varDaoName).append(";");

		sb.append("\n");

		sb.append(getAddMethod(voName, varVoName, varDaoName, true));
		sb.append("\n");
		sb.append(getUpdateMethod(voName, varVoName, varDaoName, true));
		sb.append("\n");
		sb.append(getDeleteById(voName, varVoName, varDaoName, true));
		sb.append("\n");
		sb.append(getQueryById(voName, varVoName, varDaoName, true));
		sb.append("\n");
		sb.append(getQueryListById(voName, varVoName, varDaoName, true));
		sb.append("\n");
		sb.append(getQueryListMethod(voName, varVoName, varDaoName, true));
		sb.append("\n");
		sb.append(getQueryListCountMethod(voName, varVoName, varDaoName, true));
		sb.append("\n");

		sb.append(getValidate(voName, varVoName));
		sb.append("\n");	
		
		sb.append(getValidateFieldNotZero());
		sb.append("\n");
		sb.append(getValidateFieldNotNull());
		sb.append("\n");
		sb.append(getValidateFieldNotEmpty());
		sb.append("\n");
		sb.append(getvalidateFieldStringLength());
		sb.append("\n");
		
		
		// file last '}'
		sb.append("\n");
		sb.append("}");

		return sb.toString();
	}

	private String getAddMethod(String voName, String varName, String varDaoName, boolean isImpl) {

		StringBuilder sb = new StringBuilder();
		if (isImpl) {
			sb.append("\n\t");
			sb.append("@Override");
		}
		sb.append("\n\t");

		if (isImpl && isAddTransactional()) {
			sb.append("@Transactional(readOnly=false, propagation=Propagation.REQUIRED)");
			sb.append("\n\t");
		}

		sb.append("public void add(").append(voName);
		sb.append(" ");
		sb.append(varName);
		sb.append(") throws Exception");
		if (isImpl) {
			sb.append("{");
			sb.append("\n\t\t");
			sb.append("Assert.notNull(");
			sb.append(varName);
			sb.append(");");
			sb.append("\n");
			sb.append("\n\t\t");
			sb.append("validate(");
			sb.append(varName);
			sb.append(");");

			sb.append("\n");
			sb.append("\n\t\t");
			sb.append(varDaoName);
			sb.append(".add(");
			sb.append(varName);
			sb.append(");");
			sb.append("\n\t}");
		} else {

			sb.append(";");
			return sb.toString();
		}

		return sb.toString();
	}

	private String getUpdateMethod(String voName, String varName, String varDaoName, boolean isImpl) {

		StringBuilder sb = new StringBuilder();
		if (isImpl) {
			sb.append("\n\t");
			sb.append("@Override");
		}
		sb.append("\n\t");
		if (isImpl && isAddTransactional()) {
			sb.append("@Transactional(readOnly=false, propagation=Propagation.REQUIRED)");
			sb.append("\n\t");
		}
		sb.append("public void update(").append(voName);
		sb.append(" ");
		sb.append(varName);
		sb.append(") throws Exception");
		if (isImpl) {
			sb.append("{");
			sb.append("\n\t\t");
			sb.append("Assert.notNull(");
			sb.append(varName);
			sb.append(");");
			sb.append("\n");

			sb.append("\n\t\t");
			sb.append("//if(");
			sb.append(varName);
			sb.append(".getId() == null || ");
			sb.append(varName);
			sb.append(".getId() == \"\") {");
			sb.append("\n\t\t");

			sb.append("//\t//TODO Set ");
			sb.append(varName);
			sb.append(" id");

			sb.append("\n\t\t");
			sb.append("//}");

			sb.append("\n");
			sb.append("\n\t\t");
			sb.append("validate(");
			sb.append(varName);
			sb.append(");");			
			
			sb.append("\n");
			sb.append("\n\t\t");
			sb.append(varDaoName);
			sb.append(".update(");
			sb.append(varName);
			sb.append(");");
			sb.append("\n\t}");
		} else {

			sb.append(";");
			return sb.toString();
		}

		return sb.toString();

	}

	private String getDeleteById(String voName, String varName, String varDaoName, boolean isImpl) {

		StringBuilder sb = new StringBuilder();
		if (isImpl) {
			sb.append("\n\t");
			sb.append("@Override");
		}
		sb.append("\n\t");
		if (isImpl && isAddTransactional()) {
			sb.append("@Transactional(readOnly=false, propagation=Propagation.REQUIRED)");
			sb.append("\n\t");
		}
		sb.append("public void deleteById(");
//		sb.append(voName);
//		sb.append(" ");
//		sb.append(varName);
		sb.append("String id");
		sb.append(") throws Exception");
		if (isImpl) {
			sb.append("{");
			sb.append("\n\t\t");
			sb.append(varDaoName);
			sb.append(".deleteById(");
//			sb.append(varName);
			sb.append("id");
			sb.append(");");
			sb.append("\n\t}");
		} else {

			sb.append(";");
			return sb.toString();
		}

		return sb.toString();

	}

	private String getQueryById(String voName, String varName, String varDaoName, boolean isImpl) {
		StringBuilder sb = new StringBuilder();
		if (isImpl) {
			sb.append("\n\t");
			sb.append("@Override");
		}
		sb.append("\n\t");
		sb.append("public List<").append(voName);
		sb.append("> queryById(");
		sb.append("String id");
		sb.append(") throws Exception");
		if (isImpl) {
			sb.append("{");
			sb.append("\n\t\t");
			sb.append("return ");
			sb.append(varDaoName);
			sb.append(".queryById(");
			sb.append("id");
			sb.append(");");
			sb.append("\n\t}");
		} else {

			sb.append(";");
			return sb.toString();
		}
		return sb.toString();
	}

	private String getQueryListById(String voName, String varName, String varDaoName, boolean isImpl) {
		StringBuilder sb = new StringBuilder();
		if (isImpl) {
			sb.append("\n\t");
			sb.append("@Override");
		}
		sb.append("\n\t");
		sb.append("public List<").append(voName);
		sb.append("> queryListById(");
		sb.append("List<String> idSet");
		sb.append(") throws Exception");
		if (isImpl) {
			sb.append("{");
			sb.append("\n\t\t");
			sb.append("return ");
			sb.append(varDaoName);
			sb.append(".queryListById(");
			sb.append("idSet");
			sb.append(");");
			sb.append("\n\t}");
		} else {

			sb.append(";");
			return sb.toString();
		}
		return sb.toString();
	}

	private String getQueryListMethod(String voName, String varName, String varDaoName, boolean isImpl) {

		StringBuilder sb = new StringBuilder();
		if (isImpl) {
			sb.append("\n\t");
			sb.append("@Override");
		}
		sb.append("\n\t");
		sb.append("public List<").append(voName);
		sb.append("> queryList(");
//		sb.append(voName);
//		sb.append(" ");
//		sb.append(varName);

		sb.append("Map<String, Object> parameterObject");
		sb.append(") throws Exception");
		if (isImpl) {
			sb.append("{");
			sb.append("\n\t\t");
			sb.append("return ");
			sb.append(varDaoName);
			sb.append(".queryList(");
//			sb.append(varName);
			sb.append("parameterObject");
			sb.append(");");
			sb.append("\n\t}");
		} else {

			sb.append(";");
			return sb.toString();
		}

		return sb.toString();

	}

	private String getQueryListCountMethod(String voName, String varName, String varDaoName, boolean isImpl) {

		StringBuilder sb = new StringBuilder();
		if (isImpl) {
			sb.append("\n\t");
			sb.append("@Override");
		}
		sb.append("\n\t");
		sb.append("public int queryListCount(");
//		sb.append(voName);
//		sb.append(" ");
//		sb.append(varName);
		sb.append("Map<String, Object> parameterObject");
		sb.append(") throws Exception");
		if (isImpl) {
			sb.append("{");
			sb.append("\n\t\t");
			sb.append("return ");
			sb.append(varDaoName);
			sb.append(".queryListCount(");
//			sb.append(varName);
			sb.append("parameterObject");
			sb.append(");");
			sb.append("\n\t}");
		} else {

			sb.append(";");
			return sb.toString();
		}

		return sb.toString();

	}

	private String getValidate(String voName, String varName) {
		StringBuilder sb = new StringBuilder();

		sb.append("\n\t/**");
		sb.append("\n\t* 校验");
		sb.append("\n\t*");
		sb.append("\n\t* @param ");
		sb.append(varName);
		sb.append("\n\t*/");
		
		sb.append("\n\t");
		sb.append("public void validate(ExampleFileVO exampleFileVO) throws Exception {");
		sb.append("\n\t\t");
		sb.append("// 非空校验");
		sb.append(getValidateNotnull(voName,varName));
		sb.append("\n\t\t");
		sb.append("// 长度校验");
		sb.append(getValidateLength(voName,varName));
		
		sb.append("\n\t");
		sb.append("}");
		sb.append("\n");
		
		return sb.toString();

	}
	
	private String getValidateNotnull(String voName, String varName) {
		StringBuilder sb = new StringBuilder();
//		Field[] fields = javaClass.getFields();
		PsiField[] fields = psiClass.getAllFields();
		PsiAnnotation psiAnnotation = null;
		String fieldType = "";
		String soruceFieldName = "";
		String fieldName = "";
		//Column column = null;
		for (PsiField field : fields) {
			fieldType = getPsiFieldTypeName(field).toLowerCase();
			soruceFieldName = field.getName();
			fieldName = soruceFieldName;
			if (fieldType.equalsIgnoreCase("boolean") && fieldName.startsWith("is")) {
				fieldName = fieldName.substring(2);
			}
			fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			//column = field.getAnnotation(Column.class);
//			if(column== null || column.nullable()) {
//				continue;
//			}
			psiAnnotation = field.getAnnotation("www.autogeneratecode.model.Column");
			if(psiAnnotation == null) {
				continue;
			}
			String nullabletext = getAnnotateText(psiAnnotation, "nullable", "");
			if(nullabletext== null || nullabletext.trim().length() ==0) {
				continue;
			}

			if(fieldType.equals("boolean")) {
				sb.append("\n\t\t");
				sb.append("validateFieldNotZero(\"");
				sb.append(soruceFieldName);
				sb.append("\",");
				sb.append(varName);
				sb.append(".is");
				sb.append(fieldName);
				sb.append("());");
				
			}else if(fieldType.equals("int") || fieldType.equals("short") || fieldType.equals("long")
					|| fieldType.equals("float") || fieldType.equals("double") || fieldType.equals("byte")) {
				sb.append("\n\t\t");
				sb.append("validateFieldNotZero(\"");
				sb.append(soruceFieldName);
				sb.append("\",");
				sb.append(varName);
				sb.append(".get");
				sb.append(fieldName);
				sb.append("());");				
				
			}else if(fieldType.equals("String")) {
				sb.append("\n\t\t");
				sb.append("validateFieldNotEmpty(\"");
				sb.append(soruceFieldName);
				sb.append("\",");
				sb.append(varName);
				sb.append(".get");
				sb.append(fieldName);
				sb.append("());");					
			}else {
				sb.append("\n\t\t");
				sb.append("validateFieldNotNull(\"");
				sb.append(soruceFieldName);
				sb.append("\",");
				sb.append(varName);
				sb.append(".get");
				sb.append(fieldName);
				sb.append("());");	
			}
		}
		return sb.toString();
	}
	private String getValidateLength(String voName, String varName) {
		StringBuilder sb = new StringBuilder();
		//Field[] fields = javaClass.getFields();
		PsiField[] fields = psiClass.getAllFields();
		PsiAnnotation psiAnnotation = null;
		String fieldType = "";
		String soruceFieldName = "";
		String fieldName = "";
//		Column column = null;
		for (PsiField field : fields) {
			fieldType = getPsiFieldTypeName(field);
			
			if(!fieldType.equalsIgnoreCase("String")) {
				continue;
			}
//			column = field.getAnnotation(Column.class);
//			if(column.length() == 0) {
//				continue;
//			}
			psiAnnotation = field.getAnnotation("www.autogeneratecode.model.Column");
			if(psiAnnotation == null) {
				continue;
			}
			String nullablelength = getAnnotateText(psiAnnotation, "length", "");
			if(nullablelength== null || nullablelength.trim().length() ==0) {
				continue;
			}

			soruceFieldName = field.getName();
			fieldName = soruceFieldName;
			fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

			
			sb.append("\n\t\t");
			sb.append("validateFieldStringLength(\"");
			sb.append(soruceFieldName);
			sb.append("\",");
			sb.append(varName);
			sb.append(".get");
			sb.append(fieldName);
			sb.append("(),");	
			sb.append(nullablelength);
			sb.append(");");	

		}
		return sb.toString();
	}
		
	
	private String getValidateFieldNotZero() {
		StringBuilder sb = new StringBuilder();

		sb.append("\n\t");
		sb.append("public void validateFieldNotZero(String field, Object value) throws Exception {");
		sb.append("\n\t\t");
		sb.append("boolean isError = false;");
		
		sb.append("\n\t\t");
		sb.append("if(value instanceof Long) {");
		sb.append("\n\t\t\t");
		sb.append("Long l = (Long)value;");
		sb.append("\n\t\t\t");
		sb.append("if(l.longValue() ==0 ) {");
		sb.append("\n\t\t\t\t");
		sb.append("isError = true;");
		sb.append("\n\t\t\t");
		sb.append("}");
		sb.append("\n\t\t");
		
		sb.append("\n\t\t");
		sb.append("}else if(value instanceof Float) {");
		sb.append("\n\t\t\t");
		sb.append("Float l = (Float)value;");
		sb.append("\n\t\t\t");
		sb.append("if(l.floatValue() ==0 ) {");
		sb.append("\n\t\t\t\t");
		sb.append("isError = true;");
		sb.append("\n\t\t\t");
		sb.append("}");
		sb.append("\n\t\t");
		
		sb.append("\n\t\t");
		sb.append("}else if(value instanceof Integer) {");
		sb.append("\n\t\t\t");
		sb.append("Integer l = (Integer)value;");
		sb.append("\n\t\t\t");
		sb.append("if(l.intValue() ==0 ) {");
		sb.append("\n\t\t\t\t");
		sb.append("isError = true;");
		sb.append("\n\t\t\t");
		sb.append("}");
		sb.append("\n\t\t");
		
		sb.append("\n\t\t");
		sb.append("}else if(value instanceof Short) {");
		sb.append("\n\t\t\t");
		sb.append("Short l = (Short)value;");
		sb.append("\n\t\t\t");
		sb.append("if(l.shortValue() ==0 ) {");
		sb.append("\n\t\t\t\t");
		sb.append("isError = true;");
		sb.append("\n\t\t\t");
		sb.append("}");
		sb.append("\n\t\t");
		
		sb.append("\n\t\t");
		sb.append("}else if(value instanceof Double) {");
		sb.append("\n\t\t\t");
		sb.append("Double l = (Double)value;");
		sb.append("\n\t\t\t");
		sb.append("if(l.doubleValue() ==0 ) {");
		sb.append("\n\t\t\t\t");
		sb.append("isError = true;");
		sb.append("\n\t\t\t");
		sb.append("}");
		sb.append("\n\t\t");
		
		sb.append("\n\t\t");
		sb.append("}else if(value instanceof Byte) {");
		sb.append("\n\t\t\t");
		sb.append("Byte l = (Byte)value;");
		sb.append("\n\t\t\t");
		sb.append("if(l.byteValue() ==0 ) {");
		sb.append("\n\t\t\t\t");
		sb.append("isError = true;");
		sb.append("\n\t\t\t");
		sb.append("}");
		sb.append("\n\t\t");
		sb.append("}");
		
		
		sb.append("\n\t\t");
		sb.append("if(isError) {");
		sb.append("\n\t\t\t");
		sb.append("throw new RuntimeException(\"validate error: field '\"+field+\"' is zero\");");
		sb.append("\n\t\t");
		sb.append("}");
		sb.append("\n\t");
		sb.append("}");
		sb.append("\n");
		return sb.toString();

	}
	
	private String getValidateFieldNotNull() {
		StringBuilder sb = new StringBuilder();

		sb.append("\n\t");
		sb.append("public void validateFieldNotNull(String field, Object value) throws Exception {");
		sb.append("\n\t\t");
		sb.append("if(value == null) {");
		sb.append("\n\t\t\t");
		sb.append("throw new RuntimeException(\"validate error: field '\"+field+\"' is null\");");
		sb.append("\n\t\t");
		sb.append("}");
		sb.append("\n\t");
		sb.append("}");
		sb.append("\n");
		return sb.toString();

	}	

	private String getValidateFieldNotEmpty() {
		StringBuilder sb = new StringBuilder();

		sb.append("\n\t");
		sb.append("public void validateFieldNotEmpty(String field, String value) throws Exception {");
		sb.append("\n\t\t");
		sb.append("if(value == null || value.trim().length() ==0 ) {");
		sb.append("\n\t\t\t");
		sb.append("throw new RuntimeException(\"validate error: field '\"+field+\"' is Empty\");");
		sb.append("\n\t\t");
		sb.append("}");
		sb.append("\n\t");
		sb.append("}");
		sb.append("\n");
		return sb.toString();

	}
	private String getvalidateFieldStringLength() {
		StringBuilder sb = new StringBuilder();

		sb.append("\n\t");
		sb.append("public void validateFieldStringLength(String field, String value,int length) throws Exception {");
		sb.append("\n\t\t");
		sb.append("if(value.length() > length ) {");
		sb.append("\n\t\t\t");
		sb.append("throw new RuntimeException(\"validate error: field '\"+field+\"' length is big than\"+ length);");
		sb.append("\n\t\t");
		sb.append("}");
		sb.append("\n\t");
		sb.append("}");
		sb.append("\n");
		return sb.toString();

	}	
}