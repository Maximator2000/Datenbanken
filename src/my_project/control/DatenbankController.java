package my_project.control;

import KAGO_framework.control.DatabaseController;
import KAGO_framework.model.abitur.datenbanken.mysql.QueryResult;
import KAGO_framework.model.abitur.datenstrukturen.List;
import KAGO_framework.model.abitur.datenstrukturen.Queue;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class DatenbankController {
    //TODO   Datenbanken anlegen und darstellen
    private DatabaseController databaseController;
    private ProgramController programController;
    private int actFID;
    private int actRID;

    public DatenbankController(ProgramController programController){
        this.programController=programController;
        databaseController=new DatabaseController();
        if(databaseController.connect()) {
            erstelleTabellen();
        }
        actFID =0;
        actRID=0;
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
                    sql="ALTER TABLE MG_Route DROP FOREIGN KEY fkr3;";
                    ausführen(sql,"Fremdschlüssel von Route entfernt");
                }else if(s[i][0].equals("MG_Stadt")){
                    sql="ALTER TABLE MG_Stadt DROP FOREIGN KEY fks;";
                    ausführen(sql,"Fremdschlüssel von Stadt entfernt");
                }else if(s[i][0].equals("MG_Steuer")){
                    sql="ALTER TABLE MG_Steuer DROP FOREIGN KEY fkb;";
                    ausführen(sql,"Fremdschlüssel von Steuer entfernt");
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
                "Stadt VARCHAR (30),\n" +
                "PRIMARY KEY (ID) )\n";
       ausführen(sql,"MG_Fabrik wurde neu erstellt");
        sql="CREATE TABLE MG_Stadt(\n" +
                "Name VARCHAR (30) NOT NULL,\n" +
                "Bevölkerung INTEGER,\n" +
                "Steuer INTEGER,\n" +
                "PRIMARY KEY (Name))\n";
        /*"Lagerkapazität INTEGER,\n" +
                "Angebot INTEGER,\n" +
                "Nachfrage INTEGER,\n" +*/
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
                "Stop INTEGER,\n" +
                "Stadt VARCHAR (30),\n"+
                "Straße INTEGER,\n" +
                "Fabrik INTEGER,\n" +
                "PRIMARY KEY (Straße,Fabrik,Stadt))";
        ausführen(sql,"MG_Route wurde neu erstellt");
        sql="CREATE TABLE MG_Steuer(\n" +
                "ID INTEGER,\n" +
                "Art VARCHAR(20),\n" +
                "Kosten INTEGER,\n" +
                "Stadt VARCHAR(30) NOT NULL," +
                "PRIMARY KEY(ID))";
        ausführen(sql,"MG_Steuer wurde neu erstellt");
        //Fremdschlüssel ergänzen
        sql="ALTER TABLE MG_Fabrik\n" +
                "ADD CONSTRAINT fkf FOREIGN KEY(Stadt) REFERENCES MG_Stadt(Name)";
        ausführen(sql,"1:n Beziehug zwischen Fabrik und Stadt erzeugt");
        sql="ALTER TABLE MG_Straße\n" +
                "ADD CONSTRAINT fkst FOREIGN KEY (Start) REFERENCES MG_Stadt(Name)";
        ausführen(sql,"2:n Beziehung zwischen Straße und Stadt erzeugt");
        sql="ALTER TABLE MG_Straße\n" +
                "ADD CONSTRAINT fkst2 FOREIGN KEY (Ziel) REFERENCES MG_Stadt(Name)";
        ausführen(sql,"2:n Beziehung zwischen Straße und Stadt erzeugt");
        sql="ALTER TABLE MG_Route\n" +
                "ADD CONSTRAINT fkr FOREIGN KEY (Straße) REFERENCES MG_Straße(ID)";
        ausführen(sql,"1:n Beziehung zwischen Straße und Route erzeugt");
        sql="ALTER TABLE MG_Route\n" +
                "ADD CONSTRAINT fkr2 FOREIGN KEY (Fabrik) REFERENCES MG_Fabrik(ID)";
        ausführen(sql,"1:n Beziehung zwischen Fabrik und Route erzeugt");
        sql="ALTER TABLE MG_Route\n" +
                "ADD CONSTRAINT fkr3 FOREIGN KEY (Stadt) REFERENCES MG_Stadt(Name)";
        ausführen(sql,"1:n Beziehung zwischen Stadt und Route erzeugt");
        String[] steuern=new String[]{"Bau","Immer","Nichts"};
        int count=0;
        int gesamt=0;
        String[] städte=new String[]{"Joshuandria","Lisa de Janeiro","Maxinopel","Renedig","Knebopolis","AmbroCity","St. Ibosburg","San Marcisco"};
        for(String stadt:städte){
            int idS=(int)(Math.random()*3);
            int pop=(int)(100000+Math.random()*(9900000));
            int kosten=(int)(Math.random()*200+50);
            sql="INSERT INTO MG_Stadt (Name,Bevölkerung,Steuer)\n" +
                    "VALUES ('"+stadt+"',"+pop+","+count+")";
            ausführen(sql,stadt+" wurde als Stadt hinzugefügt");
            sql="INSERT INTO MG_Steuer (ID,Art,Kosten,Stadt)\n" +
                    "VALUES ("+count+",'"+steuern[idS]+"',"+kosten+",'"+stadt+"')";
            ausführen(sql,count+" wurde als Steuer hinzugefügt");
            count++;
            gesamt+=kosten;
        }
        System.out.println("!!!!!!!!!!!!!!!!!!"+gesamt+"!!!!!!!!!!!!!!!!!");
        sql="ALTER TABLE MG_Stadt\n" +
                "ADD CONSTRAINT fks FOREIGN KEY (Steuer) REFERENCES MG_Steuer(ID)";
        ausführen(sql,"1:1 Beziehung zwischen Steuer und Stadt erzeugt");
        sql="ALTER TABLE MG_Steuer\n" +
                "ADD CONSTRAINT fkb FOREIGN KEY (Stadt) REFERENCES MG_Stadt(Name)";
        ausführen(sql,"1:1 Beziehung zwischen Stadt und Steuer erzeugt");
       //Tabelle füllen:

        //ausführen(sql,"London unter Städte hinzugefügt");

        for(int i=0;i<16;i++){
            double r=Math.random();
            String art="";
            if(r>0.66){
                art="LKW";
            }else if(r>0.33){
                art="Flugzeug";
            }else{
                art="Schiff";
            }
            int z=0;
            if(i<8){
                z=(i+2)%8;
            }else{
                z=gibZahlAußer(i%8,7);
            }
            sql="INSERT INTO MG_Straße (ID,Kosten,Dauer,Art,Start,Ziel)\n" +
                    "VALUES("+i+","+(int)(1+Math.random()*9)+","+(int)(1+Math.random()*9)+",'"+art+"','"
        +städte[z]+"','"+städte[i%8]+"');";
            ausführen(sql,"Eine Straße von "+städte[z]+" bis "+städte[i%8]+" wurde erstellt");
        }

    }

    private int gibZahlAußer(int ausnahme, int höchste){
        int r=ausnahme;
        while(r==ausnahme){
            r=(int)(Math.random()*höchste+1);
        }
        return r;
    }

    private void ausführen(String anweisung,String erfolgsmeldung){
        databaseController.executeStatement(anweisung);
        if(databaseController.getErrorMessage()!=null){
            System.out.println(databaseController.getErrorMessage());
        }else{
            System.out.println(erfolgsmeldung);
        }
    }

    public DefaultTableModel legeJTabelleAn(String name){
        String sql="SELECT *\n" +
                "FROM "+name;
        databaseController.executeStatement(sql);
        if(databaseController.getErrorMessage()==null){
            QueryResult qR =databaseController.getCurrentQueryResult();
            String[] column=qR.getColumnNames();
            String[][] values=qR.getData();
            return new DefaultTableModel(qR.getData(),qR.getColumnNames());
            /*System.out.println("Daten von "+name+" übernommen");
            for(int i=0;i<table.getColumnCount();i++){
                table.addColumn(new TableColumn(i));
                table.getColumnModel().getColumn(i).setHeaderValue(column[i]);
            }
            for(int i=0;i<values.length;i++){
                for(int j=0;j<values.length;j++){
                    table.getModel().setValueAt(values[i][j],i,j);
                }
            }*/
        }else{
            System.out.println(databaseController.getErrorMessage());
            return null;
        }
    }
    public DefaultTableModel legeStadtTabelleAn(){
        String sql="SELECT MG_Stadt.Name AS Name, MG_Stadt.Bevölkerung AS Bevölkerung, MG_Steuer.Art AS Steuerbezeichnung, MG_Steuer.Kosten AS Kosten\n" +
                "FROM MG_Stadt JOIN MG_Steuer ON MG_Stadt.Steuer=MG_Steuer.ID";
        ausführen(sql,"Stadt Tabelle wird angelelgt");
        if(databaseController.getErrorMessage()==null) {
            QueryResult qR = databaseController.getCurrentQueryResult();
            System.out.println(qR.getColumnNames()[3]);
            return new DefaultTableModel(qR.getData(), qR.getColumnNames());
        }
        return null;
    }

    public String getTaxes(String stadt){
        String res="";
        String sql="SELECT Art, Kosten\n" +
                "FROM MG_Stadt st JOIN MG_Steuer t ON st.Steuer=t.ID\n" +
                "WHERE t.stadt='"+stadt+"'";
        ausführen(sql,"Steuerinformationen abgefragt");
        if(databaseController.getErrorMessage()==null) {
            QueryResult qR = databaseController.getCurrentQueryResult();
            res=qR.getData()[0][0]+" : "+qR.getData()[0][1];

        }
        return res;
    }

    public int getProduction(){
        String sql="SELECT SUM(AktProduktion)\n" +
                "FROM MG_Fabrik";
        ausführen(sql,"Gesamtproduktion erfahren");
        if(databaseController.getErrorMessage()==null && databaseController.getCurrentQueryResult().getData()[0][0]!=null){
            System.out.println(databaseController.getCurrentQueryResult().getData()[0][0]);
            return Integer.parseInt(databaseController.getCurrentQueryResult().getData()[0][0]);
        }
        return 0;
    }

    public int getSteuerSumme() {
        String sql = "SELECT SUM(Kosten)\n" +
                "FROM (MG_Steuer t JOIN MG_Stadt st ON t.Stadt=st.Name) LEFT JOIN MG_Fabrik f ON f.Stadt=st.Name \n" +
                "WHERE t.Art='Immer' OR (t.Art='Bau' AND f.Stadt IS NOT NULL)";
        ausführen(sql,"Steuern werden berechnet");
        if(databaseController.getErrorMessage()==null  && databaseController.getCurrentQueryResult().getData()[0][0]!=null) {
            QueryResult qR = databaseController.getCurrentQueryResult();
            return Integer.parseInt(qR.getData()[0][0]);
        }
        return 0;
    }
        public void getTableRow(JComboBox jComboBox,String tableName,String columnNames,String groupName,String joinName){
        jComboBox.removeAllItems();
        String sql="SELECT "+columnNames+"\n" +
                "FROM "+tableName;
        if(joinName!=null){
            sql=sql+" JOIN "+joinName+" ON "+joinName+".ID="+tableName+".Fabrik";
        }
        if(groupName!=null){
            sql=sql+"\n" +
                    "GROUP BY "+groupName;
        }
        ausführen(sql,"Attribute: "+columnNames+" aus "+tableName+" angefragt");
        if(databaseController.getErrorMessage()==null){
            QueryResult qR=databaseController.getCurrentQueryResult();
            for(int i=0;i<qR.getData().length;i++){
                System.out.println("ACHTUNG--->  "+arrayToString(qR.getData()[i]));
                jComboBox.addItem(arrayToString(qR.getData()[i]));
            }

        }
    }
    public void getTableRow(JComboBox jComboBox,String tableName,String columnNames){
        getTableRow(jComboBox,tableName,columnNames,null,null);
    }

    public String arrayToString(String[] strings){
        if(strings.length==1){
            return strings[0];
        }
        if(strings.length==0){
            return "";
        }
        String res="";
        for(String s:strings){
            res=res+s+",";
        }
        return res;
    }

    public void addFabrik(String name,int aktProduktion,String stadt){
        String sql="INSERT INTO MG_Fabrik(ID,Name,AktProduktion,MaxProduktion,Stadt)\n" +
                "Values("+ actFID +",'"+name+"',"+aktProduktion+",1000,'"+stadt+"')";
        ausführen(sql,"Die Fabrik "+name+" wurde erstellt");
        actFID++;
    }

    public void changeValue(String column,int newData,int id){
        String sql="UPDATE MG_Fabrik \n" +
                "SET "+column+"="+newData+"\n" +
                "WHERE ID="+id;
        ausführen(sql,"In Zeile "+(id+1)+"der Spalte "+column+" wurde "+newData+" eingefügt");
    }
    public int getValue(String column,int id){
        String sql="SELECT "+column+"\n" +
                "FROM MG_Fabrik\n" +
                "WHERE ID="+id;
        ausführen(sql,"Wert wurde aus MG_FAbrik abgelesen");
        if(databaseController.getErrorMessage()==null){
            return Integer.parseInt(databaseController.getCurrentQueryResult().getData()[0][0]);
        }
        return -1;
    }

    public int getPopulation(String name){
        String sql="SELECT Bevölkerung\n" +
                "FROM MG_Stadt\n" +
                "WHERE Name='"+name+"'";
        ausführen(sql,"Wert wurde aus MG_Stadt abgelesen");
        if(databaseController.getErrorMessage()==null){
            System.out.println("Bevölkerung "+Integer.parseInt(databaseController.getCurrentQueryResult().getData()[0][0]));
            return Integer.parseInt(databaseController.getCurrentQueryResult().getData()[0][0]);
        }
        return -1;
    }
    public int getPopulations(){
        String sql="SELECT DISTINCT s.Name\n" +
                "FROM MG_Route r JOIN MG_Stadt s ON r.Stadt=s.Name";
        ausführen(sql,"BEvölkerung ermittelt");
        List<String> stringList=new List<>();
        if(databaseController.getErrorMessage()==null){
            for(String[] sa:databaseController.getCurrentQueryResult().getData()){
                for(String s:sa){
                    stringList.append(s);
                }
            }
        }
        sql="SELECT DISTINCT st.Name\n" +
                "FROM MG_Fabrik f JOIN MG_Stadt st ON f.Stadt=st.Name\n";
        ausführen(sql,"Bevölkerung ermittelt2");
        if(databaseController.getErrorMessage()==null){
            for(String[] sa:databaseController.getCurrentQueryResult().getData()){
                for(String s:sa){
                    if(!stringInList(s,stringList)) {
                        stringList.append(s);
                    }
                }
            }
        }
        int res=0;
        stringList.toFirst();
        while(stringList.hasAccess()){
            System.out.println(getPopulation(stringList.getContent()));
            res+=getPopulation(stringList.getContent());
            stringList.next();
        }
        return res;
    }

    public boolean stringInList(String s,List<String> l){
        l.toFirst();
        while(l.hasAccess()){
            if(l.getContent().equals(s)){
                return true;
            }
            l.next();
        }
        return false;
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

    public String gibRoutenÜbersicht(String stadt,String fabrik){
        String sql="SELECT s.Start, r.Stop\n" +
                "FROM (MG_Fabrik f JOIN MG_Route r ON r.Fabrik=f.ID) JOIN MG_Straße s ON r.Straße=s.ID\n" +
                "WHERE r.Stadt='"+stadt+"' AND f.Name='"+fabrik+"'\n" +
                "ORDER BY Stop";
        ausführen(sql,"Routenübersicht zu "+stadt+" und "+fabrik+" konnte geladen werden");
        if(databaseController.getErrorMessage()==null){
            String res="";
            for(int i=0;i<databaseController.getCurrentQueryResult().getData().length;i++){
                res=res+databaseController.getCurrentQueryResult().getData()[i][0]+" --> ";
                if((i+1)%3==0){
                    res=res+"\n";
                }
            }
            res=res+stadt;
            return res;
        }
        return "";
    }

    public void addRoute(int fabrik,int[] straßen,String stadt){
        String sql="";
        int count=0;
        for(int straße:straßen){
            sql="INSERT INTO MG_Route(Stop,Stadt,Straße,Fabrik)\n" +
                    "VALUES("+count+",'"+stadt+"',"+straße+","+fabrik+")";
            ausführen(sql,"Route nach "+stadt+" von Fabrik"+fabrik+" aus erweitert");
            count++;
        }
        actRID++;
    }

    public int gibMenge(){
        String sql="SELECT SUM(AktProduktion)\n" +
                "FROM MG_Fabrik";
        ausführen(sql,"Gesamtproduktion abgefragt");
        if(databaseController.getErrorMessage()==null){
            return Integer.parseInt(databaseController.getCurrentQueryResult().getData()[0][0]);
        }
        return 0;
    }
    public int gibLieferkosten(){
        //gehe alle Routen einer FAbrik durch berechne die Transportmenge/pro Route und alle Kosten über die Straßen
        String sql="SELECT SUM(s.Kosten)*f.AktProduktion/COUNT(DISTINCT r.Stadt)\n" +
                "FROM (MG_Route r JOIN MG_Straße s ON r.Straße=s.ID) JOIN MG_Fabrik f ON f.ID=r.Fabrik\n" +
                "GROUP BY r.Fabrik";
        ausführen(sql,"Lieferkosten wurden herausgesucht");
        if(databaseController.getErrorMessage()==null){
            int sum=0;
            for(int i=0;i<databaseController.getCurrentQueryResult().getData().length;i++){
                for(int j=0;j<databaseController.getCurrentQueryResult().getData()[i].length;j++){
                    String tmp=databaseController.getCurrentQueryResult().getData()[i][j];
                    String[] s=tmp.split("\\.");
                    if(s.length>0) {
                        sum += Integer.parseInt(s[0]);
                    }
                }
            }
            return sum;
        }
        return 0;
    }
    public void gibStraßenVarianten(JComboBox jComboBox,String last){
        jComboBox.removeAllItems();
        String sql="Select Ziel,Kosten,Art,ID\n" +
                "FROM MG_Straße";
        if(!last.equals("nichts")){
            sql=sql+"\n" +
                    "WHERE Start='"+last+"'";
        }
        ausführen(sql,"Straßen herausgesucht");
        if(databaseController.getErrorMessage()==null){
            QueryResult qR=databaseController.getCurrentQueryResult();
            for(int i=0;i<qR.getData().length;i++){
                jComboBox.addItem(arrayToString(qR.getData()[i]));
            }

        }
    }

    public void getFabriken(JComboBox jComboBox){
        jComboBox.removeAllItems();
        String sql="SELECT Name, AktProduktion, Stadt, ID\n" +
                "FROM MG_Fabrik\n";
        ausführen(sql,"Fabriken werden aufgelistet");
        if(databaseController.getErrorMessage()==null){
            for(String[] s:databaseController.getCurrentQueryResult().getData()){
                jComboBox.addItem(arrayToString(s));
            }
        }
    }

    public void gibStraßenVarianten(JComboBox jComboBox){
        gibStraßenVarianten(jComboBox,"nichts");
    }


    public ProgramController getProgramController() {
        return programController;
    }
}
