import java.util.Scanner;
public class Main {
    static void div(int n, int[] arr) {
        for(int i = 0; i < n; i++) {
            if(arr[i] % 2 == 0) {
                arr[i] /= 2;
            }
            System.out.print(arr[i] + " ");
        }
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] arr = new int[n];
        for(int i = 0; i < n; i++)
            arr[i] = sc.nextInt();
        // Please write your code here.
        div(n, arr);
    }
}