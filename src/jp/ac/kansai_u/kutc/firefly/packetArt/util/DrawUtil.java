package jp.ac.kansai_u.kutc.firefly.packetArt.util;

import java.lang.Math;
import java.awt.Point;

public class DrawUtil {
    private DrawUtil(){};

    /**
     * 北の方角を0度とし、radを基軸の角度としdRad度相対的に回転した時の、<br>
     * 絶対角度を取得します。例：30度+ (-390)度=330度<br>
     *
     * @param rad 基軸の角度
     * @param dRad 回転量
     * @return 回転後の絶対角度
    */
    public static int relativeRad(int rad, int dRad) {
        int rolls = rad % 360 + dRad % 360;//一回転は同一のものとみなす
        if (rolls < 0 ) {
            rolls = 360 - rolls;// 例：-330度 = 30度
        }
        return rolls;
    }

    /**
     * 極座標系で座標計算をする関数です。<br>
     * 始点座標、角度、長さを与えることで、終点座標を導き、<br>
     * 新たなPointオブジェクトとして返します。
     *
     * @param startPoint 始点の座標。
     * @param dir 棒の進行方角0~360の整数。
     * @param len 棒の長さ。あんまり長すぎると困る。
     * @return 終点の座標をPointで。
    */
    public static Point polarPointing(Point startPoint, int dir, int len) {
        Point endPoint = startPoint.getLocation();
        double sinDir = Math.sin(Math.toRadians(dir));
        double cosDir = Math.cos(Math.toRadians(dir));
        Double dx = new Double(len * sinDir);
        Double dy = new Double(len * cosDir);
        endPoint.translate(dx.intValue(), dy.intValue());
        return endPoint;
    }

    /**
     * Pointのx,yの値を定められた区間の中に無理やり変換します。<br>
     * 50x50の区画には(1230,-112)が(49,0)に変換されて入ります。
     *
     * @param point 変換したい座標
     * @param X 区画のx座標
     * @param Y 区画のy座標 
    */
    public static void pointResolver(Point point,int X,int Y) {
        double x = point.getX();
        double y = point.getY();
        if (x >= X)
            point.setLocation(X-1, y);
        if (x < 0)
            point.setLocation(0, y);
        if (y >= Y)
            point.setLocation(x, Y-1);
        if (y < 0)
            point.setLocation(x, 0);
    }
}
