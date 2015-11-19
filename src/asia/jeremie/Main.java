package asia.jeremie;


public class Main {

    public static void main(String[] args) {
        // 随机书生成器测试
//        int m = 1;
//        int[] a = new int[m];
//        for (int i = 0; i != 500; ++i) {
//            ++a[Core.random(m - 1)];
//        }
//        for (int i = 0; i < m; i++) {
//            System.out.print(" " + a[i]);
//        }

        Core core = new Core();
        System.out.println(core.initial(16, 16, 99, true));
        System.out.println(core.Hit(15, 15));
        for (int i = 0; i < core.data.length; i++) {
            for (int j = 0; j < core.data[i].length; j++) {
                System.out.print("\t" + core.data[i][j].toInt());
            }
            System.out.println();
        }

    }
}
