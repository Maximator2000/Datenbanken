package my_project.view;

import KAGO_framework.model.abitur.datenstrukturen.Queue;
import my_project.control.DatenbankController;
import my_project.control.ProgramController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LinienNeuView extends View{
    private JTextField linienErstellenTextField;
    private JTextArea textArea1;
    private JTextField aTextField;
    private JComboBox straßenBox1;
    private JTextField fügeStraßenHinzuTextField;
    private JButton fertigButton;
    private JPanel panel;
    private JTextField legeEineStartfabrikfestTextField;
    private JComboBox fabrikBox2;
    private JButton festlegenButton;
    private JButton auswählenButton;
    private JTextField kostenTextField;
    private JButton zurückButton;

    private DatenbankController datenbankController;
    private int idCount;
    private Queue<Integer> ids;
    private String ziel;

    public LinienNeuView(ProgramController programController,DatenbankController datenbankController, boolean aktualisierbar) {
        super(programController, aktualisierbar);
        this.datenbankController=datenbankController;
        jFrame.setContentPane(panel);
        jFrame.setVisible(false);
        jFrame.pack();
        idCount=0;
        datenbankController.getFabriken(fabrikBox2);
        festlegenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ids=new Queue<>();
                idCount=0;
                textArea1.setText(fabrikBox2.getModel().getSelectedItem().toString().split(",")[2]);
                straßenBox1.setEnabled(true);
                auswählenButton.setEnabled(true);
                datenbankController.gibStraßenVarianten(straßenBox1,fabrikBox2.getModel().getSelectedItem().toString().split(",")[2]);
            }
        });
        auswählenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(straßenBox1.isEnabled()){
                    fertigButton.setEnabled(true);
                    idCount++;
                    ids.enqueue(programController.berechneWert(textArea1,kostenTextField,ziel,straßenBox1.getModel().getSelectedItem().toString()));
                    datenbankController.gibStraßenVarianten(straßenBox1,ziel);
                }
            }
        });
        fertigButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] idArray=new int[idCount];
                for(int i=0;i<idCount;i++){
                    idArray[i]=ids.front();
                    ids.dequeue();
                }
                datenbankController.addRoute(Integer.parseInt(fabrikBox2.getModel().getSelectedItem().toString().split(",")[3]),idArray,ziel);
                programController.preisErhöhung(datenbankController.getPopulations());
                programController.szenenWechsel();
            }
        });
        zurückButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                programController.szenenWechsel();
            }
        });
    }


    @Override
    public JFrame getJFrame() {
        return jFrame;
    }

    @Override
    public void aktualisiere() {
        datenbankController.getFabriken(fabrikBox2);
        ids=new Queue<>();
        idCount=0;
        kostenTextField.setText("");
        straßenBox1.setEnabled(false);
        fertigButton.setEnabled(false);
        auswählenButton.setEnabled(false);
        textArea1.setText("");
    }

    public void setZiel(String ziel) {
        this.ziel = ziel;
    }
}
