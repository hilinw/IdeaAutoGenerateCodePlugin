package www.autogeneratecode.com;

import www.autogeneratecode.codegen.CodeGenerator;
import www.autogeneratecode.codegen.ProjectConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GenerateCodeUI extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textCurrentFile;
    private JLabel labAutoBuildCode;
    private JCheckBox checkBoxVo;
    private JCheckBox checkBoxService;
    private JCheckBox checkBoxIbatis;
    private JCheckBox checkBoxLanage;
    private JCheckBox checkBoxDDL;

    protected ProjectConfig config = null;


    public GenerateCodeUI() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here

        CodeGenerator generator = new CodeGenerator();

        config.setGenerateService(checkBoxService.isSelected());
        config.setGenerateEntity(checkBoxVo.isSelected());
        config.setGenerateDAO(checkBoxDDL.isSelected());
        config.setGenerateIbatisSql(checkBoxIbatis.isSelected());


        generator.setConfig(config);
        //generator.addEntity(entity);
        generator.generate();

        //SelectionUtil.refreshProject(project);
        //MessageDialog.openInformation(this.getShell(), "利维坦提示", "代码生成成功");


        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }


    public JTextField getCurrentFile() {
        return textCurrentFile;
    }


    public static void main(String[] args) {
        GenerateCodeUI dialog = new GenerateCodeUI();

        dialog.setPreferredSize(new Dimension(600, 380));
//        Toolkit kit = Toolkit.getDefaultToolkit();    // 定义工具包
//        Dimension screenSize = kit.getScreenSize();   // 获取屏幕的尺寸
//        int screenWidth = screenSize.width/2;         // 获取屏幕的宽
//        int screenHeight = screenSize.height/2;       // 获取屏幕的高
//
//
        int height = dialog.getPreferredSize().height;
        int width = dialog.getPreferredSize().width;
//        System.out.println("width:"+width+",height:"+height);

        dialog.setSize(width,height);
//        dialog.setLocation(screenWidth - width / 2, screenHeight - height / 2);



        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    public ProjectConfig getConfig() {
        return config;
    }

    public void setConfig(ProjectConfig config) {
        this.config = config;
    }
}
