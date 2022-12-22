package anexe;

import java.time.format.DateTimeFormatter;


public class Constants {
    //" Class that contains defined stuff
    public static final DateTimeFormatter FORMATTER= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final String HELPME=  "Commands\n" +
                                        "print,users/friends -prints the requested repository\n" +
                                        "adduser,<username>,<password>,<firstname>,<lastname>\n"+
                                        "deluser,<username>-sterge user\n"+
                                        "addfriend,<user1>,<user2>-adauga prietenie intre user1 si user2\n"+
                                        "dellfriend,<user1>,<user2>-sterge prietenia\n"+
                                        "graf-afiseaza detalii despre network\n";


 /*   public static  String urlDb="jdbc:postgresql://localhost:5432/Frappe";
    public static  String usernameDb="postgres";
    public static  String passwdDb="postgres";*/

}
