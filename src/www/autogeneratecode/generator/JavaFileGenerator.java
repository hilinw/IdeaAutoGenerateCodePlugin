package www.autogeneratecode.generator;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import www.autogeneratecode.codegen.CodeGenException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public abstract class JavaFileGenerator {

    protected String packageName = "";

    protected List<String> imports = new ArrayList<String>();

    protected PsiClass psiClass = null;

    public JavaFileGenerator(PsiClass psiClass1, String packagename) {
        psiClass = psiClass1;

        if (packagename != null && packagename.startsWith("metadata.")) {
            packageName = packagename.substring(9);
        } else {
            packageName = packagename;
        }
    }

    public final void generate() {
        try {
            this.beforeGenerate();
            this.generateJavaFile();
            this.afterGenerate();
        } catch (CodeGenException cge) {
            throw cge;
        } catch (Exception e) {
            throw new CodeGenException(e.getMessage(), e);
        }
    }

    public abstract File write(File file) throws IOException;

    protected abstract void generateJavaFile();

    protected void beforeGenerate() throws Exception {
    }

    protected void afterGenerate() throws Exception {
    }

    public PsiClass getPsiClass() {
        return psiClass;
    }

    public void setPsiClass(PsiClass psiClass) {
        this.psiClass = psiClass;
    }


    protected String getFields() {
        PsiField[] psiAllFields = psiClass.getAllFields();

        StringBuilder sb = new StringBuilder();

        for (PsiField psiField : psiAllFields) {
            PsiAnnotation psiAnnotation = psiField.getAnnotation("www.autogeneratecode.model.Comment");
            sb.append("\n\t");
            sb.append("private ");
            sb.append(getPsiFieldTypeName(psiField));
            sb.append(" ").append(psiField.getName());
            sb.append("; // ");
            sb.append(getAnnotateText(psiAnnotation,"content",""));
            sb.append("\n");

        }

        return sb.toString();

    }

    protected String getPsiFieldTypeName(PsiField psiField) {
        String  s = psiField.getType().getCanonicalText();
        if("java.lang.String".equals(s)){
            s = "String";
        }
        return s;
    }



    protected String getAnnotate(PsiAnnotation psiAnnotation,String tab,String addBeforeStr) {

        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(tab);
        sb.append("/**");
        sb.append("\n").append(tab);
        sb.append("*");
        sb.append(getAnnotateText(psiAnnotation,"content",addBeforeStr));
        sb.append("\n").append(tab);
        sb.append("*/");

        return sb.toString();

    }

    protected String getAnnotateText(PsiAnnotation psiAnnotation, String text,String addBeforeStr) {
        if (psiAnnotation.findAttributeValue(text) != null) {
            String s = psiAnnotation.findAttributeValue(text).getContext().getLastChild().getText();
            s = s.replaceAll("\"", "");
            return addBeforeStr+s;
        }
        return "";
    }


}
