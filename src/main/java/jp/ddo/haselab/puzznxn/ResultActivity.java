package jp.ddo.haselab.puzznxn;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.view.View;
import android.content.Intent;

/**
 * ResultActivity.
 * 完成後のActivityです。
 *
 * @author T.Hasegawa
 */
public final class ResultActivity extends Activity implements OnClickListener {

    /**
     * create.
     * 各種ボタンのイベント登録などを行います。
     * @param savedInstanceState hmm
     */
    @Override
        protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        Bundle extra = getIntent().getExtras();
        int size   = extra.getInt(GameActivity.SIZE_KEY);
        long score = extra.getLong(GameActivity.SCORE_KEY);

        Button btn = (Button) findViewById(R.id.button_back_main);
        btn.setOnClickListener(this);

        TextView textYourScore = (TextView) findViewById(R.id.your_score);
        textYourScore.setText(ScoreMgr.format(score));

        long bestScore = ScoreMgr.getScore(this, size);
        TextView textBestScore = (TextView) findViewById(R.id.best_score);
        textBestScore.setText(ScoreMgr.format(bestScore));

        if (bestScore > score) {
            ScoreMgr.setScore(this, score, size);
        }
    }

    /**
     * クリック時の処理.
     * mainに戻る処理です。
     * @param argView 押されたview
     */
    @Override
        public void onClick(final View v) {
        int id = v.getId();
        if (id == R.id.button_back_main) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }
    }
}
