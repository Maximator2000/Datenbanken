package my_project.view;

import my_project.control.DatenbankController;

import javax.swing.*;
import javax.swing.table.TableColumn;

public class FabrikView {
    private JScrollBar scrollBar1;
    private JTextField fabrikenTextField;
    private JTable table1;
    private JButton lieferwegeButton;
    private JButton städteButton1;
    private JButton lagerButton;
    private JButton produktionAnpassenButton;
    private JButton umsatzVerlaufButton;
    private JPanel panel;
    private JFrame jFrame;
    private DatenbankController datenbankController;

    public FabrikView(DatenbankController datenbankController){
        this.datenbankController=datenbankController;
        jFrame=new JFrame();
        jFrame.setContentPane(panel);
        jFrame.setVisible(false);
        jFrame.pack();
    }

    public JPanel getPanel() {
        return panel;
    }
}
