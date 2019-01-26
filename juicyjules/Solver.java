public class Solver {
    public int[] sudoku;
    public int[][] columns;
    public int[][] rows;
    public int[][] blocks;
    public int[] backup;
    public int[][] blockcode;
    public int missing;
    public int[] missing_numbers;
    public int[] values;
    public int[] backtrack_misses;
    public int bt;
    public boolean finished;
    public int[] finishedSudoku;
    public Solver(String numbers){
        sudoku = stringToNumbers(numbers);
        columns = new int[9][9];
        rows = new int[9][9];
        blocks = new int[9][9];
        backup = new int[81];
        missing = 0;
        backup();
        update();
    }
    public int[] stringToNumbers(String string){
        int counter=0;
        int[]numbers=new int[81];
        for(String number : string.split("")){
            numbers[counter]=Integer.parseInt(number);
            counter++;
        }
        return numbers;
    }
    public void update(){
        get_blocks();
        get_columns();
        get_rows();
        get_missing();
        get_missings();
    }
    private void backup(){
        System.arraycopy(sudoku,0,backup,0,sudoku.length);
    }
    private void reset(){
        System.arraycopy(backup,0,sudoku,0,sudoku.length);
        update();
    }
    public void get_rows(){
        for (int i=0; i<81;i++){
            rows[i / 9][i%9]=sudoku[i];
        }
    }
    public void get_columns(){
        for (int i=0; i<81;i++){
            columns[i % 9][i/9]=sudoku[i];
        }
    }
    public void get_blocks(){
        int counter2=0;
        for (int bx=0; bx<3;bx++) {
            for(int by=0; by<3;by++){
                int counter=0;
                for(int x=0; x<3; x++){
                    for (int y=0;y<3;y++){
                        blocks[counter2][counter]=sudoku[27*bx+3*by+9*x+y];
                        counter++;
                    }
                }
                counter2++;
            }
        }
    }
    public void get_missing(){
        missing=0;
        for(int value :sudoku){
            if(value==0){
                missing+=1;
            }
        }
    }
    public void show(){
        int counter=0;
        String symbol;
        for (int number : sudoku){
            if(counter%9==0){
                System.out.println();
            }
            if (counter%27==0 && counter!=0){
                System.out.println("-----------------------");
            }
            if (counter%3 == 0 && counter%9!=0){
                symbol = " |";
                System.out.print(symbol);
            }
            symbol = " "+Integer.toString(number);
            System.out.print(symbol);
            counter++;
        }
        System.out.println();
    }
    public void createBlockcode(){
        blockcode= new int[9][9];
        for(int i = 0; i<81;i++){
            int row = rowByNumber(i);
            int column = columnByNumber(i);
            int block = blockByRowColumn(row,column);
            int counter=0;
            int block2=0;
            for(int value : blockcode[block]){
                if (value ==0){
                    block2=counter;
                    break;
                }
                counter++;
            }
            blockcode[block][block2] = i;
        }
    }
    public int blockByRowColumn(int row,int column){
        return (row/3)*3+(column/3);
    }
    public int rowByNumber(int number){
        return number/9;
    }
    public int columnByNumber(int number) {
        return number % 9;
    }
    public boolean isFree(int number,int digit){
        update();
        int row = rowByNumber(number);
        int column = columnByNumber(number);
        int block = blockByRowColumn(row,column);
        return (in(rows[row], digit) && in(columns[column], digit) && in(blocks[block], digit));
    }
    public boolean in(int[] list,int number){
        for (int value : list){
            if (value == number) {
                return false;
            }
        }
        return true;
    }
    public void get_missings(){
        get_missing();
        missing_numbers =new int[missing];
        int counter=0;
        for(int i=0;i<81;i++){
            if(sudoku[i]==0){
                missing_numbers[counter]=i;
                counter++;
            }
        }
    }
    public void backtrack() {
        update();
        values =new int[missing];
        values[0]=1;
        bt = 0;
        backtrack_misses=new int[missing_numbers.length];
        System.arraycopy(missing_numbers,0,backtrack_misses,0,missing_numbers.length);
        backup();
        while(true){
            backtrack_routine();
            bt += 1;
            //System.out.println(missing);
            if (missing == 0) {
                break;
            }
            reset();
        }
        //System.out.println("finished with "+Integer.toString(bt));
        //show();
        finished=true;
        finishedSudoku=sudoku;
    }
    public int len(int[] list){
        int length=0;
        for(int value :list){
            if(value>0 && value <10){
                length+=1;
            }
        }
        return length;
    }
    public void backtrack_subroutine() {
        int pointer = 1;
        for (int i = 0; i < backtrack_misses.length; i++) {
            //pointer++;
            try {
                int number = backtrack_misses[i];
                if(i>len(values)){
                    update();
                    break;
                }
                while (values[i] < 10) {
                    if (isFree(number, values[i])) {
                        sudoku[number] = values[i];
                        if (i + 1 == len(values)) {
                            values[pointer] = 1;
                            pointer++;
                        }
                        update();
                        break;
                    } else {
                        update();
                        values[i] += 1;
                    }
                if (values[i] > 9) {
                    values[i] = 0;
                    pointer--;
                    values[i - 1] += 1;
                    if(values[i-1]>9){
                        values[i-1]=0;
                    }
                    i--;
                }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("catch!");
                update();
                break;
            }
        }
    }
    public void backtrack_routine(){
        int number;
        for (int i =0 ; i<backtrack_misses.length;i++){
            try {
                while (values[i] < 10 && values[i] > 0) {
                    if (isFree(backtrack_misses[i], values[i])) {
                        sudoku[backtrack_misses[i]] = values[i];
                        i++;
                        values[i] = 1;
                    } else {
                        values[i] += 1;
                        update();
                    }
                }
                if (values[i] > 9) {
                    values[i] = 0;
                    sudoku[backtrack_misses[i]]=0;
                    i-=2;
                }
            }
            catch(ArrayIndexOutOfBoundsException e){
                update();
                break;
            }
        }
    }
}
