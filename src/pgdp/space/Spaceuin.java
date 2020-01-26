package pgdp.space;

public class Spaceuin extends Thread {

    // TODO
    Beacon start;
    Beacon destination;
    FlightRecorder flightRecorder;
    Beacon current;
    int selectIndex = 0;
    public static boolean done;
    public Spaceuin(Beacon start, Beacon destination, FlightRecorder flightRecorder) {
        // TODO
        this.start = start;
        this.current = start;
        this.destination = destination;
        this.flightRecorder = flightRecorder;
        Space.radio.add(this);


    }

    public void run() {
        boolean done = false;
        synchronized (this.current) {
            while (!Spaceuin.done) {
                boolean forState = true;
                for( BeaconConnection bc :current.connections()){
//                    if (bc.beacon().equals(current)) {
//                        if (selectIndex == current.connections().size()) {
//                            //there is no way
//                        }
//                        continue;
//                    }
                    if (bc.beacon().equals(destination)) {
                        this.flightRecorder.recordArrival(bc.beacon());
                        this.flightRecorder.tellStory();
                        Space.radio.remove(this);
                        forState =false;
                        break;
                    } else {
                        if (bc.type() == ConnectionType.WORMHOLE) {
                            FlightRecorder flightRecorderCopy = this.flightRecorder.createCopy();
                            Spaceuin nt = new Spaceuin(bc.beacon(), this.destination, flightRecorderCopy);
                            nt.start();
                        }
                        else if (bc.type() == ConnectionType.NORMAL) {
                            this.flightRecorder.recordArrival(current);
                            this.flightRecorder.recordDeparture(current);
                            current = bc.beacon();
                        }
                    }
                }
                if(!forState){
                    Spaceuin.done=true;
                }
            }
        }
    }

    public boolean isAnyPenguInBeacon(Beacon target) {
        for (Spaceuin s : Space.radio) {
            if (s.current.equals(target)) {
                return true;
            }
        }
        return false;
    }

    public BeaconConnection getNextWay() {
        for (BeaconConnection bc : current.connections()) {
            if (bc.beacon().equals(destination)) {
                return bc;
            }
        }
        return current.connections().get(selectIndex);
    }
    // TODO

    @Override
    public String toString() {
        // changing that might be useful for testing
        return super.toString();
    }
}