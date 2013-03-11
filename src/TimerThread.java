import java.util.*;
import java.util.concurrent.*;
import java.util.Collection;
import java.lang.reflect.*;
// タイマークラス
public class TimerThread extends Thread {
	boolean isTerminated;
	boolean isWait;
	boolean methodsLock;
	long time;
	List<Tuple3<Object, Method, Object[]>> methods;

	TimerThread(){
		time = 0;
		isTerminated = false;
		isWait = false;
		methodsLock = false;
		methods = Collections.synchronizedList(
			new ArrayList<Tuple3<Object, Method, Object[]>>()
		);
	}

	// 60fpsで動作させる(秒間60回、指定のメソッドが実行される→だいたい16msに一回)
	// 遅くなる場合は知らない
	// 参考:http://javaappletgame.blog34.fc2.com/blog-entry-265.html
	public void run(){
		System.out.println("This thread is " + Thread.currentThread().getName());
		long currentTime = System.currentTimeMillis();
		long oldTime = currentTime;
		long sleepTime = 16;
		while(!isTerminated){
			// スレッドが止まることを要求されているなら止まる
			System.out.print(isWait ? ";" : ".");
			if(isWait) {
				System.out.println("Waiting...");
				try{
					synchronized(this){
						wait();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 待機状態から復帰したときの浦島太郎状態を正す
				oldTime = System.currentTimeMillis();
			} else {
				oldTime = currentTime;
			}

			// 登録されたメソッドの実行
			synchronized(methods){
				for(Tuple3<Object, Method,Object[]> value : methods) {
					try{
						if(value.z == null)
							value.y.invoke(value.x);
						else
							value.y.invoke(value.x, value.z);	
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			// スレッドを一時停止させる時間の計算と停止
			currentTime = System.currentTimeMillis();
			sleepTime = 16 - currentTime - oldTime;
			if(sleepTime < 2)
				sleepTime = 2;

			try{
				Thread.sleep(sleepTime);
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	// スレッドを止めることを試みる
	public void tryStop() {
		isTerminated = true;	
	}
	// スレッドの中断を試みる
	public void tryWait(){
		//System.out.println(isWait);
		isWait = true;
		//System.out.println(isWait);
	}

	public boolean isWaiting(){
		return getState() == Thread.State.WAITING;
	}

	public void restart(){
		try{
			Thread.State state = getState();
			if(state == Thread.State.WAITING)
				notify();
			else
				System.out.println("restarting state... " + state);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addInvokeMethodForTick(Object parent, Method method) {
		methods.add(new Tuple3<Object, Method, Object[]>(parent, method, null));
	}
	public void addInvokeMethodForTick(Object parent, Method method, Object[] params) {
		methods.add(new Tuple3<Object, Method, Object[]>(parent, method, params));
	}

	// TODO:メソッドの削除を実装する
	public void removeInvokeMethodTick(Method method) {
		//methods.remove(method);
	}

	public void clearInvokeMethods(){
		// methodsを使用している間に変なことをされないようにいったんスレッドを止めてから実行する
		// タイミング調整も兼ねる
		while(isWaiting())
			isWait = true;

		System.out.print("registed methods removing is... ");
		synchronized(methods){
			methods.clear();
		}
		System.out.println(methods.size() == 0 ? "success." : "FAILED.");
	}
}
