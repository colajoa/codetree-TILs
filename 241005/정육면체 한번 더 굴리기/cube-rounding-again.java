import java.util.*;
import java.io.*;

public class Main {
    // D : 진행 방향, sr, sc : 주사위 위치
    static int N, M, D = 1, sr = 0, sc = 0;
    static long ans = 0;
    static int[][] board;
    static int[][] dice = new int[4][3];
    static int[] dr = {-1, 0, 1, 0};
    static int[] dc = {0, 1, 0, -1};
    static Queue<Point> q = new ArrayDeque<>();

    static class Point {
        int r, c;

        public Point(int r, int c) {
            this.r = r;
            this.c = c;
        }

    }
    // 주사위 굴리기
    static void moveDice() {
        sr += dr[D];
        sc += dc[D];

        // 보드 범위 밖
        if(sr < 0 || sr >= N || sc < 0 || sc >= N) {
            D = (D + 2) % 4;
            sr += dr[D] * 2;
            sc += dc[D] * 2;
        }
        // 상
        if(D == 0) {
            int flag = dice[0][1];

            for(int i = 0; i < 3; i++) {
                dice[i][1] = dice[i + 1][1];
            }
            dice[3][1] = flag;
        }
        // 우
        else if(D == 1) {
            int flag = dice[3][1];
            dice[3][1] = dice[1][2];

            for(int i = 2; i > 0; i--) {
                dice[1][i] = dice[1][i - 1];
            }

            dice[1][0] = flag;
        } 
        // 하
        else if(D == 2) {
            int flag = dice[3][1];

            for(int i = 3; i > 0; i--) {
                dice[i][1] = dice[i - 1][1];
            }

            dice[0][1] = flag;
        } 
        // 좌 
        else if(D == 3) {
            int flag = dice[3][1];
            dice[3][1] = dice[1][0];

            for(int i = 0; i < 2; i++) {
                dice[1][i] = dice[1][i + 1];
            }

            dice[1][2] = flag;
        }

        moveDir();
    }

    // 진행 방향 바꾸기
    static void moveDir() {
        // 주사위 밑면이 더 크다 시계 방향
        if(board[sr][sc] > dice[3][1]) {
            D = (D + 1) % 4;
        } 
        // 주사위 밑면이 더 작다 반시계 방향
        else if(board[sr][sc] < dice[3][1]) {
            D = (4 + (D - 1)) % 4;
        }
    }

    // 점수 얻기
    static void bfs() {
        boolean[][] visited = new boolean[N][N];

        q.add(new Point(sr, sc));
        visited[sr][sc] = true;
        int point = board[sr][sc];

        while(!q.isEmpty()) {
            Point now = q.poll();
            ans += point;

            for(int d = 0; d < 4; d++) {
                int nr = now.r + dr[d];
                int nc = now.c + dc[d];

                if(nr >= 0 && nr < N && nc >= 0 && nc < N && !visited[nr][nc] && board[now.r][now.c] == board[nr][nc]) {
                    q.add(new Point(nr, nc));
                    visited[nr][nc] = true;
                }    
            }
        }
    }

    public static void main(String[] args) throws Exception{
        // 여기에 코드를 작성해주세요.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer token = new StringTokenizer(br.readLine());

        N = Integer.parseInt(token.nextToken());
        M = Integer.parseInt(token.nextToken());

        board = new int[N][N];
        // 주사위 세팅
        dice[0][1] = 5;
        dice[1][0] = 4;
        dice[1][1] = 1;
        dice[1][2] = 3;
        dice[2][1] = 2;
        dice[3][1] = 6;

        // 보드 입력
        for(int r = 0; r < N; r++) {
            token = new StringTokenizer(br.readLine());
            for(int c = 0; c < N; c++) {
                board[r][c] = Integer.parseInt(token.nextToken());
            }
        }

        // 주사위 굴리기
        for(int m = 0; m < M; m++) {
            moveDice();
            bfs();
        }

        System.out.println(ans);
    }
}