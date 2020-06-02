package www.autogeneratecode.generator;


import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiField;
import com.intellij.psi.impl.source.PsiJavaFileImpl;
import www.autogeneratecode.utils.IOUtils;

import java.io.File;
import java.io.IOException;

public class JavaEntityGenerator  extends JavaFileGenerator  {

    private boolean generateGetterAndSetter = true;

    public JavaEntityGenerator(PsiJavaFileImpl psiJavaFileImpl, String packageName){
       super(psiJavaFileImpl,packageName);

    }

    @Override
    protected void generateJavaFile() {
//        PsiReferenceList psiReferenceList = psiClass.getImplementsList();
//        PsiField[] psiFields = psiClass.getFields();
//        PsiField[] psiAllFields = psiClass.getAllFields();


    }

    public File write(File dir) throws IOException {

        dir.mkdirs();

        File file = new File(dir,psiClass.getName()+".java");
        file = IOUtils.write(file,getSource());
        System.out.println("Generate JavaFile source path:"+file.getPath());
        return file;
    }

    public String getSource(){

        StringBuilder sb = new StringBuilder();
        //包名
        sb.append("package ");
        sb.append(packageName).append(";");
        sb.append("\n\n");

        //导入import
        sb.append("import java.io.Serializable;");
        sb.append("\n");
        sb.append(this.generateImports());


        PsiAnnotation psiAnnotation = psiClass.getAnnotation("www.autogeneratecode.model.Comment");
        sb.append(getAnnotate(psiAnnotation,"",""));

        sb.append("\n");
        sb.append("public class ").append(psiClass.getName()).append(" implements  Serializable {");
        sb.append("\n");
        sb.append("\n");
        sb.append("\t");
        sb.append("private static final long serialVersionUID = 1L;");

        sb.append("\n");
        sb.append(getFields());


        sb.append("\n");
        sb.append("\n\t");
        sb.append("public ").append(psiClass.getName()).append("() {");
        sb.append("\n\t");
        sb.append("}");

        if(isGenerateGetterAndSetter()){
            sb.append(generateGetterAndSetter());
        }

        //file last '}'
        sb.append("\n");
        sb.append("}");

        return sb.toString();
    }


    private String generateGetterAndSetter(){

        StringBuilder sb = new StringBuilder();
        PsiField[] psiAllFields = psiClass.getAllFields();
        PsiAnnotation psiAnnotation = null;
        String fieldType = "";
        String soruceFieldName = "";
        String fieldName = "";
        for (PsiField psiField : psiAllFields) {

            fieldType = getPsiFieldTypeName(psiField);
            soruceFieldName = psiField.getName();
            fieldName = soruceFieldName;
            if(fieldType.equalsIgnoreCase("boolean") && fieldName.startsWith("is") ){
                fieldName = fieldName.substring(2);
            }
            fieldName = fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);

            psiAnnotation = psiField.getAnnotation("www.autogeneratecode.model.Comment");

            //get
            sb.append("\n");
            sb.append(getAnnotate(psiAnnotation,"\t","取"));
            sb.append("\n\t");
            sb.append("public ");
            sb.append(fieldType);
            if(fieldType.equalsIgnoreCase("boolean")){
                sb.append(" is");
            }else{
                sb.append(" get");
            }
            sb.append(fieldName).append("() {");
            sb.append("\n\t\t");
            sb.append("return ").append(soruceFieldName).append(";");
            sb.append("\n\t}");

            //set
            sb.append("\n");
            sb.append(getAnnotate(psiAnnotation,"\t","设置"));
            sb.append("\n\t");
            sb.append("public void set");

            sb.append(fieldName).append("(");

            sb.append(fieldType).append(" ").append( soruceFieldName).append(" ) {");

            sb.append("\n\t\t");
            sb.append("this.").append(soruceFieldName).append(" = ").append(soruceFieldName).append(";");
            sb.append("\n\t}");


            sb.append("\n");

        }

        return sb.toString();

    }



    public boolean isGenerateGetterAndSetter() {
        return generateGetterAndSetter;
    }

    public void setGenerateGetterAndSetter(boolean generateGetterAndSetter) {
        this.generateGetterAndSetter = generateGetterAndSetter;
    }
}
