package chapter08;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * ������ˮ���������
 *
 * @author CaoJing
 * @date 2019/06/24 19:44
 */
public class BankWaterService implements Runnable {

    /**
     * ����4�����ϣ�������֮��ִ�е�ǰ���run����
     */
    private CyclicBarrier c = new CyclicBarrier(4, this);

    /**
     * ����ֻ��4��sheet������ֻ����4���߳�
     */
    private Executor executor = Executors.newFixedThreadPool(4);

    /**
     * ����ÿ��sheet��������������
     */
    private static HashMap<String, Integer> sheetBankWaterCount = new HashMap<String, Integer>();

    private void count() {
        for (int i = 0; i < 4; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // ���㵱ǰsheet���������ݣ��������ʡ��
                    sheetBankWaterCount.put(Thread.currentThread().getName(), 1);
                    // ����������ɣ�����һ������
                    try {
                        c.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    /**
     * ���̳߳�ִ�������ᷢ��������δ����ϸ�о���
     * ���Կ�����ƪ����     https://surlymo.iteye.com/blog/1602537
     */
//    private void count() {
//        for (int i = 0; i < 4; i++) {
//            final int j = i;
//            executor.execute(new Runnable() {
//                @Override
//                public void run() {
//                    // ���㵱ǰsheet���������ݣ��������ʡ��
//                    sheetBankWaterCount.put(Thread.currentThread().getName(), 1);
//                    // ����������ɣ�����һ������
//                    try {
//                        c.await();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } catch (BrokenBarrierException e) {
//                        e.printStackTrace();
//                    }
//
//                    System.out.println("������" + j);
//                }
//            });
//        }
//    }

    @Override
    public void run() {
        int result = 0;
        // ����ÿ��sheet������Ľ��
        for (Map.Entry<String, Integer> sheet : sheetBankWaterCount.entrySet()) {
            result += sheet.getValue();
        }
        // ��������
        sheetBankWaterCount.put("result", result);
        System.out.println(result);
    }

    public static void main(String[] args) {
        BankWaterService bankWaterCount = new BankWaterService();
        bankWaterCount.count();
    }
}
