package my_project.view;

import my_project.control.DatenbankController;
import my_project.control.ProgramController;

import javax.print.DocFlavor;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FabrikNeuView extends View{
    private JTextField erstelleHierEineNeueTextField;
    private JTextField wähleDenStandortAusTextField;
    private JComboBox comboBox1;
    private JTextField wähleDieAktiveProduktionTextField;
    private JSlider slider1;
    private JTextField infoField;
    private JButton erstellenButton;
    private JButton zurückButton;
    private JPanel panel;
    private JTextField wähleDenNamenAusTextField;
    private JTextField nameField;
    private JTextField steuerField;

    private DatenbankController datenbankController;

    public FabrikNeuView(DatenbankController datenbankController,ProgramController programController, boolean aktualisierbar) {
        super(programController, aktualisierbar);
        jFrame.setContentPane(panel);
        jFrame.pack();
        jFrame.setVisible(false);
        this.datenbankController=datenbankController;
        datenbankController.getTableRow(comboBox1,"MG_Stadt","Name");
        slider1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                infoField.setText("Deine Produktion beträgt "+slider1.getValue()+" pro Monat. Dies kostet dich "+programController.getCostsForProduktivity(slider1.getValue())+" $");
            }
        });
        slider1.setMaximum(programController.getHighestProd());
        zurückButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                programController.szenenWechsel();
            }
        });
        erstellenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(!nameField.getText().trim().equals("")){
                    if(programController.pay(programController.getCostsForProduktivity(slider1.getValue()))) {
                        datenbankController.addFabrik(nameField.getText(), slider1.getValue(), comboBox1.getModel().getSelectedItem().toString());
                        programController.preisErhöhung(datenbankController.getPopulations());
                        programController.szenenWechsel();
                    }
                }else{
                        JOptionPane.showMessageDialog(null,"Du musst einen Namen eingeben");
                }
            }
        });
        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                steuerField.setText(datenbankController.getTaxes(comboBox1.getModel().getSelectedItem().toString()));
            }
        });
    }


    @Override
    public JFrame getJFrame() {
        return jFrame;
    }

    @Override
    public void aktualisiere() {
        slider1.setMaximum(programController.getHighestProd());
        if (comboBox1.getSelectedItem() != null) {
            steuerField.setText(datenbankController.getTaxes(comboBox1.getModel().getSelectedItem().toString()));
        }
    }
}
