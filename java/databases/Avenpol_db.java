package databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by gabo on 1/26/15.
 */
public class Avenpol_db extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "Avenpol_DB";
    private static final int DATABASE_VERSION = 1;
    SQLiteDatabase database;

    public Avenpol_db(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public void openDb(){
        database = this.getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserDataSource.createTableQuery());
        db.execSQL(CarDataSource.createTableQuery());
        db.execSQL(RouteDataSource.createTableQuery());
        db.execSQL(CommentDataSource.createTableQuery());
        db.execSQL(PointDataSource.createTableQuery());
        db.execSQL(RequestDataSource.createTableQuery());

    }

    public void populateDB(){
        UserDataSource userDataSource = new UserDataSource(database);
        userDataSource.createUser("Gabriel", "gafalcon@espol.edu.ec", "12345678");
        User user = userDataSource.createUser("Kenny", "kenny@espol.edu.ec", "123345456");
        System.out.println("User id = "+user.getId()+" Nombre: "+user.getName());
        CarDataSource carDataSource = new CarDataSource(database);
        Car car = carDataSource.createCar("Renault", "Duster", "GIF-221","Gris",1);
        System.out.println("Car id = "+car.getId()+ " marca="+car.getBrand());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public SQLiteDatabase getDatabase() { return  database; }
}
