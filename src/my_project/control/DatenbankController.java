package my_project.control;

import KAGO_framework.control.DatabaseController;
import KAGO_framework.model.abitur.datenbanken.mysql.QueryResult;

import javax.swing.*;
import javax.swing.table.TableColumn;

public class DatenbankController {
    //TODO   Datenbanken anlegen und darstellen
    private DatabaseController databaseController;

    public DatenbankController(){
        databaseController=new DatabaseController();
        if(databaseController.connect()) {
            erstelleTabellen();
        }
    }

    private void erstelleTabellen(){

        // Man kann eine temporäre Tabelle erstellen : Entweder bei SQL-Servern # oder CREATE TEMP <Name>
        //alte Tabellen finden und löschen
        String sql="SHOW TABLES LIKE'MG_%'";
        ausführen(sql,"");
        if(databaseController.getCurrentQueryResult()!=null && databaseController.getCurrentQueryResult().getData().length>0) {
            String[][] s = databaseController.getCurrentQueryResult().getData();
            for (int i = 0; i < s.length; i++) {
                if(s[i][0].equals("MG_Fabrik")){
                    sql="ALTER TABLE MG_Fabrik DROP FOREIGN KEY fkf;";
                    ausführen(sql,"Fremdschlüssel von Fabrik entfernt");
                }else if(s[i][0].equals("MG_Straße")){
                    sql="ALTER TABLE MG_Straße DROP FOREIGN KEY fkst;";
                    ausführen(sql,"Fremdschlüssel von Straße entfernt");
                    sql="ALTER TABLE MG_Straße DROP FOREIGN KEY fkst2;";
                    ausführen(sql,"Fremdschlüssel von Straße entfernt");
                }else if(s[i][0].equals("MG_Route")){
                    sql="ALTER TABLE MG_Route DROP FOREIGN KEY fkr;";
                    ausführen(sql,"Fremdschlüssel von Route entfernt");
                    sql="ALTER TABLE MG_Route DROP FOREIGN KEY fkr2;";
                    ausführen(sql,"Fremdschlüssel von Route entfernt");
                }
            }
            for (int i = 0; i < s.length; i++) {
                sql = "DROP TABLE " + s[i][0];
                databaseController.executeStatement(sql);
                ausführen(sql,"Tabelle " + s[i][0] + " wurde gelöscht");
            }

        }
        //neue Tabellen anlegen
        sql="CREATE TABLE MG_Fabrik(\n" +
                "ID INTEGER NOT NULL,\n" +
                "Name VARCHAR (40) NOT NULL,\n" +
                "AktProduktion INTEGER,\n" +
                "MaxProduktion INTEGER,\n" +
                "KostenProStk INTEGER,\n" +
                "Stadt VARCHAR (30),\n" +
                "PRIMARY KEY (ID) )\n";
       ausführen(sql,"MG_Fabrik wurde neu erstellt");
        sql="CREATE TABLE MG_Stadt(\n" +
                "Name VARCHAR (30) NOT NULL,\n" +
                "Bevölkerung INTEGER,\n" +
                "Lagerkapazität INTEGER,\n" +
                "Angebot INTEGER,\n" +
                "Nachfrage INTEGER,\n" +
                "PRIMARY KEY (Name))\n";
        ausführen(sql,"MG_Stadt wurde neu erstellt");
         sql="CREATE TABLE MG_Straße(\n" +
                "ID INTEGER NOT NULL,\n" +
                "Kosten INTEGER,\n" +
                "Dauer INTEGER,\n" +
                "Art VARCHAR (8),\n" +
                "Start VARCHAR(30),\n" +
                "Ziel VARCHAR(30),\n" +
                "PRIMARY KEY (ID))\n";
        ausführen(sql,"MG_Straße wurde neu erstellt");
        sql= "CREATE TABLE MG_Route(\n" +
                "ID INTEGER NOT NULL,\n" +
                "Stop INTEGER," +
                "Straße INTEGER,\n" +
                "Fabrik INTEGER,\n" +
                "PRIMARY KEY (ID))";
        ausführen(sql,"MG_Route wurde neu erstellt");
        //Fremdschlüssel ergänzen
        sql="ALTER TABLE MG_Fabrik\n" +
                "ADD CONSTRAINT fkf FOREIGN KEY(Stadt) REFERENCES MG_Stadt(Name)";
        ausführen(sql,"1:n Beziehug zwischen Fabrik und Stadt erzeugt");
        sql="ALTER TABLE MG_Straße\n" +
                "ADD CONSTRAINT fkst FOREIGN KEY (Start) REFERENCES MG_Stadt(Name)";
        ausführen(sql,"1:n Beziehung zwischen Straße und Stadt erzeugt");
        sql="ALTER TABLE MG_Straße\n" +
                "ADD CONSTRAINT fkst2 FOREIGN KEY (Ziel) REFERENCES MG_Stadt(Name)";
        ausführen(sql,"2:n Beziehung zwischen Straße und Stadt erzeugt");
        sql="ALTER TABLE MG_Route\n" +
                "ADD CONSTRAINT fkr FOREIGN KEY (Straße) REFERENCES MG_Straße(ID)";
        ausführen(sql,"1:n Beziehung zwischen Straße und Route erzeugt");
        sql="ALTER TABLE MG_Route\n" +
                "ADD CONSTRAINT fkr2 FOREIGN KEY (Fabrik) REFERENCES MG_Fabrik(ID)";
        ausführen(sql,"1:n Beziehung zwischen Fabrik und Route erzeugt");
        /*EXISTS( SELECT 1\n" +
                "FROM INFORMATION_SCHEMA.TABLES\n" +
                        "WHERE TABLE_NAME='MG_Fabrik')*/
                /*"CREATE TABLE MG_Stadt\n"+
                "Name VARCHAR(40)\n"+
                "CREATE TABLE MG_Fabrik(\n" +
                "ID INTEGER NOT NULL\n"+
                "Name VARCHAR(40) NOT NULL\n"+
                "AktProduktion INTEGER\n"+
                "MaxProduktion INTEGER\n"+
                "KostenProStk INTEGER\n"+
                "Stadt VARCHAR(40)\n"+
                "PRIMARY KEY(ID)";
        /*String sql="CREATE TABLE Fabrik(\n" +
                "ID VARCHAR(3) NOT NULL;\n" +
                "Name VARCHAR(40) NOT NULL\n" +
                "aktProduktion\n"+
                "PRIMARY KEY(ID)";*/

    }

