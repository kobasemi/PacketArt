package jp.ac.kansai_u.kutc.firefly.packetArt;

/**
 * フォームのユーティリティークラスです。このクラスはsingletonなので、このクラスを利用するにはgetInstanceを利用してください。
 * また、このクラスを使用しなくなった場合はnullを代入し、参照を削除しててください。メモリリークの原因になります。
 * @author midolin
 */
public class FormUtil {
	Form targetForm;
	static final FormUtil instance = new FormUtil();

	FormUtil(){
	}

	public static FormUtil getInstance(Form target){
		instance.targetForm = target;
		return instance;
	}

	public static FormUtil getInstance(){
		return instance;
	}

	public static void setForm(Form target){
		instance.targetForm = target;
	}

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

	public void changeForm(String name){
		try{
			System.out.println("Form change to " + name);
			new Thread(new ChangeThread(targetForm, name)).start();
			
		} catch(Exception e){
			e.printStackTrace();
		}
	}

}

class ChangeThread implements Runnable{
	Form form;
	String name;

	ChangeThread(Form form, String name){
		this.name = name;
		this.form = form;
	}

	public void run(){
		form.changeFormInstance(name);
	}

}