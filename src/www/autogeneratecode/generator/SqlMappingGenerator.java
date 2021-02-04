package www.autogeneratecode.generator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.impl.source.PsiJavaFileImpl;
import www.autogeneratecode.model.Column;
import www.autogeneratecode.model.Table;
import www.autogeneratecode.utils.IOUtils;

public class SqlMappingGenerator {
	protected String packageName = "";
	protected PsiClass psiClass = null;
	protected PsiJavaFileImpl psiJavaFileImpl = null;
	private boolean isSameDir = false;

	protected boolean hasPk = false;
	protected String pkColName = "";

	public SqlMappingGenerator(PsiJavaFileImpl psiJavaFileImpl, String packagename) {
		this.psiJavaFileImpl = psiJavaFileImpl;
		this.psiClass = psiJavaFileImpl.getClasses()[0];
		if (packagename != null && packagename.startsWith("metadata.")) {
			packageName = packagename.substring(9);
		} else {
			packageName = packagename;
		}

	}

	public boolean isSameDir() {
		return isSameDir;
	}

	public void setSameDir(boolean isSameDir) {
		this.isSameDir = isSameDir;
	}

	public File write(File dir) throws IOException {

		dir.mkdirs();
		this.genPkColName();
		File file = new File(dir, "sqlmapping-" + psiClass.getName() + ".xml");
		file = IOUtils.write(file, getSource());
		System.out.println("Generate sqlmapping source path:" + file.getPath());
		return file;
	}

