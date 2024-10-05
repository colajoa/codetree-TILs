import java.io.*;
import java.util.*;

public class Main {
    static int M, T, pr, pc;
    static int[] dr = {-1, -1, 0, 1, 1, 1, 0, -1};
    static int[] dc = {0, -1, -1, -1, 0, 1, 1, 1};
    static int max;
    static int[] packStep;
    static int[][] deadMon = new int[4][4];
    static List<Monster>[][] monsters = new ArrayList[4][4];
    static List<Monster>[][] eggs = new ArrayList[4][4];
    static boolean[][] visited;
    static class Monster {
        // 이동방향, 데스타이머, 상태(-1 : 시체, 0 : 알, 1 : 생존), 이동여부
        int dir, time, state, isMoved;

        public Monster(int dir, int time, int state, int isMoved) {
            this.dir = dir;
            this.time = time;
            this.state = state;
            this.isMoved = isMoved;
        }
    }
    
    // 팩맨 이동 = 중복 순열 or dfs
    static void movePack(int depth, int[] move) {
        if(depth == 3) {
            visited = new boolean[4][4];
            int eat = 0;
            int nr = pr;
            int nc = pc;
            for(int i = 0; i < 3; i++) {
                nr += dr[move[i]];
                nc += dc[move[i]];

                if(nr < 0 || nr >= 4 || nc < 0 || nc >= 4) return;
                if(!visited[nr][nc]) {
                    visited[nr][nc] = true;
                    int size = monsters[nr][nc].size();

                    for(int s = 0; s < size; s++) {
                        Monster mon = monsters[nr][nc].get(s);
                        if(mon.state == 1) {
                            eat += 1;
                        }
                    }
                }
            }

            if(eat > max) {
                max = eat;
                packStep = move.clone();
                return;
            }

            return;
        }
        
        for(int d = 0; d < 8; d += 2) {
            move[depth] = d;
            movePack(depth + 1, move);
        }
    }

    // 몬스터 섭취
    static void eatMon() {
        visited = new boolean[4][4];
        // System.out.println(packStep[0] + " "+ packStep[1] + " " + packStep[2]);
        for(int i = 0; i < 3; i++) {
            
            pr = pr + dr[packStep[i]];
            pc = pc + dc[packStep[i]];
            if(!visited[pr][pc]) {
                visited[pr][pc] = true;
                int size = monsters[pr][pc].size();

                for(int s = 0; s < size; s++) {
                    Monster mon = monsters[pr][pc].get(s);
                    
                    if(mon.state == 1) {
                        mon.state = -1;
                        deadMon[pr][pc] += 1;
                    }
                }
            }
        }
    }

    // 몬스터 복제 시도
    static void copyMon(int turn) {
        for(int r = 0; r < 4; r++) {
            for(int c = 0; c < 4; c++) {
                int size = monsters[r][c].size();

                for(int i = 0; i < size; i++) {
                    Monster egg = monsters[r][c].get(i);
                    if(egg.state == 1) {
                        eggs[r][c].add(new Monster(egg.dir, 0, 0, turn + 1));
                    }
                }
            }
        }
    }

    // 몬스터 이동
    static void moveMon(int turn) {
        for(int r = 0; r < 4; r++) {
            for(int c = 0; c < 4; c++) {
                int size = monsters[r][c].size();

                for(int i = size - 1; i > -1; i--) {
                    Monster mon = monsters[r][c].get(i);
                    if(mon.state == 1 && mon.isMoved == turn) {
                        for(int d = 0; d < 8; d++) {
                            int nr = r + dr[(mon.dir + d) % 8];
                            int nc = c + dc[(mon.dir + d) % 8];

                            if(nr >= 0 && nr < 4 && nc >= 0 && nc < 4 && !(nr == pr && nc == pc) && deadMon[nr][nc] == 0) {
                                monsters[r][c].remove(mon);
                                monsters[nr][nc].add(new Monster(((mon.dir + d) % 8), 0, 1, (turn + 1)));
                                break;
                            }
                        }

                    }
                }
            }
        }
    }
    
    // 몬스터 소멸
    static void removeMon() {
        for(int r = 0; r < 4; r++) {
            for(int c = 0; c < 4; c++) {
                int size = monsters[r][c].size();

                for(int i = size - 1; i > -1 ; i--) {
                    Monster mon = monsters[r][c].get(i);

                    // 몬스터 시체 상태시
                    if(mon.state == -1) {

                        // 두턴일때
                        if(mon.time == 2) {
                            monsters[r][c].remove(mon);
                            deadMon[r][c] -= 1;
                        } else if(mon.time < 2) {
                             mon.time += 1;
                        }
                    }
                }
            }
        }
    }

    // 몬스터 복제 완성
    static void completeCopy() {
        for(int r = 0; r < 4; r++) {
            for(int c = 0; c < 4; c++) {
                int size = eggs[r][c].size();

                for(int i = size - 1; i > -1; i--) {
                    Monster egg = eggs[r][c].remove(i);
                    egg.state = 1;
                    monsters[r][c].add(egg);
                }
            }
        }
    }
    static void printMon() {
        System.out.println("[Monster]");
        for(int r = 0; r < 4; r++) {
                for(int c = 0; c < 4; c++) {
                    System.out.print(monsters[r][c].size()+ " ");
                }
                System.out.println();
            }
            System.out.println();
    }

    static void printEgg() {
        System.out.println("[Egg]");
        for(int r = 0; r < 4; r++) {
                for(int c = 0; c < 4; c++) {
                    System.out.print(eggs[r][c].size()+ " ");
                }
                System.out.println();
            }
            System.out.println();
    }
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer token = new StringTokenizer(br.readLine());

        M = Integer.parseInt(token.nextToken());
        T = Integer.parseInt(token.nextToken());

        token = new StringTokenizer(br.readLine());

        pr = Integer.parseInt(token.nextToken()) - 1;
        pc = Integer.parseInt(token.nextToken()) - 1;

        for(int r = 0; r < 4; r++) {
            for(int c = 0; c < 4; c++) {
                monsters[r][c] = new ArrayList<>();
                eggs[r][c] = new ArrayList<>();
            }
        }

        for(int m = 0; m < M; m++) {
            token = new StringTokenizer(br.readLine());

            int r = Integer.parseInt(token.nextToken()) - 1;
            int c = Integer.parseInt(token.nextToken()) - 1;
            int dir = Integer.parseInt(token.nextToken()) - 1;

            monsters[r][c].add(new Monster(dir, 0, 1, 0));
        }

        for(int t = 0; t < T; t++) {
            copyMon(t);
            // printEgg();
            moveMon(t);
            // printMon();
            packStep = new int[3];
            max = 0;
            movePack(0, new int[3]);
            eatMon();
            removeMon();
            completeCopy();
        }
        int ans = 0;
        for(int r = 0; r < 4; r++) {
            for(int c = 0; c < 4; c++) {
                int size = monsters[r][c].size();

                for(int i = 0; i < size; i++) {
                    Monster mon = monsters[r][c].get(i);
                    // System.out.println("State " + mon.state);
                    if(mon.state == 1) {
                        ans += 1;
                    }
                }
            }
        }
        System.out.println(ans);
    }
}