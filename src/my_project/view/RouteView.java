package my_project.view;

import my_project.control.DatenbankController;
import my_project.control.ProgramController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class RouteView extends View{
    private JTextField linienTextField;
    private JComboBox comboBox1;
    private JTextArea textArea1;
    private JButton erweiternButton;
    private JButton routeButton;
    private JButton zurückButton;
    private JPanel jPanel;

    private ProgramController programController;
    private DatenbankController datenbankController;
    private int neueLinie_Num;

    public RouteView(ProgramController programController, DatenbankController datenbankController, boolean aktualisierbar, int neueLinie_Num) {
        super(programController, aktualisierbar);
        this.neueLinie_Num=neueLinie_Num;
        this.datenbankController=datenbankController;
        jFrame.setVisible(false);
        jFrame.setContentPane(jPanel);
        jFrame.pack();
        datenbankController.getTableRow(comboBox1,"MG_Route","MG_Route.Stadt, MG_Fabrik.Name","Stadt, Fabrik","MG_Fabrik");
        comboBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(comboBox1.getItemCount()>0) {
                    String tmp = comboBox1.getModel().getSelectedItem().toString();
                    String[] strings = tmp.split(",");
                    textArea1.setText(datenbankController.gibRoutenÜbersicht(strings[0], strings[1]));
                }
            }
        });
        zurückButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                programController.szenenWechsel();
            }
        });
        routeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                programController.szenenWechsel(neueLinie_Num);
            }
        });
    }


    @Override
    public JFrame getJFrame() {
        return jFrame;
    }

    @Override
    public void aktualisiere() {
        datenbankController.getTableRow(comboBox1,"MG_Route","MG_Route.Stadt, MG_Fabrik.Name","Stadt, Fabrik","MG_Fabrik");
        textArea1.setText("");
    }
}
