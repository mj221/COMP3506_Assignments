public class SecurityDB extends SecurityDBBase {

    static private class ListNode{
        String key;
        String value;
        ListNode next;
        int attempts;
    }

    private ListNode[] table;
    private int count;
    private int M;
    /**
     * Creates an empty hashtable and a variable to count non-empty elements.
     *
     * @param numPlanes             number of planes per day
     * @param numPassengersPerPlane number of passengers per plane
     */
    public SecurityDB(int numPlanes, int numPassengersPerPlane) {
        super(numPlanes, numPassengersPerPlane);

        int M = numPlanes * numPassengersPerPlane;
        while (!isPrime(M)){
            ++M;
        }
        table = new ListNode[M];
        this.M = M;
    }
    static boolean isPrime(int num){
        if (num%2==0){
            return false;
        }
        for (int i = 3; i*i <= num; i+=2){
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }
    /* Implement all the necessary methods here */
    @Override
    public int calculateHashCode(String key) {
        int hash_code = 0;
        for (int i = 0 ; i < key.length() ; i++){
            hash_code += (key.length() - i) * (int) key.charAt(i);
        }
        hash_code += key.length();
        return hash_code;
    }
    private int compfn (int hash){
        return hash % size();
    }
    @Override
    public int size() {
        return M;
    }

    @Override
    public String get(String passportId) {
        int bucket = compfn(calculateHashCode(passportId));
        ListNode list = table[bucket];
        if (list != null){
            for (int i = 0; i < size(); i++){
                if (list.key.equals(passportId)){
                    return list.value;
                }
            }
        }
        return null;
    }

    @Override
    public boolean remove(String passportId) {
        int bucket = compfn(calculateHashCode(passportId));
        if (table[bucket] == null){
            return false;
        }
        if (table[bucket].key.equals(passportId)){
            table[bucket] = table[bucket].next;
            count--;
            return true;
        }
        return false;
    }

    @Override
    public boolean addPassenger(String name, String passportId) {
        if (count() >= size()){
            resize();
        }
        int bucket = getIndex(passportId);
        ListNode list = table[bucket];

        while(list != null){
            if (list.key.equals(passportId)){
                break;
            }
            list = list.next;
        }
        if (list != null) {
            if (!list.value.equals(name)){
                System.err.println("Suspicious Behaviour");
                return false;
            }
            ListNode newNode = new ListNode();
            newNode.attempts = 1 + table[bucket].attempts;
            if (newNode.attempts > 5){
                System.err.println("Suspicious Behaviour");
                return false;
            }
            newNode.key = passportId;
            newNode.value = name;
            newNode.next = table[bucket];
            table[bucket] = newNode;
        }else{
            ListNode newNode = new ListNode();
            newNode.key = passportId;
            newNode.value = name;
            newNode.attempts = 1;
            newNode.next = table[bucket];
            table[bucket] = newNode;
            count++;
            return true;
        }
        return false;
    }
    private void resize(){
        ListNode[] newTable = new ListNode[MAX_CAPACITY];
        this.M = MAX_CAPACITY;
        for (int i = 0; i < table.length; i++){
            ListNode list = table[i];
            while(list != null){
                ListNode next = list.next;
                int hash = compfn(calculateHashCode(list.key));
                list.next = newTable[hash];
                newTable[hash] = list;
                list = next;
            }
        }
        table = newTable;
    }
    @Override
    public int count() {
        return count;
    }

    @Override
    public int getIndex(String passportId) {
        int bucket = compfn(calculateHashCode(passportId));
        ListNode list = table[bucket];
        if (list != null && !list.key.equals(passportId)){
            bucket = compfn(bucket + 1);
        }
        return bucket;
    }
}

/* Add any additional helper classes here */
