import java.io.*;
import java.util.*;

public class Main {
    static int L, N, Q;
    static int[][][] map;
    static int[] dr = { -1, 0, 1, 0 };
    static int[] dc = { 0, 1, 0, -1 };
    static Knight[] knights;
    static boolean[] visited;
    static Queue<Integer> knightsQ = new ArrayDeque<>();

    static class Knight {
        int r, c, h, w, k, damage;

        public Knight(int r, int c, int h, int w, int k, int damage) {
            this.r = r;
            this.c = c;
            this.h = h;
            this.w = w;
            this.k = k;
            this.damage = damage;
        }
    }

    static boolean checkHitWall(int k, int d) {
        visited[k] = true;
        knightsQ.add(k);

        while (!knightsQ.isEmpty()) {
            int now = knightsQ.poll();
            int r = knights[now].r;
            int c = knights[now].c;

            int to = (d == 1 || d == 3) ? knights[now].h : knights[now].w;

            // 좌우 이동
            if (d == 1 || d == 3) {
                if (d == 1) {
                    c += (knights[now].w - 1);
                }

                for (int from = 0; from < to; from++) {
                    int nr = r + from;
                    int nc = c + dc[d];

                    if (nr < 1 || nr > L || nc < 1 || nc > L || map[0][nr][nc] == 2) {
                        return false;
                    } else if (nr >= 1 && nr <= L && nc >= 1 && nc <= L && map[1][nr][nc] > 0
                            && !visited[map[1][nr][nc]]) {
                        visited[map[1][nr][nc]] = true;
                        knightsQ.add(map[1][nr][nc]);
                    }
                }
            }

            // 상하 이동
            else if (d == 0 || d == 2) {
                if (d == 2) {
                    r += (knights[now].h - 1);
                }

                for (int from = 0; from < to; from++) {
                    int nr = r + dr[d];
                    int nc = c + from;

                    if (nr < 1 || nr > L || nc < 1 || nc > L || map[0][nr][nc] == 2) {
                        return false;
                    } else if (nr >= 1 && nr <= L && nc >= 1 && nc <= L && map[1][nr][nc] > 0
                            && !visited[map[1][nr][nc]]) {
                        visited[map[1][nr][nc]] = true;
                        knightsQ.add(map[1][nr][nc]);
                    }
                }
            }
        }

        return true;
    }

    // 상하좌우로 이동후 이전 위치 지우기
    static void move(int d) {
        for (int i = 1; i < N + 1; i++) {
            if (visited[i]) {
                // 좌상단 좌표
                int sR = knights[i].r;
                int sC = knights[i].c;
                // 우하단 좌표
                int eR = knights[i].r + (knights[i].h - 1);
                int eC = knights[i].c + (knights[i].w - 1);
                int nr = sR;
                int nc = sC;
                int to = (d == 1 || d == 3) ? knights[i].h : knights[i].w;

                // 상하
                if (d == 0 || d == 2) {
                    // 밀기 && 지우기
                    if (d == 2) {
                        nr = eR;
                    }
                    nr += dr[d];
                    for (int from = 0; from < to; from++) {
                        map[1][nr][nc + from] = i;
                        if (map[1][nr - (dr[d] * knights[i].h)][nc + from] == i) {
                            map[1][nr - (dr[d] * knights[i].h)][nc + from] = 0;
                        }
                    }
                }

                // 좌우
                else if (d == 1 || d == 3) {
                    // 밀기 && 지우기
                    if (d == 1) {
                        nc = eC;
                    }
                    nc += dc[d];
                    for (int from = 0; from < to; from++) {
                        map[1][nr + from][nc] = i;
                        if (map[1][nr + from][nc - (dc[d] * knights[i].w)] == i) {
                            map[1][nr + from][nc - (dc[d] * knights[i].w)] = 0;

                        }
                    }
                }

                knights[i].r = sR + dr[d];
                knights[i].c = sC + dc[d];

            }
        }
    }

    static void damage(int k) {
        for (int i = 1; i < N + 1; i++) {
            if (visited[i] && i != k) {
                int trapCount = 0;
                for (int r = knights[i].r; r < knights[i].r + knights[i].h; r++) {
                    for (int c = knights[i].c; c < knights[i].c + knights[i].w; c++) {
                        if (map[0][r][c] == 1 && map[1][r][c] == i) {
                            trapCount += 1;
                        }
                    }
                }

                knights[i].damage += trapCount;
                if (knights[i].damage >= knights[i].k) {
                    for (int r = knights[i].r; r < knights[i].r + knights[i].h; r++) {
                        for (int c = knights[i].c; c < knights[i].c + knights[i].w; c++) {
                            map[1][r][c] = 0;
                        }
                    }
                }
            }
        }

    }

    // 4 3 1
    // 0 0 1 0
    // 0 0 1 0
    // 1 1 0 1
    // 0 0 2 0
    // 1 2 2 1 5
    // 2 1 2 1 1
    // 3 2 1 2 3
    // 1 2
    // 2 1
    // 3 3

    static void print() {
        for (int r = 1; r < L + 1; r++) {
            for (int c = 1; c < L + 1; c++) {
                System.out.print(map[1][r][c] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer token = new StringTokenizer(br.readLine());

        L = Integer.parseInt(token.nextToken());
        N = Integer.parseInt(token.nextToken());
        Q = Integer.parseInt(token.nextToken());
        map = new int[2][L + 1][L + 1];

        for (int r = 1; r < L + 1; r++) {
            token = new StringTokenizer(br.readLine());
            for (int c = 1; c < L + 1; c++) {
                map[0][r][c] = Integer.parseInt(token.nextToken());
            }
        }

        knights = new Knight[N + 1];

        for (int n = 1; n < N + 1; n++) {
            token = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(token.nextToken());
            int c = Integer.parseInt(token.nextToken());
            int h = Integer.parseInt(token.nextToken());
            int w = Integer.parseInt(token.nextToken());
            int k = Integer.parseInt(token.nextToken());

            knights[n] = new Knight(r, c, h, w, k, 0);

            for (int nr = r; nr < r + h; nr++) {
                for (int nc = c; nc < c + w; nc++) {
                    map[1][nr][nc] = n;
                }
            }
        }

        for (int q = 0; q < Q; q++) {
            token = new StringTokenizer(br.readLine());

            int k = Integer.parseInt(token.nextToken());
            int d = Integer.parseInt(token.nextToken());

            visited = new boolean[N + 1];

            if (knights[k].damage >= knights[k].k) {
                continue;
            }
            if (!checkHitWall(k, d)) {
                knightsQ.clear();
                continue;
            } else {
                move(d);
                damage(k);
            }
        }

        int ans = 0;
        for (int i = 1; i < N + 1; i++) {
            if (knights[i].damage < knights[i].k) {
                ans += knights[i].damage;
            }
        }

        System.out.println(ans);
    }
}