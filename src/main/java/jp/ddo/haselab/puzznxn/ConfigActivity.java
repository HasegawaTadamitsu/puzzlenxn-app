package jp.ddo.haselab.puzznxn;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Context;

/**
 * 設定Activity.
 * 最高スコアの削除など各種設定画面のactivityです.
 *
 * @author T.Hasegawa
 */
public final class ConfigActivity extends Activity implements OnClickListener {

    /**
     * コンテキスト.
     * スコア管理などで使うため保持しておきます
     */
    private Context context;

    /**
     * create.
     * 各種ボタンのイベント登録などを行います。
     * @param savedInstanceState hmm
     */
    @Override
        protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config);
        context = (Context) this;

        Button btn;
        btn = (Button) findViewById(R.id.button_back_main);
        btn.setOnClickListener(this);
        btn = (Button) findViewById(R.id.button_clear_score);
        btn.setOnClickListener(this);
    }

    /**
     * 最高スコア削除のダイアログ処理.
     * 最高スコア削除のダイアログ処理を行います。
     *
     * OKボタン押下なら最高スコアの初期化を行います.
     */
    private void dialogClearScore() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.config_clear_score_title);
        builder.setMessage(R.string.config_clear_score_msg);
        builder.setPositiveButton("OK",
          new DialogInterface.OnClickListener() {
              public void onClick(final DialogInterface dialog,
                                  final int whichButton) {
                  ScoreMgr.initScore(context);
                return;
              }
          }
                                  );
        builder.setNegativeButton("No", null);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        return;
    }

    /**
     * クリック時の処理.
     * クリックされた時のイベントです.
     * mainへ戻るボタンならば、mainのactivityを呼びます.
     * clear scoreならば,最高スコアの初期化を呼び出します。
     * @param v 押されたview
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
        if (id == R.id.button_clear_score) {
            dialogClearScore();
            return;
        }
    }
}
