package www.autobuildcode.com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BuildCodeUI extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JLabel labAutoBuildCode;
    private JCheckBox checkBoxVo;
    private JCheckBox checkBoxService;
    private JCheckBox checkBoxIbatis;
    private JCheckBox checkBoxLanage;
    private JCheckBox checkBoxDDL;

    public BuildCodeUI() {
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
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        BuildCodeUI dialog = new BuildCodeUI();

        dialog.setSize(600, 100);
        Toolkit kit = Toolkit.getDefaultToolkit();    // 定义工具包
        Dimension screenSize = kit.getScreenSize();   // 获取屏幕的尺寸
        int screenWidth = screenSize.width/2;         // 获取屏幕的宽
        int screenHeight = screenSize.height/2;       // 获取屏幕的高

        int height = dialog.getHeight();
        int width = dialog.getWidth();

        System.out.println("width:"+width+",height:"+height);
        dialog.setBounds(screenWidth-width/2, screenHeight-height/2, width, height);

        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
