class DisplayRandom extends DisplayRandomBase {

    public DisplayRandom(String[] csvLines) {
        super(csvLines);
    }

    @Override
    /**
     * Quicksort Algorithm- Best case: O(nlogn), Worst case: O(n^2)
     * Pivot: Middle element.
     * @return Sorted list of planes
     */
    public Plane[] sort() {
        int n = getData().length;
        Plane[] List = getData();
        quickSort(List, 0, n-1);
        setData(List);
        return getData();
    }
    /* Implement all the necessary methods here */

    void quickSort (Plane List[], int low, int high){
        int pi = partition(List, low, high);
        if (low < pi - 1) {
            quickSort(List, low, pi - 1);
        }
        if ( pi < high){
            quickSort(List, pi, high);
        }
    }

    int partition(Plane[] List, int low, int high){
        Plane pivot = List[low + (high-low)/2];
        int i = low; int j = high;

        while(i <= j){
            while(List[i].compareTo(pivot) < 0){
                i++;
            }
            while(List[j].compareTo(pivot) > 0){
                j--;
            }
            if (i <= j){
                swap (List, i, j);
                i++;
                j--;
            }
        }
        return i;
    }

    void swap (Plane[] List, int i, int j){
        Plane temp = List[i];
        List[i] = List[j];
        List[j] = temp;
    }

//    public static void main(String[] args){
//        String[] planes = {"ABC1233, 09:01", "ABC1234, 09:24", "BAA1113, 09:24", "ABC1232, 08:59"};
//        DisplayRandom test = new DisplayRandom(planes);
//        for (int i=0; i< test.getData().length; i++){
//            System.out.println(test.sort()[i]);
//        }
//    }
}

class DisplayPartiallySorted extends DisplayPartiallySortedBase {

    public DisplayPartiallySorted(String[] scheduleLines, String[] extraLines) {
        super(scheduleLines, extraLines);
    }

    /**
     * Quick Sort + Merge. Time complexity: O(nlogn) + O(n) = O(nlogn)
     * @return Sorted list of planes
     */
    @Override
    Plane[] sort() {
        Plane[] n_sorted = getSchedule();
        Plane[] n_unsorted = getExtraPlanes();
        int n = n_sorted.length + n_unsorted.length;
        quickSort(n_unsorted, 0, n_unsorted.length-1);

        setSchedule(n_sorted);
        setExtraPlanes(n_unsorted);

        return mergeLists(getSchedule(), getExtraPlanes(), n, getSchedule().length, getExtraPlanes().length);
    }
    /* Implement all the necessary methods here */

    void quickSort (Plane List[], int low, int high){
        int pi = partition(List, low, high);
        if (low < pi - 1) {
            quickSort(List, low, pi - 1);
        }
        if ( pi < high){
            quickSort(List, pi, high);
        }
    }
    int partition(Plane[] List, int low, int high){
        Plane pivot = List[low + (high-low)/2];
        int i = low; int j = high;

        while(i <= j){
            while(List[i].compareTo(pivot) < 0){
                i++;
            }
            while(List[j].compareTo(pivot) > 0){
                j--;
            }
            if (i <= j){
                swap (List, i, j);
                i++;
                j--;
            }
        }
        return i;
    }
    void swap (Plane[] List, int i, int j){
        Plane temp = List[i];
        List[i] = List[j];
        List[j] = temp;
    }

    /**
     * Merge two lists in order. Time complexity: O(n)
     * @param l1
     * @param l2
     * @param n
     * @param n1
     * @param n2
     * @return
     */
    Plane[] mergeLists (Plane[] l1, Plane[] l2, int n, int n1, int n2){

        Plane[] mergedList = new Plane[n];

        int i = 0, j = 0, k = 0;
        while (i < n1 && j < n2){
            mergedList[k++] = l1[i].compareTo(l2[j]) < 0 ? l1[i++] : l2[j++];
        }
        while (i < n1){
            mergedList[k++] = l1[i++];
        }
        while ( j < n2){
            mergedList[k++] = l2[j++];
        }
        return mergedList;
    }


//    public static void main(String[] args){
//        String[] sorted_planes = {"ABC1233, 09:25", "ABC1234, 09:25", "BAA1113, 10:28"};
//        String[] unsorted_planes = {"ENC3453, 08:21", "DNZ8431, 10:58", "JKL1111, 07:42"};
//
//        DisplayPartiallySortedBase test = new DisplayPartiallySorted(sorted_planes, unsorted_planes);
//        int n = test.getSchedule().length + test.getExtraPlanes().length;
//        for (int i=0; i < n; i++){
//            System.out.println(test.sort()[i]);
//        }
//    }
}
