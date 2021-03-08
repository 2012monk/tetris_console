import java.io.IOException;

public class Panel {
    private String[][] panel;


    public Panel(){
        this(40, 10);
    }

    public Panel(int height, int width){
        this.panel = new String[height][width];

        for (int i=0; i< height; i++){
            for (int j=0; j< width; j++){
                if (0 < i && i < height - 1 && 0 < j && j < width - 1)
                    this.panel[i][j] = " ";
                else{
                    this.panel[i][j] = "@";
                }
            }
        }
    }



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


    public void addstr(int y, int x, String s){
        if (s.length() == 1) panel[y][x] = s;
        try {
            if (x + s.length() < panel.length) {
                for (int i = x; i < x + s.length(); i++) {
                    panel[y][i] = String.valueOf(s.charAt(i-x));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("index Err");
        }

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



}
