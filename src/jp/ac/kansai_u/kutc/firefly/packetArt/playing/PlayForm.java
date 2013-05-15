package jp.ac.kansai_u.kutc.firefly.packetArt.playing;

import jp.ac.kansai_u.kutc.firefly.packetArt.FormBase;
import jp.ac.kansai_u.kutc.firefly.packetArt.FormUtil;
import jp.ac.kansai_u.kutc.firefly.packetArt.PlaySE;
import jp.ac.kansai_u.kutc.firefly.packetArt.music.MidiPlayer;
import jp.ac.kansai_u.kutc.firefly.packetArt.music.MusicPlayer;

import jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump.PcapManager;

import jp.ac.kansai_u.kutc.firefly.packetArt.util.PacketHolder;
import jp.ac.kansai_u.kutc.firefly.packetArt.util.PacketUtil;
import jp.ac.kansai_u.kutc.firefly.packetArt.handlers.PacketHandler;

import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;
import org.jnetpcap.util.PcapPacketArrayList;

import jp.ac.kansai_u.kutc.firefly.packetArt.setting.MinoType;
import jp.ac.kansai_u.kutc.firefly.packetArt.setting.ConfigStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * パケットを利用したテトリスを表示、処理するフォームです。
 *
 * @author midolin
 */
public class PlayForm extends FormBase implements ActionListener {
	PacketrisModel<PacketBlock> model;

	LinkedList<Integer> keyQueue;
	HashMap<Integer, Long> keyPressedTime;
	int keySensitivity = 10;
	long tick;
	int falldownLimit;
	long falldownTimer;
	int minoSize;
	Thread musicplayer; // ゲームBGM用のスレッドを用意。
	boolean isPaused = false;
	boolean isGranded = false;

    Point topLeft;
    Point scoreTopLeft;
    PcapManager pcapManager = PcapManager.getInstance();
    PacketHolder packetHolder = new PacketHolder();//メモリを考え、一個しか使いません。
    PlaySE playSE = PlaySE.getInstance();

	JButton[] buttons = new JButton[]{
			new JButton("リトライ"),
			new JButton("タイトルに戻る")
	};


	/**
	 * キー入力に対する敏感さを取得します。この値は0から60までの値をとります。
	 *
	 * @return キー入力に対する敏感さ
	 */
	public int getKeySensitivity() {
		return keySensitivity;
	}

	/**
	 * キー入力の敏感さを設定します。<br>
	 * キー入力の敏感さとは、キーが押しっぱなしにされていた時に、どのくらいの時間押しっぱなしにされていた場合、 連続入力とみなすかを設定する値です。<br>
	 * 0から60までの値を取り、それ以外の値が入力されると、その範囲に収まるように補正されます。
	 *
	 * @param keySensitivity 敏感さ。0から60(フレーム)。デフォルトで10f;
	 */
	public void setKeySensitivity(int keySensitivity) {
		// 範囲外なら補正する
		if (keySensitivity < 0)
			keySensitivity = 0;
		else if (keySensitivity > 60)
			keySensitivity = 60;

		this.keySensitivity = keySensitivity;
	}

	public PlayForm() {
		model = new PacketrisModel<PacketBlock>(new PacketBlock());
		falldownLimit = 60;
		addComponentListener(new ComponentListener() {
			public void componentShown(ComponentEvent e) {
				requestFocusInWindow();
			}

			public void componentResized(ComponentEvent e) {
			}

			public void componentMoved(ComponentEvent e) {
			}

			public void componentHidden(ComponentEvent e) {
			}
		});

		// 各種ボタンの設定
		for (int i = 0; i < buttons.length; i++) {
			JButton item = buttons[i];
			item.setName(i == 0 ? "Retry" : "Quit");
			item.addActionListener(this);
			item.setVisible(false);
			item.setLocation(getSize().width / 3, (getSize().height / 4) * (i + 2));
			item.setSize(getSize().width / 3, getSize().height / 10);
		}
	}