    private void ausführen(String anweisung,String erfolgsmeldung){
        databaseController.executeStatement(anweisung);
        if(databaseController.getErrorMessage()!=null){
            System.out.println(databaseController.getErrorMessage());
        }else{
            System.out.println(erfolgsmeldung);
        }
    }

    public JTable legeJTabelleAn(String name){
        String sql="SELECT *\n" +
                "FROM "+name;
        databaseController.executeStatement(sql);
        if(databaseController.getErrorMessage()==null){
            QueryResult qR =databaseController.getCurrentQueryResult();
            return new JTable(qR.getData(),qR.getColumnNames());
        }else{
            System.out.println(databaseController.getErrorMessage());
            return null;
        }
    }

    public String zeigeLinien(String FabrikID,String standort){
        String res="";
        String sql="SELECT DISTINCT ID\n" +
                "FROM MG_Lieferungsweg\n" +
                "WHERE FabrikID="+FabrikID+"\n";
        databaseController.executeStatement(sql);
        if(databaseController.getErrorMessage()==null){
            String[][] ids=databaseController.getCurrentQueryResult().getData();
            for(String id:ids[0]) {
                sql = "SELECT V.Ziel\n" +
                        "FROM MG_Lieferungsweg LW JOIN MG_Verbindung V ON LW.verbindungsID=B.ID\n" +
                        "WHERE LW.ID=" + id + "\n" +
                        "ORDER BY Stop";
                databaseController.executeStatement(sql);
                if(databaseController.getErrorMessage()==null){
                    res=res+"Die ID ist "+id+". Verlauf: "+standort;
                    String[][] ziele=databaseController.getCurrentQueryResult().getData();
                    for( String ziel:ziele[0]){
                        res=res+" -> "+ziel;
                    }
                    res=res+"\n";
                }else{
                    res=res+databaseController.getErrorMessage()+"\n";
                }
            }
        }else{
            res=databaseController.getErrorMessage();

        }
        return res;

    }



}
