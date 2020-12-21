package my_project.view;

import my_project.control.DatenbankController;

import javax.swing.*;

public class CityView {
    private JScrollBar scrollBar1;
    private JTextField cityField;
    private JPanel jPane;
    private JTable table1;
    private JFrame jFrame;
    private DatenbankController datenbankController;

    public CityView(DatenbankController datenbankController){
        this.datenbankController=datenbankController;
        jFrame=new JFrame();
        jFrame.setContentPane(jPane);
        jFrame.pack();
        jFrame.setVisible(false);
    }

    public JPanel getjPane() {
        return jPane;
    }
}
