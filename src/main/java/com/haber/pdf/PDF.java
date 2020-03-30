/*
 * Created by JFormDesigner on Mon Mar 23 16:13:00 CST 2020
 */

package com.haber.pdf;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

/**
 * @author 1
 */
public class PDF extends JPanel {
    public PDF() {

    }
    String dx = "";
    String dx1 = "";
    private void button1ActionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.showDialog(new JLabel(), "选择");
        File file = chooser.getSelectedFile();
        textField1.setText(file.getAbsoluteFile().toString());
        dx = file.getAbsoluteFile().toString();
        dx1 = file.getName();
    }

    private void button2ActionPerformed(ActionEvent e) {
        PDFExportImage PD = new PDFExportImage();
        try {
            PD.setup(dx, dx1.substring(0,dx1.length()-3)+"/");
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    public void run()
    {
        JFrame frame = new JFrame("PDF");
        initComponents();
        frame.setContentPane(this);
        frame.setResizable(false);
        frame.setSize(825,195);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);//窗体在屏幕中心出现
        frame.setVisible(true);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        textField1 = new JTextField();
        button1 = new JButton();
        button2 = new JButton();
        label1 = new JLabel();
        label2 = new JLabel();

        //======== this ========
        setLayout(null);
        add(textField1);
        textField1.setBounds(50, 64, 425, 26);

        //---- button1 ----
        button1.setText("\u9009\u62e9\u8def\u5f84");
        button1.addActionListener(e -> button1ActionPerformed(e));
        add(button1);
        button1.setBounds(525, 54, 90, 47);

        //---- button2 ----
        button2.setText("\u751f\u6210");
        button2.addActionListener(e -> button2ActionPerformed(e));
        add(button2);
        button2.setBounds(650, 54, 90, 47);

        //---- label1 ----
        label1.setText("\u8fd0\u884c");
        label1.setHorizontalAlignment(SwingConstants.CENTER);
        add(label1);
        label1.setBounds(136, 106, 253, label1.getPreferredSize().height);

        //---- label2 ----
        label2.setText("\u9875\u6570");
        label2.setHorizontalAlignment(SwingConstants.CENTER);
        add(label2);
        label2.setBounds(136, 31, 253, 17);

        {
            // compute preferred size
            Dimension preferredSize = new Dimension();
            for(int i = 0; i < getComponentCount(); i++) {
                Rectangle bounds = getComponent(i).getBounds();
                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
            }
            Insets insets = getInsets();
            preferredSize.width += insets.right;
            preferredSize.height += insets.bottom;
            setMinimumSize(preferredSize);
            setPreferredSize(preferredSize);
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JTextField textField1;
    private JButton button1;
    private JButton button2;
    private JLabel label1;
    private JLabel label2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    JLabel label1()
    {
        return label1;
    }

    JLabel label2()
    {
        return label2;
    }

}
