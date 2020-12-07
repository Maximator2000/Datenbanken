package my_project.control;

import KAGO_framework.control.DatabaseController;

import javax.swing.*;

public class DatenbankController {
    //TODO   Datenbanken anlegen und darstellen
    private DatabaseController databaseController;

    public DatenbankController(){
        databaseController=new DatabaseController();
    }

    private void erstelleTabellen(){
        String sql="IF NOT EXIST( SELECT 1\n" +
                "FROM db855656x2810214.TABLES\n" +
                "WHERE TABLE_NAME='MG_Fabrik')\n" +
                "CREATE TABLE MG_Fabrik(\n" +
                "ID VARCHAR(3) NOT NULL\n"+
                "Name VARCHAR(40) NOT NULL\n"+
                "PRIMARY KEY(ID)";
        databaseController.executeStatement(sql);
    }

    public void legeTabelleAn(JTable jTable){
        int i=jTable.getColumnCount();
        while (jTable.getColumnCount()>0){
            jTable.removeColumn(jTable.getColumn(i));
            i--;
        }

        String sql="SELECT MG_FABRIK *";
    }

}
