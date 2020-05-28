package www.autogeneratecode.com;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.PsiJavaFileImpl;
import com.intellij.psi.util.PsiUtilBase;
import org.jetbrains.annotations.NotNull;
import www.autogeneratecode.codegen.ProjectConfig;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AutoGeneratecode extends AnAction {

    protected GenerateCodeUI buildCodeUI = null;
    protected boolean showMetadataDialog = false;
    List<PsiJavaFileImpl> psiFiles = new ArrayList<PsiJavaFileImpl>();

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

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {

        if (!showMetadataDialog) {


        }

        if (buildCodeUI == null) {
            buildCodeUI = new GenerateCodeUI();
            buildCodeUI.setPreferredSize(new Dimension(600, 380));

            Toolkit kit = Toolkit.getDefaultToolkit();    // 定义工具包
            Dimension screenSize = kit.getScreenSize();   // 获取屏幕的尺寸
            int screenW = screenSize.width / 2;         // 获取屏幕的宽
            int screenH = screenSize.height / 2;       // 获取屏幕的高
            int height = buildCodeUI.getPreferredSize().height;
            int width = buildCodeUI.getPreferredSize().width;

            buildCodeUI.setSize(width, height);
            buildCodeUI.setLocation(screenW - width / 2, screenH - height / 2);
        }

        ProjectConfig projectConfig = getProjectConfig(anActionEvent);

//        System.out.println("WorkSpace:" + projectConfig.getWorkSpace());
//        System.out.println("ProjectDirectory:" + projectConfig.getProjectDirectory());
//        System.out.println("ShortName:" + projectConfig.getShortName());
        buildCodeUI.getCurrentFile().setText(projectConfig.getProjectDirectory().getPath());


        buildCodeUI.setConfig(projectConfig);

        buildCodeUI.pack();

        buildCodeUI.setVisible(true);


    }

    public static String getFileExtension(DataContext dataContext) {
        VirtualFile file = DataKeys.VIRTUAL_FILE.getData(dataContext);
        return file == null ? null : file.getExtension();
    }


    public boolean checkSelect(AnActionEvent e) {

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

            //检查是否为java文件并且是以metadata开头的路径
            if (editor != null) {
                PsiFile currentEditorFile = PsiUtilBase.getPsiFileInEditor(editor, project);
                if (currentEditorFile.getFileType().getName().equalsIgnoreCase("JAVA")) {
                    PsiJavaFileImpl psiJavaFileImpl = (PsiJavaFileImpl) currentEditorFile;
                    String packageName = psiJavaFileImpl.getPackageName();
                    psiFiles.add(psiJavaFileImpl);
                    if (!packageName.startsWith("metadata.")) {
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
                            if (packageName.startsWith("metadata.")) {
                                psiFiles.add(psiJavaFileImpl);
                            }else{
                                if(psiElements.length ==1){
                                    psiFiles.add(psiJavaFileImpl);
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


        return projectConfig;
    }


}