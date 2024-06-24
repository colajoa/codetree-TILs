import java.io.*;
import java.util.*;

public class Main {
    static int N, M, K, pathMin = Integer.MAX_VALUE;
    static int[][] map;
    static boolean[][] visited;
    static int[][][] path;
    static Turret to, from;
    static Queue<int[]> q = new ArrayDeque<>();
    static int[] dr = { 0, 1, 1, 1, 0, -1, -1, -1 };
    static int[] dc = { 1, 1, 0, -1, -1, -1, 0, 1 };
    static LinkedList<Turret> turrets = new LinkedList<>();

    static class Turret {
        int r, c, AD, k;

        public Turret(int r, int c, int AD, int k) {
            this.r = r;
            this.c = c;
            this.AD = AD;
            this.k = k;
        }

    }

    static void bfs() {
        Collections.sort(turrets, new Comparator<Turret>() {
            public int compare(Turret t1, Turret t2) {
                if (t1.AD == t2.AD) {
                    if (t1.k == t2.k) {
                        if ((t2.r + t2.c) == (t1.r + t1.c)) {
                            return t2.c - t1.c;
                        }
                        return (t2.r + t2.c) - (t1.r + t1.c);
                    }
                    return t2.k - t1.k;
                }
                return t1.AD - t2.AD;
            }
        });


        from = turrets.removeFirst();
        to = turrets.removeLast();
        from.AD += (N + M);
        map[from.r][from.c] = from.AD;
        from.k += 1;

        to.AD -= from.AD;

        visited = new boolean[N][M];
        path = new int[N][M][2];
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < M; c++) {
                path[r][c] = new int[] { -1, -1 };
            }
        }

        visited[from.r][from.c] = true;
        q.add(new int[] { from.r, from.c });
        while (!q.isEmpty()) {
            int[] now = q.poll();

            if (now[0] == to.r && now[1] == to.c) {
                map[to.r][to.c] -= from.AD;
                while (true) {
                    int nr = path[now[0]][now[1]][0];
                    int nc = path[now[0]][now[1]][1];

                    if (nr == from.r && nc == from.c) {
                        return;
                    }

                    map[nr][nc] -= (from.AD / 2);
                    now[0] = nr;
                    now[1] = nc;
                }
            }

            for (int d = 0; d < 8; d += 2) {
                int nr = (N + (now[0] + dr[d])) % N;
                int nc = (M + (now[1] + dc[d])) % M;
                if (!visited[nr][nc] && map[nr][nc] > 0) {
                    visited[nr][nc] = true;
                    path[nr][nc] = new int[] { now[0], now[1] };
                    q.add(new int[] { nr, nc });
                }
            }
        }

        for (int d = 0; d < 8; d++) {
            int nr = (N + (to.r + dr[d])) % N;
            int nc = (M + (to.c + dc[d])) % M;
            if (map[nr][nc] == 0 || (nr == from.r && nc == from.c) || (nr == to.r && nc == to.c))
                continue;
            map[nr][nc] -= (from.AD / 2);
        }
        map[to.r][to.c] -= from.AD;
    }

    static void settingTurret() {

        for (int i = turrets.size() - 1; i >= 0; i--) {
            Turret turret = turrets.get(i);

            int r = turret.r;
            int c = turret.c;

            if (map[r][c] == turret.AD) {
                turret.AD += 1;
                map[r][c] = turret.AD;
            } else {
                if (map[r][c] <= 0) {
                    map[r][c] = 0;
                    turrets.remove(i);
                } else {
                    turret.AD = map[r][c];
                }
            }
        }

        turrets.add(from);
        if (map[to.r][to.c] > 0) {
            turrets.add(to);
        } else {
            map[to.r][to.c] = 0;
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer token = new StringTokenizer(br.readLine());

        N = Integer.parseInt(token.nextToken());
        M = Integer.parseInt(token.nextToken());
        K = Integer.parseInt(token.nextToken());

        map = new int[N][M];

        for (int n = 0; n < N; n++) {
            token = new StringTokenizer(br.readLine());
            for (int m = 0; m < M; m++) {
                map[n][m] = Integer.parseInt(token.nextToken());

                if (map[n][m] != 0) {
                    turrets.add(new Turret(n, m, map[n][m], 0));
                }
            }
        }

        for (int k = 0; k < K; k++) {
            if (turrets.size() == 1)
                break;
            bfs();
            settingTurret();
            q.clear();
        }

        int max = -1;
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < M; c++) {
                if (map[r][c] > 0 && max < map[r][c]) {
                    max = map[r][c];
                }
            }
        }

        System.out.println(max);
    }
}