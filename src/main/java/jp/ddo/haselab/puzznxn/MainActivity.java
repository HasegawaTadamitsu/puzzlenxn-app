package jp.ddo.haselab.puzznxn;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.view.View;
import android.content.Intent;
import android.util.Log;

/**
 * 主処理Activity.
 * 主処理（最高スコアの表示、ゲーム難易度の選択）など行います。
 *
 * @author T.Hasegawa
 */
public final class MainActivity extends Activity implements OnClickListener {

    /**
     * create.
     * 各種ボタンのイベント登録などを行います。
     * @param savedInstanceState hmm
     */
    @Override
        protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	Log.v("hoge","start");
        setContentView(R.layout.main);

        Button button;
        button = (Button) findViewById(R.id.button_3x3);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.button_4x4);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.button_config);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.button_quit);
        button.setOnClickListener(this);

        TextView text;
        text = (TextView) findViewById(R.id.bestscore_3x3);
        text.setText(ScoreMgr.format(ScoreMgr.getScore3x3(this)));
        text = (TextView) findViewById(R.id.bestscore_4x4);
        text.setText(ScoreMgr.format(ScoreMgr.getScore4x4(this)));
    }

    /**
     * クリック時の処理.
     * 各種ボタンの処理を行います。
     * @param v 押されたview
     */
    @Override
        public void onClick(final View v) {
        int id = v.getId();
        if (id == R.id.button_quit) {
            finish();
            return;
        }
        if (id == R.id.button_config) {
            Intent intent = new Intent(this, ConfigActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        int size = 3;
        if (id == R.id.button_4x4) {
            size = 4;
        }
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GameActivity.SIZE_KEY, size);
        startActivity(intent);
        finish();
        return;
    }
}
