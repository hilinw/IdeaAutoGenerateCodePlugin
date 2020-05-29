package www.autogeneratecode.com;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.impl.source.PsiJavaFileImpl;
import www.autogeneratecode.codegen.CodeGenException;
import www.autogeneratecode.utils.CopyTask;
import www.autogeneratecode.utils.IOUtils;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;
import java.util.Vector;

public class MetadataDialogUI extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextArea textArea;
    private PsiJavaFileImpl psiJavaFileImpl = null;

    public MetadataDialogUI() {


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

        String packageName = psiJavaFileImpl.getPackageName();

        String newPackageName = "metadata."+packageName;
        String fileName = psiJavaFileImpl.getName();
        String filePath = psiJavaFileImpl.getVirtualFile().getParent().getPath();
        String packageName2 = packageName.replace(".","/");
        int p = filePath.indexOf(packageName2);
        String newPath  = "";
        if( p >0){
            newPath = filePath.substring(0,p);
            newPath = newPath + "metadata/"+packageName2;
        }
        if(newPath.length() >0){
            File file = new File(newPath);
            file.mkdirs();
            try {
                String source = getExampleFile(newPackageName);
                File javafile = new File(newPath,  "ExampleFile.java");
                System.out.println("javafile:"+javafile.getPath());
                if(javafile.exists()){
                    javafile.delete();
                }
                OutputStreamWriter writer = null;
                try {
                    writer = new OutputStreamWriter(new FileOutputStream(javafile), "UTF-8");
                    writer.write(source);
                } finally {
                    IOUtils.close(writer);
                }
                this.reflash(packageName);

            } catch (Exception e1) {
                throw new CodeGenException("生成ExampleFile.java出错， '", e1);
            }
        }

        dispose();
    }

    private  void reflash(String packageName){

        VirtualFile vf = psiJavaFileImpl.getVirtualFile().getParent();
        String[] arr = packageName.split("\\.");

        int i = arr.length;

        for(int j = 0 ;j < i; j++){
            if(vf != null) {
                vf = vf.getParent();
            }
        }
        System.out.println("vf:"+vf.getPath());
        vf.refresh(true,true);

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        MetadataDialogUI dialog = new MetadataDialogUI();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private String getExampleFile(String newPackageName){

        StringBuilder sb = new StringBuilder();

        sb.append("package ");
        sb.append(newPackageName);

        sb.append(";\n");
        sb.append("\n");
        sb.append("import www.autobuildcode.model.Column;\n");
        sb.append("import www.autobuildcode.model.Comment;\n");
        sb.append("import www.autobuildcode.model.Entity;\n");
        sb.append("import www.autobuildcode.model.Table;\n");

        sb.append("\n");


        sb.append("@Entity\n");
        sb.append("@Comment(content = \"ExampleFile\")\n");
        sb.append("@Table(name=\"T_ExampleFile\")\n");
        sb.append("public class ExampleFile {\n");

        sb.append("\n");
        sb.append("    @Comment(content=\"用户ID\")\n");
        sb.append("    @Column(name=\"FUSERID\", dataType=\"decimal\", precision=32)\n");
        sb.append("    public long id;\n");
        sb.append(" \n");



        sb.append("    @Comment(content=\"用户名\")\n");
        sb.append("    @Column(name=\"FNAME\", dataType=\"VARCHAR\" ,nullable=false ,length=50)\n");
        sb.append("    public String name;\n");
        sb.append("\n");


        sb.append("    @Comment(content=\"用户编码\")\n");
        sb.append("    @Column(name=\"FNUMBER\", dataType=\"VARCHAR\" ,nullable=false ,length=20)\n");
        sb.append("    public String number;\n");
        sb.append("\n");

        sb.append("}\n");
        sb.append("\n");

        return sb.toString();

    }



    public PsiJavaFileImpl getPsiJavaFileImpl() {
        return psiJavaFileImpl;
    }

    public void setPsiJavaFileImpl(PsiJavaFileImpl psiJavaFileImpl) {
        this.psiJavaFileImpl = psiJavaFileImpl;
    }
}
