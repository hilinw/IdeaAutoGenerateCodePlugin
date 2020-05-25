package www.autobuildcode.com;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Autobuildcode extends AnAction {

    BuildCodeUI buildCodeUI = null;

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {

        if (buildCodeUI == null) {
            buildCodeUI = new BuildCodeUI();
            buildCodeUI.setSize(600, 100);

            Toolkit kit = Toolkit.getDefaultToolkit();    // 定义工具包
            Dimension screenSize = kit.getScreenSize();   // 获取屏幕的尺寸
            int screenWidth = screenSize.width / 2;         // 获取屏幕的宽
            int screenHeight = screenSize.height / 2;       // 获取屏幕的高
            int height = buildCodeUI.getHeight();
            int width = buildCodeUI.getWidth();

            buildCodeUI.setLocation(screenWidth - width / 2, screenHeight - height / 2);
        }

        Project project = anActionEvent.getData(PlatformDataKeys.PROJECT);

        Editor editor = anActionEvent.getData(PlatformDataKeys.EDITOR);

        String currentFile = "";

        String projectPath = project.getBasePath();
        if (editor == null) {

            currentFile = projectPath;
        } else {
            PsiFile currentEditorFile = PsiUtilBase.getPsiFileInEditor(editor, project);
            currentFile = currentEditorFile.getContainingDirectory().getVirtualFile().getCanonicalPath();

        }


        System.out.println("currentFile" + currentFile);
        buildCodeUI.getCurrentFile().setText(currentFile);
        //buildCodeUI.getCurrentFile().setName(currentFile);


        buildCodeUI.pack();

        buildCodeUI.setVisible(true);


    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);

        //TODO
        if (false) {
            //e.getPresentation().setEnabledAndVisible(false);
            e.getPresentation().setEnabled(false);
        } else {
            //e.getPresentation().setEnabledAndVisible(true);
            e.getPresentation().setEnabled(true);
        }

    }
}
