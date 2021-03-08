import java.io.*;
import java.util.Arrays;
import java.util.HashSet;


public class Main implements Runnable{
    private final int width = 10, height = 40;
    int key, score, count, dropRate, initX, initY, boxSize, aCo, bCo, FPS, startX, startY, position;
    boolean running, moving, locked, WAIT;
    private final int KEY_LEFT = 68, KEY_RIGHT = 67,
            KEY_ROTATE = 65, KEY_DOWN = 66,
            KEY_HARDDOWN = 32, KEY_QUIT = 113;

    Thread thread, keyThread, paintThread, dropThread;
    Panel p;
    BlockCo bc;
    HashSet<int[]> panelStatus;
    int[][] blockCo;
    {
        startX = 10;
        startY = width / 2 - 2;
        FPS = 60;
        dropRate = 180;
        this.p = new Panel(height, width);

        score = 0;
        this.bc = new BlockCo();
        this.panelStatus = new HashSet<>(width * height);
        this.blockCo = bc.getCurrentBlock();
        moving = false;
        locked = false;
    }

    public Main(){
        WAIT = false;
        initCoor();
        start();
    }


    class Paint implements Runnable{
        BufferedWriter out;

        public Paint(){

        }

        @Override
        public void run() {
            while(running){
                try {
                    reset();
                    load();
                    blit(p);
//                    out.close();
                    Thread.sleep(1000/FPS);
                }catch (Exception e){
                    e.printStackTrace();
                    try {
                        Thread.currentThread().join();
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
            }

        }

        public synchronized void load(){
            for (int[] c : blockCo) {
                p.addstr(c[0], c[1], "#");
            }
        }

        public synchronized void blit(Panel panel) throws IOException {
            out = new BufferedWriter(new OutputStreamWriter(System.out));
            out.write("Score : " + score);
            out.newLine();
            for (String[] p : panel.getPanel()) {
                out.write(Arrays.toString(p).
                        replaceAll("[\\[\\]]", "").
                        replaceAll(",", ""));
                out.newLine();
            }
            out.flush();
        }

    }
    class DropHandle implements Runnable{

        @Override
        public void run() {
            while (running) {
                try {
//                    System.out.println(move(KEY_DOWN));
                    if (!move(KEY_DOWN)) {
                        Thread.sleep(200);
                        if (!move(KEY_DOWN)) {
                            WAIT = true;
                            Thread.sleep(600);

                            WAIT = false;
                            initCoor();
                            reset();
                        }
                    }
                    Thread.sleep(dropRate);
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        Thread.currentThread().join();
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
            }
        }
    }

    class KeyHandle implements Runnable{
        public void stop(){
            try{
                System.out.println("Key Stop");
                Thread.currentThread().join();
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        public int keyHandle() throws IOException {
            int in, t;
            InputStream inputStream = System.in;
//            if (inputStream.notify())
            in = inputStream.read();

                if (in == 27){
                    System.in.read();
                    t = System.in.read();
                    return t;
                }

            return in;
        }


        @Override
        public void run() {
            while(running)
            {
                try{
                    key = keyHandle();
                    move(key);
                }catch (IOException e){
                    e.printStackTrace();
                    this.stop();
                }
            }
        }
    }




    public void start(){
        this.count = 0;
        running = true;
        thread = new Thread(this);
        keyThread = new Thread(new KeyHandle());
        paintThread = new Thread(new Paint());
        dropThread = new Thread(new DropHandle());
        keyThread.setName("key");
        paintThread.setName("paint");
        dropThread.setName("drop");
        thread.setName("main");
//        dropThread.setDaemon(true);
        paintThread.setDaemon(true);
//        keyThread.setPriority(8);
//        dropThread.setPriority(4);
//        thread.setPriority(10);
        thread.start();
        keyThread.start();
        paintThread.start();
        dropThread.start();


    }





    public synchronized void reset(){
        for (int i = 1; i < height - 1; i++){
            for (int j = 1; j < width - 1; j++){
                    p.addstr(i, j, " ");
            }
        }
        for (int[] c:panelStatus){
            p.addstr(c[0], c[1], "B");
        }
        int chk = fullCheck();
        if (chk != -1){
            panelStatus.removeIf(co -> co[0] == chk);
            for (int[] co: panelStatus){
                if (co[0] < chk) co[0]++;
            }
            score++;
//            printPanel();
            reset();
        }

    }

    public synchronized void reset(int dir){
        for (int i = 1; i < height - 1; i++){
            for (int j = 1; j < width - 1; j++){
                p.addstr(i, j, " ");
            }
        }
        for (int[] c:panelStatus){
            p.addstr(c[0], c[1], "B");
        }
        int chk = fullCheck();
        if (chk != -1){
            panelStatus.removeIf(co -> co[0] == chk);
            for (int[] co: panelStatus){
                if (co[0] < chk) co[0]++;
            }
            score++;
//            printPanel();
            reset();
        }

    }

//    public void updatePanel(){
//
//    }

    public  void updatePanel()  {
        // erase tail
        reset();
        if (move(key)){
            for (int[] c : blockCo) {
                p.addstr(c[0], c[1], "#");
            }
        }else{
//            saveStatus();
            initCoor();
            reset();

        }

    }


    public synchronized void initCoor() {
        if (!WAIT) {
            try {
                if (moving) {
                    panelStatus.addAll(Arrays.asList(blockCo));
                }
                Thread.sleep(30);
                moving = false;
                count = 0;
                BlockCo b = new BlockCo();
                blockCo = b.getCurrentBlock();
                boxSize = b.getSize();

                position = 0;
//                initX = blockCo[1][0];
//                initY = blockCo[1][1];
                aCo = 0;
                bCo = 0;

//            if (Arrays.stream(this.blockCo).anyMatch(co -> co[0] == 0)) {
//                for (int[] c : this.blockCo) {
//                    c[0]++;
//                }
//                aCo++;
//            }
                for (int[] c : this.blockCo) {
                    c[1] += startY;
                    c[0] += startX;
                }
                aCo += startX;
                bCo += startY;

                moving = true;
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }



    public void saveStatus(){
        panelStatus.addAll(Arrays.asList(blockCo));
        count = 0;
        moving = false;
    }

    public void printPanel(){
        String[][] panel = p.getPanel();
        System.out.println(count);
        for (String[] s: panel){
            System.out.println(
                    Arrays.toString(s).replaceAll("[\\[\\]]","").
                            replaceAll(",",""));
        }
    }

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
        if (direction == KEY_DOWN){
            for (int[] c: blockCo){
                if (c[0] == height - 2) return true;
                for (int[] w: panelStatus){
                    if (Arrays.equals(new int[]{c[0]+1,c[1]}, w)) return true;
                }
            }
        }

        if (direction == KEY_RIGHT){
            for (int[] c: blockCo){
                if (c[1] == width - 2) return true;
                for (int[] w: panelStatus){
                    if (Arrays.equals(new int[]{c[0],c[1]+1}, w)) return true;
                    }
                }
            }
        if (direction == KEY_LEFT){
            for (int[] c: blockCo){
                if(c[1] == 1) return true;
                for(int[] w: panelStatus){
                    if (Arrays.equals(new int[]{c[0], c[1] -1}, w)) return true;
                }
            }
        }

        if (direction == KEY_ROTATE){
//            return rotateCollision(blockCo);
            for (int[] c: blockCo){
                int tmp = c[0];
                int h = c[1] - bCo + aCo;
                int w = aCo + bCo - c[0] + boxSize;
                if (h <= 0 | w <= 0 | w >= width - 1 | h >= height - 1) return true;
                for (int[] k: panelStatus){
                    if (Arrays.equals(new int[]{h, w}, k)) return true;
                }
            }
        }
        return false;
    }

    public boolean rotateCollision(int[][] block){
        for (int[] c: block){
            if (c[0] <= 0 | c[1] <= 0 | c[1] >= width - 1 | c[0] >= height - 2) return true;
            for (int[] k: panelStatus){
                if (Arrays.equals(c, k)) return true;
            }
        }
        return false;
    }

    public void wallKick(int[][] block, int currentPosition, int boxSize){
        int[][] tmpBlock = new int[4][2];
        for (int i = 0; i < 4; i++){
            tmpBlock[i][0] = block[i][1] - bCo + aCo;
            tmpBlock[i][1] = aCo + bCo - block[i][0] + boxSize;
        }
        boolean chk = false;
        for (int[] c: bc.getWallKickTable(boxSize, currentPosition)){
            int[][] tmp = tmpBlock.clone();
            for (int[] b: tmp){
                b[0]+=c[0];
                b[1]+=c[1];
            }
            if (!rotateCollision(tmp)) {
                aCo += c[0];
                bCo += c[1];
                blockCo = tmp;
                position = ++position % 4;
                System.out.println(position);

                break;
            }
        }

//        if (!chk) blockCo = block;
    }

    public boolean move(int direction){
        switch (direction){
            case KEY_DOWN:
                if (!collision(direction)){
                    for (int[] co : blockCo) {
                        co[0]++;
                    }
                    aCo++;
                    return true;
                }else{
                    return false;
                }
            case KEY_LEFT:
                if (!collision(direction)) {
                    for (int[] c : blockCo) {
                        c[1]--;
                    }
                    bCo--;
                }
                return true;
            case KEY_RIGHT:
                if (!collision(direction)) {
                    for (int[] c : blockCo) {
                        c[1]++;
                    }
                    bCo++;
                }
                return true;
            case KEY_HARDDOWN:
                while (true) {
                    if (!collision(KEY_DOWN)) {
                        move(KEY_DOWN);
                    } else {
                        break;
                    }
                }
                return false;
            case KEY_ROTATE:
                if (!collision(direction)) {
                    for (int[] co : blockCo) {
                        int tmp = co[0];
//                        Clockwise Rotation
                        co[0] = co[1] - bCo + aCo;
                        co[1] = aCo + bCo - tmp + boxSize;
                    }
                    position = ++position % 4;

                    return true;
                }else{
                    wallKick(blockCo, position, boxSize);
                    return true;
                }
            default:
                reset();
        }
        if (key != 113) key = -2;
        reset();
        return true;
    }
//
//    public boolean move(int direction) {
////        if (!collision(direction)){
//            switch (direction){
//                case KEY_DOWN:
//                    if (collision(direction)) return true;
//                    for (int[] co : blockCo) {
//                        co[0]++;
//                    }
//                    aCo++;
//                    return true;
//                case KEY_LEFT:
//                    if (collision(direction)) return true;
//                    for (int[] c : blockCo) {
//                        c[1]--;
//                    }
//                    bCo--;
//                    return true;
//                case KEY_RIGHT:
//                    if (collision(direction)) return true;
//                    for (int[] c : blockCo) {
//                        c[1]++;
//                    }
//                    bCo++;
//                    return true;
////                case KEY_ROTATE:
////                    for (int[] co : blockCo) {
////                        int tmp = co[0];
//////                        Clockwise Rotation
////                        co[0] = co[1] - bCo + aCo;
////                        co[1] = aCo + bCo - tmp + boxSize;
////                    }
////                    position = position < 3 ? position++ : 0;
////                    return true;
//                case KEY_HARDDOWN:
//                    while (true) {
//                        if (!collision(KEY_DOWN)) {
//                            move(KEY_DOWN);
//                        } else {
//                            break;
//                        }
//                    }
//                    return false;
////                default:
////                    key = -2;
//
//            }
//            if (direction == KEY_ROTATE){
//                if (!collision(direction)) {
//                    System.out.println("WorkWell");
//                    for (int[] co : blockCo) {
//                        int tmp = co[0];
////                        Clockwise Rotation
//                        co[0] = co[1] - bCo + aCo;
//                        co[1] = aCo + bCo - tmp + boxSize;
//                    }
//                    position = ++position % 4;
////                    position = position < 3 ? position++ : 0;
//                    return true;
//                }else{
//                    System.out.println("TNTNTNTNT\n\n\n\n\n\n");
//                    wallKick(blockCo, position, boxSize);
//                    return true;
//                }
//            }
////            if (direction == KEY_DOWN) {
////                for (int[] co : blockCo) {
////                    co[0]++;
////                }
////                aCo++;
////                return true;
////            }
////            if (direction == KEY_RIGHT) {
////                for (int[] c : blockCo) {
////                    c[1]++;
////                }
////                bCo++;
////                return true;
////            }
////            if (direction == KEY_LEFT) {
////                for (int[] c : blockCo) {
////                    c[1]--;
////                }
////                bCo--;
////                return true;
////            }
////            if (direction == KEY_HARDDOWN) {
////                while (true) {
////                    if (!collision(KEY_DOWN)) {
////                        move(KEY_DOWN);
////                    } else {
////                        break;
////                    }
////                }
////                return false;
////            }
////            if (direction == KEY_ROTATE) {
////                for (int[] co : blockCo) {
////                    int tmp = co[0];
////                    //                  Clockwise Rotation
////                    co[0] = co[1] - bCo + aCo;
////                    co[1] = aCo + bCo - tmp + boxSize;
////                }
////                position = position < 3 ? position++ : 0;
////                return true;
////            }
////            }else if (direction == KEY_ROTATE && collision(KEY_ROTATE)) {
////                wallKick(blockCo, position, boxSize);
////                return true;
////                }
////        }
////        }
//        if (direction == KEY_DOWN) {
//            return false;
//        }
//        if (key != 113) key = -2;
//        reset();
////        return true;
//        return true;
//    }

    public synchronized void playingCheck() {
        if (key == KEY_QUIT) {
            System.out.println("END");
            running = false;
        }
        for (int i = 1;i < width - 1; i++){
            if (p.isEmpty(0, i, "B") ||
                    p.isEmpty(0, i, "#")){
                running = false;

            }
        }
        boolean chk = false;
        for (int i=1; i < width-2; i++){
            chk = p.isEmpty(1, i, "B");
            if (!chk) break;
        }
        if (chk) stop();

    }

    public synchronized void stop(){
        String msg = "Game Over";
        p.addstr(8, width / 2 - msg.length() / 2, msg);
        printPanel();
        System.exit(1);
    }

    @Override
    public void run() {
        while(running){
            try{
                playingCheck();
//                updatePanel();
//                printPanel();
                Thread.sleep(10);
            }catch (Exception e){
                e.printStackTrace();
                try {
                    thread.join();
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        new Main();
    }

    public void handleBlock(){
        for (int[] c: blockCo){
            if (c[0] < height - 1) c[0]++;
        }
        count++;
    }
}
