package jp.ddo.haselab.puzznxn;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.View;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.content.res.Resources;
import android.content.Intent;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
//import android.util.Log;

/**
 * GameActivity.
 * ゲームメインのActiviytです.
 *
 * @author T.Hasegawa
 */
public final class GameActivity extends Activity
    implements android.view.View.OnClickListener {

    /** スコアです. */
    private ScoreView score = null;

    /** 各ピースのイメージです. */
    private Bitmap[] piece;

    /** ブランクのピースイメージです. */
    private Bitmap   blankPiece;

    /**
     * ゲームの列です.
     * デフォルト値は３で、onCreateにて変更されます.
     */
    private int row = 3;

    /**
     * ゲームの行です.
     *　基本列と同じ値が設定されていますが、変更は可能です。
     */
    private int line = 3;

    /**
     * puzzleのインスタンス.
     * このインスタンスに対してゲームを行います
     */
    private Puzzle puzzle;

    /**
     * シャッフル数.
     * この回数シャッフルを行います。row/lineを多くしたとき、
     *　あまり混ざっていないようでしたら、値を考慮する必要があります。
     */
    private static final int SHUFFLE_COUNT = 100;

    /**
     * 画面遷移の引き継ぎキー.
     * rowの値を引き継ぐときのキーです。
     * 現時点では、列と行が等しくsizeとして定義しています。
     */
    public static final String SIZE_KEY  = "puzzle_SIZE";

    /**
     * 画面遷移の引き継ぎキー.
     * 次画面へスコアを引き継ぎます。
     */
    public static final String SCORE_KEY = "puzzle_SCORE";


    /**
     * create.
     * 各種ボタンのイベント登録などを行います。
     * @param savedInstanceState hmm
     */
    @Override
        protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extra = getIntent().getExtras();
        int size = extra.getInt(SIZE_KEY);
        row = size;
        line = size;

        setupBitmap(R.drawable.default_image);
        setContentView(createView());

        puzzle = new Puzzle(row, line);

        initGame();

    }

    /**
     * ゲームの初期化.
     * ゲームを初期化し、書き換えを呼びます。
     */
    private void initGame() {
        puzzle.shuffle(SHUFFLE_COUNT);
        drawPiece();
	score.start();
    }

    /**
     * ピースを書く.
     * puzzleのブロックを元にピースを書きます。
     */
    private void drawPiece() {
        int[] block = puzzle.getBlock();
        for (int i = 0; i < row * line; i++) {
            ImageView iv = (ImageView) findViewById(i);
            if (block[i] == puzzle.BLANK) {
                iv.setImageBitmap(blankPiece);
            } else {
                iv.setImageBitmap(piece[block[i]]);
            }
        }
    }

    /**
     * bitmap(ピース)の初期化.
     * ピースを初期化します。
     * @param argResourceId このリソースIDを元にbitmapを取得します
     */
    private void setupBitmap(final int argResourceId) {
        piece = new Bitmap[row * line];
        Resources r = getResources();
        Bitmap image = BitmapFactory.decodeResource(r, argResourceId);
        int width  = image.getWidth();
        int height = image.getHeight();
        int widthSize = width  / row;
        int heightSize = height / line;
        blankPiece = Bitmap.createBitmap(widthSize,
                                          heightSize,
                                          Bitmap.Config.ARGB_8888);


        for (int yCount = 0; yCount < line; yCount++) {
            for (int xCount = 0; xCount < row; xCount++) {
                int xLeftUp = xCount * widthSize;
                int yLeftUp = yCount * heightSize;
                piece[yCount * row + xCount] = Bitmap.createBitmap(
                                                    image,
                                                    xLeftUp,
                                                    yLeftUp,
                                                    widthSize,
                                                    heightSize);
            }
        }
    }

    /**
     * Viewの作成.
     * Viewを作成します。このview(レイアウト）を元にゲームを作ります。
     */
    private LinearLayout createView() {
        LinearLayout llRoot = new LinearLayout(this);
        llRoot.setOrientation(LinearLayout.VERTICAL);
        llRoot.setGravity(Gravity.CENTER);

        TextView scoreText = new TextView(this);
        score = new ScoreView(scoreText);
        llRoot.addView(scoreText);
	
        for (int yCount = 0; yCount < line; yCount++) {
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.setGravity(Gravity.CENTER_HORIZONTAL);
            for (int xCount = 0; xCount < row; xCount++) {
                ImageView iv = new ImageView(this);
                iv.setImageBitmap(blankPiece);
                iv.setId(yCount * row + xCount);
                iv.setOnClickListener(this);
                ll.addView(iv);
            }
            llRoot.addView(ll);
        }
        return llRoot;
    }

    /**
     * Menuの作成.
     * Menuの作成します.
     * @param menu メニュー
     */
    @Override
        public boolean onCreateOptionsMenu(final Menu menu) {
                super.onCreateOptionsMenu(menu);

                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.game, menu);
                return true;
        }


    /**
     * 終了ダイアログの作成／処理.
     * 終了ダイアログの作成／処理します。
     * OKならばゲーム終了させます。
     */
    private void dialogQuit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.menu_game_quit_sure_title);
        builder.setMessage(R.string.menu_game_quit_sure_msg);
        builder.setPositiveButton("OK",
          new DialogInterface.OnClickListener() {
              public void onClick(final DialogInterface dialog,
                                  final int whichButton) {
                finish();
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
     * シャッフルダイアログの作成／処理.
     * シャッフルダイアログの作成／処理します。
     * OKならば再度ゲームを初期化させます。
     */
    private void dialogShuffle() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.menu_game_shuffle_sure_title);
        builder.setMessage(R.string.menu_game_shuffle_sure_msg);
        builder.setPositiveButton("OK",
          new DialogInterface.OnClickListener() {
              public void onClick(final DialogInterface dialog,
                                  final int whichButton) {
                  initGame();
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
     * menu処理の分岐.
     * menu押下時の処理です。終了・シャッフルなどあります。
     * @param item 押下されたitem
     */
    @Override
        public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_quit:
            dialogQuit();
            return true;
        case R.id.menu_shuffle:
            dialogShuffle();
            return true;
        default:
            return false;
        }
    }

    /**
     * クリック時の処理.
     * ピースを押された場合、移動処理を行います。
     *　またその後、完成されているかチェックし、完成された場合、
     *　次のactivityに遷移します。
     * @param argView 押されたview
     */
    @Override
        public void onClick(final View argView) {
        boolean ret = puzzle.moveFromPos(argView.getId());
        if (!ret) {
            return;
        }
        drawPiece();
        if (puzzle.isComplete()) {
            long now_score = score.getScore();

            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra(GameActivity.SIZE_KEY,  row);
            intent.putExtra(GameActivity.SCORE_KEY, now_score);
            startActivity(intent);
            finish();
        }
    }

}
