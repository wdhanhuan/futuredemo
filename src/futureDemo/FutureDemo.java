package futureDemo;

import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureDemo {
	public static void main(String[] args) {
		final ExecutorService exec=Executors.newFixedThreadPool(1);
		Callable<Integer> call=new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {
				System.out.println("start");
				Thread.sleep(1000*15);
				System.out.println("end");
				return 100;
			}
			
		};
		Future<Integer> future=exec.submit(call);
		try {
			/**
			 * s的值是通过jobid查询job中新增stop字段的值是否指示终止任务，如果从null变为stop，则结束任务
			 * 监控s值的，可以每隔5秒从数据库查询是否取消任务，如果查到了，取消
			 * 尝试后：无法实现超时的设置，且会阻塞在这里
			 */
			/**
			 * 结论：该方法不能实现同时实现既能超时又能手动终止任务的需求
			 */
			boolean top=true;
			boolean cancel = false;
			Scanner s=new Scanner(System.in);
			//程序将卡在这里，因此，行不通，如果放在get后面，程序执行完毕了，取消也没用
			//问题2,如果一个程序一直没接收到结束该任务，那这里可以设置一个时间点关闭该程序，但是
			//这段时间就不会返回结果给上一级，无法继续下去执行后续的上传到hdfs的程序
			while(top){
				System.out.println("shu ru:");
				String a=s.nextLine();
				if(a.equals("stop")){
					cancel = future.cancel(true);
					top=false;
				}
				//这里可以设置一个时间，未接到指示的时候，当达到多长时间的时候跳出死循环
				//这段时间就不会返回结果给上一级，无法继续下去执行后续的上传到hdfs的程序
			}
			System.out.println(cancel);
			Integer obj = future.get(1000*10,TimeUnit.MILLISECONDS);
			System.out.println(obj);
		} catch (InterruptedException e) {
			System.out.println("time out");
		} catch (ExecutionException e) {
			System.out.println("time out");
		} catch (TimeoutException e) {
			System.out.println("time out");
		}//任务处理时间为1秒
		finally{
			exec.shutdown();
		}
	}
}