	@Override
	public void initialize() {
		keyQueue = new LinkedList<Integer>();
		keyPressedTime = new HashMap<Integer, Long>();
		model.initialize();
		generateNextBlockFromPacket();
		addKeyListener(this);
		minoSize = (int) (Math.min(getPreferredSize().width / model.column, getPreferredSize().height / model.row) * 0.9);
		topLeft = new Point(
				(getSize().width - (minoSize * model.column)) / 2 + 75,
				(getSize().height - (minoSize * model.row)) / 2);
		scoreTopLeft = new Point((int) (getSize().width * 0.05), (int) (getSize().height * 0.5));

        // ゲームBGMの音楽を鳴らす。
        musicplayer = new MusicPlayer(ConfigStatus.getVolMusic(), 1000, ConfigStatus.isMelody());
        musicplayer.start();

		getContentPane().add(buttons[0], 0);
		getContentPane().add(buttons[1], 0);

		requestFocusInWindow();
	}

	@Override
	public void paint(Graphics g) {
		// TODO: backgrownd

		if (!isPaused) {

			for (PacketBlock item : model.getCurrentMinos()) {
				paintMino(g, item,
						topLeft.x + ((model.parentLocation.getX() + item.location.getX()) * minoSize),
						topLeft.y + ((model.parentLocation.getY() + item.location.getY()) * minoSize));
			}
			for (ArrayList<PacketBlock> column : model.getBoard()) {
				for (PacketBlock item : column) {
					paintMino(g, item,
							topLeft.x + item.location.getX() * minoSize,
							topLeft.y + item.location.getY() * minoSize);
				}
			}
		}


		g.setColor(Color.getHSBColor(0, 0, 0.8f));
		g.drawRoundRect(scoreTopLeft.x, scoreTopLeft.y,
				(int) (getSize().width * 0.25), (int) (getSize().height * 0.3), 5, 5);
		g.drawString("段数:" + model.getElaseLines(), scoreTopLeft.x + 20, scoreTopLeft.y + 20);


		if (model.isGameOver()) {
			String over = "Game Over";

			g.setFont(new Font(null, Font.PLAIN, 60));
			int x = getSize().width / 2 - g.getFontMetrics().stringWidth(over) / 2;
			int y = getSize().height / 4;

			// ドロップシャドウのような効果
			g.setColor(Color.BLACK);

			g.drawString(over, x - 3, y);
			g.drawString(over, x + 3, y);
			g.drawString(over, x, y - 3);
			g.drawString(over, x, y + 3);

			g.setColor(Color.getHSBColor(0.0f, 0.85f, 0.7f));
			g.drawString(over, x, y);
		}

		// 境界線
		Color borderColor = Color.getHSBColor(0f, 0.8f, 0.5f);
		g.setColor(new Color(borderColor.getRed(), borderColor.getBlue(), borderColor.getGreen(), (int) (((Math.sin(tick / 150) + 1) * 0.5) * 170)));
		g.fillRoundRect(topLeft.x, topLeft.y + minoSize * 3, model.column * minoSize, 5, 2, 2);
	}

	// TODO: painting PacketBlocks using packet data.
	void paintMino(Graphics g, PacketBlock block, int x, int y) {
		float hue, saturation, brightness;

		saturation = brightness = 0.7f;
/*
		if (block.getPacket() != null) {
			// 仮
			saturation = 1 / block.getPacket().size();
			brightness = 1 / block.getPacket().size();
		}*/

		if (block.blockType == BlockType.Wall) {
			g.setColor(Color.blue);
			g.fillRect(x, y, minoSize - 1, minoSize - 1);
		} else if (block.blockType == BlockType.Mino) {
			// 色の計算
			hue = 360 / (block.getMino().ordinal() * (block.getMino() instanceof TetroMino ? 30.0f : 5.4f));

			System.out.println("hue => " + hue + ", br => " + brightness + ", sat => " + saturation);
			// 色の決定
			Color mino = Color.getHSBColor(hue, saturation, brightness);
			g.setColor(mino);
			g.fillRoundRect(x, y, minoSize - 2, minoSize - 2, 2, 2);    // 外枠
			brightness *= 1.3;
			g.setColor(Color.getHSBColor(hue, saturation, brightness)); // 内枠
			g.fillRoundRect(x + 2, y + 2, minoSize - 4, minoSize - 4, 2, 2);
			g.setColor(new Color(Color.white.getRed(), Color.white.getGreen(), Color.white.getBlue(), 200));
			g.fillOval(x + 5, y + 5, minoSize / 10, minoSize / 10);     // ハイライト
		}
	}

