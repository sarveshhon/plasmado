package in.plasmado.helper;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UrlHelper {

    public static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public static String BASE_URL = "https://webhooks.mongodb-realm.com/api/client/v2.0/app/plasmado-mixxf/service/plasmado-api/incoming_webhook/";
    public static String BASE_KEY = "?secret=helpinghand";

    public static String REGISTRATION = "registration";
    public static String LOGIN = "login";

}
