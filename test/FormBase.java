import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * FormBaseインタフェース
 * FormBaseクラスは以下のメソッドの実装を要求する
 */
public abstract class FormBase extends Canvas implements MouseListener, KeyListener, MouseMotionListener {
	Graphics offgc;
	Image offscreen = null;
	boolean enableAntiAlias;
	JFrame parentForm;

	FormBase(){
		addMouseListener(this);
		enableAntiAlias = true;
	}
	// initialize:クラス内で使用するオブジェクトを生成する(コンストラクタ中で例外を発生させないため)
	public abstract void initialize();
	// 描画処理
	public abstract void paint(Graphics g);
	// 更新処理
	public abstract void update();

	public void paintComponent(Graphics g){
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
			getAntiAlias() ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
	}

	// http://www.ecst.csuchico.edu/~amk/classes/csciOOP/double-buffering.html
	public void update(Graphics g) {
		Dimension d = size();

		// create the offscreen buffer and associated Graphics
		offscreen = createImage(d.width, d.height);
		offgc = offscreen.getGraphics();
		// clear the exposed area
		offgc.setColor(getBackground());
		offgc.fillRect(0, 0, d.width, d.height);
		offgc.setColor(getForeground());
		// do normal redraw
		paint(offgc);
		// transfer offscreen to window
		g.drawImage(offscreen, 0, 0, this);
	}

	public void setAntiAlias(boolean value){
		enableAntiAlias = value;
	}
	public boolean getAntiAlias(){
		return enableAntiAlias;
	}

	public JFrame getParentFrame(){
		return parentForm;
	}
	public void setParentFrame(JFrame frame){
		parentForm = frame;
	}
	protected Container getContentPane(){
		return getParent();
	}
}