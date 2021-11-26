package ahu.ewn;

import java.util.concurrent.*;

/**
 * @author YangYe
 * @email imissyou5201314@outlook.com
 * @date 2020/9/1
 */
public class ThreadTest {


    /**
     * 1.创建线程池
     * 1)  Executors.newFixedThreadPool(10);
     * 2)
     */
    static ExecutorService executor = Executors.newFixedThreadPool(10);


    /**
     * 1.继承Thread
     * 2.实现Runnable接口
     * 3.实现Callable接口+FutureTask（可以拿到返回结果可以处理异常）
     * 4.线程池：给线程池直接提交任务
     * 业务代码中 1 ，2 ，3启动线程方法都不用
     * 将所有的多线程异步任务都交给线程池执行
     * <p>
     * 区别：
     * 1，2，不能得到返回值。
     * 3可以得到返回值
     * 123都不能控制资源
     * 4可以控制资源 性能稳定
     */
    public static void thread() throws ExecutionException, InterruptedException {
        System.out.println("-----main-----start----");
        //1.继承Thread
//        Thread thread = new Thread01();
//        thread.start();

        //2.实现Runnable接口
//        Runnable01 runnable01 = new Runnable01();
//        new Thread(runnable01).start();

        //3.实现Callable接口+FutureTask（可以拿到返回结果可以处理异常）
//        FutureTask<Integer> futureTask = new FutureTask<>(new Callable01());
//        new Thread(futureTask).start();
//        //阻塞等待线程执行完成获取返回结果
//        Integer i = futureTask.get();

//        4.线程池：给线程池直接提交任务
        //当前系统中线程池只有一两个，每个异步任务直接提交给线程池
//        pool.execute(new Runnable01());

        /*
            七大参数
              int corePoolSize 核心线程数(一直存在除非设置了allowCoreThreadTimeOut) : 线程池创建好以后就准备就绪的线程数量，等待接收异步任务去执行
                                                new Thread() * 5
              int maximumPoolSize 最大线程数量;控制资源
              long keepAliveTime  存活时间 ： 如果当前线程数量大于core数量，会释放超出core空闲线程（存货时间大于指定的keepAliveTime）
              TimeUnit unit 时间单位
              BlockingQueue<Runnable> workQueue 阻塞队列 如果任务有很多，就会将目前多的任务放在队列里。只要有线程空闲，就会去队列取出新的任务继续执行
                                                     new LinkedBlockingQueue<>():默认是Integer的最大值 内存占满
              ThreadFactory threadFactory 线程的创建工厂
              RejectedExecutionHandler handler 如果队列满了，按照指定的拒绝策略拒绝执行任务


            工作顺序：
                1. 线程池创建，主备好core数量的核心线程，准备接受任务
                2. 新的任务进来了，用core准备好的空闲线程执行
                    1) core满了，就将再进来的任务放入阻塞队列中。空闲的core就会自己去阻塞队列获取任务执行
                    2) 阻塞队列满了,就直接开新线程执行,最大只能开到max指定的数量
                    3) max都执行好了,Max-Core数量空闲的线程会在keepAliveTime指定时间后自动销毁,最终保持到core的大小
                    4) 如果线程开到了max的数量,还有新任务进来,就会食用reject指定的拒绝策略进行处理
                3. 所有的线程创建都是由指定的factory创建的


             线程池 core 7 max20 queue 50 100并发这么分配
             7个立即执行 50个进入队列 再开13个线程进行执行.剩下的30个使用拒绝策略
             如果不想抛弃还要执行 CallerRunsPolicy立即执行

         */
        ThreadPoolExecutor pool2 = new ThreadPoolExecutor(
                5,
                200,
                10,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );

        //缓存线程池 核心是0
        Executors.newCachedThreadPool();
        //固定值大小的线程池 max=core都不可回收
        Executors.newFixedThreadPool(5);
        //定时任务线程池
        Executors.newScheduledThreadPool(5);
        //单线程线程池 后台从队列获取任务挨个执行
        Executors.newSingleThreadExecutor();


        System.out.println("-----main-----over----  ");


    }


