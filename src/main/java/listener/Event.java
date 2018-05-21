package listener;

public abstract class Event {

    private String world;

    public Event(String world) {
        this.world = world;
    }

    public String getWorld() {
        return world;
    }
}
