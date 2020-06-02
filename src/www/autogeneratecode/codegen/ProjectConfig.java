package www.autogeneratecode.codegen;

import com.intellij.psi.PsiClass;
import com.intellij.psi.impl.source.PsiJavaFileImpl;

import java.io.File;
import java.util.List;

public class ProjectConfig {

    private File workSpace;
    private File projectDirectory;
    private String shortName;
    private boolean generateEntity = true;
    private boolean generateGetterAndSetter = true;
    private boolean generateService = true;
    private boolean generateController = true;
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

    protected List<PsiJavaFileImpl> psiFiles = null;

    public File getWorkSpace() {
        return workSpace;
    }

    public void setWorkSpace(File workSpace) {
        this.workSpace = workSpace;
    }

    public File getProjectDirectory() {
        return projectDirectory;
    }

    public void setProjectDirectory(File projectDirectory) {
        this.projectDirectory = projectDirectory;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public boolean isGenerateEntity() {
        return generateEntity;
    }

    public void setGenerateEntity(boolean generateEntity) {
        this.generateEntity = generateEntity;
    }

    public boolean isGenerateGetterAndSetter() {
        return generateGetterAndSetter;
    }

    public void setGenerateGetterAndSetter(boolean generateGetterAndSetter) {
        this.generateGetterAndSetter = generateGetterAndSetter;
    }

    public boolean isGenerateService() {
        return generateService;
    }

    public void setGenerateService(boolean generateService) {
        this.generateService = generateService;
    }

    public boolean isGenerateController() {
        return generateController;
    }

    public void setGenerateController(boolean generateController) {
        this.generateController = generateController;
    }

    public boolean isGenerateDAO() {
        return generateDAO;
    }

    public void setGenerateDAO(boolean generateDAO) {
        this.generateDAO = generateDAO;
    }

    public boolean isGenerateIbatisSql() {
        return generateIbatisSql;
    }

    public void setGenerateIbatisSql(boolean generateIbatisSql) {
        this.generateIbatisSql = generateIbatisSql;
    }

    public boolean isGenerateDDL() {
        return generateDDL;
    }

    public void setGenerateDDL(boolean generateDDL) {
        this.generateDDL = generateDDL;
    }

    public boolean isGenerateResource() {
        return generateResource;
    }

    public void setGenerateResource(boolean generateResource) {
        this.generateResource = generateResource;
    }

    public boolean isGenerateServiceTestCase() {
        return generateServiceTestCase;
    }

    public void setGenerateServiceTestCase(boolean generateServiceTestCase) {
        this.generateServiceTestCase = generateServiceTestCase;
    }

    public boolean isGenerateIbatisConfig() {
        return generateIbatisConfig;
    }

    public void setGenerateIbatisConfig(boolean generateIbatisConfig) {
        this.generateIbatisConfig = generateIbatisConfig;
    }

    public boolean isGenerateSpringConfig() {
        return generateSpringConfig;
    }

    public void setGenerateSpringConfig(boolean generateSpringConfig) {
        this.generateSpringConfig = generateSpringConfig;
    }

    public boolean isDatabaseDrop() {
        return databaseDrop;
    }

    public void setDatabaseDrop(boolean databaseDrop) {
        this.databaseDrop = databaseDrop;
    }

    public boolean isDatabaseCreate() {
        return databaseCreate;
    }

    public void setDatabaseCreate(boolean databaseCreate) {
        this.databaseCreate = databaseCreate;
    }

    public boolean isExecuteInitScript() {
        return executeInitScript;
    }

    public void setExecuteInitScript(boolean executeInitScript) {
        this.executeInitScript = executeInitScript;
    }

    public boolean isProjectCompile() {
        return projectCompile;
    }

    public void setProjectCompile(boolean projectCompile) {
        this.projectCompile = projectCompile;
    }

    public boolean isProjectTest() {
        return projectTest;
    }

    public void setProjectTest(boolean projectTest) {
        this.projectTest = projectTest;
    }

    public List<PsiJavaFileImpl> getPsiFiles() {
        return psiFiles;
    }

    public void setPsiFiles(List<PsiJavaFileImpl> psiFiles) {
        this.psiFiles = psiFiles;
    }
}

