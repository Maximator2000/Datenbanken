package my_project.view;

import javax.swing.*;
import javax.swing.table.TableColumn;

public class FabrikView {
    private JScrollBar scrollBar1;
    private JTextField textField1;
    private JTable table1;
    private JButton lieferwegeButton;
    private JButton städteButton1;
    private JButton lagerButton;
    private JButton produktionAnpassenButton;
    private JButton umsatzVerlaufButton;
    private JPanel panel;
    private JFrame jFrame;

    public FabrikView(){
        jFrame=new JFrame();
        jFrame.setContentPane(panel);
        jFrame.setVisible(false);
        jFrame.pack();
    }

    public JPanel getPanel() {
        return panel;
    }
}
