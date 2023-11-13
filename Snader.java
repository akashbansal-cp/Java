import java.util.*;


class Pair{
    private int Key;
    private int Value;
    Pair(int key, int value){
        this.Key=key;
        this.Value=value;
    }

    public int getKey(){
        return this.Key;
    }

    public int getValue(){
        return this.Value;
    }

    public void setKey(int key){
        this.Key=key;
    }

    public void setValue(int value){
        this.Value=value;
    }

}

class Snake{
    private final int head;
    private final int tail;

    Snake(Pair pair){
        this.head=pair.getKey();
        this.tail=pair.getValue();
    }

    public int getHead(){
        return this.head;
    }

    public int getTail(){
        return this.tail;
    }


}

class SnakeHub{
    private List<Snake>  sHub = new ArrayList<Snake>();

    public void addSnake(Snake snake){
        sHub.add(snake);
    }

    public boolean isValidSnake(Pair snake){
        for (Snake e : sHub) {
            if (e.getHead() == snake.getKey()) {
                return false;
            }
        }
        return true;
    }

    public int isHead(int pos){
        for(Snake s : sHub){
            if(s.getHead() == pos){
                return s.getTail();
            }
        }
        return -1;
    }
}

class Ladder{
    private final int start;
    private final int end;

    Ladder(Pair pair){
        this.start=pair.getKey();
        this.end=pair.getValue();
    }

    public int getStart(){
        return this.start;
    }

    public int getEnd(){
        return this.end;
    }
}

class LadderHub{
    private List<Ladder> lHub = new ArrayList<Ladder>();

    public void addLadder(Ladder ladder){
        lHub.add(ladder);
    }

    public boolean isValidLadder(Pair ladder){
        for(Ladder l : lHub){
            if(l.getStart()==ladder.getKey()){
                return false;
            }
        }
        return true;
    }

    public int isLadder(int pos){
        for(Ladder l:lHub){
            if(l.getStart() == pos){
                return l.getEnd();
            }
        }
        return -1;
    }
}


class Board{
    private SnakeHub snakes = new SnakeHub();
    private LadderHub ladders = new LadderHub();

    Board(){

    }
    public boolean addSnake(Pair pair){
        if(snakes.isValidSnake(pair)){
            snakes.addSnake(new Snake(pair));
            return true;
        }
        return false;
    }

    public boolean addLadder(Pair pair){
        if (ladders.isValidLadder(pair)) {
            ladders.addLadder(new Ladder(pair));
            return true;
        }
        return false;
    }

    public int checkSnake(int pos){
        return snakes.isHead(pos);
    }

    public int checkLadder(int pos){
        return ladders.isLadder(pos);
    }

}


class Player{
    private int position;
    private boolean won;
    String name;
    Player(int position,boolean won, String name){
        this.position=position;
        this.won=won;
        this.name=name;
    }

    public boolean isPlaying(){
        return !won;
    }

    public void won(){
        this.won=true;
    }

    public int getPosition(){
        return position;
    }

    public void setPosition(int x){
        this.position=x;
    }

}


class Game{

    private Board board;
    private int totalPlayers;
    private Vector<Player> playersInfo;

    // initalizing game without external snakes and ladders
    Game(int players, String[] playerList){
        playersInfo=new Vector<Player>();
        totalPlayers=players;
        for(String s:playerList){
            playersInfo.add(new Player(0,false,s));
        }
        board = new Board();

        // adding snakes
        board.addSnake(new Pair(32,10));
        board.addSnake(new Pair(36,6));
        board.addSnake(new Pair(48,26));
        board.addSnake(new Pair(62,18));
        board.addSnake(new Pair(88,24));
        board.addSnake(new Pair(95,56));
        board.addSnake(new Pair(97,78));

        // adding ladders
        board.addLadder(new Pair(4,38));
        board.addLadder(new Pair(8,30));
        board.addLadder(new Pair(19,22));
        board.addLadder(new Pair(21,42));
        board.addLadder(new Pair(28,76));
        board.addLadder(new Pair(50,67));
        board.addLadder(new Pair(71,92));
        board.addLadder(new Pair(80,99));

    }

    public void run(){
        Scanner sc = new Scanner(System.in);
        int chance = 0;
        int playersWon = 0;
        while(playersWon < totalPlayers-1) {
            if (chance >= totalPlayers){
                chance %= totalPlayers;
            }
            if(playersInfo.get(chance).isPlaying()) {
                int c=chance+1;
                System.out.println("Now chance is for player " + c + " : " + playersInfo.get(chance).name);
                int p = 2;
                while (p != 1) {
                    System.out.println("Press 1 for taking a chance...");
                    p = sc.nextInt();
                }
                int r = (int) (Math.random() * 6) + 1;
                System.out.println("You got " + r);
                int pos = playersInfo.get(chance).getPosition();
                if(pos+r < 100){
                    // check for snakes
                    if(board.checkSnake(pos+r)!=-1){
                        System.out.println("Sorry, You have bitten by a snake");
                        playersInfo.get(chance).setPosition(board.checkSnake(pos+r));
                    }
                    else if(board.checkLadder(pos+r)!=-1){
                        System.out.println("Congrats, You just climbed a ladder");
                        playersInfo.get(chance).setPosition(board.checkLadder(pos+r));
                    }
                    else {
                        playersInfo.get(chance).setPosition(pos+r);
                    }
                    System.out.println("Your new position is "+playersInfo.get(chance).getPosition());
                }
                else if(pos+r == 100){
                    playersInfo.get(chance).setPosition(pos+r);
                    playersInfo.get(chance).won();
                    System.out.println("Player "+c+" won.");
                    ++playersWon;
                }
                else{
                    System.out.println("Sorry you can't take this chance");
                }


            }
            ++chance;
        }

    }
}


class Snader{
    public static void main(String[] args) {
        Game game = new Game(2, new String[]{"A", "B"});
        game.run();
    }
}