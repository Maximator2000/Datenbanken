package my_project.control;

import java.util.Timer;
import java.util.TimerTask;

public class BudgeController {

    private int budge;
    private double preis;
    private int kostenProLeistung;
    private int delay,period;
    private double alteBev;

    private Timer timer;
    private DatenbankController datenbankController;

    public BudgeController(DatenbankController datenbankController){
        this.datenbankController=datenbankController;
        preis=5;
        delay=1000;
        period=20000;
        budge=10000;
        kostenProLeistung=200;
        timer=new Timer();
        //1
    }

    public void startTimer(){
        System.out.println("Timer gestartet");
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                budge=budge+datenbankController.gibMenge()*(int)preis-datenbankController.gibLieferkosten();
                budge-=datenbankController.getSteuerSumme();
                datenbankController.getProgramController().aktualisiere();
                System.out.println("neues Budge: "+budge);
                if(budge<0){
                    bankrot();
                }
            }
        },delay,period);
    }



    public void raisePreis(double neueBev){
        System.out.println("!!!!!!!!!!!!!"+neueBev+" "+alteBev+" "+(neueBev/alteBev));
        preis*=neueBev/alteBev;
        alteBev=neueBev;

    }

    public void setAlteBev(double alteBev) {
        this.alteBev = alteBev;
    }

    public boolean pay(int sum){
      if(sum<=budge){
          budge-=sum;
          return true;
      }else{
          return false;
      }
    }

    public int highestProd(){
        return budge/kostenProLeistung;
    }

    public int costForProductivity(int productivity){
        return productivity*kostenProLeistung;
    }

    public int getBudge() {
        return budge;
    }

    public double getPreis() {
        return preis;
    }

    public void bankrot(){
        budge=10000;
        preis=5;
        datenbankController.getProgramController().neustart();
    }
}
