package my_project.view;

import javax.swing.*;

public class CityView {
    private JList list1;
    private JScrollBar scrollBar1;
    private JTextField cityField;
    private JPanel jPane;
    private JFrame jFrame;

    public CityView(){
        jFrame=new JFrame();
        jFrame.setContentPane(jPane);
        jFrame.pack();
        jFrame.setVisible(false);
    }

    public JPanel getjPane() {
        return jPane;
    }
}
