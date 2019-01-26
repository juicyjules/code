
/**
 * Created by julian on 01.08.17.
 */
public class Main{
        public static void main(String [] args) {
        		String sdk;
        		if(args.length==0){
        			sdk="090080000005000002000301400600000050000030000500247000479000000100600700000000210";
        		}else{
        			sdk = args[0];
        		}
                Solver solver = new Solver(sdk);
                solver.backtrack();
                solver.show();
        }
}