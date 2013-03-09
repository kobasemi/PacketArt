import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.concurrent.*;
import java.lang.reflect.*;

/**
 * 利用者に表示される画面です。
 * @author midolin
 */
public class Form extends JFrame{
	String currentFormInstanceName = "";
	// 各フォームのインスタンスはDictionaryを用いて名前で管理される
	// Form(JFrame) > JComponent > FormBase extended　object の構造
	Map<String, Tuple<FormBase, JComponent>> instances = new HashMap<String, Tuple<FormBase, JComponent>>();

	CardLayout card;

	// 時間管理用スレッド 使いまわす
	TimerThread timer;

	Form(String startupFormName, Class<? extends FormBase> startupForm){
		timer  = new TimerThread();
		FormUtil.setForm(this);

		// CardLayout
		card = new CardLayout();
		getContentPane().setLayout(card);
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

		// フォームを生成
		generateForm(startupFormName, startupForm);
		currentFormInstanceName = startupFormName;

		System.out.println(getCurrentInstance());
		System.out.println(getCurrentInstance().getContentPane().getClass().getName());

		card.show(getContentPane(), startupFormName);
		show();

		// formの処理
		getCurrentInstance().initialize();
		// 実行するメソッドの登録
		registerInvokeMethodsToTimer(getCurrentInstance());

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

	/**
	 * nameに指定されたフォームに切り替えます。
	 * @param formName 切り替える対象のフォームの名前。
	 */
	public synchronized void changeFormInstance(String formName) throws IllegalArgumentException{
		// キーがおかしい場合例外を投げつける
		if(formName == null || formName.equals("") || !instances.containsKey(formName))
			throw new IllegalArgumentException("Invalid argument:" 
				+ formName == null ? "Argument is null." : 
					formName.equals("") ? "Please specify argument." : "Argument is not existed.");
		
		// Timerを切り替える
		timer.tryWait();
		while(timer.isWaiting()){
			try{
				Thread.sleep(1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		timer.clearInvokeMethods();

		// currentFormInstanceを変える
		currentFormInstanceName = formName;
		registerInvokeMethodsToTimer(getCurrentInstance());
		card.show(getContentPane(), currentFormInstanceName);

		// タイマー再開
		timer.restart();
	}

	/**
	 * 指定されたフォームを生成します。
	 * 作成結果を反映させるには、ChangeFormInstanceメソッドを実行します。
	 * @param name フォームの名前。この名前は一意に定まっている必要があります。
	 * @param form FormBaseクラスを継承したフォーム。
	 */
	public synchronized void generateForm(String name, Class<? extends FormBase> form) throws IllegalArgumentException{
		if(instances.containsKey(name))
			throw new IllegalArgumentException("Specified argument is already existed.");

		try{
			instances.put(name, new Tuple<FormBase, JComponent>(form.newInstance(), new JLayeredPane()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		FormBase inst = instances.get(name).x;
		JComponent comp = instances.get(name).y;

		inst.setBounds(0, 0, getSize().width, getSize().height);
		inst.setParentFrame((JFrame)this);
		getContentPane().setBounds(0, 0, getSize().width, getSize().height);
		comp.setLayout(null);
		comp.add(inst, -1);	// -1指定で常に最背面
		getContentPane().add(inst, null);
	}

	// updateとrepaintをtimerに登録
	void registerInvokeMethodsToTimer(FormBase form){
		try{
			timer.addInvokeMethodForTick(form, form.getClass().getMethod("update"));
			timer.addInvokeMethodForTick(form, form.getClass().getMethod("repaint"));
		} catch (Exception e) {
			// にぎりつぶす
			e.printStackTrace();
		}
	}
}

// タイマークラス
class TimerThread extends Thread {
	boolean isTerminated;
	boolean isWait;
	long time;
	ArrayList<Tuple3<Object, Method, Object[]>> methods;

	TimerThread(){
		time = 0;
		isTerminated = false;
		isWait = false;
		methods = new ArrayList<Tuple3<Object, Method, Object[]>>();
	}

	// 60fpsで動作させる(秒間60回、指定のメソッドが実行される→だいたい16msに一回)
	// 遅くなる場合は知らない
	// 参考:http://javaappletgame.blog34.fc2.com/blog-entry-265.html
	public void run(){
		// TODO:メソッド呼び出しをスレッドプールを使って並列化させたい
		// ↑update→paintの順で呼び出されるが、その呼び出し順が安定しなくなるため却下

		long currentTime = System.currentTimeMillis();
		long oldTime = currentTime;
		long sleepTime = 16;
		while(!isTerminated){
			// スレッドが止まることを要求されているなら止まる
			if(isWait) {
				try{
					synchronized(this){
						wait();
					}			
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 待機状態から復帰したときの浦島太郎状態を正す
				oldTime = System.currentTimeMillis();
			}
			else
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
	public void tryStop() {
		isTerminated = true;	
	}
	// スレッドの中断を試みる
	public void tryWait(){
		isWait = true;
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
				System.out.println(state);
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

	public synchronized void clearInvokeMethods(){
		// methodsを使用している間に変なことをされないようにいったんスレッドを止めてから実行する
		// タイミング調整も兼ねる
		while(isWaiting())
			isWait = true;

		synchronized(methods){
			methods.clear();
		}
	}
}
