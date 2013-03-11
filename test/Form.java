import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.concurrent.*;
import java.lang.reflect.*;

public class Form extends JFrame{
	String currentFormInstanceName = "";
	// 各フォームのインスタンスはDictionaryを用いて名前で管理される
	// Form(JFrame) > JComponent > FormBase extended　object の構造
	Map<String, Tuple<FormBase, JComponent>> instances = new HashMap<String, Tuple<FormBase, JComponent>>();

	// 時間管理用スレッド
	TimerThread timer = new TimerThread();

	Form(String startupFormName, FormBase startupFormInstance){
		// CardLayout
		getContentPane().setLayout(new CardLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Packet Art");
		setSize(640, 480);
		getRootPane().setDoubleBuffered(true);
		((JComponent)getContentPane()).setDoubleBuffered(true);
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}
		SwingUtilities.updateComponentTreeUI(this);

		currentFormInstanceName = startupFormName;
		instances.put(startupFormName, new Tuple<FormBase, JComponent>(startupFormInstance, new JLayeredPane()));
		getCurrentInstance().setBounds(0, 0, 640, 480);
		getCurrentInstance().setParentFrame((JFrame)this);
		getContentPane().setBounds(0, 0, 640, 480);
		getCurrentComponent().setLayout(null);
		getCurrentComponent().add(getCurrentInstance(), -1);
		getContentPane().add(getCurrentComponent(), null);

		System.out.println(getCurrentInstance());
		System.out.println(getCurrentInstance().getContentPane().getClass().getName());

 		show();

		// formの処理
		getCurrentInstance().initialize();
		try{
			timer.addInvokeMethodForTick(getCurrentInstance(), getCurrentInstance().getClass().getMethod("update"));
			timer.addInvokeMethodForTick(getCurrentInstance(), getCurrentInstance().getClass().getMethod("repaint"));
		} catch (Exception e) {
			// にぎりつぶす
			e.printStackTrace();
		}
 		timer.run();
 	}
	// インスタンスを追加
	public void addCurrentInstance (String name, FormBase instance) {
		instances.put(name, new Tuple<FormBase, JComponent>(instance, new JLayeredPane()));
	}

	// インスタンスを削除
	public void removeCurrentInstance(String name) {
		instances.remove(instances.get(name));
	}

	// 現在見えているインスタンスを取得
	public FormBase getCurrentInstance() {
		return instances.get(currentFormInstanceName).x;
	}
	public JComponent getCurrentComponent() {
		return instances.get(currentFormInstanceName).y;
	}
}

// タイマークラス、クロック回路のような働き?
class TimerThread extends Thread {
	boolean isTerminated;
	long time;
	ArrayList<Tuple3<Object, Method, Object[]>> methods;

	TimerThread(){
		time = 0;
		isTerminated = false;
		methods = new ArrayList<Tuple3<Object, Method, Object[]>>();
	}

	// 60fpsで動作させる(秒間60回、指定のメソッドが実行される→だいたい16msに一回)
	// 遅くなる場合は知らない
	// 参考:http://javaappletgame.blog34.fc2.com/blog-entry-265.html
	public void run(){
		// TODO:メソッド呼び出しをスレッドプールを使って並列化させたい
		// Executer executer = Executors.newFixedThreadPool(10);

		long currentTime = System.currentTimeMillis();
		long oldTime = currentTime;
		long sleepTime = 16;
		while(!isTerminated){
			oldTime = currentTime;

			// 登録されたメソッドの実行
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
	public void stopTimer() {
		isTerminated = true;	
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
}
