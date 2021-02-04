package www.autogeneratecode.generator;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiJavaFileImpl;
import www.autogeneratecode.codegen.CodeGenException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public abstract class JavaFileGenerator {

    protected String packageName = "";
    private boolean isSameDir = false;
    private boolean addTransactional = true;
    private boolean isNoInterface = true;

    // 源文件的导入部分
    protected List<String> sourceFileImports = new ArrayList<String>();
    // 生成文件的导入部分
    protected List<String> imports = new ArrayList<String>();
    private String extClass;

    protected PsiClass psiClass = null;
    protected PsiJavaFileImpl psiJavaFileImpl = null;
    protected List<String> ignoreImports = new ArrayList();


    public JavaFileGenerator(PsiJavaFileImpl psiJavaFileImpl, String packagename) {
        this.psiJavaFileImpl = psiJavaFileImpl;
        this.psiClass = psiJavaFileImpl.getClasses()[0];

        if (packagename != null && packagename.startsWith("metadata.")) {
            packageName = packagename.substring(9);
        } else {
            packageName = packagename;
        }
        PsiClass superClass = this.psiClass.getSuperClass();
        if(superClass != null && !"Object".equalsIgnoreCase(superClass.getName())){
            this.extClass = superClass.getName();
            //System.out.println("----------- superClass:"+this.extClass );
        }

        ignoreImports.add("www.autogeneratecode.model.Column");
        ignoreImports.add("www.autogeneratecode.model.Comment");
        ignoreImports.add("www.autogeneratecode.model.Entity");
        ignoreImports.add("www.autogeneratecode.model.Table");
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

        parsingImports();

    }

    /**
     * get all imports
     */
    protected void parsingImports() {
        if (psiClass != null) {
            PsiImportList psiImportList = psiJavaFileImpl.getImportList();
            String importString = "";
            for (PsiImportStatementBase psiImportStatementBase : psiImportList.getAllImportStatements()) {
                importString = psiImportStatementBase.getImportReference().getQualifiedName();
                if(importString.startsWith("metadata.")){
                    importString = importString.substring(9);
                }
                if (!ignoreImports.contains(importString)) {
                    imports.add(importString);
                }
            }
//            for (String importString : sourceFileImports) {
//                if (!ignoreImports.contains(importString)) {
//                    imports.add(importString);
//                }
//            }
        }

    }


    protected void afterGenerate() throws Exception {
    }

    public PsiClass getPsiClass() {
        return psiClass;
    }

    public void setPsiClass(PsiClass psiClass) {
        this.psiClass = psiClass;
    }

    public boolean isSameDir() {
        return isSameDir;
    }

    public void setSameDir(boolean sameDir) {
        isSameDir = sameDir;
    }

    public boolean isAddTransactional() {
        return addTransactional;
    }

    public void setAddTransactional(boolean addTransactional) {
        this.addTransactional = addTransactional;
    }

    public List<String> getSourceFileImports() {
        return sourceFileImports;
    }

    public void setSourceFileImports(List<String> sourceFileImports) {
        this.sourceFileImports = sourceFileImports;
    }

    public List<String> getImports() {
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }

    public String getExtClass() {
        return extClass;
    }

    public void setExtClass(String extClass) {
        this.extClass = extClass;
    }

    public boolean isNoInterface() {
        return isNoInterface;
    }

    public void setNoInterface(boolean noInterface) {
        isNoInterface = noInterface;
    }

    protected String generateImports() {
        StringBuilder sb = new StringBuilder();

        for (String importString : imports) {
            sb.append("import ").append(importString).append(";");
            sb.append("\n");
        }

        return sb.toString();
    }

    protected String getFields() {
        PsiField[] psiAllFields = psiClass.getAllFields();
        PsiClass superPsiClass = psiClass.getSuperClass();
        PsiField[] superfields = superPsiClass.getAllFields();

        StringBuilder sb = new StringBuilder();

        for (PsiField psiField : psiAllFields) {
            if (isSupperField(psiField, superfields)) {
                continue;
            }

            PsiAnnotation psiAnnotation = psiField.getAnnotation("www.autogeneratecode.model.Comment");
            sb.append("\n\t");
            sb.append("private ");
            sb.append(getPsiFieldTypeName(psiField));
            sb.append(" ").append(psiField.getName());
            sb.append("; // ");
            sb.append(getAnnotateText(psiAnnotation, "content", ""));
            sb.append("\n");

        }

        return sb.toString();

    }
    protected boolean isSupperField(PsiField field, PsiField[] fields) {
        boolean isSupperField = false;
        for (PsiField field2 : fields) {
            if (field2.getName().equals(field.getName())) {
                return true;
            }
        }
        return isSupperField;
    }

    protected String getPsiFieldTypeName(PsiField psiField) {
        String s = psiField.getType().getCanonicalText();
        if ("java.lang.String".equals(s)) {
            s = "String";
        }
        if (imports.contains(s)) {
            int p = s.lastIndexOf(".");
            if (p > 0) {
                s = s.substring(p + 1);
            }
        }

        return s;
    }


    protected String getAnnotate(PsiAnnotation psiAnnotation, String tab, String addBeforeStr) {

        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(tab);
        sb.append("/**");
        sb.append("\n").append(tab);
        sb.append("*");
        sb.append(getAnnotateText(psiAnnotation, "content", addBeforeStr));
        sb.append("\n").append(tab);
        sb.append("*/");

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


}
