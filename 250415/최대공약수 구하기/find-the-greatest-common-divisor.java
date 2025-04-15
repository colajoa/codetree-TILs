import java.util.*;
import java.io.*;

public class Main {
    static int GCD(int n, int m) {
        if(n % m == 0) {
            return m;
        }
        return GCD(m, n % m);
    }
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer token = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(token.nextToken());
        int m = Integer.parseInt(token.nextToken());

        // Please write your code here.
    
        System.out.print(GCD(n,m));
    }
}