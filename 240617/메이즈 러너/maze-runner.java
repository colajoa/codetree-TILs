import java.io.*;
import java.util.*;

public class Main {
    static int N, M, K, exitR, exitC, moveCount = 0, size, sR, sC, exitCount;
    static int[] dr = { -1, 1, 0, 0 };
    static int[] dc = { 0, 0, -1, 1 };
    static int[][] walls;
    static boolean[][] pos;
    static Player[] players;
    static List<Integer>[][] map;

    static class Player {
        int r, c;
        boolean isExit;

        public Player(int r, int c, boolean isExit) {
            this.r = r;
            this.c = c;
            this.isExit = isExit;
        }
    }

    // 이동
    static void move() {
        for (int m = 1; m < M + 1; m++) {
            if (!players[m].isExit) {
                int r = players[m].r;
                int c = players[m].c;

                for (int d = 0; d < 4; d++) {
                    int nr = r + dr[d];
                    int nc = c + dc[d];

                    if (nr >= 0 && nr < N && nc >= 0 && nc < N && walls[nr][nc] <= 0) {
                        if (Math.abs(r - exitR) + Math.abs(c - exitC) > Math.abs(nr - exitR) + Math.abs(nc - exitC)) {
                            map[r][c].remove(new Integer(-m));
                            map[nr][nc].add(-m);
                            moveCount += 1;
                            
                            if (map[r][c].size() == 0) {
                                walls[r][c] = 0;
                            }
                            if (walls[nr][nc] == -2) {
                                players[m].isExit = true;
                                map[nr][nc].remove(new Integer(-m));
                                exitCount -= 1;
                                break;
                            }
                            walls[nr][nc] = -1;
                            players[m].r = nr;
                            players[m].c = nc;

                            break;
                        }
                    }
                }
            }
        }
    }

    // 정사각형 찾기
    static void searchSquare() {
        size = 0;
        sR = 0;
        sC = 0;
        for (int d = 1; d < N; d++) {
            int nr = 0;
            int nc = 0;
            for (int r = 0; r < N - d; r++) {
                for (int c = 0; c < N - d; c++) {
                    nr = r + d;
                    nc = c + d;
                    for (int m = 1; m < M + 1; m++) {
                        Player player = players[m];
                        if (!player.isExit && exitR >= r && exitR <= nr && exitC >= c && exitC <= nc && player.r >= r
                                && player.r <= nr
                                && player.c >= c && player.c <= nc) {
                            size = d + 1;
                            sR = r;
                            sC = c;
                            return;
                        }
                    }
                }
            }
        }
    }

    // 시계방향 90도 회전
    // 여기 돌아가는 문제
    static void rotate() {
        int[][] copy = new int[size][size];
        boolean[] moved = new boolean[M + 1];
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                copy[r][c] = walls[size - 1 - c + sR][r + sC];

                if (walls[size - 1 - c + sR][r + sC] == -1) {
                    for (int m = 1; m < M + 1; m++) {

                        if (players[m].r == size - 1 - c + sR && players[m].c == r + sC && !moved[m]) {
                            map[size - 1 - c + sR][r + sC].remove(new Integer(-m));
                            moved[m] = true;
                            int nr = r + sR;
                            int nc = c + sC;
                            map[nr][nc].add(-m);
                            players[m].r = nr;
                            players[m].c = nc;
                        }
                    }
                }
            }
        }

        for (int r = sR; r < sR + size; r++) {
            for (int c = sC; c < sC + size; c++) {
                walls[r][c] = copy[r - sR][c - sC];

                if (walls[r][c] > 0) {
                    walls[r][c] -= 1;
                } else if (walls[r][c] == -2) {
                    exitR = r;
                    exitC = c;
                }
            }
        }
    }

    static void print() {
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                System.out.print(walls[r][c] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer token = new StringTokenizer(br.readLine());

        N = Integer.parseInt(token.nextToken());
        M = Integer.parseInt(token.nextToken());
        K = Integer.parseInt(token.nextToken());

        map = new ArrayList[N][N];
        walls = new int[N][N];
        pos = new boolean[N][N];
        players = new Player[M + 1];
        for (int r = 0; r < N; r++) {
            token = new StringTokenizer(br.readLine());
            for (int c = 0; c < N; c++) {
                int wall = Integer.parseInt(token.nextToken());
                map[r][c] = new ArrayList<>();
                walls[r][c] = wall;
            }
        }

        exitCount = M;
        for (int m = 1; m < M + 1; m++) {
            token = new StringTokenizer(br.readLine());

            int r = Integer.parseInt(token.nextToken()) - 1;
            int c = Integer.parseInt(token.nextToken()) - 1;

            walls[r][c] = -1;
            map[r][c].add(-m);
            players[m] = new Player(r, c, false);
        }

        token = new StringTokenizer(br.readLine());

        exitR = Integer.parseInt(token.nextToken()) - 1;
        exitC = Integer.parseInt(token.nextToken()) - 1;
        walls[exitR][exitC] = -2;
        for (int k = 0; k < K; k++) {
            move();
            searchSquare();
            rotate();
            if (exitCount == 0) {
                break;
            }
        }

        System.out.println(moveCount);
        System.out.println((exitR + 1) + " " + (exitC + 1));
    }
}