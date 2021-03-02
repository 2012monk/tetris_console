import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Main implements Runnable{
    private final int width = 10, height = 15;
    int count;
    boolean running, moving;
    Thread thread, keyThread;
    String[][] block;
    Panel p;
    Block b;
    BlockCo bc;
    int[][] blockCo;
    int[] coor;
    HashSet<int[]> panelStatus;
    int key, aCoor, bCoor;

    class KeyHandle implements Runnable{
        final int KEY_LEFT = 68;
        final int KEY_RIGHT = 67;
        final int KEY_UP = 65;
        final int KEY_DOWN = 66;
        final int KEY_ROTATE = 32;

        public KeyHandle(){
            key = 0;
        }


        public void start(){
        }

        public void stop(){
            try{
                System.out.println("Key Stop");
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        public int keyHandle() throws IOException {
            int in = 0;
            int t = 0;
            InputStream inputStream = System.in;
            in = inputStream.read();

                if (in == 27){
                    System.in.read();
                    t = System.in.read();
                }
                if (t == KEY_DOWN | t == KEY_LEFT | t == KEY_UP | t == KEY_RIGHT){
                    return t;
                }
                if (t == KEY_ROTATE){
                    return 2;
                }
                if (in == 113) {
                    return 113;
                }

            return in;
        }




        @Override
        public void run() {
            while(running)
            {
                try{
                    key = keyHandle();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }


    public Main(){
        this.bc = new BlockCo();
        this.b = new Block();
        this.block = b.getBlock();
        this.panelStatus = new HashSet<>(width * height);
//        panelStatus.add(new int[]{12,5});
//        panelStatus.add(new int[]{12,6});
//        panelStatus.add(new int[]{13,5});
//        panelStatus.add(new int[]{13,6});
        this.bc = new BlockCo();
        this.blockCo = bc.getCurrentBlock();
        this.p = new Panel();
        moving = false;

//        moving = true;
        initCoor();
        start();
    }

    public void start(){
        this.count = 0;
        running = true;
        thread = new Thread(this);
        keyThread = new Thread(new KeyHandle());
        thread.start();
        keyThread.start();

    }



    public void handleBlock(){
        for (int[] c: blockCo){
            c[0]++;
        }
        count++;
        aCoor++;
    }

    public void keyHandle() throws IOException {
        int in = 0;
        int t = 0;
        InputStream inputStream = System.in;
        in = inputStream.read();
        if (in == 27){
            System.in.read();
            t = System.in.read();
        }

        if (t == 67) key = -1;
        if (t == 68) key = 1;
        if (t == 66) key = 0;

        key = in;
    }

    public void reset(){
        for (int i = 1; i < height - 1; i++){
            for (int j = 1; j < width - 1; j++){
                    p.addstr(i, j, "`");
            }
        }

        for (int[] c:panelStatus){
//            System.out.println(Arrays.toString(c)+"RESET SEQ");
            p.addstr(c[0], c[1], "B");

        }
        int chk = fullCheck();

        if (chk != -1){
            panelStatus.removeIf(co -> co[0] == chk);
            for (int[] co: panelStatus){
                co[0]--;
            }
            reset();
        }

    }

    public void updatePanel() {
        // erase tail
        reset();
        p.addstr(count, 5, "A");
//        if (moving) {
//            if (!collision(0)) {
//                for (int[] c : blockCo) {
//                    p.addstr(c[0], c[1], "#");
//                }
//            } else {
//                saveStatus();
//            }
//        }else{
//            this.blockCo = bc.getCurrentBlock();
//            moving = true;
//            initCoor();
//        }
        move(key);

        if (!collision(0)) {
            for (int[] c : blockCo) {
                p.addstr(c[0], c[1], "#");
            }
        } else {
            saveStatus();
            initCoor();
        }

    }
    public void initCoor(){
        aCoor = 1;
//        bCoor = width / 2 - 1;
        moving = true;
        bCoor = 1;
        blockCo = new BlockCo().getCurrentBlock();
        for (int[] c: this.blockCo){
//            c[0]++;
            c[1] += width / 2 - 2;

        }
    }

    public void saveStatus(){
        panelStatus.addAll(Arrays.asList(blockCo));
        count = 0;
//        System.out.println(Arrays.deepToString(blockCo));
        moving = false;
//        System.out.println("Saved Complete At saveStatus");

    }



//    public void setPanel(int height, int width){
//        this.panel = new String[height][width];
//        for (int i=0; i< this.panel.length; i++){
//            for (int j=0; j<this.panel[0].length; j++){
//                if (0 < i && i < this.panel.length-1 && 0 < j && j < this.panel[0].length-1)
//                    this.panel[i][j] = " ";
//                else{
////                    BORDER.add(new int[]{i,j});
//                    this.panel[i][j] = "@";
//                }
//            }
//        }
//    }


    public void printPanel(){
        String[][] panel = p.getPanel();
        System.out.println(count);
        for (String[] s: panel){
            System.out.println(
                    Arrays.toString(s).replaceAll("[\\[\\]]","").
                            replaceAll(",",""));
        }
    }

//    public void updatePanel(){
//        for (int i = coor[0], x = 2; i >= coor[0] - 2 && x >= 0; i--, x--) {
//            for (int j = coor[1] - 1, y = 0; j <= coor[1] + 1 && y <= 2; j++, y++) {
//                if (block[x][y].equals("#")) {
//                    panel[i][j] = "#";
//                }
//            }
//        }
//        coor[0]++;
//
//    }

//    public boolean fullCheck(){
//        boolean chk;
//        for (int i = 1; i < height - 1; i++){
//            chk = true;
//            for (int j = 1; j < width - 1; j++){
//                if (!p.isEmpty(i, j, "B")){
//                    chk = false;
//                }
//            }
//            if (chk) return true;
//        }
//        return false;
//    }

    public int fullCheck(){
        boolean chk;
        for (int i = 1; i < height - 1; i++){
            chk = true;
            for (int j = 1; j < width - 1; j++){
                if (!p.isEmpty(i, j, "B")){
                    chk = false;
                }
            }
            if (chk) return i;
        }
        return -1;
    }

    public boolean collision(int direction){
        if (direction == 0){
            for (int[] c: blockCo){
                if (c[0] == height - 2) return true;
            }
            for (int[] c: blockCo){
                for (int[] w: panelStatus){
                    if (Arrays.equals(new int[]{c[0]+1,c[1]}, w)){
//                        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n collision Detected");
                        return true;
                    }
                }
            }
        }
        if (direction == 1){
            for (int[] c: blockCo){
                if (c[1] == width - 2) return true;
            }
            for (int[] c: blockCo){
                for (int[] w: panelStatus){
                    if (Arrays.equals(new int[]{c[0],c[1]+1}, w)) return true;
                    }
                }
            }
        if (direction == -1){
            for (int[] c: blockCo){
                if(c[1] == 1) return true;
            }
            for (int[] c: blockCo){
                for(int[] w: panelStatus){
                    if (Arrays.equals(new int[]{c[0], c[1] -1}, w)) return true;
                }
            }
        }

        if (direction == 2){
            int a = blockCo[1][0];
            int b = blockCo[1][1];
            for (int[] c: blockCo){
                int tmp = c[0];
                for (int[] w: panelStatus){
                    if (Arrays.equals(new int[]{a+b-c[1], b-1+tmp}, w)) return true;
                }
            }

        }
        handleBlock();
        return false;
    }

    public void playingCheck() {
//        String msg = "Game End";
//        for (int i = 0; i < msg.length(); i++){
//            p.addstr(8, width / 2 - msg.length() / 2, String.valueOf(msg.charAt(i)));
//        }
        if (key == 113) running = false;
        for (int i = 1;i < width - 1; i++){
            if (p.isEmpty(0, i, "B") ||
                    p.isEmpty(0, i, "#") ||
                    p.isEmpty(1, i, "B")){
                running = false;
                System.out.println("\n\n\n\n\n\n\n\n\n\n       Game End");
                break;
            }
        }

    }


    public void move(int direction){
        direction = direction == 68 ? -1 : direction == 67 ? 1 : direction == 65 ? 2 : 0;

        if(direction == 1 && !collision(1)){
            for (int[] c: blockCo){
                c[1]++;
                bCoor++;
            }
        }
        if (direction == -1 && !collision(-1)){
            for (int[] c: blockCo){
                c[1]--;
                bCoor--;
            }
        }
        if (direction == 2 && !collision(2)){
            int a = blockCo[1][0];
            int b = blockCo[1][1];
            for (int[] co: blockCo){
                int tmp = co[0];
//                co[0] = aCoor + bCoor - co[1];
//                co[1] = tmp + bCoor - aCoor;
                co[0] = a+b-co[1];
                co[1] = b-a+tmp;

            }
        }
        key = 0;

    }



    @Override
    public void run() {
        while(running){
            try{
                System.out.println(key);
                playingCheck();
                updatePanel();
                printPanel();
                Thread.sleep(200);
            }catch (Exception e){
                e.printStackTrace();
                try {
                    thread.join();
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}
