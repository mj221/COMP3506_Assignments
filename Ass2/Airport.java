import java.util.*;

public class Airport extends AirportBase {

    private List<Terminal> nodes;
    private List<Shuttle> edges;
    private Map<Terminal, Integer> time;

    /**
     * Creates a new AirportBase instance with the given capacity.
     *
     * @param capacity capacity of the airport shuttles
     *                 (same for all shuttles)
     */
    public Airport(int capacity) {
        super(capacity);
        nodes = new ArrayList<Terminal>();
        edges = new ArrayList<Shuttle>();
        time = new HashMap<>();
    }

    @Override
    public TerminalBase opposite(ShuttleBase shuttle, TerminalBase terminal) {
        if (shuttle.getOrigin().equals(terminal)){
            return shuttle.getDestination();
        }
        if (shuttle.getDestination().equals(terminal)){
            return shuttle.getOrigin();
        }
        return null;
    }

    @Override
    public TerminalBase insertTerminal(TerminalBase terminal) {

        Terminal newTerminal = (Terminal) terminal;
        nodes.add(newTerminal);
        return newTerminal;
    }

    @Override
    public ShuttleBase insertShuttle(TerminalBase origin, TerminalBase destination, int time) {
        Shuttle newShuttle = new Shuttle(origin, destination, time);
        edges.add(newShuttle);
        return newShuttle;
    }

    @Override
    public boolean removeTerminal(TerminalBase terminal) {
        if (!nodes.contains(terminal)){
            return false;
        }
        for (int i = 0; i < nodes.size(); i++){
            if (nodes.get(i).equals(terminal)) {
                nodes.remove(nodes.get(i));
            }
        }
        List<ShuttleBase> neighbors = outgoingShuttles(terminal);
        for (int k = 0; k < neighbors.size(); k++){
            removeShuttle(neighbors.get(k));
        }
        return true;
    }

    @Override
    public boolean removeShuttle(ShuttleBase shuttle) {
        if (!edges.contains((Shuttle) shuttle)){
            return false;
        }
        for (int i = 0; i < edges.size(); i++){
            if (edges.get(i).equals(shuttle)){
                edges.remove(i);
            }
        }
        return true;
    }

    @Override
    public List<ShuttleBase> outgoingShuttles(TerminalBase terminal) {
        List<ShuttleBase> neighbours = new ArrayList<>();
        for (int j = 0; j < edges.size(); j++){
            if (edges.get(j).getOrigin().equals(terminal)
                    || edges.get(j).getDestination().equals(terminal)) {
                neighbours.add(edges.get(j));
            }
        }
        return neighbours;
    }
    // Obtain all adjacent terminals to source terminal
    private List<Terminal> neighbourTerminals(TerminalBase terminal){
        List<ShuttleBase> adjacentShuttles = outgoingShuttles(terminal);
        List<Terminal> adjacentTerminals = new ArrayList<>();
        Terminal addTerminal;
        for (int i = 0; i < adjacentShuttles.size(); i++){
            addTerminal = (Terminal) opposite(adjacentShuttles.get(i), terminal);
            if (opposite(adjacentShuttles.get(i), terminal) != null){
                adjacentTerminals.add(addTerminal);
            }
        }
        return adjacentTerminals;
    }

    // Dijkstra's algorithm using lecture pseudo code
    @Override
    public Path findShortestPath(TerminalBase origin, TerminalBase destination) {
        PriorityQueue<Entry> queue = new PriorityQueue<>();
        Map<Terminal, Boolean> visited = new HashMap<>();
        time.put((Terminal) origin, 0);
        visited.put((Terminal) origin, true);
        for (int i = 0; i < nodes.size(); i++){
            if (nodes.get(i) != origin){
                time.put(nodes.get(i), Integer.MAX_VALUE);
                visited.put(nodes.get(i), false);
            }
            queue.add(new Entry(nodes.get(i), time.get(nodes.get(i))));
        }
        while(!queue.isEmpty()){
            Terminal v = queue.poll().key;
            List<ShuttleBase> incidentshuttles = outgoingShuttles(v);
            for (int i = 0; i < incidentshuttles.size(); i++){
                Terminal z = (Terminal) opposite(incidentshuttles.get(i), v);
                if (!visited.get(z)){
                    visited.replace(z, true);
                    int r = time.get(v) + v.getWaitingTime() + incidentshuttles.get(i).getTime();
                    time.put(z, r);
                    z.setPredecessor(v);
                    queue.add(new Entry(z, r));
                    if (z.equals(destination)){
                        if (time.get(destination).equals(Integer.MAX_VALUE) ){
                            return null;
                        }
                        List<TerminalBase> ShortestPath = new ArrayList<>();
                        int ShortestTime = time.get(destination);
                        for(Terminal terminal = (Terminal) destination; terminal != null; terminal = terminal.getPredecessor()){
                            ShortestPath.add(terminal);
                        }
                        Collections.reverse(ShortestPath);
                        return new Path(ShortestPath, ShortestTime);
                    }
                }
            }
        }
        return null;
    }

