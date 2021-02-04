package www.autogeneratecode.generator;


import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiJavaFileImpl;
import org.apache.commons.lang.StringUtils;
import www.autogeneratecode.codegen.CodeGenException;
import www.autogeneratecode.utils.IOUtils;

import java.io.File;
import java.io.IOException;

public class DDLGenerator {
    protected PsiClass psiClass = null;
    protected PsiJavaFileImpl psiJavaFileImpl = null;
    protected boolean hasPk = false;
    protected String pkColName = "";

    public DDLGenerator(PsiJavaFileImpl psiJavaFileImpl) {
        this.psiJavaFileImpl = psiJavaFileImpl;
    }

    public final void generate() {
        try {

            this.parsingClass();
        } catch (CodeGenException cge) {
            throw cge;
        } catch (Exception e) {
            throw new CodeGenException(e.getMessage(), e);
        }
    }

    /**
     * get all imports
     */
    protected void parsingClass() {
        this.psiJavaFileImpl = psiJavaFileImpl;
        this.psiClass = psiJavaFileImpl.getClasses()[0];
    }

    public File write(File dir) throws IOException {

        dir.mkdirs();

        File file = new File(dir,  "Create-"+ psiClass.getName() + ".sql");
        file = IOUtils.write(file, getDDL());
        System.out.println("Generate DDL file path:" + file.getPath());
        return file;
    }

    public String getDDL() throws CodeGenException {

        StringBuilder sb = new StringBuilder();
        String tableName = getTableName();
        sb.append("CREATE TABLE ");
        sb.append(tableName);
        sb.append("\n");
        sb.append("(");


        sb.append("\n");
        sb.append(generateFields());

        //file last );
        sb.append("\n");
        sb.append(");");

        //如果有ID字段，增加PK
        if (hasPk) {

            sb.append("\n");
            sb.append("ADD CONSTRAINT PK_");
            sb.append(tableName);
            sb.append(" PRIMARY KEY (");
            sb.append(pkColName);
            sb.append(");");
            sb.append("\n");

        }
        return sb.toString();
    }

    private String getTableName() {
        PsiAnnotation psiAnnotation = psiClass.getAnnotation("www.autogeneratecode.model.Table");
        String tableName = getAnnotateText(psiAnnotation, "name");

        return tableName;
    }

    private String generateFields() throws CodeGenException {

        StringBuilder sb = new StringBuilder();
        PsiField[] psiAllFields = psiClass.getAllFields();
        PsiAnnotation psiAnnotation = null;
        PsiAnnotation psiComment = null;
        String content = "";

        String fieldName = "";
        String colName = "";
        String dataType = "";
        int length = 0;
        int precision = 0;
        int scale = 0;
        boolean nullable = false;

        for (PsiField psiField : psiAllFields) {
            fieldName = psiField.getName();
            psiComment = psiField.getAnnotation("www.autogeneratecode.model.Comment");
            content = getAnnotateText(psiComment, "content");

            psiAnnotation = psiField.getAnnotation("www.autogeneratecode.model.Column");
            colName = getAnnotateText(psiAnnotation, "name");
            dataType = getAnnotateText(psiAnnotation, "dataType");
            length = getAnnotateInt(psiAnnotation, "length");
            precision = getAnnotateInt(psiAnnotation, "precision");
            scale = getAnnotateInt(psiAnnotation, "scale");

            if (StringUtils.isBlank(colName)) {
                throw new CodeGenException("属性[" + fieldName + "]的数据库表字段名称(name)不能为空");
            }
            if (StringUtils.isBlank(dataType)) {
                throw new CodeGenException("属性[" + fieldName + "]的数据库表字段类型(dataType)不能为空");
            }
            nullable = true;
            nullable = getAnnotateBoolean(psiAnnotation, "nullable", fieldName);

            if ("id".equalsIgnoreCase(fieldName)) {
                this.hasPk = true;
                this.pkColName = colName;
            }


            if (sb.length() > 0) {
                sb.append(",\n");
            }

            sb.append("\t");
            sb.append(colName).append(" ").append(dataType);
            if ("varchar".equalsIgnoreCase(dataType)) {
                if (length > 0) {
                    sb.append("(").append(length).append(") ");
                }
                if (!nullable) {
                    sb.append(" NOT NULL");
                }
            }  else if("int".equalsIgnoreCase(dataType) || "decimal".equalsIgnoreCase(dataType)){
                sb.append(" default 0");
            } else {
                if (!nullable) {
                    sb.append(" NOT NULL");
                }
            }
            if (content != null) {
                sb.append(" COMMENT '" + content +"'");

            }
        }

        return sb.toString();

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

    protected int getAnnotateInt(PsiAnnotation psiAnnotation, String text) {
        if (psiAnnotation == null) {
            return -1;
        }
        if (psiAnnotation.findAttributeValue(text) != null) {
            String s = psiAnnotation.findAttributeValue(text).getContext().getLastChild().getText();
            s = s.replaceAll("\"", "");
            if (s != null && s.trim().length() > 0) {
                return Integer.valueOf(s);
            }
        }
        return -1;
    }

    protected Boolean getAnnotateBoolean(PsiAnnotation psiAnnotation, String text, String fieldName) {
        //id字段不能为空
        if ("id".equalsIgnoreCase(fieldName)) {
            return false;
        }
        if (psiAnnotation == null) {
            return true;
        }
        if (psiAnnotation.findAttributeValue(text) != null) {
            String s = psiAnnotation.findAttributeValue(text).getContext().getLastChild().getText();
            s = s.replaceAll("\"", "");
            return Boolean.valueOf(s);
        }
        return true;
    }

    public PsiClass getPsiClass() {
        return psiClass;
    }

    public void setPsiClass(PsiClass psiClass) {
        this.psiClass = psiClass;
    }

    public PsiJavaFileImpl getPsiJavaFileImpl() {
        return psiJavaFileImpl;
    }

    public void setPsiJavaFileImpl(PsiJavaFileImpl psiJavaFileImpl) {
        this.psiJavaFileImpl = psiJavaFileImpl;
    }
}
