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
    }

    private void erstelleTabellen(){

        // Man kann eine temporäre Tabelle erstellen : Entweder bei SQL-Servern # oder CREATE TEMP <Name>
        String sql="IF NOT EXIST( SELECT 1\n" +
                "FROM db855656x2810214.TABLES\n" +
                "WHERE TABLE_NAME='MG_Fabrik')\n" +
                "CREATE TABLE MG_Stadt\n"+
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
        databaseController.executeStatement(sql);
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
                sql = "SELECT LW.Ziel\n" +
                        "FROM MG_Lieferungsweg\n" +
                        "WHERE ID=" + id + "\n" +
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
