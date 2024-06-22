import java.io.*;
import java.util.*;

public class Main {
    static int N, M, K, pathMin = Integer.MAX_VALUE;
    static int[][] map;
    static boolean[][] visited;
    static int[][][] path;
    static Queue<int[]> q = new ArrayDeque<>();
    static int[] dr = { 0, 1, 1, 1, 0, -1, -1, -1 };
    static int[] dc = { 1, 1, 0, -1, -1, -1, 0, 1 };
    static PriorityQueue<Turret> attacks = new PriorityQueue<>(new Comparator<Turret>() {
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
    static PriorityQueue<Turret> defenses = new PriorityQueue<>(new Comparator<Turret>() {
        public int compare(Turret t1, Turret t2) {
            if (t1.AD == t2.AD) {
                if (t1.k == t2.k) {
                    if ((t1.r + t1.c) == (t2.r + t2.c)) {
                        return t1.c - t2.c;
                    }
                    return (t1.r + t1.c) - (t2.r + t2.c);
                }
                return t1.k - t2.k;
            }
            return t2.AD - t1.AD;
        }

    });

    static class Turret {
        int r, c, AD, k;

        public Turret(int r, int c, int AD, int k) {
            this.r = r;
            this.c = c;
            this.AD = AD;
            this.k = k;
        }

    }

    // 최단 거리 경로 저장 이용
    // 공격
    static void bfs() {
        visited = new boolean[N][M];
        path = new int[N][M][2];

        Turret attack = attacks.poll();
        attack.AD += (N + M);
        map[attack.r][attack.c] = attack.AD;
        int damage = attack.AD;
        attack.k += 1;
        defenses.addAll(attacks);
        attacks.clear();
        attacks.add(new Turret(attack.r, attack.c, attack.AD, attack.k));
        Turret defense = defenses.poll();
        if (defense.AD > damage) {
            attacks.add(new Turret(defense.r, defense.c, defense.AD - damage, defense.k));
        }

        visited[attack.r][attack.c] = true;

        q.add(new int[] { attack.r, attack.c });
        while (!q.isEmpty()) {
            int[] now = q.poll();

            if (now[0] == defense.r && now[1] == defense.c) {

                map[now[0]][now[1]] -= damage;
                while (true) {
                    int nr = path[now[0]][now[1]][0];
                    int nc = path[now[0]][now[1]][1];

                    if (nr == attack.r && nc == attack.c) {
                        return;
                    }

                    map[nr][nc] -= damage / 2;
                    now[0] = nr;
                    now[1] = nc;
                }
            }

            for (int d = 0; d < 8; d += 2) {
                int nr = (N + (now[0] + dr[d])) % N;
                int nc = (M + (now[1] + dc[d])) % M;

                if (nr >= 0 && nr < N && nc >= 0 && nc < M && !visited[nr][nc] && map[nr][nc] > 0) {
                    visited[nr][nc] = true;
                    path[nr][nc] = new int[] { now[0], now[1] };
                    q.add(new int[] { nr, nc });
                }
            }
        }

        map[defense.r][defense.c] -= damage;

        for (int d = 0; d < 8; d++) {
            int nr = (N + (defense.r + dr[d])) % N;
            int nc = (M + (defense.c + dc[d])) % M;

            if (map[nr][nc] > 0) {
                map[nr][nc] -= damage / 2;
            }
        }

        return;
    }

    // 포탑 저장
    static void settingTurret() {
        while (!defenses.isEmpty()) {
            Turret now = defenses.poll();

            int r = now.r;
            int c = now.c;

            if (map[r][c] <= 0) {
                continue;
            }

            if (map[r][c] == now.AD) {
                map[r][c] += 1;
                now.AD += 1;
            } else if (map[r][c] > 0 && map[r][c] != now.AD) {
                now.AD = map[r][c];
            }

            attacks.add(now);
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
                    attacks.add(new Turret(n, m, map[n][m], 0));
                }
            }
        }

        for (int k = 0; k < K; k++) {
            bfs();
            settingTurret();
            q.clear();
        }

        int size = attacks.size() - 1;
        for (int i = 0; i < size; i++) {
            attacks.poll();
        }

        if (attacks.size() == 0) {
            System.out.println(0);
        } else {
            System.out.println(attacks.poll().AD);
        }

    }
}