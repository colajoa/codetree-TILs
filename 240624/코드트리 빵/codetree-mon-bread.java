import java.io.*;
import java.util.*;

public class Main {
    static int N, M, finished, time;
    static int[][] map;
    static boolean[][][] visited;
    static int[][] distance;
    static int[] dr = { -1, 0, 0, 1 };
    static int[] dc = { 0, -1, 1, 0 };
    static Point[] mans;
    static Queue<Point>[] q;

    static class Point {
        int r, c;

        public Point(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }

    static void searchCamp(int time) {
        distance = new int[N][N];
        Point p = mans[time];
        q[0].add(new Point(p.r, p.c));
        distance[p.r][p.c] = 1;
        int min = Integer.MAX_VALUE;
        int mr = -1;
        int mc = -1;
        while (!q[0].isEmpty()) {
            Point now = q[0].poll();

            if (map[now.r][now.c] == 1) {
                if (min == distance[now.r][now.c]) {
                    if (mr == now.r) {
                        if (mc > now.c) {
                            mc = now.c;
                        }
                    } else if (mr > now.r) {
                        mr = now.r;
                    }
                }

                else if (min > distance[now.r][now.c]) {
                    min = distance[now.r][now.c];
                    mr = now.r;
                    mc = now.c;
                }

                continue;
            }

            for (int d = 0; d < 4; d++) {
                int nr = now.r + dr[d];
                int nc = now.c + dc[d];

                if (nr >= 0 && nr < N && nc >= 0 && nc < N && distance[nr][nc] == 0 && map[nr][nc] >= 0) {
                    q[0].add(new Point(nr, nc));
                    distance[nr][nc] = distance[now.r][now.c] + 1;
                }
            }
        }

        map[mr][mc] = -1;
        q[time].add(new Point(mr, mc));
        visited[time][mr][mc] = true;
        q[0].clear();
    }

    static void move(int man) {
        int size = q[man].size();

        for (int i = 0; i < size; i++) {
            Point now = q[man].poll();

            for (int d = 0; d < 4; d++) {
                int nr = now.r + dr[d];
                int nc = now.c + dc[d];

                if (nr >= 0 && nr < N && nc >= 0 && nc < N && !visited[man][nr][nc] && map[nr][nc] >= 0) {
                    q[man].add(new Point(nr, nc));
                    visited[man][nr][nc] = true;
                    if (nr == mans[man].r && nc == mans[man].c) {
                        q[man].clear();
                        map[nr][nc] = -1;
                        finished -= 1;
                        return;
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer token = new StringTokenizer(br.readLine());

        N = Integer.parseInt(token.nextToken());
        M = Integer.parseInt(token.nextToken());

        map = new int[N][N];
        mans = new Point[M + 1];
        q = new ArrayDeque[M + 1];
        visited = new boolean[M + 1][N][N];
        for (int r = 0; r < N; r++) {
            token = new StringTokenizer(br.readLine());
            for (int c = 0; c < N; c++) {
                map[r][c] = Integer.parseInt(token.nextToken());
            }
        }

        q[0] = new ArrayDeque<>();
        for (int m = 1; m < M + 1; m++) {
            token = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(token.nextToken()) - 1;
            int c = Integer.parseInt(token.nextToken()) - 1;

            q[m] = new ArrayDeque<>();
            mans[m] = new Point(r, c);
        }
        finished = M;
        time = 0;

        while (time < M) {
            time++;
            for (int i = 1; i < time; i++) {
                move(i);
            }
            searchCamp(time);
        }
        while (finished != 0) {
            for (int m = 1; m < M + 1; m++) {
                move(m);
            }
            time++;
        }
        System.out.println(time);
    }
}