package www.autogeneratecode.codegen;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.impl.source.PsiJavaFileImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import www.autogeneratecode.generator.*;
import www.autogeneratecode.model.Entity;
import www.autogeneratecode.utils.CopyTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CodeGenerator {
    private static final Log LOG = LogFactory.getLog(CodeGenerator.class);
    protected ProjectConfig config;
    protected List<CopyTask> fileTasks = new ArrayList();

    public CodeGenerator() {
    }

    public CodeGenerator(ProjectConfig config) {
        this.config = config;
    }

    private File getProjectDir() {
        return this.config != null ? this.config.getProjectDirectory() : null;
    }


    public void generate() throws  CodeGenException{

        if (this.config.getWorkSpace() == null) {
            throw new CodeGenException("代码生成的工作目录未设置");
        } else {
            String shortName = this.config.getShortName();
            if (StringUtils.isEmpty(shortName)) {
                shortName = this.config.getProjectDirectory().getName();
            }

            int p = shortName.indexOf("-");
            if (p > 0) {
                shortName = shortName.substring(p + 1);
            }

            this.config.setShortName(shortName);

            try {
                if (LOG.isInfoEnabled()) {
                    LOG.info("workspace : " + this.config.getWorkSpace());
                }

                Iterator iterator = this.config.getPsiFiles().iterator();

                while (iterator.hasNext()) {
                    PsiJavaFileImpl psiJavaFileImpl = (PsiJavaFileImpl) iterator.next();
                    PsiClass[] psiClass1 = psiJavaFileImpl.getClasses();
                    if (psiClass1[0].getAnnotation("www.autogeneratecode.model.Entity") != null) {
                        if (this.config.isGenerateEntity()) {
                            this.generateEntityFile(psiJavaFileImpl);
                        }
                        if (this.config.isGenerateController()) {
                            this.generateControllerFile(psiJavaFileImpl);
                        }
                        if (this.config.isGenerateService()) {
                            this.generateServiceFile(psiJavaFileImpl);
                        }
                        if (this.config.isGenerateDao()) {
                            this.generateDaoFile(psiJavaFileImpl);
                        }
                        if (this.config.isGenerateIbatisSql()) {
                            this.generateSqlMappingFile(psiJavaFileImpl);
                        }
                        if (this.config.isGenerateDDL()) {
                            this.generateDDLFile(psiJavaFileImpl);
                        }
                    }

                }

                if (this.getProjectDir() != null) {

                }
            } catch (IOException e1) {
                if (LOG.isErrorEnabled()) {
                    LOG.error(e1.getMessage(), e1);
                }
                throw new CodeGenException(e1);
            }
            //this.reflash();
        }
    }

    public void reflash() {
        PsiJavaFileImpl pjf = this.config.getPsiFiles().get(0);
        String packageName = pjf.getPackageName();
        VirtualFile vf = pjf.getVirtualFile().getParent();
        String[] arr = packageName.split("\\.");
        int i = arr.length;
        for (int j = 0; j < i; j++) {
            if (vf != null) {
                vf = vf.getParent();
            }
        }
        System.out.println("refresh file path :"+vf.getPath());
        vf.refresh(true, true);

    }

    private File[] generateEntityFile(PsiJavaFileImpl psiJavaFileImpl) throws IOException {
        PsiClass[] classes = psiJavaFileImpl.getClasses();
        File[] files = new File[classes.length];
        int i = 0;
        String packageName = psiJavaFileImpl.getPackageName();

        JavaEntityGenerator generator = new JavaEntityGenerator(psiJavaFileImpl, packageName);
        generator.setGenerateGetterAndSetter(config.isGenerateGetterAndSetter());
        generator.setSameDir(config.isSameDir());
        generator.generate();
        files[i] = generator.write(this.config.getWorkSpace());

        if (this.getProjectDir() != null) {
            this.fileTasks.add(new CopyTask(files[i], this.getJavaFileDir(psiJavaFileImpl,"vo")));
        }
        LOG.info("generate: '" + psiJavaFileImpl.getName() + "'  ok.");
        i++;

        return files;

    }

    private File generateControllerFile(PsiJavaFileImpl psiJavaFileImpl) throws IOException {

        PsiClass[] classes = psiJavaFileImpl.getClasses();
        File[] files = new File[classes.length];
        int i = 0;
        String packageName = psiJavaFileImpl.getPackageName();

        JavaControllerGenerator generator = new JavaControllerGenerator(psiJavaFileImpl, packageName);
        generator.setSameDir(config.isSameDir());
        generator.setAddTransactional(config.isAddTransactional());//是否增加 事务标注
        generator.setNoInterface(config.isNoInterface());
        generator.generate();
        files[i] = generator.write(this.config.getWorkSpace());


        if (this.getProjectDir() != null) {
            this.fileTasks.add(new CopyTask(files[i], this.getJavaFileDir(psiJavaFileImpl,"controller")));
        }

        LOG.info("generate: '" + files[i].getName() + "'  ok.");
        System.out.println("generate: '" + files[i].getName() + "'  ok.");

        return files[i];

    }


    private void generateServiceFile(PsiJavaFileImpl psiJavaFileImpl) throws IOException {

        PsiClass[] classes = psiJavaFileImpl.getClasses();
        File[] files = new File[classes.length];
        int i = 0;
        String packageName = psiJavaFileImpl.getPackageName();

        JavaServiceGenerator generator = new JavaServiceGenerator(psiJavaFileImpl, packageName);
        generator.setSameDir(config.isSameDir());
        generator.setAddTransactional(config.isAddTransactional());//是否增加 事务标注
        generator.setNoInterface(config.isNoInterface());

        generator.generate();

        if(!this.config.isNoInterface()) {
            //接口类
            files[i] = generator.write(this.config.getWorkSpace());


            if (this.getProjectDir() != null) {
                this.fileTasks.add(new CopyTask(files[i], this.getJavaFileDir(psiJavaFileImpl, "service")));
            }

            System.out.println("generate: '" + files[i].getName() + "'  ok.");
        }
        // 写实现类
        files[i]  = generator.writeImpl(this.config.getWorkSpace());

        if (this.getProjectDir() != null) {
            String filepath = "";
            if(this.config.isNoInterface()) {
                filepath = "service";
            }else {
                filepath = "service" + File.separator + "impl";
            }
            this.fileTasks.add(new CopyTask(files[i], this.getJavaFileDir(psiJavaFileImpl,filepath)));
        }

        System.out.println("generate: '" + files[i].getName() + "'  ok.");


    }

    private File generateDaoFile(PsiJavaFileImpl psiJavaFileImpl) throws IOException {

        PsiClass[] classes = psiJavaFileImpl.getClasses();
        File[] files = new File[classes.length];
        int i = 0;
        String packageName = psiJavaFileImpl.getPackageName();

        JavaDaoGenerator generator = new JavaDaoGenerator(psiJavaFileImpl, packageName);
        generator.setSameDir(config.isSameDir());
        generator.generate();
        files[i] = generator.write(this.config.getWorkSpace());


        if (this.getProjectDir() != null) {
            this.fileTasks.add(new CopyTask(files[i], this.getJavaFileDir(psiJavaFileImpl,"dao")));
        }

        LOG.info("generate: '" + files[i].getName() + "'  ok.");
        System.out.println("generate: '" + files[i].getName() + "'  ok.");

        return files[i];

    }


    private File generateSqlMappingFile(PsiJavaFileImpl psiJavaFileImpl) throws IOException {

        PsiClass[] classes = psiJavaFileImpl.getClasses();
        File[] files = new File[classes.length];
        int i = 0;
        String packageName = psiJavaFileImpl.getPackageName();
        SqlMappingGenerator generator = new SqlMappingGenerator(psiJavaFileImpl, packageName);
        generator.setSameDir(config.isSameDir());
        files[i] = generator.write(this.config.getWorkSpace());
        if (this.getProjectDir() != null) {
            this.fileTasks.add(new CopyTask(files[i], this.getSqlFileDir(psiJavaFileImpl,"sql")));
        }
        LOG.info("generate: '" + files[i].getName() + "'  ok.");
        System.out.println("generate: '" + files[i].getName() + "'  ok.");
        return files[i];
    }

    private File[] generateDDLFile(PsiJavaFileImpl psiJavaFileImpl) throws IOException {
        PsiClass[] classes = psiJavaFileImpl.getClasses();
        File[] files = new File[classes.length];
        int i = 0;
        String packageName = psiJavaFileImpl.getPackageName();
//        for (PsiClass psiClass : classes) {

        DDLGenerator generator = new DDLGenerator(psiJavaFileImpl);

        generator.generate();
        files[i] = generator.write(this.config.getWorkSpace());

        if (this.getProjectDir() != null) {
            this.fileTasks.add(new CopyTask(files[i], this.getSqlFileDir(psiJavaFileImpl,"sql")));
        }

        LOG.info("generate: '" + psiJavaFileImpl.getName() + "'  ok.");
        i++;
//        }

        return files;

    }

    /**
     * 生成VO文件的路径。 在原来的路径里面去掉metadata
     */
    private File getJavaFileDir(PsiJavaFileImpl psiJavaFileImpl,String childDir) {

        String filePath = psiJavaFileImpl.getVirtualFile().getParent().getPath();
        int p = filePath.indexOf("metadata");
        String newPath = "";
        if (p > 0) {
            newPath = filePath.substring(0, p);
            newPath = newPath + filePath.substring(p + 9);
        }

        if (!config.isSameDir()) {
            if (childDir != null && childDir.trim().length() > 0) {

                newPath = newPath + File.separator + childDir;
            }
        }

        File file = new File(newPath);
        System.out.println("newJavaFilePath:" + file.getPath());
        return file;

    }

    /**
     * 生成sql文件的路径。
     * 如果是maven结构，生成在 src/main/resources目录下
     */
    private File getSqlFileDir(PsiJavaFileImpl psiJavaFileImpl,String childDir) {

//		String filePath = psiJavaFileImpl.getVirtualFile().getParent().getPath();
        String filePath = psiJavaFileImpl.getVirtualFile().getParent().getPath();
        //生成在 src/main/resources目录下
        int p = filePath.indexOf("src/main");
        String newPath = "";
        if (p > 0) {
            newPath = filePath.substring(0, p+8);
            newPath = newPath + File.separator+"resources";
        }else {
            p = filePath.indexOf("src");
            if (p > 0) {
                newPath = filePath.substring(0, p+3);
                newPath = newPath +File.separator+"main"+ File.separator+"resources";
            }else {
                //在原来的路径里面去掉metadata
                p = filePath.indexOf("metadata");
                if (p > 0) {
                    newPath = filePath.substring(0, p);
                    newPath = newPath + filePath.substring(p + 9);
                }

            }
        }
        if (childDir != null && childDir.trim().length() > 0) {
            newPath = newPath + File.separator + childDir;
        }

        File file = new File(newPath);
        System.out.println("newJavaFilePath:" + file.getPath());
        return file;

    }

    public void copyFile(){
        try {
            for (CopyTask copyTask : fileTasks) {

                copyTask.execute();
            }
        }catch (IOException e){
            if (LOG.isErrorEnabled()) {
                LOG.error(e.getMessage(), e);
            }
            new CodeGenException("copy code error:" + e.getMessage());

        }

    }

    public ProjectConfig getConfig() {
        return config;
    }

    public void setConfig(ProjectConfig config) {
        this.config = config;
    }
}