	@Override
	public void update() {
		// 入力されたキーを配列へ
		List<Integer> keys = new ArrayList<Integer>();
		while (keyQueue.size() != 0 && keyPressedTime.size() != 0) {
			// 未来の話なら抜ける(そんなことあり得るのか)
			// 中断したら落ちる
			int tgt = keyQueue.get(0);
			if (keyPressedTime.containsKey(tgt))
				if (keyPressedTime.get(tgt) > tick)
					break;
			//if (keyPressedTime.get(keyQueue.get(0)) < tick)
			keys.add(keyQueue.pop());
		}

		// ゲームオーバー判定
		if (model.isGameOver()) {
			// JDK 8のラムダ式が利用できればこんなコードにはならなかった(はず)
			new Thread() {
				@Override
				public void run() {
					try {
						// ボタン表示待ち
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					for (int i = 0; i < buttons.length; i++) {
						buttons[i].setVisible(true);
						buttons[i].setEnabled(true);
					}
					getContentPane().validate();

					// ゲームオーバーになったときにBGMを止める。
					if (MusicPlayer.getSequencer() != null) {
						((MusicPlayer) musicplayer).stopMusic();
					}
				}
			}.run();
			return;
		}

		if (keys.contains(KeyEvent.VK_ESCAPE)) {
			isPaused = !isPaused;
			System.out.print(isPaused);
		}
		if (isPaused)
			return;

        // キー入力の処理
        for (int key : keys) {
            if (key == ConfigStatus.getKeyLeftSpin()) {
                model.rotate(Direction.Left);
                playSE.play(PlaySE.TURN);
            }
            if (key == ConfigStatus.getKeyRightSpin()) {
                model.rotate(Direction.Right);
                playSE.play(PlaySE.TURN);
            }
            if (key == ConfigStatus.getKeyLeft()) {
                model.translate(Direction.Left);
                playSE.play(PlaySE.MOVE);
            }
            if (key == ConfigStatus.getKeyRight()) {
                model.translate(Direction.Right);
                playSE.play(PlaySE.MOVE);
            }
            if (key == ConfigStatus.getKeyDown()) {
                model.fallDown();
                falldownTimer = 0;
            }
            if (key == ConfigStatus.getKeyUp()) {
                playSE.play(PlaySE.HARDDROP);
                while (model.fallDown()) {
                }
                falldownTimer = 0;
                generateNextBlockFromPacket();
            }
        }

		// もし指定のタイミングになったらfalldown
		if (falldownTimer > falldownLimit) {
			//System.out.println("falling - " + model.parentLocation.toString());
			falldownTimer = 0;

			// 接地済みなら新しく生成
			if (!model.fallDown()) {
				generateNextBlockFromPacket();
				System.out.println(model);
				System.out.println("generate - " + model.parentLocation);
			}

			model.deleteLines();
            //if (model.deleteLines() > 0) {
            //	PlaySE.getInstance().play(PlaySE.DEMISE);
            //}
        } else {
            falldownTimer++;
        }

		tick++;
	}

    private void generateNextBlockFromPacket() {
    	PcapPacket pkt;
    	int tetroNum = -1;
    	int pentoNum = -1;
    	//int cols = 0:
    	do {
        	pkt = pcapManager.nextPacketFromQueue();
        	//TODO
        	//今はpktが来るまでぶん回しているが、空になった時点でchangeFormを呼び出しreadDumpFormへうつらせる。
        	//
        	//if (pcapManager.isReadyRun() == false) {
        		//FormUtil.getInstance().changeForm("ReadDump");
        	//}
    	} while(pkt == null);
    	packetHolder.setPacket(pkt);//パケットが到着。パケットをパケホルダーへ。
    	if (packetHolder.hasIp4()) {//このパケットはIPv4を含む
        	//System.out.println("IPv4 has come");
    		if (packetHolder.hasTcp()) {
            	//System.out.println("TCP has come");
            	tetroNum = (int)packetHolder.getTcp().seq() & 0x00007fff;
            	pentoNum = (int)packetHolder.getTcp().checksum() & 0x00007fff;
            	//ほどよくバラけたTCP のシーケンス、チェックサムを使う。
    		} else if (packetHolder.hasUdp()) {
            	//System.out.println("UDP has come");
            	tetroNum = packetHolder.getUdp().source();
            	pentoNum = packetHolder.getUdp().destination();
            	//少し偏ったUDPの送信先、宛先ポートを使う
    		} else {
//    			System.out.println("NO TCP & UDP!?  then, IPID SEQ NUMBER!!");
    			tetroNum = packetHolder.getIp4().id();
    			pentoNum = packetHolder.getIp4().checksum();
    			//少し偏ったIP ID シーケンス、適当にチェックサムを使う。
			}
    	} else if (packetHolder.hasIp6()){//このパケットはIPv6を含むパケットである。
        	//System.out.println("IPv6 has come");
        	if (packetHolder.hasTcp()) {
            	//System.out.println("TCP has come");
            	tetroNum = (int)packetHolder.getTcp().seq() & 0x00007fff;
            	pentoNum = packetHolder.getTcp().flags();
            	//ほどよくバラけたTCP のシーケンス、適当にフラグセットを使う。
    		} else if (packetHolder.hasUdp()) {
            	//System.out.println("UDP has come");
            	tetroNum = packetHolder.getUdp().checksum();
            	pentoNum = packetHolder.getUdp().destination();
            	//バラけたチェックサム、なんとなくまとまった宛先ポートを使う。
    		} else {
    			//System.out.println("NO TCP & UDP!? using IPv6 ");
    			tetroNum = packetHolder.getIp6().flowLabel();
    			pentoNum = packetHolder.getIp6().length();
    			//ほどよくまとまったフローラベルセット、データ長を使う。
    		}
    	}
    	if (tetroNum < 0) {
    		//IPv4,IPv6ではないパケット。デバイスからロードでもない限り非常に稀である。
    		//謎のプロトコルを探るくらいなら、絶対に使える値をとる。
    		tetroNum = (int)PacketUtil.getCaplen(pkt) & 0x00007fff;
    	}
    	if (pentoNum < 0) {
    		//同上
    		pentoNum = (int)PacketUtil.getMilliTimeStamp(pkt) & 0x00007fff;
    	}

    	//if (sum % ConfigStatus.MT)
		//System.out.println("sum = " + sum);
    	switch(ConfigStatus.getMino()){
		case Tetro:
			model.generateMino(TetroMino.values()[tetroNum % 7], false, model.column / 2);
			break;
		case Pento:
			model.generateMino(PentoMino.values()[pentoNum % 12], false, model.column / 2);
			break;
		case Both:
			if (packetHolder.hasIp4() && packetHolder.hasUdp()) {//IPv4でTCPなら４ブロックのミノ
				model.generateMino(TetroMino.values()[tetroNum % 7], false, model.column / 2);
			} else {//それ以外なら5ブロックのミノ
				model.generateMino(PentoMino.values()[pentoNum % 12], false, model.column / 2);
			}
			break;
		}
		
        ArrayList<PacketBlock> mino = model.getCurrentMinos();
		for(int i = 0; i < mino.size(); i++){
			if(i == 0)
				presentPacket(mino.get(i), pkt);
			else
				presentPacket(mino.get(i));
		}
    }
    
	private void presentPacket(PacketBlock packetBlock) {
        packetBlock.packet = pcapManager.nextPacketFromQueue();
    }

	private void presentPacket(PacketBlock packetBlock, PcapPacket pkt) {
		packetBlock.packet = pkt;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		long time = tick;

		System.out.println("input key:" + key);

		if (!keyPressedTime.containsKey(key)) {
			keyPressedTime.put(key, time);
			keyQueue.push(key);
		}

		if (keyPressedTime.get(key) - time > keySensitivity)
			keyQueue.push(key);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keyPressedTime.remove(e.getKeyCode());
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void onClose() {
		onFormChanged();
	}

	@Override
	public void onFormChanged() {
		keyQueue = null;
		keyPressedTime = null;
		for (JButton item : buttons) {
			getContentPane().remove(item);
		}
		//model = null;
		removeKeyListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		// JButtonの挙動を定義
		if (e.getSource() instanceof JButton) {
			// タイトルに帰る
			for (int i = 0; i < buttons.length; i++) {
				buttons[i].setVisible(false);
				buttons[i].setEnabled(false);
			}
			getContentPane().validate();

			if (((JButton) (e.getSource())).getName() == "Retry") {
				// ボタンがRetryならボタンを消して再初期化
				initialize();
			} else {
				new Thread() {
					@Override
					public void run() {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e1) {
							e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
						}
						FormUtil.getInstance().changeForm("Title");
					}
				}.run();
			}

		}
	}
}
