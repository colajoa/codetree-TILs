import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String A = sc.next();
        // Please write your code here.
        int len = A.length();
        int[] alpha = new int[26];
        for(int i = 0; i < len; i++) {
            alpha[A.charAt(i) - 'a'] += 1;
        }

        int cnt = 0;
        for(int i = 0; i < 26; i++) {
            if(alpha[i] > 0) {
                cnt += 1;
            }
        }

        if(cnt > 1) {
            System.out.print("Yes");
        } else {
            System.out.print("No");
        }
    }
}