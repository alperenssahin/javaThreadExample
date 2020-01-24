package pgdp.space;

public class Spaceuin extends Thread {

    // TODO
    Beacon start;
    Beacon destination;
    FlightRecorder flightRecorder;
    Beacon current;
    public Spaceuin(Beacon start, Beacon destination, FlightRecorder flightRecorder) {
        // TODO
        this.start = start;
        this.current = start;
        this.destination = destination;
        this.flightRecorder = flightRecorder;
        boolean done = false;
        while (!done){
            BeaconConnection bc = getNextWay();
            if(bc.beacon().equals(current)){
                continue;
            }
            flightRecorder.recordDeparture(current);
            current = bc.beacon();
            flightRecorder.recordArrival(current);
            if(current.equals(destination)){
                flightRecorder.tellStory();
                done = true;
            }
//            if(bc.type() == ConnectionType.NORMAL){
//
//            }
//            if(bc.type() == ConnectionType.WORMHOLE){
//
//            }

        }
    }
    public BeaconConnection getNextWay(){
        int limit = current.connections().size();
        int random  = (int) (Math.random() * 100);
        random = random%limit;
        return current.connections().get(random);
    }
    // TODO

    @Override
    public String toString() {
        // changing that might be useful for testing
        return super.toString();
    }
}