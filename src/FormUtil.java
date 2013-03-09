import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.concurrent.*;
import java.lang.reflect.*;
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
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void changeForm(String name){
		try{
			targetForm.changeFormInstance(name);
		} catch(Exception e){
			e.printStackTrace();
		}
	}

}