	public String getSource() {

		StringBuilder sb = new StringBuilder();
		String sourceName = psiClass.getName();
		String voName = "";
		String daoName = "";
		if(isSameDir()) {
			voName = packageName + "." + sourceName + "VO";
			daoName = packageName + "." + sourceName + "Dao";
		}else {
			voName = packageName + ".vo." + sourceName + "VO";
			daoName = packageName + ".dao." + sourceName + "Dao";
		}

//		Table table = javaClass.getAnnotation(Table.class);
//		if(table == null) {
//			throw new RuntimeException("Generate sqlmapping error, The source file no annotation of 'Table' ");
//		}
		PsiAnnotation psiAnnotation = psiClass.getAnnotation("www.autogeneratecode.model.Table");
		if(psiAnnotation == null) {
			throw new RuntimeException("Generate sqlmapping error, The source file no annotation of 'Table' ");
		}

//		String tableName = table.name();
		String tableName = getAnnotateText(psiAnnotation, "name", "");

		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
		sb.append("<!DOCTYPE mapper\n");
		sb.append("PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"\n");
		sb.append("\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n");
		sb.append("<mapper namespace=\"");
		sb.append(daoName);
		sb.append("\">\n");

		sb.append("\n");
		sb.append(getResultMap(tableName, voName, daoName));

		sb.append("\n\n");
		sb.append(getAddMethod(tableName, voName, daoName));
		sb.append("\n\n");
		sb.append(getUpdateMethod(tableName, voName, daoName));
		sb.append("\n\n");
		sb.append(getDeleteById(tableName, voName, daoName));
		sb.append("\n\n");
		sb.append(getQueryById(tableName, voName, daoName));
		sb.append("\n\n");
		sb.append(getQueryListByIds(tableName, voName, daoName));
		sb.append("\n\n");
		sb.append(getQueryListMethod(tableName, voName, daoName));
		sb.append("\n\n");
		sb.append(getQueryListCountMethod(tableName, voName, daoName));
		sb.append("\n");

		// file last 'mapper'
		sb.append("\n");
		sb.append("</mapper>");

		return sb.toString();
	}


	/**
	 *
	 <resultMap id="resultMap" type="com.uniprint.template.service.UserVO">
	 <id column="FcoId" property="coId" />
	 <result column="FUSERID" property="id" />
	 <result column="FNAME" property="name" />
	 <result column="fnumber" property="number" />
	 <result column="fmobile" property="mobile" />
	 <result column="faddress" property="address" />
	 <result column="femployeeId" property="employeeId" />
	 <result column="fisAdmin" property="isAdmin" />
	 <result column="fisSuperAdmin" property="isSuperAdmin" />
	 <result column="fsex" property="sex" />
	 <result column="fcreateTime" property="createTime" />
	 <result column="fmodifytime" property="modifytime" />
	 <result column="fisDelete" property="isDelete" />
	 <result column="fremark" property="remark" />
	 </resultMap>
	 *
	 * @param voName
	 * @param daoName
	 * @return
	 */
	private String getResultMap(String tableName, String voName, String daoName) {

		StringBuilder sb = new StringBuilder();
		sb.append("<resultMap id=\"resultMap\" type=\"");
		sb.append(voName);
		sb.append("\">");
		sb.append("\n\t");
		sb.append("<id column=\"");
		if(hasPk) {
			//主键名称不一定是fid
			sb.append(this.pkColName).append("\" property=\"id\" />");
		}else {
			sb.append("<id column=\"id\" property=\"id\" />");
		}

		PsiField[] fields = psiClass.getAllFields();
		PsiAnnotation psiAnnotation = null;

		String columnName = "";
		for (PsiField field : fields) {
			psiAnnotation = field.getAnnotation("www.autogeneratecode.model.Column");
			if(psiAnnotation == null) {
				continue;
			}

			columnName = getAnnotateText(psiAnnotation, "name", "");

			if (field.getName().equalsIgnoreCase("id")) {
				continue;
			}

			if(columnName == null || columnName.trim().length() ==0) {
				continue;
			}
			sb.append("\n\t");
//			<result column="FUSERID" property="id" />
			sb.append("<result column=\"").append(columnName);
			sb.append("\"");
			sb.append(" property=\"").append(field.getName());
			sb.append("\" />");
		}


		sb.append("\n");
		sb.append("</resultMap>");
		return sb.toString();
	}

	/**
	 * <insert id="add" parameterType="xxx.vo.XxxxxVo"> INSERT INTO tableName
	 * (column1,column2,...)VALUES(#{field1}#,#{field2},...) </insert>
	 * 
	 * @param voName
	 * @param daoName
	 * @return
	 */
	private String getAddMethod(String tableName, String voName, String daoName) {

		StringBuilder sb = new StringBuilder();
		sb.append("\t<insert id=\"add\" ");
		sb.append("parameterType=\"");
		sb.append(voName);
		sb.append("\">");
		sb.append("\n\t\t");
		sb.append("INSERT INTO ");
		sb.append(tableName);
		sb.append(" (");
		sb.append(getAddFields());
		sb.append(")");
		sb.append("\n\t\t");
		sb.append("VALUES (");
		sb.append(getValues());
		sb.append(")");

		sb.append("\n\t</insert>");
		return sb.toString();
	}

	/**
	 * <update id="update" parameterType="xxx.vo.XxxxxVo"> UPDATE tableName set
	 * column1 = #{field1}, column2 = #{field2},... WHERE fid = #{id}, </update>
	 * 
	 * @param tableName
	 * @param voName
	 * @param daoName
	 * @return
	 */
	private String getUpdateMethod(String tableName, String voName, String daoName) {

		StringBuilder sb = new StringBuilder();
		sb.append("\t<update id=\"update\" ");
		sb.append("parameterType=\"");
		sb.append(voName);
		sb.append("\">");
		sb.append("\n\t\t");
		sb.append("UPDATE ");
		sb.append(tableName);
		sb.append("\n\t\t");
		sb.append("SET ");
		sb.append(getUpdateFields());
		sb.append(")");
		sb.append("\n\t\t");
//		sb.append("WHERE Fid = #{id}");
		if(hasPk) {
			//主键名称不一定是fid
			sb.append("WHERE ").append(this.pkColName).append(" = #{id} ");
		}else {
			sb.append("WHERE Fid = #{id}");
		}

		sb.append("\n\t</update>");
		return sb.toString();

	}

	/**
	 * <delete id="deleteById" parameterType="xxx.vo.XxxxxVo"> DELETE FROM tableName
	 * WHERE fid = #{id}, </delete>
	 * 
	 * @param tableName
	 * @param voName
	 * @param daoName
	 * @return
	 */
	private String getDeleteById(String tableName, String voName, String daoName) {

		StringBuilder sb = new StringBuilder();
		sb.append("\t<delete id=\"deleteById\" ");
		sb.append("parameterType=\"");
		sb.append(voName);
		sb.append("\">");
		sb.append("\n\t\t");
		sb.append("DELETE FROM ");
		sb.append(tableName);
		sb.append("\n\t\t");
//		sb.append("WHERE Fid = #{id}");
		if(hasPk) {
			//主键名称不一定是fid
			sb.append("WHERE ").append(this.pkColName).append(" = #{id} ");
		}else {
			sb.append("WHERE Fid = #{id}");
		}

		sb.append("\n\t</delete>");
		return sb.toString();

	}
	
	
	/**
	 * <select id="queryListByIds" parameterType="java.lang.String"
	 * resultType="xxx.vo.XxxxxVo" SELECT column1,column2.... 
	 * FROM tableName LIMIT
	 * Where Fid = #{id}
	 * 
	 * @param tableName
	 * @param voName
	 * @param daoName
	 * @return
	 */
	private String getQueryById(String tableName, String voName, String daoName) {

		StringBuilder sb = new StringBuilder();
		sb.append("\t<select id=\"queryById\" ");
		sb.append("parameterType=\"java.lang.String\" ");
		// sb.append(voName);
		// sp.append("\" ");
		sb.append("resultType=\"");
		sb.append(voName);
		sb.append("\">");
		sb.append("\n\t\t");
		sb.append("SELECT ");
		sb.append(getAddFields());

		sb.append("\n\t\t");
		sb.append("FROM ");
		sb.append(tableName);
		sb.append("\n\t\t");
//		sb.append("WHERE Fid = #{id}");
		if(hasPk) {
			//主键名称不一定是fid
			sb.append("WHERE ").append(this.pkColName).append(" = #{id} ");
		}else {
			sb.append("WHERE Fid = #{id}");
		}

		//sb.append(getTestFields());

		sb.append("\n\t\t");
		//sb.append("LIMIT #{offset}, #{limit}");

		sb.append("\n\t</select>");
		return sb.toString();
	}
		
	
	/**
	 * <select id="queryListByIds" parameterType="java.util.List"
	 * resultType="xxx.vo.XxxxxVo" SELECT column1,column2.... 
	 * FROM tableName LIMIT
	 * Where id in idSet
	 * 
	 * @param tableName
	 * @param voName
	 * @param daoName
	 * @return
	 */
	private String getQueryListByIds(String tableName, String voName, String daoName) {

		StringBuilder sb = new StringBuilder();
		sb.append("\t<select id=\"queryListByIds\" ");
		sb.append("parameterType=\"java.util.List\" ");
		// sb.append(voName);
		// sp.append("\" ");
		sb.append("resultType=\"");
		sb.append(voName);
		sb.append("\">");
		sb.append("\n\t\t");
		sb.append("SELECT ");
		sb.append(getAddFields());

		sb.append("\n\t\t");
		sb.append("FROM ");
		sb.append(tableName);
		sb.append("\n\t\t");
//		sb.append("WHERE Fid in");
		if(hasPk) {
			//主键名称不一定是fid
			sb.append("WHERE ").append(this.pkColName).append(" in");
		}else {
			sb.append("WHERE Fid in");
		}
		sb.append("\n\t\t\t");
		sb.append("<foreach collection=\"idSet\" item=\"id\" index=\"index\" open=\"(\" close=\")\" separator=\",\">");
		sb.append("\n\t\t\t\t");
		sb.append("#{id}");
		sb.append("\n\t\t\t");
		
		sb.append("</foreach>");

		//sb.append(getTestFields());

		sb.append("\n\t\t");
		//sb.append("LIMIT #{offset}, #{limit}");

		sb.append("\n\t</select>");
		return sb.toString();
	}
	

	/**
	 * <select id="queryList" parameterType="java.util.Map"
	 * resultType="xxx.vo.XxxxxVo" SELECT column1,column2.... FROM tableName LIMIT
	 * #{offset}, #{limit}
	 * 
	 * @param tableName
	 * @param voName
	 * @param daoName
	 * @return
	 */
	private String getQueryListMethod(String tableName, String voName, String daoName) {

		StringBuilder sb = new StringBuilder();
		sb.append("\t<select id=\"queryList\" ");
		sb.append("parameterType=\"java.util.Map\" ");
		// sb.append(voName);
		// sp.append("\" ");
		sb.append("resultType=\"");
		sb.append(voName);
		sb.append("\">");
		sb.append("\n\t\t");
		sb.append("SELECT ");
		sb.append(getAddFields());

		sb.append("\n\t\t");
		sb.append("FROM ");
		sb.append(tableName);
		sb.append("\n\t\t");
		sb.append("WHERE 1 = 1");

		sb.append(getTestFields());

		sb.append("\n\t\t");
		sb.append("LIMIT #{offset}, #{limit}");

		sb.append("\n\t</select>");
		return sb.toString();
	}

	/**
	 * <select id="queryListCount" parameterType="xxx.vo.XxxxxVo" resultType="int" SELECT
	 * count(1) FROM tableName LIMIT #{offset}, #{limit}
	 * 
	 * @param tableName
	 * @param voName
	 * @param daoName
	 * @return
	 */
	private String getQueryListCountMethod(String tableName, String voName, String daoName) {

		StringBuilder sb = new StringBuilder();
		sb.append("\t<select id=\"queryListCount\" ");
		sb.append("parameterType=\"java.util.Map\" ");
		// sb.append(voName);
		// sp.append("\" ");
		sb.append("resultType=\"int\">");
//		sb.append(voName);
//		sb.append("\">");
		sb.append("\n\t\t");
		sb.append("SELECT count(1) ");

		sb.append("\n\t\t");
		sb.append("FROM ");
		sb.append(tableName);
		sb.append("\n\t\t");
		sb.append("WHERE 1 = 1");

		sb.append(getTestFields());

		sb.append("\n\t\t");

		sb.append("\n\t</select>");
		return sb.toString();
	}

	private String getTestFields() {

//		Field[] fields = javaClass.getFields();
		PsiField[] fields = psiClass.getAllFields();
		PsiAnnotation psiAnnotation = null;
		StringBuilder sb = new StringBuilder();
		//Column column = null;
		for (PsiField field : fields) {
			psiAnnotation = field.getAnnotation("www.autogeneratecode.model.Column");
			if(psiAnnotation == null) {
				continue;
			}
//			column = field.getAnnotation(Column.class);
//			if(column == null) {
//				continue;
//			}
			String columnName = getAnnotateText(psiAnnotation, "name", "");

			sb.append("\n\t\t\t");
			
			sb.append("<if test=\"");
			sb.append(field.getName());
			sb.append(" != null and ");
			sb.append(field.getName());
			sb.append(" != '' \">");
			sb.append("\n\t\t\t\t");			
			sb.append("and ");
//			sb.append(column.name());
			sb.append(columnName);

			sb.append(" = #{");
			sb.append(field.getName());
			sb.append("}");
			
			sb.append("\n\t\t\t");			
			sb.append("</if>");
			
		}
		return sb.toString();

	}

	private String getAddFields() {

//		Field[] fields = javaClass.getFields();
		PsiField[] fields = psiClass.getAllFields();
		PsiAnnotation psiAnnotation = null;
		StringBuilder sb = new StringBuilder();
//		Column column = null;
		int i = 0;
		for (PsiField field : fields) {
			psiAnnotation = field.getAnnotation("www.autogeneratecode.model.Column");
			if(psiAnnotation == null) {
				continue;
			}
//			column = field.getAnnotation(Column.class);
//			if(column == null) {
//				continue;
//			}
			String columnName = getAnnotateText(psiAnnotation, "name", "");
			if (i == 5) {
				sb.append("\n\t\t\t");
				i = 0;
			} else {
				i++;
			}
			if (sb.length() > 0) {
				sb.append(", ");
			}
//			sb.append(column.name());
			sb.append(columnName);

		}
		return sb.toString();

	}

	private String getValues() {

//		Field[] fields = javaClass.getFields();
		PsiField[] fields = psiClass.getAllFields();
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (PsiField field : fields) {

			if (i == 5) {
				sb.append("\n\t\t\t");
				i = 0;
			} else {
				i++;
			}
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append("#{");
			sb.append(field.getName());
			sb.append("}");

		}
		return sb.toString();

	}

	private String getUpdateFields() {
//		Field[] fields = javaClass.getFields();
		PsiField[] fields = psiClass.getAllFields();
		PsiAnnotation psiAnnotation = null;

		StringBuilder sb = new StringBuilder();
		int i = 0;
		//Column column = null;
		for (PsiField field : fields) {
			if (field.getName().equalsIgnoreCase("id")) {
				continue;
			}

//			column = field.getAnnotation(Column.class);
//			if(column == null) {
//				continue;
//			}
			psiAnnotation = field.getAnnotation("www.autogeneratecode.model.Column");
			if(psiAnnotation == null) {
				continue;
			}
			String columnName = getAnnotateText(psiAnnotation, "name", "");

			if (i == 5) {
				sb.append("\n\t\t\t");
				i = 0;
			} else {
				i++;
			}
			if (sb.length() > 0) {
				sb.append(", ");
			}

//			sb.append(column.name());
			sb.append(columnName);
			sb.append(" = ");
			sb.append("#{");
			sb.append(field.getName());
			sb.append("}");

		}
		return sb.toString();

	}
	protected String getAnnotateText(PsiAnnotation psiAnnotation, String text, String addBeforeStr) {
		if (psiAnnotation == null) {
			return "";
		}
		if (psiAnnotation.findAttributeValue(text) != null) {
			String s = psiAnnotation.findAttributeValue(text).getContext().getLastChild().getText();
			s = s.replaceAll("\"", "");
			return addBeforeStr + s;
		}
		return "";
	}

	private void genPkColName()  {

		PsiField[] psiAllFields = psiClass.getAllFields();
		PsiAnnotation psiAnnotation = null;

		String fieldName = "";
		String colName = "";

		for (PsiField psiField : psiAllFields) {
			fieldName = psiField.getName();
			psiAnnotation = psiField.getAnnotation("www.autogeneratecode.model.Column");
			colName = getAnnotateText(psiAnnotation, "name");

			if ("id".equalsIgnoreCase(fieldName)) {
				this.hasPk = true;
				this.pkColName = colName;
				break;
			}

		}
	}
	protected String getAnnotateText(PsiAnnotation psiAnnotation, String text) {
		if (psiAnnotation == null) {
			return "";
		}
		if (psiAnnotation.findAttributeValue(text) != null) {
			String s = psiAnnotation.findAttributeValue(text).getContext().getLastChild().getText();
			s = s.replaceAll("\"", "");
			return s;
		}
		return "";
	}
}
