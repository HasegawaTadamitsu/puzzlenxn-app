package jp.ddo.haselab.puzznxn;

import java.util.Random;
//import android.util.Log;

/**
 * puzzle処理.

 * @author T.Hasegawa
 */
final class Puzzle {

    /** 行と列.*/
    private int row, line;

    /** 現在のピースの配置です.*/
    private int[]  block;

    /** 
     * 乱数のインスタンスになります。
     * このインスタンスより乱数を取得します。
     */
    private Random random;

    /**
     * ブランクのピースを意味します.
     */
    public static final int BLANK = -1;

    /**
     * ブランクのピースの移動先を意味します。
     */
    public static final int UP    = 0;

    /**
     * ブランクのピースの移動先を意味します。
     */
    public static final int LEFT  = 1;

    /**
     * ブランクのピースの移動先を意味します。
     */
    public static final int DOWN  = 2;

    /**
     * ブランクのピースの移動先を意味します。
     */
    public static final int RIGHT = 3;

    /**
     * コンストラクタ.
     * ブロックの初期化など行います。
     * @param argRow 列
     * @param argLine 行
     */
    public Puzzle(final int argRow, final int argLine) {

        row = argRow;
        line = argLine;
        random = new Random();

        block = new int[row * line];
        for (int i = 0; i < row * line; i++) {
            block[i] = i;
        }
        block[ row * line - 1] = -1;
    }

    /**
     * ブランクピースの場所を返します.
     * @return ブランクピース
     */
    public int whereBlank() {
        for (int i = 0; i < row * line; i++) {
            if (block[i] == BLANK) {
                return i;
            }
        }
        throw new RuntimeException("unknown Blank");
    }

    /**
     * ピースを返します。
     * @return ピース
     */
    public int[] getBlock() {
        return block;
    }

    /**
     * シャッフルを行います。
     * 乱数で方向を決め、
     * ブランクピースを移動させます。
     * 移動できない方向に乱数がなっても、一回と数えます。
     * @param argCount シャッフルする回数
     */
    public void shuffle(final int argCount) {
        for (int i = 0; i < argCount; i++) {
            int val = random.nextInt(4);
            move(val);
        }
    }

    /**
     * 完成かどうか判断します.
     * 行＊列-1のループで順番どおり並んでいるか
     * 判断します。
     * return true 完成
     */
    public boolean isComplete() {
        for (int i = 0; i < row * line - 1; i++) {
            if (block[i] != i) {
                return false;
            }
        }
        return true;
    }

    /**
     * ブランクピースの移動.
     * ブランクピースを移動させます。
     * @param argDir 方向
     * @return true移動可能
     */
    public boolean move(final int argDir) {
        int blankPos = whereBlank();
        int newPos = 0;
        switch(argDir) {
        case UP:
            if ((blankPos / row) == 0) {
                return false;
            }
            newPos = blankPos - row;
            break;
        case LEFT:
            if ((blankPos % row) == 0) {
                return false;
            }
            newPos = blankPos - 1;
            break;
        case RIGHT:
            if ((blankPos % row) + 1 == row) {
                return false;
            }
            newPos = blankPos + 1;
            break;
        case DOWN:
            if (((blankPos) / row) + 1 == line) {
                return false;
            }
            newPos = blankPos + row;
            break;
        default:
            throw new RuntimeException("unknown dir" + argDir);
        }

        int blankVal = block[blankPos];
        int newVal   = block[newPos];
        block[blankPos] = newVal;
        block[newPos]   = blankVal;
        return true;
    }

    /**
     * ブランクピースの移動.
     * ある場所を指定し、ブランクピースをその方向に
     * ひとつ移動させます。
     * @param argPos 場所
     * @return true移動可能
     */
    public boolean moveFromPos(final int argPos) {
        int blankPos = whereBlank();

        if (argPos == blankPos) {
            return false;
        }

        int blankPosX = blankPos % row;
        int blankPosY = blankPos / row;

        int argPosX = argPos % row;
        int argPosY = argPos / row;

        if (blankPosX == argPosX) {
            int dir =  (blankPosY < argPosY) ? DOWN : UP;
            return move(dir);
        }
        if (blankPosY == argPosY) {
            int dir =  (blankPosX < argPosX) ? RIGHT : LEFT;
            return move(dir);
        }
        return false;
    }
}
