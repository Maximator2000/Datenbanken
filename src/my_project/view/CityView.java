package my_project.view;

import my_project.control.DatenbankController;
import my_project.control.ProgramController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CityView extends View{
    private JScrollBar scrollBar1;
    private JTextField cityField;
    private JPanel jPane;
    private JTable table1;
    private JButton zurückButton;
    private DatenbankController datenbankController;


    public CityView(DatenbankController datenbankController, ProgramController programController){
        super(programController,true);
        this.datenbankController=datenbankController;
        jFrame.setContentPane(jPane);
        jFrame.pack();
        jFrame.setVisible(false);
        table1.setModel(datenbankController.legeJTabelleAn("MG_Stadt"));
        zurückButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                datenbankController.getProgramController().szenenWechsel();
            }
        });
    }

    public JPanel getjPane() {
        return jPane;
    }

    public JFrame getJFrame() {
        return jFrame;
    }

    @Override
    public void aktualisiere() {
        table1.setModel(datenbankController.legeJTabelleAn("MG_Stadt"));
    }
}
