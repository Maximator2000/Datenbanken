package my_project.view;

import my_project.control.DatenbankController;

import javax.swing.*;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.util.Enumeration;

public class FabrikView {
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
    private JFrame jFrame;
    private DatenbankController datenbankController;

    public FabrikView(DatenbankController datenbankController){
        this.datenbankController=datenbankController;
        jFrame=new JFrame();
        jFrame.setContentPane(panel);
        jFrame.setVisible(true);
        jFrame.pack();
    }

    public JPanel getPanel() {
        return panel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
