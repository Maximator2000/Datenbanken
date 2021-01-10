package my_project.view;

import my_project.control.ProgramController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AnleitungsView extends View{
    private JPanel panel;
    private JButton zurückButton;

    private ProgramController programController;

    public AnleitungsView(ProgramController programController, boolean aktualisierbar) {
        super(programController, aktualisierbar);
        jFrame.setContentPane(panel);
        jFrame.setVisible(false);
        jFrame.setSize(400,600);
        jFrame.setLocation(400,400);
        jFrame.pack();


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

    }
}
