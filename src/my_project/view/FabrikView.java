package my_project.view;

import my_project.control.DatenbankController;
import my_project.control.ProgramController;

import javax.swing.*;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.event.*;
import java.util.Enumeration;

public class FabrikView extends View {
    private JScrollBar scrollBar1;
    private JTextField fabrikenTextField;
    private JButton lieferwegeButton;
    private JButton städteButton1;
    private JButton lagerButton;
    private JButton produktionAnpassenButton;
    private JButton umsatzVerlaufButton;
    private JPanel panel;
    private JScrollPane jScrolPAne;
    private JTable table1;
    private DatenbankController datenbankController;

    private int stadtNum,neuFabrikNum,routeNum;

    public FabrikView(DatenbankController datenbankController, ProgramController programController,int stadtNum,int neueFabrikNum,int routeNum){
        super(programController,true);
        this.neuFabrikNum=neueFabrikNum;
        this.routeNum=routeNum;
        this.datenbankController=datenbankController;
        this.stadtNum=stadtNum;
        jFrame.setContentPane(panel);
        jFrame.setVisible(false);
        jFrame.pack();
        table1.setModel(datenbankController.legeJTabelleAn("MG_Fabrik"));
        städteButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                datenbankController.getProgramController().szenenWechsel(stadtNum);
            }
        });
        lagerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                programController.szenenWechsel(neueFabrikNum);
            }
        });
        lieferwegeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                programController.szenenWechsel(routeNum);
            }
        });

        produktionAnpassenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table1.getSelectedColumnCount() == 1 && table1.getSelectedRowCount() == 1) {
                    int actRow = table1.getSelectedRow();
                    int actCol = table1.getSelectedColumn();
                    if (table1.getColumnName(actCol).equals("AktProduktion")) {
                        int neuerWert = Integer.parseInt(table1.getModel().getValueAt(actRow, actCol).toString());
                        System.out.println("neu " + neuerWert);
                        int alterWert = datenbankController.getValue(table1.getColumnName(actCol), actRow);
                        if (neuerWert > alterWert) {
                            int kosten = programController.getCostsForProduktivity(neuerWert - alterWert);
                            if (JOptionPane.showConfirmDialog(null, "Die Änderung kostet : " + kosten, "Sicher ?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == 0) {
                                if(programController.pay(kosten)) {
                                    datenbankController.changeValue(table1.getColumnName(actCol), neuerWert, actRow);
                                }else{
                                    JOptionPane.showMessageDialog(null,"Nicht genug Geld :(");
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Der neue Wert muss größer sein als der Alte");
                        }
                    }
                }
            }
        });
    }

    public void aktualisiere(){
        table1.setModel(datenbankController.legeJTabelleAn("MG_Fabrik"));
    }

    public JPanel getPanel() {
        return panel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public JFrame getJFrame() {
        return jFrame;
    }
}
