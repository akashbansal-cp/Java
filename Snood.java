import java.util.*;

enum cell_type {
    EMPTY_CELL,
    SNAKE_CELL,
    FOOD_CELL
}

enum direction{
    UP,
    DOWN,
    LEFT,
    RIGHT
}

class Position{
    private int xCoordinate;
    private int yCoordinate;
    Position(int x,int y){
        this.xCoordinate = x;
        this.yCoordinate = y;
    }

    Position(Position pos){
        this.xCoordinate=pos.x();
        this.yCoordinate=pos.y();
    }

    public int x(){
        return xCoordinate;
    }

    public int y(){
        return yCoordinate;
    }

    public Position updateXBy(int t){
        this.xCoordinate+=t;
        return this;
    }

    public Position updateYBy(int t){
        this.yCoordinate+=t;
        return this;
    }
}

class Cell{
    private cell_type cellType;
    private int cellScore;
    Cell(cell_type cell, int cellScore){
        this.cellType = cell;
        this.cellScore = cellScore;
    }
    public void setCellType(cell_type cellType){
        this.cellType=cellType;
    }
    public cell_type getCellType(){
        return this.cellType;
    }
    public int getCellScore(){
        return this.cellScore;
    }
    public void setCellScore(int cellScore){
        this.cellScore = cellScore;
    }
}

class Snake{
    private final LinkedList<Position> position=new LinkedList<Position>();
    Snake(Position pos){
        this.position.add(pos);
    }

    public Position getHead(){
        return position.peekFirst();
    }

    public void printSnake(){
        position.forEach((p)->{System.out.println(" "+p.x()+" "+p.y());});
    }

    public void move(Position newPos, Board board){
        position.add(0,newPos);
        board.updateCell(newPos,cell_type.SNAKE_CELL,0);
        board.updateCell(position.peekLast(),cell_type.EMPTY_CELL,0);
        position.removeLast();
    }
    public void grow(Position newPos, Board board){
        position.add(0,newPos);
        board.updateCell(newPos,cell_type.SNAKE_CELL,0);

    }
    public void eat(Position newPos,Board board){
        board.updateCell(newPos,cell_type.EMPTY_CELL,0);
    }

}

class Board{
    private final Cell[][] gameBoard;
    private final int row,column;

    Board(int row, int column){
        this.row=row;
        this.column=column;
        this.gameBoard = new Cell[row][column];
        for(int i=0;i<row;++i){
            for(int j=0;j<column;++j){
                this.gameBoard[i][j]=new Cell(cell_type.EMPTY_CELL,0);
            }
        }
    }

    public void generateFood(int score){
        int x,y;
        do{
            // generating the food
            x=(int)(Math.random()*this.row);
            y=(int)(Math.random()*this.column);
        }
        while(gameBoard[x][y].getCellType()!=cell_type.EMPTY_CELL);
        gameBoard[x][y].setCellType(cell_type.FOOD_CELL);
        gameBoard[x][y].setCellScore(score);
        System.out.println(x+" "+y+" "+gameBoard[x][y].getCellScore());
    }

    public void updateCell(Position pos, cell_type cellType, int cellScore){
        gameBoard[pos.x()][pos.y()].setCellType(cellType);
        gameBoard[pos.x()][pos.y()].setCellScore(cellScore);
    }

    public Snake generateNewSnake(){
        int x,y;
        do{
            x=(int)(Math.random()*this.row);
            y=(int)(Math.random()*this.column);
        }
        while(gameBoard[x][y].getCellType()!=cell_type.EMPTY_CELL);
        Snake snake = new Snake(new Position(x,y));
        gameBoard[x][y].setCellType(cell_type.SNAKE_CELL);
        System.out.println(x+" "+y);
        return snake;
    }

    public boolean validate(Position pos){
        return pos.x() < row && pos.x() >= 0 && pos.y() >= 0 && pos.y() < column;
    }

    public cell_type getCellType(Position pos){
        return gameBoard[pos.x()][pos.y()].getCellType();
    }

    public int getCellScore(Position pos){
        return gameBoard[pos.x()][pos.y()].getCellScore();
    }
}

class Game{
    private final Board board;
    Snake snake;
    int score = 0;
    private boolean isGameOver;
    Game(int row, int column, int baseScore){
        // Initializing new Board with given size
        this.board = new Board(row,column);

        // Initializing new snake at a random position
        this.snake = board.generateNewSnake();

        // Generating new Food
        this.board.generateFood(baseScore);

        // setting other variables
        this.isGameOver=false;
    }

    public boolean getIsGameOver(){
        return this.isGameOver;
    }
    public void setIsGameOver(boolean status){
        this.isGameOver = status;
    }

    public void update(direction d){
        Position newPos = new Position(snake.getHead());
        if(d==direction.UP)
            newPos=newPos.updateXBy(-1);
        else if(d==direction.DOWN)
            newPos=newPos.updateXBy(+1);
        else if(d==direction.LEFT)
            newPos=newPos.updateYBy(-1);
        else if(d==direction.RIGHT)
            newPos=newPos.updateYBy(+1);
        boolean validity = board.validate(newPos);
        if(validity){
            cell_type cell = board.getCellType(newPos);
            if(cell == cell_type.EMPTY_CELL){
                snake.move(newPos,board);
            }
            else if(cell == cell_type.FOOD_CELL){
                this.score+=board.getCellScore(newPos);
                snake.eat(newPos,board);
                snake.grow(newPos,board);
                board.generateFood(3);
            }
            else {
                //    #gameover
                isGameOver=true;
            }
        }
        else{
            // logic for colliding with wall
            // currently it just ignores the direction
        }
    }

}

class Execute{
    public void execute(Game game,String directions){
        for(int i=0;i<directions.length();++i){
            if(!game.getIsGameOver()){
                switch (directions.charAt(i)){
                    case 'W':
                        // move up
                        game.update(direction.UP);
                        break;
                    case 'S':
                        // move down
                        game.update(direction.DOWN);
                        break;
                    case 'A':
                        // move left
                        game.update(direction.LEFT);
                        break;
                    case 'D':
                        // move right
                        game.update(direction.RIGHT);
                        break;
                    case 'z':
                        game.setIsGameOver(true);
                        break;
                    case 'q':
                        System.out.println("Snake is at");
                        game.snake.printSnake();
                        System.out.println("Score is "+game.score);
                        break;
                    default:
                        // unknown direction
                        System.out.println("Unknown direction '"+directions.charAt(i)+"' found. Please refer to the rules again.");

                }
            }
            else{
                System.out.println("Game is over at score "+game.score);
                return;
            }
        }
        if(game.getIsGameOver()){
            System.out.println("Game is over at score "+game.score);
        }
    }
}

class Snood{
    public static void main(String[] args) {
        Execute executor = new Execute();
        Game game = new Game(10,10,3);
        Scanner sc = new Scanner(System.in);
        String directions="";
        do{
            directions = sc.next();
            executor.execute(game,directions);
        }
        while(!game.getIsGameOver());
        System.out.println("Exiting, Thanks for playing the game!!!");
    }
}



/*
A few notes:
cell is not assigned position bcoz there is no use.




 */