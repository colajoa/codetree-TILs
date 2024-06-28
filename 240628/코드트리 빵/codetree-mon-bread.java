import java.io.*;
import java.util.*;

public class Main {
    static int N, M, finished, time, ans;
    static int[][] map;
    static int[][] distance;
    static int[] dr = { -1, 0, 0, 1 };
    static int[] dc = { 0, -1, 1, 0 };
    static Point[][] mans;
    static Queue<Point>[] q;
    static List<Point> conviList = new ArrayList<>();
    static PriorityQueue<Point> pq = new PriorityQueue<>();

    static class Point implements Comparable<Point> {
        int r, c;

        public Point(int r, int c) {
            this.r = r;
            this.c = c;
        }

        public int compareTo(Point o) {
            if (this.r == o.r) {
                return this.c - o.c;
            }
            return this.r - o.r;
        }
    }

    static void searchCamp(int time) {
        distance = new int[N][N];
        Point p = mans[time][0];
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
        q[time].add(new Point(p.r, p.c));
        mans[time][1] = new Point(mr, mc);
        q[0].clear();
    }

    static void move(int man) {
        distance = new int[N][N];

        // 편의점 좌표
        int destR = mans[man][0].r;
        int destC = mans[man][0].c;

        // 탐색을 위해 편의점 방문 처리
        distance[destR][destC] = 1;

        while (!q[man].isEmpty()) {
            Point now = q[man].poll();
            for (int d = 3; d > -1; d--) {
                int nr = now.r + dr[d];
                int nc = now.c + dc[d];

                if (nr >= 0 && nr < N && nc >= 0 && nc < N && map[nr][nc] >= 0 && distance[nr][nc] == 0) {
                    distance[nr][nc] = distance[now.r][now.c] + 1;
                    q[man].add(new Point(nr, nc));
                }
            }
        }

        int min = Integer.MAX_VALUE;
        Point p = mans[man][1];

        for (int d = 0; d < 4; d++) {
            int r = p.r + dr[d];
            int c = p.c + dc[d];

            if (r >= 0 && r < N && c >= 0 && c < N && distance[r][c] > 0) {
                if (min > distance[r][c]) {
                    min = distance[r][c];
                    pq.clear();
                    pq.add(new Point(r, c));
                } else if (min == distance[r][c]) {
                    pq.add(new Point(r, c));
                }
            }
        }

        Point next = pq.poll();
        mans[man][1].r = next.r;
        mans[man][1].c = next.c;
        if (next.r == destR && next.c == destC) {
            conviList.add(next);
        } else {
            q[man].add(new Point(destR, destC));
        }
        pq.clear();
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer token = new StringTokenizer(br.readLine());

        N = Integer.parseInt(token.nextToken());
        M = Integer.parseInt(token.nextToken());

        map = new int[N][N];
        mans = new Point[M + 1][2];
        q = new ArrayDeque[M + 1];
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
            // 편의점 좌표
            mans[m][0] = new Point(r, c);
        }
        finished = M;
        time = 1;

        while (finished > 0) {

            // 편의점 이동
            for (int m = 1; m < M + 1; m++) {
                if (q[m].size() != 0) {
                    move(m);
                }
            }
            // 편의점 도착
            if (conviList.size() > 0) {
                for (Point covi : conviList) {
                    map[covi.r][covi.c] = -1;
                    finished -= 1;
                }
                conviList.clear();
            }

            // 베이스 캠프 이동
            if (time <= M) {
                searchCamp(time);
            }
            time++;
        }

        System.out.println((time - 1));
    }
}