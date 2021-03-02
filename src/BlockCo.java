import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BlockCo implements Cloneable{
    private int[][] a;
    private final int[][] currentBlock;
    private final HashMap<String, int[][]> list;

    public BlockCo(){
//        currentBlock = new int[][]
//                {
//                        {1, 0}, {1, 1},
//                        {2, 1}, {2, 2}
//                };

//        currentBlock = new int[][]
//                {
//                        {1, 0}, {1, 1},
//                        {1, 2}, {2, 2}
//                };

        currentBlock = new int[][]
                {
                        {1, 0}, {1, 1},
                        {1, 2}, {1, 3}
                };

        list = new HashMap<String, int[][]>(){{
            put("a", a);
        }};
//        this.currentBlock = a;
        setCurrentBlock();
    }

//    public int[][] getCurrentBlock(){
////        return this.currentBlock;
//
//        return a;
//    }
    public int[][] getCurrentBlock(){

        return this.currentBlock.clone();
    }

    public void setCurrentBlock(){
    }
}
