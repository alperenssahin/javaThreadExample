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
        Space.radio.add(this);


    }
    public void run(){
        boolean done = false;
        synchronized (this.current){
            while (!done){
                BeaconConnection bc = getNextWay();
                if(bc.beacon().equals(current)){
                    selectIndex++;
                    if(selectIndex == current.connections().size()){
                        //there is no way
                    }
                    continue;
                }
                if(!isAnyPenguInBeacon(bc.beacon())){
                    this.flightRecorder.recordArrival(current);
                    current = bc.beacon();
                }else{
                    continue;
                }
                if(bc.type() == ConnectionType.WORMHOLE){
                    this.flightRecorder.recordDeparture(current);
                    FlightRecorder flightRecorderCopy = this.flightRecorder.createCopy();
                    Spaceuin nt = new Spaceuin(current,this.destination,flightRecorderCopy);
                    nt.start();
                    break;
                }
//                if(bc.type() == ConnectionType.NORMAL){
//                    this.flightRecorder.recordArrival(current);
//                }
                selectIndex = 0;
                if(current.equals(destination) ){
                    this.flightRecorder.tellStory();
                    Space.radio.remove(this);
                    done = true;
                }
            }
        }
    }
    public boolean isAnyPenguInBeacon(Beacon target){
        for(Spaceuin s : Space.radio){
            if(s.current.equals(target)){
                return true;
            }
        }
        return false;
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