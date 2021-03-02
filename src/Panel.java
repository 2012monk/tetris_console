public class Panel {
    private String[][] panel;
    private final String[][] origin;

    public Panel(int x){
        this.origin = new String[15][10];

        for (int i=0; i< 15; i++){
            for (int j=0; j< 10; j++){
                if (0 < i && i < 14 && 0 < j && j < 9)
                    origin[i][j] = " ";
                else{
                    this.origin[i][j] = "@";
                }
            }
        }
    }

    public Panel(){
        this.origin = null;
        this.panel = new String[15][10];

        for (int i=0; i< 15; i++){
            for (int j=0; j< 10; j++){
                if (0 < i && i < 14 && 0 < j && j < 9)
                    this.panel[i][j] = " ";
                else{
                    this.panel[i][j] = "@";
                }
            }
        }
    }

    public void initPanel(){
        this.panel = origin.clone();
    }

    public Panel(int height, int width, String[][] origin){
        this.origin = origin;
        setPanel(height, width);
    }

//    public void setPanel(){
//        this.origin = new String[15][10];
//
//        for (int i=0; i< 15; i++){
//            for (int j=0; j< 10; j++){
//                if (0 < i && i < 14 && 0 < j && j < 9)
//                    origin[i][j] = " ";
//                else{
//                    this.origin[i][j] = "@";
//                }
//            }
//        }
//    }

    public void setPanel(int height, int width){
        panel = new String[height][width];
        for (int i=0; i< panel.length; i++){
            for (int j=0; j<panel[0].length; j++){
                if (0 < i && i < panel.length-1 && 0 < j && j < panel[0].length-1)
                    panel[i][j] = " ";
                else{
                    panel[i][j] = "@";
                }
            }
        }
    }

    public String[][] getPanel(){
        return panel;
    }


    public int addstr(int y, int x, String s){
        panel[y][x] = s;
        return 0;
    }

    public boolean isEmpty(int x, int y){
        if (panel[x][y] != null){
            return !panel[x][y].equals("#");
        }
        return true;
    }

    public boolean isEmpty(int x, int y, String s){
        return panel[x][y].equals(s);
    }


//    public HashSet<int[]> getBORDER(){
//        return BORDER;
//    }



}
