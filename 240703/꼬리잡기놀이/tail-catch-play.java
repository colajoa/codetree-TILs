import java.io.*;
import java.util.*;

public class Main {
    static int N, M, K, sr = 0, sc = 0;
    static int[] dr = { -1, 0, 1, 0 };
    static int[] dc = { 0, 1, 0, -1 };
    static int[] tr = { 1, -1, -1, 1 };
    static int[] tc = { 1, 1, -1, -1 };
    static int[][] map;
    static int[][] path;
    static boolean[][] visited;
    static long score;
    static Queue<Point> heads = new ArrayDeque<>();
    static Queue<Point> q = new ArrayDeque<>();
    static LinkedList<Point>[] teams;

    static class Point {
        int r, c;

        public Point(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }

    static void getPos(int num) {
        while (!q.isEmpty()) {
            Point now = q.poll();
            path[now.r][now.c] = num;
            if (map[now.r][now.c] == 1) {
                teams[num].addFirst(now);
            } else if (map[now.r][now.c] == 2) {
                teams[num].add(now);
            } else if (map[now.r][now.c] == 3) {
                teams[num].addLast(now);
            }

            for (int d = 0; d < 4; d++) {
                int nr = now.r + dr[d];
                int nc = now.c + dc[d];

                if (nr >= 0 && nr < N && nc >= 0 && nc < N && !visited[nr][nc] && map[nr][nc] > 0) {

                    if (map[now.r][now.c] == 1 && map[nr][nc] == 3)
                        continue;
                    else if (map[now.r][now.c] == 4 && map[nr][nc] == 3)
                        continue;
                    visited[nr][nc] = true;
                    q.add(new Point(nr, nc));

                }
            }
        }
    }

    // 앞으로 밀고 마지막은 확인하고 밀기
    static void move() {
        visited = new boolean[N][N];

        for (int m = 1; m < M + 1; m++) {
            LinkedList<Point> team = teams[m];
            int size = team.size();
            Point p = team.getFirst();
            int r = p.r;
            int c = p.c;

            for (int s = 0; s < size; s++) {
                Point now = team.get(s);
                // System.out.println("Now R " + now.r + " C " + now.c);
                for (int d = 0; d < 4; d++) {
                    int nr = now.r + dr[d];
                    int nc = now.c + dc[d];

                    if (nr < 0 || nr > N - 1 || nc < 0 || nc > N - 1)
                        continue;

                    if (map[nr][nc] == 0)
                        continue;

                    // 꼬리
                    if (s == (size - 1)) {
                        // System.out.println(r + " " + c);
                        if (r == nr && c == nc)
                            continue;
                        // System.out.println(" Tail " + now.r + " " + now.c);
                        if (map[nr][nc] == 2 && map[now.r][now.c] == 3) {
                            map[nr][nc] = 3;
                            map[now.r][now.c] = 4;
                            now.r = nr;
                            now.c = nc;
                            break;
                        } else if (map[now.r][now.c] == 1 && !(r == nr && c == nc)) {
                            map[nr][nc] = 3;
                            now.r = nr;
                            now.c = nc;
                            break;
                        }
                    }
                    // 머리
                    else if (map[now.r][now.c] == 1) {
                        if (map[nr][nc] == 2)
                            continue;
                        map[nr][nc] = 1;
                        visited[now.r][now.c] = true;
                        now.r = nr;
                        now.c = nc;
                        break;
                    }
                    // 몸
                    else if (map[now.r][now.c] == 2) {
                        if (map[nr][nc] > 2 || !visited[nr][nc])
                            continue;

                        map[nr][nc] = 2;
                        visited[now.r][now.c] = true;
                        now.r = nr;
                        now.c = nc;
                        break;
                    }
                }
                // print(map);
            }
        }
    }

    static void throwBall(int dir, int round) {
        // 던지기
        // 좌 -> 우 상 -> 하
        if (dir % 2 == 0) {
            for (int n = 0; n < N; n++) {
                int nc = sc + (tc[dir] * n);
                // System.out.println("R " + sr + " C " + sc + " NC " + nc);
                if (map[sr][nc] != 0 && map[sr][nc] != 4) {
                    getScore(sr, nc);
                    break;
                }
            }
            if(round != N - 1){
                sr = sr + tr[dir];
            }
        } else {
            for (int n = 0; n < N; n++) {
                int nr = sr + (tr[dir] * n);
                // System.out.println("R " + sr + " C " + sc + " NR " + nr);
                if (map[nr][sc] != 0 && map[nr][sc] != 4) {
                    getScore(nr, sc);
                    break;
                }
            }
            if(round != N - 1) {
                sc = sc + tc[dir];
            }
        }
    }

    static void getScore(int r, int c) {
        int m = path[r][c];
        LinkedList<Point> team = teams[m];
        int size = team.size();
        for (int i = 0; i < size; i++) {
            Point p = team.get(i);

            if (p.r == r && p.c == c) {
                score += (i + 1) * (i + 1);
                // System.out.println("Score " + score + "\n");
                break;
            }
        }

        Collections.reverse(team);
        Point head = team.getFirst();
        Point tail = team.getLast();

        map[head.r][head.c] = 1;
        map[tail.r][tail.c] = 3;
    }

    static void print(int[][] arr) {
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                System.out.print(arr[r][c] + " ");
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

        map = new int[N][N];
        path = new int[N][N];
        visited = new boolean[N][N];
        for (int r = 0; r < N; r++) {
            token = new StringTokenizer(br.readLine());
            for (int c = 0; c < N; c++) {
                map[r][c] = Integer.parseInt(token.nextToken());
                if (map[r][c] == 1) {
                    heads.add(new Point(r, c));
                    visited[r][c] = true;
                }
            }
        }

        teams = new LinkedList[M + 1];

        for (int m = 1; m < M + 1; m++) {
            teams[m] = new LinkedList<>();
            q.add(heads.poll());
            getPos(m);
        }

        // System.out.println();
        // print(path);
        // for (int m = 1; m < M + 1; m++) {
        //     for (Point p : teams[m]) {
        //         System.out.println(p.r + " " + p.c);
        //     }
        //     System.out.println();
        // }

        int round = 0;
        int turn = 0;
        int div = (K - 1) / N;

        for (int d = 0; d < div + 1; d++) {
            if (K - N < 0) {
                round = K;
            } else {
                K -= N;
                round = N;
            }

            for (int k = 0; k < round; k++) {
                move();
                // print(map);
                throwBall(turn, k);
                // print(map);
            }

            turn = (turn + 1) % 4;
        }

        System.out.println(score);
    }
}