package jp.ddo.haselab.puzznxn;

import android.widget.TextView;
import android.util.Log;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;

/**
 * ScoreView.
 * Score表示を行います. 別スレッドをつくり
 * viewを更新させます.
 *
 * @author T.Hasegawa
 */
final class ScoreView implements Handler.Callback{

    /**
     * 表示する部品.
     */
    private TextView text;

    /**
     * ハンドラ.
     */
    private Handler handler;

    /**
     * 終了Flag.
     * これが trueになると終了します.
     */
    private boolean isEnd = false;
    
    /**
     * 開始時間です.
     * 現在時刻をmsec で表したものです.
     */
    private long startTimeMillis = 0;

    /** 
     * 部品更新間隔. 
     * 単位ms
     */
    private static final int INTERVAL_TIME_MILLIS = 100;

    /**
     * コンストラクタ.
     * @param setTextがある部品
     */
    public ScoreView(final TextView argTextView) {
        text = argTextView;
	handler = new Handler(this);
    }
    
    /**
     * 現在時刻を元にtextに経過時間を設定します.
     */
    private void drawText(){
	Log.v("SC","drawText");
        long val = System.currentTimeMillis() - startTimeMillis;
        String str = ScoreMgr.format(val);
	
	text.invalidate();
	text.setText(str);
    }

    /**
     * INTERVAL_TimE_MILLISごとに実行される処理.
     * @param msg 未使用.
     * @retrun 常にtrue
     */
    @Override
    public boolean handleMessage(final Message msg) {
	drawText();
	if( !isEnd ){
	    handler.sendMessageDelayed(handler.obtainMessage(0),
				       INTERVAL_TIME_MILLIS);
	}
	return true;
    }
    
    /**
     * 計測開始.
     * これより計測を初め、部品を更新します。
     */
    public void start() {
        isEnd = false;
        startTimeMillis = System.currentTimeMillis();
	handler.sendEmptyMessage(0);
	Log.v("SC","start");
    }

    /**
     * 計測停止.
     */
    public long getScore(){
        isEnd = true;
        return  System.currentTimeMillis() -  startTimeMillis;
    }
}
