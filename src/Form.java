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

		System.out.println("before:" + timer.getState());
 		timer.start();
		System.out.println("after:" + timer.getState());
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
		System.out.println("form changing... \t" + timer.getState());
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

	public boolean isExistForm(String formName) {
		return instances.containsKey(formName);
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
