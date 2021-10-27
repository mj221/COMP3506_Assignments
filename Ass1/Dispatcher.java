public class Dispatcher extends DispatcherBase {
    private static Node head;
    private static Node prev;
    private static Node nodeP;  // helps locate node location
    private static boolean nodeLocationHead = false;   //helper head node specifier
    private int size = 0;

    static class Node{
        Plane data;
        Node next;
        Node (Plane d){
            data = d;
            next = null;
        }
        public Plane returnNodeData(){
            return data;
        }
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void addPlane(String planeNumber, String time) {
        Plane newPlane = new Plane(planeNumber,time);
        Node aNode = new Node(newPlane);
        Node current = head;
        if(size() > 0){
            if (current.returnNodeData().getTime().compareTo(aNode.returnNodeData().getTime()) > 0){
                aNode.next = head;
                head = aNode;
            }else{
                while(current.next != null
                        && current.next.returnNodeData().getTime().compareTo(aNode.returnNodeData().getTime()) < 0){
                    current = current.next;
                }
                aNode.next = current.next;
                current.next = aNode;
            }
        }else{
            aNode.next = head;
            head = aNode;
        }
        size++;
    }

    @Override
    public String allocateLandingSlot(String currentTime) {
        Node temp = head;
        if (temp != null && compareTime(temp.returnNodeData().getTime(), currentTime)){
            head = head.next;
            size--;
            return temp.returnNodeData().getPlaneNumber();
        }
        return null;
    }

    @Override
    public String emergencyLanding(String planeNumber) {
        if (!isPresent(planeNumber)){
            return null;
        }else{
            if(nodeLocationHead){
                head = head.next;
            }else{
                prev.next = nodeP.next;
            }
            size--;
            return planeNumber;
        }
    }

    @Override
    public boolean isPresent(String planeNumber) {
        Node temp = head;
        nodeLocationHead = false;
        prev = null;
        nodeP = null;
        if (temp == null){
            return false;
        }
        if (temp.returnNodeData().getPlaneNumber().equals(planeNumber)){
            nodeLocationHead = true;
            return true;
        }
        while(temp != null && !temp.returnNodeData().getPlaneNumber().equals(planeNumber)){
            prev = temp;
            temp = temp.next;
            nodeP = temp;
        }
        if (temp != null){
            nodeLocationHead = false;
            return true;
        }
        return false;
    }

    boolean compareTime(String time1, String time2){
        String[] currentTime = time1.split(":", 2);
        String[] specifiedTime= time2.split(":", 2);
        int currentTimeHour = Integer.parseInt(currentTime[0]);
        int currentTimeMinute = Integer.parseInt(currentTime[1]);
        int specifiedTimeHour = Integer.parseInt(specifiedTime[0]);
        int specifiedTimeMinute = Integer.parseInt(specifiedTime[1]);

        int allowedTimeLimitHour = specifiedTimeHour;
        int allowedTimeLimitMinute = specifiedTimeMinute + 5;
        if (allowedTimeLimitMinute >=60){
            allowedTimeLimitMinute = allowedTimeLimitMinute % 60;
            allowedTimeLimitHour = allowedTimeLimitHour + 1;
            specifiedTimeMinute = 0;
        }
        if (currentTimeHour == allowedTimeLimitHour
                && specifiedTimeMinute <= currentTimeMinute
                && currentTimeMinute <= allowedTimeLimitMinute){

            return true;
        }
        if (time1.compareTo(time2) < 0){
            return true;
        }
        return false;
    }

//    public static void main (String[] args){
//
//        Dispatcher test = new Dispatcher();
//        test.addPlane("EAA1110", "15:43");
//        test.addPlane("ANC3480", "12:12");
//        test.addPlane("ABC1230", "05:42");
//        test.addPlane("AAC3480", "18:43");
//        System.out.println("Dropped: "+test.allocateLandingSlot("12:11"));
//        Node current = head;
//
//        while(current != null){
//            Plane item = current.returnNodeData();
//            System.out.println(item);
//            current = current.next;
//        }
//    }
}

/* Add any additional helper classes here */