    // Get time taken to travel between two terminals
    private int getTime(Terminal terminal, Terminal target){
        for(int i = 0; i < edges.size(); i++){
            if(edges.get(i).getOrigin().equals(terminal)
                && edges.get(i).getDestination().equals(target)){
                return edges.get(i).getTime();
            }
        }
        return 0;
    }

    // Dijkstra's algorithm using lecture pseudo code
    @Override
    public Path findFastestPath(TerminalBase origin, TerminalBase destination) {
        PriorityQueue<Entry> queue = new PriorityQueue<>();
        time.put((Terminal) origin, 0);

        for (int i = 0; i < nodes.size(); i++){
            if (nodes.get(i) != origin){
                time.put(nodes.get(i), Integer.MAX_VALUE);
            }
            queue.add(new Entry(nodes.get(i), time.get(nodes.get(i))));
        }

        while(!queue.isEmpty()){

            Terminal v = queue.poll().key;
            List<ShuttleBase> incidentshuttles = outgoingShuttles(v);
            for (int i = 0; i < incidentshuttles.size(); i++){
                Terminal z = (Terminal) opposite(incidentshuttles.get(i), v);
                int r = time.get(v) + v.getWaitingTime() + incidentshuttles.get(i).getTime();
                if (r < time.get(z)){
                    time.put(z, r);
                    z.setPredecessor(v);
                    Entry toDelete = new Entry(z, z.getWaitingTime());
                    queue.remove(toDelete);
                    queue.add(new Entry(z, r));
                }
            }
        }
        if (time.get(destination).equals(Integer.MAX_VALUE) ){
            return null;
        }

        List<TerminalBase> FastestPath = new ArrayList<>();
        for(Terminal terminal = (Terminal) destination; terminal != null; terminal = terminal.getPredecessor()){
            FastestPath.add(terminal);
        }

        Collections.reverse(FastestPath);
        int FastestTime = time.get(destination);

        return new Path(FastestPath, FastestTime);
    }

    public static class Entry implements Comparable<Entry>{
        private Terminal key;
        private int value;

        public Entry(Terminal key, int time){
            this.key = key;
            this.value = time;
        }
        @Override
        public int compareTo(Entry other) {
            return this.value-other.value;
        }
    }

    /* Implement all the necessary methods of the Airport here */

    static class Terminal extends TerminalBase {
        private Terminal predecessor;
        /**
         * Creates a new TerminalBase instance with the given terminal ID
         * and waiting time.
         *
         * @param id          terminal ID
         * @param waitingTime waiting time for the terminal, in minutes
         */
        public Terminal(String id, int waitingTime) {
            super(id, waitingTime);
        }

        /* Implement all the necessary methods of the Terminal here */
        public Terminal getPredecessor(){
            return predecessor;
        }
        public void setPredecessor(Terminal predecessor){
            this.predecessor = predecessor;
        }
    }

    static class Shuttle extends ShuttleBase {
        /**
         * Creates a new ShuttleBase instance, travelling from origin to
         * destination and requiring 'time' minutes to travel.
         *
         * @param origin      origin terminal
         * @param destination destination terminal
         * @param time        time required to travel, in minutes
         */
        public Shuttle(TerminalBase origin, TerminalBase destination, int time) {
            super(origin, destination, time);
        }
        /* Implement all the necessary methods of the Shuttle here */
    }

}
