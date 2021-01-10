package my_project.control;

import KAGO_framework.control.ViewController;
import KAGO_framework.model.abitur.datenstrukturen.Stack;
import my_project.view.*;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.WindowListener;

/**
 * Ein Objekt der Klasse ProgramController dient dazu das Programm zu steuern. Die updateProgram - Methode wird
 * mit jeder Frame im laufenden Programm aufgerufen.
 */
public class ProgramController {

    //Attribute


    // Referenzen
    private ViewController viewController;  // diese Referenz soll auf ein Objekt der Klasse viewController zeigen. Über dieses Objekt wird das Fenster gesteuert.
    private DatenbankController datenbankController;
    private WindowListener windowListener;
    private BudgeController budgeController;
    private FabrikView fabrikView;
    private CityView cityView;
    private StartView startView;
    private FabrikNeuView fabrikNeuView;
    private LinienNeuView linienNeuView;
    private RouteView routeView;
    private AnleitungsView anleitungsView;
    private Stack<Integer> scenenStack;
    private View[] views;
    private static final int START_NUM=0;
    private static final int FABRIK_NUM=1;
    private static final int CITY_NUM=2;
    private static final int NEUEFABRIK_NUM=3;
    private static final int ROUTE_NUM=4;
    public static final int NEUELINIE_NUM=5;
    public static final int ANLEITUNG_NUM=6;
    private static final int BACK_NUM=7;
    /**
     * Konstruktor
     * Dieser legt das Objekt der Klasse ProgramController an, das den Programmfluss steuert.
     * Damit der ProgramController auf das Fenster zugreifen kann, benötigt er eine Referenz auf das Objekt
     * der Klasse viewController. Diese wird als Parameter übergeben.
     * @param viewController das viewController-Objekt des Programms
     */
    public ProgramController(ViewController viewController){
        this.viewController = viewController;
        datenbankController=new DatenbankController(this);
        budgeController=new BudgeController(datenbankController);
        startView =new StartView(datenbankController,this,FABRIK_NUM,ANLEITUNG_NUM);
        fabrikView=new FabrikView(datenbankController,this,CITY_NUM,NEUEFABRIK_NUM,ROUTE_NUM);
        fabrikNeuView=new FabrikNeuView(datenbankController,this,true);
        cityView=new CityView(datenbankController,this);
        anleitungsView=new AnleitungsView(this,true);
        routeView=new RouteView(this,datenbankController,true,NEUELINIE_NUM);
        linienNeuView=new LinienNeuView(this,datenbankController,true);
        views =new View[]{startView,fabrikView,cityView,fabrikNeuView,routeView,linienNeuView,anleitungsView};
        scenenStack=new Stack<>();
        scenenStack.push(START_NUM);

    }

    /**
     * Diese Methode wird genau ein mal nach Programmstart aufgerufen. Achtung: funktioniert nicht im Szenario-Modus
     */
    public void startProgram() {
        //Hier wird eine lokale Referenz für ein House-Objekt angelegt.
        //House firstHouse = new House();

        //Damit die draw-Methode des Objekts hinter firstHouse aufgerufen wird,
        //muss dem ViewController-Objekt mitgeteilt werden, dass es das House-Objekt zeichnen soll.
        //viewController.draw(firstHouse);
        viewController.setDrawFrameVisible(false);
        //viewController.getDrawFrame().setContentPane(new FabrikView(datenbankController).getPanel());
    }

    /**
     * Sorgt dafür, dass zunächst gewartet wird, damit der SoundController die
     * Initialisierung abschließen kann. Die Wartezeit ist fest und damit nicht ganz sauber
     * implementiert, aber dafür funktioniert das Programm auch bei falscher Java-Version
     * @param dt Zeit seit letzter Frame
     */
    public void updateProgram(double dt){

    }


    /**
     * Verarbeitet einen Mausklick.
     * @param e das Objekt enthält alle Informationen zum Klick
     */
    public void mouseClicked(MouseEvent e){

    }

    public int berechneWert(JTextArea textArea,JTextField textField,String ziel,String auswahl){
        String[] tmp=auswahl.split(",");
        linienNeuView.setZiel(tmp[0]);
        textArea.setText(textArea.getText()+"-->"+tmp[0]);
        if(!textField.getText().equals("")) {
            int temp=Integer.parseInt(tmp[1]) + Integer.parseInt(textField.getText());
            textField.setText(temp+"");
        }else{
            textField.setText(tmp[1]);
        }
        return Integer.parseInt(tmp[3]);
    }

    public void aktualisiere(){
        fabrikNeuView.aktualisiere();
        fabrikView.aktualisiere();
    }

    public void szenenWechsel(int next) {
        views[scenenStack.top()].getJFrame().setVisible(false);
        if (next == BACK_NUM) {
            scenenStack.pop();
            if(!scenenStack.isEmpty()) {
                views[scenenStack.top()].aktualisiere();
                views[scenenStack.top()].getJFrame().setVisible(true);
            }
        } else {
            views[next].aktualisiere();
            views[next].getJFrame().setVisible(true);
            scenenStack.push(next);
        }
    }
    public void szenenWechsel(){
        szenenWechsel(BACK_NUM);
    }
    public void neustart(){
        datenbankController.erstelleTabellen();
        while(scenenStack.top()!=START_NUM){
            szenenWechsel();
        }
        JOptionPane.showMessageDialog(null,"Du bist Bankrott gegangen:( Es gibt für alles ein erstes Mal ...");
    }

    public boolean pay(int sum){
        return budgeController.pay(sum);
    }
    public void startTimer(){
        budgeController.startTimer();
    }
    public int getHighestProd(){
        return budgeController.highestProd();
    }
    public int getBudge(){
        return budgeController.getBudge();
    }
    public int getCostsForProduktivity(int productivity){
        return budgeController.costForProductivity(productivity);
    }
    public void setAltePop(int pop){
        budgeController.setAlteBev(pop);
    }
    public void preisErhöhung(int newPop){
        budgeController.raisePreis(newPop);
        JOptionPane.showMessageDialog(null,"Der neue Preis beträgt : "+budgeController.getPreis());
    }
    public double getPreis(){
        return budgeController.getPreis();
    }

}
