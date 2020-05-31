package www.autogeneratecode.com;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.PsiJavaFileImpl;
import com.intellij.psi.util.PsiUtilBase;
import org.jetbrains.annotations.NotNull;
import www.autogeneratecode.codegen.ProjectConfig;
import www.autogeneratecode.model.Entity;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AutoGeneratecode extends AnAction {

    protected GenerateCodeUI generateCodeUI = null;
    protected MetadataDialogUI metadataDialogUI = null;

    protected boolean showMetadataDialog = false;
    protected List<PsiJavaFileImpl> psiFiles = new ArrayList<PsiJavaFileImpl>();
    protected List<PsiClass> psiClasss = new ArrayList<PsiClass>();

    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);

        if (checkSelect(e)) {
            //e.getPresentation().setEnabledAndVisible(false);
            e.getPresentation().setEnabled(true);
        } else {
            //e.getPresentation().setEnabledAndVisible(true);
            e.getPresentation().setEnabled(false);
        }

    }

    public void showInCenter(JDialog jDialog) {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenW = screenSize.width / 2;
        int screenH = screenSize.height / 2;
        int height = jDialog.getPreferredSize().height;
        int width = jDialog.getPreferredSize().width;

        jDialog.setSize(width, height);
        jDialog.setLocation(screenW - width / 2, screenH - height / 2);
    }


    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {

        if (showMetadataDialog) {
            if (metadataDialogUI == null) {
                metadataDialogUI = new MetadataDialogUI();
                metadataDialogUI.setPreferredSize(new Dimension(600, 400));
                showInCenter(metadataDialogUI);
            }
            metadataDialogUI.setPsiJavaFileImpl(psiFiles.get(0));
            metadataDialogUI.pack();
            metadataDialogUI.setVisible(true);

            return;
        }

        if (generateCodeUI == null) {
            generateCodeUI = new GenerateCodeUI();
            generateCodeUI.setPreferredSize(new Dimension(650, 380));

            showInCenter(generateCodeUI);

        }

        ProjectConfig projectConfig = getProjectConfig(anActionEvent);

//        System.out.println("WorkSpace:" + projectConfig.getWorkSpace());
//        System.out.println("ProjectDirectory:" + projectConfig.getProjectDirectory());
//        System.out.println("ShortName:" + projectConfig.getShortName());


        generateCodeUI.setConfig(projectConfig);
        generateCodeUI.init();

        generateCodeUI.pack();

        generateCodeUI.setVisible(true);


    }


    public static String getFileExtension(DataContext dataContext) {
        VirtualFile file = DataKeys.VIRTUAL_FILE.getData(dataContext);
        return file == null ? null : file.getExtension();
    }


    public boolean checkSelect(AnActionEvent e) {

        showMetadataDialog = false;
        psiFiles.clear();
        //条件：选择的为java文件，并且以metadata开头的路径

        Project project = e.getData(PlatformDataKeys.PROJECT);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        DataContext dataContext = e.getDataContext();
        String filetype = getFileExtension(dataContext);
        //System.out.println("filetype: " + filetype);

        if (filetype == null || !"java".equalsIgnoreCase(filetype)) {
            return false;
        } else {

            //check file is '.java' and it's package start with 'metadata.'
            if (editor != null) {
                PsiFile currentEditorFile = PsiUtilBase.getPsiFileInEditor(editor, project);
                if (currentEditorFile.getFileType().getName().equalsIgnoreCase("JAVA")) {
                    PsiJavaFileImpl psiJavaFileImpl = (PsiJavaFileImpl) currentEditorFile;
                    String packageName = psiJavaFileImpl.getPackageName();
                    psiFiles.add(psiJavaFileImpl);
                    PsiClass[] psiClass1 = psiJavaFileImpl.getClasses();
                    //for(PsiClass  psiClass : psiClass1) {
                        psiClasss.add(psiClass1[0]);
                    //}

                    if (!packageName.startsWith("metadata.") || psiClass1[0].getAnnotation("www.autogeneratecode.model.Entity") == null) {
                        showMetadataDialog = true;
                    }
                    return true;
                }
            } else {
                PsiElement[] psiElements = (PsiElement[]) dataContext.getData(LangDataKeys.PSI_ELEMENT_ARRAY.getName());
                if (psiElements != null) {
                    for (PsiElement psiElement : psiElements) {
                        PsiFile psiFile = psiElement.getContainingFile();
                        if (psiFile.getFileType().getName().equalsIgnoreCase("JAVA")) {

                            PsiJavaFileImpl psiJavaFileImpl = (PsiJavaFileImpl) psiFile;
                            String packageName = psiJavaFileImpl.getPackageName();
                            psiFiles.add(psiJavaFileImpl);
                            PsiClass[] psiClass1 = psiJavaFileImpl.getClasses();
                            //for(PsiClass  psiClass : psiClass1) {
                                psiClasss.add(psiClass1[0]);
                            //}
                            if (!packageName.startsWith("metadata.") || psiClass1[0].getAnnotation("www.autogeneratecode.model.Entity") == null) {
                                if (psiElements.length == 1) {
                                    showMetadataDialog = true;
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            if (psiFiles.size() > 0) {
                return true;
            } else {

                return false;
            }

        }
    }

    private ProjectConfig getProjectConfig(AnActionEvent e) {

        ProjectConfig projectConfig = new ProjectConfig();

        String currentFile;
        Project project = e.getData(PlatformDataKeys.PROJECT);
        //Project project = e.getProject();
        String shortName = project.getName();

        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        String projectPath = project.getBasePath();


        File workspaceFile = new File(projectPath);
        File tempFile = new File(workspaceFile, ".idea\\.plugins\\autobuildcode\\buildcode_temp");
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }
        projectConfig.setWorkSpace(tempFile);
        projectConfig.setProjectDirectory(new File(projectPath));
        projectConfig.setShortName(shortName);

        projectConfig.setPsiFiles(psiFiles);

        return projectConfig;
    }


}
