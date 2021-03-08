import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class BlockCo implements Cloneable{
    private int[][] I,O,J,Z,S,L,T;
    private String[] nameList = new String[]{"I", "O", "Z", "S", "L", "T", "J"};
    private int[][] currentBlock;
    private final HashMap<String, int[][]> list;
    private final HashMap<String, Integer> sizeList;
    private String nextBlock;
    private boolean[] picked;
    private ArrayList<StringBuffer> bag;
    Random r;
    int currentSize;


    private final int[][][] WALLKICK_DATA =  new int[][][]
            {
                    {{0, 0}, {0, -1}, {-1,-1}, {2,0}, {2,-1}}, // Spawn to right 0 -> R
                    {{0, 0}, {0, 1}, {1,1},{-2,+0}, {-2,+1}}, // Right to 3rd R -> 2
                    {{0, 0}, {0, +1}, {-1,+1}, {2,0}, {2,+1}}, // 2 to L
                    {{0, 0}, {0, -1}, {1,-1}, {-2, 0}, {-2,-1}} // L to 0
            };

    private final int[][][] WALLKICK_DATA_I = new int[][][]
            {
                    {{0, 0}, {0, -2}, {0, 1}, {1, -2}, {-2, 1}},
                    {{0, 0}, {0, -1}, {0, 2}, {-2, -1}, {1, 2}},
                    {{0, 0}, {0, 2}, {0, -1}, {-2,1}, {2,-1}},
                    {{0, 0}, {0, 1}, {0, -2}, {2, 1}, {-1,-2}}
            };

    public void fillBag(){
        for (int o=0; o<3;o++){
            nameList = new String[]{"I", "O", "Z", "S", "L", "T", "J"};
            for (int i = 6; i >= 0; i--){
                int p = r.nextInt(i+1);
                String tmp = nameList[i];
                nameList[i] = nameList[p];
                nameList[p] = tmp;
            }
            bag.add(new StringBuffer(String.join("",nameList)));
        }

    }
    {
        bag = new ArrayList<>(3);
        r = new Random();





        // z block
        Z = new int[][]
                {
                        {1, 0}, {1, 1},
                        {2, 1}, {2, 2}
                };

        // J block
        J = new int[][]
                {
                        {1, 0}, {1, 1},
                        {1, 2}, {0, 0},
//                        {0, 1}, {1, 1},
//                        {2, 1}, {2, 0}
                };

        // I block
        I = new int[][]
                {
                        {1, 0}, {1, 1},
                        {1, 2}, {1, 3}
                };

        // O block
        O = new int[][]
                {
//                        {1, 2}, {1, 1},
//                        {2, 1}, {2, 2}
                        {0, 0}, {0, 1},
                        {1, 0}, {1, 1}
                };

        // T block
        T = new int[][]
                {
//                        {1, 0}, {1, 1},
//                        {1, 2}, {2, 1}
                        {1, 0}, {1, 1},
                        {1, 2}, {0, 1}
                };

        // S block
        S = new int[][]
                {
                        {1, 2}, {1, 1},
                        {2, 0}, {2, 1}
//                        {1, 1}, {1, 2},
//                        {2, 0}, {2, 1}
                };
        // L block
        L = new int[][]
                {
                        {1, 0}, {1, 1},
                        {1, 2}, {0, 2}
//                        {0, 1}, {1, 1},
//                        {2, 1}, {2, 0}
                };

        list = new HashMap<String, int[][]>(){{
            put("I", I);
            put("L", L);
            put("S", S);
            put("Z", Z);
            put("J", J);
            put("O", O);
            put("T", T);
        }};
        sizeList = new HashMap<String, Integer>(){{
            put("I", 3);
            put("L", 2);
            put("S", 2);
            put("Z", 2);
            put("J", 2);
            put("O", 1);
            put("T", 2);
        }};

        setBlock();


    }
    public BlockCo(){
        setCurrentBlock();
    }

    public int[][] getCurrentBlock(){
        setBlock();
        return currentBlock;
//        return I.clone();
    }
    public void setBlock(){
        if (bag.isEmpty()) fillBag();
        int p = r.nextInt(bag.size());
        if (bag.get(p).length() > 0){
            nextBlock = String.valueOf(bag.get(p).charAt(0));
            bag.get(p).deleteCharAt(0);
        }else
            bag.remove(p);



        currentBlock = list.get(nextBlock).clone();
        currentSize = sizeList.get(nextBlock);
    }

    public int getSize(){
        return currentSize;
    }


    public void setCurrentBlock(){
    }

    public int[][] getWallKickTable(int boxsize, int currentPosition){
        boxsize = 4 % boxsize;
        if (boxsize == 3) return WALLKICK_DATA_I[currentPosition];
        return WALLKICK_DATA[currentPosition];
    }
}
