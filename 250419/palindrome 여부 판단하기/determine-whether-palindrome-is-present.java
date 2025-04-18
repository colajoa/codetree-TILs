import java.util.Scanner;
public class Main {
    static void func(String str) {
        int len = str.length();
        for(int i = 0; i < Math.round(len / 2); i++) {
            if(str.charAt(i) != str.charAt((len - 1) - i)) {
                System.out.print("No");
                return;
            }
        }
        System.out.print("Yes");
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.next();
        // Please write your code here.

        func(input);
    }
}