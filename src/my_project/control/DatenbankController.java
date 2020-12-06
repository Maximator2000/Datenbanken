package my_project.control;

import KAGO_framework.control.DatabaseController;

public class DatenbankController {
    //TODO   Datenbanken anlegen und darstellen
    private DatabaseController databaseController;

    public DatenbankController(){
        databaseController=new DatabaseController();
    }

    private void erstelleTabellen(){
        String sql="CREATE TABLE Fabrik(\n" +
                "ID VARCHAR(3) NOT NULL;\n" +
                "Name VARCHAR(40) NOT NULL\n" +
                "PRIMARY KEY(ID)";
        databaseController.executeStatement(sql);
    }

}
