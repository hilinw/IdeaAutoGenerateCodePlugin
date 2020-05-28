package www.autogeneratecode.codegen;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import www.autogeneratecode.model.Entity;
import www.autogeneratecode.utils.CopyTask;

import java.io.File;
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


    public void generate() {

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

                Iterator var7 = this.config.getEntities().iterator();

                while(var7.hasNext()) {
                    Class<?> entity = (Class)var7.next();
                    if (entity.getAnnotation(Entity.class) != null ) {
//                        if (this.config.isGenerateEntity()) {
//                            this.generateEntityFile(entity);
//                        }
//
//                        if (this.config.isGenerateDAO()) {
//                            this.generateDaoFile(entity);
//                        }
//
//                        if (this.config.isGenerateService()) {
//                            this.generateServiceFile(entity);
//                        }
//
//                        if (this.config.isGenerateServiceTestCase()) {
//                            this.generateTestServiceFile(entity);
//                        }
//
//                        if (this.config.isGenerateIbatisSql()) {
//                            this.generateIbatisSqlFile(entity);
//                        }
//
//                        if (this.config.isGenerateResource()) {
//                            this.generateResource(entity);
//                        }
//
//                        this.generateEntityEnum(entity);
                    }

//                    if (this.config.isGenerateDDL()) {
//                        this.generateDDLFile(entity);
//                    }

//                    if (entity.isEnum()) {
//                        this.generateEnum(entity);
//                    }
                }

                if (this.getProjectDir() != null) {


//                    var7 = this.fileTasks.iterator();
//
//                    while(var7.hasNext()) {
//                        CopyTask item = (CopyTask)var7.next();
//                        item.execute();
//                    }
//
//                    if (this.config.isGenerateIbatisConfig()) {
//                        this.generateIbatisConfig();
//                    }
//
//                    if (this.config.isGenerateSpringConfig()) {
//                        this.generateSpringConfig();
//                        this.generateSpringContextConfig();
//                    }
//
//                    if (this.config.isDatabaseCreate()) {
//                        this.databaseCreate();
//                    }




                }


            } catch (CodeGenException e1) {
                if (LOG.isErrorEnabled()) {
                    LOG.error(e1.getMessage(), e1);
                }
                throw e1;
            } catch (Exception e2) {
                if (LOG.isErrorEnabled()) {
                    LOG.error(e2.getMessage(), e2);
                }
                new CodeGenException("代码生成出错"+e2.getMessage());
            }
        }
    }

    public ProjectConfig getConfig() {
        return config;
    }

    public void setConfig(ProjectConfig config) {
        this.config = config;
    }
}
