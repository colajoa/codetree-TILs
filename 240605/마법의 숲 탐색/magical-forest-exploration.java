import java.io.*;
import java.util.*;

public class Main {
    static int R, C, K, ans = 0, mR, mC, exit, max, num = 0;
    static int[] dr = { -1, 0, 1, 0 };
    static int[] dc = { 0, 1, 0, -1 };
    static int[][] map;
    static Queue<int[]> q = new ArrayDeque<>();

    static void print() {
        for (int r = 0; r < R; r++) {
            for (int c = 0; c < C; c++) {
                System.out.print(map[r][c] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    static boolean move() {
        // 아래 이동
        if (mR + 2 < R) {
            if (map[mR + 1][mC - 1] == 0 && map[mR + 1][mC + 1] == 0 && map[mR + 2][mC] == 0) {
                mR = mR + 1;
                return true;
            }

            // 왼쪽 이동
            else if (mC - 2 >= 0 && map[mR - 1][mC - 1] == 0 && map[mR][mC - 2] == 0 && map[mR + 1][mC - 1] == 0
                    && map[mR + 1][mC - 2] == 0
                    && map[mR + 2][mC - 1] == 0) {
                mR = mR + 1;
                mC = mC - 1;
                exit = (4 + (exit - 1)) % 4;
                return true;
            }

            // 오른쪽 이동
            else if (mC + 2 < C && map[mR - 1][mC + 1] == 0 && map[mR][mC + 2] == 0 && map[mR + 1][mC + 1] == 0
                    && map[mR + 1][mC + 2] == 0
                    && map[mR + 2][mC + 1] == 0) {

                mR = mR + 1;
                mC = mC + 1;
                exit = (exit + 1) % 4;
                return true;
            }
        }

        return false;
    }

    static boolean check() {
        if (mR < 4 || mC <= 0 || mC >= C) {
            return false;
        }
        return true;
    }

    static void mapClear() {
        for (int r = 0; r < R; r++) {
            Arrays.fill(map[r], 0);
        }
    }

    static void bfs() {
        boolean[][] visited = new boolean[R][C];
        q.add(new int[] { mR, mC });
        visited[mR][mC] = true;
        max = mR;

        while (!q.isEmpty()) {
            int[] now = q.poll();

            if (max < now[0]) {
                max = now[0];
            }

            for (int d = 0; d < 4; d++) {
                int nr = now[0] + dr[d];
                int nc = now[1] + dc[d];

                if (nr >= 3 && nr < R && nc >= 0 && nc < C && !visited[nr][nc] && map[nr][nc] != 0) {
                    if (map[nr][nc] == map[now[0]][now[1]] || map[nr][nc] == -map[now[0]][now[1]]) {
                        visited[nr][nc] = true;
                        q.add(new int[] { nr, nc });
                    } else if (map[now[0]][now[1]] < 0 && map[nr][nc] != map[now[0]][now[1]]) {
                        visited[nr][nc] = true;
                        q.add(new int[] { nr, nc });
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer token = new StringTokenizer(br.readLine());

        R = Integer.parseInt(token.nextToken());
        C = Integer.parseInt(token.nextToken());
        K = Integer.parseInt(token.nextToken());

        R = R + 3;
        map = new int[R][C];
        for (int k = 0; k < K; k++) {
            token = new StringTokenizer(br.readLine());
            num = num + 1;
            mR = 1;
            mC = Integer.parseInt(token.nextToken()) - 1;
            exit = Integer.parseInt(token.nextToken());

            while (move()) {
            }

            map[mR][mC] = num;
            map[mR + 1][mC] = num;
            map[mR - 1][mC] = num;
            map[mR][mC + 1] = num;
            map[mR][mC - 1] = num;
            map[mR + dr[exit]][mC + dc[exit]] = -num;

            if (check()) {
                q.clear();
                bfs();
                ans += max - 2;
            } else {
                mapClear();
            }
        }

        System.out.println(ans);
    }
}