    public void method1() throws ExecutionException, InterruptedException {
        System.out.println("-----main-----start----" + Thread.currentThread().getId());

//        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
//            System.out.println("当前线程：" + Thread.currentThread().getId());
//            int i = 10 / 2;
//            System.out.println("运行结果：" + i);
//        }, executor);

        /**
         * 方法完成后的感知
         */
//        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程：" + Thread.currentThread().getId());
//            int i = 10 / 2;
//            System.out.println("运行结果：" + i);
//            i = 10 / 0;
//            return i;
//        }, executor).whenComplete((result, exception) -> {
//            //执行成功的回调
//            //虽然能得到异常信息，但是不能修改返回数据
//            System.out.println("异步任务成功   " + "结果是：" + result + "  异常:" + exception.getMessage() + "    线程：" + Thread.currentThread().getId());
//        }).exceptionally(throwable -> {
//            //异常回调
//            //可以感知异常，同时返回默认值
//            throwable.printStackTrace();
//            return 10;
//        });

        /**
         * 方法执行完成之后的处理
         */
//        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程：" + Thread.currentThread().getId());
//            int i = 10 / 2;
//            System.out.println("运行结果：" + i);
////            i = 10 / 0;
//            return i;
//        }, executor).handle((res,thr)->{
//            if (res!=null){
//                return res*2;
//            }
//            if (thr!=null){
//                return 0;
//            }
//            return 0;
//        });

        /**
         * 线程串行化
         * 1. thenRunXXX ：不能获取到上一步的执行结果，无返回值
         *  .thenRunAsync(() -> {
         *             System.out.println("任务2启动了");
         *
         *         }, executor);
         *
         * 2.thenAcceptXxx:能接收上一步结果，无返回值
         *      .thenAcceptAsync((res)->{
         *             System.out.println("任务2启动了，上一步结果是"+res);
         *         }, executor);
         *
         * 3.thenApplyXxx:能接收上一步结果，有返回值
         */
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("当前线程：" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果：" + i);
//            i = 10 / 0;
            return i;
        }, executor).thenApplyAsync((res) -> {
            System.out.println("任务2启动了，上一步结果是" + res);
            return "结果是:" + res + "   任务2线程：" + Thread.currentThread().getId();
        }, executor);
        String s = future.get();


        System.out.println("-----main-----over----" + future.get());
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("-----main-----start----");
        /**
         * 两个都完成
         * thenCombine:组合两个future，获取两个future的返回结果，并返回当前任务的返回值
         * thenAcceptBoth:组合两个future，获取两个future任务的返回结果，然后处理任务，没有返回值
         * runAfterBoth:组合两个future，不需要获取future的结果，只需要两个future处理完成任务后，处理该任务，
         *
         */
//        CompletableFuture<Object> future1 = CompletableFuture.supplyAsync(() -> {
//            System.out.println("任务1线程：" + Thread.currentThread().getId());
//            int i = 10 / 2;
//            System.out.println("任务1运行结果：" + i);
//            return i;
//        }, executor);
//
//        CompletableFuture<Object> future2 = CompletableFuture.supplyAsync(() -> {
//            System.out.println("任务2线程：" + Thread.currentThread().getId());
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println("任务2运行结束");
//
//            return "Hello";
//        }, executor);

//        future1.runAfterBothAsync(future2,()->{
//            System.out.println("任务3开始：" + Thread.currentThread().getId());
//        },executor);
//
//        future1.thenAcceptBothAsync(future2,(f1,f2)->{
//            System.out.println("任务3开始 线程：" + Thread.currentThread().getId()+"  f1:"+f1+"  f2:"+f2);
//        } , executor);

//        CompletableFuture<String> f = future1.thenCombineAsync(future2, (f1, f2) -> {
//            return "  f1:" + f1 + "  f2:" + f2;
//        }, executor);
//        System.out.println("f3---------->"+f.get());


        /**
         * 两个任务只要有一个完成，就执行业务3
         * runAfterEitherXxx：不感知结果，自己也无返回值
         * acceptEitherXxx: 感知结果，无返回值
         *
         */
//        future1.runAfterEitherAsync(future2,()->{
//            System.out.println("任务3开始：" + Thread.currentThread().getId());
//        } , executor);

//        future1.acceptEitherAsync(future2, res->{
//            System.out.println("任务3开始：" + Thread.currentThread().getId()+"  返回值："+res);
//        },executor);

//        CompletableFuture<String> future = future1.applyToEitherAsync(future2, res -> {
//            System.out.println("任务3开始：" + Thread.currentThread().getId() + "  返回值：" + res);
//            return "任务3------->" + res.toString();
//        }, executor);
//        System.out.println(future.get());


        CompletableFuture<String> futureImg = CompletableFuture.supplyAsync(() -> {
            System.out.println("查询商品图片");
            return "image.jpg";
        }, executor);

        CompletableFuture<String> futureAttr = CompletableFuture.supplyAsync(() -> {
            System.out.println("查询商品属性");
            return "黑色+256G";
        }, executor);

        CompletableFuture<String> futureDesc = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("查询商品介绍");
            return "小米";
        }, executor);

//        futureImg.get();
//        futureAttr.get();
//        futureDesc.get();

//        CompletableFuture<Void> allOf = CompletableFuture.allOf(futureImg, futureAttr, futureDesc);
//        allOf.get();//等待所有任务完成

        CompletableFuture<Object> anyOf = CompletableFuture.anyOf(futureImg, futureAttr, futureDesc);
        Object o = anyOf.get();
        System.out.println("anyOf--->"+o.toString());

//        System.out.println("-----main-----over----"+ futureImg.get() + futureAttr.get() + futureDesc.get() );
        System.out.println("-----main-----over----" );
    }

    public static class Thread01 extends Thread {
        @Override
        public void run() {
            System.out.println("当前线程：" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果：" + i);
        }
    }

    public static class Runnable01 implements Runnable {
        @Override
        public void run() {
            System.out.println("当前线程：" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果：" + i);
        }
    }

    public static class Callable01 implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            System.out.println("当前线程：" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果：" + i);
            return i;
        }
    }
}
