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
			 * s��ֵ��ͨ��jobid��ѯjob������stop�ֶε�ֵ�Ƿ�ָʾ��ֹ���������null��Ϊstop�����������
			 * ���sֵ�ģ�����ÿ��5������ݿ��ѯ�Ƿ�ȡ����������鵽�ˣ�ȡ��
			 * ���Ժ��޷�ʵ�ֳ�ʱ�����ã��һ�����������
			 */
			/**
			 * ���ۣ��÷�������ʵ��ͬʱʵ�ּ��ܳ�ʱ�����ֶ���ֹ���������
			 */
			boolean top=true;
			boolean cancel = false;
			Scanner s=new Scanner(System.in);
			//���򽫿��������ˣ��в�ͨ���������get���棬����ִ������ˣ�ȡ��Ҳû��
			//����2,���һ������һֱû���յ������������������������һ��ʱ���رոó��򣬵���
			//���ʱ��Ͳ��᷵�ؽ������һ�����޷�������ȥִ�к������ϴ���hdfs�ĳ���
			while(top){
				System.out.println("shu ru:");
				String a=s.nextLine();
				if(a.equals("stop")){
					cancel = future.cancel(true);
					top=false;
				}
				//�����������һ��ʱ�䣬δ�ӵ�ָʾ��ʱ�򣬵��ﵽ�೤ʱ���ʱ��������ѭ��
				//���ʱ��Ͳ��᷵�ؽ������һ�����޷�������ȥִ�к������ϴ���hdfs�ĳ���
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
		}//������ʱ��Ϊ1��
		finally{
			exec.shutdown();
		}
	}
}
