package www.autogeneratecode.codegen;

import com.intellij.psi.impl.source.PsiJavaFileImpl;

import java.io.File;
import java.util.List;

public class ProjectConfig {

    private File workSpace;
    private File projectDirectory;
    private String shortName;
    private boolean generateEntity = true;
    private boolean generateService = true;
    private boolean generateDAO = true;
    private boolean generateIbatisSql = true;
    private boolean generateDDL = true;
    private boolean generateResource = true;

    private boolean generateServiceTestCase = true;
    private boolean generateIbatisConfig = true;
    private boolean generateSpringConfig = true;
    private boolean databaseDrop = false;
    private boolean databaseCreate = false;
    private boolean executeInitScript = true;
    private boolean projectCompile = true;
    private boolean projectTest = false;

    protected final List<Class<?>> entities = null;
    List<PsiJavaFileImpl> psiFiles = null;

    public File getWorkSpace() {
        return workSpace;
    }

    public void setWorkSpace(File workSpace) {
        this.workSpace = workSpace;
    }

    public File getProjectDirectory() {
        return projectDirectory;
    }

    public String getShortName() {
        return shortName;
    }

    public boolean isGenerateEntity() {
        return generateEntity;
    }

    public boolean isGenerateService() {
        return generateService;
    }

    public boolean isGenerateDAO() {
        return generateDAO;
    }

    public boolean isGenerateIbatisSql() {
        return generateIbatisSql;
    }

    public boolean isGenerateDDL() {
        return generateDDL;
    }

    public boolean isGenerateResource() {
        return generateResource;
    }

    public boolean isGenerateServiceTestCase() {
        return generateServiceTestCase;
    }

    public boolean isGenerateIbatisConfig() {
        return generateIbatisConfig;
    }

    public boolean isGenerateSpringConfig() {
        return generateSpringConfig;
    }

    public boolean isDatabaseDrop() {
        return databaseDrop;
    }

    public boolean isDatabaseCreate() {
        return databaseCreate;
    }

    public boolean isExecuteInitScript() {
        return executeInitScript;
    }

    public boolean isProjectCompile() {
        return projectCompile;
    }

    public boolean isProjectTest() {
        return projectTest;
    }

    public List<Class<?>> getEntities() {
        return entities;
    }

    public List<PsiJavaFileImpl> getPsiFiles() {
        return psiFiles;
    }

    public void setPsiFiles(List<PsiJavaFileImpl> psiFiles) {
        this.psiFiles = psiFiles;
    }

    public void setProjectDirectory(File projectDirectory) {
        this.projectDirectory = projectDirectory;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setGenerateEntity(boolean generateEntity) {
        this.generateEntity = generateEntity;
    }

    public void setGenerateService(boolean generateService) {
        this.generateService = generateService;
    }

    public void setGenerateDAO(boolean generateDAO) {
        this.generateDAO = generateDAO;
    }

    public void setGenerateIbatisSql(boolean generateIbatisSql) {
        this.generateIbatisSql = generateIbatisSql;
    }

    public void setGenerateDDL(boolean generateDDL) {
        this.generateDDL = generateDDL;
    }

    public void setGenerateResource(boolean generateResource) {
        this.generateResource = generateResource;
    }

    public void setGenerateServiceTestCase(boolean generateServiceTestCase) {
        this.generateServiceTestCase = generateServiceTestCase;
    }

    public void setGenerateIbatisConfig(boolean generateIbatisConfig) {
        this.generateIbatisConfig = generateIbatisConfig;
    }

    public void setGenerateSpringConfig(boolean generateSpringConfig) {
        this.generateSpringConfig = generateSpringConfig;
    }

    public void setDatabaseDrop(boolean databaseDrop) {
        this.databaseDrop = databaseDrop;
    }

    public void setDatabaseCreate(boolean databaseCreate) {
        this.databaseCreate = databaseCreate;
    }

    public void setExecuteInitScript(boolean executeInitScript) {
        this.executeInitScript = executeInitScript;
    }

    public void setProjectCompile(boolean projectCompile) {
        this.projectCompile = projectCompile;
    }

    public void setProjectTest(boolean projectTest) {
        this.projectTest = projectTest;
    }
}

