import java.util.HashMap;
import java.util.Random;

public class Block {
    private final String[][] a,b,c,d,e,f,g;
    private final String[] nameList;
    private final HashMap<String, String[][]> blockList;
    private String[][] currentBlock;
    Random r;


    public Block(){
        r = new Random();
        a = new String[][]
                {
                        {"", "#", "", ""},
                        {"#", "#", "#", ""},
                        {"", "", "", ""},
                        {"", "", "", ""}
                };
        b = new String[][]
                {
                        {"#","","",""},
                        {"#","","",""},
                        {"#","#","",""},
                        {"","","",""}
                };
        c = new String[][]
                {
                        {"","#","",""},
                        {"","#","",""},
                        {"#","#","",""},
                        {"","","","",}
                };
        d = new String[][]
                {
                        {"#","#","",""},
                        {"#","#","",""},
                        {"","","",""},
                        {"","","",""}
                };
        e = new String[][]
                {
                        {"#","#","",""},
                        {"","#","#",""},
                        {"","","","",""},
                        {"","","","",""}
                };
        f = new String[][]
                {
                        {"","#","#",""},
                        {"#","#","",""},
                        {"","","","",""},
                        {"","","",""}
                };
        g = new String[][]
                {
                        {"","","",""},
                        {"","","",""},
                        {"#","#","#","#"},
                        {"","","",""}
                };

        blockList = new HashMap<String, String[][]>(){{
            put("a", a);
            put("b", b);
            put("c", c);
            put("d", d);
            put("e", e);
            put("f", f);
            put("g", g);
        }};

        nameList = new String[]{"a", "b", "c", "d", "e", "f", "g"};
        setBlock();

    }


    public void setBlock(){
        currentBlock = blockList.get(nameList[r.nextInt(7)]);
    }

    public String[][] getBlock(){
        return currentBlock;
    }

    public void rotate(){
        String[][] tmp = new String[3][3];
        for (int i=0; i<3; i++){
            for (int j=0; j<3; j++){
                tmp[2-j][i] = currentBlock[i][j];
            }
        }
    }
}
