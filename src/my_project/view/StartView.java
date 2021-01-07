package my_project.view;

import my_project.control.DatenbankController;
import my_project.control.ProgramController;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartView extends View{
    private JTextField willkommenTextField;
    private JTextArea wähleDeinenErstenStandortTextArea;
    private JButton losGehtSButton;
    private JPanel panel;
    private JComboBox cityBox;
    private JSlider slider1;
    private JTextField textField1;
    private JTextField kostenField2;
    private JTextField textField2;
    private JTextField wieSollDeineErsteTextField;
    private JTextField nameField;

    private int fabrikNum;
    private DatenbankController datenbankController;

    public StartView(DatenbankController datenbankController,ProgramController programController,int fabrikNum){
        super(programController,false);
        this.datenbankController=datenbankController;
        this.fabrikNum=fabrikNum;
        jFrame.setContentPane(panel);
        jFrame.pack();
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setSize(250,400);
        jFrame.setLocation(800,400);
        datenbankController.getTableRow(cityBox,"MG_Stadt","Name");
        slider1.setMaximum(datenbankController.getProgramController().getHighestProd());
        slider1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                kostenField2.setText("Dies kostet dich :"+datenbankController.getProgramController().getCostsForProduktivity(slider1.getValue())+" $");
            }
        });
        textField1.setText("Du hast "+datenbankController.getProgramController().getBudge()+" $");
        losGehtSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(programController.pay(programController.getCostsForProduktivity(slider1.getValue()))) {
                    datenbankController.addFabrik(nameField.getText(), slider1.getValue(), cityBox.getModel().getSelectedItem().toString());
                    datenbankController.getProgramController().szenenWechsel(fabrikNum);
                    programController.setAltePop(datenbankController.getPopulation(cityBox.getModel().getSelectedItem().toString()));
                    programController.startTimer();
                }
            }
        });
    }



    public JFrame getJFrame() {
        return jFrame;
    }

    @Override
    public void aktualisiere() {

    }
}
