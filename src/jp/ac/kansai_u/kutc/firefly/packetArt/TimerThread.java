package jp.ac.kansai_u.kutc.firefly.packetArt;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
// タイマークラス

/**
 * 1tick(約1/60秒)に1回、指定のメソッドを呼び出すためのクラスです。
 *
 * @author midolin
 */
public class TimerThread extends Thread {
	boolean isTerminated = false;
	boolean isWait = false;
	boolean methodsLock = false;
	long time = 0;
	List<Tuple3<Object, Method, Object[]>> methods;

	/**
	 * スレッドを作成します。
	 *
	 * @param threadName
	 */
	public TimerThread(String threadName) {
		setName(threadName);
		methods = Collections.synchronizedList(
				new ArrayList<Tuple3<Object, Method, Object[]>>()
		);
	}

	// 60fpsで動作させる(秒間60回、指定のメソッドが実行される→だいたい16msに一回)
	// 遅くなる場合は知らない
	// 参考:http://javaappletgame.blog34.fc2.com/blog-entry-265.html

	/**
	 * メソッド呼び出しのスレッドを実行します。
	 */
	public void run() {
		System.out.println("This thread is " + Thread.currentThread().getName());
		long currentTime = System.currentTimeMillis();
		long oldTime = currentTime;
		long sleepTime = 32;

		while (!isTerminated) {
			// スレッドが止まることを要求されているなら止まる
			//System.out.print(isWait ? ";" : ".");
			if (isWait) {
				System.out.println("Waiting...");
				try {
					//notify();
					while (isWaiting()) {
						sleep(sleepTime);
					}
					notify();
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 待機状態から復帰したときの浦島太郎状態を正す
				oldTime = System.currentTimeMillis();
			} else {
				oldTime = currentTime;
			}

			// 登録されたメソッドの実行
			synchronized (methods) {
				for (Tuple3<Object, Method, Object[]> value : methods) {
					try {
						if (value.z == null)
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
			sleepTime = sleepTime - currentTime - oldTime;
			if (sleepTime < 2)
				sleepTime = 2;

			try {
				Thread.sleep(sleepTime);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// スレッドを止めることを試みる

	/**
	 * スレッドの停止を試みます。
	 */
	public void tryStop() {
		isTerminated = true;
	}
	// スレッドの中断を試みる

	/**
	 * スレッドの中断を試みます。
	 */
	public void tryWait() {
		//System.out.println(isWait);
		isWait = true;
		//System.out.println(isWait);
	}

	/**
	 * スレッドが中断状態かどうかを取得します。
	 *
	 * @return 中断状態かどうか
	 */
	public boolean isWaiting() {
		Thread.State state = getState();
		if (state == Thread.State.WAITING
				|| state == Thread.State.TIMED_WAITING)
			return true;
		else
			return false;
	}

	/**
	 * 一時的に中断しているスレッドを再開させます。
	 */
	public void restart() {
		try {
			if (isWaiting())
				notify();
			else
				System.out.println("restarting state... " + getState());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 1回のループごとに実行されるメソッドを登録します。
	 *
	 * @param parent メソッドの呼び出し元とするオブジェクト
	 * @param method 実行するメソッド
	 */
	public void addInvokeMethodForTick(Object parent, Method method) {
		methods.add(new Tuple3<Object, Method, Object[]>(parent, method, null));
	}

	/**
	 * 1回のループごとに実行されるメソッドを登録します。
	 *
	 * @param parent メソッドの呼び出し元とするオブジェクト
	 * @param method 実行するメソッド
	 * @param params 実行時に付与される引数
	 */
	public void addInvokeMethodForTick(Object parent, Method method, Object[] params) {
		methods.add(new Tuple3<Object, Method, Object[]>(parent, method, params));
	}

	// TODO:メソッドの削除を実装する

	/**
	 * 指定したメソッドを削除します。
	 *
	 * @param method 削除するメソッド
	 */
	public void removeInvokeMethodTick(Method method) {
		//methods.remove(method);
	}

	/**
	 * 登録されているすべてのメソッドを削除します。
	 */
	public void clearInvokeMethods() {
		// methodsを使用している間に変なことをされないようにいったんスレッドを止めてから実行する
		// タイミング調整も兼ねる
		//while(isWaiting())
		//	isWait = true;

		System.out.print("registed methods removing is... ");
		synchronized (methods) {
			methods.clear();
		}
		System.out.println(methods.size() == 0 ? "success." : "FAILED.");
	}

	/**
	 * 現在登録されているメソッドをすべて表示します。
	 */
	public void showInvokeMethods() {
		for (Tuple3<Object, Method, Object[]> value : methods)
			System.out.println(value.x + "." + value.y.getName());
	}
}
