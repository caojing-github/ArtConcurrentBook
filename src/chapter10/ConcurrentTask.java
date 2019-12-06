package chapter10;

import java.util.concurrent.*;

/**
 * 假设有多个线程执行若干任务，每个任务最多只能执行一次。
 * 当多个线程试图同时执行同一个任务时，只允许一个线程执行任务
 * 其他线程需要等待这个任务执行完后才能继续执行
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
                //1.2创建任务
                FutureTask<String> futureTask = new FutureTask<String>(task);

                /**
                 * 1.3
                 *
                 * putIfAbsent
                 * 如果传入key对应的value已经存在，就返回存在的value，不进行替换。
                 * 如果不存在，就添加key和value，返回null
                 */
                future = taskCache.putIfAbsent(taskName, futureTask);
                if (future == null) {
                    future = futureTask;
                    //1.4执行任务
                    futureTask.run();
                }
            }

            try {
                //1.5,2.2线程在此等待任务执行完成
                return future.get();
            } catch (CancellationException e) {
                taskCache.remove(taskName, future);
            }
        }
    }

}
