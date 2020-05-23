package www.autobuildcode.com;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import java.awt.*;

public class Autobuildcode extends AnAction {

    BuildCodeUI buildCodeUI = null;
    @Override
    public void actionPerformed(AnActionEvent e) {

        if(buildCodeUI == null){
            buildCodeUI = new BuildCodeUI();
            buildCodeUI.setSize(600, 100);

            Toolkit kit = Toolkit.getDefaultToolkit();    // 定义工具包
            Dimension screenSize = kit.getScreenSize();   // 获取屏幕的尺寸
            int screenWidth = screenSize.width/2;         // 获取屏幕的宽
            int screenHeight = screenSize.height/2;       // 获取屏幕的高
            int height = buildCodeUI.getHeight();
            int width = buildCodeUI.getWidth();

            buildCodeUI.setLocation(screenWidth-width/2, screenHeight-height/2);
        }


        buildCodeUI.pack();
        buildCodeUI.setVisible(true);

    }
}
