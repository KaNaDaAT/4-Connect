package ki;
public class Board {
    public byte[][] board = new byte[6][7];
    
    public Board(){
        board = new byte[][]{
            {0,0,0,0,0,0,0,},
            {0,0,0,0,0,0,0,},
            {0,0,0,0,0,0,0,},
            {0,0,0,0,0,0,0,},
            {0,0,0,0,0,0,0,},
            {0,0,0,0,0,0,0,},    
        };
    } 
    
    public boolean isLegalMove(int column){
    	try {
    		return board[0][column] == 0;
    	} catch (Exception e) {
    		System.out.println("Column:" + column);
    		return false;
    	}
    }
    
    //Placing a Move on the board
    public boolean placeMove(int column, int player){ 
        if(!isLegalMove(column)) { 
        	return false;
        }
        for(int i=5;i>=0;--i){
            if(board[i][column] == 0) {
                board[i][column] = (byte)player;
                return true;
            }
        }
        return false;
    }
    
    public void undoMove(int column){
        for(int i=0;i<=5;++i){
            if(board[i][column] != 0) {
                board[i][column] = 0;
                break;
            }
        }        
    }
    //Printing the board
    public void displayBoard(){
        System.out.println();
        for(int i=0;i<=5;++i){
            for(int j=0;j<=6;++j){
            	if (board[i][j] == 0) {
            		System.out.print("- ");
            	} else if (board[i][j] == 1) {
            		System.out.print("X ");
            	} else if (board[i][j] == 2) {
            		System.out.print("O ");
            	}
            }
            System.out.println();
        }
        System.out.println();
    }
}