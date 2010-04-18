package jp.ddo.haselab.puzznxn;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * スコア管理. 
 * 本来ならばシングルトンにしたいが,
 * そこまで行う必要はないと判断し,
 * static メソッドの集まりにしてあります。
 *
 * @author T.Hasegawa
 */
final class ScoreMgr {

    /**
     * コンストラクタ. 
     * インスタンス生成を防ぎたいため, private にしてあります.
     */
    private ScoreMgr() {
    }

    /** ファイル出力するkey/valueのkeyです. {@value} */
    private static final String PREF = "PUZZ_NXN";

    /** ファイル出力するkeyです. {@value} */
    private static final String KEY_3X3 = "3X3";

    /** ファイル出力するkeyです. {@value} */
    private static final String KEY_4X4 = "4X4";

    /** スコアのデフォルト値です. {@value} */
    private static final long DEFAULT_VAL = 9 * 60 * 60 * 1000L;

    /** 3X3の時のsizeです. {@value} */
    private static final long SIZE_FOR_3X3 = 3;

    /**
     * スコアの初期化.
     * スコアを初期化します.値は{@link DEFAULT_VAL}です。
     *　@param cont コンテキスト。このコンテキストに対して、値を設定します。
     */
    public static void initScore(final Context cont) {
        setScore3x3(cont, DEFAULT_VAL);
        setScore4x4(cont, DEFAULT_VAL);
    }

    /**
     * スコアの設定.
     * スコアを設定します。現時点では、
     * sizeの値によって、{@link KEY_3X3} もしくは{@link 4X4}に対して
     * 書き込みをします。
     * 保存された値が、引き数の方が悪くても、スコアを設定します。
     *　@param cont コンテキスト。このコンテキストに対して、値を設定します。
     *  @param score long で指定するかかった時間です。単位ms
     *  @param size 3ならば{@link KEY_3X3}。
     *        それ以外ならば{@link 4X4}に対して行います。
     */
    public static void setScore(final Context cont, final long score,
                                    final int size) {
        if (size == SIZE_FOR_3X3) {
            setScore3x3(cont, score);
        } else {
            setScore4x4(cont, score);
        }
    }

    /**
     * ベストスコアの取得.
     * スコアを取得します。現時点では、
     * sizeの値によって、{@link KEY_3X3} もしくは{@link 4X4}に対して
     * 取得します。
     *　@param cont コンテキスト。このコンテキストに対して、値を取得します。
     *  @param size 3ならば{@link KEY_3X3}。
     *        それ以外ならば{@link 4X4}に対して行います。
     * @return スコア
     */
    public static long getScore(final Context cont, final int size) {
        if (size == SIZE_FOR_3X3) {
            return getScore3x3(cont);
        } else {
            return getScore4x4(cont);
        }
    }

    /**
     * スコアの設定.
     * スコアを設定します。
     * {@link KEY_3X3}に対して、設定します。
     *　@param cont コンテキスト。このコンテキストに対して、値を取得します。
     *  @param arg 設定する値。単位ms
     */
    private static void setScore3x3(final Context cont, final long arg) {
        SharedPreferences.Editor ed = cont.getSharedPreferences(PREF, 0).edit();
        ed.putLong(KEY_3X3, arg);
        ed.commit();
    }

    /**
     * スコアの設定.
     * スコアを設定します。
     * {@link KEY_4X4}に対して、設定します。
     *　@param cont コンテキスト。このコンテキストに対して、値を取得します。
     *  @param arg 設定する値。単位ms
     */
    private static void setScore4x4(final Context cont, final long arg) {
        SharedPreferences.Editor ed = cont.getSharedPreferences(PREF, 0).edit();
        ed.putLong(KEY_4X4, arg);
        ed.commit();
    }

    /**
     * スコアの取得.
     * スコアを取得します。
     * {@link KEY_3X3}に対して、取得します。
     *　@param cont コンテキスト。このコンテキストに対して、値を取得します。
     *  @return スコア 単位ms
     */
    public static long getScore3x3(final Context cont) {
        SharedPreferences sp = cont.getSharedPreferences(PREF, 0);
        return sp.getLong(KEY_3X3, DEFAULT_VAL);
    }

    /**
     * スコアの取得.
     * スコアを取得します。
     * {@link KEY_4X4}に対して、取得します。
     *　@param cont コンテキスト。このコンテキストに対して、値を取得します。
     *  @return スコア 単位ms
     */
    public static long getScore4x4(final Context cont) {
        SharedPreferences sp = cont.getSharedPreferences(PREF, 0);
        return sp.getLong(KEY_4X4, DEFAULT_VAL);
    }

    /**
     * フォーマットされた文字列をかえします.
     * 引数のmsecをhour:minuts:sec の文字列で返します。
     * ミリsecは無視されます.
     *　@param time フォーマットする値
     *  @return フォーマットされた文字列
     */
    public static String format(final long time) {
        java.text.DecimalFormat df2 = new java.text.DecimalFormat("00");
        int scd = (int) (time / 1000 % 60);
        int mini = (int) (time / 1000 / 60 % 60);
        int hour = (int) (time / 1000 / 60 / 60);
        return hour + ":" + df2.format(mini) + ":" + df2.format(scd);
    }
}
