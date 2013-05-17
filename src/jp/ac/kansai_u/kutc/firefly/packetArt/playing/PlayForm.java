package jp.ac.kansai_u.kutc.firefly.packetArt.playing;

import jp.ac.kansai_u.kutc.firefly.packetArt.FormBase;
import jp.ac.kansai_u.kutc.firefly.packetArt.FormUtil;
import jp.ac.kansai_u.kutc.firefly.packetArt.Location;
import jp.ac.kansai_u.kutc.firefly.packetArt.PlaySE;
import jp.ac.kansai_u.kutc.firefly.packetArt.music.MusicPlayer;
import jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump.PcapManager;
import jp.ac.kansai_u.kutc.firefly.packetArt.setting.ConfigStatus;
import jp.ac.kansai_u.kutc.firefly.packetArt.util.PacketHolder;
import jp.ac.kansai_u.kutc.firefly.packetArt.util.PacketUtil;
import org.jnetpcap.packet.PcapPacket;

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
    int falldownLimit;   // fallDownCountの限界．ここを変えれば，速度が変わる
    long falldownTimer;
    int minoSize;
    int fallDownCount;
    int fallDownCountLimit;
    Thread musicplayer; // ゲームBGM用のスレッドを用意。
    boolean isPaused = false;
    boolean isGranded = false;
    Location ghost;

    Point topLeft;
    Point scoreTopLeft;
    Point nextTopLeft;
    PcapManager pcapManager = PcapManager.getInstance();
    PacketHolder packetHolder = new PacketHolder();//メモリを考え、一個しか使いません。
    PlaySE playSE = PlaySE.getInstance();

    JButton[] buttons = new JButton[2];
    
    private boolean isInitialized = false;
	private boolean isStart = false;
	private int strX = 600, strY = 300;


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
        falldownLimit = 100;

        buttons[0] = new JButton("最初から");
        buttons[1] = new JButton("タイトルへ戻る");

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
            buttons[i].setName(i == 0 ? "Retry" : "Quit");
            buttons[i].setHorizontalAlignment(JButton.CENTER);
            buttons[i].setFont(new Font("IPAexゴシック", Font.PLAIN, 20));
            buttons[i].addActionListener(this);
        }
    }

    @Override
    public void initialize() {
//        requestFocusInWindow();
    	isStart = false;
        if (!PcapManager.getInstance().isReadyRun()) {
            FormUtil.getInstance().changeForm("ReadDump");
            JOptionPane.showMessageDialog(null, "正常に開始することができませんでした。はじめにパケットをロードしてください。",
                    "正常に開始することができません", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (PcapManager.getInstance().getQueueLeft() < 1000) {
            FormUtil.getInstance().changeForm("ReadDump");
            JOptionPane.showMessageDialog(null, "パケットを最低でも1000個ロードしてください",
                    "パケット不足", JOptionPane.ERROR_MESSAGE);
            return;
        }
        keyQueue = new LinkedList<Integer>();
        keyPressedTime = new HashMap<Integer, Long>();
        model.initialize();
        // CurrentとNextを生成(ネクネク以上が必要なら、これを繰り返し呼ぶ)
        for (int i = 0; i < 20; i++)
            generateNextBlockFromPacket();

        addKeyListener(this);
        addMouseListener(this);
        minoSize = (int) (Math.min(getPreferredSize().width / model.column, getPreferredSize().height / model.row) * 0.9);
        topLeft = new Point(
                (getSize().width - (minoSize * model.column)) / 2 + 75,
                (getSize().height - (minoSize * model.row)) / 2);
        scoreTopLeft = new Point((int) (getSize().width * 0.05), (int) (getSize().height * 0.5));
        nextTopLeft = new Point((int) (getSize().getWidth() * 0.05), (int) (getSize().height * 0.05));

        // ゲームBGMの音楽を鳴らす。
        musicplayer = new MusicPlayer(ConfigStatus.getVolMusic(), 1000, ConfigStatus.isMelody());
        musicplayer.start();

        fallDownCountLimit = 3;
        fallDownCount = 0;

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setVisible(false);
            buttons[i].setBounds(getPreferredSize().width / 3, (getPreferredSize().height / 4) * (i + 2),
                    getPreferredSize().width / 3, getPreferredSize().height / 10);
            if (buttons[i].getParent() == null)
                getContentPane().add(buttons[i], 0);
        }
        isInitialized = true;
    }

    @Override
    public void paint(Graphics g) {
        if (!PcapManager.getInstance().isReadyRun()) return;
        if (!isStart){
        	String str = new String("マウスクリックでゲーム開始");
        	Font f = new Font("MONOSPACE", Font.PLAIN, 40);
        	g.setFont(f);
        	g.setColor(Color.darkGray);
        	g.drawString(str, strX, strY);
        	if(strX < -500) strX=600;
        	return;
        }
        if (!isPaused) {
            for (PacketBlock item : model.getCurrentMinos()) {
                paintMino(g, item,
                        topLeft.x + ((model.parentLocation.getX() + item.location.getX()) * minoSize),
                        topLeft.y + ((model.parentLocation.getY() + item.location.getY()) * minoSize));
            }
            // ゴースト描画
            for (PacketBlock item : model.getCurrentMinos()) {
                g.drawRect(
                        topLeft.x + (ghost.getX() + item.location.getX()) * minoSize,
                        topLeft.y + (ghost.getY() + item.location.getY()) * minoSize,
                        minoSize - 2, minoSize - 2);
            }

            for (ArrayList<PacketBlock> column : model.getBoard()) {
                for (PacketBlock item : column) {
                    paintMino(g, item,
                            topLeft.x + item.location.getX() * minoSize,
                            topLeft.y + item.location.getY() * minoSize);
                }
            }


        }

        // PAINT SCORE AREA
        g.setColor(Color.getHSBColor(0, 0, 0.8f));
        g.drawRoundRect(scoreTopLeft.x, scoreTopLeft.y,
                (int) (getSize().width * 0.25), (int) (getSize().height * 0.3), 5, 5);
        g.drawString("Score : " + model.getElaseLines() * 10, scoreTopLeft.x + 20, scoreTopLeft.y + 20);

        // PAINT NEXT AREA
        g.setColor(Color.getHSBColor(0, 0, 0.8f));
        g.drawRoundRect(nextTopLeft.x, nextTopLeft.y,
                (int) (getSize().width * 0.25), (int) (getSize().height * 0.3), 5, 5);
        g.drawString("NEXT:", nextTopLeft.x + 20, nextTopLeft.y + 20);
        for (PacketBlock item : model.getNextMinos()) {
            paintMino(g, item,
                    nextTopLeft.x + 60 + item.location.getX() * minoSize,
                    nextTopLeft.y + 60 + item.location.getY() * minoSize);
        }


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

    void paintMino(Graphics g, PacketBlock block, int x, int y) {
        /*
          static Color 	getHSBColor(float h, float s, float b)
          HSB カラーモデルに指定された値に基づいて Color オブジェクトを生成します。
		 */
        float pos, saturation, brightness;
        saturation = 1.0f;//鮮やかさ。固定。
        brightness = 0.7f;//明るさのデフォルト値
        pos = 0.0f;
        float delim = 0.142f;
        if (block.blockType == BlockType.Wall) {
            g.setColor(Color.blue);//壁ブロックたちなら青でベタ塗り
            g.fillRect(x, y, minoSize - 1, minoSize - 1);
        } else if (block.blockType == BlockType.Mino) { //ミノたちなら
            if (block.getMino() instanceof PentoMino) {
                delim = 0.083f;
            }
            pos = delim / 2;
            PcapPacket pkt = null;
            if ((pkt = block.getPacket()) != null) {//ブロックがぱけっとを含む場合
                packetHolder.setPacket(pkt);//パケットをパケホルダーに装填し
                //tetroMinoは7種類、pentoMinoは12種類。
                //明示的な順番はないので、TetroMino.values()[tetroNum % 7]を使う。
                //hue,sat,bri共にfloatで1.0～0.0の値なので、
                //tetroMinoは0.142区切り、
                //pentoMinoは0.0833区切りとする。
                //なお、hueについては一周が可能なため、あふれは気にしないことにする。
                //デフォルトで中間のhueに行くように設定
                //パケットの最上階レイヤのデータサイズ（つまりは合計ヘッダサイズ）が大きいミノほど
                //HUEの色が回転しより次の色に近い色になります。
                //パケットの最上階レイヤがOSI参照モデル的に低い層は、レイヤ層が低いほど
                //色の明るさが暗くなります。

                //なお、プログラムの仕様上、上級ユーザほど
                //レイヤの高いテトリミノを駆使してゲームをしなければなりません。
                //この場合の上級テトリミノとは、「ペントミノ」のことです。
                if (packetHolder.hasTcp()) {
                    pos = packetHolder.getTcp().checksum() % delim * 0.001f;
                    brightness = 1.0f;
                } else if (packetHolder.hasUdp()) {
                    pos = packetHolder.getUdp().checksum() % delim * 0.001f;
                    brightness = 0.9f;
                } else if (packetHolder.hasIcmp()) {
                    pos = packetHolder.getIcmp().checksum() % delim * 0.001f;
                    brightness = 0.8f;
                } else if (packetHolder.hasIp6()) {
                    pos = packetHolder.getIp6().length() % delim * 0.001f;
                    brightness = 0.7f;
                } else if (packetHolder.hasIp4()) {
                    pos = packetHolder.getIp4().checksum() % delim * 0.001f;
                    brightness = 0.6f;
                } else if (packetHolder.hasEthernet()) {
                    pos = packetHolder.getEthernet().checksumOffset() % delim * 0.001f;
                    brightness = 0.2f;
                } else {
                    pos = PacketUtil.getCaplen(packetHolder.getPacket()) % delim * 0.001f;
                    brightness = 0.1f;
                }
                //System.out.println("hue => " + pos + block.getMino().ordinal() * delim + ", br => " + brightness + ", sat => " + saturation);
                // 色の決定
                //hue = 3.6f / (block.getMino().ordinal() * (block.getMino() instanceof TetroMino ? 30.0f : 51.4f));

            } else {
                //ブロックがパケットを含まない場合
                //デフォルトの値が使われる。
            }
            float hue = pos + block.getMino().ordinal() * delim;
            Color mino = Color.getHSBColor(hue, saturation, brightness);
            g.setColor(mino);
            g.fillRoundRect(x, y, minoSize - 2, minoSize - 2, 2, 2);    // 外枠
            brightness *= 1.3;
            g.setColor(Color.getHSBColor(hue, saturation, brightness)); // 内枠
            g.fillRoundRect(x + 2, y + 2, minoSize - 4, minoSize - 4, 2, 2);
            g.setColor(new Color(Color.white.getRed(), Color.white.getGreen(), Color.white.getBlue(), 200));
            g.fillOval(x + 5, y + 5, minoSize / 10, minoSize / 10); // ハイライト
            int posX = 9;
            int posY = 19;
            if (ConfigStatus.isViewLog() && pkt != null) {//もしログフラグが立っていたら
                Graphics2D g2 = (Graphics2D) g;
                Font font = new Font("MONOSCAPE", Font.BOLD, 16);
                g2.setFont(font);
                if (packetHolder.hasTcp()) {
                    if (packetHolder.hasIp4()) {
                        g.setColor(Color.YELLOW);//IPv4が下の時の文字色
                    } else if (packetHolder.hasIp6()) {
                        g.setColor(Color.BLUE);//IPv6が下の時の文字色
                    } else {
                        g.setColor(Color.RED);//それ以外が下の時の文字色
                    }
                    g2.drawString("T", x + posX, y + posY);
                } else if (packetHolder.hasUdp()) {
                    if (packetHolder.hasIp4()) {
                        g.setColor(Color.GREEN);//IPv4が下の時の文字色
                    } else if (packetHolder.hasIp6()) {
                        g.setColor(Color.BLUE);//IPv6が下の時の文字色
                    } else {
                        g.setColor(Color.RED);//それ以外が下の時の文字色
                    }
                    g2.drawString("U", x + posX, y + posY);
                    //OSI参照モデルにおいて。ICMPはL3だが、
                    //一般に、ICMPの上にTCPやUDPが来ることは少ないのでここで処理。
                } else if (packetHolder.hasIcmp()) {
                    if (packetHolder.hasEthernet()) {
                        g.setColor(Color.CYAN);//IPv4が下の時の文字色
                    } else if (packetHolder.hasPPP()) {
                        g.setColor(Color.BLUE);//PPPが下の時の文字色
                    } else {
                        g.setColor(Color.WHITE);//それ以外が下の時の文字色
                    }
                    g.drawString("I", x + posX, y + posY);
                } else {
                    //TCP,UDP,ICMPを含まないパケットはこちら
                    if (packetHolder.hasIp6()) {
                        if (packetHolder.hasEthernet()) {
                            g.setColor(Color.RED);//Ethernetが下の時の文字色
                        } else if (packetHolder.hasPPP()) {
                            g.setColor(Color.BLUE);//PPPが下の時の文字色
                        } else {
                            g.setColor(Color.WHITE);//それ以外が下の時の文字色
                        }
                        g.drawString("6", x + posX, y + posY);
                    } else if (packetHolder.hasIp4()) {
                        if (packetHolder.hasEthernet()) {
                            g.setColor(Color.MAGENTA);//Ethernetが下の時の文字色
                        } else if (packetHolder.hasPPP()) {
                            g.setColor(Color.BLUE);//PPPが下の時の文字色
                        } else {
                            g.setColor(Color.WHITE);//それ以外が下の時の文字色
                        }
                        g.drawString("4", x + posX, y + posY);
                    } else {
                        //ここ、L3調査がメインなWIDEで来ること無いと思う。
                        //「デバイスからロード」の場合のみ見られるであろう。珍しいプロトコル。
                        //レアだぜ
                        g.setColor(new Color(239, 69, 74));
                        if (packetHolder.hasL2TP()) {
                            g.drawString("L", x + posX, y + posY);
                        } else if (packetHolder.hasPPP()) {
                            g.drawString("P", x + posX, y + posY);
                        } else if (packetHolder.hasArp()) {
                            g.drawString("A", x + posX, y + posY);
                        } else if (packetHolder.hasEthernet()) {
                            g.drawString("E", x + posX, y + posY);
                        } else {
                            g.drawString("!", x + posX, y + posY);
                            //ここにきたプロトコルはすごく気持ち悪いぞ！
                        }
                    }
                }
            }
        }
    }

    @Override
    public void update() {
        if (!PcapManager.getInstance().isReadyRun()) {
            if (!PcapManager.getInstance().isReadyRun()) {
                if (isInitialized) {
                	
                    JOptionPane.showMessageDialog(null, "全パケットを消費しました",
                            "おめでとうございます", JOptionPane.INFORMATION_MESSAGE);
                    FormUtil.getInstance().changeForm("Title");
                    isInitialized = false;
                }
                return;
            }
            return;
        }
        if (!isStart){
        	strX -= 1;
        	return;
        }
        // 入力されたキーを配列へ
        List<Integer> keys = new ArrayList<Integer>();
        while (keyQueue.size() != 0 && keyPressedTime.size() != 0) {
            // 未来の話なら抜ける
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

            // ゲームオーバーになったときにBGMを止める。
            if (MusicPlayer.getSequencer() != null) {
                ((MusicPlayer) musicplayer).stopMusic();
            }
            for (int i = 0; i < buttons.length; i++) {  // Restart以外のボタンの可視化，有効化
                buttons[i].setVisible(true);
                buttons[i].setEnabled(true);
            }

            getContentPane().validate();
            return;
        }

        if (keys.contains(KeyEvent.VK_ESCAPE)) {
            isPaused = !isPaused;
            System.out.print(isPaused);
            for (int i = 0; i < buttons.length; i++) {
                buttons[i].setVisible(isPaused);
                buttons[i].setEnabled(isPaused);
            }
            getContentPane().validate();
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
                model.fixMino();
                //model.deleteLines();
                falldownTimer = 0;
                generateNextBlockFromPacket();
                return;
            }
        }

        ghost = model.parentLocation;
        while (model.canAllocate(ghost.add(0, 1))) {
            ghost = ghost.add(0, 1);
        }

        // もし指定のタイミングになったらfalldown
        if (falldownTimer > falldownLimit) {
            //System.out.println("falling - " + model.parentLocation.toString());
            falldownTimer = 0;

            // 接地済みなら新しく生成
            if (!model.fallDown()) {
                fallDownCount++;
                if (fallDownCount > fallDownCountLimit) {
                    model.fixMino();
                    model.deleteLines();
                    generateNextBlockFromPacket();
                    fallDownCount = 0;
                    System.out.println(model);
                    System.out.println("generate - " + model.parentLocation);
                }
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
            
            if (pcapManager.isReadyRun() == false) {
                FormUtil.getInstance().changeForm("ReadDump");
            }
        } while (pkt == null);
        packetHolder.setPacket(pkt);//パケットが到着。パケットをパケホルダーへ。
        if (packetHolder.hasIp4()) {//このパケットはIPv4を含む
            //System.out.println("IPv4 has come");
            if (packetHolder.hasTcp()) {
                //System.out.println("TCP has come");
                tetroNum = (int) packetHolder.getTcp().seq() & 0x00007fff;
                pentoNum = (int) packetHolder.getTcp().checksum() & 0x00007fff;
                //ほどよくバラけたTCP のシーケンス、チェックサムを使う。
            } else if (packetHolder.hasUdp()) {
                //System.out.println("UDP has come");
                tetroNum = packetHolder.getUdp().source();
                pentoNum = packetHolder.getUdp().destination();
                //少し偏ったUDPの送信先、宛先ポートを使う
            } else {
                tetroNum = packetHolder.getIp4().id();
                pentoNum = packetHolder.getIp4().checksum();
                //少し偏ったIP ID シーケンス、適当にチェックサムを使う。
            }
        } else if (packetHolder.hasIp6()) {//このパケットはIPv6を含むパケットである。
            //System.out.println("IPv6 has come");
            if (packetHolder.hasTcp()) {
                //System.out.println("TCP has come");
                tetroNum = (int) packetHolder.getTcp().seq() & 0x00007fff;
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
            tetroNum = (int) PacketUtil.getCaplen(pkt) & 0x00007fff;
        }
        if (pentoNum < 0) {
            //同上
            pentoNum = (int) PacketUtil.getMilliTimeStamp(pkt) & 0x00007fff;
        }

        switch (ConfigStatus.getMino()) {
            case Tetro:
                model.generateMino(TetroMino.values()[tetroNum % 7], false, model.column / 2);
                break;
            case Pento:
                model.generateMino(PentoMino.values()[pentoNum % 12], false, model.column / 2);
                break;
            case Both:
                if (packetHolder.hasIp4()) {//IPv4なら４ブロックのミノ
                    model.generateMino(TetroMino.values()[tetroNum % 7], false, model.column / 2);
                } else {//それ以外なら5ブロックのミノ
                    model.generateMino(PentoMino.values()[pentoNum % 12], false, model.column / 2);
                }
                break;
        }
        if (model.nextQueue.size() > 10)
            model.popNextQueue();

        ArrayList<PacketBlock> mino = model.getNextMinos();
        for (int i = 0; i < mino.size(); i++) {
            if (i == 0)
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

    public void actionPerformed(ActionEvent e) {
        // JButtonの挙動を定義
        if (e.getSource() instanceof JButton) {
            isPaused = false;
            // ボタンの不可視と無効
            for (int i = 0; i < buttons.length; i++) {
                buttons[i].setVisible(false);
                buttons[i].setEnabled(false);
            }

            if (((JButton) (e.getSource())).getName() == "Restart")
                ;
            else {
                if (MusicPlayer.getSequencer() != null)  //音楽再生中ならストップ
                    ((MusicPlayer) musicplayer).stopMusic();

                if (((JButton) (e.getSource())).getName() == "Retry")  //Retryなら再初期化
                    initialize();
                else if (((JButton) (e.getSource())).getName() == "Quit") //Quitなら，フォームチェンジ
                    FormUtil.getInstance().changeForm("Title");
            }
        }
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

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        long time = tick;

//        System.out.println("input key:" + key);

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
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    	isStart = true;
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
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
