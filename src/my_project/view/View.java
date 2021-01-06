package my_project.view;

import my_project.control.ProgramController;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public abstract class View {

    private boolean aktualisierbar;
    protected ProgramController programController;
    protected JFrame jFrame;

    public View(ProgramController programController,boolean aktualisierbar){
        this.aktualisierbar=aktualisierbar;
        this.programController=programController;
        jFrame=new JFrame();
        jFrame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                programController.szenenWechsel();
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
    }

    public abstract JFrame getJFrame();

    public boolean isAktualisierbar(){
        return aktualisierbar;
    }
    public abstract void aktualisiere();




}
