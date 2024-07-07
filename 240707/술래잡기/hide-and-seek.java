import java.io.*;
import java.util.*;

public class Main {
    static int[] dr = { -1, 0, 1, 0 };
    static int[] dc = { 0, 1, 0, -1 };
    static char[] way = { '^', '>', 'V', '<' };
    static int[][] tree;
    static int N, M, H, K, count = 1, dis = 0;
    static boolean flag = true;
    static long score = 0;
    static Player[] players;
    static int[] moveCount;
    static List<Integer>[][] map;
    static char[][] path;

    static class Player {
        int r, c, dir;

        public Player(int r, int c, int dir) {
            this.r = r;
            this.c = c;
            this.dir = dir;
        }
    }

    static void move() {
        Player seeker = players[0];

        for (int m = 1; m < M + 1; m++) {
            Player hider = players[m];

            if (hider.dir != -1) {
                int distance = Math.abs(seeker.r - hider.r) + Math.abs(seeker.c - hider.c);

                if (distance <= 3) {
                    int nr = hider.r + dr[hider.dir];
                    int nc = hider.c + dc[hider.dir];

                    if (nr >= 0 && nr < N && nc >= 0 && nc < N) {
                        if (!(nr == seeker.r && nc == seeker.c)) {
                            map[hider.r][hider.c].remove(new Integer(m));
                            map[nr][nc].add(m);
                            hider.r = nr;
                            hider.c = nc;
                        }
                    } else {
                        hider.dir = (hider.dir + 2) % 4;
                        nr = hider.r + dr[hider.dir];
                        nc = hider.c + dc[hider.dir];
                        if (!(nr == seeker.r && nc == seeker.c)) {
                            map[hider.r][hider.c].remove(new Integer(m));
                            map[nr][nc].add(m);
                            hider.r = nr;
                            hider.c = nc;
                        }
                    }
                }
            }
        }
    }

    static int catchHider() {
        Player seeker = players[0];
        // 한칸 이동
        dis += 1;
        path[seeker.r][seeker.c] = way[seeker.dir];
        seeker.r += dr[seeker.dir];
        seeker.c += dc[seeker.dir];
        // 방향 바꾸기
        if ((seeker.r == 0 && seeker.c == 0) || (seeker.r == (N / 2) && seeker.c == (N / 2))) {
            seeker.dir = (seeker.dir + 2) % 4;
            flag = !flag;
            moveCount[count] += 1;
            dis = 0;
        } else if (count == dis) {
            seeker.dir = flag ? (seeker.dir + 1) % 4 : (4 + (seeker.dir - 1)) % 4;
            moveCount[count] += 1;
            dis = 0;
            if (count != N - 1 && moveCount[count] > 0 && moveCount[count] % 2 == 0) {
                if (flag) {
                    count += 1;
                } else if (count != 1) {
                    count -= 1;

                }
            } else if (count == N - 1 && moveCount[count] > 0 && moveCount[count] % 6 == 0 && !flag) {
                count -= 1;

            }
        }

        int get = 0;
        for (int d = 0; d < 3; d++) {
            int nr = seeker.r + (dr[seeker.dir] * d);
            int nc = seeker.c + (dc[seeker.dir] * d);
            if (nr >= 0 && nr < N && nc >= 0 && nc < N && tree[nr][nc] == 0 && map[nr][nc].size() > 0) {
                for (int i = map[nr][nc].size() - 1; i > -1; i--) {
                    players[map[nr][nc].remove(i)].dir = -1;
                    get += 1;
                }
            }
        }

        return get;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer token = new StringTokenizer(br.readLine());

        N = Integer.parseInt(token.nextToken());
        M = Integer.parseInt(token.nextToken());
        H = Integer.parseInt(token.nextToken());
        K = Integer.parseInt(token.nextToken());

        tree = new int[N][N];
        path = new char[N][N];
        map = new ArrayList[N][N];
        moveCount = new int[N];
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                map[r][c] = new ArrayList<>();
            }
        }
        players = new Player[M + 1];

        players[0] = new Player(N / 2, N / 2, 0);

        for (int m = 1; m < M + 1; m++) {
            token = new StringTokenizer(br.readLine());

            int r = Integer.parseInt(token.nextToken()) - 1;
            int c = Integer.parseInt(token.nextToken()) - 1;
            int d = Integer.parseInt(token.nextToken());

            map[r][c].add(m);
            players[m] = new Player(r, c, d);
        }

        for (int h = 0; h < H; h++) {
            token = new StringTokenizer(br.readLine());

            int r = Integer.parseInt(token.nextToken()) - 1;
            int c = Integer.parseInt(token.nextToken()) - 1;
            tree[r][c] = 1;

        }

        for (int k = 1; k < K + 1; k++) {
            move();
            score += k * catchHider();
        }

        System.out.println(score);
    }
}