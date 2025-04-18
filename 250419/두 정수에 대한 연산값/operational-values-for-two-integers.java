import java.util.Scanner;

public class Main {

    static void func(int[] arr) {
        if(arr[0] > arr[1]) {
            arr[0] += 25;
            arr[1] *= 2;
        } else {
            arr[0] *= 2;
            arr[1] += 25;
        }
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();
        int b = sc.nextInt();
        // Please write your code here.
        int[] arr = {a, b};

        func(arr);
        System.out.print(arr[0] + " " + arr[1]);
    }
}