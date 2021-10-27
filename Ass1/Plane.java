public class Plane extends PlaneBase {

    public Plane(String planeNumber, String time) {
        super(planeNumber, time);
    }

    @Override
    public int compareTo(PlaneBase planeBase) {
        String p1_no = this.getPlaneNumber();
        String p1_time = this.getTime();
        String p2_no = planeBase.getPlaneNumber();
        String p2_time = planeBase.getTime();

        int p1_tLen = p1_time.length();
        int p2_tLen = p2_time.length();
        int lim = findMin(p1_tLen, p2_tLen);

        int k = 0;
        boolean timeIsEqual = p1_time.equals(p2_time);

        if (!timeIsEqual){
            while (k < lim){
                char c1 = p1_time.charAt(k);
                char c2 = p2_time.charAt(k);
                if (c1 != c2){
                    return c1 - c2;
                }
                k++;
            }
            return p1_tLen - p2_tLen;
        }else{
            k = 0;
            int p1_nLen = p1_no.length();
            int p2_nLen = p2_no.length();
            lim = findMin(p1_nLen, p2_nLen);
            while (k < lim){
                char c1 = p1_no.charAt(k);
                char c2 = p2_no.charAt(k);
                if (c1 != c2){
                    return c1 - c2;
                }
                k++;
            }
            return p1_nLen - p2_nLen;
        }
    }

    int findMin(int c1, int c2){
        if (c1 > c2){
            return c2;
        }else if(c2 > c1){
            return c1;
        }else{
            return c1;
        }
    }
    /* Implement all the necessary methods of Plane here */
}
