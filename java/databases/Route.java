package databases;



/**
 * Created by gabo on 1/26/15.
 */
public class Route {
    private long id;
    private int availability;
    private String date;
    private Double cost;
    private int type; // 1 == Entrada, 2 == Salida
    private long car_id;
    private long user_id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public int getType() {
        return type;
    }

    /**
     * Especifica si la ruta entra o sale de la ESPOL
     * @param type 1 si entra, 2 si sale
     */
    public void setType(int type) {
        this.type = type;
    }

    public long getCar_id() {
        return car_id;
    }

    public void setCar_id(long car_id) {
        this.car_id = car_id;
    }

    public long getUser_id() { return user_id; }

    public void setUser_id(long user_id) { this.user_id = user_id; }
}
