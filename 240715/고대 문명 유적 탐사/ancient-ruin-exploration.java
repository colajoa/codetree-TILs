import java.io.*;
import java.util.*;

public class Main {
    static int K, M, sr, sc, sa, max;
    static int[][] map;
    static long score;
    static List<Point>[][] partsPoint;
    static Queue<Integer> parts = new ArrayDeque<>();
    static Queue<Point> q = new ArrayDeque<>();
    static int[][] visited;
    static int[] dr = { -1, 0, 1, 0 };
    static int[] dc = { 0, 1, 0, -1 };

    static class Point implements Comparable<Point> {
        int r, c;

        public Point(int r, int c) {
            this.r = r;
            this.c = c;
        }

        public int compareTo(Point p) {
            if (this.c == p.c) {
                return p.r - this.r;
            }
            return this.c - p.c;
        }
    }

    static void find() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                for (int angle = 0; angle < 3; angle++) {
                    bfs(r, c, angle);
                }
            }
        }

        if (max != -1) {
            map = rotate(sr, sc, sa);
            getPart();
        }

    }

    static void getPart() {
        int idx = (sr * 3) + sc;
        List<Point> p = partsPoint[sa][idx];

        while (!parts.isEmpty()) {
            int size = p.size();

            Collections.sort(p);
            for (int s = 0; s < size; s++) {
                Point now = p.remove(0);
                int part = parts.poll();
                map[now.r][now.c] = part;
                score += 1;
            }

            visited = new int[5][5];
            int num = 1;
            Set<Integer> set = new HashSet<>();
            for (int r = 0; r < 5; r++) {
                for (int c = 0; c < 5; c++) {
                    if (visited[r][c] == 0) {
                        visited[r][c] = num;
                        q.add(new Point(r, c));
                        int cnt = 1;

                        while (!q.isEmpty()) {
                            Point n = q.poll();

                            for (int d = 0; d < 4; d++) {
                                int nr = n.r + dr[d];
                                int nc = n.c + dc[d];

                                if (nr >= 0 && nr < 5 && nc >= 0 && nc < 5 && visited[nr][nc] == 0
                                        && map[nr][nc] == map[r][c]) {
                                    visited[nr][nc] = num;
                                    q.add(new Point(nr, nc));
                                    cnt += 1;
                                }
                            }
                        }

                        if (cnt >= 3) {
                            set.add(num);
                        }
                        num++;
                    }
                }
            }

            if (set.size() == 0) {
                break;
            } else {
                for (int r = 0; r < 5; r++) {
                    for (int c = 0; c < 5; c++) {
                        if (set.contains(visited[r][c])) {
                            p.add(new Point(r, c));
                        }
                    }
                }
            }
        }

    }

    static int[][] rotate(int nr, int nc, int angle) {
        int[][] copy = new int[5][5];
        for (int r = 0; r < 5; r++) {
            copy[r] = map[r].clone();
        }

        if (angle == 0) {
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    copy[nr + r][nc + c] = map[(2 - c) + nr][r + nc];
                }
            }
        } else if (angle == 1) {
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    copy[nr + r][nc + c] = map[(2 - r) + nr][(2 - c) + nc];
                }
            }
        } else {
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    copy[nr + r][nc + c] = map[c + nr][(2 - r) + nc];
                }
            }
        }

        return copy;
    }

    static void bfs(int nr, int nc, int angle) {
        int[][] copy = rotate(nr, nc, angle);
        int idx = (nr * 3) + nc;
        partsPoint[angle][idx].clear();

        visited = new int[5][5];
        int num = 1;
        Set<Integer> set = new HashSet<>();
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                if (visited[r][c] == 0) {
                    visited[r][c] = num;
                    q.add(new Point(r, c));
                    int cnt = 1;

                    while (!q.isEmpty()) {
                        Point now = q.poll();

                        for (int d = 0; d < 4; d++) {
                            int nextR = now.r + dr[d];
                            int nextC = now.c + dc[d];

                            if (nextR >= 0 && nextR < 5 && nextC >= 0 && nextC < 5 && visited[nextR][nextC] == 0
                                    && copy[nextR][nextC] == copy[r][c]) {
                                visited[nextR][nextC] = num;
                                q.add(new Point(nextR, nextC));
                                cnt += 1;
                            }
                        }
                    }

                    if (cnt >= 3) {
                        set.add(num);
                    }
                    num++;
                }
            }
        }
        if (set.size() > 0) {

            int cnt = 0;
            for (int r = 0; r < 5; r++) {
                for (int c = 0; c < 5; c++) {
                    if (set.contains(visited[r][c])) {
                        cnt += 1;
                        partsPoint[angle][idx].add(new Point(r, c));
                    }
                }
            }

            if (cnt > max) {
                max = cnt;
                sa = angle;
                sr = nr;
                sc = nc;
            } else if (cnt == max) {
                if (angle == sa) {
                    if (sr == nr) {
                        if (sc > nc) {
                            sc = nc;
                        }
                    } else if (sr > nr) {
                        sr = nr;
                        sc = nc;
                    }
                } else if (angle < sa) {
                    sa = angle;
                    sr = nr;
                    sc = nc;
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer token = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();
        K = Integer.parseInt(token.nextToken());
        M = Integer.parseInt(token.nextToken());

        map = new int[5][5];
        partsPoint = new ArrayList[3][9];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                partsPoint[i][j] = new ArrayList<>();
            }
        }

        for (int r = 0; r < 5; r++) {
            token = new StringTokenizer(br.readLine());
            for (int c = 0; c < 5; c++) {
                map[r][c] = Integer.parseInt(token.nextToken());
            }
        }

        token = new StringTokenizer(br.readLine());

        for (int m = 0; m < M; m++) {
            parts.add(Integer.parseInt(token.nextToken()));
        }

        for (int k = 0; k < K; k++) {
            sr = 3;
            sc = 3;
            sa = 3;
            max = -1;
            score = 0;
            find();
            if (max == -1) {
                break;
            }
            getPart();
            sb.append(score).append(" ");
        }

        System.out.println(sb);
    }
}