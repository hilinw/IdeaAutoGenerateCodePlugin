package www.autogeneratecode.com;

import com.intellij.psi.PsiClass;
import com.intellij.psi.impl.source.PsiJavaFileImpl;
import www.autogeneratecode.codegen.CodeGenException;
import www.autogeneratecode.codegen.CodeGenerator;
import www.autogeneratecode.codegen.ProjectConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class GenerateCodeUI extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textCurrentFile;
    private JLabel labAutoGenerateCode;
    private JCheckBox checkBoxVo;
    private JCheckBox checkBoxService;
    private JCheckBox checkBoxController;
    private JCheckBox checkBoxDao;
    private JCheckBox checkBoxIbatis;
    private JCheckBox checkBoxLanage;
    private JCheckBox checkBoxDDL;
    private JCheckBox checkBoxGSetter;
    private JPanel msgPanel;
    private JLabel errMsgLable;
    private JCheckBox checkTransactional;
    private JCheckBox checkSameDir;
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
        checkBoxVo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkBoxVo.isSelected()) {
                    checkBoxGSetter.setEnabled(true);
                    checkBoxGSetter.setSelected(true);
                } else {
                    checkBoxGSetter.setEnabled(false);
                    checkBoxGSetter.setSelected(false);
                }
            }
        });
        checkBoxService.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkBoxService.isSelected()) {
                    checkTransactional.setEnabled(true);
                    checkTransactional.setSelected(true);
                } else {
                    checkTransactional.setEnabled(false);
                    checkTransactional.setSelected(false);
                }
            }
        });
    }

    private void onOK() {

        CodeGenerator generator = new CodeGenerator();

        config.setGenerateEntity(checkBoxVo.isSelected());
        config.setGenerateGetterAndSetter(checkBoxGSetter.isSelected());
        config.setGenerateService(checkBoxService.isSelected());
        config.setGenerateController(checkBoxController.isSelected());
        config.setGenerateDao(checkBoxDao.isSelected());
        config.setGenerateDDL(checkBoxDDL.isSelected());
        config.setGenerateIbatisSql(checkBoxIbatis.isSelected());
        config.setSameDir(checkSameDir.isSelected());
        config.setAddTransactional(checkTransactional.isSelected());

        try {
            generator.setConfig(config);
            //generator.addEntity(entity);

            generator.generate();
            generator.copyFile();
            generator.reflash();

        }catch ( CodeGenException e){
            errMsgLable.setText("Error:"+e.getMessage());
            errMsgLable.setForeground(Color.red);
            return;
        }
        dispose();
    }

    private void onCancel() {
        dispose();
    }


    public JTextField getCurrentFile() {
        return textCurrentFile;
    }


    public void init() {

        String javaFileName = null;
        if (this.config != null) {
            getCurrentFile().setText(this.config.getProjectDirectory().getPath());

            if (this.config.getPsiFiles() != null) {
                List<PsiJavaFileImpl> psiJavaFileImpls = this.config.getPsiFiles();
                if (psiJavaFileImpls.size() > 0) {
                    PsiClass[] psiClasss = psiJavaFileImpls.get(0).getClasses();
                    javaFileName = psiClasss[0].getName();
                }
            }
            if (javaFileName != null) {
                checkBoxVo.setText("Generate " + javaFileName + ".java");
                checkBoxService.setText("Generate " + javaFileName + "Service.java");
                checkBoxController.setText("Generate " + javaFileName + "Controller.java");
                checkBoxDao.setText("Generate " + javaFileName + "Dao.java");
                checkBoxIbatis.setText("Generate Ibatis Configuration File " + javaFileName + ".xml");
                checkBoxIbatis.setText("Generate " + "sqlmapping-" + javaFileName + ".xml");
                checkBoxDDL.setText("Generate " + "Create-" +javaFileName + ".sql");
            }
        }
    }

    public static void main(String[] args) {
        GenerateCodeUI dialog = new GenerateCodeUI();

        dialog.setPreferredSize(new Dimension(600, 380));
//        Toolkit kit = Toolkit.getDefaultToolkit();    // 定义工具包
//        Dimension screenSize = kit.getScreenSize();   // 获取屏幕的尺寸
//        int screenWidth = screenSize.width/2;         // 获取屏幕的宽
//        int screenHeight = screenSize.height/2;       // 获取屏幕的高


        int height = dialog.getPreferredSize().height;
        int width = dialog.getPreferredSize().width;
//        System.out.println("width:"+width+",height:"+height);

        dialog.setSize(width, height);
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

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public JLabel getErrMsgLable() {
        return errMsgLable;
    }
}
