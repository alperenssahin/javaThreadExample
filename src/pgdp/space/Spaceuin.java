package pgdp.space;

public class Spaceuin extends Thread {

    // TODO
    Beacon start;
    Beacon destination;
    FlightRecorder flightRecorder;
    Beacon current;
    int selectIndex = 0;
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
                selectIndex++;
                if(selectIndex == current.connections().size()){
                    //there is no way
                }
                continue;
            }
            flightRecorder.recordDeparture(current);
            current = bc.beacon();
            if(bc.type() == ConnectionType.WORMHOLE){
                FlightRecorder flightRecorderCopy = this.flightRecorder.createCopy();
                Spaceuin nt = new Spaceuin(current,this.destination,flightRecorderCopy);
                nt.start();
                this.interrupt();
            }
            if(bc.type() == ConnectionType.NORMAL){
                selectIndex = 0;
                flightRecorder.recordArrival(current);
            }
            if(current.equals(destination)){
                flightRecorder.tellStory();
                done = true;
            }


        }
    }
    public BeaconConnection getNextWay(){
        for (BeaconConnection bc : current.connections()){
            if(bc.beacon().equals(destination)){
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