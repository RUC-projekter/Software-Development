public class ShipData {
    private Integer id;
    private String name;
    private String from;
    private String to;
    private Integer containers;
    private Integer capacity;

    public ShipData(Integer id, String name, String from, String to, Integer containers, Integer capacity) {
        this.id = id;
        this.name = name;
        this.from = from;
        this.to = to;
        this.containers = containers;
        this.capacity = capacity;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Integer getContainers() {
        return containers;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setShipName(String name) {
        this.name = name;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setContainers(Integer containers) {
        this.containers = containers;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
