package jp.ac.kansai_u.kutc.firefly.packetArt;

/**
 * フォームのユーティリティークラスです。このクラスはsingletonなので、このクラスを利用するにはgetInstanceを利用してください。
 * また、このクラスを使用しなくなった場合はnullを代入し、参照を削除しててください。メモリリークの原因になります。
 * @author midolin
 */
public class FormUtil {
	Form targetForm;
	static final FormUtil instance = new FormUtil();

	/**
	 * コンストラクタです。
	 */
	FormUtil(){
	}

	/**
	 * FormUtilクラスのインスタンスを取得します。
	 * @param target ユーティリティを適用するフォーム
	 * @return インスタンス
	 */
	public static FormUtil getInstance(Form target){
		instance.targetForm = target;
		return instance;
	}

	/**
	 * FormUtilのインスタンスを取得します。
	 * @return インスタンス
	 */
	public static FormUtil getInstance(){
		return instance;
	}

	/**
	 * ユーティリティを適用するフォームを設定します。
	 * @param target
	 */
	public static void setForm(Form target){
		instance.targetForm = target;
	}

	/**
	 * フォームを生成します。
	 * @param name フォーム名
	 * @param form フォームのクラス
	 *
	 * このメソッドは以下のように使用します。
	 * <code>
	 * 	// インスタンスを設定します。
	 * 	FormUtil.getInstance().createForm("TemplateForm", TemplateForm.class);
	 * *	// 遷移します。
	 * 	FormUtil.getInstance().changeForm("TemplateForm");
	 * </code>
	 */
	public void createForm(String name, Class<? extends FormBase> form){
		if(name == null || form == null) {
			System.out.println(name == null ? "name" : "form");
			return;
		}

		try{
			targetForm.generateForm(name, form);
			System.out.println(name + " create " + (targetForm.isExistForm(name) ? "successful." : "unsuccessful."));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 指定した名前のフォームに遷移します。
	 * @param name フォーム名
	 *
	 * このメソッドは以下のように使用します。
	 * <code>
	 * 	// インスタンスを設定します。
	 * 	FormUtil.getInstance().createForm("TemplateForm", TemplateForm.class);
	 * 	// 遷移します。
	 * 	FormUtil.getInstance().changeForm("TemplateForm");
	 * </code>
	 */
	public void changeForm(String name){
		try{
			System.out.println("Form change to " + name);
			new Thread(new ChangeThread(targetForm, name)).start();

		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Form getForm(){
		return targetForm;
	}

}

/**
 * フォームを変更するスレッドです。
 * @author midolin
 *
 */
class ChangeThread implements Runnable{
	Form form;
	String name;

	ChangeThread(Form form, String name){
		this.name = name;
		this.form = form;
	}

	/**
	 * フォームの変更を実行します。
	 */
	public void run(){
		form.changeForm(name);
	}

}