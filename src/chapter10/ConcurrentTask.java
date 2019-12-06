package chapter10;

import java.util.concurrent.*;

/**
 * �����ж���߳�ִ����������ÿ���������ֻ��ִ��һ�Ρ�
 * ������߳���ͼͬʱִ��ͬһ������ʱ��ֻ����һ���߳�ִ������
 * �����߳���Ҫ�ȴ��������ִ�������ܼ���ִ��
 */
public class ConcurrentTask {

    private final ConcurrentMap<Object, Future<String>> taskCache = new ConcurrentHashMap<Object, Future<String>>();

    private String executionTask(final String taskName) throws ExecutionException, InterruptedException {
        while (true) {
            //1.1,2.1
            Future<String> future = taskCache.get(taskName);
            if (future == null) {
                Callable<String> task = new Callable<String>() {
                    @Override
                    public String call() throws InterruptedException {
                        //......
                        return taskName;
                    }
                };
                //1.2��������
                FutureTask<String> futureTask = new FutureTask<String>(task);

                /**
                 * 1.3
                 *
                 * putIfAbsent
                 * �������key��Ӧ��value�Ѿ����ڣ��ͷ��ش��ڵ�value���������滻��
                 * ��������ڣ������key��value������null
                 */
                future = taskCache.putIfAbsent(taskName, futureTask);
                if (future == null) {
                    future = futureTask;
                    //1.4ִ������
                    futureTask.run();
                }
            }

            try {
                //1.5,2.2�߳��ڴ˵ȴ�����ִ�����
                return future.get();
            } catch (CancellationException e) {
                taskCache.remove(taskName, future);
            }
        }
    }

}
