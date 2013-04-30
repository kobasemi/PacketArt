package jp.ac.kansai_u.kutc.firefly.packetArt;
import java.awt.CardLayout;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import jp.ac.kansai_u.kutc.firefly.packetArt.setting.ConfigStatus;

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
	ConfigStatus config;

	// 時間管理用スレッド 使いまわす
	TimerThread timer;

	/**
	 * フォームのコンストラクタです。
	 * @param startupFormName スタートアップするときに表示されるフォームの名前
	 * @param startupForm スタートアップするときに表示されるフォームのクラス
	 */
	public Form(String startupFormName, Class<? extends FormBase> startupForm){
		config = new ConfigStatus();
		timer  = new TimerThread("TimerThread");
		FormUtil.setForm(this);

		// CardLayout
		card = new CardLayout();
		getContentPane().setLayout(card);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationByPlatform(true);
		setTitle("Packet Art");
		setSize(600, 800);
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

		System.out.println(getCurrentForm());
		System.out.println(getCurrentForm().getContentPane().getClass().getName());

		//card.show(getContentPane(), startupFormName);
		//show();

		// formの処理
		getCurrentForm().initialize();
		// 実行するメソッドの登録
		registerInvokeMethodsToTimer(getCurrentForm());

 		//timer.start();
 		System.out.println("Thread: " + timer.toString());
 		System.out.println("Thread: " + Thread.currentThread().toString());
 		timer.showInvokeMethods();

 		try{
	 		//SwingUtilities.invokeLater(new Runnable(){public void run(){timer.start();}});
	 		timer.start();
	 	} catch(Exception e){
	 		e.printStackTrace();
	 	}
 		
 	}

	// インスタンスを追加
	/**
	 * フォームを追加します。
	 * @param name フォーム名
	 * @param instance フォーム
	 */
	public void addForm(String name, FormBase instance) {
		instances.put(name, new Tuple<FormBase, JComponent>(instance, new JLayeredPane()));
	}

	// インスタンスを削除
	/**
	 * フォームを削除します。
	 * @param name 削除するフォーム名
	 */
	public void removeCurrentInstance(String name) {
		instances.remove(instances.get(name));
	}

	// 現在見えているインスタンスを取得
	/**
	 * 現在表示されているフォームを取得します。
	 * @return 表示されているフォーム
	 */
	public FormBase getCurrentForm() {
		return instances.get(currentFormInstanceName).x;
	}

	/**
	 * 現在表示されているフォームを含むコンポーネントを取得します。
	 * @return 表示されているフォームを含むコンポーネント
	 */
	public JComponent getCurrentComponent() {
		return instances.get(currentFormInstanceName).y;
	}

	/**
	 * nameに指定されたフォームに切り替えます。
	 * @param formName 切り替える対象のフォームの名前。
	 */
	public synchronized void changeForm(String formName) throws IllegalArgumentException{
		// キーがおかしい場合例外を投げつける
		if(formName == null || formName.equals("") || !instances.containsKey(formName))
			throw new IllegalArgumentException("Invalid argument:"
				+ formName == null ? "Argument is null." :
					formName.equals("") ? "Please specify argument." : "Argument \"" + formName  + "\" is not existed.");
		
		// フォーム変更イベントを発行する
		getCurrentForm().onFormChanged();

		// Timerを切り替える
		System.out.println("Current thread is " + Thread.currentThread().getName());
		System.out.println("form changing... \t" + timer.getState());
		System.out.println("isWaiting:" + (timer.isWaiting() ? "true" : "false"));
		//System.out.println(Thread.currentThread().getName() + " to sleep...");
		//synchronized(timer) {
			while(!timer.isWaiting()){
				try{
					synchronized(timer){
						timer.wait();
					}
					//SwingUtilities.invokeLater(timer.tryWait());
					//Thread.currentThread().sleep(1);
					System.out.println(timer.isWaiting());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		//}
		System.out.println("isWaiting:" + (timer.isWaiting() ? "true" : "false"));
		timer.clearInvokeMethods();

		// currentFormInstanceを変える
		currentFormInstanceName = formName;
		System.out.println(getCurrentForm());
		registerInvokeMethodsToTimer(getCurrentForm());
		timer.showInvokeMethods();

		getCurrentForm().initialize();

		// タイマー再開
		synchronized(timer){
			timer.notifyAll();
		}
		System.out.println(currentFormInstanceName);
		card.show(getContentPane(), currentFormInstanceName);
		//getContentPane().repaint();
	}

	/**
	 * 指定されたフォームを生成します。
	 * 作成結果を反映させるには、ChangeFormInstanceメソッドを実行します。
	 * @param name フォームの名前。この名前は一意に定まっている必要があります。
	 * @param form FormBaseクラスを継承したフォーム
	 */
	public void generateForm(String name, Class<? extends FormBase> form) throws IllegalArgumentException{
		if(instances.containsKey(name))
			throw new IllegalArgumentException("Specified argument is already existed.");

		try{
			addForm(name, form.newInstance());
		} catch (Exception e) {
			e.printStackTrace();
		}

		FormBase inst = instances.get(name).x;
		JComponent comp = instances.get(name).y;

		System.out.println();

		inst.setBounds(0, 0, getSize().width, getSize().height);
		inst.setParentFrame((JFrame)this);
		//getContentPane().setBounds(0, 0, getSize().width, getSize().height);
		comp.setLayout(null);
		comp.setVisible(true);
		//comp.show();
		System.out.println(inst);
		System.out.println(comp);
		comp.add(inst, -1);	// -1指定で常に最背面
		System.out.println("\r\nbefore:");
		for(Component item : getContentPane().getComponents())
			System.out.println(item);
		getContentPane().add(comp, name);
		System.out.println("\r\nafter:");
		for(Component item : getContentPane().getComponents())
			System.out.println(item);
		System.out.println(getContentPane().getLayout());
		showForms();
		SwingUtilities.updateComponentTreeUI(this);
	}

	/**
	 * 指定された名前のフォームが存在しているかどうかを確認します。
	 * @param formName フォームの名前
	 * @return フォームの存在の可否
	 */
	public boolean isExistForm(String formName) {
		return instances.containsKey(formName);
	}

	/**
	 * 指定された名前のフォームを取得します。指定されたフォームが存在しない場合、nullを返します。
	 * @param formName フォーム名
	 */
	public FormBase getFormFromName(String formName) {
		if(isExistForm(formName))
			return instances.get(formName).x;
		else
			return null;
	}

	// updateとrepaintをtimerに登録
	/**
	 * タイマーにupdateおよびrepaintメソッドを登録します。
	 * @param form updateとpaintメソッドを含むフォーム
	 */
	void registerInvokeMethodsToTimer(FormBase form){
		try{
			timer.addInvokeMethodForTick(form, form.getClass().getMethod("update"));
			timer.addInvokeMethodForTick(form, form.getClass().getMethod("repaint"));
		} catch (Exception e) {
			// にぎりつぶす
			e.printStackTrace();
		}
	}

	/**
	 * このクラスが持っているフォームをすべて表示します。
	 */
	public void showForms(){
		System.out.println("Form has instances:");
		for(Tuple<FormBase, JComponent> item : instances.values())
			System.out.println(item.x + " are contained by " + item.y);
	}
}
