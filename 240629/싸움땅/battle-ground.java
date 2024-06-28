import java.io.*;
import java.util.*;

public class Main {
    static int N, M, K;
    static int[] dr = { -1, 0, 1, 0 };
    static int[] dc = { 0, 1, 0, -1 };
    static Player[] players;
    static List<Integer>[][] field;
    static int[][] visited;

    static class Player {
        int r, c, d, stat, gun, score;

        public Player(int r, int c, int d, int stat, int gun, int score) {
            this.r = r;
            this.c = c;
            this.d = d;
            this.stat = stat;
            this.gun = gun;
            this.score = score;
        }
    }

    static void game() {
        for (int m = 1; m < M + 1; m++) {
            Player player = players[m];

            int nr = player.r + dr[player.d];
            int nc = player.c + dc[player.d];

            if (nr < 0 || nr >= N || nc < 0 || nc >= N) {
                player.d = (player.d + 2) % 4;
                nr = player.r + dr[player.d];
                nc = player.c + dc[player.d];
            }
            int next = visited[nr][nc];
            visited[player.r][player.c] = 0;
            player.r = nr;
            player.c = nc;

            // 이동 방향에 아무도 없고
            if (visited[nr][nc] == 0) {
                visited[nr][nc] = m;
                // 총이 있을때
                int size = field[nr][nc].size();
                if (size > 0) {
                    Collections.sort(field[nr][nc]);
                    // 내 총보다 좋을 때
                    if (player.gun < field[nr][nc].get(size - 1)) {
                        int gun = player.gun;
                        player.gun = field[nr][nc].remove(size - 1);
                        if (gun != 0) {
                            field[nr][nc].add(gun);
                        }
                    }
                }
            } else {
                Player nPlayer = players[next];
                int nTotal = nPlayer.stat + nPlayer.gun;
                int total = player.stat + player.gun;
                int win = -1;
                int lose = -1;
                // 기존 플레이어가 이기거나 스탯이 더 좋음
                if (nTotal > total || (nTotal == total && nPlayer.stat > player.stat)) {
                    win = next;
                    lose = m;
                }
                // 움직인 플레이어가 이거나 스탯이 더 좋음
                else if (nTotal < total || (nTotal == total && nPlayer.stat < player.stat)) {
                    win = m;
                    lose = next;
                }

                players[win].score += Math.abs(total - nTotal);
                visited[nr][nc] = win;

                int gun = players[lose].gun;
                if (gun != 0) {
                    field[nr][nc].add(gun);
                    players[lose].gun = 0;
                }

                for (int d = 0; d < 4; d++) {
                    nr = players[lose].r + dr[players[lose].d];
                    nc = players[lose].c + dc[players[lose].d];

                    if (nr >= 0 && nr < N && nc >= 0 && nc < N && visited[nr][nc] == 0) {
                        players[lose].r = nr;
                        players[lose].c = nc;
                        visited[nr][nc] = lose;
                        int size = field[nr][nc].size();

                        if (size > 0) {
                            Collections.sort(field[nr][nc]);
                            players[lose].gun = field[nr][nc].remove(size - 1);
                        }

                        break;
                    } else {
                        players[lose].d = (players[lose].d + 1) % 4;
                    }

                }

                gun = players[win].gun;
                nr = players[win].r;
                nc = players[win].c;
                Collections.sort(field[nr][nc]);

                int size = field[nr][nc].size();

                if (size > 0 && gun < field[nr][nc].get(size - 1)) {
                    players[win].gun = field[nr][nc].remove(size - 1);
                    field[nr][nc].add(gun);
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer token = new StringTokenizer(br.readLine());

        N = Integer.parseInt(token.nextToken());
        M = Integer.parseInt(token.nextToken());
        K = Integer.parseInt(token.nextToken());

        visited = new int[N][N];
        players = new Player[M + 1];
        field = new ArrayList[N][N];

        for (int r = 0; r < N; r++) {
            token = new StringTokenizer(br.readLine());
            for (int c = 0; c < N; c++) {
                field[r][c] = new ArrayList<>();
                field[r][c].add(Integer.parseInt(token.nextToken()));
            }
        }

        for (int m = 1; m < M + 1; m++) {
            token = new StringTokenizer(br.readLine());

            int x = Integer.parseInt(token.nextToken()) - 1;
            int y = Integer.parseInt(token.nextToken()) - 1;
            int d = Integer.parseInt(token.nextToken());
            int s = Integer.parseInt(token.nextToken());
            visited[x][y] = m;
            players[m] = new Player(x, y, d, s, 0, 0);
        }

        for (int k = 0; k < K; k++) {
            game();
        }

        for (int m = 1; m < M + 1; m++) {
            System.out.print(players[m].score + " ");
        }
    }
}